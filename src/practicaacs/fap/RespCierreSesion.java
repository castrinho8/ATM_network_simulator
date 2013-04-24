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

	@Override
	protected String printCuerpo(){
		return String.format("%2i%2s%10i%10i%10i", this.cod_resp,this.cod_error,
				this.total_reintegros,this.total_abonos,this.total_traspasos);
	}

	@Override
	protected void parseComp(byte[] bs) throws MensajeNoValidoException {
		super.parseComp(bs);
		
		try {
			if( bs.toString().length() == 53  &&
				(bs.toString().substring(21, 22).equals("11") || bs.toString().substring(21, 22).equals("00"))){
				this.cod_error = CodigosError.parse((bs.toString().substring(19, 20)));
				this.cod_resp = bs.toString().substring(21, 22).equals("11");
				this.total_reintegros = new Integer(bs.toString().substring(23, 32));
				this.total_abonos = new Integer(bs.toString().substring(33, 42));
				this.total_traspasos = new Integer(bs.toString().substring(43, 52));
				return;
			}
		} catch (CodigoNoValidoException e) {}
		throw new MensajeNoValidoException();
	}

}
