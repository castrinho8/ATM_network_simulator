package practicaacs.consorcio.aux;

import practicaacs.fap.Mensaje;

public class MensajeCajero {
	
	private Mensaje mensaje;
	private String id_cajero;
	private boolean contestado;
	private int canal;
	
	public MensajeCajero(Mensaje mensaje, String id_cajero,boolean cont,int can) {
		super();
		this.mensaje = mensaje;
		this.id_cajero = id_cajero;
		this.contestado = cont;
		this.canal = can;
	}

	public boolean isContestado() {
		return contestado;
	}

	public Mensaje getMensaje() {
		return mensaje;
	}

	public String getId_cajero() {
		return id_cajero;
	}
	
	public int getCanal(){
		return canal;
	}

}
