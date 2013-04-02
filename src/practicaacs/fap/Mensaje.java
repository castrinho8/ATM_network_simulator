package practicaacs.fap;

public class Mensaje implements java.io.Serializable {

	private String origen;
	private String destino;
	private CodigosMensajes tipoMensaje;
	
	public Mensaje(String origen, String destino,CodigosMensajes tipoMensaje){
		assert(origen.length()<=8);
		assert(destino.length()<=8);
		
		this.tipoMensaje = tipoMensaje;
		this.origen = origen;
		this.destino = destino;
	}

	public Mensaje() {
		// TODO Auto-generated constructor stub
	}

	public String getOrigen() {
		return origen;
	}

	public String getDestino() {
		return destino;
	}

	public CodigosMensajes getTipoMensaje() {
		return tipoMensaje;
	}
	
	public boolean es_consulta(){
		return ((tipoMensaje == CodigosMensajes.CONSULTARSALDO) | (tipoMensaje == CodigosMensajes.CONSULTARMOVIMIENTOS));
	}

	@Override
	public String toString(){
		return this.printCabecera() + this.printCuerpo(); 
	}

	protected String printCuerpo() {
		return "";
	}

	protected String printCabecera() {
		return String.format("%8s%8s%2i", origen,destino,this.tipoMensaje.getNum());
	}

	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	public byte[] getBytes() {
		// TODO Auto-generated method stub
		return null;
	}

	public static Mensaje parse(byte[] bs) throws MensajeNoValidoException {
		Mensaje m = new Mensaje();
		m.destino = bs.toString().substring(9, 16);
		m.origen = bs.toString().substring(0, 8);
		
		try {
			m.tipoMensaje = CodigosMensajes.parse(bs.toString().substring(16, 18));
		} catch (CodigoNoValidoException e) {
			throw new MensajeNoValidoException();
		}
		
		return null;
	}
}
