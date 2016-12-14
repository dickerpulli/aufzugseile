package de.tbosch.aufzugseile.gui.utils;

import java.awt.AWTEvent;

// TODO: Auto-generated Javadoc
/**
 * The Class AufzugschachtEvent.
 */
public class AufzugschachtEvent extends AWTEvent {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3820395580207030222L;

	public static String SHOW_SEIL_OPTIONS_FRAME = "1";
	
	public static String SHOW_OPTIONS_FRAME = "2";

	public static String CHANGE_BUTTON = "3";

	public static String DESELECT_ALL_BUTTONS = "4";
	
	public static String NEW_TABBED_PANE = "5";
	
	public static String DELETE_TABBED_PANE = "6";
	
	public static String ACTIVATE_TABBED_PANE = "7";
	
	public static String REFRESH_TABBED_PANE = "8";
	
	public static String DELETE_ALL_TABBED_PANE = "9";
	
	public static String SET_AUFHAENGUNG = "10";
	
	public static String SET_ELEMENTS_OF_BERECHNUNG = "11";
	
	public static String SET_MASS = "12";
	
	public static String REFRESH_SEIL = "13";
	
	public static String SET_ZULADUNG = "14";
	
	public static String ENABLE_ALL_BUTTONS = "15";
	
	public static String SET_DURCHSCHNITT = "16";
	
	public static String ENABLE_ZICKZACK_BUTTON = "17";

	public static String SEIL_REMOVED = "18";

	public static String KABINE_REMOVED = "19";

	public static String GEWICHT_REMOVED = "20";
	
	public static String SET_PROFIL = "21";
	
	public static String SET_MASS_WEIGHT = "22";
	
	public static String SET_SCHACHTHOEHE_OF_BERECHNUNG = "23";
	
	public static String REFRESH_UMSCHL = "24";
	
	/** The command. */
	private String command;
	
	/** The parameter. */
	private Object[] parameters;

	/**
	 * The Constructor.
	 * 
	 * @param id the id
	 * @param source the source
	 * @param command the command
	 * @param parameters the parameters
	 */
	public AufzugschachtEvent(Object source, int id, String command, Object[] parameters) {
		super(source, id);
		this.command = command;
		this.parameters = parameters;
	}
	
	/**
	 * Gets the command.
	 * 
	 * @return the command
	 */
	public String getCommand() {
		return command;
	}
	
	/**
	 * Gets the parameter.
	 * 
	 * @return the parameter
	 */
	public Object[] getParameters() {
		return parameters;
	}

}
