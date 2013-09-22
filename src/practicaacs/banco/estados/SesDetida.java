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
package practicaacs.banco.estados;

import practicaacs.banco.Banco;
import practicaacs.fap.Mensaje;
import practicaacs.fap.MensajeDatos;


public class SesDetida extends EstadoSesion {
	private static SesDetida instance;
	
	private SesDetida(){}

	public static EstadoSesion instance() {
		if(instance == null)
			instance = new SesDetida();
		return instance;
	}
	
	@Override
	public void analizarMensaje(Mensaje m, Banco b) {
		if(m != null)
			switch(m.getTipoMensaje()){
			case SOLINIREC:
				b.sesionDetidaResp(m.getTipoMensaje(), 0, 0);
				break;
			case SOLSALDO:
			case SOLMOVIMIENTOS:
			case SOLREINTEGRO:
			case SOLABONO:
			case SOLTRASPASO:
				b.sesionDetidaResp(m.getTipoMensaje(), ((MensajeDatos) m).getNumcanal(),((MensajeDatos) m).getNmsg());
				break;
			default:
				break;
			}
	}


	@Override
	public boolean traficoActivo() {
		return false;
	}	
}
