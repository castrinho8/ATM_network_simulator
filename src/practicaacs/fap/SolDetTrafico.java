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
package practicaacs.fap;

public class SolDetTrafico extends Mensaje {

	private static final long serialVersionUID = 4577464211579542772L;

	/**
	 * Constructor del mensaje Solicitud para Detener el Tráfico.
	 * @param origen Origen del Mensaje
	 * @param destino Destino del Mensaje
	 */
	public SolDetTrafico(String origen, String destino) {
		super(origen, destino,CodigosMensajes.SOLDETENERTRAFICO);
	}
	
	public SolDetTrafico(){}

	@Override
	protected void parseComp(String bs) throws MensajeNoValidoException {
		super.parseComp(bs);
	}

}
