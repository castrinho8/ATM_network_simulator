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
import practicaacs.consorcio.iu.MostrarBD_IU;
import practicaacs.consorcio.iu.PantallaInicialConsorcio_IU;

public class Consorcio {

	static private int next_id_consorcio = 1;

	private PantallaInicialConsorcio_IU iu;
	private String id_consorcio;
	private InetAddress address;
	private int puerto_cajeros;
	private int puerto_bancos;
	
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
		int p_cajeros = new Integer(prop.getProperty("consorcio.cash_server.port"));
		int p_bancos = new Integer(prop.getProperty("consorcio.bank_server.port"));
		this.id_consorcio = new String(prop.getProperty("consorcio.id"));

		this.puerto_cajeros = p_cajeros;
		this.puerto_bancos = p_bancos;
		iniciarCajerosServer();
		iniciarBancosServer();
		
		this.iu = new PantallaInicialConsorcio_IU(this);
		this.iu.setVisible(true);
		
	}

    private void iniciarCajerosServer(){
		try {
			this.cajeros_server = new ServidorConsorcio_Cajeros(this,puerto_cajeros);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.cajeros_server.start();
    }
    
    private void iniciarBancosServer(){
		try {
			this.bancos_server = new ServidorConsorcio_Bancos(this,puerto_bancos);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.bancos_server.start();
    }
    
    private void cerrarBancosServer(){
    	this.bancos_server.cerrar_servidorBancos();
    	this.bancos_server = null;
    }
    
    
    public String cambiarEstadoBancosServer(){
    	if(this.bancos_server==null){
    		this.iniciarBancosServer();
    		return "Cerrar Servidor";
    	}else{
    		this.cerrarBancosServer();
    		return "Levantar Servidor";
    	}
    }
    
	public ServidorConsorcio_Cajeros getCajeros_server() {
		return cajeros_server;
	}
    
    public ServidorConsorcio_Bancos getBancos_server() {
		return bancos_server;
	}

    public boolean isDownCajerosServer(){
    	return this.cajeros_server.equals(null);
    }

    public boolean isDownBancosServer(){
    	return this.bancos_server==null;
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
	
	public void actualizarIU(){
		this.iu.actualizarListaBancos();
		this.iu.actualizarListaCajeros();
	}
    
}



