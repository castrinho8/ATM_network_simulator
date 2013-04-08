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

	@Override
	protected String printCabecera() {
		return super.printCabecera() + "";
	}

	@Override
	protected void parseComp(byte[] bs) throws MensajeNoValidoException{
		super.parseComp(bs);
		try {
			this.cod_resp = CodigosRespuesta.parse(bs.toString().substring(26, 27));
		} catch (CodigoNoValidoException e) {
			throw new MensajeNoValidoException();
		}
	}

	public CodigosRespuesta getCod_resp() {
		return cod_resp;
	}
}
