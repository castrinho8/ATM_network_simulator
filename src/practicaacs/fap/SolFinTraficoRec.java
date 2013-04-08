package practicaacs.fap;

public class SolFinTraficoRec extends Mensaje {

	private static final long serialVersionUID = -302876743043290885L;

	/**
	 * Constructor del mensaje Solicitud Fin de Tráfico en Recuperación.
	 * @param origen Origen del Mensaje
	 * @param destino Destino del Mensaje
	 */
	public SolFinTraficoRec(String origen, String destino) {
		super(origen, destino,CodigosMensajes.SOLINIREC);
	}

	@Override
	protected void parseComp(byte[] bs) throws MensajeNoValidoException {
		this.parseComp(bs);
	}

}
