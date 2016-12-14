/*
 * SchachtseileApp.java
 */

package de.tbosch.schachtseile;

import de.tbosch.commons.Context;
import de.tbosch.commons.gui.Splash;
import de.tbosch.schachtseile.gui.SchachtseileView;
import org.apache.log4j.Logger;
import org.jdesktop.application.Application;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.SingleFrameApplication;

/**
 * The main class of the application.
 */
public class SchachtseileApp extends SingleFrameApplication {
	
    /**
     * At startup create and show the main frame of the application.
     */
    @Override 
    protected void startup() {
		// Setze ThreadLocal Varialen
		Context.getContext().put(Context.PROGRAMM_NAME, "schachtseile");
		
		// Erzeuge View
		FrameView mainView = new SchachtseileView(this);
        show(mainView);
		((SchachtseileView)mainView).init();
    }

    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     */
    @Override 
    protected void configureWindow(java.awt.Window root) {
    }
    
    /**
     * A convenient static getter for the application instance.
     * @return the instance of SchachtseileApp
     */
    public static SchachtseileApp getApplication() {
        return Application.getInstance(SchachtseileApp.class);
    }

    /**
     * Main method launching the application.
     */
    public static void main(String[] args) {
		Logger.getLogger(SchachtseileApp.class.getName()).info("Programm wird gestartet.");
		Splash.showSplash();
        launch(SchachtseileApp.class, args);
    }
	
}
