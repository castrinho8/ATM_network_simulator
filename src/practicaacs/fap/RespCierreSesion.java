package practicaacs.fap;

public class RespCierreSesion extends Mensaje {

	private int cod_resp;
	private CodigosError cod_error;
	

	public RespCierreSesion(String origen, String destino, int cod_resp,
			CodigosError cod_error) {
		super(origen, destino,CodigosMensajes.CIERRESESION);
		this.cod_resp = cod_resp;
		this.cod_error = cod_error;
	}

	@Override
	protected String printCuerpo(){
		return String.format("%2i%s", this.cod_resp,this.cod_error);
	}

}
