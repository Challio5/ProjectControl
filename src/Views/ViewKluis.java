// Projectgroep b1 2014

package Views;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import Views.ButtonPanel;

/**
 * De klasse ViewKluis dit is het JPanel waar alle andere views ingeplaatst zijn
 * 
 * @author Tom, Martijn, Rob en Laurens
 * 
 */
@SuppressWarnings("serial")
public class ViewKluis extends JPanel {
	private ButtonPanel button;
	private TekstPanel tekst;
	private CijferPanel getallen;
	private ButtonsPanel buttons;

	/**
	 * De constructor initialiseert het panel. De layout van dit panel is de
	 * BorderLayout
	 * 
	 * @param cijferPanel
	 *            het panel met de getallen
	 * @param tekstPanel
	 *            het panel met de tekstvelden
	 */
	public ViewKluis(CijferPanel cijferPanel, TekstPanel tekstPanel) {
		this.setLayout(new BorderLayout());

		tekst = new TekstPanel();
		button = new ButtonPanel();
		getallen = cijferPanel;
		buttons = new ButtonsPanel();

		this.add(buttons, BorderLayout.EAST);
		this.add(tekst, BorderLayout.NORTH);
		this.add(getallen, BorderLayout.CENTER);
		this.add(button, BorderLayout.SOUTH);
	}

	/**
	 * Enabeld alle buttons nadat er geconnect is
	 */
	public void connect() {
		button.setEnabled(true);
		buttons.setEnabled(true);
	}

	/**
	 * Disabled alle buttons nadat er gedisconnect is
	 */
	public void disconnect() {
		button.setEnabled(false);
		buttons.setEnabled(false);
	}

	/**
	 * Getter voor het TekstPanel
	 * 
	 * @return tekst
	 */
	public TekstPanel getTekst() {
		return tekst;
	}

	/**
	 * Getter voor het CijferPanel
	 * 
	 * @return getallen
	 */
	public CijferPanel getGetallen() {
		return getallen;
	}

	/**
	 * Getter voor de startbutton
	 * 
	 * @return de startbutton
	 */
	public JButton getStart() {
		return button.getConnect();
	}

	/**
	 * Voegt een ActionListener toe aan een knop
	 * 
	 * @param listener
	 *            de actionListener
	 */
	public void setConnectListener(ActionListener listener) {
		button.setConnectListener(listener);
	}

	/**
	 * Voegt een ActionListener toe aan een knop
	 * 
	 * @param listener
	 *            de actionListener
	 */
	public void setDisconnectListener(ActionListener listener) {
		button.setDisconnectListener(listener);
	}

	/**
	 * Voegt een ActionListener toe aan een knop
	 * 
	 * @param listener
	 *            de actionListener
	 */
	public void setSendListener(ActionListener listener) {
		button.setSendListener(listener);
	}

	/**
	 * Voegt een ActionListener toe aan een knop
	 * 
	 * @param listener
	 *            de actionListener
	 */
	public void setCheckListener(ActionListener listener) {
		buttons.setCheckListener(listener);
	}

	/**
	 * Voegt een ActionListener toe aan een knop
	 * 
	 * @param listener
	 *            de actionListener
	 */
	public void setSetListener(ActionListener listener) {
		buttons.setSetListener(listener);
	}

	/**
	 * Voegt een ActionListener toe aan een knop
	 * 
	 * @param listener
	 *            de actionListener
	 */
	public void setChangeListener(ActionListener listener) {
		buttons.setChangeListener(listener);
	}

	/**
	 * Getter voor de disconnect button
	 * 
	 * @return De disconnect button
	 */
	public JButton getDisconnect() {
		return button.getDisconnect();
	}

	/**
	 * Getter voor de send button
	 * 
	 * @return de send button
	 */
	public JButton getSend() {
		return button.getSend();
	}

	/**
	 * Getter voor de check button
	 * 
	 * @return de check button
	 */
	public JButton getCheck() {
		return buttons.getCheck();
	}

	/**
	 * Getter voor de change button
	 * 
	 * @return de change button
	 */
	public JButton getChange() {
		return buttons.getChange();
	}

	/**
	 * Getter voor de set button
	 * 
	 * @return de set button
	 */
	public JButton getSet() {
		return buttons.getSet();
	}

}
