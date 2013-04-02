package practicaacs.fap;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RespConsMovimientos extends MensajeRespDatos {
	private int nmovimientos;
	private TiposMovimiento tipo_mov;
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
	public RespConsMovimientos(String origen, String destino,
			int numcanal, int nmsg, boolean codonline, CodigosRespuesta cod_resp,
			int nmovimientos, TiposMovimiento tipo_mov, boolean signo, int importe,
			Date fecha) {
		super(origen, destino, CodigosMensajes.RESCONSULTAMOV, numcanal, nmsg, codonline, cod_resp);
		
		assert(nmovimientos <= 20);
		
		this.nmovimientos = nmovimientos;
		this.tipo_mov = tipo_mov;
		this.signo = signo;
		this.importe = importe;
		this.fecha = fecha;
	}

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
}
