/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.tbosch.schachtseile.schacht;

import de.tbosch.seile.commons.elemente.Kapsel;

/**
 *
 * @author bobo
 */
public class FoerderschachtKapsel extends FoerderschachtElement implements Kapsel {
	
	private int mass;
	private int zuladung;
	private double durchschnitt;
	private double profil;
	
	public FoerderschachtKapsel(Foerderschacht schacht) {
		super(schacht);
		mass = 39780;
		zuladung = 0;
		durchschnitt = 1;
		profil = 1;
	}

	public int getMass() {
		return mass;
	}
	
	public void setMass(int mass) {
		this.mass = mass;
	}

	public int getZuladung() {
		return zuladung;
	}
	
	public void setZuladung(int zuladung) {
		this.zuladung = zuladung;
	}

	public double getDurchschnitt() {
		return durchschnitt;
	}

	public double getProfil() {
		return profil;
	}

}
