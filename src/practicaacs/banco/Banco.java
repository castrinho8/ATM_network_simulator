package practicaacs.banco;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;

import practicaacs.fap.*;
import practicaacs.banco.bd.*;
import practicaacs.banco.csconsorcio.AnalizadorMensajes;
import practicaacs.banco.csconsorcio.ClienteServidorConsorcio;
import practicaacs.banco.estados.*;
import practicaacs.banco.iu.VentanaBanco;

public class Banco implements AnalizadorMensajes{
	
	private ClienteBDBanco bd;
	private VentanaBanco iu;
	private ClienteServidorConsorcio cs;
	private EstadoSesion estado;
	private String idconsorcio;
	private String idbanco;
	private String portBanco;
	private int idSesion = -1;
	private boolean responderMensaxes = true;
	private int numCanales;
	
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
		
		this.estado = SesNonAberta.instance();
		
		this.idbanco = prop.getProperty("banco.id");
		this.idconsorcio = prop.getProperty("consorcio.id");
		
		String bdname = prop.getProperty("banco.bd.name");
		String bdadd = prop.getProperty("banco.bd.add");
		String bduser = prop.getProperty("banco.bd.user");
		String bdpass = prop.getProperty("banco.bd.pass");
		this.bd = new ClienteBDBanco("jdbc:mysql://" + bdadd + "/" + bdname + "?user=" + bduser + "&password=" + bdpass);
		
		int puerto = new Integer(prop.getProperty("banco.port"));
		int puertoconsorcio = new Integer(prop.getProperty("consorcio.port"));
		String hostconsorcio = prop.getProperty("consorcio.add");
		this.cs = new ClienteServidorConsorcio(puerto, hostconsorcio, puertoconsorcio, this);
		this.cs.start();
		
		this.portBanco = prop.getProperty("banco.add") + "/" + prop.getProperty("banco.port");

		
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
	public Banco(String nombre, String idbanco, String idconsorcio, String portBanco, String bdurl, int puerto,String hostconsorcio, int portconsorcio) {
		
		this.estado = SesNonAberta.instance();
		
		this.bd = new ClienteBDBanco(bdurl);
		
		this.idbanco = idbanco;
		this.idconsorcio = idconsorcio;
		this.portBanco = portBanco;
		
		this.cs = new ClienteServidorConsorcio(puerto, hostconsorcio, portconsorcio, this);
		this.cs.start();
		
		this.iu = new VentanaBanco(this, nombre);
		this.iu.setVisible(true);
		
	}

	
	
	//------------------------------------------------------------------------------------------//
	//------------------------  FUNCIÓNS DE INTERACIÓN CA INTERFACE  ---------------------------//
	//------------------------------------------------------------------------------------------//
	
	/** Metodo que engade unha nova conta ó banco.
	 * @param Numero de conta.
	 * @param Saldo da conta.
	 */
	public void engadirConta(int num, float saldo) {
		bd.engadirConta(num,saldo);
		this.iu.actualizar();
	}
	
	
	/**
	 * Método que elimina unha conta.
	 * @param codigo Codigo da conta a eliminar.
	 */
	public void eliminarConta(int codigo){
		bd.eliminarConta(codigo);
		this.iu.actualizar();
	}

	
	/**
	 * Metodo que engade unha tarxeta.
	 * @param codigo Codigo da nova tarxeta.
	 */
	public void engadirTarxeta(String codigo){
		bd.engadirTarxeta(codigo);
		this.iu.actualizar();
	}


	/**
	 * Metodo que elimina unha tarxeta do banco.
	 * @param codigo Código da tarxeta a eliminar.
	 */
	public void eliminarTarxeta(String codigo){
		bd.eliminarTarxeta(codigo);
		this.iu.actualizar();
	}
	

	/**
	 * Metodo que asocia unha tarxeta con unha conta.
	 * @param cdgtarxeta Código da tarxeta a asociar.
	 * @param cdgconta Código da conta a asociar.
	 */
	public void engadirContaAsociada(String cdgtarxeta, int numconta, int cdgconta){
		bd.engadirContaAsociada(cdgtarxeta, numconta, cdgconta);
		this.iu.actualizar();
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
	 * Método que devolve a lista de canles da sesión.
	 * @return
	 */
	public ArrayList<Canal> getCanales(){
		return this.bd.getCanales(this.idSesion);
	}
	
	
	/**
	 * Método que devolve a lista de canles recibidas.
	 * @return
	 */
	public ArrayList<Mensaxe> getMensaxesRecibidas(){
		return this.bd.getMensaxesRecibidas();
	}
	
	
	/**
	 * Metodo que devolve a lista de canles enviadas.
	 * @return
	 */
	public ArrayList<Mensaxe> getMensaxesEnviadas(){
		return this.bd.getMensaxesEnviadas();
	}

	
	/**
	 * Método que establece as contas e tarxetas do banco os seus valores por defecto.
	 */
	public void establecerValoresPorDefecto() {
		bd.valoresPorDefecto();
		this.iu.actualizar();
	}

	
	//------------------------------------------------------------------------------------------//
	//------------------------  FUNCIÓNS DE INTERACIÓN CO CONSORCIO  ---------------------------//
	//------------------------------------------------------------------------------------------//
	
	/**
	 * Método que analiza unha mensaxe recibida.
	 * @param bs mensaxe recibido
	 */
	@Override
	public void analizarMensaje(String bs) {
		
		Mensaje msx;
		try {
			msx = Mensaje.parse(bs);
			this.iu.engadirLinhaLog("Mensaxe recibida: " + msx.getTipoMensaje() + "\n");
			this.rexistrarMensaxe(msx, bs);
			this.estado.analizarMensaje(msx,this);

		} catch (MensajeNoValidoException e){
			this.iu.engadirLinhaLog("Mensaxe recibida: Formato non recoñecido (" + e.getLocalizedMessage() + ")\n");
			this.estado.analizarMensaje(null,this);
		}
	}

	


	/**
	 * Metodo que solicita a apertura dunha sesion.
	 * @param canales Canles cas que se abre a sesion.
	 */
	public void solicitarAbrirSesion(int canales){
		Mensaje m = new SolAperturaSesion(this.idbanco,this.idconsorcio, canales, new Date(), this.portBanco);
		this.enviarMensaje(m, "Solicitada apertura de sesión.\n");
		this.numCanales = canales;
		this.cambEstado(SolApertura.instance());
		this.iu.actualizar();
	}

	
	/**
	 * Metodo que solicita a reanudacion do tráfico que se atopa detido.
	 */
	public void solicitarReanudarTrafico(){
		Mensaje m = new SolReanTrafico(this.idbanco, this.idconsorcio);
		this.enviarMensaje(m, "Solicitado reanudación do trafico.\n");
		this.cambEstado(SolReanudar.instance());
		this.iu.actualizar();
		
	}


	/**
	 * Metodo que solicita a detencion do trafico.
	 */
	public void solicitarDeterTrafico(){
		Mensaje m = new SolDetTrafico(this.idbanco,this.idconsorcio);
		this.enviarMensaje(m, "Solicitado detencion do trafico.\n");
		this.cambEstado(SolDeter.instance());
		this.iu.actualizar();
	}


	/**
	 * Metodo que solicita o peche da sesion.
	 */
	public void solicitarPecheSesion(){
		int total_reintegros = this.bd.getTotalReintegrosSesion(this.idSesion);
		int total_abonos = this.bd.getTotalAbonosSesion(this.idSesion);
		int total_traspasos = this.bd.getTotalTraspasosSesion(this.idSesion);

		Mensaje m = new SolCierreSesion(this.idbanco, this.idconsorcio, total_reintegros, total_abonos, total_traspasos);
		this.enviarMensaje(m, "Solicitado peche de sesión.\n");
		this.cambEstado(SolPechar.instance());
		this.iu.actualizar();
	}


	/**
	 * Método que rexistra no log un erro nunha solicitude.
	 * @param cm 
	 * @param ce
	 */
	public void errorRespuestaSolicitud(CodigosMensajes cm, CodigosError ce){
		String s = null;
		
		switch(cm){
		case RESABRIRSESION:
			s = "Error na apertura de sesion: " + ce.getMensaje();
			break;
		case RESDETENERTRAFICO:
			s = "Error na solicitude de detencion de sesion: " + ce.getMensaje();
			break;
		case RESREANUDARTRAFICO:
			s = "Error na solicitude de reanudación de sesion: " + ce.getMensaje() ;
			break;
		case RESCIERRESESION:
			s = "Error no peche de sesion: " + ce.getMensaje();
			break;
		default:
			assert(false);
		}
		this.iu.engadirLinhaLog(s+"\n");
		this.iu.actualizar();
	}
	

	/**
	 * Método que establece a apertura da sesion.
	 */
	public void establecerSesionAceptada() {
		this.cambEstado(SesAberta.instance());
		this.idSesion = this.bd.crearTablasSesion(this.numCanales);
		this.iu.engadirLinhaLog("Sesión aberta\n");
		this.iu.actualizar();
	}
	

	/**
	 * Método que establece o peche da sesión.
	 */
	public void establecerSesionPechada() {
		this.cambEstado(SesNonAberta.instance());
		this.idSesion = -1;
		this.iu.engadirLinhaLog("Sesión pechada.\n");
		this.iu.actualizar();
	}
	

	/**
	 * Metodo que establece a detención da sesión.
	 */
	public void establecerSesionDetida() {
		this.cambEstado(SesDetida.instance());
		this.iu.engadirLinhaLog("Tráfico de sesión detido.\n");
		this.iu.actualizar();
	}
	

	/**
	 * Metodo que establece a reanudación da sesión.
	 */
	public void establecerSesionReanudada() {
		this.cambEstado(SesAberta.instance());
		this.iu.engadirLinhaLog("Tráfico de sesión reanudado\n");
		this.iu.actualizar();
	}
	

	/**
	 * Método que establece trafico en recuperación.
	 */
	public void establecerTraficoRecuperacion(){
		this.cambEstado(SesRecuperacion.instance());
		this.iu.engadirLinhaLog("Entrando en modo recuperación.\n");
		this.iu.actualizar();
	}


	/**
	 * Método que establece o fin do trafico en recuperación.
	 */
	public void establecerFinTraficoRecuperacion(){
		this.cambEstado(SesAberta.instance());
		this.iu.engadirLinhaLog("Saindo de modo recuperación.\n");
		this.iu.actualizar();
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
		int lastmsgcanal;
		RespSaldo r = null;
		RespSaldoError r2 = null;
		Conta conta;
		
		if((lastmsgcanal = this.bd.getEstadoCanal(ncanal)) < 0){
			r2 = new RespSaldoError(this.idbanco, this.idconsorcio, ncanal,nmsg,true, CodigosError.CANALOCUP);
			this.enviarMensaje(r2, "Mensaxe enviada: Error (Canal Opupado).");
			return;
		}
		
		if (lastmsgcanal + 1 != nmsg){
			r2 = new RespSaldoError(this.idbanco, this.idconsorcio, ncanal,nmsg,true, CodigosError.FUERASEC);
			this.enviarMensaje(r2, "Mensaxe enviada: Error (Canal Opupado).");
			return;
		}
		
		if(this.bd.getTarxeta(numtarx) != null){
			r = new RespSaldo(this.idbanco, this.idconsorcio, ncanal,nmsg,true, CodigosRespuesta.TARJETANVALIDA, false, 0);
			this.enviarMensaje(r, "Mensaxe enviada: Error (Tarxeta Invalida).");
			return;
		}
		
		if((conta = this.bd.getConta(numtarx, numConta)) == null){
			r = new RespSaldo(this.idbanco, this.idconsorcio, ncanal,nmsg,true, CodigosRespuesta.CUENTANVALIDA, false, 0);
			this.enviarMensaje(r, "Mensaxe enviada: Error (Conta Invalida).");
			return;
		}
		
		r = new RespSaldo(this.idbanco, this.idconsorcio, ncanal,nmsg,true, CodigosRespuesta.CONSACEPTADA, conta.getSaldo() >= 0, conta.getSaldo());
		this.enviarMensaje(r, "Mensaxe enviada: Consulta Aceptada (Saldo = " + conta.getSaldo() + ").");
		this.iu.actualizar();
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
		RespMovimientos r;
		RespMovimientosError r2;
		int lastmsgcanal, ind;
		ArrayList<Movemento> movs;
		Conta conta;
		
		if((lastmsgcanal = this.bd.getEstadoCanal(ncanal)) < 0){
			r2 = new RespMovimientosError(this.idbanco, this.idconsorcio, ncanal,nmsg,true, CodigosError.CANALOCUP);
			this.enviarMensaje(r2, "Mensaxe enviada: Error (Canal Opupado).");
			return;
		}
		
		if (lastmsgcanal != nmsg +1){
			r2 = new RespMovimientosError(this.idbanco, this.idconsorcio, ncanal,nmsg,true, CodigosError.FUERASEC);
			this.enviarMensaje(r2, "Mensaxe enviada: Error (Canal Opupado).");
			return;
		}
		
		if(this.bd.getTarxeta(numtarx) != null){
			r = new RespMovimientos(this.idbanco, this.idconsorcio, ncanal,nmsg,true, CodigosRespuesta.TARJETANVALIDA, 0, null, false, 0, null);
			this.enviarMensaje(r, "Mensaxe enviada: Error (Tarxeta Invalida).");
			return;
		}
		
		if((conta = this.bd.getConta(numtarx, numConta)) == null){
			r = new RespMovimientos(this.idbanco, this.idconsorcio, ncanal,nmsg,true, CodigosRespuesta.CUENTANVALIDA, 0, null, false, 0, null);
			this.enviarMensaje(r, "Mensaxe enviada: Error (Conta Invalida).");
			return;
		}
		
		
		movs = this.getMovementosConta(conta.getNumero());
		ind = movs.size() >= 20 ? 20 : movs.size();
		for (Movemento m : movs){
			try {
				r = new RespMovimientos(this.idbanco, this.idconsorcio, ncanal, nmsg, true, CodigosRespuesta.CONSACEPTADA, ind--,
						CodigosMovimiento.getTipoMovimiento(m.codigo), m.importe >= 0, m.importe > 0 ? m.importe : - m.importe, m.data);
				this.enviarMensaje(r, "Mensaxe enviada: Movemento("+ CodigosMovimiento.getTipoMovimiento(m.codigo) + ").");
			} catch (CodigoNoValidoException e) {
				e.printStackTrace();
			}
		}
		this.iu.actualizar();
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
		int lastmsgcanal;
		RespReintegro r;
		RespReintegroError r2;
		Conta conta;
		
		if((lastmsgcanal = this.bd.getEstadoCanal(ncanal)) < 0){
			r2 = new RespReintegroError(this.idbanco, this.idconsorcio, ncanal,nmsg,true, CodigosError.CANALOCUP);
			this.enviarMensaje(r2, "Mensaxe enviada: Error (Canal Opupado).");
			return;
		}
		
		if (lastmsgcanal != nmsg +1){
			r2 = new RespReintegroError(this.idbanco, this.idconsorcio, ncanal,nmsg,true, CodigosError.FUERASEC);
			this.enviarMensaje(r2, "Mensaxe enviada: Error (Canal Opupado).");
			return;
		}
		
		if(this.bd.getTarxeta(numtarx) != null){
			r = new RespReintegro(this.idbanco, this.idconsorcio, ncanal,nmsg,true, CodigosRespuesta.TARJETANVALIDA, false, 0);
			this.enviarMensaje(r, "Mensaxe enviada: Error (Tarxeta Invalida).");
			return;
		}
		
		if((conta = this.bd.getConta(numtarx, numConta)) == null){
			r = new RespReintegro(this.idbanco, this.idconsorcio, ncanal,nmsg,true, CodigosRespuesta.CUENTANVALIDA, false, 0);
			this.enviarMensaje(r, "Mensaxe enviada: Error (Conta Invalida).");
			return;
		}
		
		this.bd.facerReintegro(conta.getNumero(), importe);
		r = new RespReintegro(this.idbanco, this.idconsorcio, ncanal,nmsg,true, CodigosRespuesta.CONSACEPTADA, conta.getSaldo() >= 0, conta.getSaldo()+importe);
		this.enviarMensaje(r, "Mensaxe enviada: Consulta Aceptada (Saldo = " + conta.getSaldo() + ").");
		this.iu.actualizar();
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
		
		int lastmsgcanal;
		RespAbono r;
		RespAbonoError r2;
		Conta conta;
		
		if((lastmsgcanal = this.bd.getEstadoCanal(ncanal)) < 0){
			r2 = new RespAbonoError(this.idbanco, this.idconsorcio, ncanal,nmsg+1,true, CodigosError.CANALOCUP);
			this.enviarMensaje(r2, "Mensaxe enviada: Error (Canal Opupado).");
			return;
		}
		
		if (lastmsgcanal != nmsg +1){
			r2 = new RespAbonoError(this.idbanco, this.idconsorcio, ncanal,nmsg+1,true, CodigosError.FUERASEC);
			this.enviarMensaje(r2, "Mensaxe enviada: Error (Canal Opupado).");
			return;
		}
		
		if(this.bd.getTarxeta(numtarx) != null){
			r = new RespAbono(this.idbanco, this.idconsorcio, ncanal,nmsg+1,true, CodigosRespuesta.TARJETANVALIDA, false, 0);
			this.enviarMensaje(r, "Mensaxe enviada: Error (Tarxeta Invalida).");
			return;
		}
		
		if((conta = this.bd.getConta(numtarx, numConta)) == null){
			r = new RespAbono(this.idbanco, this.idconsorcio, ncanal,nmsg+1,true, CodigosRespuesta.CUENTANVALIDA, false, 0);
			this.enviarMensaje(r, "Mensaxe enviada: Error (Conta Invalida).");
			return;
		}
		
		if(conta.getSaldo() < importe){
			r = new RespAbono(this.idbanco, this.idconsorcio, ncanal,nmsg+1,true, CodigosRespuesta.IMPORTEEXCLIMITE, false, 0);
			this.enviarMensaje(r, "Mensaxe enviada: Error (Saldo insuficiente).");
			return;
		}
		
		this.bd.facerAbono(conta.getNumero(), importe);
		r = new RespAbono(this.idbanco, this.idconsorcio, ncanal,nmsg+1,true, CodigosRespuesta.CONSACEPTADA, conta.getSaldo() >= 0, conta.getSaldo()-importe);
		this.enviarMensaje(r, "Mensaxe enviada: Consulta Aceptada (Saldo = " + conta.getSaldo() + ").");
		this.iu.actualizar();
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
		int lastmsgcanal;
		RespTraspaso r;
		RespTraspasoError r2;
		Conta contaori, contades;
		
	
		if((lastmsgcanal = this.bd.getEstadoCanal(ncanal)) < 0){
			r2 = new RespTraspasoError(this.idbanco, this.idconsorcio, ncanal,nmsg+1,true, CodigosError.CANALOCUP);
			this.enviarMensaje(r2,"Mensaxe enviada: Error (Canal Opupado).");
			return;
		}
		
		if (lastmsgcanal != nmsg +1){
			r2 = new RespTraspasoError(this.idbanco, this.idconsorcio, ncanal, nmsg, true, CodigosError.FUERASEC);
			this.enviarMensaje(r2,"Mensaxe enviada: Error (Canal Opupado).");
			return;
		}
		
		if(this.bd.getTarxeta(numtarx) != null){
			r = new RespTraspaso(this.idbanco, this.idconsorcio, ncanal,nmsg, true, CodigosRespuesta.TARJETANVALIDA, false, 0, online, lastmsgcanal);
			this.enviarMensaje(r,"Mensaxe enviada: Error (Tarxeta Invalida).");
			return;
		}
		
		if((contaori = this.bd.getConta(numtarx, numContaOrigen)) == null){
			r = new RespTraspaso(this.idbanco, this.idconsorcio, ncanal,nmsg, true, CodigosRespuesta.CUENTANVALIDA, false, 0, online, lastmsgcanal);
			this.enviarMensaje(r,"Mensaxe enviada: Error (Conta Invalida).");
			return;
		}
		
		if(contaori.getSaldo() < importe){
			r = new RespTraspaso(this.idbanco, this.idconsorcio, ncanal, nmsg, true, CodigosRespuesta.IMPORTEEXCLIMITE, false, 0, online, lastmsgcanal);
			this.enviarMensaje(r,"Mensaxe enviada: Error (Saldo insuficiente).");
			return;}
		
		if((contades = this.bd.getConta(numtarx, numContaDestino)) == null){
			r = new RespTraspaso(this.idbanco, this.idconsorcio, ncanal, nmsg, true, CodigosRespuesta.CUENTANVALIDA, false, 0, online, lastmsgcanal);
			this.enviarMensaje(r,"Mensaxe enviada: Error (Conta Invalida).");
			return;
		}
		
		this.bd.facerTraspaso(contaori.getNumero(),contades.getNumero(),importe);
		r = new RespTraspaso(this.idbanco, this.idconsorcio, ncanal, nmsg, true, CodigosRespuesta.CONSACEPTADA,
				(contaori.getSaldo() - importe) >= 0, contaori.getSaldo() - importe, 
				(contades.getSaldo() + importe) >= 0, contades.getSaldo() + importe);
		this.enviarMensaje(r,"Mensaxe enviada: Consulta Aceptada.");
		this.iu.actualizar();
	}


	private boolean esEnviado(Mensaje msx) {
		msx.getTipoMensaje().equals(CodigosMensajes.RESABONO);
		msx.getTipoMensaje().equals(CodigosMensajes.RESMOVIMIENTOS);
		msx.getTipoMensaje().equals(CodigosMensajes.RESREINTEGRO);
		msx.getTipoMensaje().equals(CodigosMensajes.RESSALDO);
		msx.getTipoMensaje().equals(CodigosMensajes.RESTRASPASO);
		msx.getTipoMensaje().equals(CodigosMensajes.SOLABRIRSESION);
		msx.getTipoMensaje().equals(CodigosMensajes.SOLCIERRESESION);
		msx.getTipoMensaje().equals(CodigosMensajes.SOLDETENERTRAFICO);
		msx.getTipoMensaje().equals(CodigosMensajes.SOLREANUDARTRAFICO);
		msx.getTipoMensaje().equals(CodigosMensajes.RESFINREC);
		msx.getTipoMensaje().equals(CodigosMensajes.RESINIREC);
		return false;
	}


	private boolean eMensaxeDatos(Mensaje msx) {
		msx.getTipoMensaje().equals(CodigosMensajes.RESABONO);
		msx.getTipoMensaje().equals(CodigosMensajes.RESMOVIMIENTOS);
		msx.getTipoMensaje().equals(CodigosMensajes.RESREINTEGRO);
		msx.getTipoMensaje().equals(CodigosMensajes.RESSALDO);
		msx.getTipoMensaje().equals(CodigosMensajes.RESTRASPASO);
		msx.getTipoMensaje().equals(CodigosMensajes.SOLABONO);
		msx.getTipoMensaje().equals(CodigosMensajes.SOLMOVIMIENTOS);
		msx.getTipoMensaje().equals(CodigosMensajes.SOLREINTEGRO);
		msx.getTipoMensaje().equals(CodigosMensajes.SOLSALDO);
		msx.getTipoMensaje().equals(CodigosMensajes.SOLTRASPASO);
		return false;
	}
	
	private void rexistrarMensaxe(Mensaje m, String s){
		if(m != null){
			if(eMensaxeDatos(m)){
				this.bd.registrarMensaje(	this.idSesion,
											((MensajeDatos) m).getNumcanal(),
											((MensajeDatos) m).getNmsg(),
											m.getTipoMensaje().getNum(),
											esEnviado(m),
											s);
				
			}else{
				this.bd.registrarMensaje(	this.idSesion,
											null,
											null,
											m.getTipoMensaje().getNum(),
											esEnviado(m),
											s);
			}
		}else{
			this.bd.registrarMensaje(	this.idSesion,
										null,
										null,
										null,
										false,
										s);
		}
	}
	
	private void cambEstado(EstadoSesion nuevoEstado) {
		this.estado = nuevoEstado;
	}


	
	public void setResponderMensaxes(boolean b) {
		this.responderMensaxes = b;
		this.iu.engadirLinhaLog("Non se responden as mensaxes.\n");
	}


	public boolean sesionAberta() {
		return this.estado.sesionAberta();
	}


	public boolean traficoActivo() {
		return this.estado.traficoActivo();
	}
	
	private void enviarMensaje(Mensaje m, String string) {
		if (this.responderMensaxes){
			try {
				this.cs.enviarMensaje(m);
				rexistrarMensaxe(m, m.toString());
				this.iu.engadirLinhaLog(string);
			} catch (InterruptedException e) {
				this.iu.engadirLinhaLog("Error:: " + e.getLocalizedMessage());
			}
		}
	}

}