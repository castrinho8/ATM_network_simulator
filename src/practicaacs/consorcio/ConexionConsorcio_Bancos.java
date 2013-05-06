package practicaacs.consorcio;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import practicaacs.banco.estados.EstadoSesion;
import practicaacs.banco.estados.SesAberta;
import practicaacs.banco.estados.SesDetida;
import practicaacs.banco.estados.SesNonAberta;
import practicaacs.banco.estados.SesRecuperacion;
import practicaacs.consorcio.aux.Sesion;
import practicaacs.consorcio.bd.Database_lib;
import practicaacs.fap.*;

/**
 * Representa cada sesion establecida entre BANCO-CONSORCIO
 * Su función principal es manejar un paquete recibido y realizar las acciones correspondientes
 * al mensaje recibido, respondiendo a dicho mensaje si es necesario.
 *
 */
public class ConexionConsorcio_Bancos extends Thread {

	
	private Consorcio consorcio;
	private ServidorConsorcio_Bancos servidor;
	
	private DatagramSocket output_socket;
	private DatagramPacket input_packet;

	public ConexionConsorcio_Bancos(DatagramPacket paquete,Consorcio cons,ServidorConsorcio_Bancos s,DatagramSocket socket) {
		super();
		this.consorcio = cons;
		this.servidor = s;
		
		this.input_packet = paquete;
		this.output_socket =  socket;
	}

	
	/**
	 * Método que obtiene el Mensaje a partir del paquete y analiza dicho 
	 * mensaje para realizar la tarea adecuada.
	 */
	public void run() {
		try {
			//Creamos el mensaje correspondiente al recibido
			Mensaje recibido = Mensaje.parse(new String(this.input_packet.getData(),this.input_packet.getOffset(),this.input_packet.getLength()-1));
			System.out.printf(recibido.toString());
			
			//Analizamos el mensaje y realizamos las acciones correspondientes
			analizar_mensaje(recibido);
		}
		catch (Exception e) {
	    // manipular las excepciones
		}
	}
	
	/**
	 * Método que envia la respuesta del BANCO al CAJERO.
	 * Cambia el origen/destino, guarda el mensaje en la BD, obtiene IP y puerto para el envio
	 *  y delega en el Servidor de Cajeros para realizarlo.
	 * BANCO->CONSORCIO->CAJERO
	 * @param respuesta
	 */
	public void reply_message(MensajeDatos respuesta){
		
    	System.out.printf(respuesta.toString());

    	String id_banco = respuesta.getOrigen();
    	
    	//Cambiar origen y destino
		String destino = Database_lib.getInstance().getIdCajero(respuesta.getOrigen(),respuesta.getNumcanal());//Id_cajero
		String origen = this.consorcio.getId_consorcio(); //Id_consorcio
		respuesta.setDestino(destino);
		respuesta.setOrigen(origen);
		
		//Guardamos el Mensaje en la BD (Tabla de MENSAJES)
		Database_lib.getInstance().almacenar_mensaje(respuesta,true);
		
		//Obtenemos la direccion y el puerto a donde enviar
		InetAddress ip = Database_lib.getInstance().getIpEnvio(id_banco,respuesta.getNumcanal());
		int puerto = Database_lib.getInstance().getPortEnvio(id_banco,respuesta.getNumcanal());

		
		this.consorcio.getCajeros_server().reply_message(respuesta,ip,puerto);
	}
	
	/**
	 * Método que envia el mensaje introducido por parámetro a traves del socket de la conexión
	 * que invoca el método.
	 * @param envio El mensaje a enviar.
	 */
	private void enviar_mensaje(Mensaje envio){
		
		System.out.printf(envio.toString());
		int canal = Database_lib.getInstance().seleccionarCanal(envio.getDestino()); //escoger el canal
		
		//Creamos el datagrama
		DatagramPacket enviarPaquete = new DatagramPacket(envio.getBytes(),envio.size(),this.input_packet.getAddress(),this.input_packet.getPort());
		
		//Guardamos el mensaje en la BD (Tabla de ULTIMOS ENVIOS)
		Database_lib.getInstance().anhadir_ultimo_envio(envio,this.input_packet.getAddress(),this.input_packet.getPort());

		try{
			//Enviamos el mensaje
			this.output_socket.send(enviarPaquete);
			/*			
			if(envio.es_datos()){
				//Obtenemos la sesion y creamos un timer
				Sesion s = servidor.getSesion(envio.getDestino());
				s.setTimer(canal);
			}*/
		}catch (IOException e) {
			System.out.println("Error al enviar");
			System.exit ( 0 );
		}

	}
	
	/**
	 * Comprueba si no hay errores y devuelve el CodigosError adecuado	
	 * @param m El mensaje a analizar
	 * @param canal El canal por el que se recibe
	 * @return El código de error correspondiente, en caso de que no haya error devuelve CORRECTO.
	 */
	private CodigosError comprobar_errores(Mensaje m,int canal){
		String id_banco = m.getOrigen(); //el banco de donde ha llegado el mensaje
				
		//Solicitar ABRIR SESION y ya hay SESION ABIERTA
		if((m.getTipoMensaje().equals(CodigosMensajes.SOLABRIRSESION)) 
				&& (Database_lib.getInstance().hasSesion(id_banco)))
			return CodigosError.YAABIERTA;
		
		//El numero de mensaje entrante es != del que deberia ser (deberia recibirse el mismo numero de mensaje que se envia)
		if((Database_lib.getInstance().isContestado(id_banco,canal)))
			return CodigosError.FUERASEC;
		
		//Solicitar utilizar un canal que aun no ha obtenido respuesta (que esta ocupado)
		if(Database_lib.getInstance().isCanal_ocupado(id_banco,canal))
			return CodigosError.CANALOCUP;
		
		//Solicitar REANUDAR TRAFICO y no esta TRAFICO DETENIDO
		if((m.getTipoMensaje().equals(CodigosMensajes.SOLREANUDARTRAFICO))
				&& (!Database_lib.getInstance().getEstado_conexion_banco(id_banco).equals(SesDetida.instance())))
			return CodigosError.TRAFNODET;
		
		//Solicitar FIN RECUPERACION y no esta en RECUPERACION
		if((m.getTipoMensaje().equals(CodigosMensajes.SOLFINREC))
				&& (!Database_lib.getInstance().getEstado_conexion_banco(id_banco).equals(SesRecuperacion.instance())))
			return CodigosError.NORECUPERACION;
	
		//Solicitar OPERACION!=ABRIR SESION y no hay SESION ABIERTA
		if(!(m.getTipoMensaje().equals(CodigosMensajes.SOLABRIRSESION)) 
				&& (Database_lib.getInstance().hasSesion(id_banco)))
			return CodigosError.NOSESION;
	
		//	return CodigosError.OTRASCAUSAS;
			
		return CodigosError.CORRECTO;
	}
	
	/**
	 * Método que analiza el mensaje y en función de su tipo, realiza las funciones adecuadas.
	 * @param recibido El mensaje recibido.
	 */
	private void analizar_mensaje(Mensaje recibido){
		
		switch(recibido.getTipoMensaje()){
			//MENSAJES DE CONTROL
			case SOLINIREC: //SOLICITAR recuperacion trafico (consorcio->banco)
				break;
			case SOLFINREC: //SOLICITAR fin recuperacion trafico (consorcio->banco)
				break;
			case SOLABRIRSESION: //SOLICITAR abrir sesion (banco->consorcio)
				manejar_abrir_sesion((SolAperturaSesion) recibido);
				break;
			case SOLDETENERTRAFICO: // SOLICITAR detener trafico (banco->consorcio)
				manejar_detener_trafico((SolDetTrafico) recibido);
				break;
			case SOLREANUDARTRAFICO: //SOLICITAR reanudar trafico (banco->consorcio)
				manejar_reanudar_trafico((SolReanTrafico) recibido);
 				break;
			case SOLCIERRESESION: // SOLICITAR cerrar sesion (banco->consorcio)
				manejar_cerrar_sesion((SolCierreSesion) recibido);
				break;
			case RESINIREC: //recuperacion trafico (banco->consorcio)
				iniciar_recuperacion(((RespIniTraficoRec) recibido).getOrigen());
				break;
			case RESFINREC: //fin recuperacion trafico (banco->consorcio)
				finalizar_recuperacion((RespFinTraficoRec) recibido);
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
				maneja_consulta_saldo((RespSaldo) recibido);
				break;
			case RESMOVIMIENTOS: //respuesta consultar movimientos (banco->consorcio)
				maneja_consultar_movimiento((RespMovimientos) recibido);
				break;
			case RESREINTEGRO: //respuesta reintegro (banco->consorcio)
				maneja_reintegro((RespReintegro) recibido);
				break;
			case RESABONO: //respuesta abono (banco->consorcio)
				maneja_abono((RespAbono) recibido);
				break;
			case RESTRASPASO: //respuesta traspaso (banco->consorcio)
				maneja_traspaso((RespTraspaso) recibido);
				break;
			default:
				System.out.printf("ERROR: Mensaje -" + recibido.getTipoMensaje().toString() + "- no reconocido.");
				break;
		}
	}

	/**
	 * LLama a abrir sesion y crea un mensaje respuesta para ello
	 * @param recibido El mensaje recibido.
	 */
	private void manejar_abrir_sesion(SolAperturaSesion recibido){

		//cabecera
		String origen = this.consorcio.getId_consorcio();
		String destino = recibido.getOrigen();
		//cuerpo
		boolean cod_resp = Database_lib.getInstance().hasSesion(destino);
		CodigosError cod_error = comprobar_errores(recibido,0,recibido.getNmsg()); //Comprueba si hay errores

		if(cod_error.equals(CodigosError.CORRECTO)){ //Si no ha habido errores, realizar las tareas necesarias
	    	Calendar time = Calendar.getInstance();
	    	System.out.println("APERTURA: Enviada a las " + recibido.getTime());
	    	System.out.println("APERTURA: Sesion servidor Banco: "+ recibido.getOrigen() +" comenzada a las " + time.getTime());
	
	    	//Añadimos al hashmap de Sesiones abiertas
	    	Sesion ses = new Sesion(destino,this.servidor,recibido.getNcanales());
	    	this.servidor.insertaSesion(ses);
	    	
			//Guardamos la sesion en la BD
			Database_lib.getInstance().abrir_sesion(destino,recibido.getPuerto(),recibido.getNcanales(),SesAberta.instance());
			
			//Obtenemos los mensajes offline de este banco.
			ArrayList<Mensaje> msj_offline = Database_lib.getInstance().getMensajesOffline(destino);
			
			//Envia la respuesta
			this.enviar_mensaje(new RespAperturaSesion(origen,destino,cod_resp,cod_error));
	
			//Enviamos los mensajes offline de este banco
			while(msj_offline.iterator().hasNext()){
				this.enviar_mensaje(msj_offline.iterator().next());
			}
		}
		else{ //Si hay errores respondemos con el error correspondiente
			//Envia la respuesta
			this.enviar_mensaje(new RespAperturaSesion(origen,destino,cod_resp,cod_error));
		}
	}
	
	/**
	 * Cierra la sesion con el banco
	 * @param id_banco El identificador del banco que quiere cerrar sesion
	 * @param reintegros El numero total de reintegros realizados por el banco en la sesión que se cierra.
	 * @param abonos El numero total de abonos realizados por el banco en la sesión que se cierra.
	 * @param traspasos El numero total de traspasos realizados por el banco en la sesión que se cierra.
	 * @return Devuelve un HashMap<String,Long> con cada uno de los valores de reintegros, abonos,traspasos realizados por el consorcio.
	 * 
	 */
	private HashMap<String,Long> cerrar_sesion(String id_banco,long reintegros,long abonos,long traspasos){
    	Calendar time = Calendar.getInstance();
    	System.out.println("CIERRE: Sesion servidor Banco: "+ id_banco +" finalizado a las " + time.getTime());

    	//while(/*aun queden mensajes*/){
    		//consultar ultimos envios en BD
    	//}
    	
    	//Obtenemos la suma de los ultimos movimientos
    	HashMap<String,Long> movimientos = new HashMap<String,Long>();
    	movimientos.put("Reintegros",Database_lib.getInstance().getNumReintegros(id_banco));
    	movimientos.put("Abonos",Database_lib.getInstance().getNumAbonos(id_banco));
    	movimientos.put("Traspasos",Database_lib.getInstance().getNumTraspasos(id_banco));
    	
    	//Eliminamos la sesion del hashmap de sesiones.
    	this.servidor.eliminarSesion(id_banco);
    	//Eliminar la sesion BD
    	Database_lib.getInstance().cerrar_sesion(id_banco);
    	
    	return movimientos;
	}
	
	/**
	 * Detiene el trafico con el banco
	 * @param id_banco El identificador del banco a detener.
	 */
	public void detener_trafico(String id_banco){
		Calendar time = Calendar.getInstance();
    	System.out.println("TRAFICO DETENIDO: Sesion servidor Banco: "+ id_banco +" comenzado a las " + time.getTime());
   
    	Database_lib.getInstance().setEstado_conexion_banco(id_banco,SesDetida.instance());
	}
	
	/**
	 * Reanuda el trafico con el banco
	 * @param id_banco El identificador del banco con el que reanudar el trafico.
	 */
	private void reanudar_trafico(String id_banco){
		Calendar time = Calendar.getInstance();
    	System.out.println("TRAFICO REANUDADO: Sesion servidor Banco: "+ id_banco +" comenzado a las " + time.getTime());
    	
    	Database_lib.getInstance().setEstado_conexion_banco(id_banco,SesAberta.instance());
	}
	
	/**
	 * Inicia el proceso de recuperacion
	 * @param recibido El mensaje recibido.
	 */
	public void iniciar_recuperacion(String id_banco){
		Database_lib.getInstance().setEstado_conexion_banco(id_banco,SesRecuperacion.instance());

		//consultar ultimos envios en la BD y reenviarlos
		for(Mensaje m : Database_lib.getInstance().recupera_ultimos_mensajes(id_banco)){
			//marcar en la BD el mensaje como enviado y no respondido
			this.enviar_mensaje(m);
		}
		// enviar SOLFINTRADICOREC (solicitud fin de trafico en recuperacion)
	}
	
	/**
	 * Termina el proceso de recuperacion
	 * @param recibido El mensaje recibido.
	 */
	private Mensaje finalizar_recuperacion(RespFinTraficoRec recibido){
		String id_banco = recibido.getOrigen();

		Database_lib.getInstance().setEstado_conexion_banco(id_banco,SesAberta.instance());
		return null;
	}
	
	
	/**
	 * LLama a cerrar sesion y crea un mensaje respuesta para ello
	 * @param recibido El mensaje recibido.
	 */	
	private RespCierreSesion manejar_cerrar_sesion(SolCierreSesion recibido){
		
		//Datos recibidos
		long total_reintegros = recibido.getTotal_reintegros();
		long total_abonos = recibido.getAbonos();
		long total_traspasos = recibido.getTraspasos();
		
		//cabecera
		String origen = this.consorcio.getId_consorcio();
		String destino = recibido.getOrigen();
		//cuerpo
		boolean cod_resp = Database_lib.getInstance().hasSesion(destino);
		CodigosError cod_error =  comprobar_errores(recibido,0,recibido.getNmsg());
		
		if(cod_error.equals(CodigosError.CORRECTO)) //Ejecuta las tareas necesarias
			this.cerrar_sesion(recibido.getOrigen(),recibido.getTotal_reintegros(),recibido.getAbonos(),recibido.getTraspasos()); 

    	this.enviar_mensaje(new RespCierreSesion(origen,destino,cod_resp,cod_error,total_reintegros,total_abonos,total_traspasos));
	}
	
	/**
	 * LLama a detener trafico y crea un mensaje respuesta para ello
	 * @param recibido El mensaje recibido.
	 */	
	private void manejar_detener_trafico(SolDetTrafico recibido){

		//cabecera
		String origen = this.consorcio.getId_consorcio();
		String destino = recibido.getOrigen();
		//cuerpo
		boolean cod_resp = Database_lib.getInstance().hasSesion(destino);
		CodigosError cod_error =  comprobar_errores(recibido,0,recibido.getNmsg());
		
		if(cod_error.equals(CodigosError.CORRECTO))
			this.detener_trafico(recibido.getOrigen()); //Ejecuta las tareas necesarias

		this.enviar_mensaje(new RespDetTrafico(origen,destino,cod_resp,cod_error));
	}	
	
	
	/**
	 * LLama a reanudar trafico y crea un mensaje respuesta para ello
	 * @param recibido El mensaje recibido.
	 */
	private void manejar_reanudar_trafico(SolReanTrafico recibido){
		
    	//cabecera
		String origen = this.consorcio.getId_consorcio();
		String destino = recibido.getOrigen();
		//cuerpo
		boolean cod_resp = Database_lib.getInstance().hasSesion(destino);
		CodigosError cod_error =  comprobar_errores(recibido,0,recibido.getNmsg());
		
		if(cod_error.equals(CodigosError.CORRECTO))
			this.reanudar_trafico(recibido.getOrigen());//Ejecuta las tareas necesarias
		
    	this.enviar_mensaje(new RespReanTrafico(origen,destino,cod_resp,cod_error));
	}
	
	private void maneja_consulta_saldo(RespSaldo recibido){
		
	}
	
	private void maneja_consultar_movimiento(RespMovimientos recibido){
		
	}
	
	private void maneja_reintegro(RespReintegro recibido){
		
	}
	
	private void maneja_abono(RespAbono recibido){
		
	}
	
	private void maneja_traspaso(RespTraspaso recibido){
		
	}
	
}
