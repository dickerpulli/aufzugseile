package de.tbosch.aufzugseile.utils;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.util.logging.Logger;

// TODO: Auto-generated Javadoc
/**
 * The constants class. Includes some usefull constants, like icon sizes and paths of icons and colors, or some system specific signs like the file separator
 * 
 * @author Thomas Bosch (tbosch@gmx.de)
 */
public class Constants {

	/** The physical parameter g. */
	public static double G_ERDBESCHL = 9.81;

	/** The german ä. */
	public static String AE_K = "\u00e4";

	/** The german Ä. */
	public static String AE_G = "\u00c4";

	/** The german ü. */
	public static String UE_K = "\u00fc";

	/** The german Ü. */
	public static String UE_G = "\u00dc";

	/** The german ö. */
	public static String OE_K = "\u00f6";

	/** The german Ö. */
	public static String OE_G = "\u00d6";

	/** The german ß. */
	public static String SZ = "\u00df";

	/** The actual path of the application. */
	public static String WORKDIR = System.getProperty("user.dir");

	/** The file separator. */
	public static String FILESEP = System.getProperty("file.separator");

	/** The CLASSPATH. */
	public static String CLASSPATH = System.getProperty("java.class.path");

	/** The PATHSEP. */
	public static String PATHSEP = System.getProperty("path.separator");

	/** The icon path. */
	public static String ICON_PATH = "icons" + FILESEP;

	/** The image path. */
	public static String IMAGE_PATH = "images" + FILESEP;

	public static String ITSERVICEBOSCH_IMAGE = IMAGE_PATH + "logoitservicebosch.gif";
	public static String INFO_IMAGE = IMAGE_PATH + "lebensdauer.gif";

	/** The size of an element. */
	public static int DEFAULT_ELEMENT_SIZE_W = 64;

	/** The DEFAUL t_ ELEMEN t_ SIZ e_ h. */
	public static int DEFAULT_ELEMENT_SIZE_H = 64;

	/** The size of an gewicht. */
	public static int GEWICHT_SIZE_W = 32;

	/** The GEWICH t_ SIZ e_ h. */
	public static int GEWICHT_SIZE_H = 64;

	/** The size of an kabine. */
	public static int KABINE_SIZE_W = 64;

	/** The KABIN e_ SIZ e_ h. */
	public static int KABINE_SIZE_H = 64;

	/** The size of an treibscheibe. */
	public static int TREIBSCHEIBE_SIZE_W = 64;

	/** The TREIBSCHEIB e_ SIZ e_ h. */
	public static int TREIBSCHEIBE_SIZE_H = 64;

	/** The size of a umlenkrolle. */
	public static int UMLENKROLLE_SIZE_W = 64;

	/** The UMLENKROLL e_ SIZ e_ h. */
	public static int UMLENKROLLE_SIZE_H = 64;

	/** The size of a umlenkrolle. */
	public static int DOPPELUMLENKROLLE_SIZE_W = 128;

	/** The UMLENKROLL e_ SIZ e_ h. */
	public static int DOPPELUMLENKROLLE_SIZE_H = 64;

	/** The size of a rope-holder. */
	public static int SEILAUFHAENGER_SIZE_W = 16;

	/** The SEILAUFHAENGE r_ SIZ e_ h. */
	public static int SEILAUFHAENGER_SIZE_H = 16;

	/** The button icon size. */
	public static int BUTTON_ICON_SIZE = 32;

	/** The UMLENKROLLE ID. */
	public static int UMLENKROLLE_ID = 0;

	/** The TREIBSCHEIBE ID. */
	public static int TREIBSCHEIBE_ID = 1;

	/** The GEWICHT ID. */
	public static int GEWICHT_ID = 2;

	/** The KABINE ID. */
	public static int KABINE_ID = 3;

	/** The SEILAUFHAENGEE ID. */
	public static int SEILAUFHAENGER_ID = 4;

	/** The icon path of treibscheibe. */
	public static String PROGRAM_ICON = ICON_PATH + "mainicon.png";

	/** The icon path of treibscheibe. */
	public static String TREIBSCHEIBE_ELEMENT_ICON_16 = ICON_PATH + "treibscheibe_16x16.png";

	/** The icon path of treibscheibe. */
	public static String TREIBSCHEIBE_ELEMENT_ICON_32 = ICON_PATH + "treibscheibe_32x32.png";

	/** The icon path of treibscheibe. */
	public static String TREIBSCHEIBE_ELEMENT_ICON_48 = ICON_PATH + "treibscheibe_48x48.png";

	/** The icon path of treibscheibe. */
	public static String TREIBSCHEIBE_ELEMENT_ICON_64 = ICON_PATH + "treibscheibe_64x64.png";

	/** The icon path of umlenkrolle. */
	public static String UMLENKROLLE_ELEMENT_ICON_16 = ICON_PATH + "umlenkrolle_16x16.png";

	/** The UMLENKROLL e_ ELEMEN t_ ICO n_32. */
	public static String UMLENKROLLE_ELEMENT_ICON_32 = ICON_PATH + "umlenkrolle_32x32.png";

	/** The UMLENKROLL e_ ELEMEN t_ ICO n_48. */
	public static String UMLENKROLLE_ELEMENT_ICON_48 = ICON_PATH + "umlenkrolle_48x48.png";

	/** The UMLENKROLL e_ ELEMEN t_ ICO n_64. */
	public static String UMLENKROLLE_ELEMENT_ICON_64 = ICON_PATH + "umlenkrolle_64x64.png";

	/** The icon path of umlenkrolle. */
	public static String DOPPELUMLENKROLLE_ELEMENT_ICON_16 = ICON_PATH + "doppelumlenkrolle_32x16.png";

	/** The icon path of umlenkrolle. */
	public static String DOPPELUMLENKROLLE_ELEMENT_ICON_32 = ICON_PATH + "doppelumlenkrolle_64x32.png";

	/** The icon path of umlenkrolle. */
	public static String DOPPELUMLENKROLLE_ELEMENT_ICON_48 = ICON_PATH + "doppelumlenkrolle_96x48.png";

	/** The icon path of umlenkrolle. */
	public static String DOPPELUMLENKROLLE_ELEMENT_ICON_64 = ICON_PATH + "doppelumlenkrolle_128x64.png";

	/** The icon path of kabine. */
	public static String KABINE_ELEMENT_ICON = ICON_PATH + "kabine_64x64.png";

	/** The icon path of gewicht. */
	public static String GEWICHT_ELEMENT_ICON = ICON_PATH + "gewicht_32x64.png";

	/** The icon path of rope-holder. */
	public static String ZICKZACK_ICON = ICON_PATH + "zickzack_16x16.png";

	/** The icon path of rope-holder. */
	public static String AUFHAENGER_ICON = ICON_PATH + "seilaufhaenger_16x16.png";

	/** The icon path of treibscheibe button icon. */
	public static String TREIBSCHEIBE_BUTTON_ICON = ICON_PATH + "treibscheibe_32x32.png";

	/** The icon path of umlenkrolle button icon. */
	public static String UMLENKROLLE_BUTTON_ICON = ICON_PATH + "umlenkrolle_32x32.png";

	/** The icon path of kabine button icon. */
	public static String KABINE_BUTTON_ICON = ICON_PATH + "kabine_32x32.png";

	/** The icon path of gewicht button icon. */
	public static String GEWICHT_BUTTON_ICON = ICON_PATH + "gewicht_32x32.png";

	/** The icon path of seil button icon. */
	public static String SEIL_BUTTON_ICON = ICON_PATH + "seil_32x32.png";

	/** The icon path of seil button icon. */
	public static String UNTERSEIL_BUTTON_ICON = ICON_PATH + "unterseil_32x32.png";

	/** The icon path of rope-holder. */
	public static String ZICKZACK_BUTTON_ICON = ICON_PATH + "zickzack_32x32.png";

	/** The name of the treibscheibe button. */
	public static String TREIBSCHEIBE_NAME = "treibscheibe";

	/** The name of the umlenkrolle button. */
	public static String UMLENKROLLE_NAME = "umlenkrolle";

	/** The name of the kabine button. */
	public static String KABINE_NAME = "kabine";

	/** The name of the gewicht button. */
	public static String GEWICHT_NAME = "gewicht";

	/** The name of the seil button. */
	public static String SEIL_NAME = "seil";

	/** The name of the unterseil button. */
	public static String UNTERSEIL_NAME = "unterseil";

	/** The name of the zickzack button. */
	public static String ZICKZACK_NAME = "zickzack";

	/** the seil-parameter file. */
	public static String SEIL_FILENAME = "seilparameter.dat";

	/** The background color of the elevator. */
	public static Color BACKGROUND_COLOR = Color.WHITE;

	/** the foreground color of the elevator. */
	public static Color FOREGROUND_COLOR = Color.BLACK;

	/** The color of abled pointer. */
	public static Color ABLE_COLOR = Color.GREEN;

	/** The color of unabled pointer. */
	public static Color UNABLE_COLOR = Color.RED;

	/** The color of pointer at the border. */
	public static Color SETBORDER_COLOR = Color.GREEN;

	/** The left mouse button constant. */
	public static int LEFT_MOUSE_BUTTON = MouseEvent.BUTTON1;

	/** The right mouse button constant. */
	public static int RIGHT_MOUSE_BUTTON = MouseEvent.BUTTON3;

	/** The icon path of popup-menu 'delete'. */
	public static String DELETE_ICON = ICON_PATH + "delete_16x16.png";

	/** The icon path of popup-menu 'modify'. */
	public static String MODIFY_ICON = ICON_PATH + "modify_16x16.png";

	/** The icon path of popup-menu 'activate'. */
	public static String ACTIVATE_ICON = ICON_PATH + "activate_16x16.png";

	/** The icon path of popup-menu 'deactivate'. */
	public static String DEACTIVATE_ICON = ICON_PATH + "deactivate_16x16.png";

	/** The icon path of popup-menu 'deactivate'. */
	public static String CHANGE_ICON = ICON_PATH + "change_16x16.png";

	/** The icon path of popup-menu 'deactivate'. */
	public static String LOCKED_ICON = ICON_PATH + "lock_16x16.png";

	/** The icon path of popup-menu 'deactivate'. */
	public static String UNLOCKED_ICON = ICON_PATH + "unlock_16x16.png";

	/** The icon path of mainmenu 'file open'. */
	public static String MENU_OPEN_ICON = ICON_PATH + "file.png";

	/** The icon path of mainmenu 'file open'. */
	public static String MENU_NEW_ICON = ICON_PATH + "filenew.png";

	/** The icon path of mainmenu 'file open'. */
	public static String MENU_SAVE_ICON = ICON_PATH + "filesave.png";

	/** The icon path of mainmenu 'file open'. */
	public static String MENU_SAVEAS_ICON = ICON_PATH + "filesaveas.png";

	/** The icon path of mainmenu 'file open'. */
	public static String MENU_PRINT_ICON = ICON_PATH + "fileprint.png";

	/** The icon path of mainmenu 'file open'. */
	public static String MENU_QUIT_ICON = ICON_PATH + "fileclose.png";

	/** The icon path of mainmenu 'file open'. */
	public static String MENU_INFO_ICON = ICON_PATH + "info.png";

	/** The icon path of mainmenu 'file open'. */
	public static String MENU_HELP_ICON = ICON_PATH + "help.png";

	/** The icon path of mainmenu 'file open'. */
	public static String QUESTION_ICON = ICON_PATH + "help.png";

	/** The icon path of mainmenu 'file open'. */
	public static String ERROR_ICON = ICON_PATH + "error.png";

	/** The icon path of mainmenu 'file open'. */
	public static String MENU_LOGVIEW_ICON = ICON_PATH + "logview.png";

	/** The icon path of mainmenu 'file open'. */
	public static String MENU_GLOBAL_ICON = ICON_PATH + "menu_global.png";

	/** The icon path of mainmenu 'file open'. */
	public static String MENU_ROPE_ICON = ICON_PATH + "menu_seil.png";

	/** The icon path of mainmenu 'file open'. */
	public static String MENU_ELEVATOR_ICON = ICON_PATH + "menu_schacht.png";

	/** The DIALOG_REMOVE. */
	public static String DIALOG_REMOVE = "remove";

	/** The DIALOG_RESET. */
	public static String DIALOG_RESET = "reset";

	/** The LOGGER. */
	public static Logger LOGGER = Logger.getLogger("Logging");

	public static String FORM_SITZ = "unterschnitt";

	public static String FORM_KEIL = "keil";

	public static String FORM_RUND = "rund";

	public static String TYP_SONSTIGER = "sonstiger";

	public static String TYP_FASSADE = "fassade";

	public static String TYP_KLEINLAST = "kleinlast";
}
