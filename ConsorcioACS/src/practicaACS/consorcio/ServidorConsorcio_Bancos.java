package practicaACS.consorcio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Calendar;
import java.util.Hashtable;

import fap.*;


public class ServidorConsorcio_Bancos {

	private int port;
	private Consorcio consorcio;
	
	private Hashtable<String,ConexionConsorcio_Bancos> bancos;
	private EstadoSesion estado_serv_bancos;
	
	/**
	 * Constructor de la clase ServidorConsorcio_Cajeros
	 */
	public ServidorConsorcio_Bancos(Consorcio cons,int puerto) throws IOException{
		super();
		this.port = puerto;
		this.consorcio = cons;
		this.bancos = new Hashtable<String,ConexionConsorcio_Bancos>();
		this.estado_serv_bancos = EstadoSesion.ACTIVA;
	}
	
	//-------GETTERS & SETTERS-------
	public int getPuerto() {
		return port;
	}
	
	//-------END GETTERS & SETTERS-------
	
	/**
	 * Consulta si el banco para la tarjeta indicada se encuentra activo.
	 */
	public boolean consultar_protocolo(String tarjeta){
		String banco = tarjeta.substring(0,8);
		return bancos.get(banco).consultar_protocolo();
	}
	
	/**
	 * Detiene el trafico del banco indicado
	 */
	public void detener_trafico(String banco){
    	this.bancos.get(banco).detener_trafico();
	}
	
	/**
     * Levanta el servidorBancos
     */
    public void levantar_servidorBancos() throws IOException{
    	ServerSocket servidor = new ServerSocket(this.port);

    	Calendar time = Calendar.getInstance();
    	System.out.println("APERTURA: Sesion Servidor de Bancos comenzada a las " + time.getTime());

    	while (this.estado_serv_bancos == EstadoSesion.ACTIVA) {
    	     Socket incoming = servidor.accept();

    	     Thread t = new ConexionConsorcio_Bancos(this.consorcio,incoming);
    	     t.start();
    	}
    	
    	time = Calendar.getInstance();
    	System.out.println("CIERRE: Sesion Servidor de Bancos termidada a las " + time.getTime());
    }

    /**
     * Cierra el servidorBancos
     */
    public void cierra_servidorBancos(){
    	this.estado_serv_bancos = EstadoSesion.CERRADA;
    	//cerrar todas las conexiones con los bancos
    	for(Object banco : this.bancos.values()){
    		banco.
    	}
    }

    /**
     * Recupera los mensajes del banco
     */
    public void realiza_recuperacion(String banco){
    	for(Object message : this.bancos.get(banco).getUltimosEnvios().values()){
    		this.consorcio.getBancos_client().send_message((Mensaje) message);
    	}
    }
    
}





