package practicaacs.banco.estados;

import practicaacs.banco.Banco;
import practicaacs.fap.CodigosMensajes;
import practicaacs.fap.Mensaje;
import practicaacs.fap.RespCierreSesion;

public class SolPechar extends EstadoSesion {
	private static SolPechar instance;
	
	private SolPechar(){}
	
	@Override
	public void analizarMensaje(Mensaje m, Banco b) {
		if(m != null && m.getTipoMensaje().equals(CodigosMensajes.SOLCIERRESESION)){
			if(((RespCierreSesion) m).getCodResp()){
				b.establecerSesionPechada();
			}else{
				b.errorRespuestaSolicitud(m.getTipoMensaje(), ((RespCierreSesion) m).getCodError());
			}
		}
	}

	public static EstadoSesion instance() {
		if(instance == null)
			instance = new SolPechar();
		return instance;
	}

}
