package practicaacs.banco.estados;

import practicaacs.banco.Banco;
import practicaacs.fap.Mensaje;

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
		// TODO Auto-generated method stub

	}

	@Override
	public boolean recuperacion() {
		return true;
	}
	
	

}
