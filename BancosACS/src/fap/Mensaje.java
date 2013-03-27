package fap;

public abstract class Mensaje implements java.io.Serializable {

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
}
