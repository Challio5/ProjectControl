package Model;

import java.util.Observable;

/**
 * Dit is het model van onze kluis
 * 
 * @author Tom, Martijn, Rob en Laurens
 * 
 */
public class Kluis extends Observable {

	int[] ingevuldecode;
	boolean open;

	/**
	 * De constructor initialiseert de array en zet de kluis op gesloten.
	 */
	public Kluis() {
		ingevuldecode = new int[3];
		open = false;
	}

	/**
	 * getter voor de ingevulde code
	 * 
	 * @return de ingevulde code
	 */
	public int[] getIngevulde() {
		return ingevuldecode;
	}

	/**
	 * Getter voor het eerste ingevulde getal
	 * 
	 * @return het eerste ingevulde getal
	 */
	public int getFirstDigit() {
		return ingevuldecode[0];
	}

	/**
	 * Getter voor het tweede ingevulde getal
	 * 
	 * @return het tweede ingevulde getal
	 */
	public int getSecondDigit() {
		return ingevuldecode[1];
	}

	/**
	 * Getter voor het derde ingevulde getal
	 * 
	 * @return het derde ingevulde getal
	 */
	public int getThirdDigit() {
		return ingevuldecode[2];
	}

	/**
	 * Setter voor de ingevulde code
	 * 
	 * @param code
	 *            de code die je hebt ingevuld
	 */
	public void setIngevulde(int[] code) {
		ingevuldecode = code;
		setChanged();
		notifyObservers(code);
	}

	/**
	 * Setter voor het eerste getal
	 * 
	 * @param getal
	 *            het ingevulde getal
	 */
	public void setGetal1(int getal) {
		ingevuldecode[0] = getal;
		setChanged();
		notifyObservers(1);
	}

	/**
	 * Setter voor het tweede getal
	 * 
	 * @param getal
	 *            het ingevulde getal
	 */
	public void setGetal2(int getal) {
		ingevuldecode[1] = getal;
		setChanged();
		notifyObservers(2);
	}

	/**
	 * Setter voor het derde getal
	 * 
	 * @param getal
	 *            het ingevulde getal
	 */
	public void setGetal3(int getal) {
		ingevuldecode[2] = getal;
		setChanged();
		notifyObservers(3);
	}

	/**
	 * Setter om de kluis te openen
	 */
	public void setOpen() {
		open = true;
		setChanged();
		notifyObservers("open");
	}

	/**
	 * Setter om de kluis te sluiten
	 */
	public void setClosed() {
		open = false;
		setChanged();
		notifyObservers(open);
	}

}
