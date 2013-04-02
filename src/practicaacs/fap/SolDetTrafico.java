package practicaacs.fap;

public class SolDetTrafico extends Mensaje {

	/**
	 * Constructor del mensaje Solicitud para Detener el Tráfico.
	 * @param origen Origen del Mensaje
	 * @param destino Destino del Mensaje
	 */
	public SolDetTrafico(String origen, String destino) {
		super(origen, destino,CodigosMensajes.SOLDETENERTRAFICO);
	}

}
