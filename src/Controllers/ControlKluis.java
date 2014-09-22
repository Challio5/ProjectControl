package Controllers;

import Model.*;
import nl.projectgroepb1.projectcontrol.*;
import Views.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;

import javax.swing.JFrame;

import nl.projectgroepb1.projectcontrol.Main;

/**
 * De klasse ControlKluis, dit is de grootste control klasse, van hieruit wordt
 * de afhandeling van de meeste knoppen gedaan. en de gehele applicatie
 * geinitialiseerd
 * 
 * @author Tom, Martijn, Rob en Laurens
 * 
 */
public class ControlKluis implements ActionListener {
	private Kluis kluis;
	private ViewKluis view;
	private CijferPanel cijferPanel;
	private JFrame frame;
	private SerialHandler serial;
	private TekstPanel tekstPanel;

	/**
	 * De constructor initialiseert de gehele klasse
	 */
	public ControlKluis() {
		kluis = new Kluis();
		cijferPanel = new CijferPanel();
		tekstPanel = new TekstPanel();
		view = new ViewKluis(cijferPanel, tekstPanel);
		serial = new SerialHandler(kluis, view, this);
		kluis.addObserver(cijferPanel);
		kluis.addObserver(tekstPanel);
		frame = new Main();
		frame.add(view, BorderLayout.CENTER);
		view.setConnectListener(this);
		view.setDisconnectListener(this);
		view.setSendListener(this);
		view.setChangeListener(this);
		view.setSetListener(this);
		view.setCheckListener(this);
	}

	/**
	 * De actionPerformed methode handeld alle knoppen af en zorgt ervoor dat de
	 * serial de juiste data send over de poort.
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(view.getStart())) {
			serial.initialize();
		} else if (e.getSource().equals(view.getDisconnect())) {
			serial.close();
		} else if (e.getSource().equals(view.getSend())) {
			serial.send();
		} else if (e.getSource().equals(view.getChange())) {
			String code = view.getTekst().getField().getText();
			String[] stringArray = splitTekst(code);
			int[] codes = { Integer.parseInt(stringArray[0]),
					Integer.parseInt(stringArray[1]),
					Integer.parseInt(stringArray[2]) };
			serial.send(ProtocolConstanten.ingevulde
					+ ProtocolConstanten.delimiter + codes[0]
					+ ProtocolConstanten.delimiter + codes[1]
					+ ProtocolConstanten.delimiter + codes[2] + "\n");
			kluis.setIngevulde(codes);
			;
			view.getTekst().getField().setText(null);
		} else if (e.getSource().equals(view.getSet())) {
			serial.send(ProtocolConstanten.set + ProtocolConstanten.delimiter
					+ kluis.getFirstDigit() + ProtocolConstanten.delimiter
					+ kluis.getSecondDigit() + ProtocolConstanten.delimiter
					+ kluis.getThirdDigit() + "\n");
		} else if (e.getSource().equals(view.getCheck())) {
			serial.send(ProtocolConstanten.open + ProtocolConstanten.delimiter
					+ kluis.getFirstDigit() + ProtocolConstanten.delimiter
					+ kluis.getSecondDigit() + ProtocolConstanten.delimiter
					+ kluis.getThirdDigit() + "\n");
		}
	}

	/**
	 * Splitst een binnengekomen commando aan de hand van de Protocol.DELIMITER
	 * 
	 * @param cmd
	 *            te splitsen commando
	 * @return String-array die het commando en de argumenten bevat
	 */
	public String[] splitTekst(String cmd) {
		return cmd.split(ProtocolConstanten.delimiter);
	}

}
