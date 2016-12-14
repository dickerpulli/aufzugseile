package de.tbosch.seile.berechnung.lebensdauer;

import de.tbosch.seile.commons.elemente.Rolle;
import java.io.Serializable;


public class CalcRolle implements Serializable {

	private Rolle rolle;
	
	/** The lebensdauer depends on 'as'. NA and NA10 are 2 values.  */
	private long lebensdauer[][];
	
	public CalcRolle(Rolle rolle, int lengthAs, int lengthBs) {
		this.rolle = rolle;
		this.lebensdauer = new long[lengthAs][lengthBs];
	}

	public long[][] getLebensdauer() {
		return lebensdauer;
	}

	public String getName() {
		return rolle.getName() + rolle.getID();
	}

	public Rolle getRolle() {
		return rolle;
	}

	public void setLebensdauer(long lebensdauer, int indexAs, int indexBs) {
		this.lebensdauer[indexAs][indexBs]  = lebensdauer;
	}
}
