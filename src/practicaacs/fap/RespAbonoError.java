package practicaacs.fap;

public class RespAbonoError extends RespDatosError {

	private static final long serialVersionUID = 813915827057548514L;

	public RespAbonoError(String origen, String destino, int numcanal, int nmsg,
			boolean codonline, CodigosError codigoError) {
		super(origen, destino, CodigosMensajes.RESABONO, numcanal, nmsg, codonline,
				codigoError);
	}

	public RespAbonoError() {}
}
