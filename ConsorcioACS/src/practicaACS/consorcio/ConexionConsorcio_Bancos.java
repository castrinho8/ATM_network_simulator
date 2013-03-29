package practicaACS.consorcio;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;

import fap.*;

public class ConexionConsorcio_Bancos extends Thread {


	static private int num_next_message = 0;
	static private int next_channel = 0;
	
	private Consorcio consorcio;
	private ServidorConsorcio_Bancos servidor;

	private Socket socket;
	private int send_port;
	
	private EstadoSesion estado_conexion_banco;
	private int num_canales;
	
	public ConexionConsorcio_Bancos(Consorcio cons,ServidorConsorcio_Bancos s,Socket socket) {
		super();
		this.socket = socket;
		this.consorcio = cons;
		this.estado_conexion_banco = EstadoSesion.ACTIVA;
		this.servidor = s;
	}

	/**
	 * Devuelve el estado de la conexion con el banco
	 */
	public boolean consultar_protocolo(){
		return estado_conexion_banco.equals(EstadoSesion.ACTIVA);
	}
	
	/**
	 * Devuelve el siguiente canal disponible
	 */
	private int siguiente_canal(){
		return (this.next_channel++) % this.num_canales;
	}

	/**
	 * Devuelve True si el canal esta ocupado y False en caso contrario
	 */
	public boolean isCanal_ocupado(int canal){
		return false;
	}

	/**
	 * Funcion que ejecuta la conexión, comprueba que el primer mensaje es de inicio de sesión
	 * y en ese caso, se bloquea a la espera de una nueva conexión entrante que manejar.
	 */
	public void run() {
		try {
			boolean first = true;
	    	ServerSocket servidor = new ServerSocket();

			Mensaje respuesta = null;
			Mensaje recibido = null;
			OutputStream os;
			ObjectOutputStream o_buffer;
			InputStream is;
			ObjectInputStream i_buffer;
			
			do{
				if(!first)
					this.socket = servidor.accept();

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
					this.consorcio.getDatabase().anhadir_ultimo_envio(siguiente_canal(), respuesta);
					
					//Enviamos el mensaje
					o_buffer.writeObject(respuesta);
					o_buffer.flush();
				}

				first = false;

			}while(this.consultar_protocolo());
			
			servidor.close();
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
		
		//Solicitar ABRIR SESION y ya hay SESION ABIERTA
		if((m.getTipoMensaje().equals(CodigosMensajes.SOLABRIRSESION)) 
				&& (this.servidor.hasSesion(m.getOrigen())))
			return CodigosError.YAABIERTA;
		//
	//	if()
	//		return CodigosError.FUERASEC;
		//
		if(isCanal_ocupado(canal))
			return CodigosError.CANALOCUP;
		
		//Solicitar REANUDAR TRAFICO y no esta TRAFICO DETENIDO
		if((m.getTipoMensaje().equals(CodigosMensajes.SOLREANUDARTRAFICO))
				&& (!this.estado_conexion_banco.equals(EstadoSesion.TRAFICO_DETENIDO)))
			return CodigosError.TRAFNODET;
		
		//Solicitar FIN RECUPERACION y no esta en RECUPERACION
		if((m.getTipoMensaje().equals(CodigosMensajes.SOLFINTRADICOREC))
				&& (!this.estado_conexion_banco.equals(EstadoSesion.RECUPERACION)))
			return CodigosError.NORECUPERACION;
	
		//Solicitar OPERACION!=ABRIR SESION y no hay SESION ABIERTA
		if(!(m.getTipoMensaje().equals(CodigosMensajes.SOLABRIRSESION)) 
				&& (!this.servidor.hasSesion(m.getOrigen())))
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
		if(this.consultar_protocolo())
		switch(recibido.getTipoMensaje()){
			//MENSAJES DE CONTROL
			case SOLTRAFICOREC: //SOLICITAR recuperacion trafico (consorcio->banco)
				break;
			case SOLFINTRADICOREC: //SOLICITAR fin recuperacion trafico (consorcio->banco)
				break;
			case SOLABRIRSESION: //SOLICITAR abrir sesion (banco->consorcio)
				respuesta = abrir_sesion((SolAperturaSesion) recibido);
				break;
			case SOLDETENERTRAFICO: // SOLICITAR detener trafico (banco->consorcio)
				respuesta = detener_trafico((SolDetTrafico) recibido);
				break;
			case SOLREANUDARTRAFICO: //SOLICITAR reanudar trafico (banco->consorcio)
				respuesta = reanudar_trafico((SolReanTrafico) recibido);
 				break;
			case SOLCIERRESESION: // SOLICITAR cerrar sesion (banco->consorcio)
				respuesta = cerrar_sesion((SolCierreSesion) recibido);
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
		return respuesta;
	}
	
	/**
	 * Abre la sesion con el banco
	 */
	private RespAperturaSesion abrir_sesion(SolAperturaSesion recibido){
    	Calendar time = Calendar.getInstance();
    	System.out.println("APERTURA: Sesion servidor Banco: "+ recibido.getOrigen() +" comenzada a las " + time.getTime());
 
    	this.estado_conexion_banco = EstadoSesion.ACTIVA;
    	this.num_canales = recibido.getNcanales();
    	this.send_port = Integer.getInteger(recibido.getPuerto());
    	
		//cabecera
		String origen = Integer.toString(this.consorcio.getId_consorcio());
		String destino = recibido.getOrigen();
		//cuerpo
		CodigosRespuesta cod_resp = CodigosRespuesta.CONSACEPTADA;
		CodigosError cod_error = comprobar_errores(recibido,0);

		return new RespAperturaSesion(origen,destino,cod_resp,cod_error);
	}
	
	/**
	 * Cierra la sesion con el banco
	 */
	private RespCierreSesion cerrar_sesion(SolCierreSesion recibido){
    	Calendar time = Calendar.getInstance();
    	System.out.println("CIERRE: Sesion servidor Banco: "+ recibido.getOrigen() +" finalizado a las " + time.getTime());

		this.estado_conexion_banco = EstadoSesion.CERRADA;

		//cabecera
		String origen = Integer.toString(this.consorcio.getId_consorcio());
		String destino = recibido.getOrigen();
		//cuerpo
		CodigosRespuesta cod_resp = CodigosRespuesta.CONSACEPTADA;
		CodigosError cod_error =  comprobar_errores(recibido,0);
		
    	return new RespCierreSesion(origen,destino,cod_resp,cod_error);
	}
	
	/**
	 * Detiene el trafico con el banco
	 */
	private void detener_trafico(SolDetTrafico recibido){
		Calendar time = Calendar.getInstance();
    	System.out.println("TRAFICO DETENIDO: Sesion servidor Banco: "+ recibido.getOrigen() +" comenzado a las " + time.getTime());
   
    	this.estado_conexion_banco = EstadoSesion.TRAFICO_DETENIDO;
	}
	
	/**
	 * Reanuda el trafico con el banco
	 */
	private RespReanTrafico reanudar_trafico(SolReanTrafico recibido){
		Calendar time = Calendar.getInstance();
    	System.out.println("TRAFICO REANUDADO: Sesion servidor Banco: "+ recibido.getOrigen() +" comenzado a las " + time.getTime());
    	
    	this.estado_conexion_banco = EstadoSesion.ACTIVA;

    	//cabecera
		String origen = Integer.toString(this.consorcio.getId_consorcio());
		String destino = recibido.getOrigen();
		//cuerpo
		CodigosRespuesta cod_resp = CodigosRespuesta.CONSACEPTADA;
		CodigosError cod_error =  comprobar_errores(recibido,0);
		
    	return new RespReanTrafico(origen,destino,cod_resp,cod_error);
	}
	
	/**
	 * Inicia el proceso de recuperacion
	 */
	private Mensaje iniciar_recuperacion(RespIniTraficoRecuperacion recibido){
		this.estado_conexion_banco = EstadoSesion.RECUPERACION;
		//
		return null;
	}
	
	/**
	 * Termina el proceso de recuperacion
	 */
	private Mensaje finalizar_recuperacion(RespFinTraficoRec recibido){
		this.estado_conexion_banco = EstadoSesion.ACTIVA;
		return null;		
	}
	
	private RespDetTrafico manejar_detener_trafico(SolDetTrafico recibido){
		//Ejecuta las tareas necesarias
		detener_trafico(recibido);

		//Crea y envia el mensaje
		
		//cabecera
		String origen = Integer.toString(this.consorcio.getId_consorcio());
		String destino = recibido.getOrigen();
		//cuerpo
		CodigosRespuesta cod_resp = CodigosRespuesta.CONSACEPTADA;
		CodigosError cod_error =  comprobar_errores(recibido,0);
		
		return new RespDetTrafico(origen,destino,cod_resp,cod_error);
	}	
}
