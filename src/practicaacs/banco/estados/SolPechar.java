package practicaacs.banco.estados;

import practicaacs.banco.Banco;
import practicaacs.fap.CodigosMensajes;
import practicaacs.fap.Mensaje;

public class SolPechar extends EstadoSesion {
	private static SolPechar instance;
	
	private SolPechar(){}
	
	@Override
	public void analizarMensaje(Mensaje m, Banco b) {
		if(m != null && m.getTipoMensaje().equals(CodigosMensajes.SOLCIERRESESION)){
			b.establecerSesionPechada();
			return;
		}
	}

	public static EstadoSesion instance() {
		if(instance == null)
			instance = new SolPechar();
		return instance;
	}

}
