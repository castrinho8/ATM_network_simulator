package banco;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

import banco.iu.VentanaBanco;

public class Banco {
	
	private String nombre;
	private ClienteBDBanco bd;
	private VentanaBanco iu;
	private int puerto;
	
	public Banco(String configfile){
		Properties prop = new Properties();
	    InputStream is;
		try {
			is = new FileInputStream(configfile);
		    prop.load(is);
		} catch (FileNotFoundException e) {
			System.err.println("Non se encontrou arquivo de configuracion " + configfile + ".");
			System.exit(-1);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.nombre = prop.getProperty("banco.name");
		this.puerto = new Integer(prop.getProperty("banco.port"));
		String bdname = prop.getProperty("bd.name");
		String bdadd = prop.getProperty("bd.add");
		String bduser = prop.getProperty("bd.user");
		String bdpass = prop.getProperty("bd.pass");
		this.bd = new ClienteBDBanco("jdbc:mysql://" + bdadd + "/" + bdname + "?user=" + bduser + "&password=" + bdpass);
	}
	
	
	public Banco(String name,String urlbd,int puerto) {
		this.nombre = name;
		this.bd = new ClienteBDBanco(urlbd);
		this.puerto = puerto;
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

	public void establecerValoresPorDefecto() {
		bd.valoresPorDefecto();
	}

	public void cerrarSesion() {
		// TODO Auto-generated method stub
		
	}


	public void iniciarSesion(int numCanles) {
		// TODO Auto-generated method stub
		
	}

	
	
}