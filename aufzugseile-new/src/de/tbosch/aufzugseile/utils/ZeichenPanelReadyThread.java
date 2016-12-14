package de.tbosch.aufzugseile.utils;

import javax.swing.JPanel;

import de.tbosch.aufzugseile.gui.MainFrame;
import de.tbosch.aufzugseile.gui.aufzug.Aufzugschacht;


// TODO: Auto-generated Javadoc
/**
 * The Class SchachtReadyThread.
 */
public class ZeichenPanelReadyThread extends Thread {
	
	/** The elevator. */
	private Aufzugschacht aufzugschacht;
	
	/** The main frame. */
	private MainFrame mainFrame;
	
	/** The zeichen panel. */
	private JPanel zeichenPanel;
	
	private Thread thread = null;
	
	/**
	 * The Constructor.
	 * 
	 * @param aufzugschacht the elevator
	 * @param mainFrame the main frame
	 * @param zeichenPanel the zeichen panel
	 */
	public ZeichenPanelReadyThread(JPanel zeichenPanel, Aufzugschacht aufzugschacht, MainFrame mainFrame) {
		this.zeichenPanel = zeichenPanel;
		this.aufzugschacht = aufzugschacht;
		this.mainFrame = mainFrame;
		this.setDaemon(true);
	}

	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
    @Override
	public void run() {
		while (zeichenPanel.getSize().width == 0 && thread != null) {
			try {
				sleep(50);
			}
			catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		aufzugschacht.setStartValues(zeichenPanel.getWidth(), zeichenPanel.getHeight());
		aufzugschacht.setSize(zeichenPanel.getSize());
		aufzugschacht.setLocation(0, 0);
		
		// default from global v < 1.5
		aufzugschacht.setGeschwindigkeit(1.5);
		
		zeichenPanel.add(aufzugschacht);
		zeichenPanel.repaint();
		mainFrame.createAufzugschachtListener();
		mainFrame.setZeichenPanelThreadAlive(false);
	}
	  
	/* (non-Javadoc)
	 * @see java.lang.Thread#start()
	 */
    @Override
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
