package banco.estados;

import fap.Mensaje;
import banco.Banco;
import banco.csconsorcio.AnalizadorMensajes;

public abstract class EstadoSesion {

	public abstract void analizarMensaje(Mensaje m, Banco b);

}
