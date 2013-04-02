package practicaacs.banco.csconsorcio;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import practicaacs.fap.Mensaje;

public class ClienteServidorConsorcio extends Thread {

	int puerto;
	private BlockingQueue<Mensaje> mensajesAEnviar;
	private AnalizadorMensajes analizador;
	private DatagramSocket socketServidor;
	private InetAddress direccionIP;
	private String hostConsorcio;
	private int puertoConsorcio;
	
	public ClienteServidorConsorcio(int puerto, String hostconsorcio, int puertoconsorcio, AnalizadorMensajes analizador){
		this.puerto = puerto;
		this.analizador = analizador;
		this.hostConsorcio = hostconsorcio;
		this.puertoConsorcio = puertoconsorcio;
		this.mensajesAEnviar = new  LinkedBlockingQueue<Mensaje>();
		
		try {
			 socketServidor = new DatagramSocket(puerto);
		 }catch (IOException e) {
			 System.out.println("Error al crear el objeto socket servidor");
			 System.exit(-1);
		 }
		
		try {
			socketServidor.setSoTimeout(1000);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	
	public void enviarMensaje(Mensaje m) throws InterruptedException{
		this.mensajesAEnviar.put(m);
	}
	
	private void analizarMensaje(byte[] bs){
		analizador.analizarMensaje(bs);
	}
	
	
	@Override
	public void run() {
		Mensaje msgEnviar;
		byte [] recibirDatos = new byte[1024];
		
		while(true){
		
			DatagramPacket recibirPaquete = new DatagramPacket(recibirDatos, recibirDatos.length);
			
			try{
				socketServidor.receive(recibirPaquete);
				this.analizarMensaje(recibirPaquete.getData());
			}catch (SocketTimeoutException e){
			
			}catch (IOException e) {
				System.out.println("Error al recibir");
				System.exit ( 0 );
			}
			 
			while ((msgEnviar = mensajesAEnviar.poll()) != null){
				DatagramPacket enviarPaquete = new DatagramPacket(msgEnviar.getBytes(), msgEnviar.size(), this.direccionIP, this.puerto);
	
				try{
					socketServidor.send(enviarPaquete);
				}catch (IOException e) {
					System.out.println("Error al enviar");
					System.exit ( 0 );
				}
			}
		}
		
	}
	
}
