package practicaacs.fap;

public class SolIniTraficoRec extends Mensaje {

	private static final long serialVersionUID = -2305078832641352933L;

	/**
	 * Constructor del mensaje Solicitud de Tráfico en Recuperación.
	 * @param origen Origen del mensaje.
	 * @param destino Destino del mensaje.
	 */
	public SolIniTraficoRec(String origen, String destino) {
		super(origen, destino,CodigosMensajes.SOLINIREC);
	}

	@Override
	protected void parseComp(String bs) throws MensajeNoValidoException {
		this.parseComp(bs); 
	}
}
