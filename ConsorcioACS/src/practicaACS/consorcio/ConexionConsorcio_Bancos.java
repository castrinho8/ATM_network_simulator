package practicaACS.consorcio;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;

import fap.CodigosRespuesta;
import fap.EstadoSesion;
import fap.Mensaje;
import fap.RespSaldo;
import fap.TiposMovimiento;

public class ConexionConsorcio_Bancos extends Thread {


	static private int num_next_message = 0;
	private Socket socket;
	private Consorcio consorcio;
	
	private Hashtable ultimos_envios;
	private EstadoSesion estado_conexion_banco;

	public ConexionConsorcio_Bancos(Consorcio cons,Socket socket) {
		super();
		this.socket = socket;
		this.consorcio = cons;
		this.ultimos_envios = new Hashtable();
		this.estado_conexion_banco = EstadoSesion.ACTIVA;
	}

	/**
	 * Devuelve el estado de la conexion con el banco
	 */
	public boolean consultar_protocolo(){
		return estado_conexion_banco.equals(EstadoSesion.ACTIVA);
	}
	
	/**
	 * Detiene el trafico con el banco
	 */
	public void detener_trafico(){
    	this.estado_conexion_banco = EstadoSesion.TRAFICO_DETENIDO;
	}
	
	/**
	 * Cambia el ultimo envio del canal indicado por el pasado por parametro
	 */
	private void anhadir_ultimo_envio(int canal, Mensaje message){
		if(ultimos_envios.containsKey(canal))
			ultimos_envios.remove(canal);
		
		ultimos_envios.put(canal,message);
	}
	
	/**
	 * Devuelve el ultimo envio de un canal
	 */
	private Mensaje obtener_ultimo_envio(int canal){
		return (Mensaje) ultimos_envios.get(canal);
	}
	
	/**
	 * Devuelve el hashtable con todos los ultimos envios de un banco
	 */
	public Hashtable getUltimosEnvios(){
		return ultimos_envios;
	}
	
		
	public void run() {
		try {
	    	ServerSocket servidor = new ServerSocket();
	    	this.abrir_sesion();
	    	
	    	Calendar time = Calendar.getInstance();
	    	System.out.println("APERTURA: Sesion Servidor de Bancos comenzada a las " + time.getTime());

	    	while (this.consultar_protocolo()) {
	    	     Socket incoming = servidor.accept();

	    	     Thread t = new ConexionConsorcio_Bancos(this.consorcio,incoming);
	    	     t.start();
	    	}
	    	
	    	time = Calendar.getInstance();
	    	System.out.println("CIERRE: Sesion Servidor de Bancos termidada a las " + time.getTime());
	    	
	    	time = Calendar.getInstance();
			Mensaje respuesta = null;
			Mensaje recibido = null;
			
			//Creamos los buffers
			OutputStream os = this.socket.getOutputStream();  
			ObjectOutputStream o_buffer = new ObjectOutputStream(os);  
			InputStream is = this.socket.getInputStream();  
			ObjectInputStream i_buffer = new ObjectInputStream(is);  
			
			//Recibimos el mensaje
			recibido = (Mensaje) i_buffer.readObject();

			System.out.printf(recibido.toString());
			
			//Creamos la respuesta
			respuesta = generar_respuesta(recibido);
			
			System.out.printf(respuesta.toString());
			this.consorcio.anhadir_ultimo_envio(canal, respuesta);
			
			//Enviamos el mensaje
			o_buffer.writeObject(respuesta);
			
			os.close();
			o_buffer.close();
			this.socket.close();
		}
		catch (Exception e) {
	    // manipular las excepciones
		}
	}
		
	public Mensaje generar_respuesta(Mensaje recibido){
		switch(recibido.getTipoMensaje()){
			case :
				break;
			case :
				break;
			case :
				break;
			case :
				break;
			case :
				break;
			case :
				break;
			default:
				break;
		}
		return null;
	}
	
	public void abrir_sesion(){
		
	}
	
	public void cerrar_sesion(){
		
	}
	
		
}
