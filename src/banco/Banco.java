package banco;

import java.util.ArrayList;

import banco.iu.VentanaBanco;

public class Banco {
	
	private String nombre;
	private ClienteBDBanco bd;
	private VentanaBanco iu;
	
	public Banco(String name,String urlbd) {
		this.nombre = name;
		this.bd = new ClienteBDBanco(urlbd);
	}
	
	public void setIU(VentanaBanco iu){
		this.iu = iu;
	}
	
	public void engadirConta(Conta c){
		bd.engadirConta(c.getNumero(),c.getSaldo());
	}
	
	public void eliminarConta(int codigo){
		bd.eliminarConta(codigo);
	}
	
	public void eliminarConta(Conta c){
		this.eliminarConta(c.getNumero());
	}
	
	public void engadirTarxeta(Tarxeta t){
		this.engadirTarxeta(t.getCodigo());
	}
	
	public void engadirTarxeta(int codigo){
		bd.engadirTarxeta(codigo);
	}
	
	public void eliminarTarxeta(int codigo){
		bd.eliminarTarxeta(codigo);
	}
	
	public void engadirContaAsociada(int cdgtarxeta, int cdgconta){
		bd.engadirContaAsociada(cdgtarxeta, cdgconta);
	}
	
	public void eliminarContaAsociada(int cdgtarxeta, int cdgconta){
		bd.eliminarContaAsociada(cdgtarxeta,cdgconta);
	}
	
	public void engadirMovemento(int codconta){
		//TODO
	}
	
	public ArrayList<Conta> getContas(){
	 	return bd.getContas();
	}
	
	public ArrayList<Tarxeta> getTarxetas(){
		return bd.getTarxetas();
	}
	
	public ArrayList<Conta> getContasAsociadas(int cdgtar){
		return bd.getContasAsociadas(cdgtar);
	}
	
	public ArrayList<Conta> getContasAsociadas(Tarxeta t){
		return bd.getContasAsociadas(t.getCodigo());
	}
	
	public ArrayList<Movemento> getMovementosConta(int nconta) {
		return bd.getMovementos(nconta);
	}
	
	public ArrayList<Movemento> getMovementosConta(Conta c){
		return this.getMovementosConta(c.getNumero());
	}

    public String getName() {
    	return this.nombre;
    }

	
	
}