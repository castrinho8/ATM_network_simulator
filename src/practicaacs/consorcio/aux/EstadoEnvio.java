package practicaacs.consorcio.aux;

/**
 * Enum que define el comportamiento que se debe tomar frente a un 
 * mensaje de datos recibido por el cajero.
 */
public enum EstadoEnvio{

	ENVIO_CORRECTO(1),RECHAZAR_PETICION(2),ALMACENAMIENTO(3);
	private int numero;

	private EstadoEnvio(int numero){
		this.numero = numero;
	}
	
}
	

