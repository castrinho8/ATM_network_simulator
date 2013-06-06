package practicaacs.fap;

public class SolAbono extends MensajeDatos {

	private static final long serialVersionUID = -8665913656682341520L;
	private String num_tarjeta;
	private int num_cuenta;
	private int importe;
	
	
	public SolAbono(String origen, String destino, int numcanal, int nmsg,
			boolean codonline, String num_tarjeta, int num_cuenta, int importe) {
		
		super(origen, destino, CodigosMensajes.SOLABONO, numcanal, nmsg, codonline);
		
		this.num_tarjeta = num_tarjeta;
		this.num_cuenta = num_cuenta;
		this.importe = importe;
	}
	
	public SolAbono(){}


	@Override
	protected String printCuerpo() {
		return String.format("%11s%1d%04d", num_tarjeta, this.num_cuenta, importe);
	}

	public String getNum_tarjeta() {
		return num_tarjeta;
	}


	public int getNum_cuenta() {
		return num_cuenta;
	}


	public int getImporte() {
		return importe;
	}


	@Override
	protected void parseComp(String bs) throws MensajeNoValidoException {
		super.parseComp(bs);
		
		System.err.println(bs);
		
		if(bs.length() != 42)
			throw new MensajeNoValidoException("Lonxitude (" + bs.length() + ") non válida. (SolAbono)");
		
		this.num_tarjeta = bs.substring(26,37).trim();
		try {
			this.num_cuenta = new Integer(bs.substring(37,38));
			this.importe = new Integer(bs.substring(38, 42));
		} catch(NumberFormatException e){
			throw new MensajeNoValidoException("Error de formato dos numeros.");
		}
	}
	
	
	
}
