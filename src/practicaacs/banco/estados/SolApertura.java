package practicaacs.banco.estados;

import practicaacs.banco.Banco;
import practicaacs.fap.CodigosMensajes;
import practicaacs.fap.Mensaje;

public class SolApertura extends EstadoSesion {

	private static SolApertura instance;
	
	private SolApertura(){}
	
	@Override
	public void analizarMensaje(Mensaje m, Banco b) {
		
		if(m != null && m.getTipoMensaje().equals(CodigosMensajes.SOLABRIRSESION)){
				b.establecerSesionAceptada();
				return;
		}
	}

	public static EstadoSesion instance() {
		if (instance == null)
			instance = new SolApertura();
		return instance;
	}

}
