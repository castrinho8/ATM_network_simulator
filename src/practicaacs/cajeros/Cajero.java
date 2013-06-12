package practicaacs.cajeros;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.Properties;

import javax.swing.JFrame;

import practicaacs.cajeros.iu.ConsultaAbstracta;
import practicaacs.cajeros.iu.PantallaInicialCajero_IU;
import practicaacs.fap.*;

/**
 * Clase que representa a un cajero.
 */
public class Cajero{

	static private int next_id_cajero = 1;
	static private int next_number_message = 0;
	static private PantallaInicialCajero_IU iu;
	
	private String id_cajero;
	private InetAddress cajero_address;
	private int cajero_port;
	
	private String id_consorcio;
	private InetAddress consorcio_address;
	private int consorcio_port;
	
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
		
		//Direccion del consorcio
		this.id_consorcio = prop.getProperty("consorcio.id");
		this.consorcio_address = InetAddress.getByName(prop.getProperty("consorcio.address"));
		this.consorcio_port = new Integer(prop.getProperty("consorcio.cash_server.port"));
		this.cajero_address = InetAddress.getByName(prop.getProperty("cajero.address"));
		this.cajero_port = new Integer(prop.getProperty("cajero.port"));
		this.id_cajero = Integer.toString(next_id_cajero++);
		this.iu = new PantallaInicialCajero_IU(this);
		this.iu.setVisible(true);
		
    	System.out.println("IP: " + this.consorcio_address + "-" + this.consorcio_port);

		try {
			socketCajero = new DatagramSocket(this.cajero_port);
		 }catch (IOException e) {
			 System.out.println("Error al crear el objeto socketCajero");
			 System.exit(-1);
		 }
	}

    public static int getNext_id_cajero() {
		return next_id_cajero;
	}

	public static int getNext_number_message() {
		return next_number_message;
	}

	public static PantallaInicialCajero_IU getIu() {
		return iu;
	}

	public String getId_cajero() {
		return id_cajero;
	}

	public InetAddress getCajero_address() {
		return cajero_address;
	}

	public int getCajero_port() {
		return cajero_port;
	}

	public String getId_consorcio() {
		return id_consorcio;
	}

	public InetAddress getConsorcio_address() {
		return consorcio_address;
	}

	public int getConsorcio_port() {
		return consorcio_port;
	}

	public DatagramSocket getSocketCajero() {
		return socketCajero;
	}

	/**
     * Método recibe el Envio creado por la IU y genera el mensaje correspondiente a 
     * los datos obtenidos de la IU.
     * @return El mensaje a enviar.
     */
    public Mensaje crear_mensaje(Envio env){
    	MensajeDatos envio = null;
    	System.out.println("ENTRAA");
    	switch(env.getTipoMensaje()){
	    	case SOLSALDO:{
	        	envio = new SolSaldo(this.id_cajero,this.id_consorcio,
	        			0,next_number_message++,
	        			false,env.getNum_tarjeta(),env.getNum_cuenta_origen());
	    		break;
	    	}
	    	case SOLMOVIMIENTOS:{
	    		envio = new SolMovimientos(this.id_cajero,this.id_consorcio,
	    				env.getTipoMensaje(),0,next_number_message++,
	    				false,env.getNum_tarjeta(),env.getNum_cuenta_origen());
	        	System.out.println("Sale");
	    		break;
	    	}
	    	case SOLREINTEGRO:{
	    		envio = new SolReintegro(this.id_cajero,this.id_consorcio,
	    				0,next_number_message++,
	    				false,env.getNum_tarjeta(),env.getNum_cuenta_origen(),env.getImporte());
	    		break;
	    	}
	    	case SOLABONO:{
	    		envio = new SolAbono(this.id_cajero,this.id_consorcio,
	    				0,next_number_message++,
	    				false,env.getNum_tarjeta(),env.getNum_cuenta_origen(),env.getImporte());
	    		break;
	    	}
	    	case SOLTRASPASO:{
	    		envio = new SolTraspaso(this.id_cajero,this.id_consorcio,
	    				0,next_number_message++,
	    				false,env.getNum_tarjeta(),env.getNum_cuenta_origen(),
	    				env.getNum_cuenta_destino(),env.getImporte());
	    		break;
	    	}
	    	default:{
	    		break;
	    	}
    	}
    	return envio;
    }

    public void enviar_mensaje(Mensaje envio,ConsultaAbstracta ventana){
    	ConexionCajero c = new ConexionCajero(envio,this,ventana);
    	c.start();
    }

}


