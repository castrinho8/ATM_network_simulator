package practicaacs.consorcio;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.util.Date;

import practicaacs.fap.*;

public class ConexionConsorcio_Cajeros extends Thread{
	
	static private int num_next_message = 0;
	
	private DatagramPacket input_packet;
	private DatagramSocket output_socket;
	
	private Consorcio consorcio;
	private ServidorConsorcio_Cajeros servidor;

	public ConexionConsorcio_Cajeros(DatagramPacket paquete,Consorcio cons,ServidorConsorcio_Cajeros server,DatagramSocket socket) {
		super();
		this.input_packet = paquete;
		this.consorcio = cons;
		this.servidor = server;
		
		this.output_socket = ;

	}
	
	public void run() {
		try {
			
			//Creamos el mensaje correspondiente al recibido
			Mensaje recibido = analizarMensaje(this.input_packet.getData());
			
			System.out.printf(recibido.toString());
			
			//Creamos la respuesta
			Mensaje respuesta = generar_respuesta(recibido);
			
			System.out.printf(respuesta.toString());

			//Enviamos el mensaje
			while ((msgEnviar = mensajesAEnviar.poll()) != null){
				DatagramPacket enviarPaquete = new DatagramPacket(msgEnviar.getBytes(), msgEnviar.size(), this.direccionIP, this.puerto);
	
				try{
					socketServidor.send(enviarPaquete);
				}catch (IOException e) {
					System.out.println("Error al enviar");
					System.exit ( 0 );
				}
			}
		}
		catch (Exception e) {
	    // manipular las excepciones
		}
	}
	
	
	public RespSaldo consultar_saldo(SolSaldo recibido){
		
		//cabecera
		String origen = Integer.toString(this.consorcio.getId_consorcio());
		String destino = recibido.getOrigen();
		//subcabecera
		int numcanal = 0;
		int nmsg = 0;
		boolean codonline = Database_lib.getInstance().consultar_protocolo(destino);
		//cuerpo
		CodigosRespuesta cod_resp = 
				Database_lib.getInstance().comprobar_condiciones(recibido.getNum_tarjeta(),recibido.getNum_cuenta());
		int saldo = Database_lib.getInstance().consultar_saldo(recibido.getNum_tarjeta(),recibido.getNum_cuenta());
		boolean signo = (saldo>=0);
				
		RespSaldo respuesta = new RespSaldo(origen,destino,numcanal,nmsg,codonline,cod_resp,signo,saldo);
		
		if(!procesa_datos(recibido,respuesta,codonline)) //Si la peticion debe ser rechazada
			respuesta = new RespSaldo(origen,destino,numcanal,nmsg,false,CodigosRespuesta.CONSDEN,true,0);

		return respuesta;
	}
	
			
	
	public RespMovimientos consultar_movimientos(SolMovimientos recibido){

		//cabecera
		String origen = Integer.toString(this.consorcio.getId_consorcio());
		String destino = recibido.getOrigen();
		//subcabecera
		int numcanal = 0;
		int nmsg = 0;
		boolean codonline = Database_lib.getInstance().consultar_protocolo(destino);
		//cuerpo
		CodigosRespuesta cod_resp = 
				Database_lib.getInstance().comprobar_condiciones(recibido.getNum_tarjeta(),recibido.getNum_cuenta());
		int nmovimientos;
		CodigosMovimiento tipo_mov;
		boolean signo;
		int importe;
		Date fecha;		
		
					
		RespMovimientos respuesta = new RespMovimientos(origen,destino,numcanal, nmsg,codonline,cod_resp,
				nmovimientos,tipo_mov,signo,importe,fecha);
		
		if(!procesa_datos(recibido,respuesta,codonline)) //Si la peticion debe ser rechazada
			respuesta = new RespMovimientos(origen,destino,numcanal,nmsg,false,CodigosRespuesta.CONSDEN,
					nmovimientos,tipo_mov,signo,importe,fecha);

		return respuesta;
	}
	
	public Mensaje realizar_reintegro(Mensaje recibido){
		Mensaje respuesta = null;
		String origen = "";
		String destino = "";
		int numcanal;
		int nmsg;
		boolean codonline;
		CodigosRespuesta cod_resp;
		boolean signo;
		long saldo;
		
	//	respuesta = new RespReintegro(origen,destino,numcanal,nmsg,codonline,cod_resp,signo,saldo);
		return respuesta;
	}
	
	public Mensaje realizar_abono(Mensaje recibido){
		Mensaje respuesta = null;
		String origen = "";
		String destino = "";
		int numcanal;
		int nmsg;
		boolean codonline;
		CodigosRespuesta cod_resp;
		boolean signo;
		long saldo;
		
	//	respuesta = new RespAbono(origen,destino,numcanal,nmsg,codonline,cod_resp,signo,saldo);
		return respuesta;
	}
	
	public Mensaje realizar_traspaso(Mensaje recibido){
		Mensaje respuesta = null;
		String origen = "";
		String destino = "";
		int numcanal;
		int nmsg;
		boolean codonline;
		CodigosRespuesta cod_resp;
		boolean signo;
		long saldo;
		
	//	respuesta = new RespTraspaso(origen,destino,numcanal,nmsg,codonline,cod_resp,signo,saldo);
		return respuesta;
	}
	
	
	Mensaje generar_respuesta(Mensaje recibido){
		Mensaje respuesta = null;

		switch(recibido.getTipoMensaje()){
			case SOLSALDO:
				respuesta = consultar_saldo((SolSaldo) recibido);
				break;
			case SOLMOVIMIENTOS:
				respuesta = consultar_movimientos((SolMovimientos) recibido);
				break;
			case SOLREINTEGRO:
				respuesta = realizar_reintegro(recibido);
				break;
			case SOLABONO:
				respuesta = realizar_abono(recibido);
				break;
			case SOLTRASPASO:
				respuesta = realizar_traspaso(recibido);
				break;
			default:
				System.out.println("Error: Tipo de mensaje no reconocido.");
				break;
		}
		return respuesta;
	}
	
	/**
	 * En funcion del estado del protocolo, reenvia los datos al banco, los almacena en la BD o rechaza la peticion.
	 * Devuelve False si se rechaza la peticion y True en el resto de casos.
	 */
	private boolean procesa_datos(Mensaje recibido,Mensaje respuesta,boolean protocolo){
		if(!protocolo){
			if(respuesta.es_consulta()){
				//rechazar peticion
				return false;
			}else{
				//almacenar en la base de datos
				Database_lib.getInstance().almacenar_envio(recibido);
			}
		}else{
			//reenviar al banco
			this.consorcio.getBancos_client().send_message(recibido);
		}
		return true;
	}
	
}
