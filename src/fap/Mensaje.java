package fap;

public abstract class Mensaje{

	private String origen;
	private String destino;
	private CodigosMensajes tipoMensaje;
	
	public Mensaje(String origen, String destino,CodigosMensajes tipoMensaje){
		this.origen = origen;
		this.destino = destino;
	}

	/**
	 * @return the origen
	 */
	protected String getOrigen() {
		return origen;
	}



	/**
	 * @param origen the origen to set
	 */
	protected void setOrigen(String origen) {
		this.origen = origen;
	}



	/**
	 * @return the destino
	 */
	protected String getDestino() {
		return destino;
	}



	/**
	 * @param destino the destino to set
	 */
	protected void setDestino(String destino) {
		this.destino = destino;
	}



	/**
	 * @return the tipoMensaje
	 */
	protected CodigosMensajes getTipoMensaje() {
		return tipoMensaje;
	}



	/**
	 * @param tipoMensaje the tipoMensaje to set
	 */
	protected void setTipoMensaje(CodigosMensajes tipoMensaje) {
		this.tipoMensaje = tipoMensaje;
	}


	@Override
	public String toString(){
		return this.printCabecera() + this.printCuerpo(); 
	}

	protected String printCuerpo() {
		return "";
	}

	protected String printCabecera() {
		return origen + destino + this.tipoMensaje.toString();
	}
}
