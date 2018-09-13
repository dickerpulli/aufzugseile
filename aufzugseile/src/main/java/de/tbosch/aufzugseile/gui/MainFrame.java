package de.tbosch.aufzugseile.gui;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Vector;
import java.util.logging.Level;

import javax.help.HelpSet;
import javax.help.HelpSetException;
import javax.help.JHelp;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import de.tbosch.aufzugseile.berechnung.lebensdauer.Berechnung;
import de.tbosch.aufzugseile.berechnung.lebensdauer.CalcRolle;
import de.tbosch.aufzugseile.gui.aufzug.Aufzugschacht;
import de.tbosch.aufzugseile.gui.aufzug.DoppelUmlenkrolle;
import de.tbosch.aufzugseile.gui.aufzug.Element;
import de.tbosch.aufzugseile.gui.aufzug.Gewicht;
import de.tbosch.aufzugseile.gui.aufzug.Kabine;
import de.tbosch.aufzugseile.gui.aufzug.Rolle;
import de.tbosch.aufzugseile.gui.aufzug.Treibscheibe;
import de.tbosch.aufzugseile.gui.aufzug.Umlenkrolle;
import de.tbosch.aufzugseile.gui.aufzug.seil.Seil;
import de.tbosch.aufzugseile.gui.aufzug.seil.Unterseil;
import de.tbosch.aufzugseile.gui.utils.AufzugschachtEvent;
import de.tbosch.aufzugseile.gui.utils.AufzugschachtListener;
import de.tbosch.aufzugseile.gui.utils.DialogFrame;
import de.tbosch.aufzugseile.gui.utils.ErrorFrame;
import de.tbosch.aufzugseile.gui.utils.GenericFileFilter;
import de.tbosch.aufzugseile.gui.utils.ReactingFrame;
import de.tbosch.aufzugseile.gui.utils.SmartFileChooser;
import de.tbosch.aufzugseile.gui.utils.printing.PrintDialog;
import de.tbosch.aufzugseile.gui.utils.printing.PrintFrameFeyrer;
import de.tbosch.aufzugseile.gui.utils.printing.PrintFrameMain;
import de.tbosch.aufzugseile.utils.Constants;
import de.tbosch.aufzugseile.utils.FileHelper;
import de.tbosch.aufzugseile.utils.FileParseException;
import de.tbosch.aufzugseile.utils.FileSetWritableException;
import de.tbosch.aufzugseile.utils.Helper;
import de.tbosch.aufzugseile.utils.LongGrouping;
import de.tbosch.aufzugseile.utils.Persistence;
import de.tbosch.aufzugseile.utils.SchachtReadyThread;
import de.tbosch.aufzugseile.utils.ZeichenPanelReadyThread;

/**
 * Main frame displaying the main window.
 * 
 * @author Thomas Bosch (tbosch@gmx.de)
 */
public class MainFrame extends javax.swing.JFrame implements ReactingFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3468515089049204090L;

	/** The zeichen panel thread. */
	private ZeichenPanelReadyThread zeichenPanelThread;

	/** The schacht ready thread. */
	private SchachtReadyThread schachtReadyThread;

	/** The thread for waiting of the zeichen panel. */
	private boolean zeichenPanelThreadAlive = true;

	/** The elevator. */
	private Aufzugschacht aufzugschacht = new Aufzugschacht();

	/** The vector of the element buttons. */
	private Vector<AbstractButton> buttonVector;

	/** The global parameters frame. */
	private GlobalParametersFrame globalParametersFrame;

	/** The seil parameter frame. */
	private SeilParameterFrame seilParameterFrame;

	/** The calculation. */
	private Berechnung berechnung = new Berechnung();

	/** The decimal formats with and without a point (double, int). */
	private DecimalFormat df_nopoint, df_point;

	/** The parameter table for rope parameters. */
	private TableModel parameterTableModel;

	/** The seilparameter file. */
	private File seilparameterFile;

	/** The aufzugschacht file for saving. */
	private File aufzugschachtFile;

	/** The ergebnis table. */
	private JTable ergebnisTable;

	/** The einzel ergebnis table. */
	private JTable einzelErgebnisTable;

	/** The frame for setting special options for the elements. */
	private JFrame optionsFrame;

	/** The berechnen clicked. */
	private boolean berechnenClicked = false;

	/** If the elevator is ready. */
	private boolean schachtReady = false;

	/** The index. */
	private int index;

	/** The logfilepath. */
	private String logfilepath;

	/** The load file. */
	private boolean loadFile;

	/** The global seilmasse. */
	private double globalSeilmasse = 0;

	/** The treibfaehigkeits frame. */
	private TreibfaehigkeitFrame treibFrame;

	/**
	 * Creates new form MainFrame.
	 */
	public MainFrame(String logfilepath) {
		df_nopoint = new DecimalFormat();
		df_nopoint.setMaximumFractionDigits(0);
		df_nopoint.setGroupingUsed(false);
		df_point = new DecimalFormat();
		df_point.setMaximumFractionDigits(2);
		df_point.setMinimumFractionDigits(0);
		df_point.setGroupingUsed(false);
		initComponents();
		buttonVector = new Vector<AbstractButton>();
		registerButtons();
		loadMenuIconImages();
		java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		int height = 1024;
		int width = 1090;
		if (screenSize.width <= width || screenSize.height <= height) {
			setExtendedState(MAXIMIZED_BOTH);
		} else {
			setBounds((screenSize.width - width) / 2, (screenSize.height - height) / 2, width, height);
		}
		setName("Aufzugseile"); //$NON-NLS-1$
		setTitle(getName() + " - unbekannt.alb"); //$NON-NLS-1$
		setVisible(true);

		this.logfilepath = logfilepath;
		ImageIcon imageIcon = new ImageIcon(Helper.getFileURL(Constants.PROGRAM_ICON));
		setIconImage(imageIcon.getImage());

		umschlWinkelFormattedTextField.setEditable(false);
	}

	/**
	 * Creates the aufzugschacht listeners and the button action listeners.
	 */
	public void createAufzugschachtListener() {
		// catch events comming from the aufzugschacht
		aufzugschacht.addAufzugschachtListener(new AufzugschachtListener() {
			public void eventOccurred(AufzugschachtEvent evt) {
				if (evt.getCommand().equals(AufzugschachtEvent.SHOW_UNTERSEIL_OPTIONS_FRAME)) {
					Unterseil unterseil = (Unterseil) evt.getParameters()[0];
					showUnterseilOptionsFrame(unterseil);
				}
				if (evt.getCommand().equals(AufzugschachtEvent.SHOW_SEIL_OPTIONS_FRAME)) {
					Seil seil = (Seil) evt.getParameters()[0];
					showSeilOptionsFrame(seil);
				}
				if (evt.getCommand().equals(AufzugschachtEvent.SHOW_OPTIONS_FRAME)) {
					Element element = (Element) evt.getParameters()[0];
					boolean rolle2 = (Boolean) evt.getParameters()[1];
					showOptionsFrame(element, rolle2);
				}
				if (evt.getCommand().equals(AufzugschachtEvent.CHANGE_BUTTON)) {
					String name = (String) evt.getParameters()[0];
					boolean value = (Boolean) evt.getParameters()[1];
					changeButton(name, value);
				}
				if (evt.getCommand().equals(AufzugschachtEvent.DESELECT_ALL_BUTTONS)) {
					deselectAllButtons();
				}
				if (evt.getCommand().equals(AufzugschachtEvent.DELETE_ALL_TABBED_PANE)) {
					deleteAllTabbedPane();
				}
				if (evt.getCommand().equals(AufzugschachtEvent.NEW_TABBED_PANE)) {
					Rolle rolle = (Rolle) evt.getParameters()[0];
					newTabbedPane(rolle);
				}
				if (evt.getCommand().equals(AufzugschachtEvent.DELETE_TABBED_PANE)) {
					Rolle rolle = (Rolle) evt.getParameters()[0];
					deleteTabbedPane(rolle);
				}
				if (evt.getCommand().equals(AufzugschachtEvent.ACTIVATE_TABBED_PANE)) {
					Rolle rolle = (Rolle) evt.getParameters()[0];
					activateTabbedPane(rolle);
				}
				if (evt.getCommand().equals(AufzugschachtEvent.REFRESH_TABBED_PANE)) {
					Rolle rolle = (Rolle) evt.getParameters()[0];
					refreshTabbedPane(rolle);
				}
				if (evt.getCommand().equals(AufzugschachtEvent.SET_AUFHAENGUNG)) {
					int aufhaengung = (Integer) evt.getParameters()[0];
					setAufhaengung(aufhaengung);
				}
				if (evt.getCommand().equals(AufzugschachtEvent.SET_ELEMENTS_OF_BERECHNUNG)) {
					Seil seil = (Seil) evt.getParameters()[0];
					Kabine kabine = (Kabine) evt.getParameters()[1];
					Gewicht gewicht = (Gewicht) evt.getParameters()[2];
					Unterseil unterseil = (Unterseil) evt.getParameters()[3];
					berechnung.setElements(seil, kabine, gewicht, unterseil);
				}
				if (evt.getCommand().equals(AufzugschachtEvent.SET_SCHACHTHOEHE_OF_BERECHNUNG)) {
					int schachthoehe = (Integer) evt.getParameters()[0];
					berechnung.setSchachthoehe(schachthoehe);
				}
				if (evt.getCommand().equals(AufzugschachtEvent.SET_MASS)) {
					int mass = (Integer) evt.getParameters()[0];
					setMassKabine(mass);
				}
				if (evt.getCommand().equals(AufzugschachtEvent.SET_MASS_UNTERSEIL_GEWICHT)) {
					int mass = (Integer) evt.getParameters()[0];
					setMassUnterseilGewicht(mass);
				}
				if (evt.getCommand().equals(AufzugschachtEvent.SET_MASS_WEIGHT)) {
					int mass = (Integer) evt.getParameters()[0];
					int part = (Integer) evt.getParameters()[1];
					setMassGewicht(mass, part);
				}
				if (evt.getCommand().equals(AufzugschachtEvent.REFRESH_SEIL)) {
					Seil seil = (Seil) evt.getParameters()[0];
					refreshSeil(seil);
				}
				if (evt.getCommand().equals(AufzugschachtEvent.SET_ZULADUNG)) {
					int zuladung = (Integer) evt.getParameters()[0];
					setZuladung(zuladung);
				}
				if (evt.getCommand().equals(AufzugschachtEvent.ENABLE_ALL_BUTTONS)) {
					enableAllButtons();
				}
				if (evt.getCommand().equals(AufzugschachtEvent.SET_DURCHSCHNITT)) {
					double durchschnitt = (Double) evt.getParameters()[0];
					setDurchschnitt(durchschnitt);
				}
				if (evt.getCommand().equals(AufzugschachtEvent.SET_PROFIL)) {
					double profil = (Double) evt.getParameters()[0];
					setProfil(profil);
				}
				if (evt.getCommand().equals(AufzugschachtEvent.REFRESH_UMSCHL)) {
					double angle = (Double) evt.getParameters()[0];
					umschlWinkelFormattedTextField.setText(df_point.format(angle));
					if (treibFrame != null && treibFrame.isVisible()) {
						treibFrame.setNewTreibscheibe(aufzugschacht.getTreibscheibe());
					}
				}
				if (evt.getCommand().equals(AufzugschachtEvent.SEIL_REMOVED)) {
					if (ergebnisTable != null) {
						DefaultTableModel ergebnisTableModel = (DefaultTableModel) ergebnisTable.getModel();
						for (int i = ergebnisTableModel.getRowCount() - 1; i >= 0; i--) {
							// reset ergebnis table
							ergebnisTableModel.removeRow(i);
						}
					}
					einzelErgebnisTableScrollPane.setViewportView(null);
					berechnenClicked = false;
					berechnung.getCalcRolls().clear();
					berechnung.setElements(null, null, null, null);
					berechnung.setSchachthoehe(aufzugschacht.getHoehe());
					seilanzahlFormattedTextField.setText(""); //$NON-NLS-1$
					aufhaengungFormattedTextField.setText(""); //$NON-NLS-1$
					durchmesserFormattedTextField.setText(""); //$NON-NLS-1$
					biegelaengeFormattedTextField.setText(""); //$NON-NLS-1$
					umschlWinkelFormattedTextField.setText(""); //$NON-NLS-1$
					//fn1FormattedTextField.setText(""); //$NON-NLS-1$
					// berechnung.setFn1(0);
				}
				if (evt.getCommand().equals(AufzugschachtEvent.KABINE_REMOVED)) {
					kabinenmasseFormattedTextField.setText(""); //$NON-NLS-1$
					zuladungmasseFormattedTextField.setText(""); //$NON-NLS-1$
					durchschnittFormattedTextField.setText(""); //$NON-NLS-1$
					profilFormattedTextField.setText(""); //$NON-NLS-1$
					gewichtmasseFormattedTextField.setText(""); //$NON-NLS-1$
				}
				if (evt.getCommand().equals(AufzugschachtEvent.GEWICHT_REMOVED)) {
					gewichtmasseFormattedTextField.setText(""); //$NON-NLS-1$
				}
				if (evt.getCommand().equals(AufzugschachtEvent.AUFZUGSCHACHT_COMPLETE)) {
					boolean complete = (Boolean) evt.getParameters()[0];
					if (complete) {
						changeButton(Constants.UNTERSEIL_NAME, true);
					} else {
						changeButton(Constants.UNTERSEIL_NAME, false);
					}
				}
			}
		});
		// catch action events from all toggle buttons
		seilToggleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (seilToggleButton.isSelected()) {
					aufzugschacht.setMainFrameButtonSelected(seilToggleButton.getName(), true);
					aufzugschacht.setMainFrameIsAnyButtonSelected(true);
					aufzugschacht.setMainFrameMarkedButton(seilToggleButton.getName());
				} else {
					aufzugschacht.setMainFrameButtonSelected(seilToggleButton.getName(), false);
					aufzugschacht.setMainFrameIsAnyButtonSelected(false);
				}
			}
		});
		treibscheibeToggleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (treibscheibeToggleButton.isSelected()) {
					aufzugschacht.setMainFrameButtonSelected(treibscheibeToggleButton.getName(), true);
					aufzugschacht.setMainFrameIsAnyButtonSelected(true);
					aufzugschacht.setMainFrameMarkedButton(treibscheibeToggleButton.getName());
				} else {
					aufzugschacht.setMainFrameButtonSelected(treibscheibeToggleButton.getName(), false);
					aufzugschacht.setMainFrameIsAnyButtonSelected(false);
				}
			}
		});
		umlenkrolleToggleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (umlenkrolleToggleButton.isSelected()) {
					aufzugschacht.setMainFrameButtonSelected(umlenkrolleToggleButton.getName(), true);
					aufzugschacht.setMainFrameIsAnyButtonSelected(true);
					aufzugschacht.setMainFrameMarkedButton(umlenkrolleToggleButton.getName());
				} else {
					aufzugschacht.setMainFrameButtonSelected(umlenkrolleToggleButton.getName(), false);
					aufzugschacht.setMainFrameIsAnyButtonSelected(false);
				}
			}
		});
		kabineToggleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (kabineToggleButton.isSelected()) {
					aufzugschacht.setMainFrameButtonSelected(kabineToggleButton.getName(), true);
					aufzugschacht.setMainFrameIsAnyButtonSelected(true);
					aufzugschacht.setMainFrameMarkedButton(kabineToggleButton.getName());
				} else {
					aufzugschacht.setMainFrameButtonSelected(kabineToggleButton.getName(), false);
					aufzugschacht.setMainFrameIsAnyButtonSelected(false);
				}
			}
		});
		gewichtToggleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (gewichtToggleButton.isSelected()) {
					aufzugschacht.setMainFrameButtonSelected(gewichtToggleButton.getName(), true);
					aufzugschacht.setMainFrameIsAnyButtonSelected(true);
					aufzugschacht.setMainFrameMarkedButton(gewichtToggleButton.getName());
				} else {
					aufzugschacht.setMainFrameButtonSelected(gewichtToggleButton.getName(), false);
					aufzugschacht.setMainFrameIsAnyButtonSelected(false);
				}
			}
		});
	}

	/**
	 * Schacht ready. Width and height in centimeters. Creates a thread that waits for the elevator to be constructed and then gives the width and weight to the
	 * elevator
	 * 
	 * @param hoehe
	 *            the width
	 * @param breite
	 *            the height
	 */
	public void schachtReady(int breite, int hoehe) {
		schachtReadyThread = new SchachtReadyThread(breite, hoehe, aufzugschacht, this);
		schachtReadyThread.start();
	}

	/**
	 * Load the rope parameter from the file defined in Constants class.
	 */
	public void loadSeilparameter() {
		seilparameterFile = null;
		try {
			URL url = Helper.getFileURL(Constants.SEIL_FILENAME);
			if (url == null) {
				String path = Helper.getProgramPath();

				if (path == "") { //$NON-NLS-1$
					seilparameterFile = new File(Constants.SEIL_FILENAME);
				} else {
					seilparameterFile = new File(path + Constants.FILESEP + Constants.SEIL_FILENAME);
				}
				boolean created = seilparameterFile.createNewFile();
				if (!created) {
					Constants.LOGGER.log(Level.INFO, "File already exists , using this one: " + seilparameterFile); //$NON-NLS-1$
				}
				Constants.LOGGER.log(Level.INFO, "New file: " + seilparameterFile); //$NON-NLS-1$

				// create a rope parameter frame to get the default table model
				SeilParameterFrame frame = new SeilParameterFrame(this, null);
				parameterTableModel = frame.getParameterTable().getModel();

				// then save the default table in the new file
				createErgebnisTable();
				try {
					saveSeilparameterFile();
				} catch (FileSetWritableException e) {
					Constants.LOGGER.log(Level.SEVERE, "File " + Constants.SEIL_FILENAME + " can not be set writable! Error in saving!"); //$NON-NLS-1$ //$NON-NLS-2$
					ErrorFrame errorFrame = new ErrorFrame(this,
							"Datei " + Constants.SEIL_FILENAME + " kann nicht schreibbar gemacht werden! Fehler beim Speichern!"); //$NON-NLS-1$ //$NON-NLS-2$
					Helper.showFrame(errorFrame, this);
				}
			} else {
				// if file exists the load this file
				openSeilparameterFile(url.toURI());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open rope parameter file.
	 * 
	 * @param uri
	 *            the uri of the rope rarameters file
	 */
	public void openSeilparameterFile(URI uri) {
		try {
			seilparameterFile = new File(uri);
			Constants.LOGGER.log(Level.INFO, "Open file: " + seilparameterFile); //$NON-NLS-1$
			if (seilparameterFile.canRead()) {
				// parse the loaded file
				parseSeilparameterFile(seilparameterFile);
			} else {
				// is not readable
				Constants.LOGGER.log(Level.WARNING, "Reading of " + seilparameterFile.getAbsolutePath() + " failed! Loading defaults!"); //$NON-NLS-1$ //$NON-NLS-2$
				ErrorFrame errorFrame = new ErrorFrame(this, "Das Lesen von " + seilparameterFile.getAbsolutePath() + " ist fehlgeschlagen! Lade Vorgaben!"); //$NON-NLS-1$ //$NON-NLS-2$
				Helper.showFrame(errorFrame, this);
				// load default values
				SeilParameterFrame frame = new SeilParameterFrame(this, null);
				parameterTableModel = frame.getParameterTable().getModel();
				if (seilParameterFrame != null)
					seilParameterFrame.setOpened(true);
				createErgebnisTable();
			}
		} catch (FileParseException e) {
			// error in parsing: load default values
			Constants.LOGGER.log(Level.WARNING, "Parsing of " + seilparameterFile.getAbsolutePath() + " failed! Loading defaults!"); //$NON-NLS-1$ //$NON-NLS-2$
			ErrorFrame errorFrame = new ErrorFrame(this, "Das Parsen von " + seilparameterFile.getAbsolutePath() + " ist fehlgeschlagen! Lade Vorgaben!"); //$NON-NLS-1$ //$NON-NLS-2$
			Helper.showFrame(errorFrame, this);
			SeilParameterFrame frame = new SeilParameterFrame(this, null);
			parameterTableModel = frame.getParameterTable().getModel();
			if (seilParameterFrame != null)
				seilParameterFrame.setOpened(true);
		}
	}

	/**
	 * Saves the seilparameter file.
	 * 
	 * FileType: 'n' rows of this type (true|false);<name>*(;<value>)
	 * 
	 * @throws FileSetWritableException
	 *             the file set writable exception
	 */
	public void saveSeilparameterFile() throws FileSetWritableException {
		FileHelper.saveSeilparameterFile(seilparameterFile, parameterTableModel);
	}

	/**
	 * Parses the seilparameter file. The parsing must correspondent to the saving in method above: saveSeilparametersFile()
	 * 
	 * @param file
	 *            the file to parse
	 * 
	 * @throws FileParseException
	 *             the file parse exception
	 */
	private void parseSeilparameterFile(File file) throws FileParseException {
		Constants.LOGGER.log(Level.INFO, "Parse wire parameters file"); //$NON-NLS-1$
		SeilParameterFrame frame = new SeilParameterFrame(this, null);
		parameterTableModel = frame.getParameterTable().getModel();
		DefaultTableModel dtm = (DefaultTableModel) parameterTableModel;
		for (int i = parameterTableModel.getRowCount() - 1; i >= 0; i--) {
			// reset table
			dtm.removeRow(i);
		}
		FileHelper.parseSeilparametersFile(file, frame, dtm, parameterTableModel);
		createErgebnisTable();
		if (seilParameterFrame != null)
			seilParameterFrame.setOpened(true);
	}

	/**
	 * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
	private void initComponents() {
		java.awt.GridBagConstraints gridBagConstraints;

		vertSplitPane = new javax.swing.JSplitPane();
		horizSplitPane = new javax.swing.JSplitPane();
		parameterScrollPane = new javax.swing.JScrollPane();
		parameterPanel = new javax.swing.JPanel();
		rollenTabbedPane = new javax.swing.JTabbedPane();
		jLabel13 = new javax.swing.JLabel();
		calculateButton = new javax.swing.JButton();
		treibfaehigkeitButton = new javax.swing.JButton();
		jPanel1 = new javax.swing.JPanel();
		tolleranzFormattedTextField = new javax.swing.JFormattedTextField();
		jLabel30 = new javax.swing.JLabel();
		jLabel29 = new javax.swing.JLabel();
		jLabel31 = new javax.swing.JLabel();
		jLabel32 = new javax.swing.JLabel();
		umschlWinkelFormattedTextField = new javax.swing.JFormattedTextField(df_point);
		jLabel33 = new javax.swing.JLabel();
		massUnterseilFormattedTextField = new javax.swing.JFormattedTextField();
		jLabel34 = new javax.swing.JLabel();
		gewichtmasseFormattedTextField = new javax.swing.JFormattedTextField();
		jLabel15 = new javax.swing.JLabel();
		jLabel16 = new javax.swing.JLabel();
		jLabel8 = new javax.swing.JLabel();
		jLabel9 = new javax.swing.JLabel();
		kabinenmasseFormattedTextField = new javax.swing.JFormattedTextField(df_nopoint);
		zuladungmasseFormattedTextField = new javax.swing.JFormattedTextField(df_nopoint);
		profilFormattedTextField = new javax.swing.JFormattedTextField(df_nopoint);
		jLabel14 = new javax.swing.JLabel();
		jLabel26 = new javax.swing.JLabel();
		jLabel24 = new javax.swing.JLabel();
		jLabel7 = new javax.swing.JLabel();
		durchschnittFormattedTextField = new javax.swing.JFormattedTextField(df_nopoint);
		jLabel27 = new javax.swing.JLabel();
		jLabel28 = new javax.swing.JLabel();
		gegenanteilFormattedTextField = new javax.swing.JFormattedTextField(df_nopoint);
		jLabel2 = new javax.swing.JLabel();
		jLabel1 = new javax.swing.JLabel();
		jLabel12 = new javax.swing.JLabel();
		jLabel10 = new javax.swing.JLabel();
		jLabel3 = new javax.swing.JLabel();
		jLabel5 = new javax.swing.JLabel();
		jLabel6 = new javax.swing.JLabel();
		fs1FormattedTextField = new javax.swing.JFormattedTextField(df_point);
		fs3FormattedTextField = new javax.swing.JFormattedTextField(df_point);
		fs4FormattedTextField = new javax.swing.JFormattedTextField(df_point);
		jLabel19 = new javax.swing.JLabel();
		jLabel4 = new javax.swing.JLabel();
		fn1FormattedTextField = new javax.swing.JFormattedTextField(df_point);
		jLabel21 = new javax.swing.JLabel();
		jLabel17 = new javax.swing.JLabel();
		jLabel18 = new javax.swing.JLabel();
		jLabel22 = new javax.swing.JLabel();
		jLabel23 = new javax.swing.JLabel();
		seilanzahlFormattedTextField = new javax.swing.JFormattedTextField(df_nopoint);
		aufhaengungFormattedTextField = new javax.swing.JFormattedTextField();
		durchmesserFormattedTextField = new javax.swing.JFormattedTextField(df_point);
		jLabel25 = new javax.swing.JLabel();
		biegelaengeFormattedTextField = new javax.swing.JFormattedTextField(df_nopoint);
		jLabel11 = new javax.swing.JLabel();
		jPanel2 = new javax.swing.JPanel();
		jPanel3 = new javax.swing.JPanel();
		jPanel4 = new javax.swing.JPanel();
		jLabel35 = new javax.swing.JLabel();
		grafikPanel = new javax.swing.JPanel();
		grafikLabel = new javax.swing.JLabel();
		zeichenPanel = new javax.swing.JPanel();
		elementePanel = new javax.swing.JPanel();
		elementeLabel = new javax.swing.JLabel();
		iconsPanel = new javax.swing.JPanel();
		umlenkrollePanel = new javax.swing.JPanel();
		umlenkrolleToggleButton = new javax.swing.JToggleButton();
		umlenkrolleLabel = new javax.swing.JLabel();
		treibscheibePanel = new javax.swing.JPanel();
		treibscheibeToggleButton = new javax.swing.JToggleButton();
		treibscheibeLabel = new javax.swing.JLabel();
		kabinePanel = new javax.swing.JPanel();
		kabineToggleButton = new javax.swing.JToggleButton();
		kabineLabel = new javax.swing.JLabel();
		gewichtPanel = new javax.swing.JPanel();
		gewichtToggleButton = new javax.swing.JToggleButton();
		gewichtLabel = new javax.swing.JLabel();
		seilPanel = new javax.swing.JPanel();
		seilToggleButton = new javax.swing.JToggleButton();
		seilLabel = new javax.swing.JLabel();
		unterseilPanel = new javax.swing.JPanel();
		unterseilButton = new javax.swing.JButton();
		unterseilLabel = new javax.swing.JLabel();
		zickzackPanel = new javax.swing.JPanel();
		zickzackButton = new javax.swing.JButton();
		zickzackLabel = new javax.swing.JLabel();
		ergebnisseScrollPane = new javax.swing.JScrollPane();
		ergebnisPanel = new javax.swing.JPanel();
		ergebnisLabel = new javax.swing.JLabel();
		ergebnisTableScrollPane = new javax.swing.JScrollPane();
		einzelErgebnisTableScrollPane = new javax.swing.JScrollPane();
		jLabel20 = new javax.swing.JLabel();
		seiltypLabel = new javax.swing.JLabel();
		chooseSeiltypButton = new javax.swing.JButton();
		jMenuBar = new javax.swing.JMenuBar();
		jMenuDatei = new javax.swing.JMenu();
		jMenuItemNeu = new javax.swing.JMenuItem();
		jMenuItemOeffnen = new javax.swing.JMenuItem();
		jMenuItemSpeichernAls = new javax.swing.JMenuItem();
		jMenuItemSpeichern = new javax.swing.JMenuItem();
		jSeparator1 = new javax.swing.JSeparator();
		jMenuItemDrucken = new javax.swing.JMenuItem();
		jSeparator2 = new javax.swing.JSeparator();
		jMenuItemBeenden = new javax.swing.JMenuItem();
		jMenuParameter = new javax.swing.JMenu();
		jMenuItemSeil = new javax.swing.JMenuItem();
		jMenuItemGlobal = new javax.swing.JMenuItem();
		jMenuItemSchacht = new javax.swing.JMenuItem();
		jMenuExtras = new javax.swing.JMenu();
		jMenuItemSicherungSeiltypen = new javax.swing.JMenuItem();
		jMenuItemLadeSeiltypen = new javax.swing.JMenuItem();
		jMenuHilfe = new javax.swing.JMenu();
		jMenuItemHilfe = new javax.swing.JMenuItem();
		jMenuItemInfo = new javax.swing.JMenuItem();
		jMenuItemLogView = new javax.swing.JMenuItem();

		setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
		setTitle("Aufzugseile");
		setName("mainFrame"); // NOI18N
		addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent evt) {
				formWindowClosing(evt);
			}
		});
		addComponentListener(new java.awt.event.ComponentAdapter() {
			public void componentResized(java.awt.event.ComponentEvent evt) {
				formComponentResized(evt);
			}
		});
		addWindowStateListener(new java.awt.event.WindowStateListener() {
			public void windowStateChanged(java.awt.event.WindowEvent evt) {
				formWindowStateChanged(evt);
			}
		});

		vertSplitPane.setDividerLocation(726);
		vertSplitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

		horizSplitPane.setDividerLocation(728);

		parameterPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
		parameterPanel.setVerifyInputWhenFocusTarget(false);

		jLabel13.setText("Rollenparameter");

		calculateButton.setText("Lebensdauer");
		calculateButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				calculateButtonActionPerformed(evt);
			}
		});

		treibfaehigkeitButton.setText("Treibfähigkeit");
		treibfaehigkeitButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				treibfaehigkeitButtonActionPerformed(evt);
			}
		});

		jPanel1.setLayout(new java.awt.GridBagLayout());

		tolleranzFormattedTextField.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(java.awt.event.KeyEvent evt) {
				tolleranzFormattedTextFieldKeyReleased(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
		jPanel1.add(tolleranzFormattedTextField, gridBagConstraints);

		jLabel30.setText("%");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
		jPanel1.add(jLabel30, gridBagConstraints);

		jLabel29.setText("%");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
		jPanel1.add(jLabel29, gridBagConstraints);

		jLabel31.setText("Umschlingungswinkel:");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 8;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
		jPanel1.add(jLabel31, gridBagConstraints);

		jLabel32.setText("Grad");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 8;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
		jPanel1.add(jLabel32, gridBagConstraints);

		umschlWinkelFormattedTextField.setEditable(false);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 8;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
		jPanel1.add(umschlWinkelFormattedTextField, gridBagConstraints);

		jLabel33.setText("kg");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
		jPanel1.add(jLabel33, gridBagConstraints);

		massUnterseilFormattedTextField.setEditable(false);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 9;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
		jPanel1.add(massUnterseilFormattedTextField, gridBagConstraints);

		jLabel34.setText("Masse am Unterseil:");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 9;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
		jPanel1.add(jLabel34, gridBagConstraints);

		gewichtmasseFormattedTextField.setEditable(false);
		gewichtmasseFormattedTextField.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(java.awt.event.KeyEvent evt) {
				gewichtmasseFormattedTextFieldKeyReleased(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 7;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
		jPanel1.add(gewichtmasseFormattedTextField, gridBagConstraints);

		jLabel15.setText("Gegengewicht:");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 7;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
		jPanel1.add(jLabel15, gridBagConstraints);

		jLabel16.setText("kg");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 9;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
		jPanel1.add(jLabel16, gridBagConstraints);

		jLabel8.setText("kg");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 7;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
		jPanel1.add(jLabel8, gridBagConstraints);

		jLabel9.setText("kg");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
		jPanel1.add(jLabel9, gridBagConstraints);

		kabinenmasseFormattedTextField.setPreferredSize(new java.awt.Dimension(60, 20));
		kabinenmasseFormattedTextField.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(java.awt.event.KeyEvent evt) {
				kabinenmasseFormattedTextFieldKeyReleased(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
		jPanel1.add(kabinenmasseFormattedTextField, gridBagConstraints);

		zuladungmasseFormattedTextField.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(java.awt.event.KeyEvent evt) {
				zuladungmasseFormattedTextFieldKeyReleased(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
		jPanel1.add(zuladungmasseFormattedTextField, gridBagConstraints);

		profilFormattedTextField.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(java.awt.event.KeyEvent evt) {
				profilFormattedTextFieldKeyReleased(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
		jPanel1.add(profilFormattedTextField, gridBagConstraints);

		jLabel14.setText("durchschn. Zuladung:");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
		jPanel1.add(jLabel14, gridBagConstraints);

		jLabel26.setText("Erdgeschossfahrten:");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
		jPanel1.add(jLabel26, gridBagConstraints);

		jLabel24.setText("%");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
		jPanel1.add(jLabel24, gridBagConstraints);

		jLabel7.setText("%");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
		jPanel1.add(jLabel7, gridBagConstraints);

		durchschnittFormattedTextField.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(java.awt.event.KeyEvent evt) {
				durchschnittFormattedTextFieldKeyReleased(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
		jPanel1.add(durchschnittFormattedTextField, gridBagConstraints);

		jLabel27.setText("Gegengewichtsparameter:");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
		jPanel1.add(jLabel27, gridBagConstraints);

		jLabel28.setText("Seilablage bei:");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
		jPanel1.add(jLabel28, gridBagConstraints);

		gegenanteilFormattedTextField.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(java.awt.event.KeyEvent evt) {
				gegenanteilFormattedTextFieldKeyReleased(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
		jPanel1.add(gegenanteilFormattedTextField, gridBagConstraints);

		jLabel2.setText("maximale Zuladung:");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
		jPanel1.add(jLabel2, gridBagConstraints);

		jLabel1.setText("Kabinengewicht:");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
		jPanel1.add(jLabel1, gridBagConstraints);

		jLabel12.setText("Massen");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
		jPanel1.add(jLabel12, gridBagConstraints);

		jLabel10.setText("Seilkraftfaktoren");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 10;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(4, 1, 1, 1);
		jPanel1.add(jLabel10, gridBagConstraints);

		jLabel3.setText("Reibung der Lastführung (fs1):");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 11;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
		jPanel1.add(jLabel3, gridBagConstraints);

		jLabel5.setText("Parallele Seile (fs3):");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 12;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
		jPanel1.add(jLabel5, gridBagConstraints);

		jLabel6.setText("Beschleunigung (fs4):");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 13;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
		jPanel1.add(jLabel6, gridBagConstraints);

		fs1FormattedTextField.setPreferredSize(new java.awt.Dimension(60, 20));
		fs1FormattedTextField.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(java.awt.event.KeyEvent evt) {
				fs1FormattedTextFieldKeyReleased(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 11;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
		jPanel1.add(fs1FormattedTextField, gridBagConstraints);

		fs3FormattedTextField.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(java.awt.event.KeyEvent evt) {
				fs3FormattedTextFieldKeyReleased(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 12;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
		jPanel1.add(fs3FormattedTextField, gridBagConstraints);

		fs4FormattedTextField.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(java.awt.event.KeyEvent evt) {
				fs4FormattedTextFieldKeyReleased(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 13;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
		jPanel1.add(fs4FormattedTextField, gridBagConstraints);

		jLabel19.setText("Biegewechselfaktoren");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 14;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(4, 1, 1, 1);
		jPanel1.add(jLabel19, gridBagConstraints);

		jLabel4.setText("Seilschmierung (fn1):");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 15;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
		jPanel1.add(jLabel4, gridBagConstraints);

		fn1FormattedTextField.setPreferredSize(new java.awt.Dimension(60, 20));
		fn1FormattedTextField.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(java.awt.event.KeyEvent evt) {
				fn1FormattedTextFieldKeyReleased(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 15;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
		jPanel1.add(fn1FormattedTextField, gridBagConstraints);

		jLabel21.setText("Seiltriebparameter");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 16;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(4, 1, 1, 1);
		jPanel1.add(jLabel21, gridBagConstraints);

		jLabel17.setText("Anzahl der Seile:");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 17;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
		jPanel1.add(jLabel17, gridBagConstraints);

		jLabel18.setText("Aufhängung:");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 18;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
		jPanel1.add(jLabel18, gridBagConstraints);

		jLabel22.setText("Seilnenndurchmesser:");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 19;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
		jPanel1.add(jLabel22, gridBagConstraints);

		jLabel23.setText("Seilbiegelänge:");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 20;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
		jPanel1.add(jLabel23, gridBagConstraints);

		seilanzahlFormattedTextField.setPreferredSize(new java.awt.Dimension(60, 20));
		seilanzahlFormattedTextField.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(java.awt.event.KeyEvent evt) {
				seilanzahlFormattedTextFieldKeyReleased(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 17;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
		jPanel1.add(seilanzahlFormattedTextField, gridBagConstraints);

		aufhaengungFormattedTextField.setEditable(false);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 18;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
		jPanel1.add(aufhaengungFormattedTextField, gridBagConstraints);

		durchmesserFormattedTextField.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(java.awt.event.KeyEvent evt) {
				durchmesserFormattedTextFieldKeyReleased(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 19;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
		jPanel1.add(durchmesserFormattedTextField, gridBagConstraints);

		jLabel25.setText("mm");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 19;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
		jPanel1.add(jLabel25, gridBagConstraints);

		biegelaengeFormattedTextField.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(java.awt.event.KeyEvent evt) {
				biegelaengeFormattedTextFieldKeyReleased(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 20;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
		jPanel1.add(biegelaengeFormattedTextField, gridBagConstraints);

		jLabel11.setText("mm");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 20;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
		jPanel1.add(jLabel11, gridBagConstraints);

		jPanel2.setLayout(new java.awt.GridBagLayout());

		jPanel3.setLayout(new java.awt.GridBagLayout());

		jPanel4.setLayout(new java.awt.GridBagLayout());

		jLabel35.setText("Parameter");

		org.jdesktop.layout.GroupLayout parameterPanelLayout = new org.jdesktop.layout.GroupLayout(parameterPanel);
		parameterPanel.setLayout(parameterPanelLayout);
		parameterPanelLayout.setHorizontalGroup(parameterPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
				.add(
						parameterPanelLayout.createSequentialGroup().add(
								parameterPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(
										parameterPanelLayout.createSequentialGroup().addContainerGap().add(
												parameterPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel1,
														org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
														org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jLabel13).add(
														parameterPanelLayout.createSequentialGroup().add(rollenTabbedPane,
																org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE).addPreferredGap(
																org.jdesktop.layout.LayoutStyle.RELATED).add(
																parameterPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(
																		org.jdesktop.layout.GroupLayout.TRAILING, jPanel2,
																		org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
																		org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
																		org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(
																		org.jdesktop.layout.GroupLayout.TRAILING, jPanel3,
																		org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
																		org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
																		org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(
																		org.jdesktop.layout.GroupLayout.TRAILING, jPanel4,
																		org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
																		org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
																		org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))).add(
														parameterPanelLayout.createSequentialGroup().add(calculateButton).addPreferredGap(
																org.jdesktop.layout.LayoutStyle.RELATED).add(treibfaehigkeitButton)))).add(jLabel35))
								.addContainerGap()));
		parameterPanelLayout.setVerticalGroup(parameterPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(
				parameterPanelLayout.createSequentialGroup().add(jLabel35).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel1,
						org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
						org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jLabel13).addPreferredGap(
						org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
						org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(
						parameterPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(
								parameterPanelLayout.createSequentialGroup().add(26, 26, 26).add(jPanel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
										org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(
										org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
										org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).add(rollenTabbedPane,
								org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 160, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(
						org.jdesktop.layout.LayoutStyle.RELATED).add(
						parameterPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(calculateButton).add(treibfaehigkeitButton))
						.addContainerGap(20, Short.MAX_VALUE)));

		parameterScrollPane.setViewportView(parameterPanel);

		horizSplitPane.setRightComponent(parameterScrollPane);

		grafikPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

		grafikLabel.setText("Grafik");
		grafikLabel.setName("grafikLabel"); // NOI18N

		zeichenPanel.setName("zeichenPanel"); // NOI18N
		zeichenPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
			public void componentResized(java.awt.event.ComponentEvent evt) {
				zeichenPanelComponentResized(evt);
			}
		});

		org.jdesktop.layout.GroupLayout zeichenPanelLayout = new org.jdesktop.layout.GroupLayout(zeichenPanel);
		zeichenPanel.setLayout(zeichenPanelLayout);
		zeichenPanelLayout.setHorizontalGroup(zeichenPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(0, 570, Short.MAX_VALUE));
		zeichenPanelLayout.setVerticalGroup(zeichenPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(0, 690, Short.MAX_VALUE));

		elementePanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		elementePanel.setName("elementePanel"); // NOI18N

		elementeLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
		elementeLabel.setText("Elemente");

		iconsPanel.setMinimumSize(new java.awt.Dimension(32, 32));
		iconsPanel.setLayout(new java.awt.GridLayout(7, 1));

		umlenkrollePanel.setMinimumSize(new java.awt.Dimension(0, 0));
		umlenkrollePanel.setLayout(new java.awt.GridLayout(2, 0));

		umlenkrolleToggleButton.setBackground(java.awt.Color.lightGray);
		umlenkrolleToggleButton.setBorderPainted(false);
		umlenkrolleToggleButton.setFocusPainted(false);
		umlenkrolleToggleButton.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mousePressed(java.awt.event.MouseEvent evt) {
				umlenkrolleToggleButtonMousePressed(evt);
			}
		});
		umlenkrollePanel.add(umlenkrolleToggleButton);

		umlenkrolleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		umlenkrolleLabel.setText("Umlenkrolle");
		umlenkrolleLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
		umlenkrollePanel.add(umlenkrolleLabel);

		iconsPanel.add(umlenkrollePanel);

		treibscheibePanel.setMinimumSize(new java.awt.Dimension(0, 0));
		treibscheibePanel.setLayout(new java.awt.GridLayout(2, 0));

		treibscheibeToggleButton.setBackground(java.awt.Color.lightGray);
		treibscheibeToggleButton.setBorderPainted(false);
		treibscheibeToggleButton.setFocusPainted(false);
		treibscheibeToggleButton.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mousePressed(java.awt.event.MouseEvent evt) {
				treibscheibeToggleButtonMousePressed(evt);
			}
		});
		treibscheibePanel.add(treibscheibeToggleButton);

		treibscheibeLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		treibscheibeLabel.setText("Treibscheibe");
		treibscheibeLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
		treibscheibePanel.add(treibscheibeLabel);

		iconsPanel.add(treibscheibePanel);

		kabinePanel.setMinimumSize(new java.awt.Dimension(0, 0));
		kabinePanel.setLayout(new java.awt.GridLayout(2, 0));

		kabineToggleButton.setBackground(java.awt.Color.lightGray);
		kabineToggleButton.setBorderPainted(false);
		kabineToggleButton.setFocusPainted(false);
		kabineToggleButton.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mousePressed(java.awt.event.MouseEvent evt) {
				kabineToggleButtonMousePressed(evt);
			}
		});
		kabinePanel.add(kabineToggleButton);

		kabineLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		kabineLabel.setText("Kabine");
		kabineLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
		kabinePanel.add(kabineLabel);

		iconsPanel.add(kabinePanel);

		gewichtPanel.setLayout(new java.awt.GridLayout(2, 0));

		gewichtToggleButton.setBackground(java.awt.Color.lightGray);
		gewichtToggleButton.setBorderPainted(false);
		gewichtToggleButton.setFocusPainted(false);
		gewichtToggleButton.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mousePressed(java.awt.event.MouseEvent evt) {
				gewichtToggleButtonMousePressed(evt);
			}
		});
		gewichtPanel.add(gewichtToggleButton);

		gewichtLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		gewichtLabel.setText("Gewicht");
		gewichtLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
		gewichtPanel.add(gewichtLabel);

		iconsPanel.add(gewichtPanel);

		seilPanel.setMinimumSize(new java.awt.Dimension(0, 0));
		seilPanel.setLayout(new java.awt.GridLayout(2, 0));

		seilToggleButton.setBackground(java.awt.Color.lightGray);
		seilToggleButton.setBorderPainted(false);
		seilToggleButton.setFocusPainted(false);
		seilToggleButton.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mousePressed(java.awt.event.MouseEvent evt) {
				seilToggleButtonMousePressed(evt);
			}
		});
		seilPanel.add(seilToggleButton);

		seilLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		seilLabel.setText("Seil");
		seilLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
		seilPanel.add(seilLabel);

		iconsPanel.add(seilPanel);

		unterseilPanel.setMinimumSize(new java.awt.Dimension(0, 0));
		unterseilPanel.setLayout(new java.awt.GridLayout(2, 0));

		unterseilButton.setBackground(java.awt.Color.lightGray);
		unterseilButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				unterseilButtonActionPerformed(evt);
			}
		});
		unterseilPanel.add(unterseilButton);

		unterseilLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		unterseilLabel.setText("Unterseil");
		unterseilLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
		unterseilPanel.add(unterseilLabel);

		iconsPanel.add(unterseilPanel);

		zickzackPanel.setMinimumSize(new java.awt.Dimension(0, 0));
		zickzackPanel.setLayout(new java.awt.GridLayout(2, 0));

		zickzackButton.setBackground(java.awt.Color.lightGray);
		zickzackButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				zickzackButtonActionPerformed(evt);
			}
		});
		zickzackPanel.add(zickzackButton);

		zickzackLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		zickzackLabel.setText("Trenner");
		zickzackLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
		zickzackPanel.add(zickzackLabel);

		iconsPanel.add(zickzackPanel);

		org.jdesktop.layout.GroupLayout elementePanelLayout = new org.jdesktop.layout.GroupLayout(elementePanel);
		elementePanel.setLayout(elementePanelLayout);
		elementePanelLayout.setHorizontalGroup(elementePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(
				elementePanelLayout.createSequentialGroup().add(elementeLabel).addContainerGap(81, Short.MAX_VALUE)).add(iconsPanel,
				org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE));
		elementePanelLayout.setVerticalGroup(elementePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(
				elementePanelLayout.createSequentialGroup().add(elementeLabel).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(iconsPanel,
						org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 666, Short.MAX_VALUE)));

		org.jdesktop.layout.GroupLayout grafikPanelLayout = new org.jdesktop.layout.GroupLayout(grafikPanel);
		grafikPanel.setLayout(grafikPanelLayout);
		grafikPanelLayout.setHorizontalGroup(grafikPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(
				grafikPanelLayout.createSequentialGroup().add(
						grafikPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(
								org.jdesktop.layout.GroupLayout.TRAILING,
								grafikPanelLayout.createSequentialGroup().addContainerGap().add(zeichenPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
										org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
										.add(elementePanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
												org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).add(grafikLabel)).addContainerGap()));
		grafikPanelLayout.setVerticalGroup(grafikPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(
				grafikPanelLayout.createSequentialGroup().add(grafikLabel).add(
						grafikPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(
								grafikPanelLayout.createSequentialGroup().addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(elementePanel,
										org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).add(
								grafikPanelLayout.createSequentialGroup().add(6, 6, 6).add(zeichenPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
										org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))).addContainerGap()));

		horizSplitPane.setLeftComponent(grafikPanel);

		vertSplitPane.setTopComponent(horizSplitPane);

		ergebnisPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

		ergebnisLabel.setText("Ergebnisse");

		jLabel20.setText("Fahrtenzahl einzelner Rollen des Seils:");

		seiltypLabel.setText("<gewählter Seiltyp>");

		chooseSeiltypButton.setText("Wähle Seiltyp");
		chooseSeiltypButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				chooseSeiltypButtonActionPerformed(evt);
			}
		});

		org.jdesktop.layout.GroupLayout ergebnisPanelLayout = new org.jdesktop.layout.GroupLayout(ergebnisPanel);
		ergebnisPanel.setLayout(ergebnisPanelLayout);
		ergebnisPanelLayout.setHorizontalGroup(ergebnisPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(
				ergebnisPanelLayout.createSequentialGroup().add(ergebnisLabel).add(27, 27, 27)).add(
				ergebnisPanelLayout.createSequentialGroup().addContainerGap().add(einzelErgebnisTableScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
						859, Short.MAX_VALUE).addContainerGap()).add(
				ergebnisPanelLayout.createSequentialGroup().addContainerGap().add(ergebnisTableScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 859,
						Short.MAX_VALUE).addContainerGap()).add(
				ergebnisPanelLayout.createSequentialGroup().addContainerGap().add(jLabel20).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(
						seiltypLabel).add(3, 3, 3).add(chooseSeiltypButton).addContainerGap(481, Short.MAX_VALUE)));
		ergebnisPanelLayout.setVerticalGroup(ergebnisPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(
				ergebnisPanelLayout.createSequentialGroup().add(ergebnisLabel).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(
						ergebnisTableScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 67, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(
								ergebnisPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel20).add(seiltypLabel).add(
										chooseSeiltypButton)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(einzelErgebnisTableScrollPane,
								org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 59, Short.MAX_VALUE).addContainerGap()));

		ergebnisseScrollPane.setViewportView(ergebnisPanel);

		vertSplitPane.setRightComponent(ergebnisseScrollPane);

		jMenuDatei.setText("Datei");

		jMenuItemNeu.setText("Neu");
		jMenuItemNeu.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jMenuItemNeuActionPerformed(evt);
			}
		});
		jMenuDatei.add(jMenuItemNeu);

		jMenuItemOeffnen.setText("Öffnen");
		jMenuItemOeffnen.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jMenuItemOeffnenActionPerformed(evt);
			}
		});
		jMenuDatei.add(jMenuItemOeffnen);

		jMenuItemSpeichernAls.setText("Speichern als ...");
		jMenuItemSpeichernAls.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jMenuItemSpeichernAlsActionPerformed(evt);
			}
		});
		jMenuDatei.add(jMenuItemSpeichernAls);

		jMenuItemSpeichern.setText("Speichern");
		jMenuItemSpeichern.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jMenuItemSpeichernActionPerformed(evt);
			}
		});
		jMenuDatei.add(jMenuItemSpeichern);
		jMenuDatei.add(jSeparator1);

		jMenuItemDrucken.setText("Drucken");
		jMenuItemDrucken.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jMenuItemDruckenActionPerformed(evt);
			}
		});
		jMenuDatei.add(jMenuItemDrucken);
		jMenuDatei.add(jSeparator2);

		jMenuItemBeenden.setText("Beenden");
		jMenuItemBeenden.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jMenuItemBeendenActionPerformed(evt);
			}
		});
		jMenuDatei.add(jMenuItemBeenden);

		jMenuBar.add(jMenuDatei);

		jMenuParameter.setText("Parameter");

		jMenuItemSeil.setText("Seiltypparameter");
		jMenuItemSeil.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jMenuItemSeilActionPerformed(evt);
			}
		});
		jMenuParameter.add(jMenuItemSeil);

		jMenuItemGlobal.setText("Globale Parameter");
		jMenuItemGlobal.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jMenuItemGlobalActionPerformed(evt);
			}
		});
		jMenuParameter.add(jMenuItemGlobal);

		jMenuItemSchacht.setText("Schachtparameter");
		jMenuItemSchacht.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jMenuItemSchachtActionPerformed(evt);
			}
		});
		jMenuParameter.add(jMenuItemSchacht);

		jMenuBar.add(jMenuParameter);

		jMenuExtras.setText("Extras");

		jMenuItemSicherungSeiltypen.setText("Sichern der Seiltypen");
		jMenuItemSicherungSeiltypen.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jMenuItemSicherungSeiltypenActionPerformed(evt);
			}
		});
		jMenuExtras.add(jMenuItemSicherungSeiltypen);

		jMenuItemLadeSeiltypen.setText("Laden von Seiltypen");
		jMenuItemLadeSeiltypen.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jMenuItemLadeSeiltypenActionPerformed(evt);
			}
		});
		jMenuExtras.add(jMenuItemLadeSeiltypen);

		jMenuBar.add(jMenuExtras);

		jMenuHilfe.setText("Hilfe");

		jMenuItemHilfe.setText("Hilfe");
		jMenuItemHilfe.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jMenuItemHilfeActionPerformed(evt);
			}
		});
		jMenuHilfe.add(jMenuItemHilfe);

		jMenuItemInfo.setText("Info");
		jMenuItemInfo.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jMenuItemInfoActionPerformed(evt);
			}
		});
		jMenuHilfe.add(jMenuItemInfo);

		jMenuItemLogView.setText("Log-Datei ansehen");
		jMenuItemLogView.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jMenuItemLogViewActionPerformed(evt);
			}
		});
		jMenuHilfe.add(jMenuItemLogView);

		jMenuBar.add(jMenuHilfe);

		setJMenuBar(jMenuBar);

		org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(vertSplitPane,
				org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 885, Short.MAX_VALUE));
		layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(vertSplitPane,
				org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 928, Short.MAX_VALUE));

		java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		setBounds((screenSize.width - 893) / 2, (screenSize.height - 983) / 2, 893, 983);
	}// </editor-fold>//GEN-END:initComponents

	private void unterseilButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_unterseilToggleButtonMousePressed
		addUnterseil();
	}// GEN-LAST:event_unterseilToggleButtonMousePressed

	private void jMenuItemLogViewActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItemLogViewActionPerformed
		LogViewFrame logViewFrame = new LogViewFrame(this, logfilepath);
		Helper.showFrame(logViewFrame, this);
	}// GEN-LAST:event_jMenuItemLogViewActionPerformed

	private void treibfaehigkeitButtonActionPerformed(java.awt.event.ActionEvent evt) {
		if (aufzugschacht.getTreibscheibe() != null && aufzugschacht.getKabine() != null && aufzugschacht.getSeil() != null
				&& aufzugschacht.getGewicht() != null) {
			treibFrame = new TreibfaehigkeitFrame(this, aufzugschacht.getTreibscheibe(), aufzugschacht.getKabine(), aufzugschacht.getGewicht(), aufzugschacht
					.getHoehe(), aufzugschacht.getGeschwindigkeit(), aufzugschacht.getSeil().getCount(), aufzugschacht.getSeil().getD());
			Helper.showFrame(treibFrame, this);
			treibFrame.checkGegengewichtsparameter(aufzugschacht.getGewicht());
		} else {
			ErrorFrame errorFrame = new ErrorFrame(
					this,
					"Treibf" + Constants.AE_K + "higkeit kann nicht berechnet werden, da mindestens eines der folgenden Elemente fehlt. Kabine, Gewicht, Treibscheibe, Seil."); //$NON-NLS-1$ //$NON-NLS-2$
			Helper.showFrame(errorFrame, this);
		}
	}

	private void chooseSeiltypButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_chooseSeiltypButtonActionPerformed
		ChooseSeiltypFrame chooseFrame = new ChooseSeiltypFrame(parameterTableModel, this);
		Helper.showFrame(chooseFrame, this);
	}// GEN-LAST:event_chooseSeiltypButtonActionPerformed

	/**
	 * allowance of calculation formatted text field key released.
	 * 
	 * @param evt
	 *            the evt
	 */
	private void tolleranzFormattedTextFieldKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_tolleranzFormattedTextFieldKeyReleased
		try {
			berechnung.setToleranz(df_nopoint.parse(tolleranzFormattedTextField.getText()).intValue() / (double) 100);
			parameterChanged();
		} catch (ParseException e) {
			// auto-corrected by FormattedTextField
		}
	}// GEN-LAST:event_tolleranzFormattedTextFieldKeyReleased

	/**
	 * Gegenanteil formatted text field key released.
	 * 
	 * @param evt
	 *            the evt
	 */
	private void gegenanteilFormattedTextFieldKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_gegenanteilFormattedTextFieldKeyReleased
		try {
			if (aufzugschacht.getGewicht() != null) {
				aufzugschacht.getGewicht().setPart(df_nopoint.parse(gegenanteilFormattedTextField.getText()).intValue() / (double) 100);
				changeGewicht();
				parameterChanged();
			}
		} catch (ParseException e) {
			// auto-corrected by FormattedTextField
		}
	}// GEN-LAST:event_gegenanteilFormattedTextFieldKeyReleased

	/**
	 * Profil formatted text field key released.
	 * 
	 * @param evt
	 *            the evt
	 */
	private void profilFormattedTextFieldKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_profilFormattedTextFieldKeyReleased
		try {
			if (aufzugschacht.getKabine() != null) {
				aufzugschacht.getKabine().setProfil(df_nopoint.parse(profilFormattedTextField.getText()).intValue() / (double) 100);
				parameterChanged();
			}
		} catch (ParseException e) {
			// auto-corrected by FormattedTextField
		}
	}// GEN-LAST:event_profilFormattedTextFieldKeyReleased

	/**
	 * J menu item lade seiltypen action performed.
	 * 
	 * @param evt
	 *            the evt
	 */
	private void jMenuItemLadeSeiltypenActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItemLadeSeiltypenActionPerformed
		openFileSeil();
	}// GEN-LAST:event_jMenuItemLadeSeiltypenActionPerformed

	/**
	 * J menu item sicherung seiltypen action performed.
	 * 
	 * @param evt
	 *            the evt
	 */
	private void jMenuItemSicherungSeiltypenActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItemSicherungSeiltypenActionPerformed
		fileSaveAsSeil();
	}// GEN-LAST:event_jMenuItemSicherungSeiltypenActionPerformed

	/**
	 * J menu item info action performed.
	 * 
	 * @param evt
	 *            the evt
	 */
	private void jMenuItemInfoActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItemInfoActionPerformed
		InfoDialog infoDialog = new InfoDialog(this, true);
		infoDialog.setVisible(true);
	}// GEN-LAST:event_jMenuItemInfoActionPerformed

	/**
	 * J menu item hilfe action performed.
	 * 
	 * @param evt
	 *            the evt
	 */
	private void jMenuItemHilfeActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItemHilfeActionPerformed
		JHelp helpViewer = null;
		try {
			// Hauptfenster in der nächsten Zeile ersetzen durch aktuellen Klassennamen
			ClassLoader cl = MainFrame.class.getClassLoader();
			URL url = HelpSet.findHelpSet(cl, "helpset.hs"); //$NON-NLS-1$
			helpViewer = new JHelp(new HelpSet(cl, url));
			// Darzustellendes Kapitel festlegen, ID muss im XML existieren!
			helpViewer.setCurrentID("einleitung_html"); //$NON-NLS-1$

			JFrame frame = new JFrame();
			frame.setTitle("Hilfe zu (Aufzugseillebensdauerberechnung)"); //$NON-NLS-1$
			frame.setSize(800, 600);
			frame.setLocation(getX() + (getWidth() - 800) / 2, getY() + (getHeight() - 600) / 2);
			frame.getContentPane().add(helpViewer);
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frame.setVisible(true);
		} catch (HelpSetException e) {
			System.err.println("API Help Set not found"); //$NON-NLS-1$
		}
	}// GEN-LAST:event_jMenuItemHilfeActionPerformed

	/**
	 * Form window closing.
	 * 
	 * @param evt
	 *            the evt
	 */
	private void formWindowClosing(java.awt.event.WindowEvent evt) {// GEN-FIRST:event_formWindowClosing
		if (schachtReady) {
			jMenuItemBeendenActionPerformed(null);
		}
	}// GEN-LAST:event_formWindowClosing

	/**
	 * J menu item schacht action performed.
	 * 
	 * @param evt
	 *            the evt
	 */
	private void jMenuItemSchachtActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItemSchachtActionPerformed
		SchachtFrame schachtFrame = new SchachtFrame(this);
		Helper.showFrame(schachtFrame, this);
	}// GEN-LAST:event_jMenuItemSchachtActionPerformed

	/**
	 * Zickzack button action performed.
	 * 
	 * @param evt
	 *            the evt
	 */
	private void zickzackButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_zickzackButtonActionPerformed
		addZickzack();
	}// GEN-LAST:event_zickzackButtonActionPerformed

	/**
	 * Durchschnitt formatted text field key released.
	 * 
	 * @param evt
	 *            the evt
	 */
	private void durchschnittFormattedTextFieldKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_durchschnittFormattedTextFieldKeyReleased
		try {
			if (aufzugschacht.getKabine() != null) {
				aufzugschacht.getKabine().setDurchschnitt(df_nopoint.parse(durchschnittFormattedTextField.getText()).intValue() / (double) 100);
				changeGewicht();
				parameterChanged();
			}
		} catch (ParseException e) {
			// auto-corrected by FormattedTextField
		}
	}// GEN-LAST:event_durchschnittFormattedTextFieldKeyReleased

	/**
	 * J menu item speichern als action performed.
	 * 
	 * @param evt
	 *            the evt
	 */
	private void jMenuItemSpeichernAlsActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItemSpeichernAlsActionPerformed
		fileSaveAs();
	}// GEN-LAST:event_jMenuItemSpeichernAlsActionPerformed

	/**
	 * J menu item beenden action performed.
	 * 
	 * @param evt
	 *            the evt
	 */
	private void jMenuItemBeendenActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItemBeendenActionPerformed
		quit();
	}// GEN-LAST:event_jMenuItemBeendenActionPerformed

	/**
	 * J menu item speichern action performed.
	 * 
	 * @param evt
	 *            the evt
	 */
	private void jMenuItemSpeichernActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItemSpeichernActionPerformed
		fileSave();
	}// GEN-LAST:event_jMenuItemSpeichernActionPerformed

	/**
	 * J menu item oeffnen action performed.
	 * 
	 * @param evt
	 *            the evt
	 */
	private void jMenuItemOeffnenActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItemOeffnenActionPerformed
		fileOpen();
	}// GEN-LAST:event_jMenuItemOeffnenActionPerformed

	/**
	 * J menu item neu action performed.
	 * 
	 * @param evt
	 *            the evt
	 */
	private void jMenuItemNeuActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItemNeuActionPerformed
		fileNew();
	}// GEN-LAST:event_jMenuItemNeuActionPerformed

	/**
	 * J menu item seil action performed.
	 * 
	 * @param evt
	 *            the evt
	 */
	private void jMenuItemSeilActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItemSeilActionPerformed
		if (seilParameterFrame == null || !seilParameterFrame.isShowing()) {
			seilParameterFrame = new SeilParameterFrame(this, parameterTableModel);
			Helper.showFrame(seilParameterFrame, this);
		}
	}// GEN-LAST:event_jMenuItemSeilActionPerformed

	/**
	 * Biegelaenge formatted text field key released.
	 * 
	 * @param evt
	 *            the evt
	 */
	private void biegelaengeFormattedTextFieldKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_biegelaengeFormattedTextFieldKeyReleased
		if (aufzugschacht.getSeil() != null) {
			try {
				aufzugschacht.getSeil().setBiegelaenge((df_nopoint.parse(biegelaengeFormattedTextField.getText())).intValue());
				parameterChanged();
			} catch (ParseException e) {
				// auto-correction by FormattedTextField
			}
		}
	}// GEN-LAST:event_biegelaengeFormattedTextFieldKeyReleased

	/**
	 * Durchmesser formatted text field key released.
	 * 
	 * @param evt
	 *            the evt
	 */
	private void durchmesserFormattedTextFieldKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_durchmesserFormattedTextFieldKeyReleased
		if (aufzugschacht.getSeil() != null) {
			try {
				aufzugschacht.getSeil().setNenndurchmesser((df_point.parse(durchmesserFormattedTextField.getText())).doubleValue());
				parameterChanged();
			} catch (ParseException e) {
				// auto-correction by FormattedTextField
			}
		}
	}// GEN-LAST:event_durchmesserFormattedTextFieldKeyReleased

	/**
	 * Seilanzahl formatted text field key released.
	 * 
	 * @param evt
	 *            the evt
	 */
	private void seilanzahlFormattedTextFieldKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_seilanzahlFormattedTextFieldKeyReleased
		if (aufzugschacht.getSeil() != null) {
			try {
				aufzugschacht.getSeil().setCount((df_nopoint.parse(seilanzahlFormattedTextField.getText())).intValue());
				berechnung.setNt(aufzugschacht.getAufhaengung());
				parameterChanged();
			} catch (ParseException e) {
				// auto-correction by FormattedTextField
			}
		}
	}// GEN-LAST:event_seilanzahlFormattedTextFieldKeyReleased

	/**
	 * Fn1 formatted text field key released.
	 * 
	 * @param evt
	 *            the evt
	 */
	private void fn1FormattedTextFieldKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_fn1FormattedTextFieldKeyReleased
		try {
			berechnung.setFn1((df_point.parse(fn1FormattedTextField.getText())).doubleValue());
			parameterChanged();
		} catch (ParseException e) {
			// auto-correction by FormattedTextField
		}
	}// GEN-LAST:event_fn1FormattedTextFieldKeyReleased

	/**
	 * Fs4 formatted text field key released.
	 * 
	 * @param evt
	 *            the evt
	 */
	private void fs4FormattedTextFieldKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_fs4FormattedTextFieldKeyReleased
		try {
			berechnung.setFs4((df_point.parse(fs4FormattedTextField.getText())).doubleValue());
			parameterChanged();
		} catch (ParseException e) {
			// auto-correction by FormattedTextField
		}
	}// GEN-LAST:event_fs4FormattedTextFieldKeyReleased

	/**
	 * Fs3 formatted text field key released.
	 * 
	 * @param evt
	 *            the evt
	 */
	private void fs3FormattedTextFieldKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_fs3FormattedTextFieldKeyReleased
		try {
			berechnung.setFs3((df_point.parse(fs3FormattedTextField.getText())).doubleValue());
			parameterChanged();
		} catch (ParseException e) {
			// auto-correction by FormattedTextField
		}
	}// GEN-LAST:event_fs3FormattedTextFieldKeyReleased

	/**
	 * Fs1 formatted text field key released.
	 * 
	 * @param evt
	 *            the evt
	 */
	private void fs1FormattedTextFieldKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_fs1FormattedTextFieldKeyReleased
		try {
			berechnung.setFs1((df_point.parse(fs1FormattedTextField.getText())).doubleValue());
			parameterChanged();
		} catch (ParseException e) {
			// auto-correction by FormattedTextField
		}
	}// GEN-LAST:event_fs1FormattedTextFieldKeyReleased

	/**
	 * Gewichtmasse formatted text field key released.
	 * 
	 * @param evt
	 *            the evt
	 */
	private void gewichtmasseFormattedTextFieldKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_gewichtmasseFormattedTextFieldKeyReleased
		try {
			if (aufzugschacht.getGewicht() != null) {
				aufzugschacht.getGewicht().setMass(df_nopoint.parse(gewichtmasseFormattedTextField.getText()).intValue());
				parameterChanged();
			}
		} catch (ParseException e) {
			// auto-corrected by FormattedTextField
		}
	}// GEN-LAST:event_gewichtmasseFormattedTextFieldKeyReleased

	/**
	 * Zuladungmasse formatted text field key released.
	 * 
	 * @param evt
	 *            the evt
	 */
	private void zuladungmasseFormattedTextFieldKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_zuladungmasseFormattedTextFieldKeyReleased
		try {
			if (aufzugschacht.getKabine() != null) {
				aufzugschacht.getKabine().setZuladung(df_nopoint.parse(zuladungmasseFormattedTextField.getText()).intValue());
				changeGewicht();
				parameterChanged();
			}
		} catch (ParseException e) {
			// auto-corrected by FormattedTextField
		}
	}// GEN-LAST:event_zuladungmasseFormattedTextFieldKeyReleased

	/**
	 * Kabinenmasse formatted text field key released.
	 * 
	 * @param evt
	 *            the evt
	 */
	private void kabinenmasseFormattedTextFieldKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_kabinenmasseFormattedTextFieldKeyReleased
		try {
			if (aufzugschacht.getKabine() != null) {
				aufzugschacht.getKabine().setMass(df_nopoint.parse(kabinenmasseFormattedTextField.getText()).intValue());
				// changeGewicht();
				parameterChanged();
			}
		} catch (ParseException e) {
			// auto-corrected by FormattedTextField
		}
	}// GEN-LAST:event_kabinenmasseFormattedTextFieldKeyReleased

	/**
	 * Calculate button action performed.
	 * 
	 * @param evt
	 *            the evt
	 */
	private void calculateButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_calculateButtonActionPerformed
		// if all values are set in 'Berechnung' then it could be calculated
		if (berechnung.inputOK()) {
			berechnenClicked = true;
			// errorLabel.setText("");
			long erg[][] = berechnung.calculate();
			setErgebnisTableValues(erg);
		} else {
			ErrorFrame errorFrame = new ErrorFrame(this,
					"Lebensdauer kann nicht berechnet werden, da mindestens eines der folgenden Elemente fehlt. Aufzugkonstruktion, Globale Parameter, Seiltypparameter."); //$NON-NLS-1$
			Helper.showFrame(errorFrame, this);
		}
	}// GEN-LAST:event_calculateButtonActionPerformed

	/**
	 * Form window state changed.
	 * 
	 * @param evt
	 *            the evt
	 */
	private void formWindowStateChanged(java.awt.event.WindowEvent evt) {// GEN-FIRST:event_formWindowStateChanged
		int state = evt.getNewState();
		if ((state & JFrame.NORMAL) != 0)
			formResized();
		if ((state & JFrame.MAXIMIZED_BOTH) != 0)
			formResized();
	}// GEN-LAST:event_formWindowStateChanged

	/**
	 * J menu item global action performed.
	 * 
	 * @param evt
	 *            the evt
	 */
	private void jMenuItemGlobalActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItemGlobalActionPerformed
		if (globalParametersFrame == null || !globalParametersFrame.isShowing()) {
			globalParametersFrame = new GlobalParametersFrame(this);
			Helper.showFrame(globalParametersFrame, this);
		}
	}// GEN-LAST:event_jMenuItemGlobalActionPerformed

	/**
	 * J menu item drucken action performed.
	 * 
	 * @param evt
	 *            the evt
	 */
	private void jMenuItemDruckenActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItemDruckenActionPerformed
		print();
	}// GEN-LAST:event_jMenuItemDruckenActionPerformed

	/**
	 * Seil toggle button mouse pressed.
	 * 
	 * @param evt
	 *            the evt
	 */
	private void seilToggleButtonMousePressed(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_seilToggleButtonMousePressed
		onlyOneButton(evt);
	}// GEN-LAST:event_seilToggleButtonMousePressed

	/**
	 * Gewicht toggle button mouse pressed.
	 * 
	 * @param evt
	 *            the evt
	 */
	private void gewichtToggleButtonMousePressed(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_gewichtToggleButtonMousePressed
		onlyOneButton(evt);
	}// GEN-LAST:event_gewichtToggleButtonMousePressed

	/**
	 * Kabine toggle button mouse pressed.
	 * 
	 * @param evt
	 *            the evt
	 */
	private void kabineToggleButtonMousePressed(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_kabineToggleButtonMousePressed
		onlyOneButton(evt);
	}// GEN-LAST:event_kabineToggleButtonMousePressed

	/**
	 * Umlenkrolle toggle button mouse pressed.
	 * 
	 * @param evt
	 *            the evt
	 */
	private void umlenkrolleToggleButtonMousePressed(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_umlenkrolleToggleButtonMousePressed
		onlyOneButton(evt);
	}// GEN-LAST:event_umlenkrolleToggleButtonMousePressed

	/**
	 * Treibscheibe toggle button mouse pressed.
	 * 
	 * @param evt
	 *            the evt
	 */
	private void treibscheibeToggleButtonMousePressed(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_treibscheibeToggleButtonMousePressed
		onlyOneButton(evt);
	}// GEN-LAST:event_treibscheibeToggleButtonMousePressed

	/**
	 * Triggered if zeichenPanel is resized.
	 * 
	 * @param evt
	 *            The ComponentEvent
	 */
	private void zeichenPanelComponentResized(java.awt.event.ComponentEvent evt) {// GEN-FIRST:event_zeichenPanelComponentResized
		if (aufzugschacht != null && !zeichenPanelThreadAlive) {
			aufzugschacht.setSize(zeichenPanel.getSize());
			aufzugschacht.setLocation(0, 0);
		}
	}// GEN-LAST:event_zeichenPanelComponentResized

	/**
	 * Creates the the elevator in zeichenPanel.
	 */
	public void createAufzugschacht() {
		LoadFileDialog loadFileDialog = new LoadFileDialog(this, true);
		Helper.showDialog(loadFileDialog, this);
		if (!loadFile) {
			SchachtFrame frame = new SchachtFrame(this);
			Helper.showFrame(frame, this);
			zeichenPanelThread = new ZeichenPanelReadyThread(zeichenPanel, aufzugschacht, this);
			zeichenPanelThread.start();
		} else {
			SmartFileChooser fileChooser = new SmartFileChooser();
			fileChooser.setDialogTitle("" + Constants.OE_G + "ffnen"); //$NON-NLS-1$ //$NON-NLS-2$
			// fileChooser.setAcceptAllFileFilterUsed(false);
			fileChooser.setFileFilter(new GenericFileFilter("*.alb (Aufzugseillebensdauerberechnung)", "alb")); //$NON-NLS-1$ //$NON-NLS-2$
			int state = fileChooser.showOpenDialog(this);
			if (state == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				Constants.LOGGER.log(Level.INFO, file.getName() + " loaded for open"); //$NON-NLS-1$
				loadFileIntoAufzugschacht(file);
				aufzugschachtFile = file;
				setEnabled(true);
				setVisible(true);
			} else {
				Constants.LOGGER.log(Level.INFO, "Selection canceled"); //$NON-NLS-1$
				SchachtFrame frame = new SchachtFrame(this);
				Helper.showFrame(frame, this);
				zeichenPanelThread = new ZeichenPanelReadyThread(zeichenPanel, aufzugschacht, this);
				zeichenPanelThread.start();
			}
		}
	}

	/**
	 * Triggered if the main frame is resized.
	 * 
	 * @param evt
	 *            The ComponentEvent
	 */
	private void formComponentResized(java.awt.event.ComponentEvent evt) {// GEN-FIRST:event_formComponentResized
		formResized();
	}// GEN-LAST:event_formComponentResized

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JFormattedTextField aufhaengungFormattedTextField;
	private javax.swing.JFormattedTextField biegelaengeFormattedTextField;
	private javax.swing.JButton calculateButton;
	private javax.swing.JButton chooseSeiltypButton;
	private javax.swing.JFormattedTextField durchmesserFormattedTextField;
	private javax.swing.JFormattedTextField durchschnittFormattedTextField;
	private javax.swing.JScrollPane einzelErgebnisTableScrollPane;
	private javax.swing.JLabel elementeLabel;
	private javax.swing.JPanel elementePanel;
	private javax.swing.JLabel ergebnisLabel;
	private javax.swing.JPanel ergebnisPanel;
	private javax.swing.JScrollPane ergebnisTableScrollPane;
	private javax.swing.JScrollPane ergebnisseScrollPane;
	private javax.swing.JFormattedTextField fn1FormattedTextField;
	private javax.swing.JFormattedTextField fs1FormattedTextField;
	private javax.swing.JFormattedTextField fs3FormattedTextField;
	private javax.swing.JFormattedTextField fs4FormattedTextField;
	private javax.swing.JFormattedTextField gegenanteilFormattedTextField;
	private javax.swing.JLabel gewichtLabel;
	private javax.swing.JPanel gewichtPanel;
	private javax.swing.JToggleButton gewichtToggleButton;
	private javax.swing.JFormattedTextField gewichtmasseFormattedTextField;
	private javax.swing.JLabel grafikLabel;
	private javax.swing.JPanel grafikPanel;
	private javax.swing.JSplitPane horizSplitPane;
	private javax.swing.JPanel iconsPanel;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel10;
	private javax.swing.JLabel jLabel11;
	private javax.swing.JLabel jLabel12;
	private javax.swing.JLabel jLabel13;
	private javax.swing.JLabel jLabel14;
	private javax.swing.JLabel jLabel15;
	private javax.swing.JLabel jLabel16;
	private javax.swing.JLabel jLabel17;
	private javax.swing.JLabel jLabel18;
	private javax.swing.JLabel jLabel19;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel20;
	private javax.swing.JLabel jLabel21;
	private javax.swing.JLabel jLabel22;
	private javax.swing.JLabel jLabel23;
	private javax.swing.JLabel jLabel24;
	private javax.swing.JLabel jLabel25;
	private javax.swing.JLabel jLabel26;
	private javax.swing.JLabel jLabel27;
	private javax.swing.JLabel jLabel28;
	private javax.swing.JLabel jLabel29;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JLabel jLabel30;
	private javax.swing.JLabel jLabel31;
	private javax.swing.JLabel jLabel32;
	private javax.swing.JLabel jLabel33;
	private javax.swing.JLabel jLabel34;
	private javax.swing.JLabel jLabel35;
	private javax.swing.JLabel jLabel4;
	private javax.swing.JLabel jLabel5;
	private javax.swing.JLabel jLabel6;
	private javax.swing.JLabel jLabel7;
	private javax.swing.JLabel jLabel8;
	private javax.swing.JLabel jLabel9;
	private javax.swing.JMenuBar jMenuBar;
	private javax.swing.JMenu jMenuDatei;
	private javax.swing.JMenu jMenuExtras;
	private javax.swing.JMenu jMenuHilfe;
	private javax.swing.JMenuItem jMenuItemBeenden;
	private javax.swing.JMenuItem jMenuItemDrucken;
	private javax.swing.JMenuItem jMenuItemGlobal;
	private javax.swing.JMenuItem jMenuItemHilfe;
	private javax.swing.JMenuItem jMenuItemInfo;
	private javax.swing.JMenuItem jMenuItemLadeSeiltypen;
	private javax.swing.JMenuItem jMenuItemLogView;
	private javax.swing.JMenuItem jMenuItemNeu;
	private javax.swing.JMenuItem jMenuItemOeffnen;
	private javax.swing.JMenuItem jMenuItemSchacht;
	private javax.swing.JMenuItem jMenuItemSeil;
	private javax.swing.JMenuItem jMenuItemSicherungSeiltypen;
	private javax.swing.JMenuItem jMenuItemSpeichern;
	private javax.swing.JMenuItem jMenuItemSpeichernAls;
	private javax.swing.JMenu jMenuParameter;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel2;
	private javax.swing.JPanel jPanel3;
	private javax.swing.JPanel jPanel4;
	private javax.swing.JSeparator jSeparator1;
	private javax.swing.JSeparator jSeparator2;
	private javax.swing.JLabel kabineLabel;
	private javax.swing.JPanel kabinePanel;
	private javax.swing.JToggleButton kabineToggleButton;
	private javax.swing.JFormattedTextField kabinenmasseFormattedTextField;
	private javax.swing.JFormattedTextField massUnterseilFormattedTextField;
	private javax.swing.JPanel parameterPanel;
	private javax.swing.JScrollPane parameterScrollPane;
	private javax.swing.JFormattedTextField profilFormattedTextField;
	private javax.swing.JTabbedPane rollenTabbedPane;
	private javax.swing.JLabel seilLabel;
	private javax.swing.JPanel seilPanel;
	private javax.swing.JToggleButton seilToggleButton;
	private javax.swing.JFormattedTextField seilanzahlFormattedTextField;
	private javax.swing.JLabel seiltypLabel;
	private javax.swing.JFormattedTextField tolleranzFormattedTextField;
	private javax.swing.JButton treibfaehigkeitButton;
	private javax.swing.JLabel treibscheibeLabel;
	private javax.swing.JPanel treibscheibePanel;
	private javax.swing.JToggleButton treibscheibeToggleButton;
	private javax.swing.JLabel umlenkrolleLabel;
	private javax.swing.JPanel umlenkrollePanel;
	private javax.swing.JToggleButton umlenkrolleToggleButton;
	private javax.swing.JFormattedTextField umschlWinkelFormattedTextField;
	private javax.swing.JButton unterseilButton;
	private javax.swing.JLabel unterseilLabel;
	private javax.swing.JPanel unterseilPanel;
	private javax.swing.JSplitPane vertSplitPane;
	private javax.swing.JPanel zeichenPanel;
	private javax.swing.JButton zickzackButton;
	private javax.swing.JLabel zickzackLabel;
	private javax.swing.JPanel zickzackPanel;
	private javax.swing.JFormattedTextField zuladungmasseFormattedTextField;

	// End of variables declaration//GEN-END:variables

	/**
	 * Triggered if the main form is resized.
	 */
	private void formResized() {
		goldenerSchnitt();
	}

	/**
	 * Change the conter weight mass, with is calculated with the cabine mass.
	 */
	private void changeGewicht() {
		if (aufzugschacht.getGewicht() != null) {
			// cabine mass + half of the maximum payload of the cabine
			int mass = aufzugschacht.getKabine().getMass() + (int) (aufzugschacht.getGewicht().getPart() * aufzugschacht.getKabine().getZuladung());
			aufzugschacht.getGewicht().setMass(mass);
			gewichtmasseFormattedTextField.setText(Integer.toString(mass));
		}
	}

	/**
	 * Adds the zickzack: the divider.
	 */
	private void addZickzack() {
		if (aufzugschacht != null) {
			aufzugschacht.addZickzack();
			zickzackButton.setEnabled(false);
		}
	}

	/**
	 * Adds the unterseil. Only possible, if the whole elevator is build.
	 */
	private void addUnterseil() {
		if (aufzugschacht != null && aufzugschacht.getTreibscheibe() != null && aufzugschacht.getKabine() != null && aufzugschacht.getGewicht() != null
				&& aufzugschacht.getSeil() != null && aufzugschacht.getUnterseil() == null) {
			aufzugschacht.addUnterseil();
			unterseilButton.setEnabled(false);
		}
	}

	/**
	 * Goldener schnitt. Divide the main frame into parts of different percentage
	 */
	private void goldenerSchnitt() {
		horizSplitPane.setDividerLocation(0.66);
		vertSplitPane.setDividerLocation(0.787);
		grafikPanel.setSize((int) (horizSplitPane.getSize().width * 0.66), horizSplitPane.getSize().height);
	}

	/**
	 * Sets the name and icons of the buttons and menu items.
	 */
	private void registerButtons() {
		treibscheibeToggleButton.setName(Constants.TREIBSCHEIBE_NAME);
		treibscheibeToggleButton.setIcon(new ImageIcon(Helper.getFileURL(Constants.TREIBSCHEIBE_BUTTON_ICON)));
		umlenkrolleToggleButton.setName(Constants.UMLENKROLLE_NAME);
		umlenkrolleToggleButton.setIcon(new ImageIcon(Helper.getFileURL(Constants.UMLENKROLLE_BUTTON_ICON)));
		kabineToggleButton.setName(Constants.KABINE_NAME);
		kabineToggleButton.setIcon(new ImageIcon(Helper.getFileURL(Constants.KABINE_BUTTON_ICON)));
		gewichtToggleButton.setName(Constants.GEWICHT_NAME);
		gewichtToggleButton.setIcon(new ImageIcon(Helper.getFileURL(Constants.GEWICHT_BUTTON_ICON)));
		seilToggleButton.setName(Constants.SEIL_NAME);
		seilToggleButton.setIcon(new ImageIcon(Helper.getFileURL(Constants.SEIL_BUTTON_ICON)));
		unterseilButton.setName(Constants.UNTERSEIL_NAME);
		unterseilButton.setIcon(new ImageIcon(Helper.getFileURL(Constants.UNTERSEIL_BUTTON_ICON)));
		zickzackButton.setName(Constants.ZICKZACK_NAME);
		zickzackButton.setIcon(new ImageIcon(Helper.getFileURL(Constants.ZICKZACK_BUTTON_ICON)));

		buttonVector.add(treibscheibeToggleButton);
		buttonVector.add(umlenkrolleToggleButton);
		buttonVector.add(kabineToggleButton);
		buttonVector.add(gewichtToggleButton);
		buttonVector.add(seilToggleButton);
		buttonVector.add(zickzackButton);
		buttonVector.add(unterseilButton);
	}

	/**
	 * Load menu icon images.
	 */
	private void loadMenuIconImages() {
		jMenuItemBeenden.setIcon(new ImageIcon(Helper.getFileURL(Constants.MENU_QUIT_ICON)));
		jMenuItemDrucken.setIcon(new ImageIcon(Helper.getFileURL(Constants.MENU_PRINT_ICON)));
		jMenuItemGlobal.setIcon(new ImageIcon(Helper.getFileURL(Constants.MENU_GLOBAL_ICON)));
		jMenuItemHilfe.setIcon(new ImageIcon(Helper.getFileURL(Constants.MENU_HELP_ICON)));
		jMenuItemInfo.setIcon(new ImageIcon(Helper.getFileURL(Constants.MENU_INFO_ICON)));
		jMenuItemNeu.setIcon(new ImageIcon(Helper.getFileURL(Constants.MENU_NEW_ICON)));
		jMenuItemOeffnen.setIcon(new ImageIcon(Helper.getFileURL(Constants.MENU_OPEN_ICON)));
		jMenuItemSchacht.setIcon(new ImageIcon(Helper.getFileURL(Constants.MENU_ELEVATOR_ICON)));
		jMenuItemSeil.setIcon(new ImageIcon(Helper.getFileURL(Constants.MENU_ROPE_ICON)));
		jMenuItemSpeichern.setIcon(new ImageIcon(Helper.getFileURL(Constants.MENU_SAVE_ICON)));
		jMenuItemSpeichernAls.setIcon(new ImageIcon(Helper.getFileURL(Constants.MENU_SAVEAS_ICON)));
		jMenuItemSicherungSeiltypen.setIcon(new ImageIcon(Helper.getFileURL(Constants.MENU_SAVEAS_ICON)));
		jMenuItemLadeSeiltypen.setIcon(new ImageIcon(Helper.getFileURL(Constants.MENU_OPEN_ICON)));
		jMenuItemLogView.setIcon(new ImageIcon(Helper.getFileURL(Constants.MENU_LOGVIEW_ICON)));
	}

	/**
	 * Changes the property 'enabled' of the button with the name 'name' to the given value.
	 * 
	 * @param name
	 *            The name of the button
	 * @param value
	 *            The value of the changing
	 */
	public void changeButton(String name, boolean value) {
		if (name.equals(Constants.TREIBSCHEIBE_NAME)) {
			treibscheibeToggleButton.setEnabled(value);
			treibscheibeToggleButton.setSelected(false);
		}
		if (name.equals(Constants.UMLENKROLLE_NAME)) {
			umlenkrolleToggleButton.setEnabled(value);
			umlenkrolleToggleButton.setSelected(false);
		}
		if (name.equals(Constants.KABINE_NAME)) {
			kabineToggleButton.setEnabled(value);
			kabineToggleButton.setSelected(false);
		}
		if (name.equals(Constants.GEWICHT_NAME)) {
			gewichtToggleButton.setEnabled(value);
			gewichtToggleButton.setSelected(false);
		}
		if (name.equals(Constants.SEIL_NAME)) {
			seilToggleButton.setEnabled(value);
			seilToggleButton.setSelected(false);
		}
		aufzugschacht.setMainFrameButtonSelected(name, false);
		if (isAnyButtonSelected()) {
			aufzugschacht.setMainFrameIsAnyButtonSelected(true);
		} else {
			aufzugschacht.setMainFrameIsAnyButtonSelected(false);
		}
		if (name.equals(Constants.ZICKZACK_NAME)) {
			zickzackButton.setEnabled(value);
			zickzackButton.setSelected(false);
		}
		if (name.equals(Constants.UNTERSEIL_NAME)) {
			unterseilButton.setEnabled(value);
			unterseilButton.setSelected(false);
		}
	}

	/**
	 * Makes that only one button is selected.
	 * 
	 * @param evt
	 *            The MouseEvent
	 */
	private void onlyOneButton(MouseEvent evt) {
		JToggleButton button = (JToggleButton) evt.getComponent();
		for (int i = 0; i < buttonVector.size(); i++) {
			if (!buttonVector.get(i).equals(button)) {
				buttonVector.get(i).setSelected(false);
				aufzugschacht.setMainFrameButtonSelected(buttonVector.get(i).getName(), false);
				if (isAnyButtonSelected()) {
					aufzugschacht.setMainFrameIsAnyButtonSelected(true);
				} else {
					aufzugschacht.setMainFrameIsAnyButtonSelected(false);
				}
			}
		}
	}

	/**
	 * Checks if any button is selected.
	 * 
	 * @return True if one button is selected
	 */
	private boolean isAnyButtonSelected() {
		for (int i = 0; i < buttonVector.size(); i++) {
			if (buttonVector.elementAt(i).isSelected())
				return true;
		}
		return false;
	}

	/**
	 * Deselects all buttons from the buttonVector.
	 */
	public void deselectAllButtons() {
		for (int i = 0; i < buttonVector.size(); i++) {
			buttonVector.elementAt(i).setSelected(false);
			aufzugschacht.setMainFrameButtonSelected(buttonVector.elementAt(i).getName(), false);
			aufzugschacht.setMainFrameIsAnyButtonSelected(false);
		}
	}

	/**
	 * Enable all buttons.
	 */
	public void enableAllButtons() {
		for (int i = 0; i < buttonVector.size(); i++) {
			buttonVector.elementAt(i).setEnabled(true);
			buttonVector.elementAt(i).setSelected(false);
			aufzugschacht.setMainFrameButtonSelected(buttonVector.elementAt(i).getName(), false);
			aufzugschacht.setMainFrameIsAnyButtonSelected(false);
		}
	}

	/**
	 * Print.
	 */
	private void print() {
		PrinterJob job = PrinterJob.getPrinterJob();
		PageFormat pageFormat = job.defaultPage();
		pageFormat.setOrientation(PageFormat.PORTRAIT);
		Paper paper = new Paper();

		// default: 72, 72, 451.27xx, 697.88xx
		// Set to A4 size.
		paper.setSize(594.936, 841.536);
		// Set the margins.
		paper.setImageableArea(20, 0, 594.936, 841.536);
		pageFormat.setPaper(paper);
		Book bk = new Book();

		// main print frame: with the elevator, parameters and solution
		PrintFrameMain printFrame = new PrintFrameMain();
		printFrame.insertAufzugschacht(aufzugschacht, this);
		printFrame.setVisible(true);
		printFrame.setExtendedState(ICONIFIED);
		bk.append(printFrame, pageFormat);

		// the feyrer parameters of the used ropes
		PrintFrameFeyrer printFrameFeyrer = new PrintFrameFeyrer();
		printFrameFeyrer.insertUsedFeyrerParameters(this);
		if (getBerechnung().getCalcRolls().size() > 0)
			printFrameFeyrer.createEinzelErgebnisTables(this);
		printFrameFeyrer.setVisible(true);
		printFrameFeyrer.setExtendedState(ICONIFIED);
		bk.append(printFrameFeyrer, pageFormat);

		// show the printing dialog
		PrintDialog printDialog = new PrintDialog(this, bk, job);
		Helper.showFrame(printDialog, this);
	}

	/**
	 * Prints the now.
	 * 
	 * @param bk
	 *            the bk
	 * @param job
	 *            the job
	 * @param frame1
	 *            the frame1
	 * @param frame2
	 *            the frame2
	 */
	private void printNow(Book bk, PrinterJob job, Frame frame1, Frame frame2) {
		job.setPageable(bk);
		if (job.printDialog()) {
			try {
				job.print();
			} catch (PrinterException e) {
				e.printStackTrace();
			}
		}
		// close print frames after success or no success
		frame1.dispose();
		frame2.dispose();
	}

	/**
	 * Sets the parameters.
	 * 
	 * @param fs4
	 *            the fs4
	 * @param fs3
	 *            the fs3
	 * @param fs1
	 *            the fs1
	 * @param fn1
	 *            the fn1
	 */
	public void setParameters(double fs1, double fs3, double fs4, double fn1, double toleranz) {
		fs1FormattedTextField.setText(df_point.format(fs1));
		fs3FormattedTextField.setText(df_point.format(fs3));
		fs4FormattedTextField.setText(df_point.format(fs4));
		fn1FormattedTextField.setText(df_point.format(fn1));
		tolleranzFormattedTextField.setText(df_nopoint.format(toleranz * 100));

		berechnung.setFs1(fs1);
		berechnung.setFs3(fs3);
		berechnung.setFs4(fs4);
		berechnung.setFn1(fn1);
		berechnung.setToleranz(toleranz);
	}

	/**
	 * Sets the zuladung.
	 * 
	 * @param zuladung
	 *            the zuladung
	 */
	public void setZuladung(int zuladung) {
		zuladungmasseFormattedTextField.setText(df_nopoint.format(zuladung));
	}

	/**
	 * Sets the mass of the cabine or the weight.
	 * 
	 * @param mass
	 *            the mass
	 */
	public void setMassKabine(int mass) {
		kabinenmasseFormattedTextField.setText(df_nopoint.format(mass));
	}

	/**
	 * Sets the mass of the cabine or the weight.
	 * 
	 * @param mass
	 *            the mass
	 * @param part
	 *            the part
	 */
	public void setMassGewicht(int mass, int part) {
		gewichtmasseFormattedTextField.setText(df_nopoint.format(mass));
		gegenanteilFormattedTextField.setText(df_nopoint.format(part));
	}

	/**
	 * Sets the average load.
	 * 
	 * @param durchschnitt
	 *            the average load in percent (0 - 1)
	 */
	public void setDurchschnitt(double durchschnitt) {
		durchschnittFormattedTextField.setText(df_nopoint.format(durchschnitt * 100));
		if (aufzugschacht.getGewicht() != null) {
			long mass = aufzugschacht.getKabine().getMass() + Math.round(aufzugschacht.getKabine().getZuladung() * aufzugschacht.getGewicht().getPart());
			if (mass > Integer.MAX_VALUE) {
				Constants.LOGGER.log(Level.SEVERE, "Parsing from long to int can't be done correct! Value is too large!"); //$NON-NLS-1$
			}
			aufzugschacht.getGewicht().setMass((int) mass);
			gewichtmasseFormattedTextField.setText(df_nopoint.format(mass));
		}
		kabinenmasseFormattedTextField.setText(df_nopoint.format(aufzugschacht.getKabine().getMass()));
	}

	/**
	 * Sets the profile.
	 * 
	 * @param profil
	 *            the profil
	 */
	public void setProfil(double profil) {
		profilFormattedTextField.setText(df_nopoint.format(profil * 100));
	}

	/**
	 * Gets the calculation class.
	 * 
	 * @return the calculation class
	 */
	public Berechnung getBerechnung() {
		return berechnung;
	}

	/**
	 * New tabbed pane.
	 * 
	 * @param rolle
	 *            the rolle
	 */
	public void newTabbedPane(Rolle rolle) {
		JPanel panel = new RollenParameterPanel(rolle, this);
		rollenTabbedPane.add(rolle.getName() + rolle.getID(), panel);
	}

	/**
	 * Delete tabbed pane.
	 * 
	 * @param rolle
	 *            the rolle
	 */
	public void deleteTabbedPane(Rolle rolle) {
		for (int i = 0; i < rollenTabbedPane.getTabCount(); i++) {
			if (((RollenParameterPanel) rollenTabbedPane.getComponentAt(i)).getRolle().equals(rolle)) {
				rollenTabbedPane.remove(i);
			}
		}
	}

	/**
	 * Deletes all tabs of the tabbed pane for rolls.
	 */
	public void deleteAllTabbedPane() {
		rollenTabbedPane.removeAll();
	}

	/**
	 * Activate tabbed pane.
	 * 
	 * @param rolle
	 *            the rolle
	 */
	public void activateTabbedPane(Rolle rolle) {
		for (int i = 0; i < rollenTabbedPane.getTabCount(); i++) {
			if (((RollenParameterPanel) rollenTabbedPane.getComponentAt(i)).getRolle().equals(rolle)) {
				rollenTabbedPane.setSelectedIndex(i);
			}
		}
	}

	/**
	 * Sets the aufhaengung.
	 * 
	 * @param aufhaengung
	 *            the aufhaengung
	 */
	public void setAufhaengung(int aufhaengung) {
		if (aufhaengung == 1) {
			aufhaengungFormattedTextField.setText("1 : 1"); //$NON-NLS-1$
		} else {
			aufhaengungFormattedTextField.setText("2 : 1"); //$NON-NLS-1$
		}
		berechnung.setNt(aufhaengung);
	}

	/**
	 * Refresh seil.
	 * 
	 * @param seil
	 *            the seil
	 */
	public void refreshSeil(Seil seil) {
		seilanzahlFormattedTextField.setText(df_nopoint.format(seil.getCount()));
		// festigkeitFormattedTextField.setText(df_nopoint.format(seil.getR0()));
		durchmesserFormattedTextField.setText(df_nopoint.format(seil.getD()));
		biegelaengeFormattedTextField.setText(df_nopoint.format(seil.getL()));
		setAufhaengung(aufzugschacht.getAufhaengung());
	}

	/**
	 * Refresh tabbed pane.
	 * 
	 * @param rolle
	 *            the rolle
	 */
	public void refreshTabbedPane(Rolle rolle) {
		for (int i = 0; i < rollenTabbedPane.getTabCount(); i++) {
			if (((RollenParameterPanel) rollenTabbedPane.getComponentAt(i)).getRolle().equals(rolle)) {
				((RollenParameterPanel) rollenTabbedPane.getComponentAt(i)).getDurchmesserFormattedTextField().setText(
						df_nopoint.format(rolle.getDurchmesser()));
				((RollenParameterPanel) rollenTabbedPane.getComponentAt(i)).getSeiltriebwirkungsgradFormattedTextField().setText(
						df_point.format(rolle.getFs2()));
				((RollenParameterPanel) rollenTabbedPane.getComponentAt(i)).getSchraegzugFormattedTextField().setText(df_point.format(rolle.getFn4()));
				((RollenParameterPanel) rollenTabbedPane.getComponentAt(i)).getSeilrilleFormattedTextField().setText(df_point.format(rolle.getFn3()));
				((RollenParameterPanel) rollenTabbedPane.getComponentAt(i)).getGegenbiegungCheckBox().setSelected(rolle.isGegenbiegung());
			}
		}
		parameterChanged();
	}

	/**
	 * Creates the ergebnis table.
	 */
	@SuppressWarnings( { "serial", "unchecked" })//$NON-NLS-1$
	public void createErgebnisTable() {
		// set rope values
		Vector<String> nameVector = new Vector<String>();
		Vector<Class> typeVector = new Vector<Class>();
		Vector<Boolean> editVector = new Vector<Boolean>();

		// create name, type and edit vector to create the table
		nameVector.add("Werte"); //$NON-NLS-1$
		typeVector.add(String.class);
		editVector.add(false);

		// reset the calculation
		berechnung.resetBsFn2MassFestigkeit();

		// read all values from the parameter table an set the calculation array in the calculation
		for (int i = 0; i < parameterTableModel.getRowCount(); i++) {
			if ((Boolean) parameterTableModel.getValueAt(i, 0) != null && (Boolean) parameterTableModel.getValueAt(i, 0) == true) {
				if (parameterTableModel.getValueAt(i, 1) != null && parameterTableModel.getValueAt(i, 2) != null
						&& parameterTableModel.getValueAt(i, 3) != null && parameterTableModel.getValueAt(i, 4) != null
						&& parameterTableModel.getValueAt(i, 5) != null && parameterTableModel.getValueAt(i, 6) != null
						&& parameterTableModel.getValueAt(i, 7) != null && parameterTableModel.getValueAt(i, 8) != null
						&& parameterTableModel.getValueAt(i, 9) != null && parameterTableModel.getValueAt(i, 10) != null
						&& parameterTableModel.getValueAt(i, 11) != null && parameterTableModel.getValueAt(i, 12) != null) {
					double bsFn2MassFestigkeit[] = new double[11];
					for (int j = 2; j < parameterTableModel.getColumnCount(); j++) {
						bsFn2MassFestigkeit[j - 2] = (Double) parameterTableModel.getValueAt(i, j);
					}
					berechnung.addBsFn2MassFestigkeit(bsFn2MassFestigkeit);
					nameVector.add((String) parameterTableModel.getValueAt(i, 1));
					typeVector.add(LongGrouping.class);
					editVector.add(false);
				} else {
					Constants.LOGGER.log(Level.WARNING, "Partly chooing empty rows. Ignoring those rows."); //$NON-NLS-1$
				}
			}
		}

		// now create the table
		String nameArray[] = new String[nameVector.size()];
		nameVector.copyInto(nameArray);
		final Class typeArray[] = new Class[typeVector.size()];
		typeVector.copyInto(typeArray);
		final Boolean editArray[] = new Boolean[editVector.size()];
		editVector.copyInto(editArray);

		ergebnisTable = new JTable();
		ergebnisTable.setModel(new DefaultTableModel(null, nameArray) {
			Class[] types = typeArray;

			Boolean[] canEdit = editArray;

			public Class getColumnClass(int columnIndex) {
				return types[columnIndex];
			}

			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return canEdit[columnIndex];
			}
		});

		// sets attributes of the table
		ergebnisTable.getTableHeader().setReorderingAllowed(false);
		ergebnisTable.setRowSelectionAllowed(false);
		for (int i = 0; i < ergebnisTable.getColumnCount(); i++) {
			ergebnisTable.getColumnModel().getColumn(i).setPreferredWidth(100);
		}
		ergebnisTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		ergebnisTableScrollPane.setViewportView(ergebnisTable);

		setIndex(0);
	}

	/**
	 * Creates the einzel ergebnis table. Same as the table creation above
	 */
	@SuppressWarnings( { "serial", "unchecked" })//$NON-NLS-1$
	private void createEinzelErgebnisTable() {
		Vector<String> nameVector = new Vector<String>();
		Vector<Class> typeVector = new Vector<Class>();
		Vector<Boolean> editVector = new Vector<Boolean>();
		nameVector.add("Werte"); //$NON-NLS-1$
		typeVector.add(String.class);
		editVector.add(false);
		for (int i = 0; i < berechnung.getCalcRolls().size(); i++) {
			CalcRolle rolle = berechnung.getCalcRolls().get(i);
			nameVector.add(rolle.getName());
			typeVector.add(LongGrouping.class);
			editVector.add(false);
		}
		String nameArray[] = new String[nameVector.size()];
		nameVector.copyInto(nameArray);
		final Class typeArray[] = new Class[typeVector.size()];
		typeVector.copyInto(typeArray);
		final Boolean editArray[] = new Boolean[editVector.size()];
		editVector.copyInto(editArray);

		einzelErgebnisTable = new JTable();
		einzelErgebnisTable.setModel(new DefaultTableModel(null, nameArray) {
			Class[] types = typeArray;

			Boolean[] canEdit = editArray;

			public Class getColumnClass(int columnIndex) {
				return types[columnIndex];
			}

			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return canEdit[columnIndex];
			}
		});
		einzelErgebnisTable.getTableHeader().setReorderingAllowed(false);
		einzelErgebnisTable.setRowSelectionAllowed(false);
		for (int i = 0; i < einzelErgebnisTable.getColumnCount(); i++) {
			einzelErgebnisTable.getColumnModel().getColumn(i).setPreferredWidth(100);
		}
		einzelErgebnisTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		einzelErgebnisTableScrollPane.setViewportView(einzelErgebnisTable);
		setEinzelErgebnisTableValues(berechnung.getCalcRolls(), index);
	}

	/**
	 * Sets the index.
	 * 
	 * @param index
	 *            the index
	 */
	public void setIndex(int index) {
		this.index = index;
		seiltypLabel.setText(">>" + (String) parameterTableModel.getValueAt(index, 1) + "<<"); //$NON-NLS-1$ //$NON-NLS-2$
		parameterChanged();
	}

	/**
	 * Sets the einzel ergebnis table values.
	 * 
	 * @param calcRolle
	 *            the einzel ergebnis table values
	 */
	@SuppressWarnings("unchecked")//$NON-NLS-1$
	private void setEinzelErgebnisTableValues(Vector<CalcRolle> calcRolle, int index) {
		DefaultTableModel einzelErgebnisTableModel = (DefaultTableModel) einzelErgebnisTable.getModel();

		// reset einzelErgebnis table
		for (int i = einzelErgebnisTableModel.getRowCount() - 1; i >= 0; i--) {
			einzelErgebnisTableModel.removeRow(i);
		}

		// fill with solution array
		for (int j = 0; j < calcRolle.get(0).getLebensdauer().length; j++) {
			Vector rowData = new Vector();
			if (j == 0)
				rowData.add("N A"); //$NON-NLS-1$
			if (j == 1)
				rowData.add("N A10"); //$NON-NLS-1$
			for (int i = 0; i < calcRolle.size(); i++) {
				rowData.add(new LongGrouping(calcRolle.get(i).getLebensdauer()[j][index], true));
			}
			einzelErgebnisTableModel.addRow(rowData);
		}
	}

	/**
	 * Sets the ergebnis table values.
	 * 
	 * @param ergArray
	 *            the ergebnis table values
	 */
	@SuppressWarnings("unchecked")//$NON-NLS-1$
	private void setErgebnisTableValues(long ergArray[][]) {
		DefaultTableModel ergebnisTableModel = (DefaultTableModel) ergebnisTable.getModel();

		// reset ergebnis table
		for (int i = ergebnisTableModel.getRowCount() - 1; i >= 0; i--) {
			ergebnisTableModel.removeRow(i);
		}

		// fill with solution array
		for (int j = 0; j < ergArray.length; j++) {
			Vector rowData = new Vector();
			if (j == 0)
				rowData.add("N A"); //$NON-NLS-1$
			if (j == 1)
				rowData.add("N A10"); //$NON-NLS-1$
			for (int i = 0; i < ergArray[j].length; i++) {
				rowData.add(new LongGrouping(ergArray[j][i], true));
			}
			ergebnisTableModel.addRow(rowData);
		}
		Vector rowData = new Vector();
		rowData.add("Seilmasse (kg)"); //$NON-NLS-1$
		for (int r = 0; r < ergArray[0].length; r++) {
			if (aufzugschacht.getSeil() != null) {
				double seilmasse = (berechnung.getBs().get(r)[9] / 100) * aufzugschacht.getHoehe() * Math.pow(aufzugschacht.getSeil().getD(), 2)
						* aufzugschacht.getSeil().getCount();
				rowData.add(df_point.format(seilmasse));
				globalSeilmasse = berechnung.getBs().get(0)[9] * Math.pow(aufzugschacht.getSeil().getD(), 2);
			}
		}
		ergebnisTableModel.addRow(rowData);
		createEinzelErgebnisTable();
	}

	/**
	 * Invoked if parameter was changed. Then the calulation will be newly calculated
	 */
	public void parameterChanged() {
		if (berechnenClicked && berechnung.inputOK()) {
			long erg[][] = berechnung.calculate();
			setErgebnisTableValues(erg);
		}
	}

	/**
	 * Gets the fn1 formatted text field.
	 * 
	 * @return the fn1FormattedTextField
	 */
	public javax.swing.JFormattedTextField getFn1FormattedTextField() {
		return fn1FormattedTextField;
	}

	/**
	 * Gets the fs1 formatted text field.
	 * 
	 * @return the fs1FormattedTextField
	 */
	public javax.swing.JFormattedTextField getFs1FormattedTextField() {
		return fs1FormattedTextField;
	}

	/**
	 * Gets the fs3 formatted text field.
	 * 
	 * @return the fs3FormattedTextField
	 */
	public javax.swing.JFormattedTextField getFs3FormattedTextField() {
		return fs3FormattedTextField;
	}

	/**
	 * Gets the fs4 formatted text field.
	 * 
	 * @return the fs4FormattedTextField
	 */
	public javax.swing.JFormattedTextField getFs4FormattedTextField() {
		return fs4FormattedTextField;
	}

	/**
	 * Sets the parameter table model.
	 * 
	 * @param parameterTableModel
	 *            the parameter table model
	 */
	public void setParameterTableModel(TableModel parameterTableModel) {
		this.parameterTableModel = parameterTableModel;
	}

	/**
	 * File open.
	 */
	private void fileOpen() {
		DialogFrame frame = new DialogFrame(this, "M" + Constants.OE_K + "chten Sie die aktuelle Datei speichern?", "Oeffnen"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		Helper.showFrame(frame, this);
	}

	/**
	 * File save.
	 * 
	 * @return true, if file save
	 */
	private boolean fileSave() {
		if (aufzugschachtFile != null) {
			saveAufzugschachtIntoFile(aufzugschachtFile);
			return true;
		} else {
			return fileSaveAs();
		}
	}

	/**
	 * File save as.
	 * 
	 * @return true, if file save as
	 */
	private boolean fileSaveAs() {
		// create a file chooser to choose the file to save
		SmartFileChooser fileChooser = new SmartFileChooser();
		fileChooser.setDialogTitle("Speichern als ..."); //$NON-NLS-1$
		// fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setSelectedFile(new File("unbekannt.alb")); //$NON-NLS-1$
		fileChooser.setFileFilter(new GenericFileFilter("*.alb (Aufzugseillebensdauerberechnung)", "alb")); //$NON-NLS-1$ //$NON-NLS-2$
		int state = fileChooser.showSaveDialog(this);
		if (state == JFileChooser.APPROVE_OPTION) {
			// if OK was clicked -> save it
			File file = fileChooser.getSelectedFile();
			Constants.LOGGER.log(Level.INFO, file.getName() + " loaded for save"); //$NON-NLS-1$
			saveAufzugschachtIntoFile(file);
			aufzugschachtFile = file;
			return true;
		} else {
			Constants.LOGGER.log(Level.INFO, "Selection canceled"); //$NON-NLS-1$
			return false;
		}
	}

	/**
	 * Save rope parameters in extra file.
	 * 
	 * @return true, if file save as
	 */
	public boolean fileSaveAsSeil() {
		SmartFileChooser fileChooser = new SmartFileChooser();
		fileChooser.setDialogTitle("Speichern als ..."); //$NON-NLS-1$
		fileChooser.setSelectedFile(new File("seilparameter.dat")); //$NON-NLS-1$
		fileChooser.setFileFilter(new GenericFileFilter("*.dat", "dat")); //$NON-NLS-1$ //$NON-NLS-2$
		int state = fileChooser.showSaveDialog(this);
		if (state == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			Constants.LOGGER.log(Level.INFO, file.getName() + " loaded for save"); //$NON-NLS-1$
			saveSeilparameterIntoFile(file);
			return true;
		} else {
			Constants.LOGGER.log(Level.INFO, "Selection canceled"); //$NON-NLS-1$
			return false;
		}
	}

	/**
	 * Load a rope parameters file.
	 */
	public void openFileSeil() {
		DialogFrame frame = new DialogFrame(this, "M" + Constants.OE_K + "chten Sie die aktuellen Seilparameter speichern?", "SeilOeffnen"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		Helper.showFrame(frame, this);
	}

	/**
	 * Quit the programm. Ask for saving file.
	 */
	private void quit() {
		DialogFrame frame = new DialogFrame(this, "M" + Constants.OE_K + "chten Sie die aktuelle Datei speichern?", "Beenden"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		Helper.showFrame(frame, this);
	}

	/**
	 * File new. Ask for saving the old one.
	 */
	private void fileNew() {
		DialogFrame frame = new DialogFrame(this, "M" + Constants.OE_K + "chten Sie die aktuelle Datei speichern?", "Neu"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		Helper.showFrame(frame, this);
	}

	/**
	 * Save aufzugschacht into file. Use 'object writing' to do this. Therefor, all objects must implement the Serializable interface
	 * 
	 * @param file
	 *            the file to save into
	 */
	private void saveAufzugschachtIntoFile(File file) {
		if (!file.exists() || file.canWrite()) {
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(file);

				// write the important objects to the file
				Persistence.writeObjectsToXMLFile(aufzugschacht, berechnung, file);

				// set the title of the main window to the filename
				setTitle(getName() + " - " + file.getName()); //$NON-NLS-1$
			} catch (IOException e) {
				Constants.LOGGER.severe("IOException while saving file " + file.getAbsolutePath()); //$NON-NLS-1$
				e.printStackTrace();
			} finally {
				if (fos != null) {
					try {
						fos.close();
					} catch (IOException e) {
						Constants.LOGGER.severe("IOException while closing file " + file.getAbsolutePath()); //$NON-NLS-1$
						e.printStackTrace();
					}
				}
			}
		} else {
			ErrorFrame errorFrame = new ErrorFrame(this, "Datei " + file.getAbsolutePath() + " ist nicht schreibbar!"); //$NON-NLS-1$ //$NON-NLS-2$
			Helper.showFrame(errorFrame, this);
		}
	}

	/**
	 * Save rope parameters into extra file.
	 * 
	 * @param file
	 *            the file
	 */
	private void saveSeilparameterIntoFile(File file) {
		if (!file.exists() || file.canWrite()) {
			try {
				FileHelper.copy(seilparameterFile, file);
			} catch (IOException e) {
				Constants.LOGGER.severe("IOException while saving file " + file.getAbsolutePath()); //$NON-NLS-1$
				e.printStackTrace();
			}
		} else {
			ErrorFrame errorFrame = new ErrorFrame(this, "Datei " + file.getAbsolutePath() + " ist nicht schreibbar!"); //$NON-NLS-1$ //$NON-NLS-2$
			Helper.showFrame(errorFrame, this);
		}
	}

	/**
	 * Load file into aufzugschacht.
	 * 
	 * @param file
	 *            the file
	 */
	private void loadFileIntoAufzugschacht(File file) {
		if (file.canRead()) {
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(file);

				zeichenPanel.remove(aufzugschacht);
				ergebnisTable = new JTable();
				einzelErgebnisTable = new JTable();

				// read objects (Aufzugschacht, Berechnung) from xml file
				Object[] objects = Persistence.readObjectsFromXMLFile(file);
				if (objects == null) {
					ErrorFrame errorFrame = new ErrorFrame(this, "Datei " + file.getAbsolutePath() + " ist nicht lesbar!"); //$NON-NLS-1$ //$NON-NLS-2$
					Helper.showFrame(errorFrame, this);
					throw new FileNotFoundException();
				}

				aufzugschacht = (Aufzugschacht) objects[0];
				createAufzugschachtListener();
				deleteAllTabbedPane();
				aufzugschacht.initListener();
				aufzugschacht.fireEventsForButtons();
				aufzugschacht.fireEventsForParameters();

				aufzugschacht.fireEventsForTabbedPanes();
				aufzugschacht.setSize(zeichenPanel.getSize());
				aufzugschacht.setLocation(0, 0);
				zeichenPanelThreadAlive = false;
				zeichenPanel.add(aufzugschacht);
				zeichenPanel.repaint();
				loadSeilparameter();
				schachtReady = true;

				berechnung = (Berechnung) objects[1];
				berechnung.resetCalcRolls();
				if (berechnung.isCalculated()) {
					calculateButtonActionPerformed(null);
				}
				if (aufzugschacht.getKabine() != null && aufzugschacht.getGewicht() != null && aufzugschacht.getSeil() != null) {
					berechnung.setElements(aufzugschacht.getSeil(), aufzugschacht.getKabine(), aufzugschacht.getGewicht(), aufzugschacht.getUnterseil());
				}

				setParameters(berechnung.getFs1(), berechnung.getFs3(), berechnung.getFs4(), berechnung.getFn1(), berechnung.getToleranz());
				globalSeilmasse = berechnung.getBs().get(0)[9] * Math.pow(aufzugschacht.getSeil().getD(), 2);

				createErgebnisTable();

				setTitle(getName() + " - " + file.getName()); //$NON-NLS-1$
			} catch (FileNotFoundException e) {
				Constants.LOGGER.severe("File not found for loading: " + file.getAbsolutePath()); //$NON-NLS-1$
				// e.printStackTrace();
			} finally {
				if (fis != null) {
					try {
						fis.close();
					} catch (IOException e) {
						Constants.LOGGER.severe("IOException while closing file " + file.getAbsolutePath()); //$NON-NLS-1$
						e.printStackTrace();
					}
				}
			}
		} else {
			ErrorFrame errorFrame = new ErrorFrame(this, "Datei " + file.getAbsolutePath() + " ist nicht lesbar!"); //$NON-NLS-1$ //$NON-NLS-2$
			Helper.showFrame(errorFrame, this);
		}
	}

	/**
	 * Load seilparameter file from a FileChooser Dialog.
	 */
	private void loadSeilparameterFile() {
		SmartFileChooser fileChooser = new SmartFileChooser();
		fileChooser.setDialogTitle("" + Constants.OE_G + "ffnen"); //$NON-NLS-1$ //$NON-NLS-2$
		// fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setFileFilter(new GenericFileFilter("*.dat", "dat")); //$NON-NLS-1$ //$NON-NLS-2$
		int state = fileChooser.showOpenDialog(this);
		if (state == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			Constants.LOGGER.log(Level.INFO, file.getName() + " loaded for open"); //$NON-NLS-1$
			openSeilparameterFile(file.toURI());
		} else {
			Constants.LOGGER.log(Level.INFO, "Selection canceled"); //$NON-NLS-1$
			if (seilParameterFrame != null)
				seilParameterFrame.setOpened(true);
		}
	}

	/**
	 * Shows the options frame of the given element.
	 * 
	 * @param element
	 *            The element to show options for
	 * @param rolle2
	 *            the second roll
	 */
	public void showOptionsFrame(Element element, boolean rolle2) {
		boolean newOptionsFrame = false;
		if (element instanceof Treibscheibe) {
			optionsFrame = (JFrame) new TreibscheibeFrame((Treibscheibe) element, this);
			newOptionsFrame = true;
		}
		if (element instanceof Kabine) {
			optionsFrame = (JFrame) new KabineFrame((Kabine) element, this);
			newOptionsFrame = true;
		}
		if (element instanceof Gewicht) {
			optionsFrame = (JFrame) new GewichtFrame((Gewicht) element, this);
			newOptionsFrame = true;
		}
		if (element instanceof Umlenkrolle) {
			optionsFrame = (JFrame) new UmlenkrolleFrame((Umlenkrolle) element, this, aufzugschacht, rolle2);
			newOptionsFrame = true;
		}
		if (element instanceof DoppelUmlenkrolle) {
			optionsFrame = (JFrame) new DoppelrolleFrame((DoppelUmlenkrolle) element, this);
			newOptionsFrame = true;
		}

		if (newOptionsFrame) {
			Helper.showFrame(optionsFrame, this);
		}
	}

	/**
	 * Shows rope options frame.
	 * 
	 * @param seil
	 *            the rope
	 */
	public void showSeilOptionsFrame(Seil seil) {
		optionsFrame = (JFrame) new SeilFrame(seil, this);
		Helper.showFrame(optionsFrame, this);
	}

	/**
	 * Shows underrope options frame.
	 * 
	 * @param unterseil
	 *            the rope
	 */
	public void showUnterseilOptionsFrame(Unterseil unterseil) {
		JDialog unterseilDialog = new UnterseilDialog(unterseil, this, true);
		unterseilDialog.setLocationRelativeTo(this);
		unterseilDialog.setVisible(true);
	}

	/**
	 * The mass of the spanning weight will be added to the cabin and counter weight
	 */
	public void setMassUnterseilGewicht(int mass) {
		massUnterseilFormattedTextField.setText(String.valueOf(mass));
		parameterChanged();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.tbosch.aufzugseile.gui.utils.ReactingFrame#kClicked(javax.swing.JFrame)
	 */
	@Deprecated
	public void okClicked(JFrame child) {
		// react on some dialog returns
		setEnabled(true);
		if (child.getName().equals("Neu")) { //$NON-NLS-1$
			if (fileSave() == true) {
				newAufzugschacht();
			}
		}
		if (child.getName().equals("Oeffnen")) { //$NON-NLS-1$
			if (fileSave() == true) {
				openAufzugschacht();
			}
		}
		if (child.getName().equals("SeilOeffnen")) { //$NON-NLS-1$
			if (fileSaveAsSeil() == true) {
				loadSeilparameterFile();
			} else {
				if (seilParameterFrame != null)
					seilParameterFrame.setOpened(true);
			}
		}
		if (child.getName().equals("Beenden")) { //$NON-NLS-1$
			if (fileSave() == true) {
				quitProgram();
			}
		}
		if (child instanceof PrintDialog) {
			PrintDialog dialog = (PrintDialog) child;
			printNow(dialog.getBook(), dialog.getJob(), dialog.getPrintFrame(), dialog.getPrintFrameFeyrer());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.tbosch.aufzugseile.gui.utils.ReactingFrame#noClicked(javax.swing.JFrame)
	 */
	public void noClicked(JFrame child) {
		// react on 'NO' at a dialog
		setEnabled(true);
		if (child.getName().equals("Neu")) { //$NON-NLS-1$
			newAufzugschacht();
		} else if (child.getName().equals("Oeffnen")) { //$NON-NLS-1$
			if (!schachtReady) {
				setEnabled(false);
			}
			openAufzugschacht();
		} else if (child.getName().equals("SeilOeffnen")) { //$NON-NLS-1$
			loadSeilparameterFile();
		} else if (child.getName().equals("Beenden")) { //$NON-NLS-1$
			quitProgram();
		}
	}

	/**
	 * Opens an elevator from a chosen file.
	 */
	private void openAufzugschacht() {
		SmartFileChooser fileChooser = new SmartFileChooser();
		fileChooser.setDialogTitle("" + Constants.OE_G + "ffnen"); //$NON-NLS-1$ //$NON-NLS-2$
		// fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setFileFilter(new GenericFileFilter("*.alb (Aufzugseillebensdauerberechnung)", "alb")); //$NON-NLS-1$ //$NON-NLS-2$
		int state = fileChooser.showOpenDialog(this);
		if (state == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			Constants.LOGGER.log(Level.INFO, file.getName() + " loaded for open"); //$NON-NLS-1$
			loadFileIntoAufzugschacht(file);
			aufzugschachtFile = file;
		} else {
			Constants.LOGGER.log(Level.INFO, "Selection canceled"); //$NON-NLS-1$
		}
	}

	/**
	 * Creates a new elevator.
	 */
	private void newAufzugschacht() {
		zeichenPanel.remove(aufzugschacht);
		aufzugschacht = new Aufzugschacht(zeichenPanel.getWidth(), zeichenPanel.getHeight());
		createAufzugschachtListener();
		deleteAllTabbedPane();
		enableAllButtons();
		clearAllTextFields();
		aufzugschacht.initListener();
		aufzugschacht.fireEventsForButtons();
		aufzugschacht.fireEventsForParameters();
		aufzugschacht.fireEventsForTabbedPanes();
		aufzugschacht.setSize(zeichenPanel.getSize());
		aufzugschacht.setLocation(0, 0);
		zeichenPanelThreadAlive = false;
		zeichenPanel.add(aufzugschacht);
		zeichenPanel.repaint();
		setTitle(getName() + " - unbekannt.alb"); //$NON-NLS-1$
		aufzugschachtFile = null;
		berechnenClicked = false;
		berechnung = new Berechnung();
		jMenuItemSchachtActionPerformed(null);
	}

	/**
	 * Clear all text fields.
	 */
	private void clearAllTextFields() {
		kabinenmasseFormattedTextField.setText(""); //$NON-NLS-1$
		zuladungmasseFormattedTextField.setText(""); //$NON-NLS-1$
		gewichtmasseFormattedTextField.setText(""); //$NON-NLS-1$
		fs1FormattedTextField.setText(""); //$NON-NLS-1$
		fs3FormattedTextField.setText(""); //$NON-NLS-1$
		fs4FormattedTextField.setText(""); //$NON-NLS-1$
		fn1FormattedTextField.setText(""); //$NON-NLS-1$
		seilanzahlFormattedTextField.setText(""); //$NON-NLS-1$
		durchmesserFormattedTextField.setText(""); //$NON-NLS-1$
		biegelaengeFormattedTextField.setText(""); //$NON-NLS-1$
		aufhaengungFormattedTextField.setText(""); //$NON-NLS-1$
		massUnterseilFormattedTextField.setText("");
		durchschnittFormattedTextField.setText("");
		profilFormattedTextField.setText("");
		tolleranzFormattedTextField.setText("");
		gegenanteilFormattedTextField.setText("");
		umschlWinkelFormattedTextField.setText("");
	}

	/**
	 * Gets the ergebnis table.
	 * 
	 * @return the ergebnis table
	 */
	public JTable getErgebnisTable() {
		return ergebnisTable;
	}

	/**
	 * Gets the parameter table model.
	 * 
	 * @return the parameter table model
	 */
	public TableModel getParameterTableModel() {
		return parameterTableModel;
	}

	/**
	 * Gets the aufzugschacht.
	 * 
	 * @return the aufzugschacht
	 */
	public Aufzugschacht getAufzugschacht() {
		return aufzugschacht;
	}

	/**
	 * Sets the elevator ready.
	 * 
	 * @param schachtReady
	 *            the elevator is ready or not
	 */
	public void setSchachtReady(boolean schachtReady) {
		this.schachtReady = schachtReady;
	}

	/**
	 * Sets the zeichen panel thread alive.
	 * 
	 * @param zeichenPanelThreadAlive
	 *            if the zeichen panel thread is alive
	 */
	public void setZeichenPanelThreadAlive(boolean zeichenPanelThreadAlive) {
		this.zeichenPanelThreadAlive = zeichenPanelThreadAlive;
	}

	private void quitProgram() {
		if (schachtReadyThread != null)
			schachtReadyThread.stopThread();
		if (zeichenPanelThread != null)
			zeichenPanelThread.stopThread();
		dispose();
	}

	public javax.swing.JFormattedTextField getTolleranzFormattedTextField() {
		return tolleranzFormattedTextField;
	}

	public String getFilename() {
		if (aufzugschachtFile != null)
			return aufzugschachtFile.getName();
		else
			return "unbekannt.alb"; //$NON-NLS-1$
	}

	public double getGeschwi() {
		return 0;
	}

	public void setGeschwi(double geschw) {

	}

	/**
	 * Sets the load file.
	 * 
	 * @param loadFile
	 *            the load file
	 */
	public void setLoadFile(boolean loadFile) {
		this.loadFile = loadFile;
	}

	/**
	 * Gets the mass of the rope in the first column.
	 * 
	 * @return the mass in kg/100m
	 */
	public double getSeilmasse() {
		return globalSeilmasse;
	}

	/**
	 * Gets the umschl winkel formatted text field.
	 * 
	 * @return the umschl winkel formatted text field
	 */
	public javax.swing.JFormattedTextField getUmschlWinkelFormattedTextField() {
		return umschlWinkelFormattedTextField;
	}
}
