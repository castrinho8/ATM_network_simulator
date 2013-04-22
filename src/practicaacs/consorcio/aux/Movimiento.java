package practicaacs.consorcio.aux;

import java.util.Date;

import practicaacs.fap.CodigosMovimiento;

public class Movimiento {
	public int codigo;
	public int importe;
	public Date data;
	public CodigosMovimiento tipo;
	
	public Movimiento(int codigo, int importe, Date data, CodigosMovimiento tipo) {
		super();
		this.codigo = codigo;
		this.importe = importe;
		this.data = data;
		this.tipo = tipo;
	}
}
