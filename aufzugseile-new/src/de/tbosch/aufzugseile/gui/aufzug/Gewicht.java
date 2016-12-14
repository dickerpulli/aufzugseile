package de.tbosch.aufzugseile.gui.aufzug;

import de.tbosch.seile.commons.elemente.Kapsel;

/**
 * The gewicht class.
 * 
 * @author Thomas Bosch (tbosch@gmx.de)
 */
public class Gewicht extends AufzugschachtEndElement implements Kapsel {

	/** The mass. */
	private int mass;
	
	/** part of ... */
	private double part;
	
	/**
	 * Creates a new instance of Gewicht Extends the element class.
	 * 
	 * @param w The width
	 * @param s The scale of width and height
	 * @param ys The scale of the y-coordinates
	 * @param a The elevator itself
	 * @param xs The scale of the x-coordinates
	 * @param h The height
	 * @param y The y-coordinates
	 * @param i The path of the icon
	 * @param x The x-coordinates
	 */
	public Gewicht(int x, int y, int w, int h, double s, double xs, double ys, String i, Aufzugschacht a) {
		super(x, y, w, h, (w / 2), 0, s, xs, ys, i, a);
		// default: 1500 kg = 1000 kg kabine + 500 kg durchschn. zuladung
		setMass(1500);
		setPart(0.5);
		setName("Gewicht");
	}

	/**
	 * Gets the mass.
	 * 
	 * @return the mass
	 */
	public int getMass() {
		return mass;
	}

	/**
	 * Sets the mass.
	 * 
	 * @param mass the mass
	 */
	public void setMass(int mass) {
		this.mass = mass;
	}

	/**
	 * Gets the part.
	 * 
	 * @return the part
	 */
	public double getPart() {
		return part;
	}

	/**
	 * Sets the part.
	 * 
	 * @param part the part
	 */
	public void setPart(double part) {
		this.part = part;
	}

    public int getZuladung() {
        return 0;
    }

    public double getDurchschnitt() {
        return 1;
    }

    public double getProfil() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
