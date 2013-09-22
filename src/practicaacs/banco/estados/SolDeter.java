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
import practicaacs.fap.CodigosMensajes;
import practicaacs.fap.Mensaje;
import practicaacs.fap.RespDetTrafico;

public class SolDeter extends EstadoSesion {

	private static SolDeter instance;
	
	private SolDeter(){}

	public static EstadoSesion instance() {
		if(instance == null)
			instance = new SolDeter();
		return instance;
	}
	
	@Override
	public void analizarMensaje(Mensaje m, Banco b) {
		if(m != null && m.getTipoMensaje().equals(CodigosMensajes.RESDETENERTRAFICO)){
			if(((RespDetTrafico) m).getCodResp()){
				b.establecerSesionDetida();
			}else{
				b.errorRespuestaSolicitud(m.getTipoMensaje(), ((RespDetTrafico) m).getCodError());
			}
		}
	}


}
