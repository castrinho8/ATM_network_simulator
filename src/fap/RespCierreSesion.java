package fap;

public class RespCierreSesion extends Mensaje {

	private int cod_resp;
	private int cod_error;
	

	public RespCierreSesion(String origen, String destino, int cod_resp,
			int cod_error) {
		super(origen, destino,CodigosMensajes.CIERRESESION);
		this.cod_resp = cod_resp;
		this.cod_error = cod_error;
	}

	@Override
	protected String printCuerpo() {
		return super.printCuerpo();
	}

}
