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

public enum CodigosMovimiento {
	
	REINTEGRO(10), TRANSEMITIDO(11), TRANSRECIB(12), PAGORECIB(13), ABONO(50), COBROCHEQUE(51),OTRO(99);
	
	private int numero;

	private CodigosMovimiento(int numero){
		this.numero = numero;
	}

	public static CodigosMovimiento getTipoMovimiento(int valor) throws CodigoNoValidoException{
		switch(valor){
			case 10:
				return REINTEGRO;
			case 11:
				return TRANSEMITIDO;
			case 12:
				return TRANSRECIB;
			case 13:
				return PAGORECIB;
			case 50:
				return ABONO;
			case 51:
				return COBROCHEQUE;
			case 99:
				return OTRO;
			default:
				throw new CodigoNoValidoException("" + valor);
		}
	}
	
	public static boolean getSigno(CodigosMovimiento codigo){
		//Si es una transferencia recibida, pago recibido, abono, o cobro de cheque el signo es positivo
		return ((codigo == TRANSRECIB)||(codigo == PAGORECIB)||(codigo == ABONO)||(codigo == COBROCHEQUE));
	}

	public int getNum() {
		return numero;
	}
}
