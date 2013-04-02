package practicaacs;

import javax.swing.UnsupportedLookAndFeelException;

import practicaacs.banco.Banco;

public class AppBanco {

	/**
	 * @param args
	 */
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
		
		if(args.length == 1){
			System.out.println("Cargando configuracion...");
			new Banco(args[0]);
		}else{
			System.out.println("Cargando Banco por defecto...");
			new Banco("Santander","jdbc:mysql://localhost/acs?user=acsuser&password=password", 12334,"localhost",2424);			
		}
	}

}
