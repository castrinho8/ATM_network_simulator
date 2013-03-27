package fap;

public abstract class Mensaje{

	private String origen;
	private String destino;
	private CodigosMensajes tipoMensaje;
	
	/**
	 * Constuctor de un mensaje abstracto.
	 * @param origen
	 * @param destino
	 * @param tipoMensaje
	 */
	public Mensaje(String origen, String destino,CodigosMensajes tipoMensaje){
		assert(origen.length() != 8);
		assert(destino.length() != 8);
		
		this.tipoMensaje = tipoMensaje;
		this.origen = origen;
		this.destino = destino;
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
	
	public static Mensaje parse(String s){
		return null; //TODO
	}
}
