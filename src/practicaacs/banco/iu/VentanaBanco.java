/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package practicaacs.banco.iu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import practicaacs.banco.Banco;
import practicaacs.banco.bd.Canal;
import practicaacs.banco.bd.Conta;
import practicaacs.banco.bd.Mensaxe;
import practicaacs.banco.bd.Movemento;
import practicaacs.banco.bd.Tarxeta;

/**
 *
 * @author ch01
 */
public class VentanaBanco extends javax.swing.JFrame{

	private static final long serialVersionUID = 5641750155014378657L;
	private JButton botonabrirsesion,botondetenertrafico,botoneliminarconta,botoneliminartarxeta, botonestablecervalorespordefecto;
    private JButton botonengadirconta, botonengadirtarxeta, botonengadircontaasociada, botoneliminarcontaasociada;
    private JToggleButton botonforzarrecuperacion;
    private JPanel contasTab,monYcontrolTab,tarxTab;
    private JLabel jLabel1, jLabel2, jLabel3, jLabel4, jLabel5, jLabel6,jLabel7,jLabel8;
    private JScrollPane jScrollPane1, jScrollPane2, jScrollPane3;
    private JScrollPane jScrollPane4, jScrollPane5, jScrollPane6;
    private JSeparator jSeparator1,jSeparator2,jSeparator3;
    private JTabbedPane jTabbedPane1;
    private JList<String> listacontasasociadas,listatarxetas;
    private JTable tablacanles,movementos,tablacontas,tablamsgenviados,tablamsgrecibidos;
    private javax.swing.JTextArea textlog;
    private Banco banco;
	private boolean sesionabierta = false;
	private boolean traficoactivo;
	private boolean silenciaractivo;
	
	/**
	 * Contructor da interface de usuario do banco.
	 * @param banco Instancia do banco.
	 * @param nombre Nome do banco.
	 */
    public VentanaBanco(Banco banco, String nombre) {
    	this.banco = banco;
        initComponents();
        this.setTitle("Ventana Banco \"" + nombre + "\"" + " - ACS 2012/2013");
        this.actualizar();
        
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                System.exit(0);
            }
        });
        
        this.setIconImage(new ImageIcon("/home/ch01/Dropbox/UNI_Fedora/ACS/RepositorioPractica/res/iconobanco.png").getImage());
    }

    
    //----------------------------------------------------------------------------------------//
    //---------------------------------- METODOS CONTROL LOG  --------------------------------//
    //----------------------------------------------------------------------------------------//
    
    public void engadirLinhaLog(String s){
    	this.textlog.append(s);
    }
    
    public void baleirarLog(){
    	this.textlog.setText("");
    }
    
    
    
    
    //----------------------------------------------------------------------------------------//
    //---------------------------------- METODOS ACTUALIZAR INTERFACE ------------------------//
    //----------------------------------------------------------------------------------------//
    
	public void actualizar() {
		this.actualizarContas();
		this.actualizarContasAsociadas();
		this.actualizarMovementos();
		this.actualizarTarxetas();
		this.actualizarCanles();
		this.actualizarMensaxesRecibidas();
		this.actualizarMensaxesEnviadas();
		this.actualizarBotones();
	}

	private void actualizarBotones() {
		if(this.banco.sesionAberta()){
			this.botonabrirsesion.setEnabled(false);
			this.botonabrirsesion.setText("Cerrar Sesión");
			this.sesionabierta = true;
			this.botondetenertrafico.setEnabled(true);
			this.botonforzarrecuperacion.setEnabled(true);
			this.botonforzarrecuperacion.setText("Silenciar Canal");
			
			if(this.banco.recuperacion()){
				this.botonabrirsesion.setEnabled(false);
				this.botondetenertrafico.setEnabled(false);
				this.botonforzarrecuperacion.setEnabled(false);
			}else if(this.banco.traficoActivo()){
				this.botondetenertrafico.setText("Detener Trafico");
				this.traficoactivo = true;
			}else{
				this.botonabrirsesion.setEnabled(true);
				this.botondetenertrafico.setText("ReanudarTrafico");
				this.traficoactivo = false;
			}
		}
		else{
			this.botonabrirsesion.setText("Abrir Sesión");
			this.sesionabierta = false;
			this.botondetenertrafico.setEnabled(false);
			this.botonforzarrecuperacion.setEnabled(false);
		}
		
	}


	private void actualizarMensaxesEnviadas() {
		this.setTablaMensaxesEnviados(this.formatTaboaMensaxes(this.banco.getMensaxesEnviadas()));
	}

	private void actualizarMensaxesRecibidas() {
		this.setTablaMensaxesRecibidos(this.formatTaboaMensaxes(this.banco.getMensaxesRecibidas()));
	}
	
	private void actualizarCanles() {
		this.setTablaCanles(this.formatTaboaCanles(this.banco.getCanales()));
	}

	private void actualizarContasAsociadas(){
 		int selrow = listatarxetas.getSelectedIndex();
 		HashMap<Integer, Conta> res;
 		
 		if(selrow != -1){
 			String ntarxeta = (String) listatarxetas.getModel().getElementAt(selrow);
 			res = banco.getContasAsociadas(ntarxeta);
 		}else{
 			res = new HashMap<Integer,Conta>();
 		}
		setListaContasAsociadas(formatListContas(res));
	}
	
	private void actualizarContas(){
		setTablaContas(this.formatTaboaContas(banco.getContas()));
	}
	
	private void actualizarMovementos(){
		int selrow = tablacontas.getSelectedRow();
		ArrayList<Movemento> res;
		if(selrow != -1){
			Integer nconta = (Integer) tablacontas.getModel().getValueAt(tablacontas.getSelectedRow(), 0);
			res = banco.getMovementosConta(nconta);
		}else{
			res = new ArrayList<Movemento>();
		}
		setMovementos(formatTaboaMovementos(res));
	}
	
	private void actualizarTarxetas(){
        setListaTarxetas(this.formatListTarxetas(banco.getTarxetas()));
	}
	
    
	
	
	//----------------------------------------------------------------------------------------//
    //-------------------------------- METODO INICIALIZAR COMPONENTES ------------------------//
    //----------------------------------------------------------------------------------------//
    
	private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        contasTab = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablacontas = new javax.swing.JTable();
        jSeparator1 = new javax.swing.JSeparator();
        jScrollPane2 = new javax.swing.JScrollPane();
        movementos = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        botonengadirconta = new javax.swing.JButton();
        botoneliminarconta = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        botonestablecervalorespordefecto = new javax.swing.JButton();
        monYcontrolTab = new javax.swing.JPanel();
        botonforzarrecuperacion = new javax.swing.JToggleButton();
        botonabrirsesion = new javax.swing.JButton();
        botondetenertrafico = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tablacanles = new javax.swing.JTable();
        jSeparator2 = new javax.swing.JSeparator();
        jScrollPane4 = new javax.swing.JScrollPane();
        textlog = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();
        tarxTab = new javax.swing.JPanel();
        botonengadirtarxeta = new javax.swing.JButton();
        botoneliminartarxeta = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        listatarxetas = new javax.swing.JList<String>();
        jScrollPane6 = new javax.swing.JScrollPane();
        listacontasasociadas = new javax.swing.JList<String>();
        jSeparator3 = new javax.swing.JSeparator();
        botonengadircontaasociada = new JButton();
        botoneliminarcontaasociada = new JButton();
        tablamsgenviados = new JTable();
        tablamsgrecibidos = new JTable();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        // TAB CONTAS
                
        jScrollPane1.setViewportView(tablacontas);

        tablacontas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    	ListSelectionModel rowSM = tablacontas.getSelectionModel();
    	rowSM.addListSelectionListener(new ListSelectionListener() {
    		public void valueChanged(ListSelectionEvent e) {
    			if (e.getValueIsAdjusting()) return;
    			VentanaBanco.this.actualizarMovementos();
    		}
    	});
    	
    	
        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jScrollPane2.setViewportView(movementos);

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Movementos");

        botonengadirconta.setText("Engadir Conta");
        botonengadirconta.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				engadirConta();
			}
        	
        });

        botoneliminarconta.setText("Eliminar Conta");
        botoneliminarconta.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				eliminarConta();
			}
        	
        });

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Contas");

        botonestablecervalorespordefecto.setText("Establecer Valores por Defecto");
        botonestablecervalorespordefecto.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				establecerValoresDefecto();
			}
        	
        });

        javax.swing.GroupLayout contasTabLayout = new javax.swing.GroupLayout(contasTab);
        contasTab.setLayout(contasTabLayout);
        contasTabLayout.setHorizontalGroup(
            contasTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contasTabLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(contasTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(botonestablecervalorespordefecto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(contasTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(contasTabLayout.createSequentialGroup()
                            .addComponent(botonengadirconta, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(botoneliminarconta, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(contasTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 340, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        contasTabLayout.setVerticalGroup(
            contasTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, contasTabLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(contasTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(botonestablecervalorespordefecto)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(contasTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jSeparator1)
                    .addGroup(contasTabLayout.createSequentialGroup()
                        .addGroup(contasTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(botonengadirconta)
                            .addComponent(botoneliminarconta))
                        .addGap(7, 7, 7)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE))
                    .addComponent(jScrollPane2))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Contas", contasTab);

        //TAB TARXETAS
        
        botonengadircontaasociada.setText("Engadir Conta Asociada");
        botonengadircontaasociada.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				engadircontaasociada();
			}
        	
        });
        
        botoneliminarcontaasociada.setText("Eliminar Conta Asociada");
        botoneliminarcontaasociada.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				eliminarcontaasociada();
			}
        	
        });

        
        botonengadirtarxeta.setText("Engadir Tarxeta");
        botonengadirtarxeta.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				engadirTarxeta();
			}
        	
        });

        botoneliminartarxeta.setText("Eliminar Tarxeta");
        botoneliminartarxeta.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				eliminarTarxeta();
			}
        	
        });

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Tarxetas");

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Contas Asociadas");

        
        listatarxetas.getSelectionModel().addListSelectionListener(new ListSelectionListener(){

			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting()) return;
				VentanaBanco.this.actualizarContasAsociadas();
			}
        	
        });
        
        jScrollPane5.setViewportView(listatarxetas);

        jScrollPane6.setViewportView(listacontasasociadas);

        jSeparator3.setOrientation(javax.swing.SwingConstants.VERTICAL);

        javax.swing.GroupLayout tarxTabLayout = new javax.swing.GroupLayout(tarxTab);
        tarxTab.setLayout(tarxTabLayout);
        tarxTabLayout.setHorizontalGroup(
            tarxTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tarxTabLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tarxTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(botonengadirtarxeta, javax.swing.GroupLayout.DEFAULT_SIZE, 296, Short.MAX_VALUE)
                    .addComponent(botoneliminartarxeta, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(tarxTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane6)
                    .addComponent(botonengadircontaasociada, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(botoneliminarcontaasociada, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 290, Short.MAX_VALUE))
                .addContainerGap())
        );
        tarxTabLayout.setVerticalGroup(
            tarxTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tarxTabLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tarxTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tarxTabLayout.createSequentialGroup()
                        .addGroup(tarxTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(botonengadirtarxeta)
                            .addComponent(botonengadircontaasociada))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(tarxTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(botoneliminartarxeta)
                            .addComponent(botoneliminarcontaasociada))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(tarxTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tarxTabLayout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE))
                            .addGroup(tarxTabLayout.createSequentialGroup()
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane5))))
                    .addComponent(jSeparator3, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Tarxetas", tarxTab);
        
        //TAB MONITORIZACION e CONTROL
        
        botonforzarrecuperacion.setText("Silenciar Canal");
        botonforzarrecuperacion.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				VentanaBanco.this.silenciarCanle();
			}
        	
        });

        botonabrirsesion.setText("Abrir Sesion");
        botonabrirsesion.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(VentanaBanco.this.sesionabierta){
					cerrarSesion();
				}else{
					abrirSesion();
				}
				
			}
        	
        });

        botondetenertrafico.setEnabled(false);
        botondetenertrafico.setText("Detener Trafico");
        botondetenertrafico.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(VentanaBanco.this.traficoactivo){
					detenerTrafico();
				}else{
					reanudarTrafico();
				}
			}
        	
        });

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Canles");

        
        jScrollPane3.setViewportView(tablacanles);

        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);

        textlog.setColumns(20);
        textlog.setRows(5);
        textlog.setEditable(false);
        jScrollPane4.setViewportView(textlog);

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Log");
        
        jLabel7.setText("Mensaxes Recibidas");
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        
        jLabel8.setText("Mensaxes Enviadas");
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        
        JScrollPane jScrollPane7 = new JScrollPane();
		jScrollPane7.setViewportView(tablamsgrecibidos);
        JScrollPane jScrollPane8 = new JScrollPane();;
		jScrollPane8.setViewportView(tablamsgenviados);
        
        javax.swing.GroupLayout monYcontrolTabLayout = new javax.swing.GroupLayout(monYcontrolTab);
        monYcontrolTab.setLayout(monYcontrolTabLayout);
        monYcontrolTabLayout.setHorizontalGroup(
            monYcontrolTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(monYcontrolTabLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(monYcontrolTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(botonforzarrecuperacion, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(monYcontrolTabLayout.createSequentialGroup()
                        .addGroup(monYcontrolTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(botonabrirsesion, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(monYcontrolTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(botondetenertrafico, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(monYcontrolTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        monYcontrolTabLayout.setVerticalGroup(
            monYcontrolTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(monYcontrolTabLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(monYcontrolTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator2)
                    .addGroup(monYcontrolTabLayout.createSequentialGroup()
                        .addComponent(botonforzarrecuperacion)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(monYcontrolTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(botonabrirsesion)
                            .addComponent(botondetenertrafico))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, monYcontrolTabLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Monitorizacion e Control", monYcontrolTab);


        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );

        pack();
    }
	

	
	
	//----------------------------------------------------------------------------------------//
    //------------------------------------ METODOS ACCIONS BOTONS ----------------------------//
    //----------------------------------------------------------------------------------------//

	private void establecerValoresDefecto() {
		new DialogoSiNon("Estas seguro de que queres establecer os valores por defecto?",new Runnable(){

					@Override
					public void run() {
						banco.establecerValoresPorDefecto();
						VentanaBanco.this.actualizar();
					}
			
		}).setVisible(true);
	}

	private void eliminarcontaasociada() {
		int selectedindex = this.listatarxetas.getSelectedIndex();
		if(selectedindex == -1){
			new DialogoError("Selecione unha tarxeta.").setVisible(true);
		}
		final String cdgtarxeta = this.listatarxetas.getModel().getElementAt(selectedindex);
		
		final int numconta = this.listacontasasociadas.getSelectedIndex();
		String aux = this.listacontasasociadas.getModel().getElementAt(numconta);
		System.out.println("Eliminar conta num. " + new Integer(numconta) + "--> " + aux);
		
		if(aux.equals("---")){
			new DialogoError("Selecione unha conta ou unha diferente").setVisible(true);
		}else{
			new DialogoSiNon("Estas seguro de que queres desasociar a tarxeta " +
					cdgtarxeta + "e a conta " + aux + "?",new Runnable(){
				
						@Override
						public void run() {
							banco.eliminarContaAsociada(cdgtarxeta, numconta+1);
							VentanaBanco.this.actualizar();
						}
				
			}).setVisible(true);			
		}
	}
		
	private void engadircontaasociada() {
		String aux = null;
		int selectedindex = this.listatarxetas.getSelectedIndex();
		if(selectedindex == -1){
			new DialogoError("Selecione unha tarxeta").setVisible(true);
		}
		final String cdgtarxeta = this.listatarxetas.getModel().getElementAt(selectedindex);
		
		final int numconta = this.listacontasasociadas.getSelectedIndex();
		if(numconta != -1)
			aux = this.listacontasasociadas.getModel().getElementAt(numconta);
		
		if("---".equals(aux)){
			ArrayList<Conta> arraycontas = banco.getContas();
			
			Integer[] contas = new Integer[arraycontas.size()];
			int i = 0;
			for(Conta c : arraycontas)
				contas[i++] = c.getNumero();
			
			new DialogoNovaContaAsociada(this.banco, cdgtarxeta,numconta+1, contas).setVisible(true);
		}else{
			new DialogoError("Selecione un oco libre.").setVisible(true);
		}
		
	}

	private void reanudarTrafico() {
		this.banco.solicitarReanudarTrafico();
	}
	
	private void detenerTrafico() {
		this.banco.solicitarDeterTrafico();
		this.botondetenertrafico.setText("Reanudar Tráfico");
		this.traficoactivo = false;
	}
	
	
	
	private void silenciarCanle() {
		
		if(this.silenciaractivo){
			this.setSilenciar(false);
			this.banco.silenciarCanle(-1);
			this.botonforzarrecuperacion.setText("Silenciar canle.");
		}else{
			int selrow = this.tablacanles.getSelectedRow();
			
			if(selrow != -1){
				int numCanle = (Integer) tablacontas.getModel().getValueAt(selrow+1, 0);
				this.banco.silenciarCanle(numCanle);
				this.botonforzarrecuperacion.setText("Deixar de silenciar canle.");
				this.setSilenciar(true);
			}else{
				new DialogoError("Selecione unha canle.").setVisible(true);
			}
		}
	}
	
	private void setSilenciar(boolean b) {
		this.silenciaractivo = b;
	}


	private void abrirSesion() {
		new DialogoAbrirSesion(this.banco).setVisible(true);
		this.actualizar();
	}
	
	private void cerrarSesion() {
		new DialogoSiNon("Esta seguro de que desexa cerrar a sesión?", new Runnable(){

			@Override
			public void run() {
				banco.solicitarPecheSesion();
			}
			
		}).setVisible(true);
		this.actualizar();
	}
	
	private void eliminarTarxeta() {
		int selrow = this.listatarxetas.getSelectedIndex();
		if (selrow == -1){
			new DialogoError("Selecione unha tarxeta.").setVisible(true);
			return;
		}
		final String cdgtarxeta = listatarxetas.getModel().getElementAt(selrow);
		
		new DialogoSiNon("Esta seguro de que queres eliminar a tarxeta número " + cdgtarxeta + "?",new Runnable(){

			@Override
			public void run() {
				banco.eliminarTarxeta(cdgtarxeta);
			}
			
		}).setVisible(true);
	}
	
	private void engadirTarxeta() {
		new DialogoNovaTarxeta(this.banco).setVisible(true);
	}
	
	private void eliminarConta() {
		
		int selrow = this.tablacontas.getSelectedRow();
		if (selrow == -1){
			new DialogoError("Selecione unha conta.").setVisible(true);
			return;
		}
		final int cdgConta = (Integer) tablacontas.getModel().getValueAt(selrow, 0);
		
		new DialogoSiNon("Esta seguro de que queres eliminar a conta número " + cdgConta + "?",new Runnable(){

					@Override
					public void run() {
						banco.eliminarConta(cdgConta);
						VentanaBanco.this.actualizar();
					}
			
		}).setVisible(true);
	}
	
	private void engadirConta() {
		new DialogoNovaConta(this.banco).setVisible(true);
	}

		
	
	//----------------------------------------------------------------------------------------//
    //------------------------------ METODOS MODIFICAR VALORES TABOAS ------------------------//
    //----------------------------------------------------------------------------------------//
	
	private void setMovementos(Object[][] data){
		movementos.setModel(new javax.swing.table.DefaultTableModel(
                data,
                new String [] {"#Movemento", "Tipo","Importe","Data"}
        ));
    }
    
	
    private void setTablaContas(Object[][] data){
    	tablacontas.setModel(new javax.swing.table.DefaultTableModel(
                data,
                new String [] { "Numero Conta", "Saldo" }
            ));
    }
    
    
    private void setTablaCanles(Object[][] data){
    	tablacanles.setModel(new javax.swing.table.DefaultTableModel(
                data,
                new String [] {"Num. Canle", "Id. Ultimo Mensaxe", "Ocupado"}
            ));
    }
    
    
    private void setTablaMensaxesRecibidos(Object[][] data){
    	this.tablamsgrecibidos.setModel(new javax.swing.table.DefaultTableModel(
                data,
                new String [] {"#Canle", "#Msx", "Tipo"}
            ));
    }

    
    private void setTablaMensaxesEnviados(Object[][] data){
    	this.tablamsgenviados.setModel(new javax.swing.table.DefaultTableModel(
                data,
                new String [] {"#Canle", "#Msx", "Tipo"}
            ));
    }
    
    	
    private void setListaTarxetas(final String[] list){
    	listatarxetas.setModel(new javax.swing.AbstractListModel<String>() {
			private static final long serialVersionUID = 1L;	
			public int getSize() { return list.length; }
            public String getElementAt(int i) { return list[i]; }
        });
    }
    
    
    private void setListaContasAsociadas(final String[] list){
    	listacontasasociadas.setModel(new javax.swing.AbstractListModel<String>() {
			private static final long serialVersionUID = -5475210877168039186L;
			public int getSize() { return list.length; }
            public String getElementAt(int i) { return list[i]; }
        });
    }

    
    
    
    //----------------------------------------------------------------------------------------//
    //---------------------------------- METODOS FORMATEAR DATOS --- ------------------------//
    //----------------------------------------------------------------------------------------//
    
    private Object[][] formatTaboaMovementos(ArrayList<Movemento> movementos) {
    	int i=0;
 		Object[][] res = new Object[movementos.size()][2];
 		for(Movemento m : movementos){
 			res[i][0] = new Integer(m.codigo);
 			res[i][1] = m.tipo;
 			res[i][2] = new Float(m.importe);
 			res[i][3] = m.data;
 			i++;
 		}
 		return res;
	}
    
    private String[] formatListContas(HashMap<Integer, Conta> res2) {
    	int i;
     	String[] res = new String[3];
     	for(i=1;i<=3;i++){
     		if(res2.containsKey(i)){
     			Conta c = res2.get(i);
         		res[i-1] = new Integer(c.getNumero()).toString() + "(" + c.getSaldo() + ")";
     		}else{
     			res[i-1] = "---";
     		}
     	}
     	return res;
    }

 	private Object[][] formatTaboaContas(ArrayList<Conta> contas) {
 		int i=0;
 		Object[][] res = new Object[contas.size()][2];
 		for(Conta c : contas){
 			res[i][0] = new Integer(c.getNumero());
 			res[i][1] = new Float(c.getSaldo());
 			i++;
 		}
 		return res;
 	}
 	
 	private String[] formatListTarxetas(ArrayList<Tarxeta> tarxetas) {
 		int i=0;
 		String[] res = new String[tarxetas.size()];
 		for(Tarxeta t : tarxetas){
 			res[i++] = t.codigo;
 		}
 		return res;
	}

	private Object[][] formatTaboaCanles(ArrayList<Canal> canales) {
		int i=0;
 		Object[][] res = new Object[canales.size()][3];
 		for(Canal c : canales){
 			
 			res[i][0] = c.numero;
 			res[i][1] = c.lastMsg;
 			res[i][2] = c.ocupado ? "SI" : "NON";
 			i++;
 		}
 		return res;
	}

	private Object[][] formatTaboaMensaxes(ArrayList<Mensaxe> mensaxes){
		int i=0;
 		Object[][] res = new Object[mensaxes.size()][3];
 		for(Mensaxe c : mensaxes){
 			
 			res[i][0] = c.numCanal;
 			res[i][1] = c.numMenx;
 			res[i][2] = c.tipo;
 			i++;
 		}
 		return res;	}

}
