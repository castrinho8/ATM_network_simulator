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
		while(true){
			if(isOnline())
				recibir_servidorBancos();
			System.out.printf("ONLINE: %d\n",isOnline()?1:0);
		}
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
				System.out.println("RECIBIR SERVIDOR BANCOS");
				//Recibe datos
				socketServidor.receive(inputPacket);
				System.out.printf("recibe1:\n");

				if(isOnline()){
					//Crea una conexión para analizar el datagrama
					ConexionConsorcio_Bancos t = new ConexionConsorcio_Bancos(TipoAccion.CONEXION,inputPacket,this.consorcio,this,this.socketServidor);
					t.start();
				}
				System.out.printf("sale:\n");

			}catch (SocketTimeoutException e){
				System.out.println("Socket timeout");
				cerrar_servidorBancos();
			}catch (IOException e) {
				System.out.println("IO EXCEPTION");
			}
        }
    
    
    public void abrir_servidorBancos(){

    	try {
			 socketServidor = new DatagramSocket(this.port);
		 }catch (IOException e) {
			 System.out.println("Error al crear el objeto socket servidor Consorcio_Bancos");
			 System.exit(-1);
		 }
		
    	try { //Establece un timeout
			this.socketServidor.setSoTimeout(100000);
		} catch (SocketException e) {
			e.printStackTrace();
		}

    	this.abierto_serv_bancos = true;

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
    	this.socketServidor.close();
    	
		Calendar time = Calendar.getInstance();
    	System.out.println("CIERRE: Sesion Servidor de Bancos termidada a las " + time.getTime());
    	
    	/*ArrayList<String> i = Database_lib.getInstance().getSesiones();
    	String id_banco;
    	//Cerrar todas las conexiones con los bancos
    	while(i.iterator().hasNext()){
    		id_banco = i.iterator().next(); 
    		Database_lib.getInstance().cerrar_sesion(id_banco);
    	}*/
    }

    /**
     * Método que cambia el estado del servidor
     * @throws IOException 
     */
    public void cambiar_estado(){
    	if(isOnline())
			cerrar_servidorBancos();
		else
			abrir_servidorBancos();
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
    public void sendToBanco(MensajeDatos message,InetAddress ip_cajero, int puerto_cajero){
    	ConexionConsorcio_Bancos c = new ConexionConsorcio_Bancos(TipoAccion.ENVIO,message,ip_cajero,puerto_cajero,consorcio,this,socketServidor);
    	c.start();
    }
    
}

