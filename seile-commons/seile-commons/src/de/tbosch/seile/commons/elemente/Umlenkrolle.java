package de.tbosch.seile.commons.elemente;

/**
 * The class for umlenkrolle.
 * 
 * @author Thomas Bosch (tbosch@gmx.de)
 */
public interface Umlenkrolle extends Rolle {

	/**
	 * Checks if is free.
	 * 
	 * @return true, if is free
	 */
	public boolean isFree();

	/**
	 * Gets the hanging element.
	 * 
	 * @return the hangingElement
	 */
	public Element getHangingElement();


	/**
	 * Checks if is ohne waelzlagerung.
	 * 
	 * @return true, if is ohne waelzlagerung
	 */
	public boolean isOhneWaelzlagerung();

	/**
	 * Sets the ohne waelzlagerung.
	 * 
	 * @param ohneWaelzlagerung the ohne waelzlagerung
	 */
	public void setOhneWaelzlagerung(boolean ohneWaelzlagerung);

}
