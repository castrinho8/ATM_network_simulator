package practicaacs;

import java.io.IOException;

import javax.swing.UnsupportedLookAndFeelException;

import practicaacs.consorcio.Consorcio;

public class AppConsorcio {
	
	public static void main(String[] args) {
		
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("GTK+".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (UnsupportedLookAndFeelException | IllegalAccessException | InstantiationException | ClassNotFoundException e1){
        	System.err.println("Error establecendo GTK+ como estilo da interface.");
		}
        
    	try {
			//new Consorcio("/home/ch01/RepositorioPractica/res/consorcio.properties");
			new Consorcio("/home/castrinho8/Escritorio/UNI/ACS/res/consorcio.properties");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
