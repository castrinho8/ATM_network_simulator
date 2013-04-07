package practicaACS.consorcio;

import java.io.*;
import java.net.*;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.logging.*;

import fap.*;


public class ServidorConsorcio_Cajeros {

	private int port;
	private Consorcio consorcio;
	private EstadoSesion estado_serv_cajeros;

	/**
	 * Constructor de la clase ServidorConsorcio_Cajeros
	 */
	public ServidorConsorcio_Cajeros(Consorcio cons,int puerto) throws IOException{
		super();
		this.port = puerto;
		this.consorcio = cons;
		this.estado_serv_cajeros = EstadoSesion.ACTIVA;
	}
	
	//-------GETTERS & SETTERS-------
	public int getPuerto() {
		return port;
	}
	
    public EstadoSesion getEstado() {
		return estado_serv_cajeros;
	}

	public void setEstado(EstadoSesion estado) {
		this.estado_serv_cajeros = estado;
	}

	
	//-------END GETTERS & SETTERS-------

	/**
     * Levanta el servidorCajeros
     */
    public void levantar_servidorCajeros() throws IOException{
    	ServerSocket servidor = new ServerSocket(this.port);
    	this.estado_serv_cajeros = EstadoSesion.ACTIVA;
    	
    	Calendar time = Calendar.getInstance();
    	System.out.println("APERTURA: Sesion Servidor de Cajeros comenzada a las " + time.getTime());

    	while (this.estado_serv_cajeros == EstadoSesion.ACTIVA) {
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
    	this.estado_serv_cajeros = EstadoSesion.CERRADA;
    }
    
    
}