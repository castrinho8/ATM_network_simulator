package practicaacs.fap;

public class SolMovimientos extends MensajeDatos {

	private static final long serialVersionUID = -2378212785182317311L;
	private String num_tarjeta;
	private int num_cuenta;
	
	public SolMovimientos(String origen, String destino,
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

	public String getNum_tarjeta() {
		return num_tarjeta;
	}

	public int getNum_cuenta() {
		return num_cuenta;
	}

	@Override
	protected void parseComp(byte[] bs) throws MensajeNoValidoException {
		super.parseComp(bs);

		try{
			if(bs.toString().length() == 37){
				this.num_cuenta = new Integer(bs.toString().charAt(37));
				this.num_tarjeta = bs.toString().substring(26,36);
			}
		}catch(NumberFormatException e){}
		
		throw new MensajeNoValidoException();	
	}
	
	
}
