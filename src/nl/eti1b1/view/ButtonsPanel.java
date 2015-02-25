// Projectgroep b1 2014

package nl.eti1b1.view;

import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * De Klasse ButtonsPanel voor de knoppen aan de rechterkant in het JFrame
 * 
 * @author Tom, Martijn, Rob en Laurens
 * 
 */
@SuppressWarnings("serial")
public class ButtonsPanel extends JPanel {

	private JButton change;
	private JButton set;
	private JButton check;

	/**
	 * De constructor
	 */
	public ButtonsPanel() {

		this.setLayout(new GridLayout(3, 2));

		change = new JButton("Verander getal");
		set = new JButton("Verander code");
		check = new JButton("Open kluis");

		change.setEnabled(false);
		set.setEnabled(false);
		check.setEnabled(false);

		this.add(change);
		this.add(set);
		this.add(check);
	}

	/**
	 * Methode voor het enabelen van de Change set en Checkbuttons
	 */
	public void setEnabled(boolean bool) {
		change.setEnabled(bool);
		set.setEnabled(bool);
		check.setEnabled(bool);
	}

	/**
	 * Setter voor de actionListener
	 * 
	 * @param listener
	 *            de actionListener
	 */
	public void setCheckListener(ActionListener listener) {
		check.addActionListener(listener);
	}

	/**
	 * Setter voor de actionListener
	 * 
	 * @param listener
	 *            de actionListener
	 */
	public void setSetListener(ActionListener listener) {
		set.addActionListener(listener);
	}

	/**
	 * Getter voor de Change button
	 * 
	 * @return de change button
	 */
	public JButton getChange() {
		return change;
	}

	/**
	 * Getter voor de set button
	 * 
	 * @return de Set button
	 */
	public JButton getSet() {
		return set;
	}

	/**
	 * Getter voor de Check button
	 * 
	 * @return de checkButton
	 */
	public JButton getCheck() {
		return check;
	}

	/**
	 * Setter voor de actionListener
	 * 
	 * @param listener
	 *            de actionListener
	 */
	public void setChangeListener(ActionListener listener) {
		change.addActionListener(listener);
	}
}
