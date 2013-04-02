package practicaacs.fap;

public class RespDetTrafico extends Mensaje {

	private int cod_resp;
	private int cod_error;
	
	/**
	 * Contructor del mensaje Respuesta Detener Trafico.
	 * @param origen Origen del mensaje
	 * @param destino Destino del mensaje
	 * @param cod_resp Código de Respuesta
	 * @param cod_error Código de error.
	 */
	public RespDetTrafico(String origen, String destino, int cod_resp,
			int cod_error) {
		super(origen, destino,CodigosMensajes.DETENERTRAFICO);
		this.cod_resp = cod_resp;
		this.cod_error = cod_error;
	}
	
	@Override
	protected String printCuerpo(){
		return String.format("%2i%s", this.cod_resp,this.cod_error);
	}
}
