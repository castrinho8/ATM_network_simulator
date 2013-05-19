package practicaacs.banco.estados;

import practicaacs.banco.Banco;
import practicaacs.fap.Mensaje;
import practicaacs.fap.MensajeDatos;


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
		if(m != null)
			switch(m.getTipoMensaje()){
			case SOLINIREC:
				b.sesionDetidaResp(m.getTipoMensaje(), 0, 0);
				break;
			case SOLSALDO:
			case SOLMOVIMIENTOS:
			case SOLREINTEGRO:
			case SOLABONO:
			case SOLTRASPASO:
				b.sesionDetidaResp(m.getTipoMensaje(), ((MensajeDatos) m).getNumcanal(),((MensajeDatos) m).getNmsg());
				break;
			default:
				break;
			}
	}


	@Override
	public boolean traficoActivo() {
		return false;
	}	
}
