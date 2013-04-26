package practicaacs.fap;

public class RespSaldo extends MensajeRespDatos {

	private static final long serialVersionUID = -3030494096556041127L;
	private boolean signo;
	private int saldo;
	
	/**
	 * Constructor del mensaje Reespuesta Consultar Saldo.
	 * @param origen Origen del mensaje
	 * @param destino Destino del Mensaje
	 * @param numcanal Numero de canal
	 * @param nmsg Numero de mensaje
	 * @param codonline Codigo ON-LINE
	 * @param cod_resp Codigo de Respuesta
	 * @param signo Signo del saldo
	 * @param saldo Saldo
	 */
	public RespSaldo(String origen, String destino, int numcanal, int nmsg,
			boolean codonline, CodigosRespuesta cod_resp, boolean signo,
			int saldo) {
		super(origen, destino,CodigosMensajes.RESSALDO , numcanal, nmsg, codonline, cod_resp);
		this.signo = signo;
		this.saldo = saldo;
	}

	public RespSaldo(){}
	
	@Override
	protected String printCuerpo() {
		return String.format("%1s%010d",this.signo ? "+" : "-", this.saldo);
	}
	
	
	@Override
	protected void parseComp(String bs) throws MensajeNoValidoException {
		super.parseComp(bs);
		
		if(bs.length() != 39)
			throw new MensajeNoValidoException("Lonxitude (" + bs.length() + ") non válida (RespSaldo)");
		
		if(bs.substring(28,29).equals("+") || bs.substring(28,29).equals("-"))
			this.signo = bs.substring(28,29).equals("+");
		else
			throw new MensajeNoValidoException("Mal formato en el signo (RespSaldo)");
		
		try{
			this.saldo = new Integer(bs.toString().substring(29, 39));
		}catch(NumberFormatException e){
			throw new MensajeNoValidoException("Error saldo (RespSaldo)");
		}
	}
	
	public boolean getSigno() {
		return signo;
	}

	public int getSaldo() {
		return saldo;
	}
	
	
}
