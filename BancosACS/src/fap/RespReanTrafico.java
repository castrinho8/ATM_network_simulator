package fap;

public class RespReanTrafico extends Mensaje {

	private int cod_resp;
	private int cod_error;
	
	
	/**
	 * Constructor de mensaje Respuesta de Reanudar Tráfico.
	 * @param origen Origen de mensaje
	 * @param destino Destino de mensaje
	 * @param cod_resp Código de Respuesa
	 * @param cod_error Codigo de Error
	 */
	public RespReanTrafico(String origen, String destino, int cod_resp,
			int cod_error) {
		super(origen, destino,CodigosMensajes.REANUDARTRAFICO);
		this.cod_resp = cod_resp;
		this.cod_error = cod_error;
	}
	
	@Override
	protected String printCuerpo(){
		return String.format("%2i%s", this.cod_resp,this.cod_error);
	}
}
