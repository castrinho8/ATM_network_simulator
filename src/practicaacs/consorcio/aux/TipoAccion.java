package practicaacs.consorcio.aux;

	public enum TipoAccion{

		CONEXION(1),RECUPERACION(2),FIN_RECUPERACION(3),ENVIO(4);
		private int numero;

		private TipoAccion(int numero){
			this.numero = numero;
		}
	
}
