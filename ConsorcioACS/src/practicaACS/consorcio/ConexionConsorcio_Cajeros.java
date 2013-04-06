package practicaACS.consorcio;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Date;

import fap.*;

public class ConexionConsorcio_Cajeros extends Thread{
	
	static private int num_next_message = 0;
	
	private Socket socket;
	private Consorcio consorcio;

	public ConexionConsorcio_Cajeros(Consorcio cons,Socket socket) {
		super();
		this.socket = socket;
		this.consorcio = cons;
	}
	
	public void run() {
		try {
			Mensaje respuesta = null;
			Mensaje recibido = null;
			
			//Creamos los buffers
			OutputStream os = this.socket.getOutputStream();  
			ObjectOutputStream o_buffer = new ObjectOutputStream(os);  
			InputStream is = this.socket.getInputStream();  
			ObjectInputStream i_buffer = new ObjectInputStream(is);  
			
			//Recibimos el mensaje
			recibido = (Mensaje) i_buffer.readObject();

			System.out.printf(recibido.toString());
			
			//Creamos la respuesta
			respuesta = generar_respuesta(recibido);
			
			System.out.printf(respuesta.toString());

			//Enviamos el mensaje
			o_buffer.writeObject(respuesta);
			
			os.close();
			o_buffer.close();
			this.socket.close();
		}
		catch (Exception e) {
	    // manipular las excepciones
		}
	}
	
	
	public RespSaldo consultar_saldo(ConsultaSaldo recibido){
		
		//cabecera
		String origen = Integer.toString(this.consorcio.getId_consorcio());
		String destino = recibido.getOrigen();
		//subcabecera
		int numcanal = 0;
		int nmsg = 0;
		boolean codonline = this.consorcio.getBancos_server().consultar_protocolo(destino);
		//cuerpo
		CodigosRespuesta cod_resp = 
				this.consorcio.getDatabase().comprobar_condiciones(recibido.getNum_tarjeta(),recibido.getNum_cuenta());
		int saldo = this.consorcio.getDatabase().consultar_saldo(recibido.getNum_tarjeta(),recibido.getNum_cuenta());
		boolean signo = (saldo>=0);
				
		RespSaldo respuesta = new RespSaldo(origen,destino,numcanal,nmsg,codonline,cod_resp,signo,saldo);
		
		if(!procesa_datos(recibido,respuesta,codonline)) //Si la peticion debe ser rechazada
			respuesta = new RespSaldo(origen,destino,numcanal,nmsg,false,CodigosRespuesta.CONSDEN,true,0);

		return respuesta;
	}
	
			
	
	public RespConsMovimientos consultar_movimientos(ConsultaMovimientos recibido){

		//cabecera
		String origen = Integer.toString(this.consorcio.getId_consorcio());
		String destino = recibido.getOrigen();
		//subcabecera
		int numcanal = 0;
		int nmsg = 0;
		boolean codonline = this.consorcio.getBancos_server().consultar_protocolo(destino);
		//cuerpo
		CodigosRespuesta cod_resp = 
				this.consorcio.getDatabase().comprobar_condiciones(recibido.getNum_tarjeta(),recibido.getNum_cuenta());
		int nmovimientos;
		TiposMovimiento tipo_mov;
		boolean signo;
		int importe;
		Date fecha;		
		
		RespConsMovimientos respuesta = new RespConsMovimientos(origen,destino,numcanal, nmsg,codonline,cod_resp,
				nmovimientos,tipo_mov,signo,importe,fecha);
		
		if(!procesa_datos(recibido,respuesta,codonline)) //Si la peticion debe ser rechazada
			respuesta = new RespConsMovimientos(origen,destino,numcanal,nmsg,false,CodigosRespuesta.CONSDEN,
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
			case CONSULTARSALDO:
				respuesta = consultar_saldo((ConsultaSaldo) recibido);
				break;
			case CONSULTARMOVIMIENTOS:
				respuesta = consultar_movimientos(recibido);
				break;
			case REINTEGRO:
				respuesta = realizar_reintegro(recibido);
				break;
			case ABONO:
				respuesta = realizar_abono(recibido);
				break;
			case TRASPASO:
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
				this.consorcio.getDatabase().almacenar_envio(recibido);
			}
		}else{
			//reenviar al banco
			this.consorcio.getBancos_client().send_message(recibido);
		}
		return true;
	}
	
}