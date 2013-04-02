package practicaacs.fap;

public class RespFinTraficoRec extends Mensaje {

	private int cod_resp;
	private int cod_error;
	
	/**
	 * Constructor de Mensaje Respuesta Fin de Tráfico de Recuperación.
	 * @param origen Origen del Mensaje
	 * @param destino Destino del Mensaje
	 * @param cod_resp Codigo de Respuesta
	 * @param cod_error Código de Error
	 */
	public RespFinTraficoRec(String origen, String destino, int cod_resp,
			int cod_error) {
		super(origen, destino,CodigosMensajes.FINREC);
		this.cod_resp = cod_resp;
		this.cod_error = cod_error;
	}
	
	@Override
	protected String printCuerpo(){
		return String.format("%2i%s", this.cod_resp,this.cod_error);
	}

}
