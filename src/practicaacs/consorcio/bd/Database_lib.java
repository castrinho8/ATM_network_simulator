package practicaacs.consorcio.bd;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;

import practicaacs.banco.estados.EstadoSesion;
import practicaacs.banco.estados.SesAberta;
import practicaacs.banco.estados.SesNonAberta;
import practicaacs.consorcio.aux.Movimiento;
import practicaacs.consorcio.aux.TipoOrigDest;
import practicaacs.fap.*;

//Libreria de acceso a la base de datos
public class Database_lib {
	
	static private Database_lib instancia;

	private Connection con;
	private Statement statement = null;
	
	private Database_lib() {
		
    	//Obtenemos los datos del fichero properties
		Properties prop = new Properties();
		InputStream is;
		//LA SITUACION DEL FICHERO DE CONFIGURACION
		//String file = "/home/ch01/UNI/ACS/RepositorioPractica/res/consorcioBD.properties";
		String file = "/home/castrinho8/Escritorio/UNI/ACS/res/consorcioBD.properties";

		try {
			is = new FileInputStream(file);
		    prop.load(is);
		} catch (FileNotFoundException e) {
			System.err.println("Non se encontrou arquivo de configuracion " + file + ".");
			System.exit(-1);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String bdname = prop.getProperty("consorcio.bd.name");
		String bdadd = prop.getProperty("consorcio.bd.add");
		String bduser = prop.getProperty("consorcio.bd.user");
		String bdpass = prop.getProperty("consorcio.bd.pass");
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.err.println("Error creando o driver.");
			e.printStackTrace();
		}

		try {
			con = DriverManager.getConnection("jdbc:mysql://" + bdadd + "/" + bdname + "?user=" + bduser + "&password=" + bdpass);
			statement = con.createStatement();
		} catch (SQLException e) {
			System.err.println("Error ao conectar ca BD. " + e.getMessage());
			e.printStackTrace();
		}	
	}
	
	/**
	 * Devuelve la instancia del singleton
	 * @return El singleton de acceso a la base de datos
	 */
	static public Database_lib getInstance(){
		if(instancia == null)
			instancia = new Database_lib();
		
		return instancia;
	}
	
	/**
	 * Método que comprueba si existe la pareja Tarjeta,Cuenta en la BD. En caso de que no exista la inserta.
	 * @param tarjeta El string correspondiente a la tarjeta.
	 * @param cuenta EL int correspondiente a la cuenta.
	 */
	public void comprueba_cuenta(String tarjeta,int cuenta){
		
		ResultSet resultSet;
		//Obtiene la cuenta de la BD
		try {
			resultSet = this.statement.executeQuery("SELECT codCuenta FROM Cuenta" +  
				" WHERE codTarjeta = '" + tarjeta + "' AND codCuenta = " + cuenta);
			
			if(resultSet.next())
				return;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		//En caso de que no haya, se introduce en la BD
		try {
			this.statement.executeUpdate("INSERT INTO Cuenta(codTarjeta,codCuenta,cusaldo)" +
					" VALUES('" + tarjeta + "'," + cuenta + ",0)");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Método que comprueba si existe la tarjeta en la BD. En caso de que no exista la inserta.
	 * @param tarjeta El string correspondiente a la tarjeta.
	 */
	public void comprueba_tarjeta(String tarjeta){
		
		ResultSet resultSet;
		//Obtiene la tarjeta de la BD
		try {
			resultSet = this.statement.executeQuery("SELECT codTarjeta FROM Tarjeta" +  
				" WHERE codTarjeta = '" + tarjeta + "'");
			
			if(resultSet.next())
				return;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		//En caso de que no haya, se introduce en la BD
		try {
			this.statement.executeUpdate("INSERT INTO Tarjeta(codTarjeta,tagastoOffline)" +
					" VALUES('" + tarjeta + "',0)");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/*---------------------------------------------------
	 ---------------- CONSULTAS EN CUENTAS -----------------------
	 ----------------------------------------------------*/	
	
	public CodigosRespuesta comprobar_condiciones(String tarjeta, int cuenta_origen, int cuenta_destino,
			CodigosMensajes tipo, int importe){
		
		if (this.consultarGastoOffline(tarjeta) > 1000)
			return CodigosRespuesta.IMPORTEEXCLIMITE;

		if ((tipo.equals(CodigosMensajes.SOLTRASPASO)) && (importe > 9999))
			return CodigosRespuesta.IMPORTEEXCLIMITE;

		if ((tipo.equals(CodigosMensajes.SOLTRASPASO)) && (cuenta_origen == cuenta_destino))
			return CodigosRespuesta.TRANSCUENTASIGUALES;

		if ((tipo.equals(CodigosMensajes.SOLTRASPASO)) && (this.consultar_saldo(tarjeta,cuenta_origen) < importe))
			return CodigosRespuesta.TRANSSINFONDOS;

			return CodigosRespuesta.CONSACEPTADA;
/*		CONSDEN(10,"Consulta Denegada."), 
		CAPTARJ(11,"Consulta Denegada con Captura de Tarjeta."), 
		TARJETANVALIDA(12,"Consulta Denegada, Tarjeta no Válida."),
		CUENTANVALIDA(13,"Consulta Denegada, Cuenta especificada no válida."), 
		TRANSCUENTAORINVALIDA(23,"Consulta Denegada, En operación de Traspaso la Cuenta Origen no es válida."), 
		TRANSCUENTADESNVALIDA(24,"Consulta Denegada, En operación de Traspaso la Cuenta Destino no es válida.");
	*/	
	}
	
	
	/**
	 * Actualiza el gasto offline para la tarjeta indicada.
	 * @param tarjeta La tarjeta para actualizar.
	 * @param importe El importe a sumar.
	 */
	public void actualiza_GastoOffline(String tarjeta,int importe){
		try {
			this.statement.executeUpdate("UPDATE Tarjeta SET tagastoOffline=tagastoOffline+" + importe +  
				" WHERE codTarjeta = '" + tarjeta + "'");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * Recalcula el saldo actual de la cuenta indicada.
	 * @param cuenta La cuenta a actualizar el saldo.
	 * @param importe El importe a modificar.
	 * @param signo El signo que indica si se debe sumar o restar.
	 */
	public void recalcular_saldoActual(int cuenta,String tarjeta, int importe, char signo){
		try {
			this.statement.executeUpdate("UPDATE Cuenta SET cusaldo = cusaldo"+ signo + importe +
				" WHERE codCuenta = " + cuenta + " AND codTarjeta = '" + tarjeta + "'");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Obtiene el saldo a partir del atributo que hay en cuenta.
	 * @param cuenta La cuenta a consultar.
	 * @return Devuelve el saldo actual de la cuenta y null en caso de error
	 */
	public int consultar_saldo(String tarjeta,int cuenta){
		
		ResultSet resultSet;
		try{
			resultSet = this.statement.executeQuery("SELECT cusaldo FROM Cuenta " +
					"WHERE codCuenta=" + cuenta + " AND codTarjeta = '" + tarjeta + "'");
			if(resultSet.next())
				return resultSet.getInt(1);
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

	
	/**
	 * Método que realiza una consulta de movimientos para la cuenta indicada.
	 * @param tarjeta La tarjeta a consultar.
	 * @param cuenta La cuenta a consultar.
	 * @return Un arrayList con los movimientos existentes.
	 */
	public ArrayList<Movimiento> consultar_movimientos(String tarjeta,int cuenta){
		
		ResultSet resultSet;
		try{
			resultSet = this.statement.executeQuery("SELECT codMovimiento,moimporte,mofecha,codTMovimiento" +
					" FROM Movimiento " +
					"WHERE ((codCuentaOrig = " + cuenta + " AND codTarjeta = '" + tarjeta +
					"') || (codCuentaDest = " + cuenta + " AND codTarjeta = '" + tarjeta + "'))");
			
			ArrayList<Movimiento> res = new ArrayList<Movimiento>();
			Date fecha = null;
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

			while(resultSet.next()){
				//Obtiene el tipo
				CodigosMovimiento tipo = null;
				try {
					tipo = CodigosMovimiento.getTipoMovimiento(resultSet.getInt(4));
				} catch (CodigoNoValidoException e) {
					System.out.println("Codigo de movimiento no valido");
					e.printStackTrace();
				}
				
				try {
					String sqlDate = sdf.format(resultSet.getDate(3));
				    fecha = sdf.parse(sqlDate);
				} catch (ParseException ex) {
				    ex.printStackTrace();
				}
				res.add(new Movimiento(resultSet.getInt(1),CodigosMovimiento.getSigno(tipo),resultSet.getInt(2),fecha,tipo));
			}
			return res;
		}catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	
	/**
	 * Método que realiza un reintegro en la BD.
	 * Añade el movimiento a la tabla MOVIMIENTO, actualiza el saldo actual de la CUENTA y 
	 * si es offline
	 * @param tarjeta La tarjeta en la que realizar la operacion.
	 * @param cuenta La cuenta en la que realizar la operacion.
	 * @param importe El importe del que realizar el reintegro.
	 * @param codonline Valor booleano que indica true si es online y false si es offline
	 * @return El saldo actual de la cuenta despues de realizar la operacion.
	 */
	public int realizar_reintegro(String tarjeta,int cuenta,int importe,boolean codonline){

		//Actualizamos el gasto offline si es Offline
		if(!codonline)
			this.actualiza_GastoOffline(tarjeta, importe);
		
		System.out.println("tarjeta:" + tarjeta);
		String id_banco = "";
		try{
			id_banco = tarjeta.substring(0,tarjeta.length()-3);
		}catch(IndexOutOfBoundsException i){
			i.printStackTrace();
			return -1;
		}
		
		//Insertamos en la tabla MOVIMIENTO
		this.insertar_movimiento(tarjeta,cuenta,-1,10,importe,codonline,id_banco);
		
		//Recalculamos el saldo actual de la CUENTA
		this.recalcular_saldoActual(cuenta,tarjeta, importe,'-');
		
		//Devolvemos el saldo actual de la cuenta
		int res = this.consultar_saldo(tarjeta,cuenta);
		
		return res;
	}
	
	
	/**
	 * Método que realiza un traspaso entre dos cuentas en la BD
	 * @param tarjeta La tarjeta correspondiente a la cuenta que realiza el movimiento.
	 * @param cuenta_origen La cuenta que realiza el traspaso.
	 * @param cuenta_destino La cuenta que recibe el traspaso.
	 * @param importe El importe a traspasar.
	 * @return El nuevo saldo de la cuenta destino.
	 */
	public int realizar_traspaso(String tarjeta,int cuenta_origen,int cuenta_destino,boolean codonline,int importe){
		
		//Actualizamos el gasto offline si es Offline
		if(!codonline)
			this.actualiza_GastoOffline(tarjeta, importe);
		
		System.out.println("tarjeta:" + tarjeta);
		String id_banco = "";
		try{
			id_banco = tarjeta.substring(0,tarjeta.length()-3);
		}catch(IndexOutOfBoundsException i){
			i.printStackTrace();
			return -1;
		}
		
		//Insertamos en la tabla MOVIMIENTO
		this.insertar_movimiento(tarjeta,cuenta_origen,cuenta_destino,11,importe,codonline,id_banco);

		//Recalculamos el saldo actual de las CUENTAS
		this.recalcular_saldoActual(cuenta_origen,tarjeta, importe,'-');
		this.recalcular_saldoActual(cuenta_destino,tarjeta, importe,'+');

		//Devolvemos el saldo actual de la cuenta
		int res = this.consultar_saldo(tarjeta,cuenta_destino);
		
		return res;
	}
	
	

	/**
	 * Método que realiza un abono en la cuenta indicada. 
	 * @param tarjeta La tarjeta que realiza el abono.
	 * @param cuenta La cuenta en la que se abona el importe.
	 * @param importe La cantidad a abonar.
	 * @return El nuevo saldo actual de la cuenta despues de realizar el abono.
	 */
	public int realizar_abono(String tarjeta, int cuenta,boolean codonline,int importe){
		
		//Actualizamos el gasto offline si es Offline
		if(!codonline)
			this.actualiza_GastoOffline(tarjeta, importe);
		
		System.out.println("tarjeta:" + tarjeta);
		String id_banco = "";
		try{
			id_banco = tarjeta.substring(0,tarjeta.length()-3);
		}catch(IndexOutOfBoundsException i){
			i.printStackTrace();
			return -1;
		}
		
		//Insertamos en la tabla MOVIMIENTO
		this.insertar_movimiento(tarjeta,cuenta,-1,50,importe,codonline,id_banco);

		//Recalculamos el saldo actual de la CUENTA
		this.recalcular_saldoActual(cuenta,tarjeta, importe,'+');
		
		//Devolvemos el saldo actual de la cuenta
		int res = this.consultar_saldo(tarjeta,cuenta);
		
		return res;
	}
	
	
	/*---------------------------------------------------
	 ---------------- MOVIMIENTOS -----------------------
	 ----------------------------------------------------*/

	/**
	 * Método general que obtiene la suma de todos los importes para el tipo de movimiento introducido.
	 * @param codigo_mov El codigo correspondiente al tipo de movimiento.
	 * @param id_banco El banco del cual obtener la suma de los movimientos.
	 * @return Un int con el sumatorio de los importes de todos los movimientos.
	 */
	private int get_sumaTipoMovimiento(int codigo_mov, String id_banco){
		ResultSet resultSet;
		try{
			resultSet = this.statement.executeQuery("SELECT SUM(moimporte) FROM Movimiento " +
				"WHERE codTMovimiento = " + codigo_mov + " AND codBanco = '"+ id_banco +"'");
		
			if(resultSet.next())
				return resultSet.getInt(1);
		}catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
		return 0;
	}
	
	/**
	 * Recorre la tabla de movimientos sumando los reintegros para el banco 
	 * indicado por parámetro
	 * @param id_banco El banco en donde consultar los reintegros.
	 * @return El resultado de la suma de todos los reintegros
	 */
	public int getNumReintegros(String id_banco) {
		return get_sumaTipoMovimiento(10,id_banco);
	}

	/**
	 * Recorre la tabla de movimientos sumando los traspasos para el banco 
	 * indicado por parámetro
	 * @param id_banco El banco en donde consultar los traspasos.
	 * @return El resultado de la suma de todos los traspasos
	 */
	public int getNumTraspasos(String id_banco) {
		return get_sumaTipoMovimiento(11,id_banco);// o 12?
	}

	/**
	 * Recorre la tabla de movimientos sumando los abonos para el banco 
	 * indicado por parámetro
	 * @param id_banco El banco en donde consultar los abonos.
	 * @return El resultado de la suma de todos los abonos
	 */
	public int getNumAbonos(String id_banco) {
		return get_sumaTipoMovimiento(50,id_banco);
	}
	
	/**
	 * Método que realiza una consulta del gasto offline para la tarjeta indicada.
	 * Recorre la tabla de MOVIMIENTO y suma todos los gastos offline correspondientes 
	 * a las cuentas de la tarjeta.
	 * @param tarjeta La tarjeta a consultar
	 * @return Un entero con el gasto oofline.
	 */
	public int consultarGastoOffline(String tarjeta){

		ResultSet resultSet;
		try{
			resultSet = this.statement.executeQuery("SELECT tagastoOffline FROM Tarjeta WHERE codTarjeta = '" + tarjeta + "'");
			
			if(resultSet.next())
				return resultSet.getInt(1);
			
		}catch (SQLException e) {
				e.printStackTrace();
				return 0;
		}
		return 0;
	}
	
	/**
	 * Método que inserta un movimiento en la tabla MOVIMIENTO.
	 * Admite nulos en cualquier campo excepto en el codigo online.
	 * @param cuenta_orig La cuenta origen.
	 * @param cuenta_dest La cuenta destino. 
	 * @param cod_tmovimiento El tipo del movimiento.
	 * @param importe El importe del movimiento.
	 * @param codonline El booleando que indica si es online o offline.
	 * @param banco El número del banco.
	 */
	private void insertar_movimiento(String tarjeta,int cuenta_orig,int cuenta_dest,int cod_tmovimiento,
		int importe,boolean codonline,String banco){
		
		//Obtiene el tiempo actual y lo añade con el formato indicado
	  	Calendar time = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		//Obtiene codigo que identifica al banco en la BD a partir del string del banco
		int id_banco = 0;
		try{
			id_banco = getIdBanco(banco);
		}catch(ConsorcioBDException c){
			c.printStackTrace();
			System.exit(-1);
		}
		
		//Si no nay banco, no hacer nada
		if(id_banco<0)
			return;
		
		//Comprobamos si existen la tarjeta y las cuentas en la BD, sino, las insertamos
		comprueba_tarjeta(tarjeta);
		if(cuenta_orig>0)
			comprueba_cuenta(tarjeta,cuenta_orig);
		if(cuenta_dest>0)
			comprueba_cuenta(tarjeta,cuenta_dest);
		
		String cuentas = ((cuenta_orig<0)? "NULL":cuenta_orig) + "," + ((cuenta_dest<0)? "NULL":cuenta_dest);
		String fecha = sdf.format(time.getTime());
		
		System.out.println("INSERT INTO Movimiento" +
				"(codTarjeta,codCuentaOrig,codCuentaDest,codTMovimiento,mofecha,moimporte,mooffline,codBanco)" +
				" VALUES ('" + tarjeta + "'," + cuentas + "," + cod_tmovimiento + "," + fecha + "," +
				importe + "," + ((codonline)? 0:1) + "," + id_banco + ")");
		
		try {
			this.statement.executeUpdate("INSERT INTO Movimiento" +
				"(codTarjeta,codCuentaOrig,codCuentaDest,codTMovimiento,mofecha,moimporte,mooffline,codBanco)" +
				" VALUES ('" + tarjeta + "'," + cuentas + "," + cod_tmovimiento + ", STR_TO_DATE('" + fecha + "','%d/%m/%Y')," +
				importe + "," + ((codonline)? 0:1) + "," + id_banco + ")");
		} catch (SQLException e) {
			System.out.println("Error insertando movimiento.");
			e.printStackTrace();
		}
	}
	
	
	/*---------------------------------------------------
	 ----------------- SESIONES/BANCOS -------------------
	 ----------------------------------------------------*/
	
	/**
	 * Método que abre una sesion con el banco indicado.
	 * Comprueba si ya esta, en ese caso setea el estado a ACTIVA, sino añade una linea a la tabla
	 * de BANCO.
	 * @param id_banco El identificador a añadir.
	 * @param ip La ip en la que se encuentra el servidor del banco. Ej:'127.0.0.1'
	 * @param puerto El puerto en el que se encuentra el servidor del banco.
	 * @param num_canales El número de canales máximo que el banco puede usar.
	 */
	public void abrir_sesion(String id_banco, String ip, int puerto, int num_canales){
		
		ResultSet resultSet;
		try{
			resultSet = this.statement.executeQuery("SELECT codBanco FROM Banco " +
					"WHERE codBanco = '" + id_banco + "'");
			
			if(resultSet.next()){
				//Si ya está el banco, settear la sesion a abierta
				if(resultSet.getString(1).equals(id_banco)){
					setEstado_conexion_banco(id_banco,SesAberta.instance());
				}
			}else{
				//Añadir BANCO a la BD
				insertar_banco(id_banco,1,puerto,ip,num_canales);
			}
			
			borrar_canales(id_banco);

			int id_canal = 0;
			//Añadir todos los CANALES del BANCO
			for(id_canal=0;id_canal<num_canales;id_canal++)
				anhadir_canal(id_banco,id_canal);
			
		}catch (SQLException e) {
				System.out.println("Error abriendo sesion.");
				e.printStackTrace();
		}
	}
	
	/**
	 * Método que cierra una sesión con el banco indicado.
	 * Comprueba si está la sesión, si está setea el estado a CERRADA, sino no hace nada.
	 * @param id_banco El identificador del banco a cerrar.
	 */
	public void cerrar_sesion(String id_banco){

		ResultSet resultSet;
		try{
			resultSet = this.statement.executeQuery("SELECT codigo FROM Banco " +
					"WHERE codbanco = '" + id_banco +"'");
			
			if(resultSet.next()){
				//Si hay un banco, settear la sesion a cerrada y borrar canales
				this.setEstado_conexion_banco(id_banco,SesNonAberta.instance());
				borrar_canales(id_banco);
			}
		}catch (SQLException e) {
			System.out.println("Error cerrando sesion.");
			e.printStackTrace();
		}
	}
	
	/**
	 * Comprueba en BANCO si el banco introducido por parámetro tiene sesión.
	 * @param id_banco El banco a buscar.
	 * @return True si la sesion es ACTIVA y False en caso contrario.
	 */
	public boolean hasSesion(String id_banco){
		
		ResultSet resultSet;
		int res = 0;
		try {
		
			resultSet = this.statement.executeQuery("SELECT count(*) FROM Banco" +
					" WHERE codBanco = '" + id_banco + "' AND codEBanco = 1");
			
			if(resultSet.next())
				res = resultSet.getInt(1);
			
			return (res==1);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Comprueba si la SESION admite envios o no.
	 * @param id_banco El banco a buscar.
	 * @return True si los admite y False en caso contrario (si el estado es trafico detenido o cerrada)
	 */
	public boolean consultar_protocolo(String id_banco){

		ResultSet resultSet;
		int res = 0;
		try {
			resultSet = this.statement.executeQuery("SELECT count(*) FROM Banco" +
					" WHERE codBanco = '" + id_banco + "' AND ((codEBanco = 1 ) || (codEBanco = 4))");
			
			if(resultSet.next())
				res = resultSet.getInt(1);
			
			return (res==1);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	//------GETTERS Y SETTERS
	
	/**
	 * Obtiene el codigo real que identifica al primer banco cuyo codBanco es igual al banco pasado por parametro.
	 * @param banco El string con el codigo del banco
	 * @return El int con el codigo que lo identifica en la BD
	 * @throws ConsorcioBDException 
	 */
	public int getIdBanco(String banco) throws ConsorcioBDException{
		ResultSet resultSet;
		try {
			resultSet = this.statement.executeQuery("SELECT codigo FROM Banco" +
					" WHERE codBanco = '" + banco + "'");
			
			if(resultSet.next())
				return resultSet.getInt(1);
			else
				throw new ConsorcioBDException("El banco de id:"+banco+" no existe en la BD del consorcio.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	/**
	 * Getter en BANCO del estado de la conexion con el banco.
	 * @param id_banco El banco a buscar.
	 * @return El estado de la conexion correspondiente.
	 */
	public EstadoSesion getEstado_conexion_banco(String id_banco){
		
		ResultSet resultSet;
		int codEBanco = 0;
		try {
			resultSet = this.statement.executeQuery("SELECT codEBanco FROM Banco" +
					" WHERE codBanco = '" + id_banco + "'");
			
			if(resultSet.next())
				codEBanco = resultSet.getInt(1);
			
			return EstadoSesion.getEstadoSesion_fromInt(codEBanco);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Getter en BANCO del numero de canales
	 * @param id_banco El banco a buscar.
	 * @return El número máximo de canales del banco.
	 */
	public int getNum_canales(String id_banco){
		
		ResultSet resultSet;
		try {
			resultSet = this.statement.executeQuery("SELECT bamaxCanales FROM Banco" +
					" WHERE codBanco = '" + id_banco + "'");
			
			if(resultSet.next())
				return resultSet.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
		return 0;
	}
	
	/**
	 * Getter en BANCO del puerto del banco
	 * @param id_banco El banco a buscar.
	 * @return El puerto correspondiente. 
	 */
	public int getPortBanco(String id_banco){

		ResultSet resultSet;
		try {
			resultSet = this.statement.executeQuery("SELECT bapuerto FROM Banco" +
					" WHERE codBanco = '" + id_banco +"'");
			
			if(resultSet.next())
				return resultSet.getInt(1);
			
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
		return 0;
	}
	
	/**
	 * Getter en BANCO de la ip del banco.
	 * @param id_banco El banco a buscar.
	 * @return La ip correspondiente. 
	 * @throws UnknownHostException 
	 */
	public InetAddress getIpBanco(String id_banco){
		
		ResultSet resultSet;
		String temp = "";
		try {
			resultSet = this.statement.executeQuery("SELECT baip FROM Banco" +
					" WHERE codBanco = '" + id_banco + "'");
			
			if(resultSet.next())
				temp = resultSet.getString(1);
				
			return InetAddress.getByName(temp);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} catch (UnknownHostException ex){
			ex.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Método que obtiene una lista con los identificadores de todos los BANCO que tienen sesión abierta.
	 * @return Un array list de String con los id de todos los BANCOS que tienen sesion abierta.
	 */
	public ArrayList<String> getSesiones(int codigo_estado) {
		
		ResultSet resultSet;
		try {
			resultSet = this.statement.executeQuery("SELECT codBanco FROM Banco " +
					"WHERE codEBanco = "+codigo_estado);
			
			ArrayList<String> res = new ArrayList<String>();
			
			while(resultSet.next()){
				res.add(resultSet.getString(1));
			}
			
			return res;
		} catch (SQLException e) {
			System.err.println(e);
			return null;
		}
	}
	
	/**
	 * Setter en BANCO del estado de la conexion
	 * @param id_banco El banco al que cambiar el estado.
	 * @param estado El nuevo estado.
	 */
	public void setEstado_conexion_banco(String id_banco,EstadoSesion estado){
		try {
			//Obtiene el valor a introducir en la BD del estado correspondiente.
			int state = EstadoSesion.getInt_fromEstadoSesion(estado);
			
			this.statement.executeUpdate("UPDATE Banco SET codEbanco = " + state + 
					" WHERE codBanco = '" + id_banco + "'");
			
		} catch (SQLException e) {
			System.out.println("Error seteando el estado de la conexión del banco.");
			e.printStackTrace();
		}
	}
	
	/**
	 * Setter en BANCO del puerto
	 * @param id_banco El banco al que cambiar el puerto.
	 * @param puerto El nuevo puerto.
	 */
	public void setPuertoBanco(String id_banco, String puerto){

		try {
			int port = Integer.getInteger(puerto);

			this.statement.executeUpdate("UPDATE Banco SET bapuerto = " + port + 
					" WHERE codBanco = '" + id_banco + "'");
			
		} catch (SQLException e) {
			System.out.println("Error seteando el puerto del banco.");
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * Método que inserta un BANCO
	 * @param id_banco El id del banco
	 * @param estado El estado en el que se encuentra.
	 * @param puerto El puerto en el que escucha el banco.
	 * @param ip La ip en la que se encuentra el banco.
	 * @param num_canales El numero maximo de canales.
	 */
	private void insertar_banco(String id_banco, int estado ,int puerto,String ip,int num_canales){
		
		System.out.println("INSERTAR BANCO: "+id_banco+"-"+estado+"-"+puerto+"-"+ip+"-"+num_canales);
		try {
			this.statement.executeUpdate("INSERT INTO Banco(codBanco,codEBanco,bapuerto,baip,bamaxCanales)" +
			" VALUES('" + id_banco + "'," + estado + "," + puerto + ",'" + ip + "'," + num_canales+")");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	/*---------------------------------------------------
	 -------------------- CANALES -----------------------
	 ----------------------------------------------------*/


	/**
	 * Obtiene en CANAL el siguiente canal disponible para realizar un envio.
	 * El algoritmo de selección consiste simplemente en obtener el que tenga el número 
	 * mínimo de canal de los que están libres.
	 * @param id_banco El banco de donde obtener un nuevo canal.
	 * @return Un entero con el canal correspondiente.
	 */
	public int seleccionarCanal(String id_banco){
		//selecciona el primero de( envio == null || envio.contestado=false)

		ResultSet resultSet;
		try {
			resultSet = this.statement.executeQuery("SELECT MIN(codCanal) FROM Canal c JOIN UltimoEnvio ue " +
					"ON c.codBanco = '" + id_banco + "' WHERE c.cabloqueado = 0 AND ue.uecontestado = 1" );
			
			if(resultSet.next())
				return resultSet.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
		return 0;
	}
	
	/**
	 * Obtiene en CANAL el siguiente numero de mensaje para el banco y canal indicado 
	 * y le suma 1.
	 * @param id_banco El banco en el que se encuentra el canal.
	 * @param id_canal El canal de donde seleccionar el numero de mensaje.
	 * @return Un entero con el número de mensaje a utilizar.
	 */
	public int selecciona_num_mensaje(String id_banco,int id_canal){

		ResultSet resultSet;
		try {
			resultSet = this.statement.executeQuery("SELECT canext_numMensaje FROM Canal " +
					"WHERE codBanco = '" + id_banco + "' AND codCanal = " + id_canal);
			
			this.statement.executeUpdate("UPDATE Canal SET canext_numMensaje = canext_numMensaje+1" +
					" WHERE codBanco = '" + id_banco + "' AND codCanal = " + id_canal);
			
			if(resultSet.next())
				return resultSet.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
		return 0;
	}
	
	/**
	 * Comprueba si el canal está ocupado o el envio está sin contestar y devuelve un booleano.
	 * @param id_banco El banco a comprobar.
	 * @param canal El canal concreto a comprobar.
	 * @return True si el canal esta ocupado o False en caso contrario.
	 */
	public boolean isCanal_ocupado(String id_banco, int canal){
		
		//Obtiene el id real que identifica al banco en la BD.
		int id_banco_bd = 0;
		try{
			id_banco_bd = getIdBanco(id_banco);
		}catch(ConsorcioBDException c){
			c.printStackTrace();
			System.exit(-1);
		}
		
		ResultSet resultSet;
		try {
			resultSet = this.statement.executeQuery("SELECT c.cabloqueado || (ue.uecontestado=0)" +
					" FROM Canal c JOIN UltimoEnvio ue ON c.codUltimoEnvio = ue.codUltimoEnvio" +
					" WHERE c.codBanco = " + id_banco_bd + " AND c.codCanal = " + canal);

			if(resultSet.next())
				return (resultSet.getInt(1) == 1);
			
		} catch (SQLException e) {
			e.printStackTrace();
			return true;
		}
		return false;
	}

	/**
	 * Devuelve True si hay mensajes sin responder en el banco indicado.
	 * Recorre ULTIMOENVIO con el banco indicado comprobando si está contestado.
	 * @param id_banco El banco en el que buscar.
	 * @return True si hay algun mensaje sin responder y False en caso contrario.
	 */
	public boolean hayMensajesSinResponder(String id_banco){
		ResultSet resultSet;
		try {
			resultSet = this.statement.executeQuery("SELECT COUNT(codUltimoEnvio) FROM UltimoEnvio " +
					"WHERE codBanco = '" + id_banco + "' AND uecontestado = 0");

			if(resultSet.next())
				return (resultSet.getInt(1) != 0);
		} catch (SQLException e) {
			e.printStackTrace();
			return true;
		}
		return true;
	}

	/**
	 * Devuelve los ultimos mensajes enviados hacia el banco introducido
	 * por parámetro, por todos los canales.
	 * @param id_banco El banco en el que buscar.
	 * @return Un ArrayList con todos los ultimos mensajes de cada canal.
	 */
	public ArrayList<Mensaje> recupera_ultimos_mensajes(String id_banco){
		
		ResultSet resultSet;
		ArrayList<Mensaje> res = new ArrayList<Mensaje>();
		try {
			resultSet = this.statement.executeQuery("SELECT uestringMensaje FROM UltimoEnvio " +
					"WHERE codBanco = '" + id_banco + "'");
			
			while(resultSet.next()){
				Mensaje m = Mensaje.parse(resultSet.getString(1));
				res.add(m);
			}
			
			return res;
		} catch (SQLException | MensajeNoValidoException e) {
			System.err.println(e);
			return null;
		}
	}

	/**
	 * Settea a True la variable bloqueado de CANAL para el banco y canal correspondiente. 
	 * @param id_banco El banco en el que se encuentra.
	 * @param canal El canal concreto a bloquear.
	 */
	public void bloquearCanal(String id_banco, int canal){
		try {
			this.statement.executeUpdate("UPDATE Canal SET cabloqueado = 1 " +
					"WHERE codBanco = '" + id_banco + "' AND codCanal = " + canal);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Comprueba si el tipo de mensaje pasado por parámetro se corresponde con la respuesta adecuada a la 
	 * solicitud que hay en el banco y canal indicados por parámetros.
	 * @param tipo_contestacion El tipo de la respuesta que hemos recibido.
	 * @param id_banco El banco en el que se encuentra el canal con la solicitud a comprobar.
	 * @param canal El canal en el que se encuentra la solicitud a comprobar.
	 * @return True si la contestacion se corresponde con la pregunta y False en caso contrario.
	 */
	public boolean esCorrectaContestacion(CodigosMensajes tipo_contestacion, String id_banco, int canal){
		//comprueba si tipo_contestacion es la contestacion correspondiente al mensaje que hay en el canal y banco indicados.
		return true;
	}
	
	/**
	 * Desbloquea todos los canales del banco
	 * @param id_banco El banco para el cual desbloquear todos los canales.
	 */
	public void desbloquearCanales(String id_banco){
		try {
			this.statement.executeUpdate("UPDATE Canal SET cabloqueado = 0 " +
					"WHERE codBanco = '" + id_banco + "'");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Método que comprueba si el ultimo mensaje del canal ha sido respondido.
	 * @param id_banco El banco en el que comprobar.
	 * @param canal EL canal concreto del banco en el que se encuentra el envio.
	 * @return El valor booleando del atributo "contestado" del último envio del canal.
	 */
	public boolean isContestado(String id_banco, int canal){
		
		ResultSet resultSet;
		try {
			resultSet = this.statement.executeQuery("SELECT uecontestado " +
					"FROM Canal c JOIN UltimoEnvio ue ON c.codUltimoEnvio = ue.codUltimoEnvio " +
					"WHERE c.codBanco = '" + id_banco +"' AND c.codCanal = " + canal);

			if(resultSet.next())
				return (resultSet.getInt(1) == 1);
		} catch (SQLException e) {
			e.printStackTrace();
			return true;
		}
		return true;
	}
	
	
	
	/**
	 * Método que añade un CANAL a la BD.
	 * @param id_banco El banco en el que añadir.
	 * @param canal El numero de canal.
	 */
	public void anhadir_canal(String id_banco, int canal){
		
		//Obtiene el id real que identifica al banco en la BD.
		int id_banco_bd = 0;
		try{
			id_banco_bd = getIdBanco(id_banco);
		}catch(ConsorcioBDException c){
			c.printStackTrace();
			System.exit(-1);
		}
		
		try {
			this.statement.executeUpdate("INSERT INTO Canal(codBanco,codCanal) VALUES (" + id_banco_bd + "," + canal + ")");
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	/**
	 * Metodo que elimina un CANAL de la BD.
	 * @param id_banco El id del banco en el que se encuentra.
	 * @param canal El numero de canal a borrar.
	 */
	public void eliminar_canal(String id_banco, int canal){
		
		//Obtiene el id real que identifica al banco en la BD.
		int id_banco_bd = 0;
		try{
			id_banco_bd = getIdBanco(id_banco);
		}catch(ConsorcioBDException c){
			c.printStackTrace();
			System.exit(-1);
		}
		
		try {
			this.statement.executeUpdate("DELETE FROM Canal WHERE codBanco = " + id_banco_bd + " AND codCanal = " + canal);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Obtiene todos los canales de un banco concreto y los elimina.
	 * @param id_banco El banco a eliminar sus canales.
	 */
	public void borrar_canales(String id_banco){
		
		//Obtiene el id real que identifica al banco en la BD.
		int id_banco_bd = 0;
		try{
			id_banco_bd = getIdBanco(id_banco);
		}catch(ConsorcioBDException c){
			c.printStackTrace();
			System.exit(-1);
		}
		
		ArrayList<Integer> canales = new ArrayList<Integer>();
		ResultSet resultSet = null;
		try {
			resultSet = this.statement.executeQuery("SELECT codCanal " +
					"FROM Canal " +
					"WHERE codBanco = " + id_banco_bd);

			//Obtiene todos los canales a borrar
			while(resultSet.next()){
				canales.add(resultSet.getInt(1));
			}
			
			//Borra todos los canales
			Iterator<Integer> it = canales.iterator();
			while(it.hasNext()){
				eliminar_canal(id_banco,it.next());
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	
	/*---------------------------------------------------
	 --------------------- ULTIMOENVIO -----------------------
	 ----------------------------------------------------*/
	
	
	/**
	 * Getter de la IP del ULTIMOENVIO que indica en donde se encuentra el cajero a contestar.
	 * @param id_banco El banco correspondiente.
	 * @param num_canal El canal del que obtener.
	 * @return La ip correspondiente.
	 */
	public InetAddress getIpEnvio(String id_banco, int canal){
	
		ResultSet resultSet;
		String temp = "";
		try {
			resultSet = this.statement.executeQuery("SELECT ue.ueip FROM UltimoEnvio ue JOIN Canal c " +
					"ON ue.codUltimoEnvio = c.codUltimoEnvio " +
					"WHERE c.codBanco = '" + id_banco + "' AND c.codCanal = " + canal );
			
			if(resultSet.next())
				temp = resultSet.getString(1);
				
			return InetAddress.getByName(temp);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return null;
		}
	}
		
		
	/**
	 * Getter del puerto del ULTIMOENVIO que indica en donde se encuentra el cajero a contestar.
	 * @param id_banco El banco correspondiente.
	 * @param num_canal El canal del que obtener.
	 * @return La puerto correspondiente.
	 */
	public int getPortEnvio(String id_banco, int canal){
		
		ResultSet resultSet;
		int res = 0;
		try {
			resultSet = this.statement.executeQuery("SELECT ue.uepuerto FROM UltimoEnvio ue JOIN Canal c " +
					"ON ue.codUltimoEnvio = c.codUltimoEnvio " +
					"WHERE c.codBanco = '" + id_banco + "' AND c.codCanal = " + canal);
			
			if(resultSet.next())
				res = resultSet.getInt(1);
				
			return res;
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * Getter del id del Cajero del ULTIMOENVIO.
	 * @param id_banco El banco en el que obtener el id.
	 * @param num_canal El canal en el que se encuentra el Envio con la id del cajero que lo realizó.
	 * @return Un string que identifica al cajero que realizó el envio.
	 */
	public String getIdCajero(String id_banco, int canal){
		
		ResultSet resultSet;
		int res = 0;
		try {
			resultSet = this.statement.executeQuery("SELECT ue.uecodCajero FROM UltimoEnvio ue JOIN Canal c " +
					"ON ue.codUltimoEnvio = c.codUltimoEnvio " +
					"WHERE c.codBanco = '" + id_banco + "' AND c.codCanal = " + canal);
			
			if(resultSet.next())
				res = resultSet.getInt(1);
				
			return Integer.toString(res);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}	
	}
	
	/**
	 * Método que pone el atributo "contestado" del ULTIMOENVIO a True.
	 * En caso de que no haya ultimo movimiento para el canal indicado, no hace nada.
	 * @param id_banco El banco que identifica al envio.
	 * @param num_canal El canal que identifica al envio.
	 */
	public void setContestadoEnvio(String id_banco, int canal){
		
		ResultSet resultSet;
		try {
			int codigo=0;
			
			resultSet = this.statement.executeQuery("SELECT ue.codUltimoEnvio " +
					"FROM UltimoEnvio ue JOIN Canal c ON ue.codUltimoEnvio = c.codUltimoEnvio " +
					"WHERE c.codBanco = '" + id_banco + "' AND c.codCanal = " + canal);
		
			if(resultSet.next()){
				codigo = resultSet.getInt(1);
				this.statement.executeUpdate("UPDATE UltimoEnvio SET uecontestado = 1 " + codigo);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private String getTarjetaEnvio(MensajeDatos mensaje){
		switch(mensaje.getTipoMensaje()){
			case SOLSALDO:
				return ((SolSaldo)mensaje).getNum_tarjeta();
			case SOLMOVIMIENTOS:
				return ((SolMovimientos)mensaje).getNum_tarjeta();
			case SOLREINTEGRO:
				return ((SolReintegro)mensaje).getNum_tarjeta();
			case SOLABONO:
				return ((SolAbono)mensaje).getNum_tarjeta();
			case SOLTRASPASO:
				return ((SolTraspaso)mensaje).getNum_tarjeta();
			default:
				return null;
		}
	}
	
	private int getCuentaEnvio(MensajeDatos mensaje){
		switch(mensaje.getTipoMensaje()){
			case SOLSALDO:
				return ((SolSaldo)mensaje).getNum_cuenta();
			case SOLMOVIMIENTOS:
				return ((SolMovimientos)mensaje).getNum_cuenta();
			case SOLREINTEGRO:
				return ((SolReintegro)mensaje).getNum_cuenta();
			case SOLABONO:
				return ((SolAbono)mensaje).getNum_cuenta();
			case SOLTRASPASO:
				return ((SolTraspaso)mensaje).getNum_cuenta_origen();
			default:
				return -1;
		}	
	}
	
	/**
	 * 
	 * @param mensaje
	 * @param codCajero
	 * @param ip_cajero
	 * @param puerto_cajero
	 */
	private void insertar_ultimo_envio(Mensaje mensaje,String codCajero,String ip_cajero, int puerto_cajero){
		
		String tarjeta = null;
		int cuenta = 0;
		int num_mensaje = 0;
		String id_banco = mensaje.getDestino();
		
		//Comprueba que los datos son correctos
		if(mensaje.es_datos()){
			try{
				if((tarjeta = getTarjetaEnvio((MensajeDatos)mensaje)) == null)
					throw new CodigoNoValidoException();
				if((cuenta = getCuentaEnvio((MensajeDatos)mensaje)) == -1)
					throw new CodigoNoValidoException();
				num_mensaje = ((MensajeDatos)mensaje).getNmsg();
			}catch (CodigoNoValidoException a){
					a.printStackTrace();
					System.exit(-1);
			}
		}
		
		//Obtiene el id real que identifica al banco en la BD.
		int id_banco_bd = 0;
		try{
			id_banco_bd = getIdBanco(id_banco);
		}catch(ConsorcioBDException c){
			c.printStackTrace();
			System.exit(-1);
		}
		
		System.out.println("ULTIMO ENVIO:"+num_mensaje+"-CAJERO:"+codCajero+"-PUERTO:"+puerto_cajero+"-IP:"+ip_cajero+"-BANCO:"
		+id_banco_bd+"-TARJETA:"+tarjeta+"-CUENTA:"+cuenta);
		
		//Ejecuta la insercion en la BD
		try {
			this.statement.executeUpdate("INSERT INTO UltimoEnvio(codUltimoEnvio,uecodCajero,uepuerto,ueip," +
					"codBanco,codTarjeta,codCuenta,uestringMensaje)" +
					" VALUES (" + num_mensaje + "," + ((codCajero==null)?"NULL":"'"+codCajero+"'") + "," + puerto_cajero + ",'" + ip_cajero + 
					"'," + id_banco_bd + "," + ((tarjeta==null)?"NULL":"'"+tarjeta+"'") + "," + cuenta + ",'" + mensaje.toString() +"')");
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	/**
	 * 
	 * @param codigo_ultimo_envio
	 */
	private void eliminar_ultimo_envio(int codigo_ultimo_envio){
		try {
			this.statement.executeUpdate("DELETE FROM UltimoEnvio WHERE codUltimoEnvio=" + codigo_ultimo_envio);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Cambia el ultimo envio del canal indicado por el pasado por parametro.
	 * Tambien añade el mensaje a la tabla de MENSAJES del consorcio.
	 * Si el canal esta ocupado no se inserta como ultimo envio.
	 * @param message El mensaje a añadir.
	 * @param ip La ip de la que proviene el envio (Cajero)
	 * @param port El puerto del que proviene el envio (Cajero)
	 * @param canal El canal correspondiente.
	 */
	public void anhadir_ultimo_envio(Mensaje message,String codCajero,String ip_cajero, int puerto_cajero,int canal){

		String id_banco = message.getDestino();
		
		//Obtiene el id real que identifica al banco en la BD.
		int id_banco_bd = 0;
		try{
			id_banco_bd = getIdBanco(id_banco);
		}catch(ConsorcioBDException c){
			c.printStackTrace();
			System.exit(-1);
		}
		
		ResultSet resultSet;
		int codigo_ultimo_envio = 0;
		
		//if canal==0->mensaje de control sin canal
		//Comprueba si el canal esta ocupado o el mensaje del canal no ha sido respondido
		try {
			if (this.isCanal_ocupado(id_banco, canal))
				throw new ConsorcioBDException("El canal se encuentra ocupado, no se ha realizado la inserción.");
		} catch (ConsorcioBDException e) {
			return;
		}
		
		//Selecciona el codigo del ultimo envio para el banco y canal indicados
		try {
			resultSet = this.statement.executeQuery("SELECT codUltimoEnvio" +
					" FROM Canal" +
					" WHERE codCanal=" + canal + " AND codBanco=" + id_banco_bd + " AND codUltimoEnvio IS NOT NULL");
			if(resultSet.next()){
				codigo_ultimo_envio = resultSet.getInt(1);
				
				//Borra el envio para el banco y canal indicado
				eliminar_ultimo_envio(codigo_ultimo_envio);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		 
		//Inserta el nuevo envio para el banco y canal indicado
		insertar_ultimo_envio(message,codCajero, ip_cajero, puerto_cajero);
	}
	
	/**
	 * Devuelve el ULTIMOENVIO de un canal.
	 * @param id_banco El banco que identifica al envio.
	 * @param canal EL canal que indentifica al envio.
	 * @return El mensaje correspondiente al ULTIMOENVIO realizado por el canal indicado.
	 */
	public Mensaje obtener_ultimo_envio(String id_banco, int canal){
	
		String str_mensaje;
		ResultSet resultSet;
		Mensaje res = null;
		try {
			resultSet = this.statement.executeQuery("SELECT ue.uestringMensaje " +
					"FROM UltimoEnvio ue JOIN Canal c ON ue.codUltimoEnvio = c.codUltimoEnvio " +
					"WHERE c.codBanco = '" + id_banco + "' AND c.codCanal = " + canal);
			
			if(resultSet.next()){
				str_mensaje = resultSet.getString(1);
				res = Mensaje.parse(str_mensaje);
			}
			
			return res;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} catch (MensajeNoValidoException e) {
			e.printStackTrace();
			return null;
		}
	}

	
	/*---------------------------------------------------
	 --------------------- MENSAJE ---------------------
	 ----------------------------------------------------*/
	
	/**
	 * Método que devuelve un ArrayList con los MENSAJE que se han realizado offline para el banco
	 * indicado por parámetro.
	 * @param id_banco EL banco del que obtener los mensajes offline.
	 * @return Un arraylist con los Mensajes que han sido offline.
	 */
	public ArrayList<Mensaje> getMensajesOffline(String id_banco){
		
		//Obtiene el id real que identifica al banco en la BD.
		int id_banco_bd = 0;
		try{
			id_banco_bd = getIdBanco(id_banco);
		}catch(ConsorcioBDException c){
			c.printStackTrace();
			System.exit(-1);
		}
		
		ResultSet resultSet = null;
		ArrayList<Mensaje> res = new ArrayList<Mensaje>();
		try {
			//Obtenemos todos los mensajes OFFLINE
			resultSet = this.statement.executeQuery("SELECT mestringMensaje" +
					" FROM Mensaje " +
					" WHERE codBanco = " + id_banco_bd + " AND (meoffline IS NOT NULL || meoffline != 0) AND codTDestino=1");
			
			while(resultSet.next()){
				Mensaje m = Mensaje.parse(resultSet.getString(1));
				res.add(m);
			}
			
			//Ponemos OFFLINE a false para todos los mensajes del id_banco
			this.statement.executeUpdate("UPDATE Mensaje SET meoffline = 0 WHERE codBanco = " + id_banco_bd);
			
			return res;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} catch (MensajeNoValidoException e) {
			e.printStackTrace();
			return null;
		}
	}
	

	/**
	 * Añade una linea en la tabla MENSAJE
	 * @param es_envio Valor booleano inidica si es un envio o una recepcion
	 */
	public void almacenar_mensaje(Mensaje message,TipoOrigDest torigen,String origen,TipoOrigDest tdestino,String destino){

		int num_mensaje = -1;
		boolean offline = true;
		
		//Si el mensaje es de datos obtenemos el numero de mensaje y 
		if(message.es_datos()){
			num_mensaje = ((MensajeDatos) message).getNmsg();
			offline = !((MensajeDatos) message).getCodonline();
		}
		
		try {
			String q = "INSERT INTO Mensaje(meNumMensaje,meoffline, codTOrigen,meorigen, codTDestino, medestino,mestringMensaje) " +
					"VALUES (" + ((num_mensaje==-1)?"NULL":num_mensaje) + "," + ((offline)? 1:0) + "," + torigen.getNum() +
					",'" + origen + "'," + tdestino.getNum() + ",'" + destino + "','" + message.toString() +"')";
			
			this.statement.executeUpdate(q);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Método que obtiene todos los mensajes hacia/desde el CAJERO.
	 * Devuelve un arraylist con los Strings a mostrar en la interfaz grafica.
	 * @return Un arraylist de strings.
	 */
	public ArrayList<String> MensajesCajeroToString(){
		
		ResultSet resultSet = null;
		ArrayList<String> res = new ArrayList<String>();
		try {
			resultSet = this.statement.executeQuery("SELECT ta.todnombre as ORIGEN,tb.todnombre as DESTINO,m.mestringMensaje" +
					" FROM Mensaje m JOIN TipoOrigDest ta ON ta.codTOrigDest = m.codTOrigen" +
					" JOIN TipoOrigDest tb ON tb.codTOrigDest = m.codTDestino" +
					" WHERE ta.todnombre='Cajero' || tb.todnombre='Cajero' ORDER BY codMensaje");

			while(resultSet.next()){
				String str1 = resultSet.getString(1);
				String str2 = resultSet.getString(2);
				Mensaje m = null;
				try {
					m = Mensaje.parse(resultSet.getString(3));
				} catch (MensajeNoValidoException e) {
					e.printStackTrace();
				}
				String elemento = str1+"("+m.getOrigen()+")"+ "->" + str2+"("+m.getDestino()+")" + ": " + m.getTipoMensaje();
				res.add(elemento);
			}
			return res;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Método que obtiene todos los mensajes hacia/desde el BANCO.
	 * Devuelve un arraylist con los Strings a mostrar en la interfaz grafica.
	 * @return Un arraylist de strings.
	 */
	public ArrayList<String> MensajesBancoToString(){
		
		ResultSet resultSet = null;
		ArrayList<String> res = new ArrayList<String>();
		try {
			resultSet = this.statement.executeQuery("SELECT ta.todnombre as ORIGEN,tb.todnombre as DESTINO,m.mestringMensaje" +
					" FROM Mensaje m JOIN TipoOrigDest ta ON ta.codTOrigDest = m.codTOrigen" +
					" JOIN TipoOrigDest tb ON tb.codTOrigDest = m.codTDestino" +
					" WHERE ta.todnombre='Banco' || tb.todnombre='Banco' ORDER BY codMensaje");

			while(resultSet.next()){
				String str1 = resultSet.getString(1);
				String str2 = resultSet.getString(2);
				Mensaje m = null;
				try {
					m = Mensaje.parse(resultSet.getString(3));
				} catch (MensajeNoValidoException e) {
					e.printStackTrace();
				}
				String elemento = str1+"("+m.getOrigen()+")"+ "->" + str2+"("+m.getDestino()+")" + ": " + m.getTipoMensaje();
				res.add(elemento);
			}
			return res;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	
	
}
