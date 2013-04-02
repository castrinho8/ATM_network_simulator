package practicaacs.banco.estados;

import practicaacs.banco.Banco;
import practicaacs.fap.CodigosMensajes;
import practicaacs.fap.Mensaje;

public class SolApertura extends EstadoSesion {

	@Override
	public void analizarMensaje(Mensaje m, Banco b) {
		
		if(m != null && m.getTipoMensaje().equals(CodigosMensajes.ABRIRSESION)){
				b.abrirSesionAceptada();
				return;
		}
	}

}
