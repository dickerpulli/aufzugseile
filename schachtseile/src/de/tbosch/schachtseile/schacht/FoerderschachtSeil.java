/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.tbosch.schachtseile.schacht;

import de.tbosch.seile.commons.elemente.Element;
import de.tbosch.seile.commons.elemente.Schacht;
import de.tbosch.seile.commons.elemente.Seil;
import de.tbosch.seile.commons.elemente.SeilElement;
import java.util.Vector;

/**
 *
 * @author bobo
 */
public class FoerderschachtSeil implements Seil {
	
	private Vector<Element> elementVector = new Vector<Element>();
	private Vector<SeilElement> seilElementVector = new Vector<SeilElement>();
	private int count;
	private int biegelaenge;
	private double nenndurchmesser;
	private double massPerMSeil;
	private int length;
	
	public FoerderschachtSeil() {
		count = 4;
		biegelaenge = 6000;
		nenndurchmesser = 40;
		massPerMSeil = 0.35 * 40 / 100;
		length = 770;
	}

	public void addElement(Element element) {
		elementVector.add(element);
		FoerderschachtSeilElement seilElement = new FoerderschachtSeilElement((FoerderschachtElement)element);
		element.setSeilElement(seilElement);
		seilElementVector.add(seilElement);
		int index = seilElementVector.indexOf(seilElement);
		if (seilElementVector.size() > 1) {
			seilElementVector.get(index - 1).setNext(seilElement);
			seilElement.setPrev(seilElementVector.get(index - 1));
		}
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getLength() {
		return length;
	}

	public void setMassPerM(double mass) {
		massPerMSeil = mass;
	}

	public double getMassPerM() {
		return massPerMSeil;
	}

	public int getElementCount() {
		return elementVector.size();
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public boolean isElement(Element element) {
		return elementVector.contains((FoerderschachtElement)element);
	}

	public int getL() {
		return biegelaenge;
	}

	public void setBiegelaenge(int biegelaenge) {
		this.biegelaenge = biegelaenge;
	}

	public double getD() {
		return nenndurchmesser;
	}

	public void setNenndurchmesser(double nenndurchmesser) {
		this.nenndurchmesser = nenndurchmesser;
	}

	public Vector<Element> getElementVector() {
		return elementVector;
	}

	public Vector<SeilElement> getSeilElementVector() {
		return seilElementVector;
	}

	public boolean hasAlreadyTwoElements(Element element) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void replaceElement(Element element, Element toReplace) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void removeElement(Element element) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public Schacht getSchacht() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

}
