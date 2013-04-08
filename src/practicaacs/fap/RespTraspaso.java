package practicaacs.fap;

public class RespTraspaso extends MensajeRespDatos {

	private static final long serialVersionUID = 6454327230606401799L;
	private boolean signoOrigen;
	private int saldoOrigen;
	private boolean signoDestino;
	private int saldoDestino;
	
	/**
	 * Constructor del mensaje Reespuesta Traspaso.
	 * @param origen Origen del mensaje
	 * @param destino Destino del Mensaje
	 * @param numcanal Numero de canal
	 * @param nmsg Numero de mensaje
	 * @param codonline Codigo ON-LINE
	 * @param cod_resp Codigo de Respuesta
	 * @param signo Signo del saldo
	 * @param saldo Saldo
	 */
	public RespTraspaso(String origen, String destino, int numcanal, int nmsg,
			boolean codonline, CodigosRespuesta cod_resp, boolean signoOrigen,
			int saldoOrigen, boolean signoDestino, int saldoDestino) {
		super(origen, destino,CodigosMensajes.RESTRASPASO , numcanal, nmsg, codonline, cod_resp);
		this.signoOrigen = signoOrigen;
		this.saldoOrigen = saldoOrigen;
		this.signoOrigen = signoOrigen;
		this.saldoOrigen = saldoOrigen;
	}

	@Override
	protected String printCuerpo() {
		return String.format("%1i%10i%1i%10i",this.signoOrigen ? 1 : 0, this.saldoOrigen,this.signoDestino ? 1 : 0, this.saldoDestino);
	}
	
	
	@Override
	protected void parseComp(byte[] bs) throws MensajeNoValidoException {
		super.parseComp(bs);
		try{
			if(bs.toString().length() == 39 && ( bs.toString().charAt(28) == '+' || bs.toString().charAt(28) == '-')){
				this.signoOrigen = bs.toString().charAt(28) == '+';
				this.saldoOrigen = new Integer(bs.toString().substring(29, 38));
				this.signoDestino = bs.toString().charAt(39) == '+';
				this.saldoDestino = new Integer(bs.toString().substring(40, 49));
			}
		}catch(NumberFormatException e){}
		
		throw new MensajeNoValidoException();
	}
	
}
