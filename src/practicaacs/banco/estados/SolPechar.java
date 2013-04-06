package practicaacs.banco.estados;

import practicaacs.banco.Banco;
import practicaacs.fap.Mensaje;

public class SolPechar extends EstadoSesion {
	private static SolPechar instance;
	
	private SolPechar(){}
	
	@Override
	public void analizarMensaje(Mensaje m, Banco b) {
		// TODO Auto-generated method stub

	}

	public static EstadoSesion instance() {
		if(instance == null)
			instance = new SolPechar();
		return instance;
	}

}
