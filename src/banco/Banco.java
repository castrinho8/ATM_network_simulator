package banco;

import java.sql.SQLException;
import java.util.ArrayList;

public class Banco {
	
	private String nombre;
	private int cdgBanco;
	
	public Banco(int i, String nombre){
		this.cdgBanco = i;
		this.nombre = nombre;
	}
	
	public Banco(String name) throws InsertBancoException{
		this.cdgBanco = addBancoDB(name);
		this.nombre = name;
	}
	
	private static int addBancoDB(String name) throws InsertBancoException{
		int n = ClienteBDBanco.instance().insertBanco(name);
		if(n == -1){
			throw new InsertBancoException();
		}
		
		//inicializarBanco(n);
		
		
		return n;
	}

	public void addTarjeta(){
		
	}
	
	public void addCuenta(){
		
	}
	
	public void addMovimiento(){
		
	}
	
	public static ArrayList<Banco> getBancosBD(){
		try {
			return ClienteBDBanco.instance().getListOfBancos();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new ArrayList<Banco>();
	}

    public String getName() {
    	return this.nombre;
    }

	public void remove() {
		ClienteBDBanco.instance().removeBanco(this.cdgBanco);
	}

	public Object[][] getDatosCuentas() {
		return ClienteBDBanco.instance().getDatosCuentas(this.cdgBanco);
	}
	
}