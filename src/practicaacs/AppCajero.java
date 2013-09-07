package practicaacs;

import java.io.IOException;

import javax.swing.UnsupportedLookAndFeelException;

import practicaacs.banco.Banco;
import practicaacs.cajeros.Cajero;

public class AppCajero {
	
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
    		if(args.length == 1){
    			System.out.println("Cargando configuracion...");
    			new Cajero(args[0]);
    		}else{
	    		new Cajero("/home/castrinho8/Escritorio/UNI/ACS/res/cajero1.properties");
	    		//new Cajero("/home/ch01/RepositorioPractica/res/cajero1.properties");
    		}
    	} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
