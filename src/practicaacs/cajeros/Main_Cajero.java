package practicaacs.cajeros;

import java.io.IOException;

import practicaacs.fap.CodigosMensajes;
import practicaacs.fap.Mensaje;
import practicaacs.fap.SolSaldo;

//INTERFAZ CAJERO
public class Main_Cajero {

	public static void main(String[] args) throws ClassNotFoundException, IOException {
		Cajero caj = new Cajero("/home/castrinho8/Escritorio/UNI/ACS/res/configuracion");
		Envio env = new Envio("2111111112",CodigosMensajes.SOLSALDO,100,
				3333,4444);
		SolSaldo enviado = (SolSaldo) caj.crear_mensaje(env);
		
		System.out.printf("Mensaje ENVIADO: " + enviado.toString() + "\n");
		
		Mensaje recibido = caj.enviar_mensaje(enviado);
		
		System.out.printf("Mensaje RECIBIDO: " + recibido.toString() + "\n");
		
	}
}

