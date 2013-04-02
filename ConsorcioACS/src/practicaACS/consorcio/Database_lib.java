package practicaACS.consorcio;

import java.util.ArrayList;
import java.util.Hashtable;

import fap.*;

//Libreria de acceso a la base de datos
public class Database_lib {
	
	public CodigosRespuesta comprobar_condiciones(String tarjeta, int cuenta){
		return null;
	}
	
	public int consultar_saldo(String tarjeta, int cuenta){
		return 0;
	}

	public String consultar_movimientos(int tarjeta){
		return "";
	}

	/**
	 * Reintegro
		En general se aplica al pago o devolución de lo que se debe.
		En el contexto de cuentas bancarias se aplica a una disposición de efectivo. 
	*/
	public realizar_reintegro(){
		
	}
	
	/**
	 *  Abono en cuenta
		Asiento o anotación en el haber de una cuenta, que aumenta el saldo de la misma.
		Los cheques con la mención "para abonar en cuenta" o expresión similar en el anverso sólo se podrán hacer
		efectivos si previamente se realiza su ingreso en una cuenta corriente, nunca directamente en ventanilla. 
	 */
	public void realizar_abono(){
		
	}
	
	/**
	 *   Traspaso de efectivo
		Traspaso de dinero entre cuentas de un mismo titular, situadas en entidades distintas, 
		que se formaliza mediante una orden dada por el cliente a la entidad que ha de recibir el dinero, 
		para que esta la transmita a aquella de la que proceden los fondos. 
		El importe máximo de la orden de traspaso será de 150.000€ por cuenta de cargo y día. 
	 */
	public void realizar_traspaso(){
		
	}
	

	
	
	public void almacenar_envio(Mensaje message){
		
	}
	
	//----------------SESIONES BANCOS / CANALES--------------
	private String id_banco;
	private EstadoSesion estado_conexion_banco;
	private int num_canales;
	
	public boolean hasSesion(String id_banco){
		
	}
		
	public EstadoSesion getEstado_conexion_banco(String id_banco){
		
	}
	
	public void setEstado_conexion_banco(String id_banco,EstadoSesion estado){
		
	}
	
	public int getNum_canales(String id_banco){
		
	}
	
	public void setNum_canales(String id_banco,int canales){
		
	}
	
	public ArrayList<Integer> getCanales_ocupados(String id_banco){
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
	public void anhadir_ultimo_envio(int canal, Mensaje message){
		if(ultimos_envios.containsKey(canal))
			ultimos_envios.remove(canal);
		
		ultimos_envios.put(canal,message);
	}
	
	/**
	 * Devuelve el ultimo envio de un canal
	 */
	public Mensaje obtener_ultimo_envio(int canal){
		return (Mensaje) ultimos_envios.get(canal);
	}
	
	/**
	 * Devuelve el hashtable con todos los ultimos envios de un banco
	 */
	public Hashtable getUltimosEnvios(){
		return ultimos_envios;
	}
	
}
