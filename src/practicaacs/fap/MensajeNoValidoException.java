package practicaacs.fap;

public class MensajeNoValidoException extends Exception {
	
	public MensajeNoValidoException(String string) {
		super(string);
	}
	
	public MensajeNoValidoException() {
		super();
	}

	private static final long serialVersionUID = -3568540970014907354L;

}
