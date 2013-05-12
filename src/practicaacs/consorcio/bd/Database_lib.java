package practicaacs.consorcio.bd;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Properties;

import practicaacs.banco.bd.ClienteBDBanco;
import practicaacs.banco.bd.Conta;
import practicaacs.banco.estados.EstadoSesion;
import practicaacs.consorcio.aux.Movimiento;
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
		String file = ""; //LA SITUACION DEL FICHERO DE CONFIGURACION
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
			e.printStackTrace();
		}

		try {
			con = DriverManager.getConnection("jdbc:mysql://" + bdadd + "/" + bdname + "?user=" + bduser + "&password=" + bdpass);
			statement = con.createStatement();
		} catch (SQLException e) {
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
	
	
	/*---------------------------------------------------
	 ---------------- CONSULTAS EN CUENTAS -----------------------
	 ----------------------------------------------------*/	
	
	public CodigosRespuesta comprobar_condiciones(String tarjeta, int cuenta_origen, int cuenta_destino,CodigosMensajes tipo, int importe){
		if (this.consultarGastoOffline(tarjeta) > 1000)
			return CodigosRespuesta.IMPORTEEXCLIMITE;
		if ((tipo.equals(CodigosMensajes.SOLTRASPASO)) && (cuenta_origen == cuenta_destino))
			return CodigosRespuesta.TRANSCUENTASIGUALES;
		if ((tipo.equals(CodigosMensajes.SOLTRASPASO)) && (this.consultar_saldo(tarjeta, cuenta_origen) < importe))
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
	 * Consulta que hace un SELECT en la tabla CUENTA y devuelve la indicada.
	 * @param tarjeta La tarjeta a la que pertenece la cuenta.
	 * @param cuenta La cuenta a consultar.
	 * @return Devuelve el saldo actual de la cuenta y null en caso de error
	 */
	public int consultar_saldo(String tarjeta, int cuenta){
		ResultSet resultSet;
		try{
			resultSet = this.statement.executeQuery("SELECT ncuenta,saldo FROM ContaTarxeta JOIN Conta" +
					" USING (ccod) where tcod = " + tarjeta + " AND " + " cnum = " + cuenta);
			resultSet.next();
			return resultSet.getInt(2);
		}
		catch (SQLException e) {
			e.printStackTrace();
			return (Integer) null;
		}
		this
	}

	public ArrayList<Movimiento> consultar_movimientos(String tarjeta,int cuenta){
		return null;
	}

	/**
	 * 	Reintegro
		En general se aplica al pago o devolución de lo que se debe.
		En el contexto de cuentas bancarias se aplica a una disposición de efectivo. 
	*/
	public int realizar_reintegro(String tarjeta,int cuenta,int importe){
		return 0; //devuelve el nuevo saldo
	}
	
	/**
	 *  Traspaso de efectivo
		Traspaso de dinero entre cuentas de un mismo titular, situadas en entidades distintas, 
		que se formaliza mediante una orden dada por el cliente a la entidad que ha de recibir el dinero, 
		para que esta la transmita a aquella de la que proceden los fondos. 
		El importe máximo de la orden de traspaso será de 150.000€ por cuenta de cargo y día. 
	 */
	public int realizar_traspaso(String tarjeta,int cuenta_origen,int cuenta_destino,int importe){
		return 0; //Devuelve el nuevo saldo del destino
	}

	/**
	 *  Abono en cuenta
		Asiento o anotación en el haber de una cuenta, que aumenta el saldo de la misma.
		Los cheques con la mención "para abonar en cuenta" o expresión similar en el anverso sólo se podrán hacer
		efectivos si previamente se realiza su ingreso en una cuenta corriente, nunca directamente en ventanilla. 
	 */
	public int realizar_abono(String tarjeta, int cuenta,int importe){
		return 0;
	}
	
	/*---------------------------------------------------
	 ---------------- MOVIMIENTOS -----------------------
	 ----------------------------------------------------*/
	
	/**
	 * Recorre la tabla de movimientos sumando los reintegros para el banco 
	 * indicado por parámetro
	 * @param id_banco El banco en donde consultar los reintegros.
	 * @return El resultado de la suma de todos los reintegros
	 */
	public int getNumReintegros(String id_banco) {
		return 0;
	}

	/**
	 * Recorre la tabla de movimientos sumando los traspasos para el banco 
	 * indicado por parámetro
	 * @param id_banco El banco en donde consultar los traspasos.
	 * @return El resultado de la suma de todos los traspasos
	 */
	public int getNumTraspasos(String id_banco) {
		return 0;
	}

	/**
	 * Recorre la tabla de movimientos sumando los abonos para el banco 
	 * indicado por parámetro
	 * @param id_banco El banco en donde consultar los abonos.
	 * @return El resultado de la suma de todos los abonos
	 */
	public int getNumAbonos(String id_banco) {
		return 0;
	}
	
	/**
	 * Método que realiza una consulta del gasto offline para la tarjeta indicada.
	 * Recorre la tabla de MOVIMIENTO y suma todos los gastos offline correspondientes 
	 * a las cuentas de la tarjeta.
	 * @param tarjeta La tarjeta a consultar
	 * @return Un entero con el gasto oofline.
	 */
	public int consultarGastoOffline(String tarjeta){
		//obtener la cuenta a partir de la tarjeta
		//para la cuenta recorrer todos los movimientos y calcular
		return 0;
	}
	
	/**
	 * Método que añade un movimiento a la tabla MOVIMIENTO
	 * @param tipo El tipo de movimiento
	 * @param cantidad La cantidad.
	 * @param offline Y un valor booleano, True si es offline y False si es online.
	 */
	private void anhadir_movimiento(CodigosMovimiento tipo,int cantidad,boolean offline){
		
	}
	
	
	/*---------------------------------------------------
	 ----------------- SESIONES/BANCOS -------------------
	 ----------------------------------------------------*/
	
	/**
	 * Método que abre una sesion con el banco indicado.
	 * Comprueba si ya esta, en ese caso setea el estado a ACTIVA, sino añade una linea a la tabla
	 * de BANCO.
	 * @param id_banco El identificador a añadir.
	 * @param ip La ip en la que se encuentra el servidor del banco.
	 * @param puerto El puerto en el que se encuentra el servidor del banco.
	 * @param num_canales El número de canales máximo que el banco puede usar.
	 */
	public void abrir_sesion(String id_banco, InetAddress ip, String puerto, int num_canales){
		//Comprueba si ya esta la sesión, si está se reempleza y si no se inserta una nueva
	}
	
	/**
	 * Método que cierra una sesión con el banco indicado.
	 * Comprueba si está la sesión, si está setea el estado a CERRADA, sino no hace nada.
	 * @param id_banco El identificador del banco a cerrar.
	 */
	public void cerrar_sesion(String id_banco){
		//Comprueba si esta la sesión y si esta la elimina.
	}
	
	/**
	 * Comprueba en BANCO si el banco introducido por parámetro tiene sesión.
	 * @param id_banco El banco a buscar.
	 * @return True si la sesion es ACTIVA y False en caso contrario.
	 */
	public boolean hasSesion(String id_banco){
		return true;
	}
	
	/**
	 * Comprueba si la SESION admite envios o no.
	 * @param id_banco El banco a buscar.
	 * @return True si los admite y False en caso contrario (si el estado es trafico detenido o cerrada)
	 */
	public boolean consultar_protocolo(String id_banco){
		//si es trafico detenido o sesion cerrada return false
		return true;
	}
	
	//------GETTERS Y SETTERS
	
	/**
	 * Getter en BANCO del estado de la conexion con el banco.
	 * @param id_banco El banco a buscar.
	 * @return El estado de la conexion correspondiente.
	 */
	public EstadoSesion getEstado_conexion_banco(String id_banco){
		return null;
	}

	/**
	 * Getter en BANCO del numero de canales
	 * @param id_banco El banco a buscar.
	 * @return El número máximo de canales del banco.
	 */
	public int getNum_canales(String id_banco){
		//accede a la tabla de Sesion y obtiene el num canales para el id
		return 0;
	}
	
	/**
	 * Getter en BANCO del puerto del banco
	 * @param id_banco El banco a buscar.
	 * @return El puerto correspondiente.
	 */
	public int getPortBanco(String id_banco){
		return 0;
	}
	
	/**
	 * Getter en BANCO de la ip del banco.
	 * @param id_banco El banco a buscar.
	 * @return La ip correspondiente.
	 */
	public InetAddress getIpBanco(String id_banco) {
		return null;
	}
	
	/**
	 * Método que obtiene una lista con los identificadores de todos los BANCO que tienen sesión abierta.
	 * @return Un array list de String con los id de todos los BANCOS que tienen sesion abierta.
	 */
	public ArrayList<String> getSesiones() {
		return null;
	}
	
	/**
	 * Setter en BANCO del estado de la conexion
	 * @param id_banco El banco al que cambiar el estado.
	 * @param estado El nuevo estado.
	 */
	public void setEstado_conexion_banco(String id_banco,EstadoSesion estado){
		
	}
	
	/**
	 * Setter en BANCO del numero de canales
	 * @param id_banco El banco al que cambiar el numero de canales.
	 * @param canales El nuevo número de canales.
	 */
	public void setNum_canales(String id_banco,int canales){
		
	}
	
	/**
	 * Setter en BANCO del puerto
	 * @param id_banco El banco al que cambiar el puerto.
	 * @param puerto El nuevo puerto.
	 */
	public void setPuertoBanco(String id_banco, String puerto){
		Integer.getInteger(puerto);
	}
	
	
	/*---------------------------------------------------
	 -------------------- CANALES -----------------------
	 ----------------------------------------------------*/


	/**
	 * Obtiene en CANAL el siguiente canal disponible para realizar un envio.
	 * @param id_banco El banco de donde obtener un nuevo canal.
	 * @return Un entero con el canal correspondiente.
	 */
	public int seleccionarCanal(String id_banco){
		//selecciona el primero de( envio == null || envio.contestado=false)
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
		//Obtiene el ultimo numero de mensaje para el id y le suma 1
		return 0;
	}
	
	/**
	 * Comprueba si el canal está ocupado o el envio está sin contestar y devuelve un booleano.
	 * @param id_banco El banco a comprobar.
	 * @param canal El canal concreto a comprobar.
	 * @return True si el canal esta ocupado o False en caso contrario.
	 */
	public boolean isCanal_ocupado(String id_banco, int canal){
		//Devuelve true si canal.ultimo_envio.contestado=false && canal.bloqueado==false
		return false;
	}

	/**
	 * Devuelve True si hay mensajes sin responder en el banco indicado.
	 * Recorre ULTIMOENVIO con el banco indicado comprobando si está contestado.
	 * @param id_banco El banco en el que buscar.
	 * @return True si hay algun mensaje sin responder y False en caso contrario.
	 */
	public boolean hayMensajesSinResponder(String id_banco){
		return false;
	}

	/**
	 * Devuelve los ultimos mensajes enviados hacia el banco introducido
	 * por parámetro, por todos los canales.
	 * @param id_banco El banco en el que buscar.
	 * @return Un ArrayList con todos los ultimos mensajes de cada canal.
	 */
	public ArrayList<Mensaje> recupera_ultimos_mensajes(String id_banco){
		return null;
	}

	/**
	 * Settea a True la variable bloqueado de CANAL para el banco y canal correspondiente. 
	 * @param id_banco El banco en el que se encuentra.
	 * @param canal El canal concreto a bloquear.
	 */
	public void bloquearCanal(String id_banco, int canal){
		
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
		
	}
	
	/**
	 * Método que comprueba si el ultimo mensaje del canal ha sido respondido.
	 * @param id_banco El banco en el que comprobar.
	 * @param canal EL canal concreto del banco en el que se encuentra el envio.
	 * @return El valor booleando del atributo "contestado" del último envio del canal.
	 */
	public boolean isContestado(String id_banco, int canal){
		return true;
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
	public InetAddress getIpEnvio(String id_banco, int num_canal){
		return null;
	}

	/**
	 * Getter del puerto del ULTIMOENVIO que indica en donde se encuentra el cajero a contestar.
	 * @param id_banco El banco correspondiente.
	 * @param num_canal El canal del que obtener.
	 * @return La puerto correspondiente.
	 */
	public int getPortEnvio(String id_banco, int num_canal){
		return 0;
	}	
	
	/**
	 * Getter del id del Cajero del ULTIMOENVIO.
	 * @param id_banco El banco en el que obtener el id.
	 * @param num_canal El canal en el que se encuentra el Envio con la id del cajero que lo realizó.
	 * @return Un string que identifica al cajero que realizó el envio.
	 */
	public String getIdCajero(String id_banco, int num_canal){
		return null;
	}
	
	/**
	 * Método que pone el atributo "contestado" del ULTIMOENVIO a True.
	 * @param id_banco El banco que identifica al envio.
	 * @param num_canal El canal que identifica al envio.
	 */
	public void setContestadoEnvio(String id_banco, int num_canal){
		
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
	public void anhadir_ultimo_envio(Mensaje message, InetAddress ip, int port,int canal){
		String id_banco = message.getDestino();
		//if canal==0->mensaje de control sin canal
		/*si el canal esta ocupado, no se inserta como ultimo envio de forma que se pueda seguir la ejecucion*/
		//aceder a la tabla Sesion con id_banco, a la tabla Canal con id_canal y por ultimo a envios añadir el envio
		//borra el envio que habia antes en ese canal y pone el nuevo
		//Guardar en la tabla de mensajes 
		this.almacenar_mensaje(message,true);
	}
	
	/**
	 * Devuelve el ULTIMOENVIO de un canal.
	 * @param id_banco El banco que identifica al envio.
	 * @param canal EL canal que indentifica al envio.
	 * @return El mensaje correspondiente al ULTIMOENVIO realizado por el canal indicado.
	 */
	public Mensaje obtener_ultimo_envio(String id_banco, int canal){
		//aceder a la tabla Sesion con id_banco, a la tabla Canal con id_canal y por ultimo a envios para obtener el envio
		//con el id_envio mayor.
		return null;
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
		//ponerles offline a false
		return null;
	}
	

	/**
	 * Añade una linea en la tabla MENSAJE
	 * @param es_envio Valor booleano inidica si es un envio o una recepcion
	 */
	public void almacenar_mensaje(Mensaje message,boolean es_envio){
		
	}
	
}
