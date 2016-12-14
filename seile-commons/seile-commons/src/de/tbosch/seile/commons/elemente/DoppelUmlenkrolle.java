package de.tbosch.seile.commons.elemente;

/**
 * The Class DoppelUmlenkrolle.
 */
public interface DoppelUmlenkrolle extends Element {
    
	/**
	 * Gets the hanging element.
	 * 
	 * @return the hangingElement
	 */
	public Element getHangingElement();

	/**
	 * Gets the rolle1.
	 * 
	 * @return the rolle1
	 */
	public Umlenkrolle getRolle1();

	/**
	 * Sets the rolle1.
	 * 
	 * @param rolle1 the rolle1 to set
	 */
	public void setRolle1(Umlenkrolle rolle1);

	/**
	 * Gets the rolle2.
	 * 
	 * @return the rolle2
	 */
	public Umlenkrolle getRolle2();

	/**
	 * Sets the rolle2.
	 * 
	 * @param rolle2 the rolle2 to set
	 */
	public void setRolle2(Umlenkrolle rolle2);

	/**
	 * Checks if is activated.
	 * 
	 * @return true, if is activated
	 */
	public boolean isActivated();

	/**
	 * Sets the activated.
	 * 
	 * @param activated the activated
	 */
	public void setActivated(boolean activated);
	
	/**
	 * Gets the durchmesser of the first roll.
	 * This is the same as the one of the
	 * second roll
	 * 
	 * @return the durchmesser
	 */
	public int getDurchmesser();
	
	public int getKeilwinkel();

	public void setKeilwinkel(int keilwinkel);

	public int getUnterschnittwinkel();

	public void setUnterschnittwinkel(int unterschnittwinkel);
	
	/**
	 * Gets the form.
	 * 
	 * @return the form
	 */
	public String getForm();

	/**
	 * Sets the form.
	 * 
	 * @param form the form
	 */
	public void setForm(String form);
}
