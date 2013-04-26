package practicaacs.fap;

public abstract class MensajeRespDatos extends MensajeDatos {

	
	private static final long serialVersionUID = 3528635786217406704L;
	private CodigosRespuesta cod_resp;
	
	public MensajeRespDatos(String origen, String destino,
			CodigosMensajes tipoMensaje, int numcanal, int nmsg,
			boolean codonline, CodigosRespuesta cod_resp) {
		super(origen, destino, tipoMensaje, numcanal, nmsg, codonline);
		this.cod_resp = cod_resp;
	}

	public MensajeRespDatos() {}

	@Override
	protected String printCabecera() {
		return super.printCabecera() + cod_resp.getCodigo();
	}

	@Override
	protected void parseComp(String bs) throws MensajeNoValidoException{
		super.parseComp(bs);
		
		if(bs.length() < 28)
			throw new MensajeNoValidoException("Lonxitude (" + bs.length() + ") non válida (MensajeRespDatos)");
		
		try {
			this.cod_resp = CodigosRespuesta.parse(bs.toString().substring(26, 28));
		} catch (CodigoNoValidoException e) {
			throw new MensajeNoValidoException("Formato de codigo non válido (MensajeRespDatos)");
		}
	}

	public CodigosRespuesta getCod_resp() {
		return cod_resp;
	}
}
