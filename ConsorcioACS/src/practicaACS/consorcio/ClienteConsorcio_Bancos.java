package practicaACS.consorcio;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;


public class ClienteConsorcio_Bancos {
	
	private Consorcio consorcio;
	private Socket socket_consorcio;
	
	/**
	 * Constructor de la clase ClienteConsorcio_Bancos
	 * @param cons
	 * @param address
	 * @param port
	 * @throws IOException
	 */
	public void ClienteConsorcio_Bancos(Consorcio cons, InetAddress address,int port) throws IOException{
		this.socket_consorcio = new Socket(address,port);
		this.consorcio = cons;
	}
	
	public void start_client(){
		
	}
	
	public void stop_client(){
		
	}
	
}
