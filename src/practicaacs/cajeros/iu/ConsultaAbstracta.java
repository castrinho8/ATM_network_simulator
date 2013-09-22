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
package practicaacs.cajeros.iu;

import java.util.ArrayList;

import practicaacs.cajeros.Envio;
import practicaacs.fap.CodigoNoValidoException;
import practicaacs.fap.CodigosMensajes;
import practicaacs.fap.MensajeDatos;
import practicaacs.fap.MensajeRespDatos;
import practicaacs.fap.RespMovimientos;
import practicaacs.fap.RespMovimientosError;

/**
 * Clase abstracta que implementa funciones comunes de las ventanas de la IU.
 */
abstract public class ConsultaAbstracta extends javax.swing.JFrame {

	/**
	 * Método que realiza el envio de una operación.
	 * @param env El Envio a partir del cual se creará el mensaje a enviar.
	 */
	abstract public void envia_consulta(Envio env);
    
	/**
	 * Método que actualiza la IU.
	 * @param message El mensaje recibido con el que actualizar la IU.
	 * @throws CodigoNoValidoException Excepción que se lanza cuando se utiliza el método en una consulta no debida.
	 */
    public void actualizarIU(MensajeRespDatos message) throws CodigoNoValidoException{
    	throw new CodigoNoValidoException("Error: El método actualizarIU no puede ser llamado en esta consulta.");
    }
    
    /**
     * Método que actualiza la IU para la ventana de movimientos.
     * @param lista La lista de mensajes con los movimientos recibida.
     * @throws CodigoNoValidoException Excepción que se lanza cuando se utiliza el método en una consulta no debida.
     */
    public void actualizarIUmovimientos(ArrayList<RespMovimientos> lista) throws CodigoNoValidoException{
    	throw new CodigoNoValidoException("Error: El método actualizarIUmovimientos solo puede ser llamado para las consultas de movimientos.");
    }

    /**
     * Método que actualiza la IU para la ventana de movimientos cuando se recibe un error.
     * @param elemento El mensaje respuesta de error recibido.
     * @throws CodigoNoValidoException Excepción que se lanza cuando se utiliza el método en una consulta no debida.
     */
    public void actualizarIUmovimientos(RespMovimientosError elemento) throws CodigoNoValidoException{
    	throw new CodigoNoValidoException("Error: El método actualizarIUmovimientos solo puede ser llamado para las consultas de movimientos.");
    }
    
}
