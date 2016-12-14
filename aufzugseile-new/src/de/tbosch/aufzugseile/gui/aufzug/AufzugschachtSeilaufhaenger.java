/**
 * Seilaufhaenger.java
 *
 * Created on 11. April 2007, 15:50
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package de.tbosch.aufzugseile.gui.aufzug;

import de.tbosch.aufzugseile.gui.utils.Constants;
import de.tbosch.seile.commons.elemente.Element;
import de.tbosch.seile.commons.elemente.Seilaufhaenger;

/**
 * The class of rope holder.
 * The rope is clipped at a rope holder at the beginning and the end
 * 
 * @author Thomas Bosch (tbosch@gmx.de)
 */
public class AufzugschachtSeilaufhaenger extends AufzugschachtElement implements Seilaufhaenger {
   

	/** The orientation of the object. */
	private int himmel;
	
	/** The under treibscheibe. */
	private boolean underTreibscheibe;
	
	/** The y_under treibscheibe. */
	private int y_underRel;

	/** The lose. */
	private boolean free;

	/** The element hanging at the rope holder if lose. */
	private Element hangingElement;

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
	public AufzugschachtSeilaufhaenger(int x, int y, int w, int h, double s, double xs, double ys, String i, Aufzugschacht aufzugschacht, boolean lose, Element hangingElement, boolean underTreibscheibe) {
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
			int y_underTreibscheibe =  getY() - ((AufzugschachtTreibscheibe)aufzugschacht.getTreibscheibe()).getY();
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
	 * Gets the hanging element.
	 * 
	 * @return the element hanging at the rope holder
	 */
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

	public boolean isFree() {
		return free;
	}

	public Element getHangingElement() {
		return hangingElement;
	}
}
