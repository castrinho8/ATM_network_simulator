package practicaacs.cajeros;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketTimeoutException;
import java.util.Calendar;

import practicaacs.cajeros.iu.ConsultaAbstracta;
import practicaacs.fap.Mensaje;
import practicaacs.fap.MensajeDatos;
import practicaacs.fap.MensajeNoValidoException;

public class ConexionCajero extends Thread{

	private Mensaje envio;
	private Cajero cajero;
	private ConsultaAbstracta interfaz;
	/**
	 * Constructor de la clase
	 * @param env EL envio a realizar.
	 * @param caj El cajero que envia el mensaje.
	 */
	public ConexionCajero(Mensaje env, Cajero caj,ConsultaAbstracta ventana) {
		this.envio = env;
		this.cajero = caj;
		this.interfaz = ventana;
	}
	
	@Override
	public void run() {
		MensajeDatos m = this.enviar_mensaje();
		this.interfaz.actualizarIU(m);
	}

	/**
     * Envia el mensaje y se queda esperando una respuesta.
     * @param envio El mensaje a enviar.
     * @param address La dirección a donde enviar el mensaje.
     * @param port El puerto a donde enviar el mensaje.
     * @return El mensaje de respuesta correspondiente.
     */
    private MensajeDatos enviar_mensaje(){
    	
    	Calendar time = Calendar.getInstance();
    	System.out.println("ENVIO: Cajero: " + this.cajero + " a las " + time.getTime() + " \n" + this.envio.toString());
    	//Crea el Datagrama a enviar
		DatagramPacket enviarPaquete = new DatagramPacket(this.envio.getBytes(),this.envio.size(),this.cajero.getConsorcio_address(),this.cajero.getConsorcio_port());
		
		try{
			//Enviamos el mensaje
			this.cajero.getSocketCajero().send(enviarPaquete);
		}catch (IOException e) {
			System.out.println("Error al enviar.");
			System.exit ( 0 );
		}
		
		byte [] recibirDatos = new byte[1024];
		//Crea el Datagrama en donde recibir los datos
		DatagramPacket inputPacket = new DatagramPacket(recibirDatos, recibirDatos.length);
		
		try{
			//Recibe datos
			System.out.println("RECIBIENDO DEL CONSORCIO");
			this.cajero.getSocketCajero().receive(inputPacket);
			System.out.println("RECIBIDO DEL CONSORCIO");

		}catch (SocketTimeoutException e){
			System.out.println("Timeout");
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
    	System.out.println("RECEPCION: Cajero: " + this.cajero.getId_cajero() +" a las " + time.getTime());
    	return (MensajeDatos) recepcion;
    }
}
