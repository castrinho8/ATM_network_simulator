package practicaACS.consorcio;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.logging.*;

import fap.EstadoSesion;
import fap.Mensaje;

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
    public Consorcio() throws IOException{
		super();
		//leer datos de ficheros
		this.address = InetAddress.getByName("127.0.0.1");
		int puerto_cajeros = 2002;
		int puerto_bancos = 2001;
		
		this.id_consorcio = next_id_consorcio++;
		this.cajeros_server = new ServidorConsorcio_Cajeros(this,puerto_cajeros);
		this.bancos_server = new ServidorConsorcio_Bancos(this,puerto_bancos);
		this.bancos_client = new ClienteConsorcio_Bancos();
	}


	// GETTERS SETTERS

	//-----END GETTERS SETTERS
	
    
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

    
    //imprimir lo que se envia y recibe
    //solicitar reanudacion
    
}



