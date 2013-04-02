package banco.estados;

import banco.Banco;
import fap.Mensaje;

public class SesAberta extends EstadoSesion {

	private static SesAberta instance = null;
	
	private SesAberta(){}
	
	@Override
	public void analizarMensaje(Mensaje m, Banco b) {
		// TODO Auto-generated method stub

	}

	public static SesAberta instance() {
		if(instance == null)
			instance = new SesAberta();
		return instance;
	}

}
