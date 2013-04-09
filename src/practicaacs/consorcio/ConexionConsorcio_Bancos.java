package practicaacs.consorcio;

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

import practicaacs.banco.estados.EstadoSesion;
import practicaacs.fap.*;

/**
 * Representa cada sesion establecida entre BANCO-CONSORCIO
 *
 */
public class ConexionConsorcio_Bancos extends Thread {

	
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

	/**
	 * Devuelve True si la conexion esta ACTIVA y False en caso contrario
	 */
	public boolean consultar_protocolo(String id_banco){
		return Database_lib.getInstance().getEstado_conexion_banco(id_banco).equals(EstadoSesion.ACTIVA);
	}
	/**
	 * Devuelve True si la conexion esta CERRADA y False en caso contrario
	 */
	public boolean isClosed(String id_banco){
		return  Database_lib.getInstance().getEstado_conexion_banco(id_banco).equals(EstadoSesion.CERRADA);
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
				Database_lib.getInstance().anhadir_ultimo_envio(respuesta.getDestino(), respuesta);

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
	private CodigosError comprobar_errores(Mensaje m,int canal,int num_mensaje){
		String id_banco = m.getOrigen(); //el banco de donde ha llegado el mensaje
				
		//Solicitar ABRIR SESION y ya hay SESION ABIERTA
		if((m.getTipoMensaje().equals(CodigosMensajes.SOLABRIRSESION)) 
				&& (Database_lib.getInstance().hasSesion(id_banco)))
			return CodigosError.YAABIERTA;
		
		//El numero de mensaje entrante es != del que deberia ser (deberia recibirse el mismo numero de mensaje que se envia)
		if(num_mensaje != Database_lib.getInstance().getNext_num_message(id_banco, canal))
			return CodigosError.FUERASEC;
		
		//Solicitar utilizar un canal que aun no ha obtenido respuesta (que esta ocupado)
		if(Database_lib.getInstance().isCanal_ocupado(id_banco,canal))
			return CodigosError.CANALOCUP;
		
		//Solicitar REANUDAR TRAFICO y no esta TRAFICO DETENIDO
		if((m.getTipoMensaje().equals(CodigosMensajes.SOLREANUDARTRAFICO))
				&& (!Database_lib.getInstance().getEstado_conexion_banco(id_banco).equals(EstadoSesion.TRAFICO_DETENIDO)))
			return CodigosError.TRAFNODET;
		
		//Solicitar FIN RECUPERACION y no esta en RECUPERACION
		if((m.getTipoMensaje().equals(CodigosMensajes.SOLFINREC))
				&& (!Database_lib.getInstance().getEstado_conexion_banco(id_banco).equals(EstadoSesion.RECUPERACION)))
			return CodigosError.NORECUPERACION;
	
		//Solicitar OPERACION!=ABRIR SESION y no hay SESION ABIERTA
		if(!(m.getTipoMensaje().equals(CodigosMensajes.SOLABRIRSESION)) 
				&& (Database_lib.getInstance().hasSesion(id_banco)))
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
			case SOLINIREC: //SOLICITAR recuperacion trafico (consorcio->banco)
				break;
			case SOLFINREC: //SOLICITAR fin recuperacion trafico (consorcio->banco)
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
			case RESINIREC: //recuperacion trafico (banco->consorcio)
				respuesta = iniciar_recuperacion((RespIniTraficoRecuperacion) recibido);
				break;
			case RESFINREC: //fin recuperacion trafico (banco->consorcio)
				respuesta = finalizar_recuperacion((RespFinTraficoRec) recibido);
				break;
			case RESABRIRSESION: //abrir sesion (consorcio->banco)
				break;
			case RESDETENERTRAFICO: //detener trafico (consorcio->banco)
				break;
			case RESREANUDARTRAFICO: //reaundar trafico (consorcio->banco)
				break;
			case RESCIERRESESION: //cierre sesion (consorcio->banco)
				break;
				//MENSAJES DE DATOS
			case SOLSALDO: //consultarsaldo (consorcio->banco)
				break;
			case SOLMOVIMIENTOS://consultar movimientos (consorcio->banco)
				break;
			case SOLREINTEGRO: //reintegro (consorcio->banco)
				break;
			case SOLABONO: //abono (consorcio->banco)
				break;
			case SOLTRASPASO: //traspaso (consorcio->banco)
				break;
			case RESSALDO: //respuesta consultar saldo (banco->consorcio)
				//	procesar_consulta_saldo();
				break;
			case RESMOVIMIENTOS: //respuesta consultar movimientos (banco->consorcio)
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
		
		//En caso de que no haya sesion activa y el mensaje recibido no sea una consulta, se almacena para enviar futuramente.
		if((!Database_lib.getInstance().hasSesion(recibido.getOrigen())) && (!recibido.es_consulta())){
				//almacenar en DB con marca offline el mensaje a enviar
		}
		return respuesta;
	}
	
	/**
	 * Abre la sesion con el banco
	 */
	private void abrir_sesion(SolAperturaSesion recibido){
		String id_banco = recibido.getOrigen(); //el banco del que viene la solicitud
		
    	Calendar time = Calendar.getInstance();
    	System.out.println("APERTURA: Enviada a las " + recibido.getTime());
    	System.out.println("APERTURA: Sesion servidor Banco: "+ recibido.getOrigen() +" comenzada a las " + time.getTime());

		//Guardamos los datos en la base de datos
		Database_lib.getInstance().insertar_sesion(id_banco,recibido.getPuerto(),recibido.getNcanales(),EstadoSesion.ACTIVA);
	}
	
	/**
	 * Cierra la sesion con el banco
	 */
	private boolean cerrar_sesion(String id_banco,long reintegros,long abonos,long traspasos){
    	Calendar time = Calendar.getInstance();
    	System.out.println("CIERRE: Sesion servidor Banco: "+ id_banco +" finalizado a las " + time.getTime());

    	//while(/*aun queden mensajes*/){
    		//consultar ultimos envios en BD
    	//}
    	
    	//Seteamos el estado de la conexion a cerrada
    	Database_lib.getInstance().setEstado_conexion_banco(id_banco,EstadoSesion.CERRADA);

    	//Comprobar total de reintegros, abonos y traspasos
    	return Database_lib.getInstance().comprueba_cuentas(id_banco,reintegros,abonos,traspasos);
	}
	
	/**
	 * Detiene el trafico con el banco
	 */
	public void detener_trafico(String id_banco){
		Calendar time = Calendar.getInstance();
    	System.out.println("TRAFICO DETENIDO: Sesion servidor Banco: "+ id_banco +" comenzado a las " + time.getTime());
   
    	Database_lib.getInstance().setEstado_conexion_banco(id_banco,EstadoSesion.TRAFICO_DETENIDO);
	}
	
	/**
	 * Reanuda el trafico con el banco
	 */
	private void reanudar_trafico(String id_banco){
		Calendar time = Calendar.getInstance();
    	System.out.println("TRAFICO REANUDADO: Sesion servidor Banco: "+ id_banco +" comenzado a las " + time.getTime());
    	
    	Database_lib.getInstance().setEstado_conexion_banco(id_banco,EstadoSesion.ACTIVA);
	}
	
	/**
	 * Inicia el proceso de recuperacion
	 */
	private void iniciar_recuperacion(RespIniTraficoRec recibido){
		String id_banco = recibido.getOrigen();
		
		Database_lib.getInstance().setEstado_conexion_banco(id_banco,EstadoSesion.RECUPERACION);

		//consultar ultimos envios en la BD y reenviarlos
		for(Mensaje m : Database_lib.getInstance().recupera_mensajes(id_banco)){
			this.consorcio.getBancos_client().send_message(m);
		}
		// enviar SOLFINTRADICOREC (solicitud fin de trafico en recuperacion)
	}
	
	/**
	 * Termina el proceso de recuperacion
	 */
	private Mensaje finalizar_recuperacion(RespFinTraficoRec recibido){
		String id_banco = recibido.getOrigen();

		Database_lib.getInstance().setEstado_conexion_banco(id_banco,EstadoSesion.ACTIVA);
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
		CodigosRespuesta cod_resp = Database_lib.getInstance().hasSesion(destino) ? CodigosRespuesta.CONSDEN:CodigosRespuesta.CONSACEPTADA;
		CodigosError cod_error = comprobar_errores(recibido,0,recibido.getNmsg()); //Comprueba si hay errores

		if((cod_error.equals(CodigosError.CORRECTO)) && cod_resp.equals(CodigosRespuesta.CONSACEPTADA))
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
		CodigosRespuesta cod_resp = Database_lib.getInstance().hasSesion(destino) ? CodigosRespuesta.CONSACEPTADA:CodigosRespuesta.CONSDEN;
		CodigosError cod_error =  comprobar_errores(recibido,0,recibido.getNmsg());
		
		if(cod_error.equals(CodigosError.CORRECTO)) //Ejecuta las tareas necesarias
			this.cerrar_sesion(recibido.getOrigen(),recibido.getTotal_reintegros(),recibido.getAbonos(),recibido.getTraspasos()); 

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
		CodigosRespuesta cod_resp = Database_lib.getInstance().hasSesion(destino) ? CodigosRespuesta.CONSACEPTADA:CodigosRespuesta.CONSDEN;
		CodigosError cod_error =  comprobar_errores(recibido,0,recibido.getNmsg());
		
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
		CodigosRespuesta cod_resp = Database_lib.getInstance().hasSesion(destino) ? CodigosRespuesta.CONSACEPTADA:CodigosRespuesta.CONSDEN;;
		CodigosError cod_error =  comprobar_errores(recibido,0,recibido.getNmsg());
		
		if(cod_error.equals(CodigosError.CORRECTO))
			this.reanudar_trafico(recibido.getOrigen());//Ejecuta las tareas necesarias
		
    	return new RespReanTrafico(origen,destino,cod_resp,cod_error);
	}
	
	
	
}
