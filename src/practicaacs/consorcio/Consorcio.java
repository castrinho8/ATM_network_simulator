package practicaacs.consorcio;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Properties;
import java.util.logging.*;

import practicaacs.banco.bd.ClienteBDBanco;
import practicaacs.fap.*;
import practicaacs.consorcio.bd.*;
import practicaacs.consorcio.iu.PantallaInicialConsorcio_IU;

public class Consorcio {

	static private int next_id_consorcio = 0;

	private PantallaInicialConsorcio_IU iu;
	private String id_consorcio;
	private InetAddress address;
	
	private ServidorConsorcio_Cajeros cajeros_server;
	private ServidorConsorcio_Bancos bancos_server;
	
	/**
	 * Constructor de la clase Consorcio
	 * @param file El path al archivo de propiedades.
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
		}
		
    	//Lee del fichero de propiedades
		this.address = InetAddress.getByName(prop.getProperty("consorcio.address"));
		int puerto_cajeros = new Integer(prop.getProperty("consorcio.cash_server.port"));
		int puerto_bancos = new Integer(prop.getProperty("consorcio.bank_server.port"));
		
		this.id_consorcio = Integer.toString(next_id_consorcio++);
		this.cajeros_server = new ServidorConsorcio_Cajeros(this,puerto_cajeros);
		this.cajeros_server.start();
		this.bancos_server = new ServidorConsorcio_Bancos(this,puerto_bancos);
		this.bancos_server.start();
		
		this.iu = new PantallaInicialConsorcio_IU(this);
		this.iu.setVisible(true);
	}

	public ServidorConsorcio_Cajeros getCajeros_server() {
		return cajeros_server;
	}
    
    public ServidorConsorcio_Bancos getBancos_server() {
		return bancos_server;
	}

	public String getId_consorcio() {
		return id_consorcio;
	}

	public InetAddress getAddress() {
		return address;
	}
	
	public void realizar_recuperacion(String id_banco){
		this.bancos_server.solicitar_recuperacion(id_banco);
	}
	
	public void realizar_finRecuperacion(String id_banco){
		this.bancos_server.solicitar_fin_recuperacion(id_banco);
	}

	public PantallaInicialConsorcio_IU getIu() {
		return iu;
	}
    
}



