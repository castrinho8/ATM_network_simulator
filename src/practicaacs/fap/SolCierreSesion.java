package practicaacs.fap;

public class SolCierreSesion extends Mensaje {

	private static final long serialVersionUID = 8905474980740931695L;
	private int total_reintegros;
	private int abonos;
	private int traspasos;
	
	/**
	 * Contructor del mensaje de Solicitud de Cierre de Sesion.
	 * @param origen Origen del mensaje
	 * @param destino Destino del mensaje
	 * @param total_reintegros Suma total de los reintegros.
	 * @param abonos Suma total de los abonos.
	 * @param traspasos Suma total de los transpasos.
	 */
	public SolCierreSesion(String origen, String destino, int total_reintegros,
			int abonos, int traspasos) {
		super(origen, destino,CodigosMensajes.SOLCIERRESESION);
		
		assert(total_reintegros >= 0);
		assert(abonos >=0);
		assert(traspasos >= 0);
		
		//TODO: Asserts cota superior
				
		this.total_reintegros = total_reintegros;
		this.abonos = abonos;
		this.traspasos = traspasos;
	}
	
	public SolCierreSesion(){}

	public int getTotal_reintegros() {
		return total_reintegros;
	}

	public int getAbonos() {
		return abonos;
	}

	public int getTraspasos() {
		return traspasos;
	}

	@Override
	protected String printCuerpo() {
		return String.format("%010d%010d%010d",
				this.total_reintegros,
				this.abonos,
				this.traspasos);
	}

	@Override
	protected void parseComp(String bs) throws MensajeNoValidoException {
		super.parseComp(bs);
				
		if(bs.toString().length() != 48)
			throw new MensajeNoValidoException("Lonxitude (" + bs.length() + ") non válida (SolCierreSesion)");
		
		try{
				this.total_reintegros = new Integer(bs.toString().substring(18, 28));
				this.abonos = new Integer(bs.toString().substring(28, 38));
				this.traspasos = new Integer(bs.toString().substring(38, 48));
		}catch(NumberFormatException e){
			String msg = "Error no formato dos numeros (" + bs.substring(29, 38) + bs.substring(39, 48) + bs.substring(49, 58) + ") (SolCierreSesion)";
			throw new MensajeNoValidoException(msg);
		}
		
		
	}
}
