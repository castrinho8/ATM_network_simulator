package practicaACS.consorcio;

import java.io.IOException;
import java.util.ArrayList;

import practicaACS.bancos.Banco;
import practicaACS.cajeros.Cajero;

public class MainConsorcio {
	
	public static void main(String[] args) throws IOException {
		ArrayList<Banco> bancos = new ArrayList<Banco>();
		ArrayList<Cajero> cajeros_clientes = new ArrayList<Cajero>();

		bancos.add(new Banco());
		bancos.add(new Banco());
		bancos.add(new Banco());
		
		cajeros_clientes.add(new Cajero());
		cajeros_clientes.add(new Cajero());
		cajeros_clientes.add(new Cajero());
		cajeros_clientes.add(new Cajero());
		
		Consorcio cons = new Consorcio(bancos, cajeros_clientes);
		
		cons.getCajeros_server().levantar_servidorCajeros();
		
		
	}
}
