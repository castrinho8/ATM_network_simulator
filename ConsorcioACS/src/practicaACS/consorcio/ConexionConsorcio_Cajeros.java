package practicaACS.consorcio;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import practicaACS.mensajes.Mensaje;

public class ConexionConsorcio_Cajeros extends Thread{
	
	private Socket socket;
	private Consorcio consorcio;

	public ConexionConsorcio_Cajeros(Consorcio cons,Socket socket) {
		super();
		this.socket = socket;
		this.consorcio = cons;
	}
	
	public void run() {
		try {
			Mensaje respuesta = null;
			Mensaje recibido = null;
			
			//Creamos los buffers
			OutputStream os = this.socket.getOutputStream();  
			ObjectOutputStream o_buffer = new ObjectOutputStream(os);  
			InputStream is = this.socket.getInputStream();  
			ObjectInputStream i_buffer = new ObjectInputStream(is);  
			
			//Recibimos el mensaje
			recibido = (Mensaje) i_buffer.readObject();

			//Creamos la respuesta
			respuesta = generar_respuesta(recibido);
			
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
	
	   
    public void rechazar_consulta_cajero(){
    	
    		
    }
    
    public void almacenar_consulta_cajero(){
    
    	//almacenar y reenviar offline al banco
    }
    
	
	Mensaje generar_respuesta(Mensaje recibido){
		Mensaje respuesta = null;
    	if(this.consorcio.consultar_protocolo()){
    		
    	}else{
    		
    	}
		return respuesta;
	}
	

	
}
