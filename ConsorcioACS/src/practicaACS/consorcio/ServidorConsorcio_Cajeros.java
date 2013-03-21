package practicaACS.consorcio;

import java.io.*;
import java.net.*;
import java.util.Calendar;
import java.util.logging.*;

import practicaACS.bancos.EstadoSesion;
import practicaACS.mensajes.Mensaje;

public class ServidorConsorcio_Cajeros {

	private int port;
	private InetAddress address;
	private EstadoSesion estado;
	private Consorcio consorcio;
	
	/**
	 * Constructor de la clase ServidorConsorcio_Cajeros
	 */
	public ServidorConsorcio_Cajeros(Consorcio cons) throws IOException{
		super();
		//cargar los datos de fichero
		int puerto = 2002;
		InetAddress direccion = InetAddress.getByName("127.0.0.1");
		this.port = puerto;
		this.address = direccion;
		this.consorcio = cons;
	}
	
	//-------GETTERS & SETTERS-------
	public int getPuerto() {
		return port;
	}
	
    public InetAddress getAddress() {
		return address;
	}

    public EstadoSesion getEstado() {
		return estado;
	}

	public void setEstado(EstadoSesion estado) {
		this.estado = estado;
	}

	//-------END GETTERS & SETTERS-------

	/**
     * Levanta el servidorCajeros
     */
    public void levantar_servidorCajeros() throws IOException{
    	ServerSocket servidor = new ServerSocket(this.port);
    	this.estado = EstadoSesion.ACTIVA;
    	
    	Calendar time = Calendar.getInstance();
    	System.out.println("APERTURA: Sesion Servidor de Cajeros comenzada a las " + time.getTime());

    	while (this.estado == EstadoSesion.ACTIVA) {
    	     Socket incoming = servidor.accept();

    	     Thread t = new ConexionConsorcio_Cajeros(this.consorcio,incoming);
    	     t.start();
    	}
    	
    	time = Calendar.getInstance();
    	System.out.println("CIERRE: Sesion Servidor de Cajeros termidada a las " + time.getTime());
    }

    /**
     * Cierra el servidorCajeros
     */
    public void cierra_servidorCajeros(){
    	this.estado = EstadoSesion.CERRADA;
    }
    
    
}