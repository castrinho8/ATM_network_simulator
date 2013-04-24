package practicaacs.consorcio;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;

import practicaacs.banco.estados.EstadoSesion;
import practicaacs.banco.estados.SesAberta;
import practicaacs.banco.estados.SesDetida;
import practicaacs.banco.estados.SesNonAberta;
import practicaacs.banco.estados.SesRecuperacion;
import practicaacs.fap.*;

/**
 * Representa cada sesion establecida entre BANCO-CONSORCIO
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
	 * Funcion que ejecuta la conexión, comprueba que el primer mensaje es de inicio de sesión
	 * y en ese caso, se bloquea a la espera de una nueva conexión entrante que manejar.
	 */
	public void run() {
		try {
			//Creamos el mensaje correspondiente al recibido
			Mensaje recibido = Mensaje.parse(this.input_packet.getData());
			System.out.printf(recibido.toString());
			
			//Analizamos el mensaje y realizamos las acciones correspondientes
			analizar_mensaje(recibido);
		}
		catch (Exception e) {
	    // manipular las excepciones
		}
	}
	
	
	/**
	 * Comprueba si no hay errores y devuelve el CodigosError adecuado	
	 * @param m El mensaje a analizar
	 * @param canal El canal por el que se recibe
	 * @param num_mensaje El número de mensaje recibido
	 * @return El código de error correspondiente, en caso de que no haya error devuelve CORRECTO.
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
	 * Funcion que analiza el mensaje recibido y devuelve una respuesta si es necesario
	 * y null en caso de que no sea necesario responder.
	 */
	private void analizar_mensaje(Mensaje recibido){
		Mensaje respuesta = null;
		
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
				iniciar_recuperacion((RespIniTraficoRec) recibido);
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
	}
	
	/**
	 * Abre la sesion con el banco
	 * @param recibido El mensaje de solicitud de apertura de sesion recibido.
	 */
	private void abrir_sesion(SolAperturaSesion recibido){
		String id_banco = recibido.getOrigen(); //el banco del que viene la solicitud
		
    	Calendar time = Calendar.getInstance();
    	System.out.println("APERTURA: Enviada a las " + recibido.getTime());
    	System.out.println("APERTURA: Sesion servidor Banco: "+ recibido.getOrigen() +" comenzada a las " + time.getTime());

		//Guardamos los datos en la base de datos
		Database_lib.getInstance().insertar_sesion(id_banco,recibido.getPuerto(),recibido.getNcanales(),SesAberta.instance());
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
    	
    	//Seteamos el estado de la conexion a cerrada
    	Database_lib.getInstance().setEstado_conexion_banco(id_banco,SesNonAberta.instance());
    	
    	//Obtenemos la suma de los ultimos movimientos
    	HashMap<String,Long> movimientos = new HashMap<String,Long>();
    	movimientos.put("Reintegros",Database_lib.getInstance().getNumReintegros(id_banco));
    	movimientos.put("Abonos",Database_lib.getInstance().getNumAbonos(id_banco));
    	movimientos.put("Traspasos",Database_lib.getInstance().getNumTraspasos(id_banco));
    	
    	return movimientos;
	}
	
	/**
	 * Detiene el trafico con el banco
	 */
	public void detener_trafico(String id_banco){
		Calendar time = Calendar.getInstance();
    	System.out.println("TRAFICO DETENIDO: Sesion servidor Banco: "+ id_banco +" comenzado a las " + time.getTime());
   
    	Database_lib.getInstance().setEstado_conexion_banco(id_banco,SesDetida.instance());
	}
	
	/**
	 * Reanuda el trafico con el banco
	 */
	private void reanudar_trafico(String id_banco){
		Calendar time = Calendar.getInstance();
    	System.out.println("TRAFICO REANUDADO: Sesion servidor Banco: "+ id_banco +" comenzado a las " + time.getTime());
    	
    	Database_lib.getInstance().setEstado_conexion_banco(id_banco,SesAberta.instance());
	}
	
	/**
	 * Inicia el proceso de recuperacion
	 */
	private void iniciar_recuperacion(RespIniTraficoRec recibido){
		String id_banco = recibido.getOrigen();
		
		Database_lib.getInstance().setEstado_conexion_banco(id_banco,SesRecuperacion.instance());

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

		Database_lib.getInstance().setEstado_conexion_banco(id_banco,SesAberta.instance());
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
		boolean cod_resp = Database_lib.getInstance().hasSesion(destino);
		CodigosError cod_error = comprobar_errores(recibido,0,recibido.getNmsg()); //Comprueba si hay errores

		if((cod_error.equals(CodigosError.CORRECTO)) && cod_resp.equals(CodigosRespuesta.CONSACEPTADA))
			this.abrir_sesion(recibido); //Ejecuta las tareas necesarias
		
		return RespAperturaSesion(origen,destino,cod_resp,cod_error);
	}
	
	/**
	 * LLama a cerrar sesion y crea un mensaje respuesta para ello
	 */	
	private RespCierreSesion manejar_cerrar_sesion(SolCierreSesion recibido){
		
		//Datos recibidos
		long total_reintegros = recibido.getTotal_reintegros();
		long total_abonos = recibido.getAbonos();
		long total_traspasos = recibido.getTraspasos();
		
		//cabecera
		String origen = Integer.toString(this.consorcio.getId_consorcio());
		String destino = recibido.getOrigen();
		//cuerpo
		boolean cod_resp = Database_lib.getInstance().hasSesion(destino);
		CodigosError cod_error =  comprobar_errores(recibido,0,recibido.getNmsg());
		
		if(cod_error.equals(CodigosError.CORRECTO)) //Ejecuta las tareas necesarias
			this.cerrar_sesion(recibido.getOrigen(),recibido.getTotal_reintegros(),recibido.getAbonos(),recibido.getTraspasos()); 

    	return new RespCierreSesion(origen,destino,cod_resp,cod_error,total_reintegros,total_abonos,total_traspasos);
	}
	
	/**
	 * LLama a detener trafico y crea un mensaje respuesta para ello
	 */	
	private RespDetTrafico manejar_detener_trafico(SolDetTrafico recibido){

		//cabecera
		String origen = Integer.toString(this.consorcio.getId_consorcio());
		String destino = recibido.getOrigen();
		//cuerpo
		boolean cod_resp = Database_lib.getInstance().hasSesion(destino);
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
		boolean cod_resp = Database_lib.getInstance().hasSesion(destino);
		CodigosError cod_error =  comprobar_errores(recibido,0,recibido.getNmsg());
		
		if(cod_error.equals(CodigosError.CORRECTO))
			this.reanudar_trafico(recibido.getOrigen());//Ejecuta las tareas necesarias
		
    	return new RespReanTrafico(origen,destino,cod_resp,cod_error);
	}
	
	
	
}
