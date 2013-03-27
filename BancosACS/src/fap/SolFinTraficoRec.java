package fap;

public class SolFinTraficoRec extends Mensaje {

	/**
	 * Constructor del mensaje Solicitud Fin de Tráfico en Recuperación.
	 * @param origen Origen del Mensaje
	 * @param destino Destino del Mensaje
	 */
	public SolFinTraficoRec(String origen, String destino) {
		super(origen, destino,CodigosMensajes.SOLFINTRADICOREC);
	}

}
