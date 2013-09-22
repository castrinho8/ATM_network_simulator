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
import practicaacs.fap.*;
public class SesAberta extends EstadoSesion {

	private static SesAberta instance = null;
	
	private SesAberta(){}
	
	@Override
	public void analizarMensaje(Mensaje m, Banco b) {
		System.out.println(m.obtenerImprimible("CONSORCIO", "BANCO"));
		if(m != null)
			switch(m.getTipoMensaje()){
			case SOLSALDO:
				b.facerConsultaSaldo(	((SolSaldo) m).getNumcanal(),
										((SolSaldo) m).getNmsg(),
										((SolSaldo) m).getCodonline(),
										((SolSaldo) m).getNum_tarjeta(),
										((SolSaldo) m).getNum_cuenta()
									);
				return;
			case SOLMOVIMIENTOS:
				b.facerConsultaMovementos(	((SolMovimientos) m).getNumcanal(),
											((SolMovimientos) m).getNmsg(),
											((SolMovimientos) m).getCodonline(),
											((SolMovimientos) m).getNum_tarjeta(),
											((SolMovimientos) m).getNum_cuenta()
										);
				return;
			case SOLINIREC:
				b.establecerTraficoRecuperacion();
				return;
			case SOLREINTEGRO:
				b.facerReintegro(	((SolReintegro) m).getNumcanal(),
									((SolReintegro) m).getNmsg(), 
									((SolReintegro) m).getCodonline(), 
									((SolReintegro) m).getNum_tarjeta(), 
									((SolReintegro) m).getNum_cuenta(), 
									((SolReintegro) m).getImporte()
								);
				return;
			case SOLABONO:
				b.facerAbono(	((SolAbono) m).getNumcanal(),
								((SolAbono) m).getNmsg(),
								((SolAbono) m).getCodonline(),
								((SolAbono) m).getNum_tarjeta(),
								((SolAbono) m).getNum_cuenta(),
								((SolAbono) m).getImporte()
							);
				return;
			case SOLTRASPASO:
				b.facerTranspaso(	((SolTraspaso) m).getNumcanal(),
									((SolTraspaso) m).getNmsg(), 
									((SolTraspaso) m).getCodonline(), 
									((SolTraspaso) m).getNum_tarjeta(), 
									((SolTraspaso) m).getNum_cuenta_origen(), 
									((SolTraspaso) m).getNum_cuenta_destino(), 
									((SolTraspaso) m).getImporte()
								);
				return;
			default:
				break;
			}
	}

	public static SesAberta instance() {
		if(instance == null)
			instance = new SesAberta();
		return instance;
	}

}
