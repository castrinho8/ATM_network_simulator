package practicaacs.fap;

public class RespCierreSesion extends Mensaje {


	private static final long serialVersionUID = 8175815137753089508L;
	private boolean cod_resp;
	private CodigosError cod_error;
	private long total_reintegros;
	private long total_abonos;
	private long total_traspasos;
	

	public RespCierreSesion(String origen, String destino, boolean cod_resp,
			CodigosError cod_error,long total_reintegros, long total_abonos, long total_traspasos) {
		super(origen, destino,CodigosMensajes.RESCIERRESESION);
		this.cod_resp = cod_resp;
		this.cod_error = cod_error;
		this.total_abonos = total_abonos;
		this.total_reintegros = total_reintegros;
		this.total_traspasos = total_traspasos;
	}
	
	public RespCierreSesion(){}

	@Override
	protected String printCuerpo(){
		return String.format("%2s%2s%010d%010d%010d", this.cod_resp ? "00" : "11",this.cod_error.getCodigo(),
				this.total_reintegros,this.total_abonos,this.total_traspasos);
	}

	@Override
	protected void parseComp(String bs) throws MensajeNoValidoException {
		super.parseComp(bs);
		
		if(bs.length() != 52)
			throw new MensajeNoValidoException("Lonxitude (" + bs.length() + ") non válida (RespCierreSesion)");
		
		if (bs.toString().substring(18, 20).equals("11") || bs.toString().substring(18, 20).equals("00"))
			this.cod_resp = bs.toString().substring(18, 20).equals("00");
		else
			throw new MensajeNoValidoException("Formato codigo resposta non válido (RespCierreSesion)");

		try{
			this.cod_error = CodigosError.parse((bs.toString().substring(20, 22)));
		} catch (CodigoNoValidoException e) {
			throw new MensajeNoValidoException("Codigo de error non válido (RespCierreSesion)");
		}
		
		try {
				this.total_reintegros = new Integer(bs.toString().substring(22, 32));
				this.total_abonos = new Integer(bs.toString().substring(32, 42));
				this.total_traspasos = new Integer(bs.toString().substring(42, 52));
		} catch (NumberFormatException e) {
			throw new MensajeNoValidoException("Formato dos numeros (" + bs.substring(22, 32) +  ", " + bs.substring(32, 42) +
											   " e " + bs.substring(42, 52) + ") non valido (RespCierreSesion)");
		}
	}

	public boolean getCodResp() {
		return cod_resp;
	}

	public CodigosError getCodError() {
		return cod_error;
	}

	public int getTotalReintegros() {
		return total_reintegros;
	}

	public int getTotalAbonos() {
		return total_abonos;
	}

	public int getTotalTraspasos() {
		return total_traspasos;
	}
	
	

}
