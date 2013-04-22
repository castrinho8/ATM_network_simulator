package practicaacs.fap;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public abstract class Mensaje implements java.io.Serializable {

	private String origen;
	private String destino;
	private CodigosMensajes tipoMensaje;
	private static HashMap<CodigosMensajes,Class<? extends Mensaje>> codigo_clase;
	
	public Mensaje(String origen, String destino,CodigosMensajes tipoMensaje){
		assert(origen.length()<=8);
		assert(destino.length()<=8);
		
		this.tipoMensaje = tipoMensaje;
		this.origen = origen;
		this.destino = destino;
		
		if(codigo_clase == null){
			codigo_clase = new HashMap<CodigosMensajes,Class<? extends Mensaje>>();
			codigo_clase.put(CodigosMensajes.SOLINIREC,SolIniTraficoRec.class);
			codigo_clase.put(CodigosMensajes.SOLFINREC,SolFinTraficoRec.class);
			codigo_clase.put(CodigosMensajes.SOLABRIRSESION, SolAperturaSesion.class );
			codigo_clase.put(CodigosMensajes.SOLDETENERTRAFICO,  SolDetTrafico.class);
			codigo_clase.put(CodigosMensajes.SOLREANUDARTRAFICO, SolReanTrafico.class);
			codigo_clase.put(CodigosMensajes.SOLCIERRESESION, SolCierreSesion.class);
			codigo_clase.put(CodigosMensajes.SOLSALDO, SolSaldo.class);
			codigo_clase.put(CodigosMensajes.SOLMOVIMIENTOS, SolMovimientos.class);
			codigo_clase.put(CodigosMensajes.SOLREINTEGRO, SolReintegro.class);
			codigo_clase.put(CodigosMensajes.SOLABONO,  SolAbono.class);
			codigo_clase.put(CodigosMensajes.SOLTRASPASO, SolTraspaso.class);
			
			codigo_clase.put(CodigosMensajes.RESINIREC, RespIniTraficoRec.class);
			codigo_clase.put(CodigosMensajes.RESFINREC, RespFinTraficoRec.class);
			codigo_clase.put(CodigosMensajes.RESABRIRSESION, RespAperturaSesion.class);
			codigo_clase.put(CodigosMensajes.RESCIERRESESION, RespCierreSesion.class);
			codigo_clase.put(CodigosMensajes.RESDETENERTRAFICO, RespDetTrafico.class);
			codigo_clase.put(CodigosMensajes.RESREANUDARTRAFICO, RespReanTrafico.class);
			codigo_clase.put(CodigosMensajes.RESSALDO, RespSaldo.class);
			codigo_clase.put(CodigosMensajes.RESMOVIMIENTOS, RespMovimientos.class);
			codigo_clase.put(CodigosMensajes.RESREINTEGRO, RespReintegro.class);
			codigo_clase.put(CodigosMensajes.RESABONO, RespAbono.class);
			codigo_clase.put(CodigosMensajes.RESTRASPASO, RespTraspaso.class);
		}
		
	}

	
	public Mensaje() {
		super();
	}

	public String getOrigen() {
		return origen;
	}

	public String getDestino() {
		return destino;
	}

	public CodigosMensajes getTipoMensaje() {
		return tipoMensaje;
	}

	public boolean es_consulta(){
		return ((tipoMensaje == CodigosMensajes.SOLSALDO) | (tipoMensaje == CodigosMensajes.SOLMOVIMIENTOS));
	}

	
	@Override
	public String toString(){
		return this.printCabecera() + this.printCuerpo(); 
	}

	protected String printCuerpo() {
		return "";
	}

	protected String printCabecera() {
		return String.format("%8s%8s%2d", origen,destino,this.tipoMensaje.getNum());
	}
	
	public int size() {
		return this.toString().length();
	}

	public byte[] getBytes() {
		return this.toString().getBytes();
	}

	public static Mensaje parse(byte[] bs) throws MensajeNoValidoException {
		CodigosMensajes tipo;
		
		try {
			tipo = CodigosMensajes.parse(bs.toString().substring(16, 18));
		} catch (CodigoNoValidoException e) {
			throw new MensajeNoValidoException();
		}
			
		try {
			Mensaje m = Mensaje.codigo_clase.get(tipo).getConstructor(new Class<?>[]{}).newInstance();
			m.parseComp(bs);
			return m;
		}catch (IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	protected void parseComp(byte[] bs) throws MensajeNoValidoException{
		try{
			this.origen = bs.toString().substring(0, 7);
			this.destino = bs.toString().substring(8, 15);
			this.tipoMensaje = CodigosMensajes.parse(bs.toString().substring(16, 18));
		}catch(CodigoNoValidoException | NumberFormatException e){
			throw new MensajeNoValidoException();
		}
	}

}
