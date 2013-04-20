package practicaacs.cajeros;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Properties;

import practicaacs.consorcio.ConexionConsorcio_Cajeros;
import practicaacs.fap.*;

public class Cajero{

	static private int next_id_cajero = 0;
	static private int next_number_message = 0;
	
	private int id_cajero;
	private boolean ocupado;
	
	private int port;
	private InetAddress address;
	private DatagramSocket socketCajero;
		
	/**
	 * Constructor de la clase Cajero
	 * @param file Un string con el path del archivo de propiedades.
	 */
    public Cajero(String file) throws UnknownHostException {
    	
    	//Obtenemos los datos del fichero properties
		Properties prop = new Properties();
		InputStream is;
		try {
			is = new FileInputStream(file);
		    prop.load(is);
		} catch (FileNotFoundException e) {
			System.err.println("Non se encontrou arquivo de configuracion " + file + ".");
			System.exit(-1);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
    	//leer datos de ficheros
    	//int port = 2002;
		//InetAddress direccion = InetAddress.getByName("127.0.0.1");
		
		//Direccion del consorcio
		this.address = InetAddress.getByName(prop.getProperty("consorcio.address"));
		this.port = new Integer(prop.getProperty("consorcio.port"));
		this.id_cajero = next_id_cajero++;
		this.ocupado = false;

		try {
			socketCajero = new DatagramSocket(this.port);
		 }catch (IOException e) {
			 System.out.println("Error al crear el objeto socket cliente");
			 System.exit(-1);
		 }
	}

    
    /**
     * Método que llama a la IU y obtiene el mensaje a enviar.
     * @return El mensaje a enviar.
     */
    private Mensaje crear_mensaje(Envio env){
    	//Llama a un metodo de la iu que o tiene los datos y generamos el mensaje
    	String tarjeta = "0123456789";
    	int num_cuenta = 1111;
    	String banco = tarjeta.substring(0,8);
    	SolSaldo envio = new SolSaldo(Integer.toString(this.id_cajero),banco,01,next_number_message++,true,tarjeta,num_cuenta);
		
    	return envio;
    }
    
    /**
     * Envia el mensaje y se queda esperando una respuesta
     * @param envio El mensaje a enviar.
     * @param address La dirección a donde enviar el mensaje.
     * @param port El puerto a donde enviar el mensaje.
     * @return La respuesta del consorcio en DatagramPacket
     */
    private DatagramPacket send_message(Mensaje envio,InetAddress address,int port) throws IOException, ClassNotFoundException{
    	
    	Calendar time = Calendar.getInstance();
    	System.out.println("APERTURA: Sesion cajero: " + this.id_cajero + "comenzada a las " + time.getTime());

    	//Crea el Datagrama a enviar
		DatagramPacket enviarPaquete = new DatagramPacket(envio.getBytes(),envio.size(),address,port);
		
		try{
			//Enviamos el mensaje
			this.socketCajero.send(enviarPaquete);
		}catch (IOException e) {
			System.out.println("Error al enviar");
			System.exit ( 0 );
		}
		
		byte [] recibirDatos = new byte[1024];

		//Crea el Datagrama en donde recibir los datos
		DatagramPacket inputPacket = new DatagramPacket(recibirDatos, recibirDatos.length);
		
		try{
			//Recibe datos
			socketCajero.receive(inputPacket);
		}catch (SocketTimeoutException e){
		}catch (IOException e) {
			System.out.println("Error al recibir");
			System.exit ( 0 );
		}

		time = Calendar.getInstance();
    	System.out.println("CIERRE: Sesion cajero: " + this.id_cajero +" termidada a las " + time.getTime());
    	return inputPacket;
    }
    
    /**
     * Método principal del cajero que realiza un envio y devuelve el resultado.
     */
    public Mensaje realizar_consulta(Envio env) throws ClassNotFoundException, IOException{
    	   
    	if(this.ocupado)
    		return null;

    	this.ocupado = true;
    	
    	//Crea el mensaje
    	Mensaje m = crear_mensaje(env);
    	
    	//Envia el mensaje y recibe la respuesta
    	DatagramPacket recibirDatos = send_message(m,this.address,this.port);
    	
    	//Traduce el byte[] a mensaje
    	Mensaje respuesta = null;
		try {
			respuesta = Mensaje.parse(recibirDatos.getData());
		} catch (MensajeNoValidoException e) {
			e.printStackTrace();
		}

    	this.ocupado = false;
    	return respuesta;
    }
    
}


