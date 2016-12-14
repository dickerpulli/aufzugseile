/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.tbosch.schachtseile.schacht;

import de.tbosch.seile.commons.elemente.DoppelUmlenkrolle;
import de.tbosch.seile.commons.elemente.Rolle;
import de.tbosch.seile.commons.elemente.Umlenkrolle;

/**
 *
 * @author bobo
 */
public class FoerderschachtRolle extends FoerderschachtElement implements Rolle {

	protected int durchmesser;
	protected double fn4;
	protected double fs2;
	protected double fn3;
	protected boolean activated;
	protected boolean gegenbiegung;
	protected int keilwinkel;
	protected int unterschnittwinkel;
	protected String form;
	protected boolean doppelteUmschlingung;
	protected boolean zweiteUmschlingung;
	
	public FoerderschachtRolle(Foerderschacht schacht) {
		super(schacht);
	}

	public int getDurchmesser() {
		return durchmesser;
	}

	public void setDurchmesser(int durchmesser) {
		this.durchmesser = durchmesser;
	}

	public double getFn4() {
		return fn4;
	}

	public void setSchraegzug(double schraegzug) {
		this.fn4 = schraegzug;
	}

	public double getFs2() {
		return fs2;
	}

	public void setSeiltriebwirkungsgrad(double seiltriebwirkungsgrad) {
		this.fs2 = seiltriebwirkungsgrad;
	}

	public double getFn3() {
		return fn3;
	}

	public void setSeilrille(double seilrille) {
		this.fn3 = seilrille;
	}

	public void setID(int id) {
		this.id = id;
	}

	public boolean isActivated() {
		return activated;
	}

	public void setActivated(boolean activated) {
		this.activated = activated;
	}

	public boolean isGegenbiegung() {
		return gegenbiegung;
	}

	public void setGegenbiegung(boolean gegenbiegung) {
		this.gegenbiegung = gegenbiegung;
	}

	public int getKeilwinkel() {
		return keilwinkel;
	}

	public void setKeilwinkel(int keilwinkel) {
		this.keilwinkel = keilwinkel;
	}

	public int getUnterschnittwinkel() {
		return unterschnittwinkel;
	}

	public void setUnterschnittwinkel(int unterschnittwinkel) {
		this.unterschnittwinkel = unterschnittwinkel;
	}

	public String getForm() {
		return form;
	}

	public void setForm(String form) {
		this.form = form;
	}

	public boolean isDoppelteUmschlingung() {
		return doppelteUmschlingung;
	}

	public void setDoppelteUmschlingung(boolean doppelteUmschlingung) {
		this.doppelteUmschlingung = doppelteUmschlingung;
	}

	public boolean isZweiteUmschlingung() {
		return zweiteUmschlingung;
	}

	public void setZweiteUmschlingung(boolean zweiteUmschlingung) {
		this.zweiteUmschlingung = zweiteUmschlingung;
	}

	public int getRadius() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public DoppelUmlenkrolle getParentDoubleRoll() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public Rolle getRolle1teUmschlingung() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void setRolle1teUmschlingung(Rolle rolle1teUmschlingung) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public Rolle getRolle2teUmschlingung() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public boolean isPartOfDoppelUmlenkrolle() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public Umlenkrolle getBrother() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void setBrother(Umlenkrolle brother) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

}
