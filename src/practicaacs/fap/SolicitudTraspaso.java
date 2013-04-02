package practicaacs.fap;

public class SolicitudTraspaso extends MensajeDatos {

	private String num_tarjeta;
	private int num_cuenta_origen;
	private int num_cuenta_destino;
	private int importe;
	
	
	public SolicitudTraspaso(String origen, String destino,
			CodigosMensajes tipoMensaje, int numcanal, int nmsg,
			boolean codonline, String num_tarjeta, int num_cuenta_origen,
			int num_cuenta_destino, int importe) {
		super(origen, destino, tipoMensaje, numcanal, nmsg, codonline);
		this.num_tarjeta = num_tarjeta;
		this.num_cuenta_origen = num_cuenta_origen;
		this.num_cuenta_destino = num_cuenta_destino;
		this.importe = importe;
	}
}
