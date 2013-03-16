package fap;

public class RespAbono extends MensajeRespDatos {

	private boolean signo;
	private int saldo;
	
	public RespAbono(String origen, String destino,
			CodigosMensajes tipoMensaje, int numcanal, int nmsg,
			boolean codonline, CodigosRespuesta cod_resp, boolean signo,
			int saldo) {
		super(origen, destino, tipoMensaje, numcanal, nmsg, codonline, cod_resp);
		this.signo = signo;
		this.saldo = saldo;
	}

	
}
