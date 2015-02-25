// Projectgroep b1 2014

package nl.eti1b1.main;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import nl.eti1b1.control.ControlKluis;

/**
 * Dit is de main klasse, hier wordt de applicatie gestart
 * 
 * @author Groep 1vb1
 * @version 1.0
 */

@SuppressWarnings("serial")
public class Main extends JFrame {

	public Main() {
		setLayout(new BorderLayout());
		setSize(800, 400);
		setTitle("Kluis");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}

	public static void main(String[] args) {
		new ControlKluis();
	}
}
