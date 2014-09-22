package Controllers;

import java.io.BufferedReader;

import Views.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import Model.*;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.util.Enumeration;

import nl.projectgroepb1.projectcontrol.ProtocolConstanten;

/**
 * De Serial Handler klasse, deze klasse handelt alle data af die via de
 * SerialPort binnen komt.
 * 
 * @author Tom, Martijn, Rob en Laurens
 * 
 */
public class SerialHandler implements SerialPortEventListener {
	SerialPort serialPort;
	Kluis kluis;
	ViewKluis viewKluis;
	String code;
	ControlKluis controlKluis;

	/** The port we're normally going to use. */
	private static final String PORT_NAMES[] = { "/dev/tty.usbmodem411", // Mac
																			// OS
																			// X
			"/dev/ttyUSB0", // Linux
			"COM6", // Windows
			"COM5" };
	/**
	 * A BufferedReader which will be fed by a InputStreamReader converting the
	 * bytes into characters making the displayed results codepage independent
	 */
	private BufferedReader input;
	/** The output stream to the port */
	private OutputStream output;
	/** Milliseconds to block while waiting for port open */
	private static final int TIME_OUT = 2000;
	/** Default bits per second for COM port. */
	private static final int DATA_RATE = 9600;

	/**
	 * De constructor
	 * 
	 * @param kluis
	 *            het model
	 * @param kluisview
	 *            de view
	 * @param controlKluis
	 *            de Control
	 */
	public SerialHandler(Kluis kluis, ViewKluis kluisview,
			ControlKluis controlKluis) {
		this.kluis = kluis;
		this.viewKluis = kluisview;
		this.controlKluis = controlKluis;
	}

	/**
	 * Deze methode initialiseert de verbinding met de SerialPort
	 */
	public void initialize() {
		CommPortIdentifier portId = null;
		@SuppressWarnings("rawtypes")
		Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

		// First, Find an instance of serial port as set in PORT_NAMES.
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier currPortId = (CommPortIdentifier) portEnum
					.nextElement();
			for (String portName : PORT_NAMES) {
				if (currPortId.getName().equals(portName)) {
					portId = currPortId;
					break;
				}
			}
		}
		if (portId == null) {
			System.out.println("Could not find COM port.");
			return;
		}

		try {
			// open serial port, and use class name for the appName.
			serialPort = (SerialPort) portId.open(this.getClass().getName(),
					TIME_OUT);

			// set port parameters
			serialPort.setSerialPortParams(DATA_RATE, SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

			// open the streams
			input = new BufferedReader(new InputStreamReader(
					serialPort.getInputStream()));
			setOutput(serialPort.getOutputStream());

			// add event listeners
			serialPort.addEventListener(this);
			serialPort.notifyOnDataAvailable(true);
		} catch (Exception e) {
			System.err.println(e.toString());
		}
		viewKluis.connect();
	}

	/**
	 * This should be called when you stop using the port. This will prevent
	 * port locking on platforms like Linux.
	 */
	public synchronized void close() {
		if (serialPort != null) {
			serialPort.removeEventListener();
			serialPort.close();
			viewKluis.setEnabled(false);
		}
		viewKluis.disconnect();
	}

	/**
	 * Handle an event on the serial port. Read the data and print it.
	 */
	public synchronized void serialEvent(SerialPortEvent oEvent) {
		String inputLine = "";
		if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
				inputLine = input.readLine();
				System.out.println(inputLine);
			} catch (Exception e) {
				System.err.println(e.toString());
			}
			String[] commands = splitCommand(inputLine);

			if (commands[0].equals(ProtocolConstanten.rotary)) {
				if (commands[1].equals("0")) {
					kluis.setGetal1(Integer.parseInt(commands[2]));
				}
				if (commands[1].equals("1")) {
					kluis.setGetal2(Integer.parseInt(commands[2]));
				}
				if (commands[1].equals("2")) {
					kluis.setGetal3(Integer.parseInt(commands[2]));
				}
			} else if (commands[0].equals(ProtocolConstanten.open)) {
				if (commands[1].equals("true")) {
					kluis.setOpen();
				} else if (commands[1].equals("false")) {
					kluis.setClosed();
				}
			} else if (commands[0].equals(ProtocolConstanten.code)) {
				kluis.notifyObservers("Code is " + commands[1] + " "
						+ commands[2] + " " + commands[3]);
			}
		}
		// Ignore all the other eventTypes, but you should consider the other
		// ones.
	}

	/**
	 * Deze methode send de data die in de tekstfield staat via de OutputStream
	 */
	public synchronized void send() {
		String data = viewKluis.getTekst().getField().getText() + '\n';
		try {
			System.out.println(data);
			OutputStream output = serialPort.getOutputStream();
			output.write(data.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			System.out.println("Nog niet verbonden");
		}
		viewKluis.getTekst().getField().setText(null);
		data = "";
	}

	/**
	 * Deze methode send de data die wordt meegegeven over de OutputStream
	 * 
	 * @param data
	 *            de String die verzonden moet worden
	 */
	public synchronized void send(String data) {
		try {
			System.out.println(data);
			OutputStream output = serialPort.getOutputStream();
			output.write(data.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		viewKluis.getTekst().getField().setText(null);
	}

	/**
	 * Splitst een binnengekomen commando aan de hand van de Protocol.DELIMITER
	 * 
	 * @param cmd
	 *            te splitsen commando
	 * @return String-array die het commando en de argumenten bevat
	 */
	public String[] splitCommand(String cmd) {
		return cmd.split(ProtocolConstanten.delimiter);
	}

	/**
	 * Getter voor de OutputStream
	 * 
	 * @return de output
	 */
	public OutputStream getOutput() {
		return output;
	}

	/**
	 * Setter voor de OutputStream
	 * 
	 * @param output
	 *            de Outputstream die wordt gezet
	 */
	public void setOutput(OutputStream output) {
		this.output = output;
	}

}