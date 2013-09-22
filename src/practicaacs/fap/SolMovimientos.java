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

public class SolMovimientos extends MensajeDatos {

	private static final long serialVersionUID = -2378212785182317311L;
	private String num_tarjeta;
	private int num_cuenta;
	
	public SolMovimientos(String origen, String destino,
			CodigosMensajes tipoMensaje, int numcanal, int nmsg,
			boolean codonline, String num_tarjeta, int num_cuenta) {
		super(origen, destino, tipoMensaje, numcanal, nmsg, codonline);
		
		assert(num_tarjeta.length() <= 11);
		assert(num_cuenta >= 0);
		assert(num_cuenta <=9);
		
		this.num_tarjeta = num_tarjeta;
		this.num_cuenta = num_cuenta;
	}
	
	public SolMovimientos(){}

	@Override
	protected String printCuerpo() {
		return String.format("%11s%1d",this.num_tarjeta, this.num_cuenta);
	}

	public String getNum_tarjeta() {
		return num_tarjeta;
	}

	public int getNum_cuenta() {
		return num_cuenta;
	}

	@Override
	protected void parseComp(String bs) throws MensajeNoValidoException {
		super.parseComp(bs);

		if(bs.length() != 38)
			throw new MensajeNoValidoException("Lonxitude (" + bs.length() + ") non válida. (SolMovimeintos)");
		
		this.num_tarjeta = bs.substring(26,37).trim();
		try {
			this.num_cuenta = new Integer(bs.substring(37,38));
		} catch(NumberFormatException e){
			throw new MensajeNoValidoException("Error de formato dos numeros.");
		}	
	}
	
	protected String formatearMensaje(){
		return super.formatearMensaje()+"---- Tarjeta: "+this.num_tarjeta+"("+this.num_cuenta+") ";
	}
	
}

