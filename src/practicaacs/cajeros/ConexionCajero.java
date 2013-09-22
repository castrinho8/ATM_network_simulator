/***************************************************************************
 *	ATM Network Simulator ACS. FIC. UDC. 2012/2013									*
 *	Copyright (C) 2013 by Pablo Castro and Marcos Chavarria 						*
 * <pablo.castro1@udc.es>, <marcos.chavarria@udc.es> 								*
 * 																								*
 * This program is free software; you can redistribute it and/or modify 	*
 * it under the terms of the GNU General Public License as published by 	*
 * the Free Software Foundation; either version 2 of the License, or 		*
 * (at your option) any later version. 												*
 ***************************************************************************/
package practicaacs.cajeros;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Calendar;

import practicaacs.cajeros.iu.ConsultaAbstracta;
import practicaacs.fap.CodigoNoValidoException;
import practicaacs.fap.CodigosMensajes;
import practicaacs.fap.Mensaje;
import practicaacs.fap.MensajeDatos;
import practicaacs.fap.MensajeNoValidoException;
import practicaacs.fap.MensajeRespDatos;
import practicaacs.fap.RespMovimientos;
import practicaacs.fap.RespMovimientosError;
import practicaacs.fap.SolMovimientos;

/**
 * Clase que implementa la conexión con el consorcio.
 * Extiende la clase Thread para que se puedan realizar varias
 * conexiones simultáneas.
 */
public class ConexionCajero extends Thread{

	private Mensaje envio;
	private Cajero cajero;
	private ConsultaAbstracta interfaz;
	
	/**
	 * Constructor de la clase.
	 * @param env El envio a realizar.
	 * @param caj El cajero que envia el mensaje.
	 * @param ventana La ventana de la IU que realiza la conexión.
	 */
	public ConexionCajero(Mensaje env, Cajero caj,ConsultaAbstracta ventana) {
		this.envio = env;
		this.cajero = caj;
		this.interfaz = ventana;
	}
	

	/**
	 * Método run del thread
	 */
	@Override
	public void run() {
		
		MensajeRespDatos m = null;
		RespMovimientos resp = null;
		RespMovimientosError resp_error = null;
		ArrayList<RespMovimientos> lista = new ArrayList<RespMovimientos>();
		this.enviar_mensaje();

		//Si es MOVIMIENTOS hay que recibir varios mensajes
		if(this.envio.getTipoMensaje().equals(CodigosMensajes.SOLMOVIMIENTOS)){
			MensajeDatos m_datos = null;
			do{
				//Recibe el mensaje
				m_datos = this.recibir_mensaje();

				if(m_datos==null)
					continue;
				
				System.out.println("RECIBIDO MOVIMIENTOS: "+m_datos.obtenerImprimible("CONSORCIO", "CAJERO"));

				//Comprobamos que sea del tipo RESMOVIMIENTOS
				if(m_datos.getTipoMensaje().equals(CodigosMensajes.RESMOVIMIENTOS)){
					//Si es respuesta de movimientos normal la añadimos a la lista
					try{
						resp = (RespMovimientos) m_datos;
						lista.add(resp);
					//Si es respuesta de movimientos error salimos del bucle
					}catch(ClassCastException e){
						resp_error = (RespMovimientosError) m_datos;
						break;
					}
				}
			}while((!envio.esContestacionCorrecta(m_datos.getTipoMensaje())) || (resp.getNmovimientos()>0));
			
			try {
				//Si no hay errores actualizamos los movimientos
				if(resp_error==null)
					this.interfaz.actualizarIUmovimientos(lista);
				//Si hay errores actualizamos el error
				else
					this.interfaz.actualizarIUmovimientos(resp_error);
			} catch (CodigoNoValidoException e) {
				e.printStackTrace();
			}
			
		//Si no es MOVIMIENTOS solo se recibe uno
		}else{
			//Recibimos mensajes hasta que la contestacion sea la adecuada
			m = this.recibir_mensaje();
			
			if(m!=null)
				System.out.println("RECIBIDO: "+m.obtenerImprimible("CONSORCIO", "CAJERO"));
			
			//Comprobamos si la contestacion es correcta
			if((m!=null) && (!envio.esContestacionCorrecta(m.getTipoMensaje())))
				m = null;
			
			try {
				this.interfaz.actualizarIU(m);
			} catch (CodigoNoValidoException e) {
				e.printStackTrace();
			}
		}
	}

	
	/**
     * Método que realiza el envio del mensaje.
     */
    private void enviar_mensaje(){
    	
    	Calendar time = Calendar.getInstance();
    	System.out.println("ENVIO: " + this.envio.getTipoMensaje() + " Cajero: " + this.cajero + " a las " + time.getTime() + " \n" + this.envio.toString());
    	//Crea el Datagrama a enviar
		DatagramPacket enviarPaquete = new DatagramPacket(this.envio.getBytes(),this.envio.size(),this.cajero.getConsorcio_address(),this.cajero.getConsorcio_port());
		
		try{
			//Enviamos el mensaje
			this.cajero.getSocketCajero().send(enviarPaquete);
		}catch (IOException e) {
			System.out.println("Error al enviar.");
			System.exit ( 0 );
		}
    }
    
    /**
     * Método que recibe el datagrama y lo parsea para obtener el Mensaje.
     * @return El Mensaje Respuesta de Datos correspondiente al datagrama recibido.
     */
    public MensajeRespDatos recibir_mensaje(){
    
    	Calendar time = Calendar.getInstance();
    	byte [] recibirDatos = new byte[1024];
		//Crea el Datagrama en donde recibir los datos
		DatagramPacket inputPacket = new DatagramPacket(recibirDatos, recibirDatos.length);
		
		//Establece un timeout
		try {
			this.cajero.getSocketCajero().setSoTimeout(100000);
		} catch (SocketException e1) {
			e1.printStackTrace();
			System.exit ( 0 );
		}
		
		try{
			//Recibe datos
			this.cajero.getSocketCajero().receive(inputPacket);
			
		}catch(SocketTimeoutException a){
			return null;
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
    	System.out.println("RECEPCION: " + recepcion.getTipoMensaje() + " Cajero: " + this.cajero.getId_cajero() +" a las " + time.getTime());
    	return (MensajeRespDatos) recepcion;
    }
    
    
}
