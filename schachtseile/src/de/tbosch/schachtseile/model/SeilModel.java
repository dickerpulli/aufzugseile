/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.tbosch.schachtseile.model;

/**
 *
 * @author Thomas Bosch (tbosch@gmx.de)
 */
public class SeilModel {
	
	private int masse;
	private double durchmesser;
	private int anzahl;
	
	public SeilModel() {
	}

	public int getAnzahl() {
		return anzahl;
	}

	public void setAnzahl(int anzahl) {
		this.anzahl = anzahl;
	}

	public double getDurchmesser() {
		return durchmesser;
	}

	public void setDurchmesser(double durchmesser) {
		this.durchmesser = durchmesser;
	}

	public int getMasse() {
		return masse;
	}

	public void setMasse(int masse) {
		this.masse = masse;
	}
	
}
