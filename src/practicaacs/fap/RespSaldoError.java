package practicaacs.fap;

public class RespSaldoError extends RespDatosError {

	private static final long serialVersionUID = 2571889635224128529L;

	public RespSaldoError(String origen, String destino, int numcanal, int nmsg,
			boolean codonline, CodigosError codigoError) {
		super(origen, destino, CodigosMensajes.RESSALDO, numcanal, nmsg, codonline,
				codigoError);
	}

	public RespSaldoError() {}

}
