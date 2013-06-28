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
	
	public SolAperturaSesion(){}
	
	public int getNcanales() {
		return ncanales;
	}

	public String getIpPuerto(){
		return puerto;
	}
	
	public String getIp(){
		try{
			String[] port = this.puerto.split("/");
			return port[0];
		}catch(IndexOutOfBoundsException e){
			e.printStackTrace();
			System.exit(-1);
		}
		return null;
	}
	
	public int getPuerto() {
		try{
			String[] port = this.puerto.split("/");
			return Integer.parseInt(port[1]);
		}catch(IndexOutOfBoundsException | NumberFormatException e){
			e.printStackTrace();
			System.exit(-1);
		}
		return 0;
	}
	
	public Date getTime() {
		return time;
	}

	@Override
	protected String printCuerpo(){
		return String.format("%02d%10s%8s%20s",
				this.ncanales, 
				new SimpleDateFormat("dd/MM/yyyy").format(this.time),
				new SimpleDateFormat("hh:mm:ss").format(this.time),
				puerto);
	}

	@Override
	protected void parseComp(String bs) throws MensajeNoValidoException {
		super.parseComp(bs);
		
		if(bs.length() != 58)
			throw new MensajeNoValidoException("Lonxitude (" + bs.length() + ") non valida (SolAperturaSesion)");
		
		try{
			this.ncanales = new Integer(bs.substring(18, 20));
		}catch (NumberFormatException e){
			throw new MensajeNoValidoException("Formato do numero de mensaxes non valido. (SolAperturaSesion)");
		}

		try {
			this.time = new SimpleDateFormat("dd/MM/yyyyhh:mm:ss").parse(bs.substring(20, 38));
		} catch (ParseException e) {
			throw new MensajeNoValidoException("Formato da data non valido (SolAperturaSesion)");
		}

		this.puerto = bs.toString().substring(38,58);
	}
}
