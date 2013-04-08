package practicaacs.fap;

public enum CodigosMensajes {
	SOLINIREC(01), SOLFINREC(02),RESABRIRSESION(11),RESDETENERTRAFICO(13),
	RESREANUDARTRAFICO(14),RESCIERRESESION(12),SOLSALDO(31),SOLMOVIMIENTOS(32),
	SOLREINTEGRO(33),SOLABONO(34),SOLTRASPASO(35),RESINIREC(91),RESFINREC(92),SOLABRIRSESION(81),
	SOLDETENERTRAFICO(83),SOLREANUDARTRAFICO(84),SOLCIERRESESION(82),RESSALDO(61),
	RESMOVIMIENTOS(62),RESREINTEGRO(63),RESABONO(64),RESTRASPASO(65);
	private int numero;

	private CodigosMensajes(int numero){
		this.numero = numero;
	}
	
	public int getNum(){
		return this.numero;
	}

	public static CodigosMensajes parse(String s) throws CodigoNoValidoException {
		try{
			int numero = new Integer(s);
			for(CodigosMensajes c : CodigosMensajes.values()){
				if(c.numero == numero){
					return c;
				}
			}
		}catch(NumberFormatException e){}
		
		throw new CodigoNoValidoException();
	}
}
