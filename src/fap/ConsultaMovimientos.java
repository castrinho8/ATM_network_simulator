package fap;

public class ConsultaMovimientos extends MensajeDatos {

	private String num_tarjeta;
	private int num_cuenta;
	
	public ConsultaMovimientos(String origen, String destino,
			CodigosMensajes tipoMensaje, int numcanal, int nmsg,
			boolean codonline, String num_tarjeta, int num_cuenta) {
		super(origen, destino, tipoMensaje, numcanal, nmsg, codonline);
		this.num_tarjeta = num_tarjeta;
		this.num_cuenta = num_cuenta;
	}
	
	
	
}
