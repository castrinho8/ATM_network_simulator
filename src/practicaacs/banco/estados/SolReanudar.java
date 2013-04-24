package practicaacs.banco.estados;

import practicaacs.banco.Banco;
import practicaacs.fap.CodigosMensajes;
import practicaacs.fap.Mensaje;
import practicaacs.fap.RespReanTrafico;

public class SolReanudar extends EstadoSesion {
	private static SolReanudar instance;
	
	private SolReanudar(){}

	public static EstadoSesion instance() {
		if(instance == null)
			instance = new SolReanudar();
		return instance;
	}
	
	@Override
	public void analizarMensaje(Mensaje m, Banco b) {
		if(m != null && m.getTipoMensaje().equals(CodigosMensajes.SOLREANUDARTRAFICO)){
			if(((RespReanTrafico) m).getCodResp()){
				b.establecerSesionReanudada();
			}else{
				b.errorRespuestaSolicitud(m.getTipoMensaje(), ((RespReanTrafico) m).getCodError());
			}
		}
	}

}
