package practicaacs.banco.bd;

public class Canal {
	public Integer numero;
	public Integer lastMsg;
	public boolean ocupado;
	
	
	public Canal(Integer numero, Integer lastMsg) {
		this.numero = numero;
		this.lastMsg = lastMsg;
		this.ocupado = false;
	}
	
	public Canal(Integer numero, Integer lastMsg,boolean ocupado) {
		this.numero = numero;
		this.lastMsg = lastMsg;
		this.ocupado = ocupado;
	}
	
	
}
