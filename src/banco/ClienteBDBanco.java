package banco;

import java.sql.*;
import java.util.ArrayList;

public class ClienteBDBanco {
	private Connection con;
	private Statement statement = null;
	
	public ClienteBDBanco(String urlbd) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		try {
			con = DriverManager.getConnection(urlbd);
			statement = con.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}
	
	public void engadirTarxeta(int codigo) {
		try {
			this.statement.executeUpdate("insert into Tarxeta values ('" + codigo + "')");
		} catch (SQLException e) {
			System.err.print(e);
		}
	}
	
	public void eliminarTarxeta(int codigo) {
		try {
			this.statement.executeUpdate("delete from Tarxeta where tcod = ", codigo);
		} catch (SQLException e) {
			System.err.print(e);
		}
	}

	public void engadirConta(int numero, float saldo) {
		try {
			this.statement.executeUpdate("insert into Conta values ('" + numero + "," + saldo + "')");
		} catch (SQLException e) {
			System.err.print(e);
		}
	}

	public void eliminarConta(int codigo) {
		try {
			this.statement.executeUpdate("delete from Conta where ccod = ", codigo);
		} catch (SQLException e) {
			System.err.print(e);
		}
	}

	public void engadirContaAsociada(int cdgtarxeta, int cdgconta) {
		try {
			this.statement.executeUpdate("insert into ContaTarxeta(ccod,tcod) values ('" + cdgconta + "," + cdgtarxeta + "')");
		} catch (SQLException e) {
			System.err.print(e);
		}		
	}

	public void eliminarContaAsociada(int cdgtarxeta, int cdgconta) {
		try {
			this.statement.executeUpdate("delete from ContaTarxeta where ccod = " + cdgconta + " AND tcod = " + cdgtarxeta);
		} catch (SQLException e) {
			System.err.print(e);
		}
	}

	public ArrayList<Conta> getContas() {
		ResultSet resultSet;
		try {
			resultSet = this.statement.executeQuery("SELECT ccod, saldo FROM Conta");
			
			ArrayList<Conta> res = new ArrayList<Conta>();
			
			while(resultSet.next()){
				res.add(new Conta(resultSet.getInt(1),resultSet.getFloat(2)));
			}
			
			return res;
		} catch (SQLException e) {
			System.err.println(e);
			return null;
		}
	}

	public ArrayList<Tarxeta> getTarxetas() {
		ResultSet resultSet;
		try {
			resultSet = this.statement.executeQuery("SELECT tcod FROM Tarxeta");
			
			ArrayList<Tarxeta> res = new ArrayList<Tarxeta>();
			
			while(resultSet.next()){
				res.add(new Tarxeta(resultSet.getInt(1)));
			}
			
			return res;
		} catch (SQLException e) {
			System.err.println(e);
			return null;
		}
	}

	public ArrayList<Conta> getContasAsociadas(int cdgtar) {
		ResultSet resultSet;
		try {
			resultSet = this.statement.executeQuery("SELECT ccod,saldo FROM ContaTarxeta JOIN Conta USING (ccod) WHERE tcod = " + cdgtar);
			
			ArrayList<Conta> res = new ArrayList<Conta>();
			
			while(resultSet.next()){
				res.add(new Conta(resultSet.getInt(1),resultSet.getFloat(2)));
			}
			
			return res;
		} catch (SQLException e) {
			System.err.println(e);
			return null;
		}
	}

	public ArrayList<Movemento> getMovementos(int numeroconta) {
		ResultSet resultSet;
		try {
			//TODO
			resultSet = this.statement.executeQuery("SELECT mcod,tmnome,importe,data FROM Movemento JOIN TipoMovemento USING (tmcod) WHERE ccod = " + numeroconta);
			
			ArrayList<Movemento> res = new ArrayList<Movemento>();
			
			while(resultSet.next()){
				res.add(new Movemento(resultSet.getInt(1),resultSet.getFloat(3),resultSet.getString(2),resultSet.getString(4)));
			}
			
			return res;
		} catch (SQLException e) {
			System.err.println(e);
			return null;
		}
	}
	
}