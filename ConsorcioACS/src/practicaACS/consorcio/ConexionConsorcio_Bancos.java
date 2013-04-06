package practicaACS.consorcio;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;

import fap.*;

/**
 * Representa cada sesion establecida entre BANCO-CONSORCIO
 *
 */
public class ConexionConsorcio_Bancos extends Thread {

	
	static private int num_next_message = 0;
	
	private Consorcio consorcio;
	private ServidorConsorcio_Bancos servidor;

	private Socket socket; //Socket de conexion con el servidor
	private int send_port; //Puerto al que enviar los mensajes

	
	public ConexionConsorcio_Bancos(Consorcio cons,ServidorConsorcio_Bancos s,Socket socket) {
		super();
		this.consorcio = cons;
		this.servidor = s;
		this.socket = socket;
		this.send_port = socket.getPort();
	}

	//FUNCIONES QUE ACCEDEN A LA BD
	private void setEstado_conexion_banco(String id_banco,EstadoSesion estado){
		this.consorcio.getDatabase().setEstado_conexion_banco(id_banco,estado);
	}			
			
	private EstadoSesion getEstado_conexion_banco(String id_banco){
		return this.consorcio.getDatabase().getEstado_conexion_banco(id_banco);
	}			
	
	private int getNext_canal(String id_banco){
		return this.consorcio.getDatabase().getNext_canal(id_banco);
	}
	
	private int getNum_canales(String id_banco){
		return this.consorcio.getDatabase().getNum_canales(id_banco);
	}
	
	private void setNum_canales(String id_banco, int num_canales){
		this.consorcio.getDatabase().setNum_canales(id_banco,num_canales);
	}
	
	private ArrayList<Integer> getCanales_ocupados(String id_banco){
		return this.consorcio.getDatabase().getCanales_ocupados(id_banco);
	}
	
	private void anahdir_ultimo_envio(Mensaje respuesta) {
		this.consorcio.getDatabase().anhadir_ultimo_envio(this.siguiente_canal(respuesta.getDestino()), respuesta);
	}
	//-----------------------------
	
	/**
	 * Devuelve True si la conexion esta ACTIVA y False en caso contrario
	 */
	public boolean consultar_protocolo(String id_banco){
		return this.getEstado_conexion_banco(id_banco).equals(EstadoSesion.ACTIVA);
	}
	/**
	 * Devuelve True si la conexion esta CERRADA y False en caso contrario
	 */
	public boolean isClosed(String id_banco){
		return  this.getEstado_conexion_banco(id_banco).equals(EstadoSesion.CERRADA);
	}
	
	/**
	 * Devuelve el siguiente canal disponible
	 */
	private int siguiente_canal(String id_banco){
		return (this.getNext_canal(id_banco)) % this.getNum_canales(id_banco);
	}

	/**
	 * Devuelve True si el canal esta ocupado y False en caso contrario
	 */
	public boolean isCanal_ocupado(String id_banco,int canal){
		ArrayList<Integer> lista = this.getCanales_ocupados(id_banco);
		return lista.contains(canal);
	}

	/**
	 * Funcion que ejecuta la conexión, comprueba que el primer mensaje es de inicio de sesión
	 * y en ese caso, se bloquea a la espera de una nueva conexión entrante que manejar.
	 */
	public void run() {
		try {
			Mensaje respuesta = null;
			Mensaje recibido = null;
			OutputStream os;
			ObjectOutputStream o_buffer;
			InputStream is;
			ObjectInputStream i_buffer;
			
			os = this.socket.getOutputStream();  
			o_buffer = new ObjectOutputStream(os);  
			is = this.socket.getInputStream();  
			i_buffer = new ObjectInputStream(is);  
			
			//Recibimos el mensaje
			recibido = (Mensaje) i_buffer.readObject();

			System.out.printf(recibido.toString());
			
			//Creamos la respuesta
			respuesta = generar_respuesta(recibido);

			if(respuesta != null){
				System.out.printf(respuesta.toString());
				anahdir_ultimo_envio(respuesta);

				//Enviamos el mensaje
				o_buffer.writeObject(respuesta);
				o_buffer.flush();
			}

			os.close();
			o_buffer.close();
			this.socket.close();
		}
		catch (Exception e) {
	    // manipular las excepciones
		}
	}
	
	
	/**
	 * Comprueba si no hay errores y devuelve el CodigosError adecuado	
	 */
	private CodigosError comprobar_errores(Mensaje m,int canal){
		String id_banco = m.getOrigen(); //el banco de donde ha llegado el mensaje
				
		//Solicitar ABRIR SESION y ya hay SESION ABIERTA
		if((m.getTipoMensaje().equals(CodigosMensajes.SOLABRIRSESION)) 
				&& (this.servidor.hasSesion(id_banco)))
			return CodigosError.YAABIERTA;
		//
	//	if()
	//		return CodigosError.FUERASEC;
		//
		if(isCanal_ocupado(id_banco, canal))
			return CodigosError.CANALOCUP;
		
		//Solicitar REANUDAR TRAFICO y no esta TRAFICO DETENIDO
		if((m.getTipoMensaje().equals(CodigosMensajes.SOLREANUDARTRAFICO))
				&& (!this.getEstado_conexion_banco(id_banco).equals(EstadoSesion.TRAFICO_DETENIDO)))
			return CodigosError.TRAFNODET;
		
		//Solicitar FIN RECUPERACION y no esta en RECUPERACION
		if((m.getTipoMensaje().equals(CodigosMensajes.SOLFINTRADICOREC))
				&& (!this.getEstado_conexion_banco(id_banco).equals(EstadoSesion.RECUPERACION)))
			return CodigosError.NORECUPERACION;
	
		//Solicitar OPERACION!=ABRIR SESION y no hay SESION ABIERTA
		if(!(m.getTipoMensaje().equals(CodigosMensajes.SOLABRIRSESION)) 
				&& (!this.servidor.hasSesion(id_banco)))
			return CodigosError.NOSESION;
	
		//	return CodigosError.OTRASCAUSAS;
			
		return CodigosError.CORRECTO;
	}
	
	/**
	 * Funcion que analiza el mensaje recibido y devuelve una respuesta si es necesario
	 * y null en caso de que no sea necesario responder.
	 */
	private Mensaje generar_respuesta(Mensaje recibido){
		Mensaje respuesta = null;
		EstadoSesion estado = null;
		
		switch(recibido.getTipoMensaje()){
			//MENSAJES DE CONTROL
			case SOLTRAFICOREC: //SOLICITAR recuperacion trafico (consorcio->banco)
				break;
			case SOLFINTRADICOREC: //SOLICITAR fin recuperacion trafico (consorcio->banco)
				break;
			case SOLABRIRSESION: //SOLICITAR abrir sesion (banco->consorcio)
				respuesta = manejar_abrir_sesion((SolAperturaSesion) recibido);
				break;
			case SOLDETENERTRAFICO: // SOLICITAR detener trafico (banco->consorcio)
				respuesta = manejar_detener_trafico((SolDetTrafico) recibido);
				break;
			case SOLREANUDARTRAFICO: //SOLICITAR reanudar trafico (banco->consorcio)
				respuesta = manejar_reanudar_trafico((SolReanTrafico) recibido);
 				break;
			case SOLCIERRESESION: // SOLICITAR cerrar sesion (banco->consorcio)
				respuesta = manejar_cerrar_sesion((SolCierreSesion) recibido);
				break;
			case TRAFICOREC: //recuperacion trafico (banco->consorcio)
				respuesta = iniciar_recuperacion((RespIniTraficoRecuperacion) recibido);
				break;
			case FINREC: //fin recuperacion trafico (banco->consorcio)
				respuesta = finalizar_recuperacion((RespFinTraficoRec) recibido);
				break;
			case ABRIRSESION: //abrir sesion (consorcio->banco)
				break;
			case DETENERTRAFICO: //detener trafico (consorcio->banco)
				break;
			case REANUDARTRAFICO: //reaundar trafico (consorcio->banco)
				break;
			case CIERRESESION: //cierre sesion (consorcio->banco)
				break;
				//MENSAJES DE DATOS
			case CONSULTARSALDO: //consultarsaldo (consorcio->banco)
				break;
			case CONSULTARMOVIMIENTOS://consultar movimientos (consorcio->banco)
				break;
			case REINTEGRO: //reintegro (consorcio->banco)
				break;
			case ABONO: //abono (consorcio->banco)
				break;
			case TRASPASO: //traspaso (consorcio->banco)
				break;
			case RESCONSULTARSALDO: //respuesta consultar saldo (banco->consorcio)
				//	procesar_consulta_saldo();
				break;
			case RESCONSULTAMOV: //respuesta consultar movimientos (banco->consorcio)
				//	procesar_consultar_movimiento();
				break;
			case RESREINTEGRO: //respuesta reintegro (banco->consorcio)
				//	procesar_reintegro();
				break;
			case RESABONO: //respuesta abono (banco->consorcio)
				//	procesar_abono();
				break;
			case RESTRASPASO: //respuesta traspaso (banco->consorcio)
				//	procesar_traspaso();
				break;
			default:
				System.out.printf("ERROR: Mensaje -" + recibido.getTipoMensaje().toString() + "- no reconocido.");
				break;
		}
		estado = this.getEstado_conexion_banco(recibido.getOrigen());

		if(estado.equals(EstadoSesion.TRAFICO_DETENIDO)){
			//devolver error
		}
		if(estado.equals(EstadoSesion.CERRADA) && !recibido.getTipoMensaje().equals(CodigosMensajes.ABRIRSESION)){
			//devolver error
		}

		
		return respuesta;
	}
	
	/**
	 * Abre la sesion con el banco
	 */
	private void abrir_sesion(SolAperturaSesion recibido){
		String id_banco = recibido.getOrigen(); //el banco del que viene la solicitud
		
    	Calendar time = Calendar.getInstance();
    	System.out.println("APERTURA: Sesion servidor Banco: "+ recibido.getOrigen() +" comenzada a las " + time.getTime());
 
    	this.setEstado_conexion_banco(id_banco,EstadoSesion.ACTIVA);
    	this.setNum_canales(id_banco,recibido.getNcanales());
    	this.send_port = Integer.getInteger(recibido.getPuerto());
	}
	
	/**
	 * Cierra la sesion con el banco
	 */
	private void cerrar_sesion(String id_banco){
    	Calendar time = Calendar.getInstance();
    	System.out.println("CIERRE: Sesion servidor Banco: "+ id_banco +" finalizado a las " + time.getTime());

    	while(/*aun queden mensajes*/){
    		//consultar ultimos envios en BD
    	}
		this.setEstado_conexion_banco(id_banco,EstadoSesion.CERRADA);
		//comprobar total de reintegros, abonos y traspasos
	}
	
	/**
	 * Detiene el trafico con el banco
	 */
	public void detener_trafico(String id_banco){
		Calendar time = Calendar.getInstance();
    	System.out.println("TRAFICO DETENIDO: Sesion servidor Banco: "+ id_banco +" comenzado a las " + time.getTime());
   
    	this.setEstado_conexion_banco(id_banco,EstadoSesion.TRAFICO_DETENIDO);
	}
	
	/**
	 * Reanuda el trafico con el banco
	 */
	private void reanudar_trafico(String id_banco){
		Calendar time = Calendar.getInstance();
    	System.out.println("TRAFICO REANUDADO: Sesion servidor Banco: "+ id_banco +" comenzado a las " + time.getTime());
    	
    	this.setEstado_conexion_banco(id_banco,EstadoSesion.ACTIVA);
	}
	
	/**
	 * Inicia el proceso de recuperacion
	 */
	private Mensaje iniciar_recuperacion(String id_banco){
		this.setEstado_conexion_banco(id_banco,EstadoSesion.RECUPERACION);
		//consultar ultimos envios en la BD y reenviarlos
		return null;
		// enviar SOLFINTRADICOREC (solicitud fin de trafico en recuperacion)
	}
	
	/**
	 * Termina el proceso de recuperacion
	 */
	private Mensaje finalizar_recuperacion(String id_banco){
		this.setEstado_conexion_banco(id_banco,EstadoSesion.ACTIVA);
		return null;		
	}
	
	
	/**
	 * LLama a abrir sesion y crea un mensaje respuesta para ello
	 */
	private RespAperturaSesion manejar_abrir_sesion(SolAperturaSesion recibido){
		
		//cabecera
		String origen = Integer.toString(this.consorcio.getId_consorcio());
		String destino = recibido.getOrigen();
		//cuerpo
		CodigosRespuesta cod_resp = CodigosRespuesta.CONSACEPTADA;
		CodigosError cod_error = comprobar_errores(recibido,0); //Comprueba si hay errores

		if(cod_error.equals(CodigosError.CORRECTO))
			this.abrir_sesion(recibido); //Ejecuta las tareas necesarias

		return new RespAperturaSesion(origen,destino,cod_resp,cod_error);
	}
	
	
	/**
	 * LLama a cerrar sesion y crea un mensaje respuesta para ello
	 */	
	private RespCierreSesion manejar_cerrar_sesion(SolCierreSesion recibido){
		
		//cabecera
		String origen = Integer.toString(this.consorcio.getId_consorcio());
		String destino = recibido.getOrigen();
		//cuerpo
		CodigosRespuesta cod_resp = CodigosRespuesta.CONSACEPTADA;
		CodigosError cod_error =  comprobar_errores(recibido,0);
		
		if(cod_error.equals(CodigosError.CORRECTO))
			this.cerrar_sesion(recibido.getOrigen()); //Ejecuta las tareas necesarias

    	return new RespCierreSesion(origen,destino,cod_resp,cod_error);
	}
	
	/**
	 * LLama a detener trafico y crea un mensaje respuesta para ello
	 */	
	private RespDetTrafico manejar_detener_trafico(SolDetTrafico recibido){

		//cabecera
		String origen = Integer.toString(this.consorcio.getId_consorcio());
		String destino = recibido.getOrigen();
		//cuerpo
		CodigosRespuesta cod_resp = CodigosRespuesta.CONSACEPTADA;
		CodigosError cod_error =  comprobar_errores(recibido,0);
		
		if(cod_error.equals(CodigosError.CORRECTO))
			this.detener_trafico(recibido.getOrigen()); //Ejecuta las tareas necesarias

		return new RespDetTrafico(origen,destino,cod_resp,cod_error);
	}	
	
	
	/**
	 * LLama a reanudar trafico y crea un mensaje respuesta para ello
	 */
	private RespReanTrafico manejar_reanudar_trafico(SolReanTrafico recibido){
		
    	//cabecera
		String origen = Integer.toString(this.consorcio.getId_consorcio());
		String destino = recibido.getOrigen();
		//cuerpo
		CodigosRespuesta cod_resp = CodigosRespuesta.CONSACEPTADA;
		CodigosError cod_error =  comprobar_errores(recibido,0);
		
		if(cod_error.equals(CodigosError.CORRECTO))
			this.reanudar_trafico(recibido.getOrigen());//Ejecuta las tareas necesarias
		
    	return new RespReanTrafico(origen,destino,cod_resp,cod_error);
	}
	
	
	
}
