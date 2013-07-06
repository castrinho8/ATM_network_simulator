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

	public SolTraspaso(){}
	
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

	public int getNum_cuenta(){
		return num_cuenta_destino;
	}


	public int getImporte() {
		return importe;
	}
	

	@Override
	protected String printCuerpo() {
		return String.format("%11s%1d%1d%04d", this.num_tarjeta,this.num_cuenta_origen,this.num_cuenta_destino,this.importe);
	}

	@Override
	protected void parseComp(String bs) throws MensajeNoValidoException {
		super.parseComp(bs);
		
		if(bs.length() != 43)
			throw new MensajeNoValidoException("Lonxitude (" + bs.length() + ") non válida (SolTraspaso)");
		
		this.num_tarjeta = bs.substring(26,37).trim();
		
		try{
			this.num_cuenta_origen = new Integer(bs.substring(37,38));
			this.num_cuenta_destino = new Integer(bs.substring(38,39));
			this.importe = new Integer(bs.substring(39, 43));
		}catch(NumberFormatException e){
			throw new MensajeNoValidoException("Formato de numeros incorrecto (SolTraspaso)");
		}
	}
	
}
