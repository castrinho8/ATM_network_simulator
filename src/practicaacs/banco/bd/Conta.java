package practicaacs.banco.bd;

public class Conta {
	private int numero;
	private int saldo;
	
	public Conta(int numero, int saldo) {
		super();
		this.numero = numero;
		this.saldo = saldo;
	}
	public int getNumero() {
		return numero;
	}
	public int getSaldo() {
		return saldo;
	}
}
