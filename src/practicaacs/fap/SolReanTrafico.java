package practicaacs.fap;

public class SolReanTrafico extends Mensaje {
	
	private static final long serialVersionUID = -7077713190257835648L;

	/**
	 * Constructor del mensaje Solicitude para Reanudar el Tráfico.
	 * @param origen Origen del Mensaje.
	 * @param destino Destino del Mensaje.
	 */
	public SolReanTrafico(String origen, String destino) {
		super(origen, destino,CodigosMensajes.SOLREANUDARTRAFICO);
	}

	@Override
	protected void parseComp(String bs) throws MensajeNoValidoException {
		this.parseComp(bs);
	}
}
