/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.tbosch.schachtseile.model;

import de.tbosch.schachtseile.schacht.FoerderschachtKapsel;
import de.tbosch.schachtseile.schacht.FoerderschachtSeil;
import de.tbosch.schachtseile.schacht.FoerderschachtTreibscheibe;
import de.tbosch.schachtseile.schacht.FoerderschachtUmlenkrolle;
import java.util.Map;

/**
 *
 * @author Thomas Bosch (tbosch@gmx.de)
 */
public class FoerderschachtModel {
	
    private FoerderschachtSeil seil;
	private FoerderschachtSeil unterseil;
	private FoerderschachtTreibscheibe treibscheibe;
	private FoerderschachtUmlenkrolle umlenkrolle1;
	private FoerderschachtUmlenkrolle umlenkrolle2;
	private FoerderschachtKapsel kapsel1;
	private FoerderschachtKapsel kapsel2;
	private String typ;
	private Map<String, Double> doubleFaktoren;
	private Map<String, Integer> intFaktoren;
	
	public FoerderschachtModel(FoerderschachtSeil seil, 
		FoerderschachtSeil unterseil,
		FoerderschachtTreibscheibe treibscheibe, 
		FoerderschachtUmlenkrolle umlenkrolle1, 
		FoerderschachtUmlenkrolle umlenkrolle2, 
		FoerderschachtKapsel kapsel1,  
		FoerderschachtKapsel kapsel2, 
		String typ, 
		Map<String, Double> doubleFaktoren,
		Map<String, Integer> intFaktoren) {
		
		this.doubleFaktoren = doubleFaktoren;
		this.intFaktoren = intFaktoren;
		this.kapsel1 = kapsel1;
		this.kapsel2 = kapsel2;
		this.seil = seil;
		this.treibscheibe = treibscheibe;
		this.typ = typ;
		this.umlenkrolle1 = umlenkrolle1;
		this.umlenkrolle2 = umlenkrolle2;
		this.unterseil = unterseil;
	}
	
	public Map<String, Double> getDoubleFaktoren() {
		return doubleFaktoren;
	}
	
	public void setDoubleFaktoren(Map<String, Double> doubleFaktoren) {
		this.doubleFaktoren = doubleFaktoren;
	}

	public Map<String, Integer> getIntFaktoren() {
		return intFaktoren;
	}

	public void setIntFaktoren(Map<String, Integer> intFaktoren) {
		this.intFaktoren = intFaktoren;
	}

	public FoerderschachtKapsel getKapsel1() {
		return kapsel1;
	}

	public void setKapsel1(FoerderschachtKapsel kapsel1) {
		this.kapsel1 = kapsel1;
	}

	public FoerderschachtKapsel getKapsel2() {
		return kapsel2;
	}

	public void setKapsel2(FoerderschachtKapsel kapsel2) {
		this.kapsel2 = kapsel2;
	}

	public FoerderschachtSeil getSeil() {
		return seil;
	}

	public void setSeil(FoerderschachtSeil seil) {
		this.seil = seil;
	}

	public FoerderschachtTreibscheibe getTreibscheibe() {
		return treibscheibe;
	}

	public void setTreibscheibe(FoerderschachtTreibscheibe treibscheibe) {
		this.treibscheibe = treibscheibe;
	}

	public String getTyp() {
		return typ;
	}

	public void setTyp(String typ) {
		this.typ = typ;
	}

	public FoerderschachtUmlenkrolle getUmlenkrolle1() {
		return umlenkrolle1;
	}

	public void setUmlenkrolle1(FoerderschachtUmlenkrolle umlenkrolle1) {
		this.umlenkrolle1 = umlenkrolle1;
	}

	public FoerderschachtUmlenkrolle getUmlenkrolle2() {
		return umlenkrolle2;
	}

	public void setUmlenkrolle2(FoerderschachtUmlenkrolle umlenkrolle2) {
		this.umlenkrolle2 = umlenkrolle2;
	}

	public FoerderschachtSeil getUnterseil() {
		return unterseil;
	}

	public void setUnterseil(FoerderschachtSeil unterseil) {
		this.unterseil = unterseil;
	}
	
}
