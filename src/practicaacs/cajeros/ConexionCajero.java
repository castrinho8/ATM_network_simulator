package practicaacs.cajeros;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Calendar;

import practicaacs.cajeros.iu.ConsultaAbstracta;
import practicaacs.fap.CodigoNoValidoException;
import practicaacs.fap.CodigosMensajes;
import practicaacs.fap.Mensaje;
import practicaacs.fap.MensajeDatos;
import practicaacs.fap.MensajeNoValidoException;
import practicaacs.fap.RespMovimientos;
import practicaacs.fap.SolMovimientos;

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
		
		MensajeDatos m = null;
		RespMovimientos resp = null;
		ArrayList<RespMovimientos> lista = new ArrayList<RespMovimientos>();
		this.enviar_mensaje();

		//Si es MOVIMIENTOS hay que recibir varios mensajes
		if(this.envio.getTipoMensaje().equals(CodigosMensajes.SOLMOVIMIENTOS)){
			do{
				resp = (RespMovimientos) this.recibir_mensaje();
				lista.add(resp);
				System.out.println(resp.getNmovimientos());
			}while(resp.getNmovimientos()>0);
			
			try {
				this.interfaz.actualizarIUmovimientos(lista);
			} catch (CodigoNoValidoException e) {
				e.printStackTrace();
			}
		//Si no es MOVIMIENTOS solo se recibe uno
		}else{
			m = this.recibir_mensaje();
			
			try {
				this.interfaz.actualizarIU(m);
			} catch (CodigoNoValidoException e) {
				e.printStackTrace();
			}
		}
	}

	/**
     * Envia el mensaje y se queda esperando una respuesta.
     * @param envio El mensaje a enviar.
     * @param address La dirección a donde enviar el mensaje.
     * @param port El puerto a donde enviar el mensaje.
     * @return El mensaje de respuesta correspondiente.
     */
    private void enviar_mensaje(){
    	
    	Calendar time = Calendar.getInstance();
    	System.out.println("ENVIO: Cajero: " + this.cajero + " a las " + time.getTime() + " \n" + this.envio.toString());
    	//Crea el Datagrama a enviar
		DatagramPacket enviarPaquete = new DatagramPacket(this.envio.getBytes(),this.envio.size(),this.cajero.getConsorcio_address(),this.cajero.getConsorcio_port());
		
		System.out.println("ENVIA: " + this.envio.getTipoMensaje().toString());

		try{
			//Enviamos el mensaje
			this.cajero.getSocketCajero().send(enviarPaquete);
		}catch (IOException e) {
			System.out.println("Error al enviar.");
			System.exit ( 0 );
		}
    }
    
    
    public MensajeDatos recibir_mensaje(){
    
    	Calendar time = Calendar.getInstance();
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
			String msg = new String(inputPacket.getData(),inputPacket.getOffset(),inputPacket.getLength());
			recepcion = Mensaje.parse(msg);
		} catch (MensajeNoValidoException e) {
			System.out.println("Error al parsear el mensaje recibido.");
			e.printStackTrace();
		}
		
		time = Calendar.getInstance();
    	System.out.println("RECEPCION: Cajero: " + this.cajero.getId_cajero() +" a las " + time.getTime());
    	return (MensajeDatos) recepcion;
    }
    
    
}
