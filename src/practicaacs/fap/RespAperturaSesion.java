/**
 * 
 */
package practicaacs.fap;

/**
 * @author ch01
 *
 */
public class RespAperturaSesion extends Mensaje {


	private static final long serialVersionUID = -1076294313735245375L;
	private boolean cod_resp;
	private CodigosError cod_error;
		
	/**
	 * Contructor del mensaje Respuesta Apertura Sesión.
	 * @param origen
	 * @param destino
	 * @param cod_resp
	 * @param cod_error
	 */
	public RespAperturaSesion(String origen, String destino, boolean cod_resp,
			CodigosError cod_error) {
		super(origen, destino,CodigosMensajes.RESREINTEGRO);
		
		this.cod_resp = cod_resp;
		this.cod_error = cod_error;
	}
	
	public RespAperturaSesion(){}
	
	@Override
	protected String printCuerpo(){
		return String.format("%2s%s", cod_resp ? "11" : "00",this.cod_error);
	}

	@Override
	protected void parseComp(String bs) throws MensajeNoValidoException {
		super.parseComp(bs);
		
		try {
			if(bs.length() != 22)
				throw new MensajeNoValidoException("Error na lonxitude (" + bs.length() + ") (RespAperturaSesion)");

			if(bs.toString().substring(18, 20).equals("11") || bs.toString().substring(18, 20).equals("00")){
				this.cod_resp = bs.toString().substring(18, 20).equals("00");
				this.cod_error = CodigosError.parse((bs.toString().substring(20, 22)));
				return;
			}else{
				throw new MensajeNoValidoException("Error no codResp (RespAperturaSesion)");
			}
		} catch (CodigoNoValidoException e) {
			throw new MensajeNoValidoException("Error no codError (RespAperturaSesion)");
		}
	}

	public boolean getCodResp() {
		return cod_resp;
	}

	public CodigosError getCodError() {
		return cod_error;
	}

	
	
}
