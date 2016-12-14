package de.tbosch.seile.commons.elemente;

import de.tbosch.commons.IDPool;

/**
 *
 * @author Thomas Bosch (tbosch@gmx.de)
 */
public interface Schacht extends Cloneable {
    
	/** The ID pool for the treibscheibe. */
	public static final IDPool iDPoolTreibscheibe = new IDPool(16);
    
	/** The ID pool for the umlenkrolle. */
	public static final IDPool iDPoolUmlenkrolle = new IDPool(16);
    
    public IDPool getIDPoolTreibscheibe();
    
    public IDPool getIDPoolUmlenkrolle();

    public Treibscheibe getTreibscheibe();

    public Seil getSeil();
    
}
