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
import practicaacs.consorcio.aux.TipoAccion;
import practicaacs.consorcio.aux.TipoOrigDest;
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
	
	private TipoAccion tipo_accion;
	//CONEXION
	private DatagramSocket output_socket;
	private DatagramPacket input_packet;
	
	//RECUPERACION/FIN RECUPERACION
	private String id_banco_rec;
	
	//ENVIO
	private MensajeDatos envio;
	private String id_cajero;
	private InetAddress ip_cajero;
	private int puerto_cajero;
	
	
	/*---------------------------------------------
	  --------------- CONSTRUCTORES ---------------
	  ---------------------------------------------*/

	public ConexionConsorcio_Bancos(TipoAccion tipo, DatagramPacket paquete,Consorcio cons,ServidorConsorcio_Bancos s,DatagramSocket socket) {
		super();
		this.consorcio = cons;
		this.servidor = s;
		
		this.tipo_accion = tipo;
		this.input_packet = paquete;
		this.output_socket =  socket;
	}

	public ConexionConsorcio_Bancos(TipoAccion tipo,MensajeDatos env,String caj,InetAddress ip_caj, int puerto_caj, Consorcio cons,ServidorConsorcio_Bancos s,DatagramSocket socket) {
		super();
		this.consorcio = cons;
		this.servidor = s;
		
		this.tipo_accion = tipo;
		this.envio = env;
		this.id_cajero = caj;
		this.ip_cajero = ip_caj;
		this.puerto_cajero = puerto_caj;
		this.output_socket =  socket;
	}
	
	public ConexionConsorcio_Bancos(TipoAccion tipo,String id_b,Consorcio cons,ServidorConsorcio_Bancos s,DatagramSocket socket) {
		super();
		this.consorcio = cons;
		this.servidor = s;
		
		this.tipo_accion = tipo;
		this.id_banco_rec = id_b;
		this.output_socket =  socket;
	}
	
	
	/*--------------------------------------
	  --------------- RUN ------------------
	  --------------------------------------*/

	
	/**
	 * Método que obtiene el Mensaje a partir del paquete y analiza dicho 
	 * mensaje para realizar la tarea adecuada.
	 */
	public void run() {
		
			switch(this.tipo_accion){
				case CONEXION:
					Mensaje recibido = null;
					String msg =  new String(this.input_packet.getData(),this.input_packet.getOffset(),this.input_packet.getLength());
					System.out.println("recibe2:" + msg);
					
					//Creamos el mensaje correspondiente al recibido
					try {
						recibido = Mensaje.parse(msg);
					}catch (MensajeNoValidoException e) {
					    System.err.println(e.getLocalizedMessage());
					}
				
					//Guardamos el mensaje en la BD (Tabla de MENSAJES)
					Database_lib.getInstance().almacenar_mensaje(recibido,TipoOrigDest.BANCO,recibido.getOrigen(),TipoOrigDest.CONSORCIO,recibido.getDestino());
					//Actualizar la interfaz grafica
					this.consorcio.actualizarIU();
					
					//Analizamos el mensaje y realizamos las acciones correspondientes
					analizar_mensaje(recibido);
					break;
			
				case RECUPERACION:{
					solicitar_iniciar_recuperacion(this.id_banco_rec);
					break;
				}
				case FIN_RECUPERACION:{
					solicitar_finalizar_recuperacion(this.id_banco_rec);
					break;
				}
				case ENVIO:{
					resendToBanco(this.envio);
					break;
				}
			}	
		
	}
	
	
	/*-----------------------------------------------
	------------FUNCIONES DE ENVIO DE DATOS-----------
	-------------------------------------------------*/
	
	/**
	 * Método que envia la respuesta del BANCO al CAJERO.
	 * Cambia el origen/destino, obtiene IP y puerto para el envio
	 *  y delega en el Servidor de Cajeros para realizarlo.
	 * BANCO->CONSORCIO->CAJERO
	 * @param respuesta
	 */
	public void sendToCajero(MensajeDatos respuesta){
		
    	String id_banco = respuesta.getOrigen();
    	
    	//Cambiar origen y destino
		String destino = Database_lib.getInstance().getIdCajero(respuesta.getOrigen(),respuesta.getNumcanal());//Id_cajero
		String origen = this.consorcio.getId_consorcio(); //Id_consorcio
		respuesta.setDestino(destino);
		respuesta.setOrigen(origen);
		
		//Obtenemos la direccion y el puerto a donde enviar
		InetAddress ip_dest = Database_lib.getInstance().getIpEnvio(id_banco,respuesta.getNumcanal());
		int puerto_dest = Database_lib.getInstance().getPortEnvio(id_banco,respuesta.getNumcanal());

		//Delegar en el ServidorCajeros para el reenvio y el almacenamiento del envio
		this.consorcio.getCajeros_server().sendToCajero(respuesta,ip_dest,puerto_dest);
	}
	
	/**
	 * Método que envia el mensaje introducido por parámetro a traves del socket de la conexión
	 * que invoca el método.
	 * ENVIA MENSAJES DE CONTROL -  CONSORCIO->BANCO
	 * @param envio El mensaje a enviar.
	 */
	private void sendToBanco(Mensaje envio){
		
    	String id_banco = envio.getDestino();

		//Obtenemos la direccion y el puerto a donde enviar
		//InetAddress ip = Database_lib.getInstance().getIpBanco(id_banco);
		//int puerto = Database_lib.getInstance().getPortBanco(id_banco);
    	InetAddress ip = this.input_packet.getAddress();
    	int puerto = this.input_packet.getPort();
		int canal = 0; //Porque solo envia mensajes de control
		
		//Almacenamos el envio en la BD (Tabla de ULTIMO ENVIO) 
		//Database_lib.getInstance().anhadir_ultimo_envio(envio,envio.getOrigen(),this.input_packet.getAddress(),this.input_packet.getPort(),canal);

		//Guardamos el mensaje en la BD (Tabla de MENSAJES)
		Database_lib.getInstance().almacenar_mensaje(envio,TipoOrigDest.CONSORCIO,envio.getOrigen(),TipoOrigDest.BANCO,envio.getDestino());

		//Actualizar la interfaz grafica
		this.consorcio.actualizarIU();
		
		//Creamos el datagrama
		DatagramPacket enviarPaquete = new DatagramPacket(envio.getBytes(),envio.size(),ip,puerto);

		System.out.println(envio.getOrigen()+"->"+envio.getDestino()+":"+envio.getTipoMensaje());
		try{
			//Enviamos el mensaje
			this.output_socket.send(enviarPaquete);
		}catch (IOException e) {
			e.getStackTrace();
			System.out.println("Error al enviar CONSORCIO->BANCO (Mensaje de control)");
			System.exit ( 0 );
		}

	}
	
	/**
	 * Metodo que reenvia el mensaje a la IP y puerto introducidos por parámetro
	 * ENVIA MENSAJES DE DATOS - CONSORCIO->BANCO
	 * @param envio
	 */
	private void resendToBanco(MensajeDatos envio){
		
    	String id_banco = envio.getDestino();

		//Obtenemos la direccion y el puerto a donde enviar
		InetAddress ip = Database_lib.getInstance().getIpBanco(id_banco);
		int puerto = Database_lib.getInstance().getPortBanco(id_banco);
		
		//Seleccionamos el canal y se lo añadimos el mensaje
		int canal = Database_lib.getInstance().seleccionarCanal(envio.getDestino());
		envio.setNumcanal(canal);
		
		//Seleccionamos el numero de mensaje siguiente en el canal
		int n_mensaje = Database_lib.getInstance().selecciona_num_mensaje(envio.getDestino(),canal);
		envio.setNmsg(n_mensaje);
		
		//Almacenamos el envio en la BD (Tabla de ULTIMO ENVIO) 
		Database_lib.getInstance().anhadir_ultimo_envio(envio,this.id_cajero,this.ip_cajero,this.puerto_cajero,canal);

		//Guardamos el mensaje en la BD (Tabla de MENSAJES)
		Database_lib.getInstance().almacenar_mensaje(envio,TipoOrigDest.CONSORCIO,envio.getOrigen(),TipoOrigDest.BANCO,envio.getDestino());
		//Actualizar la interfaz grafica
		this.consorcio.actualizarIU();
		
		//Creamos el datagrama
		DatagramPacket enviarPaquete = new DatagramPacket(envio.getBytes(),envio.size(),ip,puerto);

		try{
			/*			
			if(envio.es_datos()){
				//Obtenemos la sesion y creamos un timer
				Sesion s = servidor.getSesion(envio.getDestino());
				s.setTimer(canal);
			}*/
			//Enviamos el mensaje
			this.output_socket.send(enviarPaquete);
		}catch (IOException e) {
			System.out.println("Error al enviar CONSORCIO->BANCO (Mensaje de datos)");
			System.exit ( 0 );
		}
	}
	
	
	/*-----------------------------------------------
	--------------- MÉTODOS DE ANALISIS -------------
	-------------------------------------------------*/
	
	
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
		if(!(Database_lib.getInstance().isContestado(id_banco,canal)))
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
	
		//OPERACION ==CERRAR SESION y hay Mensajes sin responder
		if((m.getTipoMensaje().equals(CodigosMensajes.SOLCIERRESESION)) 
				&& (Database_lib.getInstance().hayMensajesSinResponder(id_banco)))
			return CodigosError.OTRASCAUSAS;
			
		return CodigosError.CORRECTO;
	}
	
	/**
	 * Método que analiza el mensaje y en función de su tipo, realiza las funciones adecuadas.
	 * @param recibido El mensaje recibido.
	 */
	private void analizar_mensaje(Mensaje recibido){
		
		switch(recibido.getTipoMensaje()){
			//MENSAJES DE CONTROL
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
				manejar_iniciar_recuperacion(((RespIniTraficoRec) recibido));
				break;
			case RESFINREC: //fin recuperacion trafico (banco->consorcio)
				manejar_finalizar_recuperacion((RespFinTraficoRec) recibido);
				break;
				//MENSAJES DE DATOS
			case RESSALDO: //respuesta consultar saldo (banco->consorcio)
				maneja_mensajes_datos((MensajeRespDatos) recibido);
				break;
			case RESMOVIMIENTOS: //respuesta consultar movimientos (banco->consorcio)
				maneja_mensajes_datos((MensajeRespDatos) recibido);
				break;
			case RESREINTEGRO: //respuesta reintegro (banco->consorcio)
				maneja_mensajes_datos((MensajeRespDatos) recibido);
				break;
			case RESABONO: //respuesta abono (banco->consorcio)
				maneja_mensajes_datos((MensajeRespDatos) recibido);
				break;
			case RESTRASPASO: //respuesta traspaso (banco->consorcio)
				maneja_mensajes_datos((MensajeRespDatos) recibido);
				break;
			default:
				System.out.printf("ERROR: Mensaje -" + recibido.getTipoMensaje().toString() + "- no reconocido.");
				break;
		}
	}

	
	/*------------------------------------------------
	----------- METODOS DE COMPORTAMIENTO ----------
	-------------------------------------------------*/
	
	
	/**
	 * LLama a abrir sesion y crea un mensaje respuesta para ello
	 * @param recibido El mensaje recibido.
	 */
	private void manejar_abrir_sesion(SolAperturaSesion recibido){

		String id_banco = recibido.getOrigen();
		//cabecera
		String origen = this.consorcio.getId_consorcio();
		String destino = recibido.getOrigen();
		//cuerpo
		CodigosError cod_error = comprobar_errores(recibido,0); //Comprueba el tipo de error
		boolean cod_resp = cod_error.equals(CodigosError.CORRECTO); //True si correcto y false si error

		if(cod_error.equals(CodigosError.CORRECTO)){ //Si no ha habido errores, realizar las tareas necesarias
	    	Calendar time = Calendar.getInstance();
	    	System.out.println("APERTURA: Sesion con el banco: '"+ id_banco +"' comenzada a las " + time.getTime());
	
	    	//Añadimos al hashmap de Sesiones abiertas
	    	Sesion ses = new Sesion(id_banco,this.servidor,recibido.getNcanales());
	    	this.servidor.insertaSesion(ses);
	    	
	    	int port = recibido.getPuerto();
	    	String ip = recibido.getIp();
	    	
			//Guardamos la sesion en la BD
			Database_lib.getInstance().abrir_sesion(id_banco,ip,port,recibido.getNcanales());

			//Envia la respuesta
			this.sendToBanco(new RespAperturaSesion(origen,destino,cod_resp,cod_error));

			System.out.println("SALE:"+origen+"->"+destino+"cod:"+cod_resp+"err:"+cod_error);

			//Enviamos los mensajes offline de este banco
			for(Mensaje m : Database_lib.getInstance().getMensajesOffline(id_banco)){
				this.sendToBanco(m);
			}
		}
		else{ //Si hay errores respondemos con el error correspondiente
			System.out.println("SALE ERROR:"+origen+"->"+destino+"cod:"+cod_resp+"err:"+cod_error);

			//Envia la respuesta
			this.sendToBanco(new RespAperturaSesion(origen,destino,cod_resp,cod_error));
		}
	}
	
	
	/**
	 * LLama a cerrar sesion y crea un mensaje respuesta para ello
	 * @param recibido El mensaje recibido.
	 */	
	private void manejar_cerrar_sesion(SolCierreSesion recibido){
		
		String id_banco = recibido.getOrigen();
		//Datos recibidos
		int total_reintegros = recibido.getTotal_reintegros();
		int total_abonos = recibido.getAbonos();
		int total_traspasos = recibido.getTraspasos();
		
		//cabecera
		String origen = this.consorcio.getId_consorcio();
		String destino = recibido.getOrigen();
		//cuerpo
		boolean cod_resp = Database_lib.getInstance().hasSesion(destino);
		CodigosError cod_error =  comprobar_errores(recibido,0);
		
    	int reintegros_consorcio = 0;
    	int abonos_consorcio = 0;
    	int traspasos_consorcio = 0;
    	
		if(cod_error.equals(CodigosError.CORRECTO)){ //Ejecuta las tareas necesarias
		   	Calendar time = Calendar.getInstance();
	    	System.out.println("CIERRE: Sesion servidor Banco: "+ id_banco +" finalizado a las " + time.getTime());
	    	
	    	//Obtenemos la suma de los ultimos movimientos de la BD
	    	reintegros_consorcio = Database_lib.getInstance().getNumReintegros(id_banco);
	    	abonos_consorcio = Database_lib.getInstance().getNumAbonos(id_banco);
	    	traspasos_consorcio = Database_lib.getInstance().getNumTraspasos(id_banco);

	    	//En el caso de que no coincidan se imprime/avisa a la IU
	    	if(reintegros_consorcio!=total_reintegros || abonos_consorcio!=total_abonos || traspasos_consorcio!=total_traspasos){
	    		System.out.println("TOTALES RECIBIDOS: Reintegros: " + total_reintegros + ". Abonos: " + total_abonos + 
	    			". Traspasos: " + total_traspasos);
	    		System.out.println("TOTALES CONSORCIO: Reintegros: " + reintegros_consorcio + ". Abonos: " + abonos_consorcio + 
	    			". Traspasos: " + traspasos_consorcio);
	    	}
	    	
	    	//Eliminamos la sesion del hashmap de sesiones.
	    	this.servidor.eliminarSesion(id_banco);
	    	//Cerrar la sesion en la BD
	    	Database_lib.getInstance().cerrar_sesion(id_banco);
	    	
		}
		//Envia la respuesta
    	this.sendToBanco(new RespCierreSesion(origen,destino,
    			cod_resp,cod_error,
    			reintegros_consorcio,abonos_consorcio,traspasos_consorcio));
	}
	
	/**
	 * LLama a detener trafico y crea un mensaje respuesta para ello
	 * @param recibido El mensaje recibido.
	 */	
	private void manejar_detener_trafico(SolDetTrafico recibido){

		String id_banco = recibido.getOrigen();
		//cabecera
		String origen = this.consorcio.getId_consorcio();
		String destino = recibido.getOrigen();
		//cuerpo
		boolean cod_resp = Database_lib.getInstance().hasSesion(destino);
		CodigosError cod_error =  comprobar_errores(recibido,0);
		
		if(cod_error.equals(CodigosError.CORRECTO)){
			Calendar time = Calendar.getInstance();
	    	System.out.println("TRAFICO DETENIDO: Sesion servidor Banco: "+ id_banco +" comenzado a las " + time.getTime());
	   
    		//Settear el estado a DETIDA
	    	Database_lib.getInstance().setEstado_conexion_banco(id_banco,SesDetida.instance());
		}
		//Envia la respuesta
		this.sendToBanco(new RespDetTrafico(origen,destino,cod_resp,cod_error));
	}	
	
	/**
	 * LLama a reanudar trafico y crea un mensaje respuesta para ello
	 * @param recibido El mensaje recibido.
	 */
	private void manejar_reanudar_trafico(SolReanTrafico recibido){
		
		String id_banco = recibido.getOrigen();
    	//cabecera
		String origen = this.consorcio.getId_consorcio();
		String destino = recibido.getOrigen();
		//cuerpo
		boolean cod_resp = Database_lib.getInstance().hasSesion(destino);
		CodigosError cod_error =  comprobar_errores(recibido,0);
		
		if(cod_error.equals(CodigosError.CORRECTO)){ //realiza las acciones necesarias
			Calendar time = Calendar.getInstance();
    		System.out.println("TRAFICO REANUDADO: Sesion servidor Banco: "+ id_banco +" comenzado a las " + time.getTime());
    	
    		//Settear el estado a ABERTA
    		Database_lib.getInstance().setEstado_conexion_banco(id_banco,SesAberta.instance());

    		//Enviar el mensaje confirmando la apertura
    		this.sendToBanco(new RespReanTrafico(origen,destino,cod_resp,cod_error));
    		
			//Enviamos los mensajes offline de este banco
			for(Mensaje m : Database_lib.getInstance().getMensajesOffline(id_banco)){
				this.sendToBanco(m);
			}
			
		}else{
			//En caso de error, enviamos el error
			this.sendToBanco(new RespReanTrafico(origen,destino,cod_resp,cod_error));
		}
	}
	
	/**
	 * Metodo que inicia la recuperacion
	 * @param recibido El mensaje recibido.
	 */
	public void manejar_iniciar_recuperacion(RespIniTraficoRec recibido){
	
		String id_banco = recibido.getOrigen();
		boolean cod_resp = recibido.getCodResp();
		CodigosError cod_error = recibido.getCodError();
		
		if(cod_resp){
			//Setear el estado de la conexion a RECUPERACION
			Database_lib.getInstance().setEstado_conexion_banco(id_banco,SesRecuperacion.instance());
		
			//Consultar ultimos envios en la BD y reenviarlos
			for(Mensaje m : Database_lib.getInstance().recupera_ultimos_mensajes(id_banco)){
				this.sendToBanco(m);
			}
		}else{
			System.out.println("Error iniciando a recuperación: " + cod_error.getMensaje());
		}
	}

	
	/**
	 * Método que finaliza la recuperacion
	 * @param recibido El mensaje recibido.
	 */
	private void manejar_finalizar_recuperacion(RespFinTraficoRec recibido){
		
		String id_banco = recibido.getOrigen();
		boolean cod_resp = recibido.getCodResp();
		CodigosError cod_error = recibido.getCodError();
		
		
		if(cod_resp){
			//Settear el estado de la conexion a ABERTA
			Database_lib.getInstance().setEstado_conexion_banco(id_banco,SesAberta.instance());

			//Liberar todos los canales del banco en la BD
			Database_lib.getInstance().desbloquearCanales(id_banco);
		}else{
			System.out.println("Error finalizando a recuperación: " + cod_error.getMensaje());
		}
	}
	
	
	/*-----------------------------------------------
	------------------- SOLICITUDES -----------------
	-------------------------------------------------*/

	
	/**
	 * Genera y envia un mensaje de solicitud de inicio de trafico en recuperacion:
	 * SOLINITRAFICOREC
	 * @param id_banco El banco con el que iniciar la recuperacion.
	 */
	private void solicitar_iniciar_recuperacion(String id_banco){
		//Obtenemos los datos
		String origen = this.consorcio.getId_consorcio();
		String destino = id_banco;

		//Enviamos la solicitud de recuperacion
		SolIniTraficoRec envio = new SolIniTraficoRec(origen,destino); 
		this.sendToBanco(envio);
	}
	
	/**
	 * Genera y envia un mensaje de solicitud de fin de trafico en recuperación:
	 * SOLFINTRAFICOREC
	 * @param id_banco EL banco con el que finalizar la recuperacion.
	 */
	private void solicitar_finalizar_recuperacion(String id_banco){
		//Obtenemos los datos
		String origen = this.consorcio.getId_consorcio();
		String destino = id_banco;
		
		SolFinTraficoRec envio = new SolFinTraficoRec(origen,destino);
		this.sendToBanco(envio);
	}
	
	
	/*-----------------------------------------------
	--------------- MENSAJES DE DATOS ---------------
	-------------------------------------------------*/
	
	/**
	 * Método que se ejecuta para dar respuesta a los mensajes de datos recibidos por el banco.
	 * @param recibido El mensaje recibido.
	 */
	private void maneja_mensajes_datos(MensajeRespDatos recibido){

		String id_banco = recibido.getOrigen();
		int canal = recibido.getNumcanal();
		CodigosMensajes tipo_mensaje = recibido.getTipoMensaje();
		
		//En caso de que fose todo correctamente
		if(recibido.getCod_resp().equals(CodigosRespuesta.CONSACEPTADA)){

			//Comprueba que el orden es correcto y marca el envio anterior en ese canal como contestado.
			if(Database_lib.getInstance().esCorrectaContestacion(tipo_mensaje,id_banco,canal))
				Database_lib.getInstance().setContestadoEnvio(id_banco,canal);
			else{
				System.out.println("Error: No se esperaba el mensaje " + tipo_mensaje.toString() + " recibido por el canal "+ canal);
			}
			
			//Si esta en recuperacion bloquea el canal
			if(Database_lib.getInstance().getEstado_conexion_banco(id_banco).equals(SesRecuperacion.instance()))
				Database_lib.getInstance().bloquearCanal(id_banco,canal);
		}
		
		//Reenviar al Cajero
		this.sendToCajero(recibido);
	}
	
	
	
	
}
