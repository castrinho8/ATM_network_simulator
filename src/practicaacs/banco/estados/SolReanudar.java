package practicaacs.banco.estados;

import practicaacs.banco.Banco;
import practicaacs.fap.Mensaje;

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
		// TODO Auto-generated method stub

	}

}
