package practicaacs.cajeros;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import practicaacs.fap.*;

public class Cajero{

	static private int next_id_cajero = 0;
	static private int next_number_message = 0;
	
	private int id_cajero;
	private int puerto;
	private InetAddress address;
		
	/**
	 * Constructor de la clase Cajero
	 */
    public Cajero() throws UnknownHostException {
    	
    	//Cojer los datos de un fichero
    	int port = 2002;
		InetAddress direccion = InetAddress.getByName("127.0.0.1");
        
		this.id_cajero = next_id_cajero++;
        this.puerto = port;
        this.address = direccion;
    }

    /**
     * Realiza una conexion
     * @return Devuelve el socket si se produce la conexion o null en caso contrario.
     */
    public Socket conectar_consorcio(){
    	Socket socket = null;
    	try {
			socket = new Socket(this.address , this.puerto);
	    } catch (IOException e) {
			System.out.println("Error: no se ha podido establecer la conexion.");
			e.printStackTrace();
			System.exit(-1);
	    }
		return socket;
    }
    
    /**
     * Conecta con el consorcio, envia el mensaje y devuelve la respuesta
     * @return La respuesta del consorcio.
     */
    public Mensaje send_message(Mensaje message) throws IOException, ClassNotFoundException{
    	
    	Mensaje respuesta = null;
    	
    	Socket conexion = this.conectar_consorcio();
    	
    	//Creamos los buffers
    	OutputStream os = conexion.getOutputStream();  
    	ObjectOutputStream o_buffer = new ObjectOutputStream(os);  
    	InputStream is = conexion.getInputStream();  
    	ObjectInputStream i_buffer = new ObjectInputStream(is);  
    	
    	//Enviamos el mensaje solicitando el login
    	o_buffer.writeObject(message);
    	o_buffer.flush();
    	
    	System.out.printf(message.toString());
    	
    	//Recibimos la respuesta
    	respuesta = (Mensaje) i_buffer.readObject();

    	System.out.printf(respuesta.toString());

    	os.close();
    	o_buffer.close();
    	conexion.close();
    	
    	return respuesta;
    }
    


    
    
    //Funciones de datos
    public String consultar_saldo(int tarjeta) throws IOException, ClassNotFoundException{
    	
    	int num_cuenta = 20;
    	
    	Mensaje envio = new ConsultaSaldo("cajero","consorcio",01,next_number_message++,true,"111",num_cuenta);
		
    	Mensaje respuesta = send_message(envio);
    	
    	return respuesta.toString();
    }
    
    public String consultar_movimientos(int tarjeta) throws IOException, ClassNotFoundException{
    	Mensaje envio = null;
    	Mensaje respuesta = send_message(envio);
    	
    	return respuesta.toString();
    }
    
    public String realizar_reintegro(int tarjeta) throws IOException, ClassNotFoundException{
    	Mensaje envio = null;
    	Mensaje respuesta = send_message(envio);
    	
    	return respuesta.toString();
    }
    
    public String realizar_abono(int tarjeta) throws IOException, ClassNotFoundException{
    	Mensaje envio = null;
    	Mensaje respuesta = send_message(envio);
    	
    	return respuesta.toString();
    }
    
    public String realizar_traspaso(int tarjeta) throws IOException, ClassNotFoundException{
    	Mensaje envio = null;
    	Mensaje respuesta = send_message(envio);
    	
    	return respuesta.toString();
    } 
    
    @Override
    public String toString(){
    	return "";
    }
}


