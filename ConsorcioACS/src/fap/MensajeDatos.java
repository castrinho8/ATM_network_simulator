package fap;

public abstract class MensajeDatos extends Mensaje {

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

	@Override
	protected String printCabecera() {
		return super.printCabecera() + 
				String.format("%2i%5i%1i", numcanal, nmsg, codonline ? 1 : 0);
	}

	public int getNumcanal() {
		return numcanal;
	}

	public int getNmsg() {
		return nmsg;
	}

	public boolean isCodonline() {
		return codonline;
	}
	
}
