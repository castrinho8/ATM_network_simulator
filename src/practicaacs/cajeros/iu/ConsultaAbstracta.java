package practicaacs.cajeros.iu;

import java.util.ArrayList;

import practicaacs.cajeros.Envio;
import practicaacs.fap.CodigoNoValidoException;
import practicaacs.fap.MensajeDatos;
import practicaacs.fap.RespMovimientos;

abstract public class ConsultaAbstracta extends javax.swing.JFrame {

	abstract public void envia_consulta(Envio env);
    
    public void actualizarIU(MensajeDatos message) throws CodigoNoValidoException{
    	throw new CodigoNoValidoException("Error: El método actualizarIU no puede ser llamado en esta consulta.");
    }
    
    public void actualizarIUmovimientos(ArrayList<RespMovimientos> lista) throws CodigoNoValidoException{
    	throw new CodigoNoValidoException("Error: El método actualizarIUmovimientos solo puede ser llamado para las consultas de movimientos.");
    }

	
}
