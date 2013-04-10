package practicaacs.consorcio;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Calendar;
import java.util.Hashtable;

import practicaacs.banco.estados.EstadoSesion;
import practicaacs.fap.*;


public class ServidorConsorcio_Bancos {

	private int port;
	private Consorcio consorcio;
	
	private boolean abierto_serv_bancos;
	private DatagramSocket socketServidor;

	/**
	 * Constructor de la clase ServidorConsorcio_Cajeros
	 */
	public ServidorConsorcio_Bancos(Consorcio cons,int puerto) throws IOException{
		this.consorcio = cons;
		this.port = puerto;

		this.abierto_serv_bancos = false;
		
		try {
			 socketServidor = new DatagramSocket(this.port);
		 }catch (IOException e) {
			 System.out.println("Error al crear el objeto socket servidor");
			 System.exit(-1);
		 }
		try { //Establece un timeout
			socketServidor.setSoTimeout(1000);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	
	//-------GETTERS & SETTERS-------
	public int getPuerto() {
		return port;
	}
	
	public boolean isOnline() {
		return abierto_serv_bancos;
	}
	//-------END GETTERS & SETTERS-------
	


	/**
     * Levanta el servidorBancos
     */
    public void levantar_servidorBancos() throws ClassNotFoundException, IOException{
    	ServerSocket servidor = new ServerSocket(this.port);

    	Calendar time = Calendar.getInstance();
    	System.out.println("APERTURA: Sesion Servidor de Bancos comenzada a las " + time.getTime());

		while (this.abierto_serv_bancos) {
			Socket incoming = servidor.accept();

			Thread t = new ConexionConsorcio_Bancos(this.consorcio,this,incoming);
		    t.start();
		}
    	
    	time = Calendar.getInstance();
    	System.out.println("CIERRE: Sesion Servidor de Bancos termidada a las " + time.getTime());
    }

    /**
     * Cierra el servidorBancos
     */
    public void cierra_servidorBancos(){
    	this.abierto_serv_bancos = false;
    	//cerrar todas las conexiones con los bancos
    	for(Object sesion : Database_lib.getInstance().getSesiones()){

    	}
    }

    /**
     * Recupera los mensajes del banco
     */
    public void realiza_recuperacion(String banco){
    	for(Object message : this.conexiones.get(banco)){
    		this.consorcio.getBancos_client().send_message((Mensaje) message);
    	}
    }
    
}





