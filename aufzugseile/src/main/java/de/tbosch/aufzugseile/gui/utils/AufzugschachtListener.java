package de.tbosch.aufzugseile.gui.utils;

import java.util.EventListener;

public interface AufzugschachtListener extends EventListener {
	
	public void eventOccurred(AufzugschachtEvent evt);
	
}
