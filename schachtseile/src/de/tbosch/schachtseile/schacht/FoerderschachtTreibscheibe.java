/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.tbosch.schachtseile.schacht;

import de.tbosch.seile.commons.elemente.Treibscheibe;

/**
 *
 * @author bobo
 */
public class FoerderschachtTreibscheibe extends FoerderschachtRolle implements Treibscheibe {

	public FoerderschachtTreibscheibe(Foerderschacht schacht) {
		super(schacht);
		durchmesser = 4000;
		fn4 = 1;
		fs2 = 1;
		fn3 = 1;
		activated = true;
		gegenbiegung = false;
		keilwinkel = 75;
		unterschnittwinkel = 100;
		form = FORM_RUND;
		doppelteUmschlingung = false;
		zweiteUmschlingung = false;
		name = "TS";
		id = schacht.getIDPoolTreibscheibe().getID();
	}
	
}
