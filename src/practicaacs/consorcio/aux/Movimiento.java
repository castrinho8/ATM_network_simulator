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

import java.util.Date;

import practicaacs.fap.CodigosMovimiento;

/**
 * Clase que implementa cada uno de los movimientos que se obtienen de la BD.
 */
public class Movimiento {
	private int codigo;
	private boolean signo;
	private int importe;
	private Date data;
	private CodigosMovimiento tipo;

	/**
	 * Constructor de la clase.
	 * @param codigo El codigo de movimiento.
	 * @param sign El signo del movimiento.
	 * @param importe El importe del movimiento.
	 * @param data La fecha del movimiento.
	 * @param tipo El tipo de movimiento.
	 */
	public Movimiento(int codigo,boolean sign, int importe, Date data, CodigosMovimiento tipo) {
		super();
		this.codigo = codigo;
		this.signo = sign;
		this.importe = importe;
		this.data = data;
		this.tipo = tipo;
	}
	
	/**
	 * Getter del codigo de movimiento.,
	 * @return El codigo de movimiento.
	 */
	public int getCodigo() {
		return codigo;
	}

	/**
	 * Getter del signo del movimiento.
	 * @return El signo del movimiento.
	 */
	public boolean getSigno() {
		return signo;
	}

	/**
	 * Getter del importe del movimiento.
	 * @return El importe del movimiento.
	 */
	public int getImporte() {
		return importe;
	}

	/**
	 * Getter de la fecha del movimiento.
	 * @return La fecha del movimiento.
	 */
	public Date getData() {
		return data;
	}

	/**
	 * Getter del tipo de movimiento.
	 * @return El tipo de movimiento.
	 */
	public CodigosMovimiento getTipo() {
		return tipo;
	}

}
