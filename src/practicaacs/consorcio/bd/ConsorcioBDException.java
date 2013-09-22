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
package practicaacs.consorcio.bd;

/**
 * Exepción utilizada cuando se producen errores en el acceso a la 
 * BD del consorcio.
 */
public class ConsorcioBDException extends Exception{

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor de la clase.
	 */
	public ConsorcioBDException() {}

	/**
	 * Constructor de la clase.
	 * @param message El mensaje a mostrar.
	 */
	public ConsorcioBDException(String message) {
		super(message);
	}
}


