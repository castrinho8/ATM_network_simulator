package practicaacs.consorcio;

import java.net.Socket;


public enum EstadoEnvio{

	ENVIO_CORRECTO(1),RECHAZAR_PETICION(2),ALMACENAMIENTO(3);
	private int numero;

	private EstadoEnvio(int numero){
		this.numero = numero;
	}
	
}
	

/*
public class Sesion {

	private static int next_id_sesion = 0;
	
	private String id_banco;
	private int id_consorcio;
	private int id_sesion;
	private ServidorConsorcio_Bancos servidor;
	
	private Socket socket; //Socket de conexion con el banco
	private int send_port; //Puerto al que enviar los mensajes
	
	public Sesion(String id_banco, int id_consorcio,ServidorConsorcio_Bancos servidor,Socket socket) {
		super();
		this.id_banco = id_banco;
		this.id_consorcio = id_consorcio;
		this.servidor = servidor;

		this.id_sesion = next_id_sesion++;
		this.socket = socket;
		this.send_port = socket.getPort();
	}
}	
*/
	
	
	
	
