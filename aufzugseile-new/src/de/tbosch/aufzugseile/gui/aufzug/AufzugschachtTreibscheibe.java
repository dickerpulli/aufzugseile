package de.tbosch.aufzugseile.gui.aufzug;

import de.tbosch.aufzugseile.gui.utils.Constants;
import de.tbosch.seile.commons.elemente.Treibscheibe;

/**
 * The class of treibscheibe.
 * 
 * @author Thomas Bosch (tbosch@gmx.de)
 */
public class AufzugschachtTreibscheibe extends AufzugschachtRolle implements Treibscheibe {

	/**
	 * Creates a new instance of Treibscheibe Extends the roll class.
	 * 
	 * @param w The width
	 * @param s The scale of width and height
	 * @param aufzugschacht the aufzugschacht
	 * @param ys The scale of the y-coordinates
	 * @param xs The scale of the x-coordinates
	 * @param y The y-coordinates
	 * @param h The height
	 * @param x The x-coordinates
	 * @param i The path of the icon
	 */
	public AufzugschachtTreibscheibe(int x, int y, int w, int h, double s, double xs, double ys, String i, Aufzugschacht aufzugschacht) {
		super(x, y, w, h, s, xs, ys, i, aufzugschacht, false, null);
		// default 400 mm
		setDurchmesser(400);
		setName("TS");
		setID(aufzugschacht.getIDPoolTreibscheibe().getID());
		setToolTipText(getName() + getID());
	}
	
	/* (non-Javadoc)
	 * @see java.awt.Component#setLocation(int, int)
	 */
	@Override
	public void setLocation(int x, int y) {
		super.setLocation(x, y);
		if (getSchacht() != null) ((Aufzugschacht)getSchacht()).moveAllUmlenkrolle();
		if (getSchacht() != null) ((Aufzugschacht)getSchacht()).moveAllSeilaufhaenger();
	}
	
	/* (non-Javadoc)
	 * @see java.awt.Component#setSize(int, int)
	 */
	@Override
	public void setSize(int width, int height) {
		super.setSize(width, height);
		if (width > 0 && width <= 16) {
			setImage(Constants.TREIBSCHEIBE_ELEMENT_ICON_16);
		}
		else if (width > 16 && width <= 32) {
			setImage(Constants.TREIBSCHEIBE_ELEMENT_ICON_32);
		}
		else if (width > 32 && width <= 48) {
			setImage(Constants.TREIBSCHEIBE_ELEMENT_ICON_48);	
		}
		else {
			setImage(Constants.TREIBSCHEIBE_ELEMENT_ICON_64);
		}
	}
    
    /* (non-Javadoc)
	 * @see de.tbosch.aufzugseile.gui.aufzug.Rolle#setDurchmesser(int)
	 */
    @Override
	public void setDurchmesser(int durchmesser) {
		super.setDurchmesser(durchmesser);
		if (getSchacht() != null) ((Aufzugschacht)getSchacht()).sizeAllUmlenkrolle();
	}

}
