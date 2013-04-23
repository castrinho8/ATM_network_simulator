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
	
	@Override
	protected String printCuerpo(){
		return String.format("%2i%s", this.cod_resp,this.cod_error);
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
