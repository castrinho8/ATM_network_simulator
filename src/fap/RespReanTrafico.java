package fap;

public class RespReanTrafico extends Mensaje {

	private int cod_resp;
	private int cod_error;
	
	public RespReanTrafico(String origen, String destino, int cod_resp,
			int cod_error) {
		super(origen, destino,CodigosMensajes.REANUDARTRAFICO);
		this.cod_resp = cod_resp;
		this.cod_error = cod_error;
	}
}
