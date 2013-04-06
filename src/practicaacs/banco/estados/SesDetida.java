package practicaacs.banco.estados;

import practicaacs.banco.Banco;
import practicaacs.fap.Mensaje;

public class SesDetida extends EstadoSesion {
	private static SesDetida instance;
	
	private SesDetida(){}

	public static EstadoSesion instance() {
		if(instance == null)
			instance = new SesDetida();
		return instance;
	}
	
	@Override
	public void analizarMensaje(Mensaje m, Banco b) {
		// TODO Auto-generated method stub

	}

}
