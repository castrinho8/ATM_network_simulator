package practicaacs.cajeros;

import java.io.IOException;

import practicaacs.fap.Mensaje;
import practicaacs.fap.SolSaldo;

//INTERFAZ CAJERO
public class Main_Cajero {

	public static void main(String[] args) throws ClassNotFoundException, IOException {
		Cajero caj = new Cajero("/home/castrinho8/Escritorio/UNI/ACS/res/configuracion");
		
		SolSaldo m = (SolSaldo) caj.realizar_consulta("1234",567);
		
		System.out.printf("Mensaje: " + m.toString() + "\n");
	}
	  
    public Mensaje obtener_mensaje(){
    	return null;
    }
}

