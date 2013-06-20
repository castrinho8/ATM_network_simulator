package practicaacs.cajeros.iu;

import java.io.IOException;

import javax.swing.JFrame;

import practicaacs.cajeros.Cajero;
import practicaacs.cajeros.Envio;
import practicaacs.fap.CodigosMensajes;
import practicaacs.fap.Mensaje;
import practicaacs.fap.MensajeDatos;
import practicaacs.fap.RespAbono;
import practicaacs.fap.RespTraspaso;


public class RealizarTraspaso_IU extends ConsultaAbstracta {

    JFrame parent;
    Envio envio;
    Cajero cajero;
    
    /**
     * Creates new form RealizarTraspaso_IU
     */
    public RealizarTraspaso_IU(JFrame padre,Cajero caj,Envio env) {
        this.parent = padre;
        this.envio = env;
        this.cajero = caj;
        initComponents();
        inicializa_visibilidades();
        this.setLocationRelativeTo(null);
    }

    private void inicializa_visibilidades(){
        this.EsperandoRespuestaLabel.setVisible(false);
        this.SaldoLabel.setVisible(false);
        this.SaldoText.setVisible(false);
        this.ErrorLabel.setVisible(false);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        ImporteText = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        SaldoLabel = new javax.swing.JLabel();
        AceptarButton = new javax.swing.JButton();
        EsperandoRespuestaLabel = new javax.swing.JLabel();
        SaldoText = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        FinalizarButton = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        CuentaText = new javax.swing.JTextField();
        ErrorLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Traspaso");

        ImporteText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ImporteTextActionPerformed(evt);
            }
        });

        jLabel2.setText("Introduzca el importe:");

        SaldoLabel.setText("Saldo actual:");

        AceptarButton.setText("Aceptar");
        AceptarButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AceptarButtonActionPerformed(evt);
            }
        });

        EsperandoRespuestaLabel.setText("Esperando respuesta...");

        SaldoText.setEditable(false);

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("TRASPASO");

        FinalizarButton.setText("Finalizar");
        FinalizarButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FinalizarButtonActionPerformed(evt);
            }
        });

        jLabel3.setText("Introduzca la cuenta destino:");

        CuentaText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CuentaTextActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(CuentaText)
                    .addComponent(ImporteText, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(SaldoLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE)
                    .addComponent(SaldoText, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(EsperandoRespuestaLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(AceptarButton))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(FinalizarButton, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(ErrorLabel, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ImporteText, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(CuentaText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(ErrorLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(EsperandoRespuestaLabel)
                    .addComponent(AceptarButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(SaldoLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(SaldoText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(FinalizarButton)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ImporteTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ImporteTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ImporteTextActionPerformed

    /**
     * Metodo que se ejecuta cuando se pulsa el boton aceptar
     * @param evt El evento.
     */
    private void AceptarButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AceptarButtonActionPerformed
        try{
	    	inicializa_visibilidades();
	        String importe = this.ImporteText.getText();
	        String cdestino = this.CuentaText.getText();
	        int importe_traspaso = 0;
	        int cuenta_destino = 0;
	        
	        //Comprobamos si se ha introducido la cuenta y el importe
	        if(importe.equals("") | cdestino.equals("")){
	        	throw new NumberFormatException("No hay introducido importe o cuenta destino...");
	        }
	        
	        //Comprobamos si es correcto el importe introducido (si es un numero)
	        try{
	        	importe_traspaso = Integer.parseInt(importe);
	        }catch(NumberFormatException nfe){
        		throw new NumberFormatException("Importe incorrecto...");
	        }
	        
	        //Comprobamos si es correcta la cuenta introducida
	        try{
	            if(cdestino.length()!=1)
	        		throw new NumberFormatException();
	        	cuenta_destino = Integer.parseInt(cdestino);
	        }catch(NumberFormatException nfe){
	    		throw new NumberFormatException("Cuenta destino incorrecta...");
	        }
        
	        //Comprobamos si la cuenta origen y destino son la misma
	        if(cuenta_destino == this.envio.getNum_cuenta_origen()){
	        	throw new NumberFormatException("La cuenta origen y destino son la misma...");
	        }
		    
	        this.EsperandoRespuestaLabel.setVisible(true);

	        //Añadimos los componentes del envio
	        this.envio.setTipoMensaje(CodigosMensajes.SOLTRASPASO);
	        this.envio.setImporte(importe_traspaso);
	        this.envio.setNum_cuenta_destino(cuenta_destino);
	        envia_consulta(this.envio);
	        
        }catch(NumberFormatException nfe){
            this.ErrorLabel.setText(nfe.getMessage());
            this.ErrorLabel.setVisible(true);
            return;
        }
    }//GEN-LAST:event_AceptarButtonActionPerformed

    private void FinalizarButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FinalizarButtonActionPerformed
        if(this.parent != null)
            this.parent.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_FinalizarButtonActionPerformed

    private void CuentaTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CuentaTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CuentaTextActionPerformed

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
            java.util.logging.Logger.getLogger(RealizarTraspaso_IU.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(RealizarTraspaso_IU.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(RealizarTraspaso_IU.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(RealizarTraspaso_IU.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new RealizarTraspaso_IU(null,null,null).setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AceptarButton;
    private javax.swing.JTextField CuentaText;
    private javax.swing.JLabel ErrorLabel;
    private javax.swing.JLabel EsperandoRespuestaLabel;
    private javax.swing.JButton FinalizarButton;
    private javax.swing.JTextField ImporteText;
    private javax.swing.JLabel SaldoLabel;
    private javax.swing.JTextField SaldoText;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    // End of variables declaration//GEN-END:variables
    
    
	@Override
    public void envia_consulta(Envio env){
    	env.setTipoMensaje(CodigosMensajes.SOLTRASPASO);
    	Mensaje envio = this.cajero.crear_mensaje(env);
    	this.cajero.enviar_mensaje(envio,this);
    }
    
	@Override
    public void actualizarIU(MensajeDatos respuesta){
		this.SaldoText.setText(String.valueOf("ORIGEN: " + String.valueOf(((RespTraspaso)respuesta).getSaldoOrigen()) +
				"\nDESTINO: " + String.valueOf(((RespTraspaso)respuesta).getSaldoDestino())));
        this.EsperandoRespuestaLabel.setVisible(false);
        this.ErrorLabel.setVisible(false);
        this.SaldoLabel.setVisible(true);
        this.SaldoText.setVisible(true);
    }
    
    
}
