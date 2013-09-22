package practicaacs.consorcio.aux;

/**
 * Enum que modela un tipo de acción a realizar por la
 * ConexiónConsorcio_Cajeros.
 */
public enum TipoAccion{

	CONEXION(1),RECUPERACION(2),FIN_RECUPERACION(3),ENVIO(4);
	private int numero;

	private TipoAccion(int numero){
		this.numero = numero;
	}
	
}
