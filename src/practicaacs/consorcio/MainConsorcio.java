package practicaACS.consorcio;

import java.io.IOException;
import java.util.ArrayList;

public class MainConsorcio {
	
	public static void main(String[] args) throws IOException {
		Consorcio cons = new Consorcio();
		cons.getCajeros_server().levantar_servidorCajeros();
		
		
	}
}
