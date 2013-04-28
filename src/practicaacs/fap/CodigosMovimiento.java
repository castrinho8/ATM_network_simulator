package practicaacs.fap;

public enum CodigosMovimiento {
	
	REINTEGRO(10), TRANSEMITIDO(11), TRANSRECIB(12), PAGORECIB(13), ABONO(50), COBROCHEQUE(51),OTRO(99);
	
	private int numero;

	private CodigosMovimiento(int numero){
		this.numero = numero;
	}

	public static CodigosMovimiento getTipoMovimiento(int valor) throws CodigoNoValidoException{
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
			throw new CodigoNoValidoException();
		}
	}

	public int getNum() {
		// TODO Auto-generated method stub
		return numero;
	}
}
