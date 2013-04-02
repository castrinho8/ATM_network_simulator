package practicaacs.fap;

public class SolReanTrafico extends Mensaje {
	
	/**
	 * Constructor del mensaje Solicitude para Reanudar el Tráfico.
	 * @param origen Origen del Mensaje.
	 * @param destino Destino del Mensaje.
	 */
	public SolReanTrafico(String origen, String destino) {
		super(origen, destino,CodigosMensajes.SOLREANUDARTRAFICO);
	}
}
