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

import practicaacs.cajeros.iu.PantallaInicialCajero_IU;
import practicaacs.fap.*;

/**
 * Clase que representa a un cajero.
 * Es un Singleton
 *
 */
public class Cajero{

	static private int next_id_cajero = 0;
	static private int next_number_message = 0;
	static private PantallaInicialCajero_IU iu;
	
	private String id_cajero;
	private InetAddress cajero_address;
	private int cajero_port;
	
	private String id_consorcio;
	private int consorcio_port;
	private InetAddress consorcio_address;
	
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
				
		try {
			socketCajero = new DatagramSocket(this.cajero_port);
		 }catch (IOException e) {
			 System.out.println("Error al crear el objeto socket cliente");
			 System.exit(-1);
		 }
	}

    /**
     * Método recibe el Envio creado por la IU y genera el mensaje correspondiente a 
     * los datos obtenidos de la IU.
     * @return El mensaje a enviar.
     */
    public Mensaje crear_mensaje(Envio env){
    	MensajeDatos envio = null;
    	
    	//Llama a un metodo de la iu que o tiene los datos y generamos el mensaje
    	switch(env.getTipoMensaje()){	
	    	case SOLSALDO:{
	        	envio = new SolSaldo(this.id_cajero,this.id_consorcio,
	        			0,next_number_message++,
	        			true,env.getNum_tarjeta(),env.getNum_cuenta_origen());
	    		break;
	    	}
	    	case SOLMOVIMIENTOS:{
	    		envio = new SolMovimientos(this.id_cajero,this.id_consorcio,
	    				env.getTipoMensaje(),0,next_number_message++,
	    				true,env.getNum_tarjeta(),env.getNum_cuenta_origen());
	    		break;
	    	}
	    	case SOLREINTEGRO:{
	    		envio = new SolReintegro(this.id_cajero,this.id_consorcio,
	    				0,next_number_message++,
	    				true,env.getNum_tarjeta(),env.getNum_cuenta_origen(),env.getImporte());
	    		break;
	    	}
	    	case SOLABONO:{
	    		envio = new SolAbono(this.id_cajero,this.id_consorcio,
	    				0,next_number_message++,
	    				true,env.getNum_tarjeta(),env.getNum_cuenta_origen(),env.getImporte());
	    		break;
	    	}
	    	case SOLTRASPASO:{
	    		envio = new SolTraspaso(this.id_cajero,this.id_consorcio,
	    				0,next_number_message++,
	    				true,env.getNum_tarjeta(),env.getNum_cuenta_origen(),
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
     * Envia el mensaje y se queda esperando una respuesta.
     * @param envio El mensaje a enviar.
     * @param address La dirección a donde enviar el mensaje.
     * @param port El puerto a donde enviar el mensaje.
     * @return El mensaje de respuesta correspondiente.
     */
    public Mensaje enviar_mensaje(Mensaje envio) throws IOException, ClassNotFoundException{
    	
    	Calendar time = Calendar.getInstance();
    	System.out.println("ENVIO: Cajero: " + this.id_cajero + " a las " + time.getTime());

    	//Crea el Datagrama a enviar
		DatagramPacket enviarPaquete = new DatagramPacket(envio.getBytes(),envio.size(),this.consorcio_address,this.consorcio_port);
		
		try{
			//Enviamos el mensaje
			this.socketCajero.send(enviarPaquete);
		}catch (IOException e) {
			System.out.println("Error al enviar.");
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
			System.out.println("Error al recibir.");
			System.exit ( 0 );
		}

		Mensaje recepcion = null;
		try {
			recepcion = Mensaje.parse(new String(inputPacket.getData(),inputPacket.getOffset(),inputPacket.getLength()-1));
		} catch (MensajeNoValidoException e) {
			System.out.println("Error al parsear el mensaje recibido.");
			e.printStackTrace();
		}
		
		time = Calendar.getInstance();
    	System.out.println("RECEPCION: Cajero: " + this.id_cajero +" a las " + time.getTime());
    	return recepcion;
    }
}


