package de.tbosch.seile.commons.elemente;

import java.io.Serializable;

/**
 * The Class SeilElement. Every element on the rope is an SeilElement. This class calculates
 * the incoming and outgoing point of the rope
 * 
 * @author Thomas Bosch (tbosch@gmx.de)
 */
public interface SeilElement extends Serializable {

	/**
	 * Gets the element.
	 * 
	 * @return the element
	 */
	public Element getElement();

	/**
	 * Sets the nextLoc.
	 * 
	 * @param nextLoc the nextLoc to set
	 */
	public void setNext(SeilElement next);

	/**
	 * Sets the prevLoc.
	 * 
	 * @param prevLoc the prevLoc to set
	 */
	public void setPrev(SeilElement prev);
	

	/**
	 * Gets the nextLoc.
	 * 
	 * @return the nextLoc
	 */
	public SeilElement getNext();

	/**
	 * Gets the prevLoc.
	 * 
	 * @return the prevLoc
	 */
	public SeilElement getPrev();
	
}
