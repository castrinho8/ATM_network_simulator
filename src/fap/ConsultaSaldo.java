package fap;

public class ConsultaSaldo extends MensajeDatos {

	private String num_tarjeta;
	private int num_cuenta;
	
	/**
	 * Constructor del mensaje Consultar Saldo.
	 * @param origen Origen del mensaje
	 * @param destino Destino del mensaje.
	 * @param numcanal Numero de canal.
	 * @param nmsg Numero de mensaje
	 * @param codonline Codigo ONLINE.
	 * @param num_tarjeta Numero de Tarjeta.
	 * @param num_cuenta Numero de Cuenta.
	 */
	public ConsultaSaldo(String origen, String destino, int numcanal, int nmsg,
			boolean codonline, String num_tarjeta, int num_cuenta) {
		
		super(origen, destino, CodigosMensajes.CONSULTARSALDO, numcanal, nmsg, codonline);
		
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
