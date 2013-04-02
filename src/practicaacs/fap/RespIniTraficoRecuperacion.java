package practicaacs.fap;

public class RespIniTraficoRecuperacion extends Mensaje {

	private int cod_resp;
	private int cod_error;
	
	/**
	 * Constructor de mensaje Respuesta Inicio Trafico Recuperación.
	 * @param origen Origen del mensaje.
	 * @param destino Destino del mensaje.
	 * @param cod_resp Codigo de Respuesta.
	 * @param cod_error Codigo de error.
	 */
	public RespIniTraficoRecuperacion(String origen, String destino,
			int cod_resp, int cod_error) {
		super(origen, destino,CodigosMensajes.TRAFICOREC);
		this.cod_resp = cod_resp;
		this.cod_error = cod_error;
	}
	
	@Override
	protected String printCuerpo(){
		return String.format("%2i%s", this.cod_resp,this.cod_error);
	}
}
