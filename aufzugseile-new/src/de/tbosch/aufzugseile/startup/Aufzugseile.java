package de.tbosch.aufzugseile.startup;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.SplashScreen;
import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.SimpleFormatter;

import de.tbosch.aufzugseile.gui.MainFrame;
import de.tbosch.commons.Helper;
import de.tbosch.commons.SystemConstants;
import java.util.logging.Logger;

/**
 * The main class for the application 'Aufzugseile'
 * 
 * @author Thomas Bosch (tbosch@gmx.de)
 */
public class Aufzugseile {
    
    /** The Logger */
    private static final Logger logger = Logger.getLogger(Aufzugseile.class.getName());
	
	/**
	 * main method
	 * 
	 * @param args
	 *            Console parameters
	 */
	public static void main(String args[]) {
		// start logging
		ConsoleHandler ch = new ConsoleHandler();
		ch.setLevel(Level.FINEST);
		FileHandler fh = null;
		
		String path = Helper.getProgramPath();
		String logfilepath = "";
		
		try {
			if ("".equals(path)) {
				logfilepath = "logging.log";
			}
			else {
				logfilepath = path + SystemConstants.FILESEP + "logging.log";
			}
			fh = new FileHandler(logfilepath);
		}
		catch (SecurityException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		fh.setLevel(Level.FINEST);
		fh.setFormatter(new SimpleFormatter());
		logger.addHandler(ch);
		logger.addHandler(fh);
		logger.setUseParentHandlers(false);
		logger.setLevel(Level.INFO);
		
		logger.info("Start logging in Level:"+logger.getLevel());
		
		showSplash();
		
		MainFrame mainFrame = new MainFrame(logfilepath);
		mainFrame.setVisible(true);
		mainFrame.createAufzugschacht();
	}
	
	static void showSplash() {
		final SplashScreen splash = SplashScreen.getSplashScreen();
        if (splash == null) {
        	logger.info("SplashScreen.getSplashScreen() returned null");
            return;
        }
        Graphics2D g = (Graphics2D)splash.createGraphics();
        if (g == null) {
        	logger.info("g is null");
            return;
        }
        for(int i=0; i<100; i++) {
            renderSplashFrame(g, i);
            splash.update();
            try {
                Thread.sleep(30);
            }
            catch(InterruptedException e) {
            }
        }
        splash.close();
	}
	
	static void renderSplashFrame(Graphics2D g, int frame) {
        //final String[] comps = {"foo", "bar", "baz"};
        g.setComposite(AlphaComposite.Clear);
        g.fillRect(0,300,400,20);
        g.setPaintMode();
        //g.setColor(Color.WHITE);
        //g.drawString("Starte "+comps[(frame/5)%3]+"...", 320, 0);
        g.setColor(Color.BLACK);
        g.fillRect(0,300,(400/100)*frame,20);
    }
}
