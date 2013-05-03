package practicaacs.fap;

public abstract class RespDatosError extends MensajeRespDatos {

	private static final long serialVersionUID = -2043917782582614162L;
	private CodigosError codigoError;

	public RespDatosError(String origen, String destino,
			CodigosMensajes tipoMensaje, int numcanal, int nmsg,
			boolean codonline, CodigosError codigoError) {
		super(origen, destino, tipoMensaje, numcanal, nmsg, codonline, CodigosRespuesta.CONSDEN);
		this.codigoError = codigoError;
	}

	public RespDatosError() {}

	
	public CodigosError getCodigoError() {
		return codigoError;
	}

	@Override
	protected void parseComp(String bs) throws MensajeNoValidoException {
		super.parseComp(bs);
		
		if(bs.length() < 30)
			throw new MensajeNoValidoException("Lonxitude (" + bs.length() + ") non válida (RespDatosError)");
		
		try {
			this.codigoError = CodigosError.parse(bs.toString().substring(28, 30));
		} catch (CodigoNoValidoException e) {
			throw new MensajeNoValidoException("Formato de codigo non válido (RespDatosError)");
		}
		
	}

	@Override
	protected String printCuerpo() {
		return this.codigoError.getCodigo();
	}

	
	
	
}
