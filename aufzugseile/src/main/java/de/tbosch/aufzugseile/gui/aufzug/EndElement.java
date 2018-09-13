package de.tbosch.aufzugseile.gui.aufzug;

import de.tbosch.aufzugseile.gui.aufzug.seil.Seilaufhaenger;

/**
 * The meta element for an end element wich is at the end or the beginning of the rope.
 * 
 * @author Thomas Bosch (tbosch@gmx.de)
 */
public class EndElement extends Element {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5118477250664907329L;

	/** The rope holder. */
	private Seilaufhaenger seilaufhaenger;

	/** The umlenkrolle. */
	private Umlenkrolle umlenkrolle;

	/** The double umlenkrolle. */
	private DoppelUmlenkrolle doppelUmlenkrolle;

	/**
	 * Creates a new instance of EndElement.
	 * 
	 * @param w The width
	 * @param s The scale of width and height
	 * @param ys The scale of the y-coordinate
	 * @param a The elevator itself
	 * @param xs The scale of the x-coordinate
	 * @param sy The relative y-coordinte of the rope-clipping point
	 * @param y The y-coordinate
	 * @param h The height
	 * @param x The x-coordinate
	 * @param sx The relative x-coordinte of the rope-clipping point
	 * @param i The icon path
	 */
	public EndElement(int x, int y, int w, int h, int sx, int sy, double s, double xs, double ys, String i, Aufzugschacht a) {
		super(x, y, w, h, sx, sy, s, xs, ys, i, a);
	}

	/**
	 * Overrides the setLocation-method of Element.
	 * 
	 * @param y The new y-coordinate
	 * @param x The new x-coordinate
	 */
	public void setLocation(int x, int y) {
		super.setLocation(x, y);
		if (seilaufhaenger != null) {
			int sy = y - seilaufhaenger.getHeight();
			int sx = x + ((getWidth() - seilaufhaenger.getWidth()) / 2);
			seilaufhaenger.setLocation(sx, sy);
		}
		if (umlenkrolle != null) {
			int sy = y - umlenkrolle.getHeight();
			int sx = x + (getWidth() / 2) - (umlenkrolle.getWidth() / 2);
			umlenkrolle.setLocation(sx, sy);
		}
		if (doppelUmlenkrolle != null) {
			int sy = y - doppelUmlenkrolle.getHeight();
			int sx = x + (getWidth() / 2) - (doppelUmlenkrolle.getWidth() / 2);
			doppelUmlenkrolle.setLocation(sx, sy);
		}
	}

	/**
	 * Overrides the setScale-method of Element.
	 * 
	 * @param scale The scale of width and height
	 * @param yscale The scale of the y-coordinate
	 * @param xscale The scale of the x-coordinate
	 */
	public void setScale(double scale, double xscale, double yscale) {
		super.setScale(scale, xscale, yscale);
		if (seilaufhaenger != null) {
			seilaufhaenger.setScale(scale, xscale, yscale);
			seilaufhaenger.setLocation(getX() + ((getWidth() - seilaufhaenger.getWidth()) / 2), getY() - seilaufhaenger.getHeight());
		}
		if (umlenkrolle != null) {
			umlenkrolle.setScale(scale, xscale, yscale);
			umlenkrolle.setLocation(getX() + ((getWidth() - umlenkrolle.getWidth()) / 2), getY() - umlenkrolle.getHeight());
		}
		if (doppelUmlenkrolle != null) {
			doppelUmlenkrolle.setScale(scale, xscale, yscale);
			doppelUmlenkrolle.setLocation(getX() + ((getWidth() - doppelUmlenkrolle.getWidth()) / 2), getY() - doppelUmlenkrolle.getHeight());
		}
	}

	/**
	 * Sets the rope holder.
	 * 
	 * @param seilaufhaenger The rope holder
	 */
	public void setSeilaufhaenger(Seilaufhaenger seilaufhaenger) {
		this.seilaufhaenger = seilaufhaenger;
	}

	/**
	 * Gets the rope holder.
	 * 
	 * @return The rope holder
	 */
	public Seilaufhaenger getSeilaufhaenger() {
		return seilaufhaenger;
	}

	/**
	 * Sets the umlenkrolle.
	 * 
	 * @param umlenkrolle The umlenkrolle
	 */
	public void setUmlenkrolle(Umlenkrolle umlenkrolle) {
		this.umlenkrolle = umlenkrolle;
	}

	/**
	 * Gets the umlenkrolle.
	 * 
	 * @return The umlenkrolle
	 */
	public Umlenkrolle getUmlenkrolle() {
		return umlenkrolle;
	}

	/**
	 * Gets the double umlenkrolle.
	 * 
	 * @return the double umlenkrolle
	 */
	public DoppelUmlenkrolle getDoppelUmlenkrolle() {
		return doppelUmlenkrolle;
	}

	/**
	 * Sets the double umlenkrolle.
	 * 
	 * @param doppelUmlenkrolle the double umlenkrolle
	 */
	public void setDoppelUmlenkrolle(DoppelUmlenkrolle doppelUmlenkrolle) {
		this.doppelUmlenkrolle = doppelUmlenkrolle;
	}
}
