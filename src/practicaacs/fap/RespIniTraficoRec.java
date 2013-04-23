package practicaacs.fap;

public class RespIniTraficoRec extends Mensaje {

	private static final long serialVersionUID = -8489515003977583959L;
	private boolean cod_resp;
	private CodigosError cod_error;
	
	/**
	 * Constructor de mensaje Respuesta Inicio Trafico Recuperación.
	 * @param origen Origen del mensaje.
	 * @param destino Destino del mensaje.
	 * @param cod_resp Codigo de Respuesta.
	 * @param cod_error Codigo de error.
	 */
	public RespIniTraficoRec(String origen, String destino,
			boolean cod_resp, CodigosError cod_error) {
		super(origen, destino,CodigosMensajes.RESINIREC);
		this.cod_resp = cod_resp;
		this.cod_error = cod_error;
	}
	
	@Override
	protected String printCuerpo(){
		return String.format("%2i%2s", this.cod_resp,this.cod_error);
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
