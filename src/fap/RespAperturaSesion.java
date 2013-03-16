/**
 * 
 */
package fap;

import java.util.Date;

/**
 * @author ch01
 *
 */
public class RespAperturaSesion extends Mensaje {

	
	private int cod_resp;
	private int cod_error;
	

	public RespAperturaSesion(String origen, String destino, int cod_resp,
			int cod_error) {
		super(origen, destino,CodigosMensajes.RESREINTEGRO);
		this.cod_resp = cod_resp;
		this.cod_error = cod_error;
	}

	@Override
	protected String printCuerpo(){
		return "";
		
	}
}
