/**
 * 
 */
package practicaacs.fap;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class SolAperturaSesion extends Mensaje {

	private static final long serialVersionUID = 5685993518176437136L;
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
	
	public int getNcanales() {
		return ncanales;
	}

	public String getPuerto() {
		return puerto;
	}
	
	public Date getTime() {
		return time;
	}

	@Override
	protected String printCuerpo(){
		return String.format("%2s%10s%8s%20s",
				this.ncanales, 
				new SimpleDateFormat("dd/MM/yyyy").format(this.time),
				new SimpleDateFormat("hh:mm:ss").format(this.time),
				puerto);
	}

	@Override
	protected void parseComp(byte[] bs) throws MensajeNoValidoException {
		super.parseComp(bs);
		try {
			if(bs.toString().length() == 59){
				this.ncanales = new Integer(bs.toString().substring(19, 20));
				this.time = new SimpleDateFormat("dd/MM/yyyyhh:mm:ss").parse(bs.toString().substring(21, 38));
				this.puerto = bs.toString().substring(39,58);
				return;
			}
		} catch (ParseException e) {}
		throw new MensajeNoValidoException();
	}
}
