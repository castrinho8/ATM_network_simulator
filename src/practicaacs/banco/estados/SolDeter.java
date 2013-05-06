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
