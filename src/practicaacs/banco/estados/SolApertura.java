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
import practicaacs.fap.RespAperturaSesion;

public class SolApertura extends EstadoSesion {

	private static SolApertura instance;
	
	private SolApertura(){}
	
	@Override
	public void analizarMensaje(Mensaje m, Banco b) {
		
		if(m != null && m.getTipoMensaje().equals(CodigosMensajes.RESABRIRSESION)){
			if(((RespAperturaSesion) m).getCodResp()){
				b.establecerSesionAceptada();
			}else{
				b.errorRespuestaSolicitud(m.getTipoMensaje(), ((RespAperturaSesion) m).getCodError());
			}
		}
	}

	public static EstadoSesion instance() {
		if (instance == null)
			instance = new SolApertura();
		return instance;
	}

	@Override
	public boolean sesionAberta() {
		return false;
	}
	
	

}
