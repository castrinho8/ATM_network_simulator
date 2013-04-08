package practicaacs.fap;

public class SolTraspaso extends MensajeDatos {

	private static final long serialVersionUID = 7732183219803557100L;
	private String num_tarjeta;
	private int num_cuenta_origen;
	private int num_cuenta_destino;
	private int importe;
	
	
	public SolTraspaso(String origen, String destino, int numcanal, int nmsg,
			boolean codonline, String num_tarjeta, int num_cuenta_origen,
			int num_cuenta_destino, int importe) {
		super(origen, destino, CodigosMensajes.SOLTRASPASO, numcanal, nmsg, codonline);
		this.num_tarjeta = num_tarjeta;
		this.num_cuenta_origen = num_cuenta_origen;
		this.num_cuenta_destino = num_cuenta_destino;
		this.importe = importe;
	}

	/**
	 * Getter para o número de tarxeta.
	 * @return
	 */
	public String getNum_tarjeta() {
		return num_tarjeta;
	}


	public int getNum_cuenta_origen() {
		return num_cuenta_origen;
	}


	public int getNum_cuenta_destino() {
		return num_cuenta_destino;
	}


	public int getImporte() {
		return importe;
	}

	@Override
	protected void parseComp(byte[] bs) throws MensajeNoValidoException {
		super.parseComp(bs);
		
		try{
			if(bs.toString().length() == 41){
				this.num_tarjeta = bs.toString().substring(26,36);
				this.num_cuenta_origen = new Integer(bs.toString().charAt(37));
				this.num_cuenta_destino = new Integer(bs.toString().charAt(38));
				this.importe = new Integer(bs.toString().substring(39, 42));
				return;
			}
		}catch(NumberFormatException e){}
		
		throw new MensajeNoValidoException();
	}
	
}
