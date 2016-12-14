package de.tbosch.aufzugseile.utils;

import de.tbosch.aufzugseile.gui.MainFrame;
import de.tbosch.aufzugseile.gui.aufzug.Aufzugschacht;

// TODO: Auto-generated Javadoc
/**
 * The Class SchachtReadyThread.
 */
public class SchachtReadyThread extends Thread {
	
	/** The elevator. */
	private Aufzugschacht aufzugschacht;
	
	/** The main frame. */
	private MainFrame mainFrame;
	
	/** The width and height. */
	private int breite, hoehe;
	
	private Thread thread = null;
	
	/**
	 * The Constructor.
	 * 
	 * @param hoehe the height
	 * @param breite the width
	 * @param aufzugschacht the elevator
	 * @param mainFrame the main frame
	 */
	public SchachtReadyThread(int breite, int hoehe, Aufzugschacht aufzugschacht, MainFrame mainFrame) {
		this.breite = breite;
		this.hoehe = hoehe;
		this.aufzugschacht = aufzugschacht;
		this.mainFrame = mainFrame;
		this.setDaemon(true);
	}

	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		while (aufzugschacht == null && thread != null) {
			try {
				sleep(50);
			}
			catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		mainFrame.setSchachtReady(true);
		aufzugschacht.setMeasures(breite, hoehe);
		mainFrame.loadSeilparameter();
		mainFrame.setEnabled(true);
		mainFrame.setVisible(true);
		mainFrame.parameterChanged();
	}
	  
	/* (non-Javadoc)
	 * @see java.lang.Thread#start()
	 */
	public synchronized void start(){
	    if (thread == null) {
	    	thread = new Thread(this);
	    	thread.start();
	    }
	}
	
	/**
	 * Stop the thread.
	 * Own implementation
	 */
	public synchronized void stopThread(){
		if (thread != null) {
		    thread = null;
		}
	}
}
