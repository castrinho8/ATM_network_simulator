package practicaacs.consorcio;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

import practicaacs.consorcio.aux.EstadoEnvio;
import practicaacs.consorcio.aux.Movimiento;
import practicaacs.consorcio.aux.TipoAccion;
import practicaacs.consorcio.aux.TipoOrigDest;
import practicaacs.consorcio.bd.ConsorcioBDException;
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
	 * Constructor de la clase.
	 * 
	 * @param tipo El tipo de acción a realizar.
	 * @param paquete El paquete que se recibe.
	 * @param cons El consorcio que realiza la conexión.
	 * @param socket El socket de conexión.
	 */
	public ConexionConsorcio_Cajeros(TipoAccion tipo,DatagramPacket paquete,Consorcio cons, DatagramSocket socket) {
		this.consorcio = cons;
		this.tipo_accion = tipo;

		this.input_packet = paquete;
		this.output_socket = socket;
	}
	
	/**
	 * Constructor de la clase.
	 * Se utiliza para los envios CONSORCIO->CAJERO
	 * 
	 * @param tipo El tipo de acción a realizar.
	 * @param resp La respuesta recibida.
	 * @param cons El consorcio que realiza la conexión.
	 * @param socket EL socket de la conexión.
	 * @param ip La ip a donde realizar el envio.
	 * @param port El puerto a donde realizar el envio.
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
				String msg = new String(this.input_packet.getData(),this.input_packet.getOffset(),this.input_packet.getLength());
				
				//Creamos el mensaje correspondiente al recibido
				Mensaje recibido = null;
				try{
					recibido = Mensaje.parse(msg);
				}catch(MensajeNoValidoException e){
					System.err.println(e.getLocalizedMessage());
				}
				
				MensajeDatos recibido_datos = null;
				String id_banco = null;
				//Casteamos a Mensaje de datos y obtenemos el id
				try{
					recibido_datos = ((MensajeDatos)recibido);
					id_banco = recibido_datos.getIdBancoFromTarjeta();
				}catch(NullPointerException ne){
					System.out.println("Error en el mensaje recibido. La tarjeta introducida tiene un formato incorrecto.");
					System.exit(-1);
				}
				
				//Seteamos si el banco esta online o no
				recibido_datos.setCodonline(Database_lib.getInstance().consultar_protocolo(id_banco));
				
				//Guardamos el mensaje en la BD (Tabla de MENSAJES)
				int bd_num_message = Database_lib.getInstance().almacenar_mensaje(recibido,TipoOrigDest.CAJERO,recibido.getOrigen(),TipoOrigDest.CONSORCIO,recibido.getDestino());
				
				//Actualizar la interfaz grafica
				this.consorcio.actualizarIU();
				
				//Analizamos el mensaje y realizamos las acciones correspondientes
				analizar_mensaje(recibido,bd_num_message);
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
	 * @param ip_cajero La IP del cajero a donde se envía la respuesta.
	 * @param port_cajero El puerto del cajero a donde se envía la respuesta.
	 */
	public void sendToCajero(MensajeDatos respuesta,InetAddress ip_cajero,int port_cajero){
		
		//Guardamos el Mensaje en la BD (Tabla de MENSAJES)
		Database_lib.getInstance().almacenar_mensaje(respuesta,TipoOrigDest.CONSORCIO,respuesta.getOrigen(),TipoOrigDest.CAJERO,respuesta.getDestino());
		
		//Actualizar la interfaz grafica
		this.consorcio.actualizarIU();
		
		System.out.println("SALE: "+respuesta.obtenerImprimible("CONSORCIO", "CAJERO"));
		
		//Creamos el datagrama
		DatagramPacket enviarPaquete = new DatagramPacket(respuesta.getBytes(),respuesta.size(),ip_cajero,port_cajero);
		
		try{
			//Enviamos el mensaje
			this.output_socket.send(enviarPaquete);
		}catch (IOException e) {
			e.printStackTrace();
			System.out.println("Error al enviar CONSORCIO->CAJERO (Mensaje de datos)");
			System.exit (-1);
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
		
		String id_cajero = message.getOrigen();
		String destino = null;
		
		//Cambiamos origen y destino
		try{
			destino = numTarjeta.substring(0,numTarjeta.length()-3); //Id_banco
		}catch(IndexOutOfBoundsException e){
			e.printStackTrace();
			System.out.println("Error obteniendo el banco destino a partir del número de tarjeta.");
			System.exit (-1);
		}
		
		String origen = this.consorcio.getId_consorcio(); //Id_consorcio
		message.setDestino(destino);
		message.setOrigen(origen);

		//Delegar en el ServidorBancos para el reenvio y el almacenamiento del envio
		this.consorcio.getBancos_server().sendToBanco(message,id_cajero);
	}
	
	

	/*--------------------------------------
	  ------- METODOS DE ANALISIS ----------
	  --------------------------------------*/
	
	
	/**
	 * Función que selecciona la accion a realizar en funcion del tipo de mensaje recibido.
	 * @param recibido El mensaje que recibimos.
	 * @param bd_num_message El número que identifica al mensaje en la BD.
	 */
	private void analizar_mensaje(Mensaje recibido,int bd_num_message){
		
		switch(recibido.getTipoMensaje()){
			case SOLSALDO:{
				consultar_saldo((SolSaldo) recibido,bd_num_message);
				break;
			}
			case SOLMOVIMIENTOS:{
				consultar_movimientos((SolMovimientos) recibido,bd_num_message);
				break;
			}
			case SOLREINTEGRO:{
				realizar_reintegro((SolReintegro) recibido,bd_num_message);
				break;
			}
			case SOLABONO:{
				realizar_abono((SolAbono) recibido,bd_num_message);
				break;
			}
			case SOLTRASPASO:{
				realizar_traspaso((SolTraspaso) recibido,bd_num_message);
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
	 * @param recibido El mensaje de consulta de saldo recibido.
	 * @param bd_num_message El número que identifica al mensaje en la BD.
	 */
	public void consultar_saldo(SolSaldo recibido,int bd_num_message){
			
		String id_banco = recibido.getIdBancoFromTarjeta();

		String origen = this.consorcio.getId_consorcio();
		String destino = recibido.getOrigen();
		int numcanal = 0;
		int nmsg = recibido.getNmsg();
		boolean codonline = Database_lib.getInstance().consultar_protocolo(id_banco) && !this.consorcio.isDownBancosServer();
		CodigosRespuesta cod_resp = 
				Database_lib.getInstance().comprobar_condiciones(recibido.getNum_tarjeta(),-1,recibido.getNum_cuenta(),CodigosMensajes.SOLSALDO,0,codonline);

		RespSaldo respuesta = null;
		
		switch(obtiene_tipo_envio(recibido,codonline,cod_resp)){
			case RECHAZAR_PETICION:{
				
				//Impide que el mensaje erroneo se reenvie al banco cuando este inicie sesion
				Database_lib.getInstance().setMessageCodOnlineToNull(bd_num_message);
				
				//La respuesta en caso de error.
				respuesta = new RespSaldo(origen,destino,numcanal,nmsg,codonline,cod_resp,true,0);

				//Enviamos el mensaje
				sendToCajero(respuesta,this.input_packet.getAddress(),this.input_packet.getPort());
				break;
			}
			case ENVIO_CORRECTO:{
				//Reenviamos el mensaje al banco
				this.sendToBanco(recibido, recibido.getNum_tarjeta());
				break;
			}
		}
	}
	
			
	/**
	 * Función que implementa el comportamiento de la consulta de movimientos.
	 * @param recibido El mensaje de consulta de movimientos recibido
	 * @param bd_num_message El número que identifica al mensaje en la BD.
	 */
	public void consultar_movimientos(SolMovimientos recibido,int bd_num_message){

		String id_banco = recibido.getIdBancoFromTarjeta();

		String origen = this.consorcio.getId_consorcio();
		String destino = recibido.getOrigen();
		int numcanal = 0;
		int nmsg = recibido.getNmsg();
		boolean codonline = Database_lib.getInstance().consultar_protocolo(id_banco) && !this.consorcio.isDownBancosServer();
		CodigosRespuesta cod_resp = 
				Database_lib.getInstance().comprobar_condiciones(recibido.getNum_tarjeta(),-1,recibido.getNum_cuenta(),CodigosMensajes.SOLMOVIMIENTOS,0,codonline);
		
		RespMovimientos respuesta = null;
		Calendar c = Calendar.getInstance();
		
		switch(obtiene_tipo_envio(recibido,codonline,cod_resp)){
			case RECHAZAR_PETICION:{

				//Impide que el mensaje erroneo se reenvie al banco cuando este inicie sesion
				Database_lib.getInstance().setMessageCodOnlineToNull(bd_num_message);

				//La respuesta en caso de error.
				respuesta = new RespMovimientos(origen,destino,numcanal,nmsg,codonline,cod_resp,
						0,CodigosMovimiento.OTRO,true,0,c.getTime());
			
				//Enviamos el mensaje
				sendToCajero(respuesta,this.input_packet.getAddress(),this.input_packet.getPort());
				break;
			}
			case ENVIO_CORRECTO:{
				//Reenviamos el mensaje al banco
				this.sendToBanco(recibido, recibido.getNum_tarjeta());
				break;
			}
		}
	}
	
	/**
	 * Método que realiza un reintegro.
	 * @param recibido El mensaje a analizar
	 * @param bd_num_message El número que identifica al mensaje en la BD.
	 */
	public void realizar_reintegro(SolReintegro recibido,int bd_num_message){
		
		String id_banco = recibido.getIdBancoFromTarjeta();

		String origen = this.consorcio.getId_consorcio();
		String destino = recibido.getOrigen();
		int numcanal = 0;
		int nmsg = recibido.getNmsg();
		boolean codonline = Database_lib.getInstance().consultar_protocolo(id_banco) && !this.consorcio.isDownBancosServer();
		CodigosRespuesta cod_resp = 
				Database_lib.getInstance().comprobar_condiciones(recibido.getNum_tarjeta(),-1,recibido.getNum_cuenta(),CodigosMensajes.SOLREINTEGRO,recibido.getImporte(),codonline);

		RespReintegro respuesta = null;
		
		switch(obtiene_tipo_envio(recibido,codonline,cod_resp)){
			case RECHAZAR_PETICION:{

				//Impide que el mensaje erroneo se reenvie al banco cuando este inicie sesion
				Database_lib.getInstance().setMessageCodOnlineToNull(bd_num_message);

				//La respuesta en caso de error.
				respuesta = new RespReintegro(origen,destino,numcanal,nmsg,codonline,cod_resp,true,0); 
						
				//Enviamos el mensaje
				sendToCajero(respuesta,this.input_packet.getAddress(),this.input_packet.getPort());
				break;
			}
			case ALMACENAMIENTO:{
				//REALIZA EL REINTEGRO
				int saldo = Database_lib.getInstance().realizar_reintegro(recibido.getNum_tarjeta(),recibido.getNum_cuenta(),recibido.getImporte(),codonline);
				boolean signo = saldo >= 0;
				
				//Creamos la respuesta correcta
				respuesta = new RespReintegro(origen,destino,numcanal,nmsg,codonline,cod_resp,signo,saldo); 

				//Enviamos el mensaje
				sendToCajero(respuesta,this.input_packet.getAddress(),this.input_packet.getPort());
				break;
			}
			case ENVIO_CORRECTO:{
				//REALIZA EL REINTEGRO
				Database_lib.getInstance().realizar_reintegro(recibido.getNum_tarjeta(),recibido.getNum_cuenta(),recibido.getImporte(),codonline);

				//Reenviamos el mensaje al banco
				this.sendToBanco(recibido, recibido.getNum_tarjeta());
				break;
			}
		}
		
	}
	
	/**
	 * Método que realiza un abono.
	 * @param recibido El mensaje a analizar.
	 * @param bd_num_message El número que identifica al mensaje en la BD.
	 */
	public void realizar_abono(SolAbono recibido,int bd_num_message){
		
		String id_banco = recibido.getIdBancoFromTarjeta();

		String origen = this.consorcio.getId_consorcio();
		String destino = recibido.getOrigen();
		int numcanal = 0;
		int nmsg = recibido.getNmsg();
		boolean codonline = Database_lib.getInstance().consultar_protocolo(id_banco) && !this.consorcio.isDownBancosServer();
		CodigosRespuesta cod_resp = 
				Database_lib.getInstance().comprobar_condiciones(recibido.getNum_tarjeta(),-1,recibido.getNum_cuenta(),CodigosMensajes.SOLABONO,recibido.getImporte(),codonline);
		
		RespAbono respuesta = null;
		
		switch(obtiene_tipo_envio(recibido,codonline,cod_resp)){
			case RECHAZAR_PETICION:{
				
				//Impide que el mensaje erroneo se reenvie al banco cuando este inicie sesion
				Database_lib.getInstance().setMessageCodOnlineToNull(bd_num_message);

				//La respuesta en caso de error.
				respuesta = new RespAbono(origen,destino,numcanal,nmsg,codonline,cod_resp,true,0);
						
				//Enviamos el mensaje
				sendToCajero(respuesta,this.input_packet.getAddress(),this.input_packet.getPort());
				break;
			}
			case ALMACENAMIENTO:{
				//REALIZA EL ABONO
				int saldo = Database_lib.getInstance().realizar_abono(recibido.getNum_tarjeta(),recibido.getNum_cuenta(),codonline,recibido.getImporte());
				boolean signo = saldo >= 0;
				
				//Creamos la respuesta correcta
				respuesta = new RespAbono(origen,destino,numcanal,nmsg,codonline,cod_resp,signo,saldo);
	
				//Enviamos el mensaje
				sendToCajero(respuesta,this.input_packet.getAddress(),this.input_packet.getPort());
				break;
			}
			case ENVIO_CORRECTO:{
				//REALIZA EL ABONO
				Database_lib.getInstance().realizar_abono(recibido.getNum_tarjeta(),recibido.getNum_cuenta(),codonline,recibido.getImporte());

				//Reenviamos el mensaje al banco
				this.sendToBanco(recibido, recibido.getNum_tarjeta());
				break;
			}
		}
	}
	
	/**
	 * Método que realiza un traspaso.
	 * @param recibido El mensaje a analizar.
	 * @param bd_num_message El número que identifica al mensaje en la BD.
	 */
	public void realizar_traspaso(SolTraspaso recibido,int bd_num_message){
		
		String id_banco = recibido.getIdBancoFromTarjeta();

		String origen = this.consorcio.getId_consorcio();
		String destino = recibido.getOrigen();
		int numcanal = 0;
		int nmsg = recibido.getNmsg();
		boolean codonline = Database_lib.getInstance().consultar_protocolo(id_banco) && !this.consorcio.isDownBancosServer();
		CodigosRespuesta cod_resp = 
				Database_lib.getInstance().comprobar_condiciones(recibido.getNum_tarjeta(),recibido.getNum_cuenta_origen(),recibido.getNum_cuenta_destino(),CodigosMensajes.SOLTRASPASO,recibido.getImporte(),codonline);
			
		RespTraspaso respuesta = null;
		
		switch(obtiene_tipo_envio(recibido,codonline,cod_resp)){
			case RECHAZAR_PETICION:{

				//Impide que el mensaje erroneo se reenvie al banco cuando este inicie sesion
				Database_lib.getInstance().setMessageCodOnlineToNull(bd_num_message);

				//La respuesta en caso de error.
				respuesta = new RespTraspaso(origen,destino,numcanal,nmsg,codonline,cod_resp,true,0,true,0);
						
				//Enviamos el mensaje
				sendToCajero(respuesta,this.input_packet.getAddress(),this.input_packet.getPort());
				break;
			}
			case ALMACENAMIENTO:{
				//REALIZA EL TRASPASO
				int saldoDestino = Database_lib.getInstance().realizar_traspaso(recibido.getNum_tarjeta(),
						recibido.getNum_cuenta_origen(),recibido.getNum_cuenta_destino(),codonline,recibido.getImporte());
				boolean signoDestino = (saldoDestino>=0);
				
				//Obtiene el nuevo saldo en el origen
				int saldoOrigen = -1;
				saldoOrigen = Database_lib.getInstance().consultar_saldo(recibido.getNum_tarjeta(),recibido.getNum_cuenta_origen());
				
				boolean signoOrigen = (saldoOrigen>=0);
				
				//Creamos la respuesta correcta
				respuesta = new RespTraspaso(origen,destino,numcanal,nmsg,codonline,cod_resp,
						signoOrigen,saldoOrigen,signoDestino,saldoDestino);
	
				//Enviamos el mensaje
				sendToCajero(respuesta,this.input_packet.getAddress(),this.input_packet.getPort());
				break;
			}
			case ENVIO_CORRECTO:{
				//REALIZA EL TRASPASO
				Database_lib.getInstance().realizar_traspaso(recibido.getNum_tarjeta(),
						recibido.getNum_cuenta_origen(),recibido.getNum_cuenta_destino(),codonline,recibido.getImporte());
				
				//Reenviamos el mensaje al banco
				this.sendToBanco(recibido, recibido.getNum_tarjeta());
				break;
			}
		}
	}
	
	
}
