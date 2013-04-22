package practicaacs.consorcio;

import java.io.IOException;
import java.util.ArrayList;

public class MainConsorcio {
	
	public static void main(String[] args) throws IOException {
		Consorcio cons = new Consorcio("/home/castrinho8/Escritorio/UNI/ACS/res/configuracion");
		cons.getCajeros_server().levantar_servidorCajeros();
		
		
	}
}
