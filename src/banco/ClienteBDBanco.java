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
			this.statement.executeUpdate("INSERT INTO Tarxeta VALUES (" + codigo + ")");
		} catch (SQLException e) {
			System.err.print(e);
		}
	}
	
	public void eliminarTarxeta(int codigo) {
		try {
			this.statement.executeUpdate("DELETE FROM Tarxeta WHERE tcod = " + codigo);
		} catch (SQLException e) {
			System.err.print(e);
		}
	}

	public void engadirConta(int numero, float saldo) {
		try {
			this.statement.executeUpdate("INSERT INTO Conta (ccod,saldo) VALUES (" + numero + "," + saldo + ")");
		} catch (SQLException e) {
			System.err.print(e);
		}
	}

	public void eliminarConta(int codigo) {
		try {
			this.statement.executeUpdate("DELETE FROM Conta WHERE ccod = " + codigo);
		} catch (SQLException e) {
			System.err.print(e);
		}
	}

	public void engadirContaAsociada(int cdgtarxeta, int cdgconta) {
		try {
			this.statement.executeUpdate("insert into ContaTarxeta(ccod,tcod) values (" + cdgconta + "," + cdgtarxeta + ")");
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

	public void valoresPorDefecto() {
		try {
			this.statement.executeUpdate("delete from Conta where true");
			this.statement.executeUpdate("delete from Tarxeta where true");
			this.statement.executeUpdate("INSERT INTO Tarxeta VALUES (1)");
			this.statement.executeUpdate("INSERT INTO Tarxeta VALUES (2)");
			this.statement.executeUpdate("INSERT INTO Tarxeta VALUES (3)");
			this.statement.executeUpdate("INSERT INTO Tarxeta VALUES (4)");
			this.statement.executeUpdate("INSERT INTO Tarxeta VALUES (5)");
			this.statement.executeUpdate("INSERT INTO Conta VALUES (0,0000)");
			this.statement.executeUpdate("INSERT INTO Conta VALUES (1,1000)");
			this.statement.executeUpdate("INSERT INTO Conta VALUES (2,2000)");
			this.statement.executeUpdate("INSERT INTO Conta VALUES (3,3000)");
			this.statement.executeUpdate("INSERT INTO Conta VALUES (4,4000)");
			this.statement.executeUpdate("INSERT INTO Conta VALUES (5,5000)");
			this.statement.executeUpdate("INSERT INTO Conta VALUES (6,6000)");
			this.statement.executeUpdate("INSERT INTO Conta VALUES (7,7000)");
			this.statement.executeUpdate("INSERT INTO Conta VALUES (8,8000)");
			this.statement.executeUpdate("INSERT INTO Conta VALUES (9,9000)");
			this.statement.executeUpdate("INSERT INTO ContaTarxeta VALUES(1,1)");
			this.statement.executeUpdate("INSERT INTO ContaTarxeta VALUES(2,1)");
			this.statement.executeUpdate("INSERT INTO ContaTarxeta VALUES(3,1)");
			this.statement.executeUpdate("INSERT INTO ContaTarxeta VALUES(1,2)");
			this.statement.executeUpdate("INSERT INTO ContaTarxeta VALUES(3,2)");
			this.statement.executeUpdate("INSERT INTO ContaTarxeta VALUES(4,3)");
			this.statement.executeUpdate("INSERT INTO ContaTarxeta VALUES(5,3)");
			this.statement.executeUpdate("INSERT INTO ContaTarxeta VALUES(6,3)");
			this.statement.executeUpdate("INSERT INTO ContaTarxeta VALUES(7,4)");
			this.statement.executeUpdate("INSERT INTO ContaTarxeta VALUES(8,4)");
			this.statement.executeUpdate("INSERT INTO ContaTarxeta VALUES(0,5)");
			this.statement.executeUpdate("INSERT INTO ContaTarxeta VALUES(9,5)");	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}