package fap;

public class RespDetTrafico extends Mensaje {

	private int cod_resp;
	private int cod_error;
	
	
	public RespDetTrafico(String origen, String destino, int cod_resp,
			int cod_error) {
		super(origen, destino,CodigosMensajes.DETENERTRAFICO);
		this.cod_resp = cod_resp;
		this.cod_error = cod_error;
	}

}
