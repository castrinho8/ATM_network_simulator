package practicaacs.consorcio.aux;

/**
 * Enum que modela el origen/destino de los mensajes.
 */
public enum TipoOrigDest {
		BANCO(1),CONSORCIO(2),CAJERO(3);
		private int numero;

		private TipoOrigDest(int numero){
			this.numero = numero;
		}

		public int getNum(){
			return this.numero;
		}
}
