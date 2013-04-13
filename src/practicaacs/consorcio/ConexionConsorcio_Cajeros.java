package practicaacs.consorcio;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;

import practicaacs.consorcio.aux.Movimiento;
import practicaacs.fap.*;

public class ConexionConsorcio_Cajeros extends Thread{
	
	private Consorcio consorcio;
	private ServidorConsorcio_Cajeros servidor;
	
	private DatagramPacket input_packet;
	private DatagramSocket output_socket;
	
	/**
	 * Constructor de la clase.
	 * @param paquete El paquete a enviar.
	 * @param cons El consorcio
	 * @param socket
	 */
	public ConexionConsorcio_Cajeros(DatagramPacket paquete,Consorcio cons,ServidorConsorcio_Cajeros s, DatagramSocket socket) {
		super();
		this.servidor = s;
		this.consorcio = cons;
		
		this.input_packet = paquete;
		this.output_socket = socket;
	}
	
	
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
	 * Funcion que envia el mensaje pasado por parámetro con los datos de la conexion.
	 * @param respuesta El mensaje a enviar
	 * @param address La direccion a la que enviar
	 * @param port El puerto a donde enviar
	 */
	public void send_message(Mensaje respuesta,InetAddress address,int port){
		
		System.out.printf(respuesta.toString());
		
		//Creamos el datagrama
		DatagramPacket enviarPaquete = new DatagramPacket(respuesta.getBytes(),respuesta.size(),address,port);
		
		try{
			//Enviamos el mensaje
			this.output_socket.send(enviarPaquete);
		}catch (IOException e) {
			System.out.println("Error al enviar");
			System.exit ( 0 );
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
				send_message(respuesta,this.output_socket.getInetAddress(),this.output_socket.getPort());
				break;
			}
			case ALMACENAMIENTO:{
				//Almacenamos el envio en la BD
				Database_lib.getInstance().almacenar_envio(recibido);
				
				//REALIZA LA CONSULTA
				int saldo = Database_lib.getInstance().consultar_saldo(recibido.getNum_tarjeta(),recibido.getNum_cuenta());
				boolean signo = (saldo>=0);
				
				//Creamos la respuesta
				respuesta = new RespSaldo(origen,destino,numcanal,nmsg,codonline,cod_resp,signo,saldo);

				//Enviamos el mensaje
				send_message(respuesta,this.output_socket.getInetAddress(),this.output_socket.getPort());
				break;
			}
			case ENVIO_CORRECTO:{
				//No respondemos en caso de que se haya reenviado.
				break;
			}
		}
	}
	
			
	
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
				send_message(respuesta,this.output_socket.getInetAddress(),this.output_socket.getPort());
				break;
			}
			case ALMACENAMIENTO:{
				//Almacenamos el envio en la BD
				Database_lib.getInstance().almacenar_envio(recibido);

				//Obtenemos los movimientos de la BD
				ArrayList<Movimiento> movimientos = 
						Database_lib.getInstance().consultar_movimientos(recibido.getNum_tarjeta(),recibido.getNum_cuenta());
				
				//Enviamos todos los movimientos realizados
				for(Movimiento m : movimientos){
					//Creamos la respuesta correcta.
					respuesta = new RespMovimientos(origen,destino,numcanal,nmsg,codonline,cod_resp,
							movimientos.size(),m.tipo,(m.importe>=0),m.importe,m.data);
				
					//Enviamos el mensaje
					send_message(respuesta,this.output_socket.getInetAddress(),this.output_socket.getPort());
				}
				break;
			}
			case ENVIO_CORRECTO:{
				//No respondemos en caso de que se haya reenviado.
				break;
			}
		}
	}
	
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
				send_message(respuesta,this.output_socket.getInetAddress(),this.output_socket.getPort());
				break;
			}
			case ALMACENAMIENTO:{
				//Almacenamos el envio en la BD
				Database_lib.getInstance().almacenar_envio(recibido);

				//REALIZA EL REINTEGRO
				int saldo = Database_lib.getInstance().realizar_reintegro(recibido.getNum_tarjeta(),recibido.getNum_cuenta(),recibido.getImporte());
				boolean signo = saldo >= 0;
				
				//Creamos la respuesta correcta
				respuesta = new RespReintegro(origen,destino,numcanal,nmsg,codonline,cod_resp,signo,saldo); 

				//Enviamos el mensaje
				send_message(respuesta,this.output_socket.getInetAddress(),this.output_socket.getPort());
				break;
			}
			case ENVIO_CORRECTO:{
				//No respondemos en caso de que se haya reenviado.
				break;
			}
		}
		
	}
	
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
				send_message(respuesta,this.output_socket.getInetAddress(),this.output_socket.getPort());
				break;
			}
			case ALMACENAMIENTO:{
				//Almacenamos el envio en la BD
				Database_lib.getInstance().almacenar_envio(recibido);
	
				//REALIZA EL ABONO
				int saldo = Database_lib.getInstance().realizar_abono(recibido.getNum_tarjeta(),recibido.getNum_cuenta(),recibido.getImporte());
				boolean signo = saldo >= 0;
				
				//Creamos la respuesta correcta
				respuesta = new RespAbono(origen,destino,numcanal,nmsg,codonline,cod_resp,signo,saldo);
	
				//Enviamos el mensaje
				send_message(respuesta,this.output_socket.getInetAddress(),this.output_socket.getPort());
				break;
			}
			case ENVIO_CORRECTO:{
				//No respondemos en caso de que se haya reenviado.
				break;
			}
		}
	}
	
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
				send_message(respuesta,this.output_socket.getInetAddress(),this.output_socket.getPort());
				break;
			}
			case ALMACENAMIENTO:{
				//Almacenamos el envio en la BD
				Database_lib.getInstance().almacenar_envio(recibido);
	
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
				send_message(respuesta,this.output_socket.getInetAddress(),this.output_socket.getPort());
				break;
			}
			case ENVIO_CORRECTO:{
				//No respondemos en caso de que se haya reenviado.
				break;
			}
		}
	}
	
	
	private void analizar_mensaje(Mensaje recibido){
		switch(recibido.getTipoMensaje()){
			case SOLSALDO:
				consultar_saldo((SolSaldo) recibido);
				break;
			case SOLMOVIMIENTOS:
				consultar_movimientos((SolMovimientos) recibido);
				break;
			case SOLREINTEGRO:
				realizar_reintegro((SolReintegro) recibido);
				break;
			case SOLABONO:
				realizar_abono((SolAbono) recibido);
				break;
			case SOLTRASPASO:
				realizar_traspaso((SolTraspaso) recibido);
				break;
			default:
				System.out.println("Error: Tipo de mensaje no reconocido.");
				break;
		}
	}
	
	/**
	 * En funcion del estado del protocolo, reenvia los datos al banco, los almacena en la BD o rechaza la peticion.
	 * Devuelve False si se rechaza la peticion y True en el resto de casos.
	 */
	private EstadoEnvio obtiene_tipo_envio(Mensaje recibido,boolean protocolo,CodigosRespuesta cod_resp){
		
		if(!cod_resp.equals(CodigosRespuesta.CONSACEPTADA))
			return EstadoEnvio.RECHAZAR_PETICION;
		
		if(!protocolo){
			if(recibido.es_consulta()){
				//rechazar peticion
				return EstadoEnvio.RECHAZAR_PETICION;
			}else{
				//almacenar en la base de datos
				return EstadoEnvio.ALMACENAMIENTO;
			}
		}else{
			//reenviar al banco
			return EstadoEnvio.ENVIO_CORRECTO;
		}
	}
	
}
