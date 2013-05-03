package practicaacs.banco.bd;

import java.util.Date;

public class Movemento {
	public int codigo;
	public int importe;
	public Date data;
	public String tipo;
	
	public Movemento(int codigo, int importe, Date data, String tipo) {
		super();
		this.codigo = codigo;
		this.importe = importe;
		this.data = data;
		this.tipo = tipo;
	}
	
}
