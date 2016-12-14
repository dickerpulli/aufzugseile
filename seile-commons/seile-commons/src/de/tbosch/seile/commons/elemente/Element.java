package de.tbosch.seile.commons.elemente;

import java.io.Serializable;

/**
 * The element class. All elements of the elevator are implementet over this class. Moving, dragging are also implemented as popupmenus and scaling.
 * 
 * @author Thomas Bosch (tbosch@gmx.de)
 */
public interface Element extends Cloneable, Serializable {
    
	/**
	 * Gets the default ID.
	 * Could be overriden by inheriting class
	 * 
	 * @return the ID
	 */
	public int getID();

	/**
	 * Gets the seil element.
	 * 
	 * @param first get the first one?
	 * 
	 * @return the first seilElement if first is true. otherwise the last element
	 */
	public SeilElement getSeilElement(boolean first);

	/**
	 * Sets the seil element.
	 * 
	 * @param seilElement the seilElement to set
	 */
	public void setSeilElement(SeilElement seilElement);
	
	/**
	 * Reset seil elements.
	 */
	public void resetSeilElements();
	
	/**
	 * Gets the schacht.
	 * 
	 * @return the schacht
	 */
	public Schacht getSchacht();
    
    public String getName();

}
