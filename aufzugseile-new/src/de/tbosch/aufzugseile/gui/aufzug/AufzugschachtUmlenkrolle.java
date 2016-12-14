package de.tbosch.aufzugseile.gui.aufzug;

import de.tbosch.aufzugseile.gui.utils.Constants;
import de.tbosch.seile.commons.elemente.Element;
import de.tbosch.seile.commons.elemente.Umlenkrolle;
import java.util.logging.Logger;

/**
 * The class for umlenkrolle.
 * 
 * @author Thomas Bosch (tbosch@gmx.de)
 */
public class AufzugschachtUmlenkrolle extends AufzugschachtRolle implements Umlenkrolle {
    
    /** The Logger */
    private static final Logger logger = Logger.getLogger(AufzugschachtUmlenkrolle.class.getName());

	/** If the roll is free or not. */
	private boolean free;

	/** The element hanging at the rolle ... if there is someone. */
	private AufzugschachtElement hangingElement;
	
	/** The ohne waelzlagerung. */
	private boolean ohneWaelzlagerung;

	/** The relative scale in relation to the treibscheibe. */
	private double relScale;
	
	/** The horiz distance to the treibscheibe in centimeters. */
	private int horizEntf, vertEntf;

	/**
	 * Creates a new instance of Umlenkroll Extends the roll class.
	 * 
	 * @param w The width
	 * @param lose the lose
	 * @param s The scale of width and height
	 * @param aufzugschacht the aufzugschacht
	 * @param ys The scale of the y-coordinates
	 * @param p if roll is part of a double roll
	 * @param hangingElement the hanging element
	 * @param xs The scale of the x-coordinates
	 * @param y The y-coordinates
	 * @param h The height
	 * @param x The x-coordinates
	 * @param i The path of the icon
	 */
	public AufzugschachtUmlenkrolle(int x, int y, int w, int h, double s, double xs, double ys, String i, Aufzugschacht aufzugschacht, boolean lose, AufzugschachtElement hangingElement, boolean p, AufzugschachtDoppelUmlenkrolle d) {
		super(x, y, w, h, s, xs, ys, i, aufzugschacht, p, d);
		if (aufzugschacht.getTreibscheibe() != null) {
			relScale = (double)getDurchmesser() / aufzugschacht.getTreibscheibe().getDurchmesser();
			int thisMX = x + (int)((w * s) / 2);
			int thisMY = y + (int)((h * s) / 2);
			horizEntf = (((thisMX - ((AufzugschachtTreibscheibe)aufzugschacht.getTreibscheibe()).getMPoint().x) * aufzugschacht.getMmPerPixel()));
			vertEntf = (((thisMY - ((AufzugschachtTreibscheibe)aufzugschacht.getTreibscheibe()).getMPoint().y) * aufzugschacht.getMmPerPixel()));
        }
        this.free = lose;
		this.hangingElement = hangingElement;
		// default: 40 cm
		setDurchmesser(400);
		setName("Rolle");
		setID(aufzugschacht.getIDPoolUmlenkrolle().getID());
		setOhneWaelzlagerung(false);
		setToolTipText(getName() + getID());
	}

	/* (non-Javadoc)
	 * @see java.awt.Component#setLocation(int, int)
	 */
	@Override
	public void setLocation(int x, int y) {
		super.setLocation(x, y);
		if (getSchacht() != null && getSchacht().getTreibscheibe() != null) {
			horizEntf = (((getMPoint().x - ((AufzugschachtTreibscheibe)getSchacht().getTreibscheibe()).getMPoint().x) * ((Aufzugschacht)getSchacht()).getMmPerPixel()));
			vertEntf = (((getMPoint().y - ((AufzugschachtTreibscheibe)getSchacht().getTreibscheibe()).getMPoint().y) * ((Aufzugschacht)getSchacht()).getMmPerPixel()));
		}
	}
	
	/* (non-Javadoc)
	 * @see java.awt.Component#setSize(int, int)
	 */
	@Override
	public void setSize(int width, int height) {
		super.setSize(width, height);
		if (width > 0 && width <= 16) {
			setImage(Constants.UMLENKROLLE_ELEMENT_ICON_16);
		}
		else if (width > 16 && width <= 32) {
			setImage(Constants.UMLENKROLLE_ELEMENT_ICON_32);
		}
		else if (width > 32 && width <= 48) {
			setImage(Constants.UMLENKROLLE_ELEMENT_ICON_48);	
		}
		else {
			setImage(Constants.UMLENKROLLE_ELEMENT_ICON_64);
		}
	}

	/* (non-Javadoc)
	 * @see de.tbosch.aufzugseile.gui.aufzug.Rolle#setDurchmesser(int)
	 */
	@Override
	public void setDurchmesser(int durchmesser) {
		super.setDurchmesser(durchmesser);
		refreshSize();
	}

	/**
	 * Refresh the position position in relation to the treibscheibe.
	 */
	public void refreshPosition() {
		if (getSchacht() != null && getSchacht().getTreibscheibe() != null) {
			int nx = ((AufzugschachtTreibscheibe)getSchacht().getTreibscheibe()).getMPoint().x + ((horizEntf) / ((Aufzugschacht)getSchacht()).getMmPerPixel()) - getRadius();
			int ny = ((AufzugschachtTreibscheibe)getSchacht().getTreibscheibe()).getMPoint().y + ((vertEntf) / ((Aufzugschacht)getSchacht()).getMmPerPixel()) - getRadius();
			setLocation(nx, ny);
			if (getSchacht().getSeil() != null) {
				((AufzugschachtSeil)getSchacht().getSeil()).repaint();
				((AufzugschachtSeil)getSchacht().getSeil()).refreshSeil();
			}
		}
	}
	
	/**
	 * Refresh the size in relation to the treibscheibe.
	 */
	public void refreshSize() {
		if (getSchacht() != null && getSchacht().getTreibscheibe() != null) {
			AufzugschachtTreibscheibe scheibe = (AufzugschachtTreibscheibe)getSchacht().getTreibscheibe();
			relScale = (double)getDurchmesser() / scheibe.getDurchmesser();
			setSize((int)(scheibe.getWidth() * relScale), (int)(scheibe.getHeight() * relScale));
			((Aufzugschacht)getSchacht()).moveAllUmlenkrolle();
			if (getSchacht().getSeil() != null) {
				((AufzugschachtSeil)getSchacht().getSeil()).repaint();
				((AufzugschachtSeil)getSchacht().getSeil()).refreshSeil();
			}
		}
	}

	/**
	 * Gets the horizontal distance to the treibscheibe.
	 * 
	 * @return the horizontal distance
	 */
	public int getHorizEntf() {
		return horizEntf;
	}

	/**
	 * Sets the horizontal distance to the treibscheibe.
	 * 
	 * @param horizEntf the horizontal distance to set
	 */
	public void setHorizEntf(int horizEntf) {
		this.horizEntf = horizEntf;
	}

	/**
	 * Gets the vertical distance to the treibscheibe.
	 * 
	 * @return the vertical distance
	 */
	public int getVertEntf() {
		return vertEntf;
	}

	/**
	 * Sets the vertical distance to the treibscheibe.
	 * 
	 * @param vertEntf the vertical distance to set
	 */
	public void setVertEntf(int vertEntf) {
		this.vertEntf = vertEntf;
	}

	public boolean isFree() {
		return free;
	}

	public Element getHangingElement() {
		return hangingElement;
	}

	public boolean isOhneWaelzlagerung() {
		return ohneWaelzlagerung;
	}

	public void setOhneWaelzlagerung(boolean ohneWaelzlagerung) {
		this.ohneWaelzlagerung = ohneWaelzlagerung;
	}
}
