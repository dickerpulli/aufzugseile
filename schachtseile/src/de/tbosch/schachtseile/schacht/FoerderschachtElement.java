package de.tbosch.schachtseile.schacht;

import de.tbosch.seile.commons.elemente.Element;
import de.tbosch.seile.commons.elemente.Schacht;
import de.tbosch.seile.commons.elemente.SeilElement;

/**
 *
 * @author Thomas Bosch (tbosch@gmx.de)
 */
public class FoerderschachtElement implements Element {
	
	protected int id;
	protected FoerderschachtSeilElement seilElement;
	transient protected Foerderschacht schacht;
	protected String name;
	
	public FoerderschachtElement(Foerderschacht schacht) {
		this.schacht = schacht;
	}

	public int getID() {
		return id;
	}

	public SeilElement getSeilElement(boolean first) {
		return seilElement;
	}

	public void setSeilElement(SeilElement seilElement) {
		this.seilElement = (FoerderschachtSeilElement)seilElement;
	}

	public Schacht getSchacht() {
		return schacht;
	}

	public String getName() {
		return name;
	}

	public void resetSeilElements() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

}
