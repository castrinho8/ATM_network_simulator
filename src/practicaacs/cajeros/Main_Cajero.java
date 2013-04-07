package cajeros;

import java.io.IOException;
import java.util.ArrayList;

import fap.*;

//INTERFAZ CAJERO
public class Main_Cajero {

	public static void main(String[] args) throws ClassNotFoundException, IOException {
		Cajero caj = new Cajero();
		
		System.out.print(caj.consultar_saldo(124));
		
		
	}
	  
}

