package de.tbosch.aufzugseile.berechnung.lebensdauer;

import java.io.Serializable;

import de.tbosch.aufzugseile.gui.aufzug.Rolle;

public class CalcRolle implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8889990631810887861L;

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
