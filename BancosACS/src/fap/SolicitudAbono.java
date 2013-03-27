package fap;

public class SolicitudAbono extends MensajeDatos {

	private String num_tarjeta;
	private int num_cuenta;
	private int importe;
	
	
	public SolicitudAbono(String origen, String destino,
			CodigosMensajes tipoMensaje, int numcanal, int nmsg,
			boolean codonline, String num_tarjeta, int num_cuenta, int importe) {
		super(origen, destino, tipoMensaje, numcanal, nmsg, codonline);
		this.num_tarjeta = num_tarjeta;
		this.num_cuenta = num_cuenta;
		this.importe = importe;
	}
	
}
