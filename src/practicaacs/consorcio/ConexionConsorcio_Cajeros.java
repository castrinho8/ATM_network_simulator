package practicaacs.consorcio;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

import practicaacs.consorcio.aux.EstadoEnvio;
import practicaacs.consorcio.aux.Movimiento;
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
	
	private DatagramPacket input_packet;
	private DatagramSocket output_socket;
	
	/**
	 * Constructor de la clase ConexionConsorcio_Cajeros.
	 * @param paquete El paquete a enviar.
	 * @param cons El consorcio correspondiente.
	 * @param socket El socket para la conexión creada.
	 */
	public ConexionConsorcio_Cajeros(DatagramPacket paquete,Consorcio cons, DatagramSocket socket) {
		super();
		this.consorcio = cons;
		
		this.input_packet = paquete;
		this.output_socket = socket;
	}
	
	/**
	 * Método que se ejecuta cuando se inicia la ejecución del thread.
	 */
	public void run() {
		try {
			//Creamos el mensaje correspondiente al recibido
			Mensaje recibido = Mensaje.parse(this.input_packet.getData());
			System.out.printf(recibido.toString());
			
			//Guardamos el mensaje en la BD
			Database_lib.getInstance().almacenar_recepcion(recibido,false);
			
			//Analizamos el mensaje y realizamos las acciones correspondientes
			analizar_mensaje(recibido);
		}
		catch (Exception e) {
	    // manipular las excepciones
		}
	}
	
	/**
	 * Funcion que envia el mensaje pasado por parámetro con los datos de la conexion.
	 * Por lo tanto, solo sirve para responder el envio hacia el Cajero.
	 * @param respuesta El mensaje a enviar
	 */
	public void reply_message(Mensaje respuesta){
		
		System.out.printf(respuesta.toString());
		
		//Creamos el datagrama
		DatagramPacket enviarPaquete = new DatagramPacket(respuesta.getBytes(),respuesta.size(),this.output_socket.getInetAddress(),this.output_socket.getPort());
		
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
	public void reenviar_mensaje(MensajeDatos message,String numTarjeta){
		String destino = numTarjeta.substring(0, 8); //Id_banco
		String origen = Integer.toString(this.consorcio.getId_consorcio()); //Id_consorcio
		
		message.setDestino(destino);
		message.setOrigen(origen);
		
		//Almacenamos el envio en la BD
		Database_lib.getInstance().almacenar_envio(message);
		
		this.consorcio.getBancos_server().send_message(message);
	}
	
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
	
	/**
	 * Función que implementa el comportamiento de la consulta de saldo.
	 * @param recibido El mensaje de consulta de saldo recibido
	 */
	public void consultar_saldo(SolSaldo recibido){
			
		String origen = Integer.toString(this.consorcio.getId_consorcio());
		String destino = recibido.getOrigen();
		int numcanal = 0;
		int nmsg = 0;
		boolean codonline = Database_lib.getInstance().consultar_protocolo(destino);
		CodigosRespuesta cod_resp = 
				Database_lib.getInstance().comprobar_condiciones(recibido.getNum_tarjeta(),recibido.getNum_cuenta(),0);

		RespSaldo respuesta = null;
		
		switch(obtiene_tipo_envio(recibido,codonline,cod_resp)){
			case RECHAZAR_PETICION:{
				//La respuesta en caso de error.
				respuesta = new RespSaldo(origen,destino,numcanal,nmsg,codonline,cod_resp,true,0);

				//Enviamos el mensaje
				reply_message(respuesta);
				break;
			}
			case ENVIO_CORRECTO:{
				//Reenviamos el mensaje al banco
				reenviar_mensaje(recibido,recibido.getNum_tarjeta());
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
		String origen = Integer.toString(this.consorcio.getId_consorcio());
		String destino = recibido.getOrigen();
		//subcabecera
		int numcanal = 0;
		int nmsg = 0;
		boolean codonline = Database_lib.getInstance().consultar_protocolo(destino);
		//cuerpo
		CodigosRespuesta cod_resp = 
				Database_lib.getInstance().comprobar_condiciones(recibido.getNum_tarjeta(),recibido.getNum_cuenta(),0);
		
		RespMovimientos respuesta = null;
		
		switch(obtiene_tipo_envio(recibido,codonline,cod_resp)){
			case RECHAZAR_PETICION:{
				//La respuesta en caso de error.
				respuesta = new RespMovimientos(origen,destino,numcanal,nmsg,codonline,cod_resp,
						0,CodigosMovimiento.OTRO,true,0,null/*Fecha*/);
			
				//Enviamos el mensaje
				reply_message(respuesta);
				break;
			}
			case ENVIO_CORRECTO:{
				//Reenviamos el mensaje al banco
				reenviar_mensaje(recibido,recibido.getNum_tarjeta());
				break;
			}
		}
	}
	
	/**
	 * Método que realiza un reintegro.
	 * @param recibido El mensaje a analizar
	 */
	public void realizar_reintegro(SolReintegro recibido){
		
		String origen = Integer.toString(this.consorcio.getId_consorcio());
		String destino = recibido.getOrigen();
		int numcanal = 0;
		int nmsg = 0;
		boolean codonline = Database_lib.getInstance().consultar_protocolo(destino);
		CodigosRespuesta cod_resp = 
				Database_lib.getInstance().comprobar_condiciones(recibido.getNum_tarjeta(),recibido.getNum_cuenta(),0);

		RespReintegro respuesta = null;
		
		switch(obtiene_tipo_envio(recibido,codonline,cod_resp)){
			case RECHAZAR_PETICION:{
				//La respuesta en caso de error.
				respuesta = new RespReintegro(origen,destino,numcanal,nmsg,codonline,cod_resp,true,0); 
						
				//Enviamos el mensaje
				reply_message(respuesta);
				break;
			}
			case ALMACENAMIENTO:{
				//REALIZA EL REINTEGRO
				int saldo = Database_lib.getInstance().realizar_reintegro(recibido.getNum_tarjeta(),recibido.getNum_cuenta(),recibido.getImporte());
				boolean signo = saldo >= 0;
				
				//Creamos la respuesta correcta
				respuesta = new RespReintegro(origen,destino,numcanal,nmsg,codonline,cod_resp,signo,saldo); 

				//Enviamos el mensaje
				reply_message(respuesta);
				break;
			}
			case ENVIO_CORRECTO:{
				//Reenviamos el mensaje al banco
				reenviar_mensaje(recibido,recibido.getNum_tarjeta());
				break;
			}
		}
		
	}
	
	/**
	 * Método que realiza un abono.
	 * @param recibido El mensaje a analizar.
	 */
	public void realizar_abono(SolAbono recibido){
		
		String origen = Integer.toString(this.consorcio.getId_consorcio());
		String destino = recibido.getOrigen();
		int numcanal = 0;
		int nmsg = 0;
		boolean codonline = Database_lib.getInstance().consultar_protocolo(destino);
		CodigosRespuesta cod_resp = 
				Database_lib.getInstance().comprobar_condiciones(recibido.getNum_tarjeta(),recibido.getNum_cuenta(),0);
		
		RespAbono respuesta = null;
		
		switch(obtiene_tipo_envio(recibido,codonline,cod_resp)){
			case RECHAZAR_PETICION:{
				//La respuesta en caso de error.
				respuesta = new RespAbono(origen,destino,numcanal,nmsg,codonline,cod_resp,true,0);
						
				//Enviamos el mensaje
				reply_message(respuesta);
				break;
			}
			case ALMACENAMIENTO:{
				//REALIZA EL ABONO
				int saldo = Database_lib.getInstance().realizar_abono(recibido.getNum_tarjeta(),recibido.getNum_cuenta(),recibido.getImporte());
				boolean signo = saldo >= 0;
				
				//Creamos la respuesta correcta
				respuesta = new RespAbono(origen,destino,numcanal,nmsg,codonline,cod_resp,signo,saldo);
	
				//Enviamos el mensaje
				reply_message(respuesta);
				break;
			}
			case ENVIO_CORRECTO:{
				//Reenviamos el mensaje al banco
				reenviar_mensaje(recibido,recibido.getNum_tarjeta());
				break;
			}
		}
	}
	
	/**
	 * Método que realiza un traspaso.
	 * @param recibido El mensaje a analizar.
	 */
	public void realizar_traspaso(SolTraspaso recibido){
		
		String origen = Integer.toString(this.consorcio.getId_consorcio());
		String destino = recibido.getOrigen();
		int numcanal = 0;
		int nmsg = 0;
		boolean codonline = Database_lib.getInstance().consultar_protocolo(destino);
		CodigosRespuesta cod_resp = 
				Database_lib.getInstance().comprobar_condiciones(recibido.getNum_tarjeta(),recibido.getNum_cuenta_origen(),recibido.getNum_cuenta_destino());
		
		RespTraspaso respuesta = null;
		
		switch(obtiene_tipo_envio(recibido,codonline,cod_resp)){
			case RECHAZAR_PETICION:{
				//La respuesta en caso de error.
				respuesta = new RespTraspaso(origen,destino,numcanal,nmsg,codonline,cod_resp,true,0,true,0);
						
				//Enviamos el mensaje
				reply_message(respuesta);
				break;
			}
			case ALMACENAMIENTO:{
				//REALIZA EL TRASPASO
				int saldoDestino = Database_lib.getInstance().realizar_traspaso(recibido.getNum_tarjeta(),
						recibido.getNum_cuenta_origen(),recibido.getNum_cuenta_destino(),recibido.getImporte());
				boolean signoDestino = (saldoDestino>=0);
				
				//Obtiene el nuevo saldo en el origen
				int saldoOrigen = Database_lib.getInstance().consultar_saldo(recibido.getNum_tarjeta(), recibido.getNum_cuenta_origen());
				boolean signoOrigen = (saldoOrigen>=0);
				
				//Creamos la respuesta correcta
				respuesta = new RespTraspaso(origen,destino,numcanal,nmsg,codonline,cod_resp,
						signoOrigen,saldoOrigen,signoDestino,saldoDestino);
	
				//Enviamos el mensaje
				reply_message(respuesta);
				break;
			}
			case ENVIO_CORRECTO:{
				//Reenviamos el mensaje al banco
				reenviar_mensaje(recibido,recibido.getNum_tarjeta());
				break;
			}
		}
	}
	
	
}
