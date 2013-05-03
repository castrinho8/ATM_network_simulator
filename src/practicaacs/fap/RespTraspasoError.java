package practicaacs.fap;

public class RespTraspasoError extends RespDatosError {

	private static final long serialVersionUID = 6562567123749840000L;

	public RespTraspasoError(String origen, String destino, int numcanal, int nmsg,
			boolean codonline, CodigosError codigoError) {
		super(origen, destino, CodigosMensajes.RESTRASPASO, numcanal, nmsg, codonline,
				codigoError);
	}

	public RespTraspasoError() {}

}
