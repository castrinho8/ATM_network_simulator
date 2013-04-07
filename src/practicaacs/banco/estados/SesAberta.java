package practicaacs.banco.estados;

import practicaacs.banco.Banco;
import practicaacs.fap.ConsultaMovimientos;
import practicaacs.fap.ConsultaSaldo;
import practicaacs.fap.Mensaje;
import practicaacs.fap.SolicitudAbono;
import practicaacs.fap.SolicitudReintegro;
import practicaacs.fap.SolicitudTraspaso;

public class SesAberta extends EstadoSesion {

	private static SesAberta instance = null;
	
	private SesAberta(){}
	
	@Override
	public void analizarMensaje(Mensaje m, Banco b) {
		if(m != null)
			switch(m.getTipoMensaje()){
			case CONSULTARSALDO:
				b.facerConsultaSaldo(	((ConsultaSaldo) m).getNumcanal(),
										((ConsultaSaldo) m).getNmsg(),
										((ConsultaSaldo) m).isCodonline(),
										((ConsultaSaldo) m).getNum_tarjeta(),
										((ConsultaSaldo) m).getNum_cuenta()
									);
				return;
			case CONSULTARMOVIMIENTOS:
				b.facerConsultaMovementos(	((ConsultaMovimientos) m).getNumcanal(),
											((ConsultaMovimientos) m).getNmsg(),
											((ConsultaMovimientos) m).isCodonline(),
											((ConsultaMovimientos) m).getNum_tarjeta(),
											((ConsultaMovimientos) m).getNum_cuenta()
										);
				return;
			case SOLTRAFICOREC:
				b.establecerTraficoRecuperacion();
				return;
			case REINTEGRO:
				b.facerReintegro(	((SolicitudReintegro) m).getNumcanal(),
									((SolicitudReintegro) m).getNmsg(), 
									((SolicitudReintegro) m).isCodonline(), 
									((SolicitudReintegro) m).getNum_tarjeta(), 
									((SolicitudReintegro) m).getNum_cuenta(), 
									((SolicitudReintegro) m).getImporte()
								);
				return;
			case ABONO:
				b.facerAbono(	((SolicitudAbono) m).getNumcanal(),
								((SolicitudAbono) m).getNmsg(),
								((SolicitudAbono) m).isCodonline(),
								((SolicitudAbono) m).getNum_tarjeta(),
								((SolicitudAbono) m).getNum_cuenta(),
								((SolicitudAbono) m).getImporte()
							);
				return;
			case TRASPASO:
				b.facerTranspaso(	((SolicitudTraspaso) m).getNumcanal(),
									((SolicitudTraspaso) m).getNmsg(), 
									((SolicitudTraspaso) m).isCodonline(), 
									((SolicitudTraspaso) m).getNum_tarjeta(), 
									((SolicitudTraspaso) m).getNum_cuenta_origen(), 
									((SolicitudTraspaso) m).getNum_cuenta_destino(), 
									((SolicitudTraspaso) m).getImporte()
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
