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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import practicaacs.banco.estados.EstadoSesion;
import practicaacs.banco.estados.SesAberta;
import practicaacs.consorcio.aux.Sesion;
import practicaacs.consorcio.aux.TipoAccion;
import practicaacs.consorcio.bd.Database_lib;
import practicaacs.fap.*;


public class ServidorConsorcio_Bancos extends Thread{

	private int port;
	private Consorcio consorcio;
	
	private boolean abierto_serv_bancos;
	private DatagramSocket socketServidor;
	
	private HashMap<String,Sesion> sesiones; //Banco,sesion
	
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
	}
	
	//-------GETTERS & SETTERS-------
	public int getPuerto() {
		return port;
	}
	
	public boolean isOnline() {
		return this.abierto_serv_bancos;
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
	
	@Override
	public void run() {
		abrir_servidorBancos();
		recibir_servidorBancos();
	}

	/**
     * Levanta el servidorBancos hasta que la variable que controla el estado se ponga a False.
     * El servidor espera la recepcion de mensajes y para cada uno crea un thread para realizar las tareas
     * que sean necesarias.
	 * @throws IOException 
     */
    public void recibir_servidorBancos(){
    		
    		byte [] recibirDatos = new byte[1024];

    		//Crea el Datagrama en donde recibir los datos
			DatagramPacket inputPacket = new DatagramPacket(recibirDatos, recibirDatos.length);
			try{
				while(abierto_serv_bancos){
					//Recibe datos
					socketServidor.receive(inputPacket);
					
					if(isOnline()){
						//Crea una conexión para analizar el datagrama
						ConexionConsorcio_Bancos t = new ConexionConsorcio_Bancos(TipoAccion.CONEXION,inputPacket,this.consorcio,this,this.socketServidor);
						t.start();
					}
					sleep(1000);
				}
			}catch (SocketException s){
				cerrar_servidorBancos();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}catch (IOException e) {
				System.out.println("IO EXCEPTION");
			}
    }
    
    /**
     * Método que levanta el servidor de Bancos
     */
    private void abrir_servidorBancos(){
    	this.abierto_serv_bancos = true;

    	try {
			 socketServidor = new DatagramSocket(this.port);
		 }catch (IOException e) {
			 System.out.println("Error al crear el objeto socket servidor Consorcio_Bancos");
			 System.exit(-1);
		 }

    	Calendar time = Calendar.getInstance();
    	System.out.println("APERTURA: Sesion Servidor de Bancos comenzada a las " + time.getTime());
    }
    
    /**
     * Cierra el servidorBancos.
     * Para ello cierra todas las sesiones con los bancos
     * @throws IOException 
     */
    public void cerrar_servidorBancos(){
    	this.abierto_serv_bancos = false;
    	
    	if(!this.socketServidor.isClosed())
    		this.socketServidor.close();
    	
    	//Obtenemos todos los bancos que tienen sesion en el consorcio y los cerramos en la BD del consorcio.
    	ArrayList<String> i = Database_lib.getInstance().getSesiones(1);
    	i.addAll(Database_lib.getInstance().getSesiones(3));
    	i.addAll(Database_lib.getInstance().getSesiones(4));
    	String id_banco;
    	Iterator it = i.iterator();
    	//Cerrar todas las conexiones con los bancos
    	while(it.hasNext()){
    		id_banco = (String) it.next(); 
    		Database_lib.getInstance().cerrar_sesion(id_banco);
    	}

		Calendar time = Calendar.getInstance();
    	System.out.println("CIERRE: Sesion Servidor de Bancos termidada a las " + time.getTime());
    }

    
    /**
     * Método que envia un mensaje de solicitar Recuperacion
     * @param id_banco 
     */
    public void solicitar_recuperacion(String id_banco){
    	ConexionConsorcio_Bancos c = new ConexionConsorcio_Bancos(TipoAccion.RECUPERACION, id_banco, consorcio, this, socketServidor);
    	c.start();
    }
    
    /**
     * Método que envia un mensaje de finalizar recuperacion
     * @param id_banco
     */
    public void solicitar_fin_recuperacion(String id_banco){
    	ConexionConsorcio_Bancos c = new ConexionConsorcio_Bancos(TipoAccion.FIN_RECUPERACION,id_banco, consorcio, this, socketServidor);
    	c.start();
    }
    
    /**
     * Método que envia el mensaje pasado por parámetro.
     * CAJERO->CONSORCIO->BANCOS
     * @param message El mensaje a enviar.
     */
    public void sendToBanco(MensajeDatos message,String id_cajero){
    	ConexionConsorcio_Bancos c = new ConexionConsorcio_Bancos(TipoAccion.ENVIO,message,id_cajero,consorcio,this,socketServidor);
    	c.start();
    }
    
}

