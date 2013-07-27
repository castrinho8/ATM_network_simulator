package practicaacs.consorcio.aux;

import practicaacs.fap.Mensaje;

public class MensajeCajero {
	
	private Mensaje mensaje;
	private String id_cajero;
	
	public MensajeCajero(Mensaje mensaje, String id_cajero) {
		super();
		this.mensaje = mensaje;
		this.id_cajero = id_cajero;
	}

	public Mensaje getMensaje() {
		return mensaje;
	}

	public String getId_cajero() {
		return id_cajero;
	}
	
	

}
