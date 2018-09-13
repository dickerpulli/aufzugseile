package de.tbosch.aufzugseile.gui.aufzug;

import java.awt.Dimension;

import de.tbosch.aufzugseile.utils.Constants;

// TODO: Auto-generated Javadoc
/**
 * The class of roll. The Umlenkrolle and Treibscheibe are both rolls.
 * 
 * @author Thomas Bosch (tbosch@gmx.de)
 */
public class Rolle extends Element {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -7212347550409016288L;

	/** The diameter in real millimeters. */
	private int durchmesser;

	/** The name. */
	private int id;

	/** The fs2 : seiltriebwirkungsgrad. */
	private double seiltriebwirkungsgrad;

	/** The fn4: schraegzug. */
	private double schraegzug;

	/** The fn3: seilrille. */
	private double seilrille;

	/** The doppelte umschlingung. */
	private boolean doppelteUmschlingung;

	/** The rolle 2te umschlingung. */
	private Rolle rolle2teUmschlingung;

	/** The rolle 1te umschlingung. */
	private Rolle rolle1teUmschlingung;

	/** The zweite umschlingung. */
	private boolean zweiteUmschlingung;

	/** The activated status. */
	private boolean activated;

	/** if there is a gegenbiegung before this roll. */
	private boolean gegenbiegung;

	/** If is part of doppel umlenkrolle. */
	private boolean partOfDoppelUmlenkrolle;

	/** The brother rolle, if it is part of a double roll. */
	private Umlenkrolle brother;

	/** keil angle in degrees. */
	private int keilwinkel;

	/** keil angle in degrees. */
	private int unterschnittwinkel;

	/** The form of the rille. */
	private String form;

	/** The parent double roll if existing. */
	private DoppelUmlenkrolle parentDoubleRollIfExisting;

	/** Mark for using other rope direction */
	private boolean otherRope = false;

	/** if the gegenbiegung can be set automaticly by program */
	private boolean gegenbiegungNotChangeable = false;

	/**
	 * Creates a new instance of Rolle Extends the element class.
	 * 
	 * @param w
	 *            The width
	 * @param s
	 *            The scale of width and height
	 * @param ys
	 *            The scale of the y-coordinates
	 * @param aufzugschacht
	 *            the aufzugschacht
	 * @param partOfDoppelUmlenkrolle
	 *            the part of doppel umlenkrolle
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
	 */
	public Rolle(int x, int y, int w, int h, double s, double xs, double ys, String i, Aufzugschacht aufzugschacht, boolean partOfDoppelUmlenkrolle,
			DoppelUmlenkrolle parent) {
		super(x, y, w, h, (w / 2), (h / 2), s, xs, ys, i, aufzugschacht);
		this.seiltriebwirkungsgrad = 1.0;
		this.schraegzug = 1.0;
		this.seilrille = 1.0;
		// deactivated by default
		this.activated = false;
		this.gegenbiegung = false;
		this.partOfDoppelUmlenkrolle = partOfDoppelUmlenkrolle;
		this.keilwinkel = 45;
		this.unterschnittwinkel = 0;
		this.form = Constants.FORM_RUND;
		this.doppelteUmschlingung = false;
		this.zweiteUmschlingung = false;
		this.parentDoubleRollIfExisting = parent;
	}

	/**
	 * Gets the radius.
	 * 
	 * @return the radius
	 */
	public int getRadius() {
		return getWidth() / 2;
	}

	/**
	 * Gets the durchmesser.
	 * 
	 * @return the durchmesser
	 */
	public int getDurchmesser() {
		return durchmesser;
	}

	/**
	 * Sets the durchmesser.
	 * 
	 * @param durchmesser
	 *            the durchmesser to set
	 */
	public void setDurchmesser(int durchmesser) {
		this.durchmesser = durchmesser;
	}

	/**
	 * Gets the fn4 : schraegzug.
	 * 
	 * @return the fn4 : schraegzug
	 */
	public double getFn4() {
		return schraegzug;
	}

	/**
	 * Sets the schraegzug.
	 * 
	 * @param schraegzug
	 *            the schraegzug to set
	 */
	public void setSchraegzug(double schraegzug) {
		this.schraegzug = schraegzug;
	}

	/**
	 * Gets the fs2 : seiltriebwirkungsgrad.
	 * 
	 * @return the fs2 : seiltriebwirkungsgrad
	 */
	public double getFs2() {
		return seiltriebwirkungsgrad;
	}

	/**
	 * Sets the seiltriebwirkungsgrad.
	 * 
	 * @param seiltriebwirkungsgrad
	 *            the seiltriebwirkungsgrad to set
	 */
	public void setSeiltriebwirkungsgrad(double seiltriebwirkungsgrad) {
		this.seiltriebwirkungsgrad = seiltriebwirkungsgrad;
	}

	/**
	 * Gets the fn3 : Seilrille.
	 * 
	 * @return the fn3
	 */
	public double getFn3() {
		return seilrille;
	}

	/**
	 * Sets the seilrille.
	 * 
	 * @param seilrille
	 *            the seilrille
	 */
	public void setSeilrille(double seilrille) {
		this.seilrille = seilrille;
	}

	/**
	 * Checks if is doppelte umschlingung.
	 * 
	 * @return the doppelteUmschlingung
	 */
	public boolean isDoppelteUmschlingung() {
		return doppelteUmschlingung;
	}

	/**
	 * Sets the doppelte umschlingung.
	 * 
	 * @param doppelteUmschlingung
	 *            the doppelteUmschlingung to set
	 */
	public void setDoppelteUmschlingung(boolean doppelteUmschlingung) {
		// if there is no double enlancement at this time
		if (!this.doppelteUmschlingung && doppelteUmschlingung) {
			this.doppelteUmschlingung = doppelteUmschlingung;
			if (this instanceof Treibscheibe) {
				Treibscheibe tr = (Treibscheibe) this;
				rolle2teUmschlingung = new Treibscheibe(tr.getX(), tr.getY(), tr.getWidth(), tr.getHeight(), 1, 1, 1, null, getAufzugschacht());
				rolle2teUmschlingung.setDurchmesser(tr.getDurchmesser());
			} else {
				Umlenkrolle um = (Umlenkrolle) this;
				rolle2teUmschlingung = new Umlenkrolle(um.getX(), um.getY(), um.getWidth(), um.getHeight(), 1, 1, 1, null, getAufzugschacht(), false, null,
						false, null);
				rolle2teUmschlingung.setDurchmesser(um.getDurchmesser());
			}
			rolle2teUmschlingung.setRolle1teUmschlingung(this);
			rolle2teUmschlingung.setZweiteUmschlingung(true);
			rolle2teUmschlingung.setActivated(true);
			// correct tooltip text
			setToolTipText(getName() + getID() + "/" + rolle2teUmschlingung.getName() + rolle2teUmschlingung.getID());
		} else if (!doppelteUmschlingung) {
			// reset label after deleting double enlancement
			setToolTipText(getName() + getID());
		}
		this.doppelteUmschlingung = doppelteUmschlingung;
	}

	/**
	 * Gets the rolle2te umschlingung.
	 * 
	 * @return the rolle2teUmschlingung
	 */
	public Rolle getRolle2teUmschlingung() {
		return rolle2teUmschlingung;
	}

	/**
	 * Gets the ID.
	 * 
	 * @return the ID
	 */
	public int getID() {
		return id;
	}

	/**
	 * Sets the ID.
	 * 
	 * @param id
	 *            the ID
	 */
	public void setID(int id) {
		this.id = id;
	}

	/**
	 * Checks if the roll is activated for calculation.
	 * 
	 * @return true, if is activated
	 */
	public boolean isActivated() {
		return activated;
	}

	/**
	 * Sets the activated.
	 * 
	 * @param activated
	 *            the activated
	 */
	public void setActivated(boolean activated) {
		this.activated = activated;
	}

	/**
	 * Checks if is gegenbiegung.
	 * 
	 * @return true, if is gegenbiegung
	 */
	public boolean isGegenbiegung() {
		return gegenbiegung;
	}

	/**
	 * Sets the gegenbiegung.
	 * 
	 * @param gegenbiegung
	 *            the gegenbiegung
	 */
	public void setGegenbiegung(boolean gegenbiegung) {
		if (!gegenbiegungNotChangeable) {
			this.gegenbiegung = gegenbiegung;
		} else {
			Constants.LOGGER.fine("Reverse bend not setable");
		}
	}

	/**
	 * Checks if is part of doppel umlenkrolle.
	 * 
	 * @return true, if is part of doppel umlenkrolle
	 */
	public boolean isPartOfDoppelUmlenkrolle() {
		return partOfDoppelUmlenkrolle;
	}

	/**
	 * Gets the brother. This is the second roll of this double roll.
	 * 
	 * @return the brother
	 */
	public Umlenkrolle getBrother() {
		return brother;
	}

	/**
	 * Sets the brother.
	 * 
	 * @param brother
	 *            the brother
	 */
	public void setBrother(Umlenkrolle brother) {
		this.brother = brother;
	}

	/**
	 * Gets the keilwinkel.
	 * 
	 * @return the keilwinkel
	 */
	public int getKeilwinkel() {
		return keilwinkel;
	}

	/**
	 * Sets the keilwinkel.
	 * 
	 * @param keilwinkel
	 *            the keilwinkel
	 */
	public void setKeilwinkel(int keilwinkel) {
		this.keilwinkel = keilwinkel;
	}

	/**
	 * Gets the unterschnittwinkel.
	 * 
	 * @return the unterschnittwinkel
	 */
	public int getUnterschnittwinkel() {
		return unterschnittwinkel;
	}

	/**
	 * Sets the unterschnittwinkel.
	 * 
	 * @param unterschnittwinkel
	 *            the unterschnittwinkel
	 */
	public void setUnterschnittwinkel(int unterschnittwinkel) {
		this.unterschnittwinkel = unterschnittwinkel;
	}

	/**
	 * Gets the form.
	 * 
	 * @return the form
	 */
	public String getForm() {
		if (form == null) {
			Constants.LOGGER.severe("getForm() is null -> form=default");
			form = Constants.FORM_RUND;
		}
		return form;
	}

	/**
	 * Sets the form.
	 * 
	 * @param form
	 *            the form
	 */
	public void setForm(String form) {
		this.form = form;
	}

	/**
	 * Checks if is zweite umschlingung.
	 * 
	 * @return true, if is zweite umschlingung
	 */
	public boolean isZweiteUmschlingung() {
		return zweiteUmschlingung;
	}

	/**
	 * Sets the zweite umschlingung.
	 * 
	 * @param zweiteUmschlingung
	 *            the zweite umschlingung
	 */
	public void setZweiteUmschlingung(boolean zweiteUmschlingung) {
		this.zweiteUmschlingung = zweiteUmschlingung;
	}

	/**
	 * Gets the parent double roll, if existing.
	 * 
	 * @return the parent double roll if existing
	 */
	public DoppelUmlenkrolle getParentDoubleRoll() {
		return parentDoubleRollIfExisting;
	}

	public Rolle getRolle1teUmschlingung() {
		return rolle1teUmschlingung;
	}

	public void setRolle1teUmschlingung(Rolle rolle1teUmschlingung) {
		this.rolle1teUmschlingung = rolle1teUmschlingung;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Component#setLocation(int, int)
	 */
	@Override
	public void setLocation(int x, int y) {
		super.setLocation(x, y);
		if (rolle2teUmschlingung != null) {
			rolle2teUmschlingung.setLocation(x, y);
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
		if (rolle2teUmschlingung != null) {
			rolle2teUmschlingung.setSize(width, height);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Component#setSize(java.awt.Dimension)
	 */
	@Override
	public void setSize(Dimension d) {
		super.setSize(d);
		if (rolle2teUmschlingung != null) {
			rolle2teUmschlingung.setSize(d);
		}
	}

	/**
	 * Changes the rope on the roll. Tries to change the direction.
	 */
	public void changeSeil() {
		if (otherRope == true) {
			otherRope = false;
		} else {
			otherRope = true;
		}
	}

	/**
	 * @return if the roll uses the other rope direction
	 */
	public boolean isOtherRope() {
		return otherRope;
	}

	/**
	 * @param otherRope
	 */
	public void setOtherRope(boolean otherRope) {
		this.otherRope = otherRope;
	}

	/**
	 * Sets the ability of changing Gegenbiegung
	 * 
	 * @param gegenbiegungNotChangeable
	 */
	public void setGegenbiegungNotChangeable(boolean gegenbiegungNotChangeable) {
		this.gegenbiegungNotChangeable = gegenbiegungNotChangeable;
	}

	/**
	 * @return if Gegenbiegung is changeable or not
	 */
	public boolean isGegenbiegungNotChangeable() {
		return gegenbiegungNotChangeable;
	}
}
