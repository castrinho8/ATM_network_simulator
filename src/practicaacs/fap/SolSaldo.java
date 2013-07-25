package practicaacs.fap;

public class SolSaldo extends MensajeDatos {

	private static final long serialVersionUID = 8506678630346699119L;
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
	public SolSaldo(String origen, String destino, int numcanal, int nmsg,
			boolean codonline, String num_tarjeta, int num_cuenta) {
		
		super(origen, destino, CodigosMensajes.SOLSALDO, numcanal, nmsg, codonline);

		assert(num_tarjeta.length() <= 11);
		assert(num_cuenta >= 0);
		assert(num_cuenta <=9);
		
		this.num_tarjeta = num_tarjeta;
		this.num_cuenta = num_cuenta;
	}
	
	public SolSaldo(){}
	
	public String getNum_tarjeta() {
		return num_tarjeta;
	}

	public int getNum_cuenta() {
		return num_cuenta;
	}

	@Override
	protected String printCuerpo() {
		return String.format("%11s%1d", this.num_tarjeta,this.num_cuenta);
	}

	@Override
	protected void parseComp(String bs) throws MensajeNoValidoException {
		
		super.parseComp(bs);

		if(bs.length() != 38)
			throw new MensajeNoValidoException("Lonxitude (" + bs.length() + ") non válida. (SolSaldo)");
		
		this.num_tarjeta = bs.substring(26,37).trim();
		try {
			this.num_cuenta = new Integer(bs.substring(37,38));
		} catch(NumberFormatException e){
			throw new MensajeNoValidoException("Error de formato dos numeros.");
		}	
	}
	
	protected String formatearMensaje(){
		return super.formatearMensaje()+"---- Tarjeta: "+this.num_tarjeta+"("+this.num_cuenta+")";
	}
}
