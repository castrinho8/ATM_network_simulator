package practicaacs.consorcio.aux;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import practicaacs.consorcio.ServidorConsorcio_Bancos;

/**
 * Clase que modela una Sesion y siver para gestionar los Timers de cada canal
 * de una sesion con un banco. Contiene un hashmap con todos los canales y el timer
 * que tienen asociado asi como un manejador para la tarea que deberá realizar si 
 * se sobrepasa el tiempo.
 *
 */

public class Sesion {

	private String id_banco;
	private int n_canales;
	private ServidorConsorcio_Bancos servidor;
	
	private TimerTask manejador;
	private long delay;
	//Canal,timer
	private HashMap<Integer,Timer> cronos;
	
	/**
	 * Constructor de la clase
	 * @param id El id del banco con el que se crea la sesion.
	 * @param serv El servidor del consorcio.
	 * @param num_canales El número de canales que tendrá la sesión
	 */
	public Sesion(String id,ServidorConsorcio_Bancos serv,int num_canales) {
		super();
		this.id_banco = id;
		this.servidor = serv;
		this.n_canales = num_canales;
		this.cronos = new HashMap<Integer,Timer>(this.n_canales);
		this.manejador = new TimerTask(){
							public void run(){
								servidor.realiza_recuperacion(id_banco);
							}
						};
		this.delay = 1000; //Asignar un minuto
	}

	/**
	 * Metodo que crea un timer en el canal indicado.
	 * @param canal EL canal en el que crear el timer.
	 */
	public void setTimer(int canal){
		Timer timer = new Timer();
		this.cronos.put(canal,timer);
		timer.schedule(this.manejador,this.delay);
	}
	
	/**
	 * Metodo que cancela el timer. Si no hay timer en el canal no hace nada.
	 * @param canal El canal en el que se encuentra el timer a cancelar
	 */
	public void cancelarTimer(int canal){
		Timer channel_timer = null;
		if ((channel_timer = this.cronos.get(canal)) != null)
			channel_timer.cancel();
	}
	
	public String getId(){
		return this.id_banco;
	}
	
}	

