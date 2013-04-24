package practicaacs.fap;

public class RespAbono extends MensajeRespDatos {

	private static final long serialVersionUID = 3081079194749323473L;
	private boolean signo;
	private int saldo;
	
	/**
	 * Constructor del mensaje Reespuesta Abono.
	 * @param origen Origen del mensaje
	 * @param destino Destino del Mensaje
	 * @param numcanal Numero de canal
	 * @param nmsg Numero de mensaje
	 * @param codonline Codigo ON-LINE
	 * @param cod_resp Codigo de Respuesta
	 * @param signo Signo del saldo
	 * @param saldo Saldo
	 */
	public RespAbono(String origen, String destino, int numcanal, int nmsg,
			boolean codonline, CodigosRespuesta cod_resp, boolean signo,
			int saldo) {
		super(origen, destino,CodigosMensajes.RESABONO , numcanal, nmsg, codonline, cod_resp);
		this.signo = signo;
		this.saldo = saldo;
	}

	public RespAbono(){}
	
	@Override
	protected String printCuerpo() {
		return String.format("%1s%10d",this.signo ? "+" : "-", this.saldo);
	}

	@Override
	protected void parseComp(String bs) throws MensajeNoValidoException {
		super.parseComp(bs);
		try{
			if(bs.toString().length() != 39){
				this.signo = bs.toString().charAt(28) == '+';
				this.saldo = new Integer(bs.toString().substring(29, 38));
				return;
			}
		}catch(NumberFormatException e){}
		throw new MensajeNoValidoException();
	}

	public boolean getSigno() {
		return signo;
	}

	public int getSaldo() {
		return saldo;
	}
	
	
	
	
}