package practicaacs.banco.bd;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

import practicaacs.fap.Mensaje;
import practicaacs.fap.RespSaldo;


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
			System.exit(-1);
		}	
	}
	
	public void engadirTarxeta(String codigo) {
		try {
			this.statement.executeUpdate("INSERT INTO Tarxeta VALUES (" + codigo + ")");
		} catch (SQLException e) {
			System.err.print(e);
		}
	}
	
	public void eliminarTarxeta(String codigo) {
		try {
			this.statement.executeUpdate("DELETE FROM Tarxeta WHERE tcod = '" + codigo + "'");
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

	public void engadirContaAsociada(String cdgtarxeta, int numconta, int cdgconta) {
		try {
			this.statement.executeUpdate("insert into ContaTarxeta(tcod,cnum,ccod) values ('" + cdgtarxeta + "' ," + numconta + "," + cdgconta + ")");
		} catch (SQLException e) {
			System.err.print(e);
		}		
	}

	public void eliminarContaAsociada(String cdgtarxeta, int numconta) {
		try {
			this.statement.executeUpdate("delete from ContaTarxeta where cnum = " + numconta + " AND tcod = '" + cdgtarxeta + "'");
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
				res.add(new Conta(resultSet.getInt(1),resultSet.getInt(2)));
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
				res.add(new Tarxeta(resultSet.getString(1)));
			}
			
			return res;
		} catch (SQLException e) {
			System.err.println(e);
			return null;
		}
	}

	public HashMap<Integer,Conta> getContasAsociadas(String ntarxeta) {
		ResultSet resultSet;
		try {
			resultSet = this.statement.executeQuery("SELECT cnum,ccod,saldo FROM ContaTarxeta JOIN Conta" +
					" USING (ccod) WHERE tcod = '" + ntarxeta + "' order by cnum");
			
			HashMap<Integer,Conta> res = new HashMap<Integer,Conta>(3);
			
			while(resultSet.next()){
				res.put(resultSet.getInt(1),new Conta(resultSet.getInt(2),resultSet.getInt(3)));
			}
			
			return res;
		} catch (SQLException e) {
			System.err.println(e);
			System.err.println("A consulta foi" + "SELECT cnum,ccod,saldo FROM ContaTarxeta JOIN Conta" +
					" USING (ccod) WHERE tcod = " + ntarxeta + " order by cnum");
			return null;
		}
	}

	public ArrayList<Movemento> getMovementos(int numeroconta) {
		ResultSet resultSet;
		try {
			//TODO
			resultSet = this.statement.executeQuery("SELECT mcod,tmnome,importe,data FROM Movemento JOIN" +
					" TipoMovemento USING (tmcod) WHERE ccod = " + numeroconta);
			
			ArrayList<Movemento> res = new ArrayList<Movemento>();
			
			while(resultSet.next()){
				res.add(new Movemento(resultSet.getInt(1),resultSet.getInt(3),resultSet.getDate(2),resultSet.getString(4)));
			}
			
			return res;
		} catch (SQLException e) {
			System.err.println(e);
			return null;
		}
	}

	public Conta getConta(String numtarx, int numConta) {
		ResultSet resultSet;
		try {
			resultSet = this.statement.executeQuery("SELECT ccod,saldo FROM ContaTarxeta JOIN Conta" +
					" USING (ccod) where tcod = '" + numtarx + "' AND " + " cnum = " + numConta);
			resultSet.next();
			return new Conta(resultSet.getInt(1),resultSet.getInt(2));
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Tarxeta getTarxeta(String numtarx) {
		ResultSet resultSet;
		try {
			resultSet = this.statement.executeQuery("SELECT tcod from Tarxeta where tcod = '" + numtarx + "'");
			resultSet.next();
			return new Tarxeta(resultSet.getString(1));
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void valoresPorDefecto() {
		try {
			this.statement.executeUpdate("delete from Conta where true");
			this.statement.executeUpdate("delete from Tarxeta where true");
			this.statement.executeUpdate("INSERT INTO Tarxeta VALUES ('pastor42 01')");
			this.statement.executeUpdate("INSERT INTO Tarxeta VALUES ('pastor42 02')");
			this.statement.executeUpdate("INSERT INTO Tarxeta VALUES ('pastor42 03')");
			this.statement.executeUpdate("INSERT INTO Tarxeta VALUES ('pastor42 04')");
			this.statement.executeUpdate("INSERT INTO Tarxeta VALUES ('pastor42 05')");
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
			this.statement.executeUpdate("INSERT INTO ContaTarxeta(tcod,cnum,ccod)  VALUES('pastor42 01',1,1)");
			this.statement.executeUpdate("INSERT INTO ContaTarxeta(tcod,cnum,ccod)  VALUES('pastor42 01',2,2)");
			this.statement.executeUpdate("INSERT INTO ContaTarxeta(tcod,cnum,ccod)  VALUES('pastor42 01',3,3)");
			this.statement.executeUpdate("INSERT INTO ContaTarxeta(tcod,cnum,ccod)  VALUES('pastor42 02',1,1)");
			this.statement.executeUpdate("INSERT INTO ContaTarxeta(tcod,cnum,ccod)  VALUES('pastor42 02',2,3)");
			this.statement.executeUpdate("INSERT INTO ContaTarxeta(tcod,cnum,ccod)  VALUES('pastor42 03',1,4)");
			this.statement.executeUpdate("INSERT INTO ContaTarxeta(tcod,cnum,ccod)  VALUES('pastor42 03',2,5)");
			this.statement.executeUpdate("INSERT INTO ContaTarxeta(tcod,cnum,ccod)  VALUES('pastor42 03',3,6)");
			this.statement.executeUpdate("INSERT INTO ContaTarxeta(tcod,cnum,ccod)  VALUES('pastor42 04',1,7)");
			this.statement.executeUpdate("INSERT INTO ContaTarxeta(tcod,cnum,ccod)  VALUES('pastor42 04',2,8)");
			this.statement.executeUpdate("INSERT INTO ContaTarxeta(tcod,cnum,ccod)  VALUES('pastor42 05',1,0)");
			this.statement.executeUpdate("INSERT INTO ContaTarxeta(tcod,cnum,ccod)  VALUES('pastor42 05',3,9)");	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int getTotalReintegrosSesion(int idSesion) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getTotalAbonosSesion(int idSesion) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getTotalTraspasosSesion(int idSesion) {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setCanal(int sesionid, int ncanal, int nmsg, boolean ocupado){
		try {
			this.statement.executeUpdate("UPDATE Canle SET lastmsg = " + nmsg + " WHERE scod = " + sesionid + " AND cncod = " + ncanal);
			this.statement.executeUpdate("UPDATE Canle SET ocupado = " + (ocupado ? "1" : "0") + " WHERE scod = " + sesionid + " AND cncod = " + ncanal);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Canal getCanal(int sesionid, int ncanal) {
		try {
			ResultSet r = this.statement.executeQuery("SELECT lastmsg, ocupado FROM Canle WHERE scod = " + sesionid +  " AND cncod = " + ncanal);
			
			if(r.next()){
				int lastmsx = r.getInt(1);
				return  new Canal(ncanal, lastmsx == 0 ? null : lastmsx, r.getBoolean(2));
			}else{
				return null;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void facerReintegro(int num_conta, int importe) {
		// TODO Auto-generated method stub
		
	}

	public void facerAbono(int num_conta, int importe) {
		// TODO Auto-generated method stub
		
	}

	public void facerTraspaso(int num_ori, int num_des, int importe) {
		// TODO Auto-generated method stub
		
	}

	public int crearTablasSesion(int numCanles) {
		try {
			this.statement.executeUpdate("INSERT INTO Sesion() VALUES ()");
			
			ResultSet resultSet = this.statement.executeQuery("SELECT max(scod) FROM Sesion");
			resultSet.next();
			int numSesion = resultSet.getInt(1);
					
			for(int i = 1; i <= numCanles; i++){
				this.statement.executeUpdate("INSERT INTO Canle(scod,cncod) VALUES (" + numSesion + ", " + i + ")");
			}
		
			return numSesion;
		
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}

	public void registrarMensaje(String tipo, Integer codSesion, Integer numCanal, Integer numMsx, Integer codMsx, boolean esEnviado, String s) {
		String sql;
		if(codSesion == null){
			sql = "INSERT INTO Mensaxe(tipo, enviado, texto) values ('" + tipo + "', "  + (esEnviado ? "1" : "0") + ", '" + s + "')";
		}else if (numCanal == null || numMsx == null || codMsx == null){
			sql = "INSERT INTO Mensaxe(tipo, scod,enviado,texto) values ('" + tipo + "', " + codSesion + ", "  + (esEnviado ? "1" : "0") + ", '" + s + "')";
		}else{
			sql = "INSERT INTO Mensaxe(tipo, scod,cncod,msnum,enviado,texto) values ( '" + tipo + "', " + codSesion + ", "  + numCanal + ", "  + numMsx + ", "
					+ (esEnviado ? "1" : "0") + ", '" + s + "')";
			
		}
		
		try {
			this.statement.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	public ArrayList<Mensaxe> getMensaxesRecibidas() {
		try {
			ResultSet resultSet = statement.executeQuery("SELECT tipo, cncod, msnum from Mensaxe where enviado = 0 order by mscod");
			
			ArrayList<Mensaxe> res = new ArrayList<Mensaxe>();
			
			while(resultSet.next()){
				String tipo = resultSet.getString(1);
				Integer ncanal = resultSet.getInt(2);
				Integer nmsx = resultSet.getInt(3);
				res.add(new Mensaxe(tipo,ncanal == 0 ? null : ncanal,nmsx == 0 ? null : nmsx, false));
			}
			return res;
		} catch (SQLException e) {
			e.printStackTrace();
			return new ArrayList<Mensaxe>();
		}
	}

	public ArrayList<Mensaxe> getMensaxesEnviadas() {
		try {
			ResultSet resultSet = statement.executeQuery("SELECT tipo, cncod, msnum from Mensaxe where enviado = 1 order by mscod");
			
			ArrayList<Mensaxe> res = new ArrayList<Mensaxe>();
			
			while(resultSet.next()){
				String tipo = resultSet.getString(1);
				Integer ncanal = resultSet.getInt(2);
				Integer nmsx = resultSet.getInt(3);
				res.add(new Mensaxe(tipo,ncanal == 0 ? null : ncanal,nmsx == 0 ? null : nmsx, true));
			}
			return res;
		} catch (SQLException e) {
			e.printStackTrace();
			return new ArrayList<Mensaxe>();
		}
	}

	public ArrayList<Canal> getCanales(int sesionId) {
		if(sesionId == -1) return new ArrayList<Canal>();
		
		try {
			ResultSet resultSet = statement.executeQuery("SELECT cncod, lastmsg FROM Canle WHERE scod = " + sesionId + " ORDER BY cncod");
			
			ArrayList<Canal> res = new ArrayList<Canal>();
			
			while(resultSet.next()){
				res.add(new Canal(resultSet.getInt(1),resultSet.getInt(2)));
			}
			return res;
		} catch (SQLException e) {
			e.printStackTrace();
			return new ArrayList<Canal>();
		}	
	}
	
}