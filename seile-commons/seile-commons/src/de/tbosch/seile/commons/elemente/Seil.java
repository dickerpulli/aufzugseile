package de.tbosch.seile.commons.elemente;

import java.io.Serializable;
import java.util.Vector;

/**
 * The class of the rope.
 * 
 * @author Thomas Bosch (tbosch@gmx.de)
 */
public interface Seil extends Cloneable, Serializable {
	
	/**
	 * Length
	 * @param length
	 */
	public void setLength(int length);
	
	/**
	 * Length in m
	 * @return
	 */
	public int getLength();
	
	/**
	 * Set Masse in kg / m
	 * @param mass
	 */
	public void setMassPerM(double mass);
	
	/**
	 * Masse in kg / m
	 * @return
	 */
	public double getMassPerM();

	/**
	 * Adds an element to the rope.
	 * 
	 * @param element The element to add
	 */
	public void addElement(Element element);

	/**
	 * Replace an element by another one.
	 * 
	 * @param element the element
	 * @param toReplace the element to replace
	 */
	public void replaceElement(Element element, Element toReplace);

	/**
	 * Removes an element from the rope.
	 * 
	 * @param element The element to remove
	 */
	public void removeElement(Element element);

	/**
	 * Return the count of elements on the rope.
	 * 
	 * @return Count of elements
	 */
	public int getElementCount();

	/**
	 * Gets the schacht.
	 * 
	 * @return the schacht
	 */
	public Schacht getSchacht();
	
	/**
	 * Gets the count of ropes in one big rope.
	 * 
	 * @return the count
	 */
	public int getCount();

	/**
	 * Sets the count of ropes in one big rope.
	 * 
	 * @param count the count to set
	 */
	public void setCount(int count);
	
	/**
	 * Checks if is element.
	 * 
	 * @param element the element
	 * 
	 * @return true, if is element
	 */
	public boolean isElement(Element element);

	/**
	 * Gets the biegelaenge.
	 * 
	 * @return the biegelaenge
	 */
	public int getL();

	/**
	 * Sets the biegelaenge.
	 * 
	 * @param biegelaenge the biegelaenge to set
	 */
	public void setBiegelaenge(int biegelaenge);

	/**
	 * Gets the nenndurchmesser in millimeters.
	 * 
	 * @return the nenndurchmesser
	 */
	public double getD();

	/**
	 * Sets the nenndurchmesser in millimeters.
	 * 
	 * @param nenndurchmesser the nenndurchmesser to set
	 */
	public void setNenndurchmesser(double nenndurchmesser);

	/**
	 * @return the elementVector
	 */
	public Vector<Element> getElementVector();

	/**
	 * @return the seilElementVector
	 */
	public Vector<SeilElement> getSeilElementVector();
	
	/**
	 * Checks if the elementVector already contains two elements.
	 * 
	 * @param element the element
	 * 
	 * @return true, if has already two elements
	 */
	public boolean hasAlreadyTwoElements(Element element);
}
