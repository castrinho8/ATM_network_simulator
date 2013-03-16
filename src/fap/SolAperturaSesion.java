/**
 * 
 */
package fap;

import java.util.Date;

/**
 * @author ch01
 *
 */
public class SolAperturaSesion extends Mensaje {

	
	private int ncanales;
	private Date time;
	private String puerto;
	
	public SolAperturaSesion(String origen, String destino, int ncanales,
			Date time, String puerto) {
		super(origen, destino,CodigosMensajes.SOLABRIRSESION);
		this.ncanales = ncanales;
		this.time = time;
		this.puerto = puerto;
	}

	@Override
	protected String printCuerpo(){
		return "";
		
	}
}
