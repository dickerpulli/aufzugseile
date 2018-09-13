package de.tbosch.aufzugseile.gui.utils.printing;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import de.tbosch.aufzugseile.utils.Constants;

public class Messages {

	// default language: german
	private static String BUNDLE_NAME = "de.tbosch.aufzugseile.gui.utils.printing.german"; //$NON-NLS-1$

	private Messages() {
	}

	public static String getString(String key) {
		ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);
		try {
			return RESOURCE_BUNDLE.getString(key);
		}
		catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
	
	public static void setLanguage(String lang) {
		if (lang.equals("german")) {
			Constants.LOGGER.info("Language changed to german");
			BUNDLE_NAME = "de.tbosch.aufzugseile.gui.utils.printing.german";
		}
		else if (lang.equals("english")) {
			Constants.LOGGER.info("Language changed to english");
			BUNDLE_NAME = "de.tbosch.aufzugseile.gui.utils.printing.english";
		}
		else {
			Constants.LOGGER.info("Language not found, use default german");
			BUNDLE_NAME = "de.tbosch.aufzugseile.gui.utils.printing.german";
		}
	}
}
