package practicaacs.consorcio;

import java.io.*;
import java.net.*;
import java.util.Calendar;


public class ServidorConsorcio_Cajeros {

	private int port;
	private Consorcio consorcio;
	
	private boolean abierto_serv_cajeros;
	private DatagramSocket socketServidor;

	/**
	 * Constructor de la clase ServidorConsorcio_Cajeros
	 * @param cons Consorcio asociado.
	 * @param puerto El puerto en el que se va a establecer el servidor.
	 */
	public ServidorConsorcio_Cajeros(Consorcio cons,int puerto) throws IOException{
		this.consorcio = cons;
		this.port = puerto;
		this.abierto_serv_cajeros = false;
		
		try {
			 socketServidor = new DatagramSocket(this.port);
		 }catch (IOException e) {
			 System.out.println("Error al crear el objeto socket servidor Consorcio_Cajeros");
			 System.exit(-1);
		 }
		try { //Establece un timeout
			socketServidor.setSoTimeout(10000);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	
	
	//-------GETTERS & SETTERS-------
	public int getPuerto() {
		return port;
	}
	
    public boolean isOnline() {
		return abierto_serv_cajeros;
	}
	//-------END GETTERS & SETTERS-------

	/**
     * Levanta el servidorCajeros
     */
    public void levantar_servidorCajeros() throws IOException{
    	
		byte [] recibirDatos = new byte[1024];
		
    	Calendar time = Calendar.getInstance();
    	System.out.println("APERTURA: Sesion Servidor de Cajeros comenzada a las " + time.getTime());
    	this.abierto_serv_cajeros = true;

		while(this.abierto_serv_cajeros){
			//Crea el Datagrama en donde recibir los datos
			DatagramPacket inputPacket = new DatagramPacket(recibirDatos, recibirDatos.length);
			try{
				//Recibe datos
				socketServidor.receive(inputPacket);
				
				//Crea un thread para tratar el Datagrama recibido
				Thread t = new ConexionConsorcio_Cajeros(inputPacket,this.consorcio,this.socketServidor);
				t.start();

			}catch (SocketTimeoutException e){
				cierra_servidorCajeros();
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
    	this.abierto_serv_cajeros = false;
    }
    
}

