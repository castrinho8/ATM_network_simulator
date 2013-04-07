package practicaacs.banco.estados;

import practicaacs.banco.Banco;
import practicaacs.fap.CodigosMensajes;
import practicaacs.fap.Mensaje;

public class SesNonAberta extends EstadoSesion {
	private static SesNonAberta instance;
	
	private SesNonAberta(){}

	public static EstadoSesion instance() {
		if(instance == null)
			instance = new SesNonAberta();
		return instance;
	}
	
	@Override
	public void analizarMensaje(Mensaje m, Banco b) {
	}

}
