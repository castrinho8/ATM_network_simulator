package fap;

public class RespFinTraficoRec extends Mensaje {

	private int cod_resp;
	private int cod_error;
	
	
	public RespFinTraficoRec(String origen, String destino, int cod_resp,
			int cod_error) {
		super(origen, destino,CodigosMensajes.FINREC);
		this.cod_resp = cod_resp;
		this.cod_error = cod_error;
	}

}
