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
		
		if(m != null && m.getTipoMensaje().equals(CodigosMensajes.SOLABRIRSESION)){
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

}
