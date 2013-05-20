package practicaacs.banco.bd;

public class Canal {
	public Integer numero;
	public Integer lastMsg;
	public boolean ocupado;
	public boolean recuperado;
	
	
	public Canal(Integer numero, Integer lastMsg) {
		this.numero = numero;
		this.lastMsg = lastMsg;
		this.ocupado = false;
		this.recuperado = true;
	}
	
	public Canal(Integer numero, Integer lastMsg, boolean ocupado) {
		this(numero,lastMsg);
		this.ocupado = ocupado;
		this.recuperado = true;
	}
	
	public Canal(Integer numero, Integer lastMsg, boolean ocupado, boolean recuperado){
		this(numero,lastMsg,ocupado);
		this.recuperado = recuperado;
	}
	
	
}
