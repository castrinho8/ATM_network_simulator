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
 * Enum que define el comportamiento que se debe tomar frente a un 
 * mensaje de datos recibido por el cajero.
 */
public enum EstadoEnvio{

	ENVIO_CORRECTO(1),RECHAZAR_PETICION(2),ALMACENAMIENTO(3);
	private int numero;

	private EstadoEnvio(int numero){
		this.numero = numero;
	}
	
}
	

