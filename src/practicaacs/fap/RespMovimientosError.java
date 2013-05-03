package practicaacs.fap;

public class RespMovimientosError extends RespDatosError {

	private static final long serialVersionUID = -2048642012062371860L;

	public RespMovimientosError(String origen, String destino, int numcanal, int nmsg,
			boolean codonline, CodigosError codigoError) {
		super(origen, destino, CodigosMensajes.RESMOVIMIENTOS, numcanal, nmsg, codonline,
				codigoError);
	}

	public RespMovimientosError() {}

}
