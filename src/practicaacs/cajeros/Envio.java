package practicaacs.cajeros;

import practicaacs.fap.CodigosMensajes;

/**
 * Clase utilizada para guardar los datos que se obtienen de la IU
 * y a partir de los cuales, posteriormente se construye el mensaje
 * a enviar.
 */
public class Envio {

	private String num_tarjeta;
	private CodigosMensajes tipoMensaje;
	private int importe;
	
	private int num_cuenta_origen;
	private int num_cuenta_destino;
	
	/**
	 * Constructor de la clase.
	 * @param tarjeta La tarjeta que realiza la acción.
	 * @param cuenta La cuenta perteneciente a la tarjeta.
	 */
	public Envio(String tarjeta, int cuenta) {
		this.num_tarjeta = tarjeta;
		this.num_cuenta_origen = cuenta;
	}

	/**
	 * Constructor de la clase
	 * @param num_tarjeta La tarjeta que realiza la acción.
	 * @param tipoMensaje El tipo de mensaje a enviar.
	 * @param importe El importe.
	 * @param num_cuenta_origen La cuenta desde la que se origina el movimiento.
	 * @param num_cuenta_destino La cuenta que recibe el movimiento.
	 */
	public Envio(String num_tarjeta, CodigosMensajes tipoMensaje, int importe,
			int num_cuenta_origen, int num_cuenta_destino) {
		this.num_tarjeta = num_tarjeta;
		this.tipoMensaje = tipoMensaje;
		this.importe = importe;
		this.num_cuenta_origen = num_cuenta_origen;
		this.num_cuenta_destino = num_cuenta_destino;
	}

	/**
	 * Setter del numero de tarjeta.
	 * @param num_tarjeta El nuevo número de tarjeta.
	 */
	public void setNum_tarjeta(String num_tarjeta) {
		this.num_tarjeta = num_tarjeta;
	}

	/**
	 * Setter del tipo de Mensaje.
	 * @param tipoMensaje El nuevo tipo de mensaje.
	 */
	public void setTipoMensaje(CodigosMensajes tipoMensaje) {
		this.tipoMensaje = tipoMensaje;
	}

	/**
	 * Setter del importe.
	 * @param importe El nuevo importe.
	 */
	public void setImporte(int importe) {
		this.importe = importe;
	}

	/**
	 * Setter de la cuenta de origen.
	 * @param num_cuenta_origen El nuevo valor para la cuenta de origen.
	 */
	public void setNum_cuenta_origen(int num_cuenta_origen) {
		this.num_cuenta_origen = num_cuenta_origen;
	}

	/**
	 * Setter de la cuenta destino.
	 * @param num_cuenta_destino El nuevo valor para la cuenta destino.
	 */
	public void setNum_cuenta_destino(int num_cuenta_destino) {
		this.num_cuenta_destino = num_cuenta_destino;
	}

	/**
	 * Getter del número de tarjeta.
	 * @return El string con el número de tarjeta.
	 */
	public String getNum_tarjeta() {
		return num_tarjeta;
	}

	/**
	 * Getter del tipo de Mensaje. 
	 * @return El tipo de mensaje del Envio.
	 */
	public CodigosMensajes getTipoMensaje() {
		return tipoMensaje;
	}

	/**
	 * Getter del importe.
	 * @return El importe del Envio.
	 */
	public int getImporte() {
		return importe;
	}

	/**
	 * Getter del número de cuenta de origen de la operación.
	 * @return El número de cuenta origen.
	 */
	public int getNum_cuenta_origen() {
		return num_cuenta_origen;
	}

	/**
	 * Getter del número de cuenta de destino de la operación.
	 * @return El número de cuenta destino.
	 */
	public int getNum_cuenta_destino() {
		return num_cuenta_destino;
	}
	
}
