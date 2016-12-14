package de.tbosch.aufzugseile.gui.aufzug;

import de.tbosch.aufzugseile.gui.utils.Constants;
import de.tbosch.seile.commons.elemente.DoppelUmlenkrolle;
import de.tbosch.seile.commons.elemente.Element;
import de.tbosch.seile.commons.elemente.Rolle;
import de.tbosch.seile.commons.elemente.Umlenkrolle;


/**
 * The Class DoppelUmlenkrolle.
 */
public class AufzugschachtDoppelUmlenkrolle extends AufzugschachtElement implements DoppelUmlenkrolle {

	/** The relative scale in relation to the treibscheibe. */
	private double relScale;

	/** The element hanging at the rolle ... if there is someone. */
	private AufzugschachtElement hangingElement;
	
	/** The first roll. */
	private AufzugschachtUmlenkrolle rolle1;
	
	/** The second roll. */
	private AufzugschachtUmlenkrolle rolle2;
	
	/** The activated. */
	private boolean activated;
	
	/** keil angle in degrees. */ 
	private int keilwinkel;
	
	/** keil angle in degrees. */ 
	private int unterschnittwinkel;
	
	/** The form of the rille. */
	private String form;

	/**
	 * Creates a new instance of Rolle Extends the element class.
	 * 
	 * @param w The width
	 * @param s The scale of width and height
	 * @param aufzugschacht the aufzugschacht
	 * @param ys The scale of the y-coordinates
	 * @param hangingElement the hanging element
	 * @param xs The scale of the x-coordinates
	 * @param y The y-coordinates
	 * @param h The height
	 * @param x The x-coordinates
	 * @param i The path of the icon
	 */
	public AufzugschachtDoppelUmlenkrolle(int x, int y, int w, int h, double s, double xs, double ys, String i, Aufzugschacht aufzugschacht, AufzugschachtElement hangingElement) {
		super(x, y, w, h, (w / 2), (h / 2), s, xs, ys, i, aufzugschacht);
		rolle1 = new AufzugschachtUmlenkrolle(0,0,0,0,0,0,0,null,aufzugschacht,true,null,true,this);
		rolle2 = new AufzugschachtUmlenkrolle(0,0,0,0,0,0,0,null,aufzugschacht,true,null,true,this);
		rolle1.setBrother(rolle2);
		rolle2.setBrother(rolle1);
		this.form = Rolle.FORM_RUND;
		this.hangingElement = hangingElement;
		setToolTipText(rolle1.getToolTipText()+"/"+rolle2.getToolTipText());
		this.activated = false;
		if (aufzugschacht.getTreibscheibe() != null) {
			relScale = (double)getDurchmesser() / aufzugschacht.getTreibscheibe().getDurchmesser();
		}
		refreshSize();
	}
	
	/* (non-Javadoc)
	 * @see java.awt.Component#setSize(int, int)
	 */
	@Override
	public void setSize(int width, int height) {
		super.setSize(width, height);
		if (width > 0 && width <= 16) {
			setImage(Constants.DOPPELUMLENKROLLE_ELEMENT_ICON_16);
		}
		else if (width > 16 && width <= 32) {
			setImage(Constants.DOPPELUMLENKROLLE_ELEMENT_ICON_32);
		}
		else if (width > 32 && width <= 48) {
			setImage(Constants.DOPPELUMLENKROLLE_ELEMENT_ICON_48);	
		}
		else {
			setImage(Constants.DOPPELUMLENKROLLE_ELEMENT_ICON_64);
		}
	}
	
	/**
	 * Refresh the size in relation to the treibscheibe.
	 */
	public void refreshSize() {
		if (getSchacht() != null && getSchacht().getTreibscheibe() != null) {
			AufzugschachtTreibscheibe scheibe = (AufzugschachtTreibscheibe)getSchacht().getTreibscheibe();
			relScale = (double)getDurchmesser() / scheibe.getDurchmesser();
			setSize((int)(scheibe.getWidth() * relScale * 2), (int)(scheibe.getHeight() * relScale));
			((Aufzugschacht)getSchacht()).moveAllUmlenkrolle();
			if (getSchacht().getSeil() != null) {
				((AufzugschachtSeil)getSchacht().getSeil()).repaint();
				((AufzugschachtSeil)getSchacht().getSeil()).refreshSeil();
			}
		}
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
	 * Gets the rolle1.
	 * 
	 * @return the rolle1
	 */
	public Umlenkrolle getRolle1() {
		return rolle1;
	}

	/**
	 * Sets the rolle1.
	 * 
	 * @param rolle1 the rolle1 to set
	 */
	public void setRolle1(Umlenkrolle rolle1) {
		this.rolle1 = (AufzugschachtUmlenkrolle)rolle1;
	}

	/**
	 * Gets the rolle2.
	 * 
	 * @return the rolle2
	 */
	public Umlenkrolle getRolle2() {
		return rolle2;
	}

	/**
	 * Sets the rolle2.
	 * 
	 * @param rolle2 the rolle2 to set
	 */
	public void setRolle2(Umlenkrolle rolle2) {
		this.rolle2 = (AufzugschachtUmlenkrolle)rolle2;
	}

	/**
	 * Checks if is activated.
	 * 
	 * @return true, if is activated
	 */
	public boolean isActivated() {
		return activated;
	}

	/**
	 * Sets the activated.
	 * 
	 * @param activated the activated
	 */
	public void setActivated(boolean activated) {
		this.activated = activated;
	}
	
	/**
	 * Gets the durchmesser of the first roll.
	 * This is the same as the one of the
	 * second roll
	 * 
	 * @return the durchmesser
	 */
	public int getDurchmesser() {
		return rolle1.getDurchmesser();
	}
	
	public int getKeilwinkel() {
		return keilwinkel;
	}

	public void setKeilwinkel(int keilwinkel) {
		this.keilwinkel = keilwinkel;
	}

	public int getUnterschnittwinkel() {
		return unterschnittwinkel;
	}

	public void setUnterschnittwinkel(int unterschnittwinkel) {
		this.unterschnittwinkel = unterschnittwinkel;
	}
	
	/**
	 * Gets the form.
	 * 
	 * @return the form
	 */
	public String getForm() {
		return form;
	}

	/**
	 * Sets the form.
	 * 
	 * @param form the form
	 */
	public void setForm(String form) {
		this.form = form;
	}
	
}
