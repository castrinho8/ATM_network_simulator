package practicaacs.banco.bd;

public class Mensaxe {
	public String tipo;
	public Integer numCanal;
	public Integer numMenx;
	public boolean enviado;
	
	public Mensaxe(String tipo, Integer numCanal, Integer numMenx, boolean enviado) {
		this.tipo = tipo;
		this.numCanal = numCanal;
		this.numMenx = numMenx;
		this.enviado = enviado;
	}
}
