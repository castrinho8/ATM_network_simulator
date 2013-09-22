/***************************************************************************
 *	ATM Network Simulator ACS. FIC. UDC. 2012/2013									*
 *	Copyright (C) 2013 by Pablo Castro and Marcos Chavarria 						*
 * <pablo.castro1@udc.es>, <marcos.chavarria@udc.es> 								*
 * 																								*
 * This program is free software; you can redistribute it and/or modify 	*
 * it under the terms of the GNU General Public License as published by 	*
 * the Free Software Foundation; either version 2 of the License, or 		*
 * (at your option) any later version. 												*
 ***************************************************************************/
package practicaacs.cajeros.iu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;

import practicaacs.cajeros.Cajero;
import practicaacs.cajeros.Envio;
import practicaacs.consorcio.bd.Database_lib;
import practicaacs.fap.CodigoNoValidoException;
import practicaacs.fap.CodigosMensajes;
import practicaacs.fap.CodigosRespuesta;
import practicaacs.fap.Mensaje;
import practicaacs.fap.MensajeDatos;
import practicaacs.fap.RespMovimientos;
import practicaacs.fap.RespMovimientosError;
import practicaacs.fap.RespSaldo;

public class ConsultarMovimientos_IU extends ConsultaAbstracta {

    JFrame parent;
    Cajero cajero;
    
    
    /**
     * Creates new form ConsultarMovimientos_IU
     */
    public ConsultarMovimientos_IU(JFrame padre,Cajero caj,Envio env) {
        this.parent = padre;
        this.cajero = caj;
        initComponents();
        this.ConsultandoLabel.setVisible(true);
        envia_consulta(env);
        this.setLocationRelativeTo(null);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        AceptarButton = new javax.swing.JButton();
        ConsultandoLabel = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        listMovimientos = new javax.swing.JList();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Movimientos");

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("CONSULTAR MOVIMIENTOS");

        AceptarButton.setText("Aceptar");
        AceptarButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AceptarButtonActionPerformed(evt);
            }
        });

        ConsultandoLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        ConsultandoLabel.setText("Consultando...");

        listMovimientos.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(listMovimientos);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 604, Short.MAX_VALUE)
                    .addComponent(ConsultandoLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(AceptarButton, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ConsultandoLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(AceptarButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>                        

    private void AceptarButtonActionPerformed(java.awt.event.ActionEvent evt) {                                              
        if(this.parent != null)
            this.parent.setVisible(true);
        this.dispose();
    }                                             

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ConsultarMovimientos_IU.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ConsultarMovimientos_IU.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ConsultarMovimientos_IU.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ConsultarMovimientos_IU.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ConsultarMovimientos_IU(null,null,null).setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify                     
    private javax.swing.JButton AceptarButton;
    private javax.swing.JLabel ConsultandoLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JList listMovimientos;
    // End of variables declaration                
    

	/**
	 * Método que realiza el envio de una operación.
	 * @param env El Envio a partir del cual se creará el mensaje a enviar.
	 */
	@Override
    public void envia_consulta(Envio env){
    	env.setTipoMensaje(CodigosMensajes.SOLMOVIMIENTOS);
    	Mensaje envio = this.cajero.crear_mensaje(env);
    	this.cajero.enviar_mensaje(envio,this);
    }
    
    /**
     * Método que actualiza la IU para la ventana de movimientos.
     * @param lista La lista de mensajes con los movimientos recibida.
     * @throws CodigoNoValidoException Excepción que se lanza cuando se utiliza el método en una consulta no debida.
     */
	@Override
    public void actualizarIUmovimientos(ArrayList<RespMovimientos> lista) throws CodigoNoValidoException{
		this.ConsultandoLabel.setVisible(false);
		
		Iterator<RespMovimientos> it = lista.iterator();
		ArrayList<String> strlist = new ArrayList<String>();
		String texto = null;
		
		while(it.hasNext()){
			RespMovimientos m = (RespMovimientos) it.next();

			if(m.getCodonline() && !m.getCod_resp().respuestaAceptada()){
				texto = "Error: "+m.getCod_resp().getMensaje();
				strlist.clear();
				strlist.add(texto);
				break;
			}
			
			if(m.getCodonline() && m.hayMovimientos()){
				CodigosRespuesta codigo = m.getCod_resp();
				if(codigo.equals(CodigosRespuesta.CONSACEPTADA))
					texto = m.getTipoMov() + " - Importe: " + ((m.getSigno())?"+":"-")+m.getImporte()+"€  Fecha: "+m.getFecha();
				else
					texto = String.valueOf(codigo.getMensaje());
				
				strlist.add(texto);
			}else{
				String[] s =  {(!m.hayMovimientos())?"- No hay movimientos para esta cuenta":"Error: No hay conexion"};
				this.listMovimientos.setListData(s);
				return;
			}
		}
        this.listMovimientos.setListData(strlist.toArray());
	}
	
    /**
     * Método que actualiza la IU para la ventana de movimientos cuando se recibe un error.
     * @param elemento El mensaje respuesta de error recibido.
     * @throws CodigoNoValidoException Excepción que se lanza cuando se utiliza el método en una consulta no debida.
     */	
	@Override
    public void actualizarIUmovimientos(RespMovimientosError elemento) throws CodigoNoValidoException{
		this.ConsultandoLabel.setVisible(false);
		
		ArrayList<String> strlist = new ArrayList<String>();
		String texto = null;

		if(elemento.getCodonline()){
			texto = "Error: "+elemento.getCod_resp().getMensaje();
		}
		else 
			texto = "Error: No hay conexion";

		strlist.add(texto);
        this.listMovimientos.setListData(strlist.toArray());
    }
	
}


