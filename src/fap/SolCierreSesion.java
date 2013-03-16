package fap;

public class SolCierreSesion extends Mensaje {

	private int total_reintegros;
	private int abonos;
	private int traspasos;
	
	public SolCierreSesion(String origen, String destino, int total_reintegros,
			int abonos, int traspasos) {
		super(origen, destino,CodigosMensajes.SOLCIERRESESION);
		this.total_reintegros = total_reintegros;
		this.abonos = abonos;
		this.traspasos = traspasos;
	}

	@Override
	protected String printCuerpo() {
		return super.printCuerpo();
	}

}
