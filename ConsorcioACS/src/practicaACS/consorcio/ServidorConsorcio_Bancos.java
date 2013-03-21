package practicaACS.consorcio;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import org.omg.CORBA.portable.InputStream;

import practicaACS.mensajes.Mensaje;


public class ServidorConsorcio_Bancos {
//tiene que estar esperando por peticiones de abrir sesiones con bancos
	
	private Consorcio consorcio;
	private ServerSocket socket_consorcio;
	
	/**
	 * Constructor de la clase ServidorConsorcio_Bancos
	 * @param cons
	 * @param address
	 * @param port
	 * @throws IOException
	 */
	public void ServidorConsorcio(Consorcio cons,int port) throws IOException{
		this.socket_consorcio = new ServerSocket(port);
		this.consorcio = cons;
	}
	
	private void start_conection(Socket conexion){
		Thread hilo = new Thread();
		System.out.println("Connection received from Player 1 "+conexion.getInetAddress().getHostName());
		Mensaje message = buffer.readObject();
		if (message != null){ //Hacer cosas con el mensaje
			System.out.println("MENSAJEE");
		}
		hilo.join();//Elimina el thread cuando acaba la conexion
	}
	
	public void start_server() throws IOException{
		System.out.println("Waiting for connection...");
		
		while(this.consorcio.getEstadoSesion() == EstadoSesion.ACTIVA){
			Socket conexion = this.socket_consorcio.accept();
			start_conection(conexion);
		}

	}
	
	public void stop_server() throws IOException{
		this.consorcio.setEstadoSesion(EstadoSesion.TRAFICO_DETENIDO);

		this.socket_consorcio.close();
		this.consorcio.setEstadoSesion(EstadoSesion.CERRADA);
	}
	
}
