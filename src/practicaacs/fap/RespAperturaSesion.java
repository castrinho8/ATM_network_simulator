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

	@Override
	protected String printCuerpo(){
		return String.format("%2i%s", this.cod_resp,this.cod_error);
	}

	@Override
	protected void parseComp(byte[] bs) throws MensajeNoValidoException {
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
}
