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
import practicaacs.fap.MensajeDatos;
import practicaacs.fap.SolAbono;
import practicaacs.fap.SolMovimientos;
import practicaacs.fap.SolReintegro;
import practicaacs.fap.SolSaldo;
import practicaacs.fap.SolTraspaso;

public class SesRecuperacion extends EstadoSesion {
	private static SesRecuperacion instance;
	
	private SesRecuperacion(){}

	public static EstadoSesion instance() {
		if(instance == null)
			instance = new SesRecuperacion();
		return instance;
	}
	
	@Override
	public void analizarMensaje(Mensaje m, Banco b) {
		System.out.println(m.obtenerImprimible("CONSORCIO", "BANCO"));

		if(m != null){
			
			//Se é fin de recuperacion
			if(m.getTipoMensaje().equals(CodigosMensajes.SOLFINREC)){
				b.establecerFinTraficoRecuperacion();
				return;
			}
			
			//Se non é solicitude de datos sae
			if(!m.es_solicitudDatos())
				return;
			
			//Obten a canle pola que chegou
			int canle = ((MensajeDatos)m).getNumcanal();

			//Comproba si xa hay resposta para dita canle e se a hay responde
			if(b.ultimaMensaxeRespondida(canle)){
				System.out.println("Entra");
				b.manexaRespostaUltimaMensaxe(canle);
				return;
			}
			//Se non hay resposta, crea a mesma e responde
			else{
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
		}
	}

	@Override
	public boolean recuperacion() {
		return true;
	}
	
	public boolean sesionAberta(){
		return false;
	}

}
