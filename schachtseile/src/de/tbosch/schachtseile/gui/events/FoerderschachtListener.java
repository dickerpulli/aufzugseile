package de.tbosch.schachtseile.gui.events;

import java.util.EventListener;

/**
 *
 * @author Thomas Bosch (tbosch@gmx.de)
 */
public interface FoerderschachtListener extends EventListener {

	/**
	 * Wird beim Aufruf des Events ausgeführt
	 * @param event das übergebene Event
	 */
	public void eventOccurred(FoerderschachtEvent event);
	
}
