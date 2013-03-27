package banco;

public class Banco {
	
	private String nombre;
	private int cdgBanco;
	
	public Banco(String nombre){
		this.nombre = nombre;
	}
	
	private void inicializar(){
	}
	
	private void addBancoDB(){
		
	}
	
	public void addTarjeta(){
		
	}
	
	public void addCuenta(){
		
	}
	
	public void addMovimiento(){
		
	}
	
	/**
	 * Se crean las tablas de banco y de sesion si no están creadas.
	 */
	public static void inicializarBD(){
		//Crear tablas si no están creadas.
	}

    public String getName() {
    	return this.nombre;
    }
	
}