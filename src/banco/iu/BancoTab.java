/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package banco.iu;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;

import banco.Banco;

/**
 *
 * @author ch01
 */
public class BancoTab extends javax.swing.JPanel {
    private Banco banco;
    private JButton botonAbrirSesion;
    private JButton botonCerrarSesion;
    private JButton botonDetenerTrafico;
    private JButton botonReanudarTrafico;
    private JLabel labelLog;
    private JLabel labelTabla;
    private JPanel jPanel1;
    private JScrollPane scrollLog;
    private JScrollPane scrollTablaBotones;
    private JTable tablaBanco;
    private JTextArea textLogs;


    public BancoTab(Banco b) {
        banco = b;
        setName(banco.getName());
        initComponents();
        setDatosTabla(new Object[][]{
        		{null,null,null}	
        });
             
    }


    private void initComponents() {

        scrollLog = new JScrollPane();
        textLogs = new JTextArea();
        scrollTablaBotones = new JScrollPane();
        tablaBanco = new JTable();
        jPanel1 = new JPanel();
        botonAbrirSesion = new JButton();
        botonCerrarSesion = new JButton();
        botonDetenerTrafico = new JButton();
        botonReanudarTrafico = new JButton();
        labelLog = new JLabel();
        labelTabla = new JLabel();
        
        textLogs.setColumns(20);
        textLogs.setLineWrap(true);
        textLogs.setRows(5);
        textLogs.setTabSize(4);
        scrollLog.setViewportView(textLogs);
        
        scrollTablaBotones.setViewportView(tablaBanco);

        botonAbrirSesion.setText("Abrir Sesion");
        botonAbrirSesion.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonAbrirSesion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                abrirSesion();
            }
        });

        botonCerrarSesion.setText("Cerrar Sesion");
        botonCerrarSesion.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonCerrarSesion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cerrarSesion();
            }
        });

        botonDetenerTrafico.setText("Detener Trafico");
        botonDetenerTrafico.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonDetenerTrafico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                detenerTrafico();
            }
        });
        
        botonReanudarTrafico.setText("Reanudar Trafico");
        botonReanudarTrafico.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonReanudarTrafico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reanudarTrafico();
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 2, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(botonAbrirSesion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(botonCerrarSesion, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(botonDetenerTrafico, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(botonReanudarTrafico, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE))
                .addContainerGap(13, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(botonAbrirSesion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(botonDetenerTrafico, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(botonCerrarSesion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(botonReanudarTrafico, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(6, 6, 6))
        );

        labelLog.setFont(new java.awt.Font("Cantarell", 1, 15));
        labelLog.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelLog.setText("Mensaxes Log");

        labelTabla.setFont(new java.awt.Font("Cantarell", 1, 15)); // NOI18N
        labelTabla.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelTabla.setText("Estado Contas");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(labelTabla, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(scrollTablaBotones, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(labelLog, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(scrollLog, javax.swing.GroupLayout.DEFAULT_SIZE, 465, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(labelLog, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(scrollLog))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labelTabla, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(scrollTablaBotones, javax.swing.GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE)))
                .addContainerGap())
        );
    }

    private void abrirSesion(){
    	
    }
    
    private void cerrarSesion(){
    	
    }

    private void detenerTrafico(){
    	
    }
    
    private void reanudarTrafico(){
    	
    }

    public void setDatosTabla(Object[][] datos){
    	tablaBanco.setModel(new javax.swing.table.DefaultTableModel(
                datos,
                new String [] {"#Conta", "#NTarxeta", "Saldo"}
            ));
    }
    
    public void emptyLog(){
    	textLogs.setText("");
    }

    
    public void addLogLine(String concepto){
    	Calendar cal = Calendar.getInstance();
    	cal.getTime();
    	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    	textLogs.append(sdf.format(cal.getTime()) + "\t" + concepto + "\n");
    }
    
}
