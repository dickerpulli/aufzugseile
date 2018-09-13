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
import de.tbosch.aufzugseile.utils.Constants;
import de.tbosch.aufzugseile.utils.Helper;

/**
 * The main class for the application 'Aufzugseile'
 * 
 * @author Thomas Bosch (tbosch@gmx.de)
 */
public class Aufzugseile {

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
			if (path == "") {
				logfilepath = "logging.log";
			} else {
				logfilepath = path + Constants.FILESEP + "logging.log";
			}
			fh = new FileHandler(logfilepath);
			fh.setLevel(Level.FINEST);
			fh.setFormatter(new SimpleFormatter());
			Constants.LOGGER.addHandler(ch);
			Constants.LOGGER.addHandler(fh);
			Constants.LOGGER.setUseParentHandlers(false);
			Constants.LOGGER.setLevel(Level.INFO);

			Constants.LOGGER.info("Start logging in Level:" + Constants.LOGGER.getLevel());

			showSplash();

			MainFrame mainFrame = new MainFrame(logfilepath);
			mainFrame.setVisible(true);
			mainFrame.createAufzugschacht();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static void showSplash() {
		final SplashScreen splash = SplashScreen.getSplashScreen();
		if (splash == null) {
			Constants.LOGGER.info("SplashScreen.getSplashScreen() returned null");
			return;
		}
		Graphics2D g = (Graphics2D) splash.createGraphics();
		if (g == null) {
			Constants.LOGGER.info("g is null");
			return;
		}
		for (int i = 0; i < 100; i++) {
			renderSplashFrame(g, i);
			splash.update();
			try {
				Thread.sleep(30);
			} catch (InterruptedException e) {
			}
		}
		splash.close();
	}

	static void renderSplashFrame(Graphics2D g, int frame) {
		// final String[] comps = {"foo", "bar", "baz"};
		g.setComposite(AlphaComposite.Clear);
		g.fillRect(0, 300, 400, 20);
		g.setPaintMode();
		// g.setColor(Color.WHITE);
		// g.drawString("Starte "+comps[(frame/5)%3]+"...", 320, 0);
		g.setColor(Color.BLACK);
		g.fillRect(0, 300, (400 / 100) * frame, 20);
	}
}
