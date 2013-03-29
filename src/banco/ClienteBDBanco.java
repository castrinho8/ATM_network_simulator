package banco;

import java.sql.*;
import java.util.ArrayList;

public class ClienteBDBanco {
	static private ClienteBDBanco instance;
	private Connection con;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	
	private ClienteBDBanco(){
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost/acs?user=acsuser&password=password");
			statement = con.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static ClienteBDBanco instance(){
		if (instance == null){
			instance = new ClienteBDBanco();
		}
		return instance;
	}
	
	public ArrayList<Banco> getListOfBancos() throws SQLException{
		ResultSet resultSet = this.statement.executeQuery("select * from Banco");
		
		ArrayList<Banco> list = new ArrayList<Banco>();
		
		while(resultSet.next()){
			list.add(new Banco(resultSet.getInt(1),resultSet.getString(2)));
		}
		
		return list;
	}

	public int insertBanco(String name) {
		ResultSet resultSet;
		try {
			this.statement.executeUpdate("insert into Banco (bname)  values ('" + name + "')");
			resultSet = this.statement.executeQuery("SELECT LAST_INSERT_ID();");
			resultSet.next();
			return resultSet.getInt(1);
		} catch (SQLException e) {
			System.err.print(e);
		}
		return -1;
	}
	
	public boolean removeBanco(int id){
		try {
			this.statement.executeUpdate("DELETE FROM Banco WHERE bcod = " + id);
			return true;
		} catch (SQLException e) {
			return false;
		}
	}

	public Object[][] getDatosCuentas(int cdgBanco) {
		ResultSet resultSet;
		try {
			resultSet = this.statement.executeQuery("SELECT tcod,ccod,saldo FROM Cuenta WHERE bcod = " + cdgBanco);
			
			resultSet.last();			
			Object[][] list = new Object[resultSet.getRow()][3];
			
			if(resultSet.getRow() != 0){
			
				resultSet.first();
				int i = 0;			
				do{
					list[i++] = new Object[]{new Integer(resultSet.getInt(1)),
											 new Integer(resultSet.getInt(2)),
											 new Float(resultSet.getFloat(3))};
				}while(resultSet.next());	
			}
	
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void insertTarjeta(int n, int i) {
		// TODO Auto-generated method stub
		
	}
	
}