package banco;

import banco.iu.VentanaBanco;

public class AppBanco {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Banco b;
		
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("GTK+".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(VentanaBanco.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VentanaBanco.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VentanaBanco.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VentanaBanco.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
		
		if(args.length == 1){
			System.out.println("Cargando configuracion...");
			b = new Banco(args[0]);
		}else{
			System.out.println("Cargando Banco por defecto...");
			b = new Banco("Santander","jdbc:mysql://localhost/acs?user=acsuser&password=password", 1234);			
		}
		
		VentanaBanco iu = new VentanaBanco(b);
		iu.setVisible(true);
	}

}
