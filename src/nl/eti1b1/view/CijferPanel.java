package nl.eti1b1.view;

import java.awt.Font;
import java.awt.GridLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JPanel;

import nl.eti1b1.model.Kluis;

/**
 * De klasse cijferpaneel deze klasse laat de ingevulde getallen zien.
 * 
 * @author Tom, Martijn, Rob en Laurens
 * 
 */
@SuppressWarnings("serial")
public class CijferPanel extends JPanel implements Observer {

	private JButton getal1;
	private JButton getal2;
	private JButton getal3;

	/**
	 * De constructor, die de cijfers aanmaakt als JButtons, dit zodat ze mooi
	 * weergegeven worden.
	 */
	public CijferPanel() {

		this.setLayout(new GridLayout(1, 2));

		getal1 = new JButton("0");
		getal2 = new JButton("0");
		getal3 = new JButton("0");

		getal1.setFont(new Font("Arial", Font.BOLD, 60));
		getal2.setFont(new Font("Arial", Font.BOLD, 60));
		getal3.setFont(new Font("Arial", Font.BOLD, 60));

		getal1.setEnabled(false);
		getal2.setEnabled(false);
		getal3.setEnabled(false);

		this.add(getal1);
		this.add(getal2);
		this.add(getal3);
	}

	/**
	 * Getter voor het eerste getal
	 * 
	 * @return het eerste getal
	 */
	public JButton getGetal1() {
		return getal1;
	}

	/**
	 * Getter voor het tweede getal
	 * 
	 * @return het tweede getal
	 */
	public JButton getGetal2() {
		return getal2;
	}

	/**
	 * Getter voor het derde getal
	 * 
	 * @return het derde getal
	 */
	public JButton getGetal3() {
		return getal3;
	}

	/**
	 * De Methode update, deze klasse zorgt voor het updaten van de cijfers
	 * wanneer er een cijfer veranderd word.
	 */
	@Override
	public void update(Observable o, Object arg) {
		// als er een Integer wordt meegegeven veranderd hij alleen dit getal
		if (o instanceof Kluis && arg instanceof Integer) {
			Kluis kluis = (Kluis) o;
			int[] ingevulde = kluis.getIngevulde();
			if ((Integer) arg == 1)
				getal1.setText(Integer.toString(ingevulde[0]));
			else if ((Integer) arg == 2)
				getal2.setText(Integer.toString(ingevulde[1]));
			else if ((Integer) arg == 3)
				getal3.setText(Integer.toString(ingevulde[2]));
		}
		// Als er een int array wordt meegegeven veranderd hij alle getallen
		if (o instanceof Kluis && arg instanceof int[]) {
			int[] ingevulde = (int[]) arg;
			getal1.setText(Integer.toString(ingevulde[0]));
			getal2.setText(Integer.toString(ingevulde[1]));
			getal3.setText(Integer.toString(ingevulde[2]));
		}

	}

}
