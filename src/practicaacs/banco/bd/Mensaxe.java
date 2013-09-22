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
package practicaacs.banco.bd;

public class Mensaxe {
	public String tipo;
	public Integer numCanal;
	public Integer numMenx;
	public boolean enviado;
	
	public Mensaxe(String tipo, Integer numCanal, Integer numMenx, boolean enviado) {
		this.tipo = tipo;
		this.numCanal = numCanal;
		this.numMenx = numMenx;
		this.enviado = enviado;
	}
}
