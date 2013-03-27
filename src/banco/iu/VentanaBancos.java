/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package banco.iu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;

import banco.Banco;

/**
 *
 * @author ch01
 */
public class VentanaBancos extends javax.swing.JFrame {

	private JMenu menuBanco;
    private JMenu menuContas;
    private JMenu menuSobre;
    private JMenuBar barraDeMenu;
    private JMenuItem engadirBanco;
    private JMenuItem eliminarBanco;
    private JTabbedPane tabbedframe;
	private JMenuItem engadirConta;
	private JMenuItem eliminarConta;
	private JMenuItem amosarAxuda;
	private JMenuItem amosarSobre;
	
    public VentanaBancos() {
        initComponents();
        this.setName("Ventana Bancos - ACS 2012/2013");
        this.setBounds(100, 100, 830, 450);
    }

    private void initComponents() {

        tabbedframe = new JTabbedPane();
        barraDeMenu = new JMenuBar();
        menuBanco = new JMenu();
        engadirBanco = new JMenuItem();
        eliminarBanco = new JMenuItem();
        engadirConta = new JMenuItem();
        eliminarConta = new JMenuItem();
        amosarAxuda = new JMenuItem();
        amosarSobre = new JMenuItem();
        menuSobre = new JMenu();
        menuContas = new JMenu();

        this.setName("Ventana Bancos - ACS 2012/2013");
        this.setTitle("Ventana Bancos - ACS 2012/2013");
        
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        //MENU BANCOS
        menuBanco.setText("Banco");

        engadirBanco.setText("Engadir Banco");
        engadirBanco.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				addBanco();
			}
        	
        });
        menuBanco.add(engadirBanco);

        eliminarBanco.setText("Eliminar Banco");
        eliminarBanco.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt){
                deleteBanco();
            }
        });
        menuBanco.add(eliminarBanco);

        barraDeMenu.add(menuBanco);

        //MENU CONTAS
        menuContas.setText("Contas");
        
        engadirConta.setText("Engadir Conta");
        engadirConta.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				addConta();
			}
        	
        });
        menuContas.add(engadirConta);

        eliminarConta.setText("Eliminar Conta");
        eliminarConta.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt){
                deleteConta();
            }
        });
        menuContas.add(eliminarConta);
        
        barraDeMenu.add(menuContas);

        //MENU SOBRE
        menuSobre.setText("Sobre");
        
        amosarAxuda.setText("Axuda");
        amosarAxuda.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				displayAxuda();
			}
        	
        });
        menuSobre.add(amosarAxuda);
        
        amosarSobre.setText("Sobre");
        amosarSobre.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				displaySobre();
			}
        	
        });
        menuSobre.add(amosarSobre);
        
        barraDeMenu.add(menuSobre);
        
        setJMenuBar(barraDeMenu);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        
        
        getContentPane().setLayout(layout);
        
        
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tabbedframe, javax.swing.GroupLayout.DEFAULT_SIZE, 366, Short.MAX_VALUE)
                .addContainerGap())
        );
        
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tabbedframe, javax.swing.GroupLayout.DEFAULT_SIZE, 259, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }


	/**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
    	VentanaBancos b = new VentanaBancos();
        b.tabbedframe.add(new BancoTab(new Banco("CaixaGaliza")));
        b.tabbedframe.add(new BancoTab(new Banco("CaixaRuralGalega")));
        b.setVisible(true);
    }

    
    private void addBanco(){
    	
    }
    
    private void deleteBanco(){
    	
    }

    private void addConta(){
    	
    }
    
    private void deleteConta(){
    	
    }
    
    private void displaySobre(){
    	
    }
    
    private void displayAxuda() {
	
	}
    
}
