package practicaacs.consorcio.bd;

/**
 * Exepción utilizada cuando se producen errores en el acceso a la 
 * BD del consorcio.
 */
public class ConsorcioBDException extends Exception{

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor de la clase.
	 */
	public ConsorcioBDException() {}

	/**
	 * Constructor de la clase.
	 * @param message El mensaje a mostrar.
	 */
	public ConsorcioBDException(String message) {
		super(message);
	}
}


