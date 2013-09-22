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

public class RespReintegro extends MensajeRespDatos {

	private static final long serialVersionUID = 2560629111000703341L;
	private boolean signo;
	private int saldo;
	
	/**
	 * Constructor mensaje Respesta Reintegro.
	 * @param origen Origen del mensaje
	 * @param destino Destino del Mensaje
	 * @param numcanal Numero de canal
	 * @param nmsg Numero de mensaje
	 * @param codonline Codigo ON-LINE
	 * @param cod_resp Codigo de Respuesta
	 * @param signo Signo del saldo
	 * @param saldo Saldo
	 */
	public RespReintegro(String origen, String destino, int numcanal, int nmsg,
			boolean codonline, CodigosRespuesta cod_resp, boolean signo,
			int saldo) {
		super(origen, destino, CodigosMensajes.RESREINTEGRO, numcanal, nmsg, codonline, cod_resp);
		
		assert(saldo >= 0);
		
		this.signo = signo;
		this.saldo = saldo;
	}
	
	public RespReintegro(){}
	
	@Override
	protected String printCuerpo() {
		return String.format("%1s%010d",this.signo ? "+" : "-", this.saldo);
	}

	@Override
	protected void parseComp(String bs) throws MensajeNoValidoException {
		super.parseComp(bs);
		
		if(bs.length() != 39)
			throw new MensajeNoValidoException("Lonxitude (" + bs.length() + ") non válida (RespSaldo)");
		
		if(bs.substring(28,29).equals("+") || bs.substring(28,29).equals("-"))
			this.signo = bs.substring(28,29).equals("+");
		else
			throw new MensajeNoValidoException("Mal formato en el signo (RespSaldo)");
		
		try{
			this.saldo = new Integer(bs.substring(29, 39));
		}catch(NumberFormatException e){
			throw new MensajeNoValidoException("Error saldo (" + bs.substring(29, 39) + ") (RespSaldo)");
		}
	}

	public boolean getSigno() {
		return signo;
	}

	public int getSaldo() {
		return saldo;
	}
	
	
}
