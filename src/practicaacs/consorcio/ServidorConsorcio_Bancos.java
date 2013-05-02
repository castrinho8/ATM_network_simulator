package practicaacs.consorcio;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;

import practicaacs.banco.estados.EstadoSesion;
import practicaacs.banco.estados.SesAberta;
import practicaacs.consorcio.aux.Sesion;
import practicaacs.consorcio.bd.Database_lib;
import practicaacs.fap.*;


public class ServidorConsorcio_Bancos {

	private int port;
	private Consorcio consorcio;
	
	private boolean abierto_serv_bancos;
	private DatagramSocket socketServidor;
	//Banco,sesion
	private HashMap<String,Sesion> sesiones;
	
	/**
	 * Constructor de la clase ServidorConsorcio_Cajeros
	 * @param cons El consorcio asociado al servidor.
	 * @param puerto El puerto en el que se establece.
	 */
	public ServidorConsorcio_Bancos(Consorcio cons,int puerto) throws IOException{
		this.consorcio = cons;
		this.port = puerto;

		this.abierto_serv_bancos = false;
		this.sesiones = new HashMap<String,Sesion>();
		
		try {
			 socketServidor = new DatagramSocket(this.port);
		 }catch (IOException e) {
			 System.out.println("Error al crear el objeto socket servidor Consorcio_Bancos");
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
	
	public Sesion getSesion(String id_banco){
		return this.sesiones.get(id_banco);
	}

	public void insertaSesion(Sesion ses){
    	this.sesiones.put(ses.getId(),ses);
    }
	
	public void eliminarSesion(String id_banco){
		this.sesiones.remove(id_banco);
	}
	//-------END GETTERS & SETTERS-------
	


	/**
     * Levanta el servidorBancos hasta que la variable que controla el estado se ponga a False.
     * El servidor espera la recepcion de mensajes y para cada uno crea un thread para realizar las tareas
     * que sean necesarias.
     */
    public void levantar_servidorBancos() throws ClassNotFoundException, IOException{
    	
    		byte [] recibirDatos = new byte[1024];
    		
        	Calendar time = Calendar.getInstance();
        	System.out.println("APERTURA: Sesion Servidor de Bancos comenzada a las " + time.getTime());
        	this.abierto_serv_bancos = true;

    		while(this.abierto_serv_bancos){
    			//Crea el Datagrama en donde recibir los datos
    			DatagramPacket inputPacket = new DatagramPacket(recibirDatos, recibirDatos.length);
    			try{
    				//Recibe datos
    				socketServidor.receive(inputPacket);
    				
    				//Crea una conexión para analizar el datagrama
    				ConexionConsorcio_Bancos t = new ConexionConsorcio_Bancos(inputPacket,this.consorcio,this,this.socketServidor);
    				t.start();
    				
    			}catch (SocketTimeoutException e){
    				cierra_servidorBancos();
    			}catch (IOException e) {
    				System.out.println("Error al recibir");
    				System.exit ( 0 );
    			}
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
     * Método que envia un mensaje de solicitar Recuperacion
     * @param id_banco 
     */
    public void solicitar_recuperacion(String id_banco){
    	
    }
    
    /**
     * Método que envia un mensaje de finalizar recuperacion
     * @param id_banco
     */
    public void solicitar_fin_recuperacion(String id_banco){
    	
    }
    
    /**
     * Recupera los mensajes del banco
     */
    public void realiza_recuperacion(String banco){
    	ConexionConsorcio_Bancos c = new ConexionConsorcio_Bancos(null, consorcio, this, socketServidor);
    	c.iniciar_recuperacion(banco);
    }
    
    
    /**
     * Método que envia el mensaje pasado por parámetro.
     * @param message El mensaje a enviar.
     */
    public void send_message(Mensaje message){
    	
    	//CAMBIAR TODOOOO PARA CREAR UN THREAD QUE HAGA EL ENVIO y acordarse de poner los timers
    	
    	//Obtiene el banco al que enviar
    	String id_banco = message.getDestino();
    	
    	//Obtiene de la base de datos la IP y PUERTO del banco al que enviar
    	InetAddress dir = Database_lib.getInstance().getDestinationAddress(id_banco);
    	int puerto = Database_lib.getInstance().getPuerto(id_banco);
    	
		//Creamos el datagrama
		DatagramPacket enviarPaquete = new DatagramPacket(message.getBytes(),message.size(),dir,puerto);
		
		try{
			//Enviamos el mensaje
			this.socketServidor.send(enviarPaquete);
		}catch (IOException e) {
			System.out.println("Error al enviar");
			System.exit ( 0 );
		}
    }
    
}

