package practicaacs.cajeros.iu;

import practicaacs.cajeros.Envio;
import practicaacs.fap.MensajeDatos;

abstract public class ConsultaAbstracta extends javax.swing.JFrame {

	abstract public void envia_consulta(Envio env);
    
    abstract public void actualizarIU(MensajeDatos message);

	
}
