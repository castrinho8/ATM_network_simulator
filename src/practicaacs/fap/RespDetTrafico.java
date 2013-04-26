package practicaacs.fap;

public class RespDetTrafico extends Mensaje {

	private static final long serialVersionUID = -7090099935674923033L;
	private boolean cod_resp;
	private CodigosError cod_error;
	
	/**
	 * Contructor del mensaje Respuesta Detener Trafico.
	 * @param origen Origen del mensaje
	 * @param destino Destino del mensaje
	 * @param cod_resp Código de Respuesta
	 * @param cod_error Código de error.
	 */
	public RespDetTrafico(String origen, String destino, boolean cod_resp, CodigosError cod_error) {
		super(origen, destino,CodigosMensajes.RESDETENERTRAFICO);
		this.cod_resp = cod_resp;
		this.cod_error = cod_error;
	}
	
	public RespDetTrafico(){}
	
	@Override
	protected String printCuerpo(){
		return String.format("%2s%s", this.cod_resp ? "00" : "11",this.cod_error.getCodigo());
	}

	@Override
	protected void parseComp(String bs) throws MensajeNoValidoException {
		super.parseComp(bs);
		
		if(bs.length() != 22)
			throw new MensajeNoValidoException("Lonxitude (" + bs.length() + ") non válida (RespDetTrafico)");
		
		if (bs.toString().substring(18, 20).equals("11") || bs.toString().substring(18, 20).equals("00"))
			this.cod_resp = bs.toString().substring(18, 20).equals("00");
		else
			throw new MensajeNoValidoException("Formato codigo resposta non válido (RespIniTrafico)");

		try{
			this.cod_error = CodigosError.parse((bs.toString().substring(20, 22)));
		} catch (CodigoNoValidoException e) {
			throw new MensajeNoValidoException("Codigo de error non válido (RespIniTrafico)");
		}	
	}

	public boolean getCodResp() {
		return cod_resp;
	}

	public CodigosError getCodError() {
		return cod_error;
	}
	
	
	

}
