package banco;

public class Conta {
	private int numero;
	private float saldo;
	
	public Conta(int numero, float saldo) {
		super();
		this.numero = numero;
		this.saldo = saldo;
	}
	public int getNumero() {
		return numero;
	}
	public float getSaldo() {
		return saldo;
	}
}
