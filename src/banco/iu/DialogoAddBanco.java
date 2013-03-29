package banco.iu;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;

public class DialogoAddBanco extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	private JButton cancelButton;
	private JButton okButton;

	/**
	 * Create the dialog.
	 */
	public DialogoAddBanco(final AddBancoListener a) {
		this.setTitle("Engadir Banco");
		setBounds(100, 100, 307, 105);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.NORTH);
		
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		
		okButton = new JButton("OK");
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);
		
		cancelButton = new JButton("Cancel");
		cancelButton.setActionCommand("Cancel");
		buttonPane.add(cancelButton);
		
		JLabel lblNomeDoBanco = new JLabel("Nome do Banco");
		getContentPane().add(lblNomeDoBanco, BorderLayout.CENTER);
		
		textField = new JTextField();
		getContentPane().add(textField, BorderLayout.EAST);
		textField.setColumns(10);
		
		okButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String name = textField.getText();
				a.addBanco(name);
				setVisible(false);
			}	
		});
		
		cancelButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		
	}

}
