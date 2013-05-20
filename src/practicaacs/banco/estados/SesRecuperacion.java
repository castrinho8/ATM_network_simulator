package practicaacs.banco.estados;

import practicaacs.banco.Banco;
import practicaacs.fap.CodigosMensajes;
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
		if(m != null && m.getTipoMensaje().equals(CodigosMensajes.SOLFINREC)){
			b.establecerFinTraficoRecuperacion();
		}
	}

	@Override
	public boolean recuperacion() {
		return true;
	}
	
	

}
