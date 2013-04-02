/**
 * 
 */
package practicaacs.fap;

/**
 * @author ch01
 *
 */
public class RespAperturaSesion extends Mensaje {

	
	private int cod_resp;
	private CodigosError cod_error;
	
	/**
	 * Contructor del mensaje Respuesta Apertura Sesión.
	 * @param origen
	 * @param destino
	 * @param cod_resp
	 * @param cod_error
	 */
	public RespAperturaSesion(String origen, String destino, int cod_resp,
			CodigosError cod_error) {
		super(origen, destino,CodigosMensajes.RESREINTEGRO);
		
		assert(cod_resp == 00 || cod_resp == 11);
		
		this.cod_resp = cod_resp;
		this.cod_error = cod_error;
	}

	@Override
	protected String printCuerpo(){
		return String.format("%2i%s", this.cod_resp,this.cod_error);
	}
}
