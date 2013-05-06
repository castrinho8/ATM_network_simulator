package practicaacs.fap;

public class RespReintegroError extends RespDatosError {

	private static final long serialVersionUID = 8917570265207751301L;

	public RespReintegroError(String origen, String destino,int numcanal, int nmsg,
			boolean codonline, CodigosError codigoError) {
		super(origen, destino, CodigosMensajes.RESREINTEGRO, numcanal, nmsg, codonline,
				codigoError);
	}

	public RespReintegroError() {}

}
