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

public class SolIniTraficoRec extends Mensaje {

	private static final long serialVersionUID = -2305078832641352933L;

	/**
	 * Constructor del mensaje Solicitud de Tráfico en Recuperación.
	 * @param origen Origen del mensaje.
	 * @param destino Destino del mensaje.
	 */
	public SolIniTraficoRec(String origen, String destino) {
		super(origen, destino,CodigosMensajes.SOLINIREC);
	}
	
	public SolIniTraficoRec(){}

	@Override
	protected void parseComp(String bs) throws MensajeNoValidoException {
		super.parseComp(bs); 
	}
}
