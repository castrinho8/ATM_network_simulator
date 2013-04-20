package practicaacs.cajeros;

import practicaacs.fap.CodigosMensajes;

public class Envio {

	private String num_tarjeta;

	private CodigosMensajes tipoMensaje;
	private int importe;
	
	private int num_cuenta_origen;
	private int num_cuenta_destino;
	
	
	public Envio(String tarjeta, int cuenta) {
		this.num_tarjeta = tarjeta;
		this.num_cuenta_origen = cuenta;
	}


	public void setNum_tarjeta(String num_tarjeta) {
		this.num_tarjeta = num_tarjeta;
	}

	public void setTipoMensaje(CodigosMensajes tipoMensaje) {
		this.tipoMensaje = tipoMensaje;
	}


	public void setImporte(int importe) {
		this.importe = importe;
	}


	public void setNum_cuenta_origen(int num_cuenta_origen) {
		this.num_cuenta_origen = num_cuenta_origen;
	}


	public void setNum_cuenta_destino(int num_cuenta_destino) {
		this.num_cuenta_destino = num_cuenta_destino;
	}
	
	

	
}
