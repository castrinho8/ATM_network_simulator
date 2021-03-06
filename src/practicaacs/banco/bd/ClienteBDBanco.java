/***************************************************************************
 *	ATM Network Simulator ACS. FIC. UDC. 2012/2013									*
 *	Copyright (C) 2013 by Pablo Castro and Marcos Chavarria 						*
 * <pablo.castro1@udc.es>, <marcos.chavarria@udc.es> 								*
 * 																								*
 * This program is free software; you can redistribute it and/or modify 	*
 * it under the terms of the GNU General Public License as published by 	*
 * the Free Software Foundation; either version 2 of the License, or 		*
 * (at your option) any later version. 												*
 ***************************************************************************/
package practicaacs.banco.bd;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import practicaacs.fap.Mensaje;
import practicaacs.fap.MensajeNoValidoException;


public class ClienteBDBanco {
	private Connection con;
	private Statement statement;
	private int execucionId;
	
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
			System.err.println("Error ao conectar ca BD");
			System.exit(-1);
		}
		
		
		try {
			this.statement.executeUpdate("INSERT INTO Execucion() values()");
			ResultSet s = this.statement.executeQuery("SELECT max(ecod) FROM Execucion");	
			s.next();
			this.execucionId = s.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
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
			resultSet = this.statement.executeQuery("SELECT mcod,tmnome,importe, DATE_FORMAT(data, '%Y-%m-%d %H:%i:%S'),tmcod FROM Movemento JOIN" +
					" TipoMovemento USING (tmcod) WHERE ccod = " + numeroconta);
			
			ArrayList<Movemento> res = new ArrayList<Movemento>();
			
			while(resultSet.next()){
				java.util.Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(resultSet.getString(4));
				res.add(new Movemento(resultSet.getInt(1),resultSet.getInt(3),date,resultSet.getString(2),resultSet.getInt(5)));
			}
			
			return res;
		} catch (SQLException e) {
			System.err.println(e);
			return null;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public Conta getConta(String numtarx, int numConta) {
		ResultSet resultSet;
		try {
			resultSet = this.statement.executeQuery("SELECT ccod,saldo FROM ContaTarxeta JOIN Conta" +
					" USING (ccod) where tcod = '" + numtarx + "' AND " + " cnum = " + numConta);
			if(!resultSet.next())
				return null;
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
			this.statement.executeUpdate("INSERT INTO Tarxeta VALUES ('banco001001')");
			this.statement.executeUpdate("INSERT INTO Tarxeta VALUES ('banco001002')");
			this.statement.executeUpdate("INSERT INTO Tarxeta VALUES ('banco001003')");
			this.statement.executeUpdate("INSERT INTO Tarxeta VALUES ('banco001004')");
			this.statement.executeUpdate("INSERT INTO Tarxeta VALUES ('banco001005')");

			this.statement.executeUpdate("INSERT INTO Conta(ccod,saldo) VALUES (0,0000)");
			this.statement.executeUpdate("INSERT INTO Conta(ccod,saldo) VALUES (1,1000)");
			this.statement.executeUpdate("INSERT INTO Conta(ccod,saldo) VALUES (2,2000)");
			this.statement.executeUpdate("INSERT INTO Conta(ccod,saldo) VALUES (3,3000)");
			this.statement.executeUpdate("INSERT INTO Conta(ccod,saldo) VALUES (4,4000)");
			this.statement.executeUpdate("INSERT INTO Conta(ccod,saldo) VALUES (5,5000)");
			this.statement.executeUpdate("INSERT INTO Conta(ccod,saldo) VALUES (6,6000)");
			this.statement.executeUpdate("INSERT INTO Conta(ccod,saldo) VALUES (7,7000)");
			this.statement.executeUpdate("INSERT INTO Conta(ccod,saldo) VALUES (8,8000)");
			this.statement.executeUpdate("INSERT INTO Conta(ccod,saldo) VALUES (9,9000)");

			this.statement.executeUpdate("INSERT INTO ContaTarxeta(tcod,cnum,ccod)  VALUES('banco001001',1,1)");
			this.statement.executeUpdate("INSERT INTO ContaTarxeta(tcod,cnum,ccod)  VALUES('banco001001',2,2)");
			this.statement.executeUpdate("INSERT INTO ContaTarxeta(tcod,cnum,ccod)  VALUES('banco001001',3,3)");
			this.statement.executeUpdate("INSERT INTO ContaTarxeta(tcod,cnum,ccod)  VALUES('banco001002',1,1)");
			this.statement.executeUpdate("INSERT INTO ContaTarxeta(tcod,cnum,ccod)  VALUES('banco001002',2,3)");
			this.statement.executeUpdate("INSERT INTO ContaTarxeta(tcod,cnum,ccod)  VALUES('banco001003',1,4)");
			this.statement.executeUpdate("INSERT INTO ContaTarxeta(tcod,cnum,ccod)  VALUES('banco001003',2,5)");
			this.statement.executeUpdate("INSERT INTO ContaTarxeta(tcod,cnum,ccod)  VALUES('banco001003',3,6)");
			this.statement.executeUpdate("INSERT INTO ContaTarxeta(tcod,cnum,ccod)  VALUES('banco001004',1,7)");
			this.statement.executeUpdate("INSERT INTO ContaTarxeta(tcod,cnum,ccod)  VALUES('banco001004',2,8)");
			this.statement.executeUpdate("INSERT INTO ContaTarxeta(tcod,cnum,ccod)  VALUES('banco001005',1,0)");
			this.statement.executeUpdate("INSERT INTO ContaTarxeta(tcod,cnum,ccod)  VALUES('banco001005',3,9)");	
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public int getTotalReintegrosSesion(int idSesion) {
		try {
			ResultSet s = this.statement.executeQuery("SELECT treintegros FROM Sesion WHERE scod = " + idSesion);
			s.next();
			return s.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}

	public int getTotalAbonosSesion(int idSesion) {
		try {
			ResultSet s = this.statement.executeQuery("SELECT tabono FROM Sesion WHERE scod = " + idSesion);
			s.next();
			return s.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}

	public int getTotalTraspasosSesion(int idSesion) {
		try {
			ResultSet s = this.statement.executeQuery("SELECT ttraspasos FROM Sesion WHERE scod = " + idSesion);
			s.next();
			return s.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}

	public void setCanal(int sesionid, int ncanal, int nmsg, boolean ocupado, boolean recuperado){
		try {
			this.statement.executeUpdate("UPDATE Canle SET lastmsg = " + nmsg + " WHERE scod = " + sesionid + " AND cncod = " + ncanal);
			this.statement.executeUpdate("UPDATE Canle SET ocupado = " + (ocupado ? "1" : "0") + " WHERE scod = " + sesionid + " AND cncod = " + ncanal);
			this.statement.executeUpdate("UPDATE Canle SET recuperado = " + (recuperado ? "1" : "0") + " WHERE scod = " + sesionid + " AND cncod = " + ncanal);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Canal getCanal(int sesionid, int ncanal) {
		try {
			ResultSet r = this.statement.executeQuery("SELECT lastmsg, ocupado, recuperado FROM Canle WHERE scod = " + sesionid +  " AND cncod = " + ncanal);
			
			if(r.next()){
				int lastmsx = r.getInt(1);
				return  new Canal(ncanal, lastmsx == 0 ? null : lastmsx, r.getBoolean(2),r.getBoolean(3));
			}else{
				return null;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	private void updateConta(int num_conta, int importe) throws SQLException{
		this.statement.executeUpdate("UPDATE Conta SET saldo = saldo " + (importe > 0 ? " + " + importe : " - " + (-importe)) + 
					" WHERE ccod = " + num_conta);
	}
	
	private void addMovemento(int num_conta, int tipo_mensaxe, int importe) throws SQLException{
		
		ResultSet r = this.statement.executeQuery("select max(mcod) from Movemento where ccod = " + num_conta);
		
		r.next();
		int num_mov = r.getInt(1); 
		
		this.statement.executeUpdate("INSERT INTO Movemento(mcod,ccod,data,importe,tmcod) values("+ (num_mov +1) + ", " + num_conta +
				", NOW(), " + importe + ", " + tipo_mensaxe + ")");
	}
	
	public void facerReintegro(int sesion, int num_conta, int importe) {
		try {
			this.updateConta(num_conta, -importe);
			this.addMovemento(num_conta, 10, importe);
			this.statement.executeUpdate("UPDATE Sesion SET treintegros = treintegros + " + importe +
					" WHERE scod = " + sesion);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public void facerAbono(int sesion, int num_conta, int importe) {
		try {
			this.updateConta(num_conta, importe);
			this.addMovemento(num_conta, 50, importe);
			this.statement.executeUpdate("UPDATE Sesion SET tabono = tabono + " + importe +
					" WHERE scod = " + sesion);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void facerTraspaso(int sesion, int num_ori, int num_des, int importe) {
		try {
			this.updateConta(num_ori, -importe);
			this.addMovemento(num_ori, 11, importe);
			this.updateConta(num_des, importe);
			this.addMovemento(num_des, 12, importe);
			this.statement.executeUpdate("UPDATE Sesion SET ttraspasos = ttraspasos + " + importe +
					" WHERE scod = " + sesion);
		} catch (SQLException e) {
			e.printStackTrace();
		}

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
			sql = "INSERT INTO Mensaxe(ecod, tipo, enviado, texto) values (" + this.execucionId + ", '" + tipo + "', "  + (esEnviado ? "1" : "0") + ", '" + s + "')";
		}else if (numCanal == null || numMsx == null || codMsx == null){
			sql = "INSERT INTO Mensaxe(ecod, tipo, scod,enviado,texto) values (" + this.execucionId + ", '" + tipo + "', " + codSesion + ", "  + (esEnviado ? "1" : "0") + ", '" + s + "')";
		}else{
			sql = "INSERT INTO Mensaxe(ecod, tipo, scod,cncod,msnum,enviado,texto) values (" + this.execucionId + ", '" + tipo + "', " + codSesion + ", "  + numCanal + ", "  + numMsx + ", "
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
			ResultSet resultSet = statement.executeQuery("SELECT tipo, cncod, msnum from Mensaxe WHERE enviado = 0  " +
					"AND ecod = " + this.execucionId + " ORDER BY mscod");
			
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
			ResultSet resultSet = statement.executeQuery("SELECT tipo, cncod, msnum from Mensaxe WHERE enviado = 1  " +
					"AND ecod = " + this.execucionId + " ORDER BY mscod");
			
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
			ResultSet resultSet = statement.executeQuery("SELECT cncod, lastmsg, ocupado, recuperado FROM Canle WHERE scod = " + sesionId + " ORDER BY cncod");
			
			ArrayList<Canal> res = new ArrayList<Canal>();
			
			while(resultSet.next()){
				res.add(new Canal(resultSet.getInt(1),resultSet.getInt(2),resultSet.getBoolean(3), resultSet.getBoolean(4)));
			}
			return res;
		} catch (SQLException e) {
			e.printStackTrace();
			return new ArrayList<Canal>();
		}	
	}
	
	public boolean mensaxeRespondida(int id_sesion,int canle ){

		try {
			ResultSet resultSet = statement.executeQuery("SELECT enviado,mscod FROM Mensaxe WHERE scod = " + id_sesion + " AND cncod = " + canle + " ORDER BY mscod DESC");
			
			//Avanzamos un valor
			resultSet.next();
			
			if(resultSet.next()){
				System.out.println(resultSet.getBoolean(1)+"-"+resultSet.getInt(2));
				return resultSet.getBoolean(1);
			}
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public Mensaje getUltimaMensaxe(int id_sesion,int canle){

		Mensaje m = null;
		ResultSet resultSet;
		try {
			resultSet = statement.executeQuery("SELECT texto FROM Mensaxe WHERE scod = " + id_sesion + " AND cncod = " + canle + " ORDER BY mscod DESC");
			
			//Avanzamos un valor
			resultSet.next();
			
			if(resultSet.next()){
				String texto = resultSet.getString(1);
				m = Mensaje.parse(texto);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (MensajeNoValidoException e) {
			e.printStackTrace();
		}
		return m;
	}
	
	
}
