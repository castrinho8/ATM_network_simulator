package practicaacs.consorcio;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import practicaacs.consorcio.aux.EstadoEnvio;
import practicaacs.consorcio.aux.TipoAccion;
import practicaacs.consorcio.aux.TipoOrigDest;
import practicaacs.consorcio.bd.Database_lib;
import practicaacs.fap.*;

/**
 * Clase que modela un thread que se utiliza para analizar, realizar las tareas necesarias y  
 * responder a un mensaje enviado por parte de un cajero.
 *
 * Se recibe el mensaje, se analiza para ver su tipo y se reenvia al banco en el caso en que todo sea correcto,
 * se rechaza la petición y se envia un mensaje de error o se almacena el envio en la BD para reenviar al 
 * banco en cuanto este inicie sesión.
 */
public class ConexionConsorcio_Cajeros extends Thread{
	
	private Consorcio consorcio;
	private TipoAccion tipo_accion;

	private DatagramPacket input_packet;
	private DatagramSocket output_socket;
	
	private MensajeDatos respuesta;
	private InetAddress ip_envio;
	private int puerto_envio;

	
	/*--------------------------------------
	  ----------- CONSTRUCTORES ------------
	  --------------------------------------*/

	/**
	 * Constructor de la clase ConexionConsorcio_Cajeros.
	 */
	public ConexionConsorcio_Cajeros(TipoAccion tipo,DatagramPacket paquete,Consorcio cons, DatagramSocket socket) {
		this.consorcio = cons;
		this.tipo_accion = tipo;

		this.input_packet = paquete;
		this.output_socket = socket;
	}
	
	/**
	 * Constructor de la clase.
	 */
	public ConexionConsorcio_Cajeros(TipoAccion tipo, MensajeDatos resp,Consorcio cons, DatagramSocket socket,InetAddress ip,int port){
		this.consorcio = cons;
		this.tipo_accion = tipo;

		this.respuesta = resp;
		this.output_socket = socket;
		this.puerto_envio = port;
		this.ip_envio = ip;
	}

	
	/*--------------------------------------
	  --------------- RUN ------------------
	  --------------------------------------*/
	
	/**
	 * Método que se ejecuta cuando se inicia la ejecución del thread.
	 */
	public void run() {
		switch(this.tipo_accion){
			//RECEPCION DE MENSAJE DE CAJERO -> BANCO
			case CONEXION:{
				System.out.println("CREA THREAD");
				String msg = new String(this.input_packet.getData(),this.input_packet.getOffset(),this.input_packet.getLength());
				System.out.println("STRING-" + msg + "-");
				
				//Creamos el mensaje correspondiente al recibido
				Mensaje recibido = null;
				try{
					recibido = Mensaje.parse(msg);
				}catch(MensajeNoValidoException e){
					System.err.println(e.getLocalizedMessage());
				}
				
				System.out.printf("SALE DEL PARSE" + recibido.toString());
				
				//Guardamos el mensaje en la BD (Tabla de MENSAJES)
				Database_lib.getInstance().almacenar_mensaje(recibido,TipoOrigDest.CAJERO,recibido.getOrigen(),TipoOrigDest.CONSORCIO,recibido.getDestino());
				//Actualizar la interfaz grafica
				this.consorcio.actualizarIU();
				
				//Analizamos el mensaje y realizamos las acciones correspondientes
				analizar_mensaje(recibido);
				break;
			}
			//REENVIO DE MENSAJE DEL BANCO -> CAJERO
			case ENVIO:{
				this.sendToCajero(respuesta,this.ip_envio,this.puerto_envio);
				break;
			}
		}
	}
	

	/*-----------------------------------------------
	------------FUNCIONES DE ENVIO DE DATOS-----------
	-------------------------------------------------*/

	/**
	 * Funcion que envia el mensaje pasado por parámetro con los datos de la conexion.
	 * Por lo tanto, solo sirve para responder el envio hacia el Cajero.
	 * @param respuesta El mensaje a enviar
	 */
	public void sendToCajero(MensajeDatos respuesta,InetAddress ip_cajero,int port_cajero){
		
		//Guardamos el Mensaje en la BD (Tabla de MENSAJES)
		Database_lib.getInstance().almacenar_mensaje(respuesta,TipoOrigDest.CONSORCIO,respuesta.getOrigen(),TipoOrigDest.CAJERO,respuesta.getDestino());
		//Actualizar la interfaz grafica
		this.consorcio.actualizarIU();
		
		//Creamos el datagrama
		DatagramPacket enviarPaquete = new DatagramPacket(respuesta.getBytes(),respuesta.size(),ip_cajero,port_cajero);
		
		try{
			//Enviamos el mensaje
			this.output_socket.send(enviarPaquete);
		}catch (IOException e) {
			System.out.println("Error al enviar");
			System.exit ( 0 );
		}
	}
	
	/**
	 * Método que recibe un mensaje de datos para reenviarlo al banco correspondiente.
	 * Cambia el origen "id_cajero"->"id_consorcio" y el destino "id_consorcio"->"id_banco" y
	 * delega en el servidor de Bancos para su envio.
	 * @param message El mensaje a enviar.
	 * @param numTarjeta El número de tarjeta, necesario para cambiar el destino.
	 */
	public void sendToBanco(MensajeDatos message,String numTarjeta){
		
		//Cambiamos origen y destino
		String destino = numTarjeta.substring(0, 8); //Id_banco
		String origen = this.consorcio.getId_consorcio(); //Id_consorcio
		message.setDestino(destino);
		message.setOrigen(origen);

		//Delegar en el ServidorBancos para el reenvio y el almacenamiento del envio
		this.consorcio.getBancos_server().sendToBanco(message,this.output_socket.getInetAddress(),this.output_socket.getPort());
	}
	
	

	/*--------------------------------------
	  ------- METODOS DE ANALISIS ----------
	  --------------------------------------*/
	
	
	/**
	 * Función que selecciona la accion a realizar en funcion del tipo de mensaje recibido.
	 * @param recibido El mensaje que recibimos.
	 */
	private void analizar_mensaje(Mensaje recibido){
		switch(recibido.getTipoMensaje()){
			case SOLSALDO:{
				consultar_saldo((SolSaldo) recibido);
				break;
			}
			case SOLMOVIMIENTOS:{
				consultar_movimientos((SolMovimientos) recibido);
				break;
			}
			case SOLREINTEGRO:{
				realizar_reintegro((SolReintegro) recibido);
				break;
			}
			case SOLABONO:{
				realizar_abono((SolAbono) recibido);
				break;
			}
			case SOLTRASPASO:{
				realizar_traspaso((SolTraspaso) recibido);
				break;
			}
			default:{
				System.out.println("Error: Tipo de mensaje no reconocido.");
				break;
			}
		}
	}
	
	/**
	 * Función que analiza si se rechaza la petición, se almacena para enviar (en caso de que no se pueda enviar
	 * en el momento o si se reenvia directamente al banco.
	 * @param recibido El mensaje a analizar.
	 * @param protocolo Indica True si esta levantada la sesion y False en caso contrario.
	 * @param cod_resp El código que indica si la consulta ha sido aceptada o no.
	 * @return El EstadoEnvio correspondiente al caso.
	 */
	private EstadoEnvio obtiene_tipo_envio(Mensaje recibido,boolean protocolo,CodigosRespuesta cod_resp){
		
		if(!cod_resp.equals(CodigosRespuesta.CONSACEPTADA))
			return EstadoEnvio.RECHAZAR_PETICION;
		
		if(!protocolo){
			if(recibido.es_consulta()){
				return EstadoEnvio.RECHAZAR_PETICION;
			}else{
				return EstadoEnvio.ALMACENAMIENTO;
			}
		}else{
			return EstadoEnvio.ENVIO_CORRECTO;
		}
	}
	

	/*--------------------------------------
	  ----- METODOS DE COMPORTAMIENTO ------
	  --------------------------------------*/

	
	/**
	 * Función que implementa el comportamiento de la consulta de saldo.
	 * @param recibido El mensaje de consulta de saldo recibido
	 */
	public void consultar_saldo(SolSaldo recibido){
			
		String origen = this.consorcio.getId_consorcio();
		String destino = recibido.getOrigen();
		int numcanal = 0;
		int nmsg = 0;
		boolean codonline = Database_lib.getInstance().consultar_protocolo(destino);
		CodigosRespuesta cod_resp = 
				Database_lib.getInstance().comprobar_condiciones(recibido.getNum_tarjeta(),-1,recibido.getNum_cuenta(),CodigosMensajes.SOLABONO,0);

		RespSaldo respuesta = null;
		
		System.out.printf("ONLINE:%i-COD_RES:%s",codonline?1:0,cod_resp.toString());

		switch(obtiene_tipo_envio(recibido,codonline,cod_resp)){
			case RECHAZAR_PETICION:{
				//La respuesta en caso de error.
				respuesta = new RespSaldo(origen,destino,numcanal,nmsg,codonline,cod_resp,true,0);

				//Enviamos el mensaje
				sendToCajero(respuesta,this.output_socket.getInetAddress(),this.output_socket.getPort());
				break;
			}
			case ENVIO_CORRECTO:{
				//Reenviamos el mensaje al banco
				//reenviar_mensaje(recibido,recibido.getNum_tarjeta());
				break;
			}
		}
	}
	
			
	/**
	 * Función que implementa el comportamiento de la consulta de movimientos.
	 * @param recibido El mensaje de consulta de movimientos recibido
	 */
	public void consultar_movimientos(SolMovimientos recibido){

		//cabecera
		String origen = this.consorcio.getId_consorcio();
		String destino = recibido.getOrigen();
		//subcabecera
		int numcanal = 0;
		int nmsg = 0;
		boolean codonline = Database_lib.getInstance().consultar_protocolo(destino);
		//cuerpo
		CodigosRespuesta cod_resp = 
				Database_lib.getInstance().comprobar_condiciones(recibido.getNum_tarjeta(),-1,recibido.getNum_cuenta(),CodigosMensajes.SOLMOVIMIENTOS,0);
		
		RespMovimientos respuesta = null;
		
		switch(obtiene_tipo_envio(recibido,codonline,cod_resp)){
			case RECHAZAR_PETICION:{
				//La respuesta en caso de error.
				respuesta = new RespMovimientos(origen,destino,numcanal,nmsg,codonline,cod_resp,
						0,CodigosMovimiento.OTRO,true,0,null/*Fecha*/);
			
				//Enviamos el mensaje
				sendToCajero(respuesta,this.output_socket.getInetAddress(),this.output_socket.getPort());
				break;
			}
			case ENVIO_CORRECTO:{
				//Reenviamos el mensaje al banco
				//reenviar_mensaje(recibido,recibido.getNum_tarjeta());
				break;
			}
		}
	}
	
	/**
	 * Método que realiza un reintegro.
	 * @param recibido El mensaje a analizar
	 */
	public void realizar_reintegro(SolReintegro recibido){
		
		String origen = this.consorcio.getId_consorcio();
		String destino = recibido.getOrigen();
		int numcanal = 0;
		int nmsg = 0;
		boolean codonline = Database_lib.getInstance().consultar_protocolo(destino);
		CodigosRespuesta cod_resp = 
				Database_lib.getInstance().comprobar_condiciones(recibido.getNum_tarjeta(),-1,recibido.getNum_cuenta(),CodigosMensajes.SOLREINTEGRO,recibido.getImporte());

		RespReintegro respuesta = null;
		
		switch(obtiene_tipo_envio(recibido,codonline,cod_resp)){
			case RECHAZAR_PETICION:{
				//La respuesta en caso de error.
				respuesta = new RespReintegro(origen,destino,numcanal,nmsg,codonline,cod_resp,true,0); 
						
				//Enviamos el mensaje
				sendToCajero(respuesta,this.output_socket.getInetAddress(),this.output_socket.getPort());
				break;
			}
			case ALMACENAMIENTO:{
				//REALIZA EL REINTEGRO
				int saldo = Database_lib.getInstance().realizar_reintegro(recibido.getNum_tarjeta(),recibido.getNum_cuenta(),recibido.getImporte(),codonline);
				boolean signo = saldo >= 0;
				
				//Creamos la respuesta correcta
				respuesta = new RespReintegro(origen,destino,numcanal,nmsg,codonline,cod_resp,signo,saldo); 

				//Enviamos el mensaje
				sendToCajero(respuesta,this.output_socket.getInetAddress(),this.output_socket.getPort());
				break;
			}
			case ENVIO_CORRECTO:{
				//Reenviamos el mensaje al banco
				//reenviar_mensaje(recibido,recibido.getNum_tarjeta());
				break;
			}
		}
		
	}
	
	/**
	 * Método que realiza un abono.
	 * @param recibido El mensaje a analizar.
	 */
	public void realizar_abono(SolAbono recibido){
		
		String origen = this.consorcio.getId_consorcio();
		String destino = recibido.getOrigen();
		int numcanal = 0;
		int nmsg = 0;
		boolean codonline = Database_lib.getInstance().consultar_protocolo(destino);
		CodigosRespuesta cod_resp = 
				Database_lib.getInstance().comprobar_condiciones(recibido.getNum_tarjeta(),-1,recibido.getNum_cuenta(),CodigosMensajes.SOLABONO,recibido.getImporte());
		
		RespAbono respuesta = null;
		
		switch(obtiene_tipo_envio(recibido,codonline,cod_resp)){
			case RECHAZAR_PETICION:{
				//La respuesta en caso de error.
				respuesta = new RespAbono(origen,destino,numcanal,nmsg,codonline,cod_resp,true,0);
						
				//Enviamos el mensaje
				sendToCajero(respuesta,this.output_socket.getInetAddress(),this.output_socket.getPort());
				break;
			}
			case ALMACENAMIENTO:{
				//REALIZA EL ABONO
				int saldo = Database_lib.getInstance().realizar_abono(recibido.getNum_tarjeta(),recibido.getNum_cuenta(),codonline,recibido.getImporte());
				boolean signo = saldo >= 0;
				
				//Creamos la respuesta correcta
				respuesta = new RespAbono(origen,destino,numcanal,nmsg,codonline,cod_resp,signo,saldo);
	
				//Enviamos el mensaje
				sendToCajero(respuesta,this.output_socket.getInetAddress(),this.output_socket.getPort());
				break;
			}
			case ENVIO_CORRECTO:{
				//Reenviamos el mensaje al banco
				//reenviar_mensaje(recibido,recibido.getNum_tarjeta());
				break;
			}
		}
	}
	
	/**
	 * Método que realiza un traspaso.
	 * @param recibido El mensaje a analizar.
	 */
	public void realizar_traspaso(SolTraspaso recibido){
		
		String origen = this.consorcio.getId_consorcio();
		String destino = recibido.getOrigen();
		int numcanal = 0;
		int nmsg = 0;
		boolean codonline = Database_lib.getInstance().consultar_protocolo(destino);
		CodigosRespuesta cod_resp = 
				Database_lib.getInstance().comprobar_condiciones(recibido.getNum_tarjeta(),recibido.getNum_cuenta_origen(),recibido.getNum_cuenta_destino(),CodigosMensajes.SOLTRASPASO,recibido.getImporte());
		
		RespTraspaso respuesta = null;
		
		switch(obtiene_tipo_envio(recibido,codonline,cod_resp)){
			case RECHAZAR_PETICION:{
				//La respuesta en caso de error.
				respuesta = new RespTraspaso(origen,destino,numcanal,nmsg,codonline,cod_resp,true,0,true,0);
						
				//Enviamos el mensaje
				sendToCajero(respuesta,this.output_socket.getInetAddress(),this.output_socket.getPort());
				break;
			}
			case ALMACENAMIENTO:{
				//REALIZA EL TRASPASO
				int saldoDestino = Database_lib.getInstance().realizar_traspaso(recibido.getNum_tarjeta(),
						recibido.getNum_cuenta_origen(),recibido.getNum_cuenta_destino(),codonline,recibido.getImporte());
				boolean signoDestino = (saldoDestino>=0);
				
				//Obtiene el nuevo saldo en el origen
				int saldoOrigen = Database_lib.getInstance().consultar_saldo(recibido.getNum_tarjeta(),recibido.getNum_cuenta_origen());
				boolean signoOrigen = (saldoOrigen>=0);
				
				//Creamos la respuesta correcta
				respuesta = new RespTraspaso(origen,destino,numcanal,nmsg,codonline,cod_resp,
						signoOrigen,saldoOrigen,signoDestino,saldoDestino);
	
				//Enviamos el mensaje
				sendToCajero(respuesta,this.output_socket.getInetAddress(),this.output_socket.getPort());
				break;
			}
			case ENVIO_CORRECTO:{
				//Reenviamos el mensaje al banco
				//reenviar_mensaje(recibido,recibido.getNum_tarjeta());
				break;
			}
		}
	}
	
	
}
