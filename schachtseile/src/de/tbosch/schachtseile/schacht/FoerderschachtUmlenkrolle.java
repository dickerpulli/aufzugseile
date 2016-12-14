/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.tbosch.schachtseile.schacht;

import de.tbosch.seile.commons.elemente.Element;
import de.tbosch.seile.commons.elemente.Umlenkrolle;

/**
 *
 * @author bobo
 */
public class FoerderschachtUmlenkrolle extends FoerderschachtRolle implements Umlenkrolle {
	
	public FoerderschachtUmlenkrolle(Foerderschacht schacht) {
		super(schacht);
		durchmesser = 3500;
		fs2 = 1;
		fn3 = 1;
		fn4 = 1;
		activated = true;
		form = FORM_RUND;
		gegenbiegung = false;
		keilwinkel = 75;
		unterschnittwinkel = 100;
		doppelteUmschlingung = false;
		zweiteUmschlingung = false;
		name = "Rolle";
		id = schacht.getIDPoolUmlenkrolle().getID();
	}

	public boolean isFree() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public Element getHangingElement() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public boolean isOhneWaelzlagerung() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void setOhneWaelzlagerung(boolean ohneWaelzlagerung) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

}
