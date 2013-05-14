package practicaacs.consorcio.aux;

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
