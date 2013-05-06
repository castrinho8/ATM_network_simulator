package practicaacs.consorcio.aux;

public enum EstadoEnvio{

	ENVIO_CORRECTO(1),RECHAZAR_PETICION(2),ALMACENAMIENTO(3);
	private int numero;

	private EstadoEnvio(int numero){
		this.numero = numero;
	}
	
}
	

