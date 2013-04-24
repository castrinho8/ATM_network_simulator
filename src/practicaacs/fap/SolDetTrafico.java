package practicaacs.fap;

public class SolDetTrafico extends Mensaje {

	private static final long serialVersionUID = 4577464211579542772L;

	/**
	 * Constructor del mensaje Solicitud para Detener el Tráfico.
	 * @param origen Origen del Mensaje
	 * @param destino Destino del Mensaje
	 */
	public SolDetTrafico(String origen, String destino) {
		super(origen, destino,CodigosMensajes.SOLDETENERTRAFICO);
	}
	
	public SolDetTrafico(){}

	@Override
	protected void parseComp(String bs) throws MensajeNoValidoException {
		super.parseComp(bs);
	}

}
