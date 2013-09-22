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

public abstract class RespDatosError extends MensajeRespDatos {

	private static final long serialVersionUID = -2043917782582614162L;
	private CodigosError codigoError;

	public RespDatosError(String origen, String destino,
			CodigosMensajes tipoMensaje, int numcanal, int nmsg,
			boolean codonline, CodigosError codigoError) {
		super(origen, destino, tipoMensaje, numcanal, nmsg, codonline, CodigosRespuesta.CONSDEN);
		this.codigoError = codigoError;
	}

	public RespDatosError() {}

	
	public CodigosError getCodigoError() {
		return codigoError;
	}

	@Override
	protected void parseComp(String bs) throws MensajeNoValidoException {
		super.parseComp(bs);
		
		if(bs.length() < 30)
			throw new MensajeNoValidoException("Lonxitude (" + bs.length() + ") non válida (RespDatosError)");
		
		try {
			this.codigoError = CodigosError.parse(bs.toString().substring(28, 30));
		} catch (CodigoNoValidoException e) {
			throw new MensajeNoValidoException("Formato de codigo non válido (RespDatosError)");
		}
		
	}

	@Override
	protected String printCuerpo() {
		return this.codigoError.getCodigo();
	}

	
	
	
}
