package practicaacs.fap;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RespMovimientos extends MensajeRespDatos {
	
	private static final long serialVersionUID = -6409226798666821301L;
	private int nmovimientos;
	private CodigosMovimiento tipo_mov;
	private boolean signo;
	private int importe;
	private Date fecha;
	
	/**
	 * Contructor mensaje Respuesta Consulta Movimientos
	 * @param origen Origen del Mensaje
	 * @param destino Destino del Mensaje.
	 * @param numcanal Numero del canal.
	 * @param nmsg Numero de Mensaje.
	 * @param codonline Codigo ONLINE.
	 * @param cod_resp Codigo Respuesta
	 * @param nmovimientos Numero Movimientos
	 * @param tipo_mov Tipo de Movimientos.
	 * @param signo Signo del importe.
	 * @param importe Importe
	 * @param fecha Fecha de la operación.
	 */
	public RespMovimientos(String origen, String destino,
			int numcanal, int nmsg, boolean codonline, CodigosRespuesta cod_resp,
			int nmovimientos, CodigosMovimiento tipo_mov, boolean signo, int importe,
			Date fecha) {
		super(origen, destino, CodigosMensajes.RESMOVIMIENTOS, numcanal, nmsg, codonline, cod_resp);
		
		assert(nmovimientos <= 20);
		
		this.nmovimientos = nmovimientos;
		this.tipo_mov = tipo_mov;
		this.signo = signo;
		this.importe = importe;
		this.fecha = fecha;
	}
	
	public RespMovimientos(){}

	@Override
	protected String printCuerpo() {
		return String.format("%02d%s%s%08d%s", 
				nmovimientos,
				tipo_mov.getNum(),
				signo?"+":"-",
				importe,
				new SimpleDateFormat("dd/MM/yyhh:mm:ss").format(this.fecha));
	}

	@Override
	protected void parseComp(String bs) throws MensajeNoValidoException {
		super.parseComp(bs);
		
		if(bs.length() != 57)
			throw new MensajeNoValidoException("Lonxitude (" + bs.length() + ") non válida (RespMovimiento)");
		
		try{
			this.nmovimientos = new Integer(bs.substring(28, 30));
			if (nmovimientos > 20)
				throw new MensajeNoValidoException("Formato de numero de mensaxes (" + bs.substring(28, 30) +  ") non valido (RespMovimiento)");
		}catch(NumberFormatException e){
			throw new MensajeNoValidoException("Formato de numero de mensaxes (" + bs.substring(28, 30) +  ") non valido (RespMovimiento)");
		}
		
		try {
			this.tipo_mov = CodigosMovimiento.getTipoMovimiento(new Integer(bs.substring(30, 32)));
		} catch (NumberFormatException | CodigoNoValidoException e) {
			throw new MensajeNoValidoException("Formato de codigo de movimento non valido (RespMovimiento)");
		}
		
		if(bs.substring(32, 33).equals("+") || bs.substring(32, 33).equals("-")){
			this.signo = bs.substring(32, 33).equals("+");
		}else{
			throw new MensajeNoValidoException("Formato de signo non valido (RespMovimiento)");
		}
		
		try{
			this.importe = new Integer(bs.substring(33, 41));
		}catch (NumberFormatException e){
			throw new MensajeNoValidoException("Formato de importe non valido (RespMovimiento)");
		}
		
		try {
			this.fecha = new SimpleDateFormat("dd/MM/yyhh:mm:ss").parse(bs.substring(41, 57));
		} catch (ParseException e) {
			throw new MensajeNoValidoException("Formato de data non valido (RespMovimiento)");
		}
		
	}

	public int getNmovimientos() {
		return nmovimientos;
	}

	public CodigosMovimiento getTipoMov() {
		return tipo_mov;
	}

	public boolean getSigno() {
		return signo;
	}

	public int getImporte() {
		return importe;
	}

	public Date getFecha() {
		return fecha;
	}
	
	protected String formatearMensaje(){
		return super.formatearMensaje()+"---- Movimiento: "+this.tipo_mov+" "+(this.signo?"+":"-")+this.importe+"€ / "+this.fecha;
	}
	
}
