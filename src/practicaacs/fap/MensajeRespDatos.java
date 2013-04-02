package practicaacs.fap;

public class MensajeRespDatos extends MensajeDatos {

	CodigosRespuesta cod_resp;
	
	public MensajeRespDatos(String origen, String destino,
			CodigosMensajes tipoMensaje, int numcanal, int nmsg,
			boolean codonline, CodigosRespuesta cod_resp) {
		super(origen, destino, tipoMensaje, numcanal, nmsg, codonline);
		this.cod_resp = cod_resp;
	}

	@Override
	protected String printCabecera() {
		return super.printCabecera() + "";
	}
	
}
