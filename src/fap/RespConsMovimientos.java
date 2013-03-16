package fap;

import java.util.Date;

public class RespConsMovimientos extends MensajeRespDatos {
	private int nmovimientos;
	private TiposMovimiento tipo_mov;
	private boolean signo;
	private int importe;
	private Date fecha;
	
	public RespConsMovimientos(String origen, String destino,
			CodigosMensajes tipoMensaje, int numcanal, int nmsg,
			boolean codonline, CodigosRespuesta cod_resp, int nmovimientos,
			TiposMovimiento tipo_mov, boolean signo, int importe, Date fecha) {
		super(origen, destino, tipoMensaje, numcanal, nmsg, codonline, cod_resp);
		this.nmovimientos = nmovimientos;
		this.tipo_mov = tipo_mov;
		this.signo = signo;
		this.importe = importe;
		this.fecha = fecha;
	}
	
	
	
}
