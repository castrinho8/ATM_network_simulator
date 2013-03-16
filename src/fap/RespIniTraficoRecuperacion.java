package fap;

public class RespIniTraficoRecuperacion extends Mensaje {

	private int cod_resp;
	private int cod_error;
	
	
	public RespIniTraficoRecuperacion(String origen, String destino,
			int cod_resp, int cod_error) {
		super(origen, destino,CodigosMensajes.TRAFICOREC);
		this.cod_resp = cod_resp;
		this.cod_error = cod_error;
	}
}
