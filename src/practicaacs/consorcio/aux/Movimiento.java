package practicaacs.consorcio.aux;

import java.util.Date;

import practicaacs.fap.CodigosMovimiento;

public class Movimiento {
	private int codigo;
	private boolean signo;
	private int importe;
	private Date data;
	private CodigosMovimiento tipo;
	
	public Movimiento(int codigo,boolean sign, int importe, Date data, CodigosMovimiento tipo) {
		super();
		this.codigo = codigo;
		this.signo = sign;
		this.importe = importe;
		this.data = data;
		this.tipo = tipo;
	}
	
	public int getCodigo() {
		return codigo;
	}

	public boolean getSigno() {
		return signo;
	}

	public int getImporte() {
		return importe;
	}

	public Date getData() {
		return data;
	}

	public CodigosMovimiento getTipo() {
		return tipo;
	}

}
