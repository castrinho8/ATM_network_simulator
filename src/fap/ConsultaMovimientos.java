package fap;

public class ConsultaMovimientos extends MensajeDatos {

	private String num_tarjeta;
	private int num_cuenta;
	
	public ConsultaMovimientos(String origen, String destino,
			CodigosMensajes tipoMensaje, int numcanal, int nmsg,
			boolean codonline, String num_tarjeta, int num_cuenta) {
		super(origen, destino, tipoMensaje, numcanal, nmsg, codonline);
		
		assert(num_tarjeta.length() <= 11);
		assert(num_cuenta >= 0);
		assert(num_cuenta <=9);
		
		this.num_tarjeta = num_tarjeta;
		this.num_cuenta = num_cuenta;
	}

	@Override
	protected String printCuerpo() {
		return String.format("%11s%1i", this.num_cuenta,this.num_tarjeta);
	}
}
