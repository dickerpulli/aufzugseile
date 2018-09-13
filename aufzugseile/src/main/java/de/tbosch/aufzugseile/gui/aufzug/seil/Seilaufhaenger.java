/**
 * Seilaufhaenger.java
 *
 * Created on 11. April 2007, 15:50
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package de.tbosch.aufzugseile.gui.aufzug.seil;

import de.tbosch.aufzugseile.gui.aufzug.Aufzugschacht;
import de.tbosch.aufzugseile.gui.aufzug.Element;
import de.tbosch.aufzugseile.utils.Constants;

// TODO: Auto-generated Javadoc
/**
 * The class of rope holder.
 * The rope is clipped at a rope holder at the beginning and the end
 * 
 * @author Thomas Bosch (tbosch@gmx.de)
 */
public class Seilaufhaenger extends Element {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7039181183185198198L;

	/** Constant for 'north' orientation. */
	public static int NORD = 0;

	/** Constant for 'east' orientation. */
	public static int OST = 1;

	/** Constant for 'south' orientation. */
	public static int SUED = 2;

	/** Constant for 'west' orientation. */
	public static int WEST = 3;

	/** The orientation of the object. */
	private int himmel;

	/** The lose. */
	private boolean free;

	/** The element hanging at the rope holder if lose. */
	private Element hangingElement;
	
	/** The under treibscheibe. */
	private boolean underTreibscheibe;
	
	/** The y_under treibscheibe. */
	private int y_underRel;

	/**
	 * Creates a new instance of Seilaufhaenger Extends the element class.
	 * 
	 * @param w The width
	 * @param lose the lose
	 * @param s The scale of width and height
	 * @param ys The scale of the y-coordinates
	 * @param aufzugschacht The elevator itself
	 * @param hangingElement the hanging element
	 * @param xs The scale of the x-coordinates
	 * @param h The height
	 * @param y The y-coordinates
	 * @param i The path of the icon
	 * @param x The x-coordinates
	 */
	public Seilaufhaenger(int x, int y, int w, int h, double s, double xs, double ys, String i, Aufzugschacht aufzugschacht, boolean lose, Element hangingElement, boolean underTreibscheibe) {
		super(x, y, w, h, (w / 2), (h / 2), s, xs, ys, i, aufzugschacht);
		int maxy = aufzugschacht.getHeight() - (int) (Constants.SEILAUFHAENGER_SIZE_W * s) - 1;
		int maxx = aufzugschacht.getWidth() - (int) (Constants.SEILAUFHAENGER_SIZE_H * s) - 1;
		if (x == 1 && y != 1)
			himmel = WEST;
		if (y == 1 && x != maxx)
			himmel = NORD;
		if (x == maxx && y != maxy)
			himmel = OST;
		if (y == maxy && x != 1)
			himmel = SUED;
		this.free = lose;
		this.hangingElement = hangingElement;
		this.underTreibscheibe = underTreibscheibe;
		if (underTreibscheibe && aufzugschacht.getTreibscheibe() != null) {
			int y_underTreibscheibe =  getY() - aufzugschacht.getTreibscheibe().getY();
			y_underRel = (int) ((double) y_underTreibscheibe / ys);
		}
	}
	
	/**
	 * Gives the orientation.
	 * 
	 * @return Orientation defined in constants
	 */
	public int getHimmel() {
		return himmel;
	}

	/**
	 * Checks if is free.
	 * 
	 * @return true, if is free
	 */
	public boolean isFree() {
		return free;
	}

	/**
	 * Gets the hanging element.
	 * 
	 * @return the element hanging at the rope holder
	 */
	public Element getHangingElement() {
		return hangingElement;
	}

	public int getY_underTreibscheibe() {
		//return (getY() * aufzugschacht.getTreibscheibe().getY());
		return (int)(y_underRel * getYscale());
	}
	
	/**
	 * Checks if is under treibscheibe.
	 * 
	 * @return true, if is under treibscheibe
	 */
	public boolean isUnderTreibscheibe() {
		return underTreibscheibe;
	}
}
