package practicaacs.consorcio;

import java.util.ArrayList;
import java.util.Hashtable;

import practicaacs.banco.estados.EstadoSesion;
import practicaacs.consorcio.aux.Movimiento;
import practicaacs.fap.*;

//Libreria de acceso a la base de datos
public class Database_lib {
	
	static private Database_lib instancia = new Database_lib();
	
	/**
	 * Constructor del Singleton.
	 */
	private Database_lib(){}
	
	/**
	 * Devuelve la instancia del singleton
	 */
	static Database_lib getInstance(){
		return instancia;
	}
	
	public CodigosRespuesta comprobar_condiciones(String tarjeta, int cuenta_origen, int cuenta_destino){
		return CodigosRespuesta.CONSACEPTADA;
	}
	
	public int consultar_saldo(String tarjeta, int cuenta){
		return 0;
	}

	public ArrayList<Movimiento> consultar_movimientos(String tarjeta,int cuenta){
		return null;
	}

	/**
	 * Reintegro
		En general se aplica al pago o devolución de lo que se debe.
		En el contexto de cuentas bancarias se aplica a una disposición de efectivo. 
	*/
	public int realizar_reintegro(String tarjeta,int cuenta,int importe){
		return 0; //devuelve el nuevo saldo
	}
	
	/**
	 *   Traspaso de efectivo
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
	
	public void almacenar_envio(Mensaje message){
		
	}
	
	
	//----------------MOVIMIENTOS------------------------
	
	/**
	 * Función que comprueba si el total de movimientos guardados en la base para la sesion indicada
	 * son iguales a los que se le pasa por parámetro.
	 */
	public boolean comprueba_cuentas(String id_banco,long reintegros, long abonos, long traspasos){
		return false;
	}
	
	public Long getNumReintegros(String id_banco2) {
		return null;
	}

	public Long getNumTraspasos(String id_banco2) {
		return null;
	}

	public Long getNumAbonos(String id_banco2) {
		return null;
	}
	//-------------------------
	
	
	//----------------SESIONES--------------
	private String id_banco;
	private int puerto;
	private EstadoSesion estado_sesion;
	private int num_canales;
	//tabla de canales
	
	public void insertar_sesion(String id_banco, String puerto, int num_canales, EstadoSesion estado){
		
	}
	
	/**
	 * Comprueba en SESION si el banco introducido por parámetro tiene sesión.
	 */
	public boolean hasSesion(String id_banco){
		return true;
	}
	
	/**
	 * Comprueba si la SESION admite envios o no.
	 * True si los admite y False en caso contrario
	 */
	public boolean consultar_protocolo(String id_banco){
		return true;
	}
	
	/**
	 * Getter en SESION del estado de la conexion
	 */
	public EstadoSesion getEstado_conexion_banco(String id_banco){
		return null;
	}

	/**
	 * Setter en SESION del estado de la conexion
	 */
	public void setEstado_conexion_banco(String id_banco,EstadoSesion estado){
		
	}
	
	/**
	 * Getter en SESION del numero de canales
	 */
	public int getNum_canales(String id_banco){
		//accede a la tabla de Sesion y obtiene el num canales para el id
		return 0;
	}
	
	/**
	 * Setter en SESION del numero de canales
	 */
	public void setNum_canales(String id_banco,int canales){
		
	}

	/**
	 * Getter en SESION del puerto
	 */
	public int getPuerto(String id_banco){
		return 0;
	}
	
	/**
	 * Setter en SESION del puerto
	 */
	public void setPuerto(String id_banco, String puerto){
		Integer.getInteger(puerto);
	}
	
	
	//----------------CANALES---------------
	
	private int id_canal;
	private Mensaje ultimo_mensaje;
	private boolean contestado;
	
	
	/**
	 * Getter en CANALES del siguiente numero de mensaje para el banco y canal indicado
	 */
	public int getNext_num_message(String id_banco,int id_canal){
		//Obtiene el ultimo numero de mensaje para el id.
		return 0;
	}
	
	/**
	 * Getter en CANALES del siguiente numero de mensaje para el banco y canal indicado.
	 * Ademas, suma 1 al número del ultimo mensaje y lo vuelve a guardar.
	 */
	public int next_num_message(String id_banco,int id_canal){
		//Obtiene el ultimo numero de mensaje para el id.
		
		//Le suma 1 al ultimo numero de mensaje y lo vuelve a guardar.
		return 0;
	}
	
	/**
	 * Getter en del siguiente numero de canal libre para el banco indicado
	 */
	public int getNext_canal(String id_banco){
		//Obtiene el ultimo canal libre para el id.
		
		return 0;
	}
	
	/**
	 * Getter en del siguiente numero de canal libre para el banco indicado
	 * Ademas, suma 1 al número del ulitmo canal y lo vuelve a guardar.
	 */
	public int next_canal(String id_banco){
		//Obtiene el ultimo canal libre para el id.
		
		//Le suma 1 al ultimo canal y lo vuelve a guardar.
		return 0;
	}

	
	public boolean isCanal_ocupado(String id_banco, int canal){
		//Devuelve true si contestado=false 
		return false;
	}

	public ArrayList<Integer> getCanales_ocupados(String id_banco){
		//accede a la tabla sesion para obteneer la tabla de canales del banco y devolvemos los que contestado=false
		return null;
	}
	
	/**
	 * Devuelve los ultimos mensajes enviados hacia el banco introducido
	 * por parámetro, por todos los canales.
	 */
	public ArrayList<Mensaje> recupera_mensajes(String id_banco){
		return null;
	}
	
	
	//---------------ULTIMOS ENVIOS----------------
	
	//private int id_banco;
	private int canal;
	private Mensaje mensaje;
	private boolean conestado;

	/**
	 * Cambia el ultimo envio del canal indicado por el pasado por parametro
	 */
	public void anhadir_ultimo_envio(String id_banco, Mensaje message){
		int id_canal = Database_lib.getInstance().getNext_canal(id_banco);
		//aceder a la tabla Sesion con id_banco, a la tabla Canal con id_canal y por ultimo a envios añadir el envio
	}
	
	/**
	 * Devuelve el ultimo envio de un canal
	 */
	public Mensaje obtener_ultimo_envio(String id_banco, int id_canal){
		//aceder a la tabla Sesion con id_banco, a la tabla Canal con id_canal y por ultimo a envios para obtener el envio
		//con el id_envio mayor.
		return null;
	}
	
	/**
	 * Devuelve el hashtable con todos los ultimos envios de un banco
	 */
	public Hashtable getUltimosEnvios(String id_banco){
		//aceder a la tabla Sesion con id_banco y para cada id_canal de la tabla Canal
		//crear una entrada en Hastable con <canal,mensaje_enviado> 
		return null;
	}


	
}
