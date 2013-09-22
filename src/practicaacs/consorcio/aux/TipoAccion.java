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
package practicaacs.consorcio.aux;

/**
 * Enum que modela un tipo de acción a realizar por la
 * ConexiónConsorcio_Cajeros.
 */
public enum TipoAccion{

	CONEXION(1),RECUPERACION(2),FIN_RECUPERACION(3),ENVIO(4);
	private int numero;

	private TipoAccion(int numero){
		this.numero = numero;
	}
	
}
