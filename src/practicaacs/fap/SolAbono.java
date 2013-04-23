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
		
		try{
			if(bs.toString().length() == 41){
				this.num_cuenta = new Integer(bs.toString().charAt(37));
				this.num_tarjeta = bs.toString().substring(26,36);
				this.importe = new Integer(bs.toString().substring(37, 40));
				return;
			}
		}catch(NumberFormatException e){}
		
		throw new MensajeNoValidoException();
	}
	
	
	
}
