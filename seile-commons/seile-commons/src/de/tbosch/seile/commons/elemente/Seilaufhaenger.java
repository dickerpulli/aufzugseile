package de.tbosch.seile.commons.elemente;

/**
 * The class of rope holder.
 * The rope is clipped at a rope holder at the beginning and the end
 * 
 * @author Thomas Bosch (tbosch@gmx.de)
 */
public interface Seilaufhaenger extends Element {

	/** Constant for 'north' orientation. */
	public static final int NORD = 0;

	/** Constant for 'east' orientation. */
	public static final int OST = 1;

	/** Constant for 'south' orientation. */
	public static final int SUED = 2;

	/** Constant for 'west' orientation. */
	public static final int WEST = 3;

	/**
	 * Checks if is free.
	 * 
	 * @return true, if is free
	 */
	public boolean isFree();

	/**
	 * Gets the hanging element.
	 * 
	 * @return the element hanging at the rope holder
	 */
	public Element getHangingElement();

}
