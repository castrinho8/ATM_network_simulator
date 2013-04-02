package banco.estados;

import banco.Banco;
import fap.CodigosMensajes;
import fap.Mensaje;

public class SolApertura extends EstadoSesion {

	@Override
	public void analizarMensaje(Mensaje m, Banco b) {
		
		if(m != null && m.getTipoMensaje().equals(CodigosMensajes.ABRIRSESION)){
				b.abrirSesionAceptada();
				return;
		}
	}

}
