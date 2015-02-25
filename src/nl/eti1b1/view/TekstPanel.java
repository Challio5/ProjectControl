// Projectgroep b1 2014

package nl.eti1b1.view;

import java.awt.GridLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import nl.eti1b1.model.Kluis;

/**
 * De Klasse Tekspanel, deze klasse verzorgt het in en uitvoer veld
 * 
 * @author Tom, Martijn, Rob en Laurens
 * 
 */
@SuppressWarnings("serial")
public class TekstPanel extends JPanel implements Observer {
	private JTextField tekstveld;
	private JLabel uitvoer;
	private JLabel tekstLabel;
	private JLabel uitLabel;

	/**
	 * De construcutor initialiseert de labels en het textfield en set de layout
	 * op GridLayout zodat alles mooi tegenover elkaar staat
	 */
	public TekstPanel() {
		setLayout(new GridLayout(0, 2));
		tekstveld = new JTextField(20);
		uitLabel = new JLabel("Uitvoer");
		tekstLabel = new JLabel("Invoer");
		uitvoer = new JLabel("");
		add(tekstLabel);
		add(tekstveld);
		add(uitLabel);
		add(uitvoer);
	}

	/**
	 * Getter voor het JTextField
	 * 
	 * @return tekstveld
	 */
	public JTextField getField() {
		return tekstveld;
	}

	/**
	 * De methode Updat zorgt ervoor dat er op het uitvoer label geprint wordt
	 * wat er is gebeurt
	 */
	@Override
	public void update(Observable o, Object arg1) {
		if (o instanceof Kluis && arg1 instanceof Boolean) {
			if ((boolean) arg1 == true) {
				uitvoer.setText("De kluis is open");
			} else if ((boolean) arg1 == false) {
				uitvoer.setText("De kluis is gesloten");
			}
		}
		if (o instanceof Kluis && arg1 instanceof String) {
			uitvoer.setText((String) arg1);
			System.out.println((String) arg1);
		}
	}
}
