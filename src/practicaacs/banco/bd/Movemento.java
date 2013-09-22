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

import java.util.Date;

public class Movemento {
	public int codigo;
	public int importe;
	public Date data;
	public String tipo;
	public int numtipo;
	
	public Movemento(int codigo, int importe, Date data, String tipo) {
		super();
		this.codigo = codigo;
		this.importe = importe;
		this.data = data;
		this.tipo = tipo;
	}
	
	public Movemento(int codigo, int importe, Date data, String tipo, int numtipo) {
		super();
		this.codigo = codigo;
		this.importe = importe;
		this.data = data;
		this.tipo = tipo;
		this.numtipo = numtipo;
	}
	
}
