package practicaacs.consorcio;

import java.io.*;
import java.net.*;
import java.util.Calendar;

import practicaacs.consorcio.aux.TipoOrigDest;
import practicaacs.consorcio.bd.Database_lib;
import practicaacs.fap.Mensaje;
import practicaacs.fap.MensajeDatos;

/**
 * Clase que implementa un servidor para la recepción de mensajes de los cajeros.
 *
 */
public class ServidorConsorcio_Cajeros extends Thread{

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
			socketServidor.setSoTimeout(100000);
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

	@Override
	public void run() {
		abrir_servidorCajeros();
		while(true){
			try {
				if(isOnline())
					recibir_servidorCajeros();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
     * Levanta el servidorCajeros
     */
    public void recibir_servidorCajeros() throws IOException{
    	
		byte [] recibirDatos = new byte[1024];
		
		//Crea el Datagrama en donde recibir los datos
		DatagramPacket inputPacket = new DatagramPacket(recibirDatos, recibirDatos.length);
		try{
			//Recibe datos
			socketServidor.receive(inputPacket);

			//Crea un thread para tratar el Datagrama recibido
			Thread t = new ConexionConsorcio_Cajeros(inputPacket,this.consorcio,this.socketServidor);
			t.start();

		}catch (SocketTimeoutException e){
			cerrar_servidorCajeros();
		}catch (IOException e) {
			System.out.println("Error al recibir");
			System.exit ( 0 );
		}
    }
    	
    /**
     * Abre el servidorCajeros
     */
    public void abrir_servidorCajeros(){
    	this.abierto_serv_cajeros = true;

    	Calendar time = Calendar.getInstance();
    	System.out.println("APERTURA: Sesion Servidor de Cajeros comenzada a las " + time.getTime());
    }
    
    /**
     * Cierra el servidorCajeros
     */
    public void cerrar_servidorCajeros(){
    	this.abierto_serv_cajeros = false;

    	Calendar time = Calendar.getInstance();
    	System.out.println("CIERRE: Sesion Servidor de Cajeros termidada a las " + time.getTime());
    }
    
    /**
     * Método que envia la respuestas del MENSAJE DE DATOS al cajero.
     * BANCOS->CONSORCIO->CAJERO
     * @param respuesta El mensaje a enviar.
     */
    public void reply_message(MensajeDatos respuesta, InetAddress ip, int puerto){
		
    	//Guardamos el Mensaje en la BD (Tabla de MENSAJES)
		Database_lib.getInstance().almacenar_mensaje(respuesta,TipoOrigDest.CONSORCIO,respuesta.getOrigen(),TipoOrigDest.CAJERO,respuesta.getDestino());

		//Creamos el datagrama
		DatagramPacket enviarPaquete = new DatagramPacket(respuesta.getBytes(),respuesta.size(),ip,puerto);
		
		try{
			//Enviamos el mensaje
			this.socketServidor.send(enviarPaquete);
		}catch (IOException e) {
			System.out.println("Error al enviar");
			System.exit ( 0 );
		}
    }
}

