package practicaacs.cajeros.iu;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;

import practicaacs.cajeros.Cajero;
import practicaacs.cajeros.Envio;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class DialogoDatos extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	private JTextField textField_1;
	
	private Cajero cajero;
	private Envio envio;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			DialogoDatos dialog = new DialogoDatos();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public DialogoDatos() {
		
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			textField = new JTextField();
			textField.setBounds(103, 60, 230, 19);
			textField.setText("Tarjeta de Crédito");
			textField.setColumns(10);
			contentPanel.add(textField);
		}
		{
			JLabel label = new JLabel("Introduzca su Tarjeta de Crédito");
			label.setBounds(103, 33, 230, 15);
			contentPanel.add(label);
		}
		
		JLabel label = new JLabel("Introduzca su Número de Cuenta");
		label.setBounds(103, 120, 246, 15);
		contentPanel.add(label);
		
		textField_1 = new JTextField();
		textField_1.setText("Número de Cuenta");
		textField_1.setColumns(10);
		textField_1.setBounds(103, 147, 246, 19);
		contentPanel.add(textField_1);
		{
			JButton btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String tarjeta = textField.getText();
			        int cuenta = Integer.parseInt(textField_1.getText());
			        envio = new Envio(tarjeta,cuenta);
				}
			});
			btnAceptar.setBounds(190, 233, 117, 25);
			contentPanel.add(btnAceptar);
		}
		{
			JButton btnCancelar = new JButton("Cancelar");
			btnCancelar.setBounds(319, 233, 117, 25);
			contentPanel.add(btnCancelar);
		}
	}
}
