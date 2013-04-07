package fap;

public class SolCierreSesion extends Mensaje {

	private long total_reintegros;
	private long abonos;
	private long traspasos;
	
	/**
	 * Contructor del mensaje de Solicitud de Cierre de Sesion.
	 * @param origen Origen del mensaje
	 * @param destino Destino del mensaje
	 * @param total_reintegros Suma total de los reintegros.
	 * @param abonos Suma total de los abonos.
	 * @param traspasos Suma total de los transpasos.
	 */
	public SolCierreSesion(String origen, String destino, long total_reintegros,
			long abonos, long traspasos) {
		super(origen, destino,CodigosMensajes.SOLCIERRESESION);
		
		assert(total_reintegros >= 0);
		assert(abonos >=0);
		assert(traspasos >= 0);
		
		//TODO: Asserts cota superior
				
		this.total_reintegros = total_reintegros;
		this.abonos = abonos;
		this.traspasos = traspasos;
	}

	@Override
	protected String printCuerpo() {
		return String.format("%10i%10i%10i",
				this.total_reintegros,
				this.abonos,
				this.traspasos);
	}

	public long getTotal_reintegros() {
		return total_reintegros;
	}

	public long getAbonos() {
		return abonos;
	}

	public long getTraspasos() {
		return traspasos;
	}

}
