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

public class RespFinTraficoRec extends Mensaje {

	private static final long serialVersionUID = 5440864609643008701L;
	private boolean cod_resp;
	private CodigosError cod_error;
	
	/**
	 * Constructor de Mensaje Respuesta Fin de Tráfico de Recuperación.
	 * @param origen Origen del Mensaje
	 * @param destino Destino del Mensaje
	 * @param cod_resp Codigo de Respuesta
	 * @param cod_error Código de Error
	 */
	public RespFinTraficoRec(String origen, String destino, boolean cod_resp,
			CodigosError cod_error) {
		super(origen, destino,CodigosMensajes.RESFINREC);
		this.cod_resp = cod_resp;
		this.cod_error = cod_error;
	}
	
	public RespFinTraficoRec(){}
	
	@Override
	protected String printCuerpo(){
		return String.format("%2s%s", this.cod_resp ? "00" : "11",this.cod_error.getCodigo());
	}

	@Override
	protected void parseComp(String bs) throws MensajeNoValidoException {
		super.parseComp(bs);
		
		if(bs.length() != 22)
			throw new MensajeNoValidoException("Lonxitude (" + bs.length() + ") non válida (RespIniTrafico)");
		
		if (bs.toString().substring(18, 20).equals("11") || bs.toString().substring(18, 20).equals("00"))
			this.cod_resp = bs.toString().substring(18, 20).equals("00");
		else
			throw new MensajeNoValidoException("Formato codigo resposta non válido (RespIniTrafico)");

		try{
			this.cod_error = CodigosError.parse((bs.toString().substring(20, 22)));
		} catch (CodigoNoValidoException e) {
			throw new MensajeNoValidoException("Codigo de error non válido (RespIniTrafico)");
		}	
	}
	public boolean getCodResp() {
		return cod_resp;
	}

	public CodigosError getCodError() {
		return cod_error;
	}
	
	


}
