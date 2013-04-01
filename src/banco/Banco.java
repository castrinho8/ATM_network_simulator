package banco;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

import fap.CodigosMensajes;
import fap.Mensaje;
import fap.MensajeNoValidoException;

import banco.csconsorcio.AnalizadorMensajes;
import banco.csconsorcio.ClienteServidorConsorcio;
import banco.iu.VentanaBanco;

public class Banco implements AnalizadorMensajes{
	
	private ClienteBDBanco bd;
	private VentanaBanco iu;
	private ClienteServidorConsorcio cs;
	
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
		this.cs = new ClienteServidorConsorcio(puerto, this);
		this.cs.start();
		
		this.iu = new VentanaBanco(this,prop.getProperty("banco.name"));
		this.iu.setVisible(true);

	}
	
	public Banco(String nombre, String bdurl, int puerto) {
		
		this.bd = new ClienteBDBanco(bdurl);
		
		this.cs = new ClienteServidorConsorcio(puerto, this);
		this.cs.start();
		
		this.iu = new VentanaBanco(this, nombre);
		this.iu.setVisible(true);
		
	}

	public void setIU(VentanaBanco iu){
		this.iu = iu;
	}
	
	public void engadirConta(Conta c){
		bd.engadirConta(c.getNumero(),c.getSaldo());
	}
	
	public void eliminarConta(int codigo){
		bd.eliminarConta(codigo);
	}
	
	public void eliminarConta(Conta c){
		this.eliminarConta(c.getNumero());
	}
	
	public void engadirTarxeta(Tarxeta t){
		this.engadirTarxeta(t.getCodigo());
	}
	
	public void engadirTarxeta(int codigo){
		bd.engadirTarxeta(codigo);
	}
	
	public void eliminarTarxeta(int codigo){
		bd.eliminarTarxeta(codigo);
	}
	
	public void engadirContaAsociada(int cdgtarxeta, int cdgconta){
		bd.engadirContaAsociada(cdgtarxeta, cdgconta);
	}
	
	public void eliminarContaAsociada(int cdgtarxeta, int cdgconta){
		bd.eliminarContaAsociada(cdgtarxeta,cdgconta);
	}
	
	public void engadirMovemento(int codconta){
		//TODO
	}
	
	public ArrayList<Conta> getContas(){
	 	return bd.getContas();
	}
	
	public ArrayList<Tarxeta> getTarxetas(){
		return bd.getTarxetas();
	}
	
	public ArrayList<Conta> getContasAsociadas(int cdgtar){
		return bd.getContasAsociadas(cdgtar);
	}
	
	public ArrayList<Conta> getContasAsociadas(Tarxeta t){
		return bd.getContasAsociadas(t.getCodigo());
	}
	
	public ArrayList<Movemento> getMovementosConta(int nconta) {
		return bd.getMovementos(nconta);
	}
	
	public ArrayList<Movemento> getMovementosConta(Conta c){
		return this.getMovementosConta(c.getNumero());
	}

	public void establecerValoresPorDefecto() {
		bd.valoresPorDefecto();
	}

	public void cerrarSesion() {
		// TODO Auto-generated method stub
		
	}


	public void iniciarSesion(int numCanles) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void analizarMensaje(byte[] bs) {
		this.iu.engadirLinhaLog("MensaxeRecibida!!\n");
		CodigosMensajes cod;
		
		Mensaje msx;
		try {
			msx = Mensaje.parse(bs);
			cod = msx.getTipoMensaje();
		} catch (MensajeNoValidoException e) {
			cod = null;
		}
		switch(cod){
		case SOLTRAFICOREC:
			break;
		case SOLFINTRADICOREC:
			break;
		case ABRIRSESION:
			break;
		case DETENERTRAFICO:
			break;
		case REANUDARTRAFICO:
			break;
		case CIERRESESION:
			break;
		case CONSULTARSALDO:
			break;
		case CONSULTARMOVIMIENTOS:
			break;
		case REINTEGRO:
			break;
		case ABONO:
			break;
		case TRASPASO:
			break;
		default:
		}
	}

	
	
}