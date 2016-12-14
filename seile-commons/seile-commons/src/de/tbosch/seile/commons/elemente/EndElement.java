package de.tbosch.seile.commons.elemente;

/**
 * The meta element for an end element wich is at the end or the beginning of the rope.
 * 
 * @author Thomas Bosch (tbosch@gmx.de)
 */
public interface EndElement extends Element {

	/**
	 * Sets the rope holder.
	 * 
	 * @param seilaufhaenger The rope holder
	 */
	public void setSeilaufhaenger(Seilaufhaenger seilaufhaenger);

	/**
	 * Gets the rope holder.
	 * 
	 * @return The rope holder
	 */
	public Seilaufhaenger getSeilaufhaenger();

	/**
	 * Sets the umlenkrolle.
	 * 
	 * @param umlenkrolle The umlenkrolle
	 */
	public void setUmlenkrolle(Umlenkrolle umlenkrolle);

	/**
	 * Gets the umlenkrolle.
	 * 
	 * @return The umlenkrolle
	 */
	public Umlenkrolle getUmlenkrolle();

	/**
	 * Gets the double umlenkrolle.
	 * 
	 * @return the double umlenkrolle
	 */
	public DoppelUmlenkrolle getDoppelUmlenkrolle();

	/**
	 * Sets the double umlenkrolle.
	 * 
	 * @param doppelUmlenkrolle the double umlenkrolle
	 */
	public void setDoppelUmlenkrolle(DoppelUmlenkrolle doppelUmlenkrolle);
    
}
