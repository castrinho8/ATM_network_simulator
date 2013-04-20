package practicaacs.cajeros.iu;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class DialogoSelOperaciones extends JDialog {

	private final JPanel contentPanel = new JPanel();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			DialogoSelOperaciones dialog = new DialogoSelOperaciones();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public DialogoSelOperaciones() {
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JButton button = new JButton("Consultar Saldo");
		button.setBounds(31, 70, 200, 25);
		contentPanel.add(button);
		
		JButton button_1 = new JButton("Consultar Movimientos");
		button_1.setBounds(31, 126, 200, 25);
		contentPanel.add(button_1);
		
		JButton button_2 = new JButton("Traspaso");
		button_2.setBounds(300, 46, 117, 25);
		contentPanel.add(button_2);
		
		JButton button_3 = new JButton("Reintegro");
		button_3.setBounds(300, 101, 117, 25);
		contentPanel.add(button_3);
		
		JButton button_4 = new JButton("Abono");
		button_4.setBounds(300, 158, 117, 25);
		contentPanel.add(button_4);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton cancelButton = new JButton("Cancelar");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
}
