package practicaacs.fap;

public class SolIniTraficoRecuperacion extends Mensaje {

	/**
	 * Constructor del mensaje Solicitud de Tráfico en Recuperación.
	 * @param origen Origen del mensaje.
	 * @param destino Destino del mensaje.
	 */
	public SolIniTraficoRecuperacion(String origen, String destino) {
		super(origen, destino,CodigosMensajes.SOLTRAFICOREC);
	}
}
