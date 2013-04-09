package practicaacs.consorcio;

import java.io.*;
import java.net.*;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.logging.*;

import practicaacs.banco.estados.EstadoSesion;
import practicaacs.fap.*;


public class ServidorConsorcio_Cajeros {

	private int port;
	private Consorcio consorcio;
	private EstadoSesion estado_serv_cajeros;
	private DatagramSocket socketServidor;

	/**
	 * Constructor de la clase ServidorConsorcio_Cajeros
	 */
	public ServidorConsorcio_Cajeros(Consorcio cons,int puerto) throws IOException{
		super();
		this.port = puerto;
		this.consorcio = cons;
		this.estado_serv_cajeros = EstadoSesion.ACTIVA;
		
		try {
			 socketServidor = new DatagramSocket(puerto);
		 }catch (IOException e) {
			 System.out.println("Error al crear el objeto socket servidor");
			 System.exit(-1);
		 }
		
		try {
			socketServidor.setSoTimeout(1000);
		} catch (SocketException e) {
			e.printStackTrace();
		}
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
    	
		Mensaje msgEnviar;
		byte [] recibirDatos = new byte[1024];
		
    	Calendar time = Calendar.getInstance();
    	System.out.println("APERTURA: Sesion Servidor de Cajeros comenzada a las " + time.getTime());
    	this.estado_serv_cajeros = EstadoSesion.ACTIVA;

		while(true){
		
			DatagramPacket inputPacket = new DatagramPacket(recibirDatos, recibirDatos.length);

			try{
				socketServidor.receive(inputPacket);
				
				//this.analizarMensaje(recibirPaquete.getData());
	    	    Thread t = new ConexionConsorcio_Cajeros(inputPacket,this.consorcio,this,this.socketServidor);
	    	    t.start();
	    	     
			}catch (SocketTimeoutException e){
			
			}catch (IOException e) {
				System.out.println("Error al recibir");
				System.exit ( 0 );
			}
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