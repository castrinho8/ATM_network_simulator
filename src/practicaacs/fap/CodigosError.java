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

public enum CodigosError {
	CORRECTO(00, "Correcto"),
	YAABIERTA(30, "La sesión ya estaba abierta"),
	FUERASEC(31, "Mensaje fuera de secuencia"),
	CANALOCUP(32, "Mensaje por un canal ocupado"),
	TRAFNODET(33, "Tráfico no detenido"),
	NORECUPERACION(34, "No está establecida la recuperación"),
	NOSESION(35, "Petición denegada por no existir sesión"),
	OTRASCAUSAS(99, "Consulta Denegada por otras causas.");
	
	private int valor;
	private String msg;
	
	private CodigosError(int valor, String msg){
		this.valor = valor;
	}
	
	public String getMensaje(){
		return this.msg;
	}
	
	public String getCodigo(){
		if (this.valor < 10){
			return '0' + (new Integer(this.valor)).toString();
		}
		return (new Integer(this.valor)).toString();
		
	}
	
	public static CodigosError parse(String s) throws CodigoNoValidoException {
		try{
			int numero = new Integer(s);
			for(CodigosError c : CodigosError.values()){
				if(c.valor == numero){
					return c;
				}
			}
		}catch(NumberFormatException e){}
		throw new CodigoNoValidoException();
	}	
	
}
