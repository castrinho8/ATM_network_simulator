package practicaacs.fap;

public abstract class MensajeDatos extends Mensaje {

	private static final long serialVersionUID = -4316897518534151364L;
	private int numcanal;
	private int nmsg;
	private boolean codonline;
	
	
	public MensajeDatos(String origen, String destino,
			CodigosMensajes tipoMensaje, int numcanal, int nmsg,
			boolean codonline) {
		super(origen, destino, tipoMensaje);
		assert(numcanal > 0);
		assert(numcanal < 100);
		assert(nmsg > 0);
		assert(nmsg < 32767);
		
		this.numcanal = numcanal;
		this.nmsg = nmsg;
		this.codonline = codonline;
	}
	
	public MensajeDatos(){}

	@Override
	protected String printCabecera() {
		return super.printCabecera() + 
				String.format("%2d%5d%1d", numcanal, nmsg, codonline ? 1 : 0);
	}

	public int getNumcanal() {
		return numcanal;
	}

	public int getNmsg() {
		return nmsg;
	}

	public boolean getCodonline() {
		return codonline;
	}

	@Override
	protected void parseComp(String bs) throws MensajeNoValidoException {
		super.parseComp(bs);
		try{
			if(bs.toString().charAt(25) == '1' || bs.toString().charAt(25) == '0'){
				this.numcanal = new Integer(bs.toString().substring(18,19));
				this.nmsg = new Integer(bs.toString().substring(20, 24));
				this.codonline =  bs.toString().charAt(25) == '1';
				return;
			}
		}catch(NumberFormatException e){}
		throw new MensajeNoValidoException();
	}
	
	
	
	
	
}
