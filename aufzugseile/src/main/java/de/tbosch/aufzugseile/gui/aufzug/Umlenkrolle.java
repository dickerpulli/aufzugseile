package de.tbosch.aufzugseile.gui.aufzug;

import de.tbosch.aufzugseile.utils.Constants;

// TODO: Auto-generated Javadoc
/**
 * The class for umlenkrolle.
 * 
 * @author Thomas Bosch (tbosch@gmx.de)
 */
public class Umlenkrolle extends Rolle {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 3395575138686857741L;

	/** If the roll is free or not. */
	private boolean free;

	/** The element hanging at the rolle ... if there is someone. */
	private Element hangingElement;

	/** The relative scale in relation to the treibscheibe. */
	private double relScale;

	/** The horiz distance to the treibscheibe in centimeters. */
	private int horizEntf, vertEntf;

	/** The ohne waelzlagerung. */
	private boolean ohneWaelzlagerung;

	/**
	 * Creates a new instance of Umlenkroll Extends the roll class.
	 * 
	 * @param w
	 *            The width
	 * @param lose
	 *            the lose
	 * @param s
	 *            The scale of width and height
	 * @param aufzugschacht
	 *            the aufzugschacht
	 * @param ys
	 *            The scale of the y-coordinates
	 * @param p
	 *            if roll is part of a double roll
	 * @param hangingElement
	 *            the hanging element
	 * @param xs
	 *            The scale of the x-coordinates
	 * @param y
	 *            The y-coordinates
	 * @param h
	 *            The height
	 * @param x
	 *            The x-coordinates
	 * @param i
	 *            The path of the icon
	 * @param d
	 *            The double roll (if exists, NULL otherwise)
	 */
	public Umlenkrolle(int x, int y, int w, int h, double s, double xs, double ys, String i, Aufzugschacht aufzugschacht, boolean lose, Element hangingElement,
			boolean p, DoppelUmlenkrolle d) {
		super(x, y, w, h, s, xs, ys, i, aufzugschacht, p, d);
		this.free = lose;
		this.hangingElement = hangingElement;
		// default: 40 cm
		setDurchmesser(400);
		if (aufzugschacht.getTreibscheibe() != null) {
			relScale = (double) getDurchmesser() / aufzugschacht.getTreibscheibe().getDurchmesser();
			int thisMX = x + (int) ((w * s) / 2);
			int thisMY = y + (int) ((h * s) / 2);
			horizEntf = (((thisMX - aufzugschacht.getTreibscheibe().getMPoint().x) * aufzugschacht.getMmPerPixel()));
			vertEntf = (((thisMY - aufzugschacht.getTreibscheibe().getMPoint().y) * aufzugschacht.getMmPerPixel()));
		}
		setName("Rolle");
		setID(aufzugschacht.getIDPoolUmlenkrolle().getID());
		setToolTipText(getName() + getID());
		setOhneWaelzlagerung(false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Component#setLocation(int, int)
	 */
	@Override
	public void setLocation(int x, int y) {
		super.setLocation(x, y);
		if (getAufzugschacht() != null && getAufzugschacht().getTreibscheibe() != null) {
			horizEntf = (((getMPoint().x - getAufzugschacht().getTreibscheibe().getMPoint().x) * getAufzugschacht().getMmPerPixel()));
			vertEntf = (((getMPoint().y - getAufzugschacht().getTreibscheibe().getMPoint().y) * getAufzugschacht().getMmPerPixel()));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Component#setSize(int, int)
	 */
	@Override
	public void setSize(int width, int height) {
		super.setSize(width, height);
		if (width > 0 && width <= 16) {
			setImage(Constants.UMLENKROLLE_ELEMENT_ICON_16);
		} else if (width > 16 && width <= 32) {
			setImage(Constants.UMLENKROLLE_ELEMENT_ICON_32);
		} else if (width > 32 && width <= 48) {
			setImage(Constants.UMLENKROLLE_ELEMENT_ICON_48);
		} else {
			setImage(Constants.UMLENKROLLE_ELEMENT_ICON_64);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
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
		if (getAufzugschacht() != null && getAufzugschacht().getTreibscheibe() != null) {
			int nx = getAufzugschacht().getTreibscheibe().getMPoint().x + ((horizEntf) / getAufzugschacht().getMmPerPixel()) - getRadius();
			int ny = getAufzugschacht().getTreibscheibe().getMPoint().y + ((vertEntf) / getAufzugschacht().getMmPerPixel()) - getRadius();
			setLocation(nx, ny);
			if (getAufzugschacht().getSeil() != null) {
				getAufzugschacht().getSeil().repaint();
				getAufzugschacht().getSeil().refreshSeil();
			}
		}
	}

	/**
	 * Refresh the size in relation to the treibscheibe.
	 */
	public void refreshSize() {
		if (getAufzugschacht() != null && getAufzugschacht().getTreibscheibe() != null) {
			Treibscheibe scheibe = getAufzugschacht().getTreibscheibe();
			relScale = (double) getDurchmesser() / scheibe.getDurchmesser();
			setSize((int) (scheibe.getWidth() * relScale), (int) (scheibe.getHeight() * relScale));
			getAufzugschacht().moveAllUmlenkrolle();
			if (getAufzugschacht().getSeil() != null) {
				getAufzugschacht().getSeil().repaint();
				getAufzugschacht().getSeil().refreshSeil();
			}
		}
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
	 * @return the hangingElement
	 */
	public Element getHangingElement() {
		return hangingElement;
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
	 * @param horizEntf
	 *            the horizontal distance to set
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
	 * @param vertEntf
	 *            the vertical distance to set
	 */
	public void setVertEntf(int vertEntf) {
		this.vertEntf = vertEntf;
	}

	/**
	 * Checks if is ohne waelzlagerung.
	 * 
	 * @return true, if is ohne waelzlagerung
	 */
	public boolean isOhneWaelzlagerung() {
		return ohneWaelzlagerung;
	}

	/**
	 * Sets the ohne waelzlagerung.
	 * 
	 * @param ohneWaelzlagerung
	 *            the ohne waelzlagerung
	 */
	public void setOhneWaelzlagerung(boolean ohneWaelzlagerung) {
		this.ohneWaelzlagerung = ohneWaelzlagerung;
	}

}
