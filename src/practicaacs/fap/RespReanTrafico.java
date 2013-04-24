package practicaacs.fap;

public class RespReanTrafico extends Mensaje {

	private static final long serialVersionUID = 8816475327853896857L;
	private boolean cod_resp;
	private CodigosError cod_error;
	
	
	/**
	 * Constructor de mensaje Respuesta de Reanudar Tráfico.
	 * @param origen Origen de mensaje
	 * @param destino Destino de mensaje
	 * @param cod_resp Código de Respuesa
	 * @param cod_error Codigo de Error
	 */
	public RespReanTrafico(String origen, String destino, boolean cod_resp,
			CodigosError cod_error) {
		super(origen, destino,CodigosMensajes.RESREANUDARTRAFICO);
		this.cod_resp = cod_resp;
		this.cod_error = cod_error;
	}
	
	public RespReanTrafico(){}
	
	@Override
	protected String printCuerpo(){
		return String.format("%2s%s", this.cod_resp ? "00" : "11",this.cod_error);
	}

	@Override
	protected void parseComp(String bs) throws MensajeNoValidoException {
		super.parseComp(bs);
		
		try {
			if(bs.toString().length() == 23 && 
					(bs.toString().substring(21, 22).equals("11") || bs.toString().substring(21, 22).equals("00"))){
				this.cod_error = CodigosError.parse((bs.toString().substring(19, 20)));
				this.cod_resp = bs.toString().substring(21, 22).equals("11");
				return;
			}
		} catch (CodigoNoValidoException e) {}
		throw new MensajeNoValidoException();
	}

	public boolean getCodResp() {
		return cod_resp;
	}

	public CodigosError getCodError() {
		return cod_error;
	}
	
	
	

}
