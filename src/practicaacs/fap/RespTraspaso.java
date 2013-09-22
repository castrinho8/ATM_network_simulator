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

public class RespTraspaso extends MensajeRespDatos {

	private static final long serialVersionUID = 6454327230606401799L;
	private boolean signoOrigen;
	private int saldoOrigen;
	private boolean signoDestino;
	private int saldoDestino;
	
	/**
	 * Constructor del mensaje Reespuesta Traspaso.
	 * @param origen Origen del mensaje
	 * @param destino Destino del Mensaje
	 * @param numcanal Numero de canal
	 * @param nmsg Numero de mensaje
	 * @param codonline Codigo ON-LINE
	 * @param cod_resp Codigo de Respuesta
	 * @param signo Signo del saldo
	 * @param saldo Saldo
	 */
	public RespTraspaso(String origen, String destino, int numcanal, int nmsg,
			boolean codonline, CodigosRespuesta cod_resp, boolean signoOrigen,
			int saldoOrigen, boolean signoDestino, int saldoDestino) {
		super(origen, destino,CodigosMensajes.RESTRASPASO , numcanal, nmsg, codonline, cod_resp);
		this.signoOrigen = signoOrigen;
		this.saldoOrigen = saldoOrigen;
		this.saldoDestino = saldoDestino;
		this.signoDestino = signoDestino;
	}
	
	public RespTraspaso(){}

	@Override
	protected String printCuerpo() {
		return String.format("%1s%010d%1s%010d",this.signoOrigen ? "+" : "-", this.saldoOrigen,this.signoDestino ? "+" : "-", this.saldoDestino);
	}
	
	
	@Override
	protected void parseComp(String bs) throws MensajeNoValidoException {
		super.parseComp(bs);

		if(bs.length() != 50)
			throw new MensajeNoValidoException("Lonxitude (" + bs.length() + ") non válida (RespSaldo)");
		
		if(bs.substring(28,29).equals("+") || bs.substring(28,29).equals("-"))
			this.signoOrigen = bs.substring(28,29).equals("+");
		else
			throw new MensajeNoValidoException("Mal formato en el signo origen (" + bs.substring(28, 29) + ") (RespTraspaso)");
		
		try{
			this.saldoOrigen = new Integer(bs.substring(29, 39));
		}catch(NumberFormatException e){
			throw new MensajeNoValidoException("Error saldo origen (" + bs.substring(29, 39) + ") (RespTraspaso)");
		}
		
		if(bs.substring(39,40).equals("+") || bs.substring(39,40).equals("-"))
			this.signoDestino = bs.substring(39,40).equals("+");
		else
			throw new MensajeNoValidoException("Mal formato en el signo origen (RespTraspaso)");
		
		try{
			this.saldoDestino = new Integer(bs.substring(40, 50));
		}catch(NumberFormatException e){
			throw new MensajeNoValidoException("Error saldo origen (" + bs.substring(40,50) + ") (RespTraspaso)");
		}
	}

	public boolean getSignoOrigen() {
		return signoOrigen;
	}

	public int getSaldoOrigen() {
		return saldoOrigen;
	}

	public boolean getSignoDestino() {
		return signoDestino;
	}

	public int getSaldoDestino() {
		return saldoDestino;
	}

	
	
	
}
