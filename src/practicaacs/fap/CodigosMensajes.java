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

public enum CodigosMensajes {
	SOLINIREC(01), SOLFINREC(02),RESABRIRSESION(11),RESDETENERTRAFICO(13),
	RESREANUDARTRAFICO(14),RESCIERRESESION(12),SOLSALDO(31),SOLMOVIMIENTOS(32),
	SOLREINTEGRO(33),SOLABONO(34),SOLTRASPASO(35),RESINIREC(91),RESFINREC(92),SOLABRIRSESION(81),
	SOLDETENERTRAFICO(83),SOLREANUDARTRAFICO(84),SOLCIERRESESION(82),RESSALDO(61),
	RESMOVIMIENTOS(62),RESREINTEGRO(63),RESABONO(64),RESTRASPASO(65);
	private int numero;

	private CodigosMensajes(int numero){
		this.numero = numero;
	}
	
	public int getNum(){
		return this.numero;
	}

	public static CodigosMensajes parse(String s) throws CodigoNoValidoException {
		try{
			int numero = new Integer(s);
			for(CodigosMensajes c : CodigosMensajes.values()){
				if(c.numero == numero){
					return c;
				}
			}
		}catch(NumberFormatException e){}
		
		throw new CodigoNoValidoException();
	}
}
