package practicaacs.consorcio;

import java.io.*;
import java.net.*;
import java.util.Calendar;

import practicaacs.consorcio.aux.TipoAccion;
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
		
		//Establece el servidor
		try {
			 socketServidor = new DatagramSocket(this.port);
		 }catch (IOException e) {
			 System.out.println("Error al crear el objeto socket servidor Consorcio_Cajeros::" + e.getLocalizedMessage());
			 System.exit(-1);
		 }
		//Establece un timeout
		try {
			socketServidor.setSoTimeout(100000);
		} catch (SocketException e) {
			 System.out.println("Error estableciendo el timeout");
			 e.printStackTrace();
			 System.exit(-1);
		}
	}
	
	public int getPuerto() {
		return port;
	}
	
    public boolean isOnline() {
		return abierto_serv_cajeros;
	}

    /**
     * Método RUN que ejecuta el thread del Servidor.
     */
	@Override
	public void run() {
		abrir_servidorCajeros();
		while(true){
			if(isOnline())
				recibir_servidorCajeros();
		}
	}
	
	/**
     * Prepara el servidorCajeros para recibir un envio.
     */
    public void recibir_servidorCajeros(){
    	
		byte [] recibirDatos = new byte[1024];
		
		//Crea el Datagrama en donde recibir los datos
		DatagramPacket inputPacket = new DatagramPacket(recibirDatos, recibirDatos.length);
		try{
			System.out.println("ESPERANDO");
			//Recibe datos
			socketServidor.receive(inputPacket);
			System.out.println("RECIBIDO");

			//Crea un thread para tratar el Datagrama recibido
			Thread t = new ConexionConsorcio_Cajeros(TipoAccion.CONEXION,inputPacket,this.consorcio,this.socketServidor);
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
    public void sendToCajero(MensajeDatos respuesta, InetAddress ip_destino, int puerto_destino){
		ConexionConsorcio_Cajeros c = new ConexionConsorcio_Cajeros(TipoAccion.ENVIO,respuesta,this.consorcio,socketServidor,ip_destino,puerto_destino);
    	c.start();
    }
}

