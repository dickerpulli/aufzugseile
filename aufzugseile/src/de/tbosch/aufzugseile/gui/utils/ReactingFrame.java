package de.tbosch.aufzugseile.gui.utils;

import javax.swing.JFrame;

public interface ReactingFrame {
	
	/**
	 * Interface to react on something.
	 * 
	 * @param child the child
	 */
	public void okClicked(JFrame child);
	
	/**
	 * Cancel clicked -> react.
	 * 
	 * @param child the child
	 */
	public void noClicked(JFrame child);
	
	/**
	 * Maybe set the 'enabled'-variable of 'JFrame'.
	 * 
	 * @param enabled the enabled
	 */
	public void setEnabled(boolean enabled);

}
