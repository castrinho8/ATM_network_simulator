package practicaacs.fap;

public class RespReintegro extends MensajeRespDatos {

	private static final long serialVersionUID = 2560629111000703341L;
	private boolean signo;
	private int saldo;
	
	/**
	 * Constructor mensaje Respesta Reintegro.
	 * @param origen Origen del mensaje
	 * @param destino Destino del Mensaje
	 * @param numcanal Numero de canal
	 * @param nmsg Numero de mensaje
	 * @param codonline Codigo ON-LINE
	 * @param cod_resp Codigo de Respuesta
	 * @param signo Signo del saldo
	 * @param saldo Saldo
	 */
	public RespReintegro(String origen, String destino, int numcanal, int nmsg,
			boolean codonline, CodigosRespuesta cod_resp, boolean signo,
			int saldo) {
		super(origen, destino, CodigosMensajes.RESREINTEGRO, numcanal, nmsg, codonline, cod_resp);
		
		assert(saldo >= 0);
		
		this.signo = signo;
		this.saldo = saldo;
	}
	
	protected RespReintegro(){}
	
	@Override
	protected String printCuerpo() {
		return String.format("%1i%10i",this.signo ? 1 : 0, this.saldo);
	}

	@Override
	protected void parseComp(String bs) throws MensajeNoValidoException {
		super.parseComp(bs);
		try{
			if(bs.toString().length() == 39 && ( bs.toString().charAt(28) == '+' || bs.toString().charAt(28) == '-')){
				this.signo = bs.toString().charAt(28) == '+';
				this.saldo = new Integer(bs.toString().substring(29, 38));
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
