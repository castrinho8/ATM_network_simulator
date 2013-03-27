/**
 * 
 */
package fap;

import java.text.SimpleDateFormat;
import java.util.Date;


public class SolAperturaSesion extends Mensaje {

	private int ncanales;
	private Date time;
	private String puerto;
	
	/**
	 * Contructor para el mensaje de Solicitud de Apertura de Sesión.
	 * @param origen Origen del mensaje.
	 * @param destino Destino del mensaje.
	 * @param ncanales Numero de canales de la sesion.
	 * @param time Fecha y hora de la apertura de sesión.
	 * @param puerto IP y puerto donde el banco espera recibir las peticiones.
	 */
	public SolAperturaSesion(String origen, String destino, int ncanales,
			Date time, String puerto) {
		super(origen, destino,CodigosMensajes.SOLABRIRSESION);
		
		assert(ncanales > 0);
		assert(ncanales < 100);
		assert(puerto.length() <= 20);
		
		this.ncanales = ncanales;
		this.time = time;
		this.puerto = puerto;
	}

	@Override
	protected String printCuerpo(){
		return String.format("%2i%10s%8s%20s",
				this.ncanales, 
				new SimpleDateFormat("dd/MM/yy").format(this.time),
				new SimpleDateFormat("hh:mm:ss").format(this.time),
				puerto);
	}
}
