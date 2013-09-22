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
	static private int next_number_message = 1;
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
		this.id_cajero = new String(prop.getProperty("cajero.id"));
		this.cajero_address = InetAddress.getByName(prop.getProperty("cajero.address"));
		this.cajero_port = new Integer(prop.getProperty("cajero.port"));
		
		this.id_consorcio = new String(prop.getProperty("cajero.consorcio.id"));
		this.consorcio_address = InetAddress.getByName(prop.getProperty("cajero.consorcio.address"));
		this.consorcio_port = new Integer(prop.getProperty("cajero.consorcio.port"));


		this.iu = new PantallaInicialCajero_IU(this);
		this.iu.setVisible(true);
		
    	System.out.println("Establecido Cajero en IP: " + this.consorcio_address + "- Puerto:" + this.consorcio_port);

		try {
			socketCajero = new DatagramSocket(this.cajero_port);
		 }catch (IOException e) {
			 System.out.println("Error al crear el objeto socketCajero");
			 System.exit(-1);
		 }
	}

    /**
     * Getter de la IU del Cajero.
     * @return La pantalla inicial de la IU del cajero.
     */
	public static PantallaInicialCajero_IU getIu() {
		return iu;
	}

	/**
	 * Getter del id del cajero.
	 * @return Un String con la id del cajero.
	 */
	public String getId_cajero() {
		return id_cajero;
	}

	/**
	 * Getter de la IP en la que está el cajero.
	 * @return La InetAddress correspondiente a la IP.
	 */
	public InetAddress getCajero_address() {
		return cajero_address;
	}

	/**
	 * Getter del puerto del cajero.
	 * @return Un entero que indica el puerto en el que está el cajero.
	 */
	public int getCajero_port() {
		return cajero_port;
	}

	/**
	 * Getter de la id del consorcio con el que trabaja el cajero.
	 * @return Un string con la id del consorcio.
	 */
	public String getId_consorcio() {
		return id_consorcio;
	}

	/**
	 * Getter de la IP del consorcio.
	 * @return La InetAddress correspondiente.
	 */
	public InetAddress getConsorcio_address() {
		return consorcio_address;
	}

	/**
	 * Getter del puerto del consorcio con el que el cajero se comunica.
	 * @return El puerto correspondiente.
	 */
	public int getConsorcio_port() {
		return consorcio_port;
	}

	/**
	 * Getter del socket de conexión con el consorcio.
	 * @return El DatagramSocket correspondiente.
	 */
	public DatagramSocket getSocketCajero() {
		return socketCajero;
	}

	/**
     * Método recibe el Envio creado por la IU y genera el mensaje correspondiente a 
     * los datos obtenidos de la IU.
     * @param env El Envio con los datos obtenidos para generar el mensaje.
     * @return El mensaje a enviar.
     */
    public Mensaje crear_mensaje(Envio env){
    	MensajeDatos envio = null;
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

    /**
     * Método que crea un thread de conexión con el consorcio y envia el mensaje.
     * @param envio El mensaje a enviar.
     * @param ventana La ventana de la IU desde la que se envia una operación.
     */
    public void enviar_mensaje(Mensaje envio,ConsultaAbstracta ventana){
    	ConexionCajero c = new ConexionCajero(envio,this,ventana);
    	c.start();
    }

}


