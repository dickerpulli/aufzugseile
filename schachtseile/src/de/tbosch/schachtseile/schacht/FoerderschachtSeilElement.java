package de.tbosch.schachtseile.schacht;

import de.tbosch.seile.commons.elemente.Element;
import de.tbosch.seile.commons.elemente.SeilElement;

/**
 *
 * @author Thomas Bosch (tbosch@gmx.de)
 */
public class FoerderschachtSeilElement implements SeilElement {
	
	private FoerderschachtElement element;
	private FoerderschachtSeilElement next;
	private FoerderschachtSeilElement prev;
	
	public FoerderschachtSeilElement(FoerderschachtElement element) {
		this.element = element;
	}

	public Element getElement() {
		return element;
	}

	public void setNext(SeilElement next) {
		this.next = (FoerderschachtSeilElement)next;
	}

	public void setPrev(SeilElement prev) {
		this.prev = (FoerderschachtSeilElement)prev;
	}

	public SeilElement getNext() {
		return next;
	}

	public SeilElement getPrev() {
		return prev;
	}

}
