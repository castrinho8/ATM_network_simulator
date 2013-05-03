package practicaacs.banco.estados;

import practicaacs.fap.Mensaje;
import practicaacs.banco.Banco;

public abstract class EstadoSesion {

	public abstract void analizarMensaje(Mensaje m, Banco b);

	public boolean sesionAberta(){
		return true;
	}

	public boolean traficoActivo(){
		return true;
	}

}
