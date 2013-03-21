package practicaACS.consorcio;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.logging.*;

import practicaACS.bancos.Banco;
import practicaACS.bancos.EstadoSesion;
import practicaACS.cajeros.Cajero;
import practicaACS.threads.ThreadServerHandler;
	
public class Consorcio {

	static private int next_id_consorcio = 0;
	
	private int id_consorcio;
	private ServidorConsorcio_Cajeros cajeros_server;
	private ArrayList<Cajero> cajeros_client;

	private ArrayList<Banco> bancos;
	private ServidorConsorcio_Bancos bancos_server;
	private ClienteConsorcio_Bancos bancos_client;
	private EstadoSesion estado_sesion;
	
	/**
	 * Constructor de la clase Consorcio
	 */
    public Consorcio(ArrayList<Banco> bancos, ArrayList<Cajero> cajeros_clientes){
		super();
		this.id_consorcio = next_id_consorcio++;
		this.cajeros_server = new ServidorConsorcio_Cajeros(this);
		this.cajeros_client = cajeros_clientes;
		this.bancos = bancos;
		this.bancos_server = new ServidorConsorcio_Bancos();
		this.bancos_client = new ClienteConsorcio_Bancos();
	}

    //CONSORCIO - CAJEROS
    
    public ServidorConsorcio_Cajeros getCajeros_server() {
		return cajeros_server;
	}
    
    
    
    

    
    //CONSORCIO - BANCO
	/**
     * Devuelve true si el estado de la conexion es ACTIVA y false en caso contrario 
     */
    public boolean consultar_protocolo(){
    	return this.estado_sesion.equals(EstadoSesion.ACTIVA);
    }
    
    
     /** Inicia una nueva sesion con el banco.
     **/
    public void abrir_sesion(Banco bank){
		bank.setEstadoSesion(EstadoSesion.ACTIVA);
		//COUSAS
	}
	
    /**
     * Cierra una sesion con el banco
     */
    public void cerrar_sesion(Banco bank){
		//Acabar las transacciones y cerrar
		bank.setEstadoSesion(EstadoSesion.CERRADA);
	}
    
    //imprimir lo que se envia y recibe
    //solicitar reanudacion
    
}