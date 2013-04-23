package practicaacs.fap;

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
	
	protected RespMovimientos(){}

	@Override
	protected String printCuerpo() {
		return String.format("%2i%s%s%8i%s%s", 
				nmovimientos,
				tipo_mov,
				signo?"+":"-",
				importe,
				new SimpleDateFormat("dd/MM/yy").format(this.fecha),
				new SimpleDateFormat("hh:mm:ss").format(this.fecha));
	}

	@Override
	protected void parseComp(String bs) throws MensajeNoValidoException {
		// TODO Auto-generated method stub
		return;
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
	
	
}
