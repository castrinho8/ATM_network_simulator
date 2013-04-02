package practicaacs.banco;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import practicaacs.fap.Mensaje;
import practicaacs.fap.MensajeNoValidoException;
import practicaacs.fap.SolAperturaSesion;
import practicaacs.fap.SolCierreSesion;
import practicaacs.fap.SolDetTrafico;
import practicaacs.fap.SolReanTrafico;

import practicaacs.banco.bd.ClienteBDBanco;
import practicaacs.banco.csconsorcio.AnalizadorMensajes;
import practicaacs.banco.csconsorcio.ClienteServidorConsorcio;
import practicaacs.banco.estados.EstadoSesion;
import practicaacs.banco.estados.SesAberta;
import practicaacs.banco.estados.SolApertura;
import practicaacs.banco.estados.SolDeter;
import practicaacs.banco.estados.SolPechar;
import practicaacs.banco.estados.SolReanudar;
import practicaacs.banco.iu.IniciarSesionListener;
import practicaacs.banco.iu.NovaContaAsociadaListener;
import practicaacs.banco.iu.NovaContaListener;
import practicaacs.banco.iu.NovaTarxetaListener;
import practicaacs.banco.iu.VentanaBanco;

public class Banco implements AnalizadorMensajes,IniciarSesionListener, NovaContaAsociadaListener, NovaContaListener, NovaTarxetaListener{
	
	private ClienteBDBanco bd;
	private VentanaBanco iu;
	private ClienteServidorConsorcio cs;
	private EstadoSesion estado;
	
	/**
	 * Constructor do banco a partir dun ficheiro de configuración.
	 * @param configfile Ficheiro de configuración.
	 */
	public Banco(String configfile){
		Properties prop = new Properties();
	    InputStream is;
		try {
			is = new FileInputStream(configfile);
		    prop.load(is);
		} catch (FileNotFoundException e) {
			System.err.println("Non se encontrou arquivo de configuracion " + configfile + ".");
			System.exit(-1);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String bdname = prop.getProperty("bd.name");
		String bdadd = prop.getProperty("bd.add");
		String bduser = prop.getProperty("bd.user");
		String bdpass = prop.getProperty("bd.pass");
		this.bd = new ClienteBDBanco("jdbc:mysql://" + bdadd + "/" + bdname + "?user=" + bduser + "&password=" + bdpass);
		
		int puerto = new Integer(prop.getProperty("banco.port"));
		int puertoconsorcio = new Integer(prop.getProperty("consorcio.port"));
		String hostconsorcio = prop.getProperty("consorcio.host");
		this.cs = new ClienteServidorConsorcio(puerto, hostconsorcio, puertoconsorcio, this);
		this.cs.start();
		
		this.iu = new VentanaBanco(this,prop.getProperty("banco.name"));
		this.iu.setVisible(true);

	}
	
	/**
	 * Contructor do banco a partir dos parametros.
	 * @param nombre Nome do banco.
	 * @param bdurl Url da base de datos.
	 * @param puerto Porto no que se escoita.
	 * @param hostconsorcio Host do consorcio.
	 * @param portconsorcio Porto do consorcio.
	 */
	public Banco(String nombre, String bdurl, int puerto,String hostconsorcio, int portconsorcio) {
		
		this.bd = new ClienteBDBanco(bdurl);
		
		this.cs = new ClienteServidorConsorcio(puerto, hostconsorcio, portconsorcio, this);
		this.cs.start();
		
		this.iu = new VentanaBanco(this, nombre);
		this.iu.setVisible(true);
		
	}

	/**
	 * Metodo que engade unha nova conta ó banco.
	 * @param Numero de conta.
	 * @param Saldo da conta.
	 */
	public void engadirConta(int num, float saldo) {
		bd.engadirConta(num,saldo);
		this.iu.actualizarContas();
	}
	
	/**
	 * Método que elimina unha conta.
	 * @param codigo Codigo da conta a eliminar.
	 */
	public void eliminarConta(int codigo){
		bd.eliminarConta(codigo);
		this.iu.actualizarContas();
	}
	
	/**
	 * Metodo que engade unha tarxeta.
	 * @param codigo Codigo da nova tarxeta.
	 */
	public void engadirTarxeta(String codigo){
		bd.engadirTarxeta(codigo);
		this.iu.actualizarTarxetas();
	}
	
	/**
	 * Metodo que elimina unha tarxeta do banco.
	 * @param codigo Código da tarxeta a eliminar.
	 */
	public void eliminarTarxeta(String codigo){
		bd.eliminarTarxeta(codigo);
		this.iu.actualizarTarxetas();
	}
	
	/**
	 * Metodo que asocia unha tarxeta con unha conta.
	 * @param cdgtarxeta Código da tarxeta a asociar.
	 * @param cdgconta Código da conta a asociar.
	 */
	public void engadirContaAsociada(String cdgtarxeta, int numconta, int cdgconta){
		bd.engadirContaAsociada(cdgtarxeta, numconta, cdgconta);
		this.iu.actualizarContasAsociadas();
	}

	/**
	 * Método que desasocia unha tarxeta dunha conta asociada.
	 * @param cdgtarxeta Codigo da tarxeta.
	 * @param cdgconta Código da conta asociada.
	 */
	public void eliminarContaAsociada(String cdgtarxeta, int numconta){
		bd.eliminarContaAsociada(cdgtarxeta,numconta);
	}
	
	/**
	 * Método para engadir un movemento a unha conta.
	 * @param codconta
	 */
	public void engadirMovemento(int codconta){
		//TODO
	}
	
	/**
	 * Método que devolve todas as contas do banco.
	 * @return Devolve as contas do banco.
	 */
	public ArrayList<Conta> getContas(){
	 	return bd.getContas();
	}
	
	/**
	 * Método que devolve todas as tarxetas do banco.
	 * @return Devolve as tarxetas do banco.
	 */
	public ArrayList<Tarxeta> getTarxetas(){
		return bd.getTarxetas();
	}
	
	/**
	 * Método que devolve as contas asociadas con unha certa tarxeta.
	 * @param ntarxeta Código da tarxeta.
	 * @return Un array de contas asociadas a tarxeta.
	 */
	public HashMap<Integer, Conta> getContasAsociadas(String ntarxeta){
		return bd.getContasAsociadas(ntarxeta);
	}
	
	/**
	 * Metodo que devolve os movementos dunha determinada conta.
	 * @param nconta Número da conta a analizar.
	 * @return Devolve un array de movementos.
	 */
	public ArrayList<Movemento> getMovementosConta(int nconta) {
		return bd.getMovementos(nconta);
	}

	/**
	 * Método que establece as contas e tarxetas do banco os seus valores por defecto.
	 */
	public void establecerValoresPorDefecto() {
		bd.valoresPorDefecto();
		this.iu.actualizarContas();
		this.iu.actualizarMovementos();
		this.iu.actualizarTarxetas();
		this.iu.actualizarContasAsociadas();
	}

	/**
	 * Metodo que analiza unha mensaxe recibida.
	 */
	@Override
	public void analizarMensaje(byte[] bs) {
		this.iu.engadirLinhaLog("MensaxeRecibida!!\n");
		
		Mensaje msx;
		try {
			msx = Mensaje.parse(bs);
			this.estado.analizarMensaje(msx,this);
		} catch (MensajeNoValidoException e){ 
			this.estado.analizarMensaje(null,this);
		}
	}

	/**
	 * Metodo que solicita a apertura dunha sesion.
	 * @param canales Canles cas que se abre a sesion.
	 */
	public void solicitarAbrirSesion(int canales){
		Mensaje m = new SolAperturaSesion(null, null, canales, null, null); //TODO
		try {
			this.cs.enviarMensaje(m);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.cambEstado(SolApertura.instance());
		this.iu.engadirLinhaLog("Solicitada apertura de sesión.\n");
	}

	
	/**
	 * Metodo que solicita a reanudacion do tráfico que se atopa detido.
	 */
	public void solicitarReanudarTrafico(){
		Mensaje m = new SolReanTrafico(null, null);//TODO
		try {
			this.cs.enviarMensaje(m);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.cambEstado(SolReanudar.instance());
		this.iu.engadirLinhaLog("Soliciatado reanudación do trafico.\n");
	}
	
	
	/**
	 * Metodo que solicita a detencion do trafico.
	 */
	public void solicitarDeterTrafico(){
		Mensaje m = new SolDetTrafico(null, null);//TODO
		try {
			this.cs.enviarMensaje(m);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.cambEstado(SolDeter.instance());
		this.iu.engadirLinhaLog("Soliciatado detencion do trafico.\n");
	}


	/**
	 * Metodo que solicita o peche da sesion.
	 */
	public void solicitarPecheSesion(){
		Mensaje m = new SolCierreSesion(null, null, 0, 0, 0);
		try {
			this.cs.enviarMensaje(m);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.cambEstado(SolPechar.instance());
		this.iu.engadirLinhaLog("Soliciatado peche de sesión.\n");
	}
	
	/**
	 * Metodo que establece a apertura da sesion.
	 */
	public void establecerSesionAceptada() {
		this.cambEstado(SesAberta.instance());
		this.iu.engadirLinhaLog("Sesión aberta\n");
	}
	
	/**
	 * Método que establece trafico en recuperación.
	 */
	public void establecerTraficoRecuperacion(){
		
	}

	/**
	 * Método que establece o fin do trafico en recuperación.
	 */
	public void establecerFinTraficoRecuperacion(){
		
	}
	
	/**
	 * Metodo que fai unha consulta de saldo.
	 * @param ncanal Número da canle que emitiu a consulta.
	 * @param nmsg Número da mensaxe.
	 * @param online Se a petición fixose online ou non.
	 * @param numtarx A tarxeta a consultar.
	 * @param numConta O número de conta dentro de esta tarxeta.
	 */
	public void facerConsultaSaldo(int ncanal,int nmsg, boolean online,String numtarx,int numConta){
		bd.getConta(numtarx, numConta);
	}
	
	/**
	 * Metodo que fai unha consulta dos movementos.
	 * @param ncanal Número da canle que emitiu a consulta.
	 * @param nmsg Número da mensaxe.
	 * @param online Se a petición fixose online ou non.
	 * @param numtarx A tarxeta a consultar.
	 * @param numConta O número de conta dentro de esta tarxeta.
	 */
	public void facerConsultaMovementos(int ncanal,int nmsg, boolean online,String numtarx,int numConta){
		
	}
	
	/**
	 * Metodo que fai un reitegro dun certo importe.
	 * @param ncanal Número da canle que emitiu a consulta.
	 * @param nmsg Número da mensaxe.
	 * @param online Se a petición fixose online ou non.
	 * @param numtarx A tarxeta a consultar.
	 * @param numConta O número de conta dentro de esta tarxeta.
	 * @param importe Importe.
	 */
	public void facerReintegro(int ncanal,int nmsg, boolean online,String numtarx,int numConta,int importe){
		
	}
	
	/**
	 * Metodo que fai un abono.
	 * @param ncanal Número da canle que emitiu a consulta.
	 * @param nmsg Número da mensaxe.
	 * @param online Se a petición fixose online ou non.
	 * @param numtarx A tarxeta a consultar.
	 * @param numConta O número de conta dentro de esta tarxeta.
	 * @param importe Importe.
	 */
	public void facerAbono(int ncanal,int nmsg, boolean online,String numtarx,int numConta,int importe){
		
	}
	
	/**
	 * Metodo que fai unha consulta de saldo.
	 * @param ncanal Número da canle que emitiu a consulta.
	 * @param nmsg Número da mensaxe.
	 * @param online Se a petición fixose online ou non.
	 * @param numtarx A tarxeta a consultar.
	 * @param numContaOrigen O número de conta dentro de esta tarxeta.
	 * @param numContaDestino 
	 * @param importe
	 */
	public void facerTranspaso(int ncanal,int nmsg, boolean online,String numtarx,int numContaOrigen, int numContaDestino,int importe){
		
	}
	
	private void cambEstado(EstadoSesion nuevoEstado) {
		this.estado = nuevoEstado;
	}
}