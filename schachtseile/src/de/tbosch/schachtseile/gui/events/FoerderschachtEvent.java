package de.tbosch.schachtseile.gui.events;

import java.awt.AWTEvent;

/**
 *
 * @author Thomas Bosch (tbosch@gmx.de)
 */
public class FoerderschachtEvent extends AWTEvent {
	
	public final static int PARAMETER_GEAENDERT_ID = 0;

	public FoerderschachtEvent(Object source, int id) {
		super(source, id);
	}
	
}
