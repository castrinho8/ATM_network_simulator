package practicaacs.consorcio;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Properties;
import java.util.logging.*;

import practicaacs.fap.*;

public class Consorcio {

	static private int next_id_consorcio = 0;
	
	private int id_consorcio;
	private InetAddress address;

	private ServidorConsorcio_Cajeros cajeros_server;

	private ServidorConsorcio_Bancos bancos_server;
	private ClienteConsorcio_Bancos bancos_client;
	
	/**
	 * Constructor de la clase Consorcio
	 * @throws IOException 
	 */
    public Consorcio(String file) throws IOException{
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
		this.address = InetAddress.getByName(prop.getProperty("addres"));
		int puerto_cajeros = new Integer(prop.getProperty("cash_port"));
		int puerto_bancos = new Integer(prop.getProperty("bank_port"));
		
		this.id_consorcio = next_id_consorcio++;
		this.cajeros_server = new ServidorConsorcio_Cajeros(this,puerto_cajeros);
		this.bancos_server = new ServidorConsorcio_Bancos(this,puerto_bancos);
		this.bancos_client = new ClienteConsorcio_Bancos();
	}

    //CONSORCIO - CAJEROS
    
	public ServidorConsorcio_Cajeros getCajeros_server() {
		return cajeros_server;
	}
    
    public ServidorConsorcio_Bancos getBancos_server() {
		return bancos_server;
	}

	public int getId_consorcio() {
		return id_consorcio;
	}

	public InetAddress getAddress() {
		return address;
	}
    
    //CONSORCIO - BANCO
    
	public ClienteConsorcio_Bancos getBancos_client() {
		return bancos_client;
	}

    //solicitar reanudacion
    
}



