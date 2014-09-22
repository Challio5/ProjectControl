// Projectgroep b1 2014

package Views;

import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * De klasse buttenPanel, dit is de onderste panel van het frame met de connect
 * send en disconnect button.
 * 
 * @author Tom, Martijn, Rob en Laurens
 * @version 1.0
 */
@SuppressWarnings("serial")
public class ButtonPanel extends JPanel {

	private JButton connect;
	private JButton send;
	private JButton disconnect;

	/**
	 * De constructor
	 */
	public ButtonPanel() {

		this.setLayout(new GridLayout(1, 3));

		connect = new JButton("Connect");
		send = new JButton("send");
		disconnect = new JButton("Disconnect");

		send.setEnabled(false);
		disconnect.setEnabled(false);

		this.add(connect);
		this.add(send);
		this.add(disconnect);
	}

	/**
	 * Methode voor het enabelen van de send en disconnect button.
	 */
	public void setEnabled(boolean bool) {
		send.setEnabled(bool);
		disconnect.setEnabled(bool);
	}

	/**
	 * Getter voor de connect button
	 * 
	 * @return de connect button
	 */
	public JButton getConnect() {
		return connect;
	}

	/**
	 * Getter voor de send button
	 * 
	 * @return de send button
	 */
	public JButton getSend() {
		return send;
	}

	/**
	 * Getter voor de disconnect buuton
	 * 
	 * @return de disconnect button
	 */
	public JButton getDisconnect() {
		return disconnect;
	}

	/**
	 * Setter voor de actionListener
	 * 
	 * @param listener
	 *            de actionListener
	 */
	public void setDisconnectListener(ActionListener listener) {
		disconnect.addActionListener(listener);
	}

	/**
	 * Setter voor de actionListener
	 * 
	 * @param listener
	 *            de actionListener
	 */
	public void setSendListener(ActionListener listener) {
		send.addActionListener(listener);
	}

	/**
	 * Setter voor de actionListener
	 * 
	 * @param listener
	 *            de actionListener
	 */
	public void setConnectListener(ActionListener listener) {
		connect.addActionListener(listener);
	}
}
