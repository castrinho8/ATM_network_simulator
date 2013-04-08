package practicaacs.banco.estados;

import practicaacs.banco.Banco;
import practicaacs.fap.CodigosMensajes;
import practicaacs.fap.Mensaje;

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
		if(m != null && m.getTipoMensaje().equals(CodigosMensajes.SOLDETENERTRAFICO)){
			b.establecerSesionDetida();
			return;
		}
	}


}
