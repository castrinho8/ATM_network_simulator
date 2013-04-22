package practicaacs.banco.bd;

public class Movemento {
	public int codigo;
	public float importe;
	public String data;
	public String tipo;
	
	public Movemento(int codigo, float importe, String data, String tipo) {
		super();
		this.codigo = codigo;
		this.importe = importe;
		this.data = data;
		this.tipo = tipo;
	}
	
}
