package practicaacs.fap;

public enum TiposMovimiento {
	
	REINTEGRO(10), TRANSEMITIDO(11), TRANSRECIB(12), PAGORECIB(13), ABONO(50), COBROCHEQUE(51),OTRO(99);
	
	private int numero;

	private TiposMovimiento(int numero){
		this.numero = numero;
	}
	
	public static TiposMovimiento getTipoMovimiento(int valor){
		switch(valor){
		case 10:
			return REINTEGRO;
		case 11:
			return TRANSEMITIDO;
		case 12:
			return TRANSRECIB;
		case 13:
			return PAGORECIB;
		case 50:
			return ABONO;
		case 51:
			return COBROCHEQUE;
		case 99:
			return OTRO;
		default:
			throw new IllegalArgumentException();
		}
	}
}
