package practicaacs.consorcio.aux;

import practicaacs.fap.Mensaje;

/**
 * Clase utilizada para obtener los últimos envios realizados por los canales.
 * Cuando se realiza una recuperación, se crea un elemento MensajeCajero para cada
 * mensaje que se obtiene de la BD.
 */
public class MensajeCajero {
	
	private Mensaje mensaje;
	private String id_cajero;
	private boolean contestado;
	private int canal;
	
	/**
	 * Constructor de la clase.
	 * @param mensaje El mensaje obtenido.
	 * @param id_cajero El id del cajero que envió el mensaje.
	 * @param cont Valor booleano que indica si el mensaje está contestado.
	 * @param can El canal por el que se envió el mensaje.
	 */
	public MensajeCajero(Mensaje mensaje, String id_cajero,boolean cont,int can) {
		super();
		this.mensaje = mensaje;
		this.id_cajero = id_cajero;
		this.contestado = cont;
		this.canal = can;
	}

	/**
	 * Método que devuelve si el mensaje está contestado.
	 * @return True si está contestado y False en caso contrario.
	 */
	public boolean isContestado() {
		return contestado;
	}

	/**
	 * Getter del Mensaje.
	 * @return El Mensaje.
	 */
	public Mensaje getMensaje() {
		return mensaje;
	}

	/**
	 * Getter del id del Cajero que envió el mensaje.
	 * @return El identificador del cajero.
	 */
	public String getId_cajero() {
		return id_cajero;
	}
	
	/**
	 * Getter del canal por el que se envió el Mensaje.
	 * @return El canal por el que se realizó el envío.
	 */
	public int getCanal(){
		return canal;
	}

}
