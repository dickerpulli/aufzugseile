/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.tbosch.seile.commons.gui.util;

import de.tbosch.commons.gui.GUIUtilities;
import de.tbosch.commons.Helper;
import de.tbosch.seile.commons.gui.SeilParameterDialog;
import java.awt.Component;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import org.apache.log4j.Logger;

/**
 *
 * @author bobo
 */
public class SeilparameterUtilities {

	/** Der Logger */
	private static final Logger logger = Logger.getLogger(SeilparameterUtilities.class.getName());
	
	/**
	 * Save rope parameters in extra file.
	 * 
	 * @return true, if file save as
	 */
	public static boolean seilparameterSpeichernAls(TableModel tableModel, Component parent) {
		File file = GUIUtilities.oeffneDateiSpeichernDialog("Speichern unter ...", "dat", parent);
		if (file != null) {
			seilparameterSpeichern(file, tableModel);
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * Speichert die Seilparameter aus dem Tabellen Modell in eine Datei.
	 * 
	 * FileType: 'n' rows of this type (true|false);<name>*(;<value>)
	 * 
	 * @param datei Die Datei
	 * @param tableModel das zu Speichernde Model
	 * 
	 * @throws FileSetWritableException the file set writable exception
	 */
	public static void seilparameterSpeichern(File datei, TableModel tableModel) {
		FileWriter fw = null;
		try {
			fw = new FileWriter(datei);
			String string = ""; //$NON-NLS-1$
			for (int i = 0; i < tableModel.getRowCount(); i++) {
				// row per row
				for (int j = 0; j < tableModel.getColumnCount(); j++) {
					// first column: boolean value
					if (j == 0) {
						if (tableModel.getValueAt(i, j) != null && (Boolean)tableModel.getValueAt(i, j) == true) string += "true;"; //$NON-NLS-1$
						else string += "false;"; //$NON-NLS-1$
					}
					// second column: name of type
					else if (j == 1) {
						string += (String)tableModel.getValueAt(i, j) + ";";
					}
					// rest of columns: values
					else if (j == tableModel.getColumnCount() - 1) {
						Object object = tableModel.getValueAt(i, j);
						if (object == null) {
							string += "null\n"; //$NON-NLS-1$
						}
						else {
							string += ((Double)object).toString() + "\n";
						}
					}
					else {
						Object object = tableModel.getValueAt(i, j);
						if (object == null) {
							string += "null;"; //$NON-NLS-1$
						}
						else {
							string += ((Double)object).toString() + ";";
						}
					}
				}
			}
			// write sting to file
			fw.write(string);
		}
		catch (IOException e) {
			logger.error("IOException while saving file " + datei.getAbsolutePath());
			e.printStackTrace();
		}
		finally {
			if (fw != null) {
				try {
					fw.close();
					logger.info("Save file: " + datei);
				}
				catch (IOException e) {
					logger.error("IOException while closing file " + datei.getAbsolutePath());
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Parst die Seilparameterdatei und füllt das übergebene Tabellen Modell
	 * Der Datei-Typ muss mit dem gespeicherten übereinstimmen - seilparameterSpeichern()
	 * 
	 * @param seilparameterDatei die Datei
	 * @param parameterTableModel Das Tabellen Modell
	 * 
	 * @throws FileParseException the file parse exception
	 */
	public static TableModel seilparameterParseDatei(File seilparameterDatei) {
		logger.info("Parse wire parameters file");
		SeilParameterDialog frame = new SeilParameterDialog(null, null, null, false);
		TableModel parameterTableModel = frame.getParameterTable().getModel();
		DefaultTableModel dtm = (DefaultTableModel)parameterTableModel;
		for (int i = parameterTableModel.getRowCount() - 1; i >= 0; i--) {
			// reset table
			dtm.removeRow(i);
		}
		FileReader fr = null;
		try {
			fr = new FileReader(seilparameterDatei);
			int r;
			int i = 0;
			int j = 0;
			String word = ""; //$NON-NLS-1$
			try {
				// while not end
				while ((r = fr.read()) != -1) {
					Vector<Object> nullVector = new Vector<Object>();
					if (dtm.getRowCount() < 1) dtm.addRow(nullVector);
					char c = (char)r;
					// parse word per word
					if (c != ';' && c != '\n') {
						word += c;
					}
					else {
						if (j == 0) {
							if (word.equals("true")) { //$NON-NLS-1$
								parameterTableModel.setValueAt(true, i, j);
							}
							else if (word.equals("false")) { //$NON-NLS-1$
								parameterTableModel.setValueAt(false, i, j);
							}
							else {
								logger.error("Parsing failed.");
							}
						}
						else if (j == 1) {
							if (word.equals("null")) { //$NON-NLS-1$
								parameterTableModel.setValueAt("", i, j); //$NON-NLS-1$
							}
							else {
								parameterTableModel.setValueAt(word, i, j);
							}
						}
						else {
							if (word.equals("null")) { //$NON-NLS-1$
								parameterTableModel.setValueAt(null, i, j);
							}
							else {
								try {
									parameterTableModel.setValueAt(Double.parseDouble(word), i, j);
								}
								catch (NumberFormatException e) {
									logger.error("Parsing failed.");
									e.printStackTrace();
								}
							}
						}
						if (c == ';') {
							// jump to next column if ';' is found
							j++;
						}
						else {
							// if '\n' is found: create new row in table
							dtm.addRow(nullVector);
							i++;
							j = 0;
						}
						word = ""; //$NON-NLS-1$
					}
				}
				if (dtm.getRowCount() > 0) dtm.removeRow(dtm.getRowCount() - 1);
				return parameterTableModel;
			}
			catch (IOException e) {
				logger.error("IOException while reading file " + seilparameterDatei.getAbsolutePath());
				e.printStackTrace();
			}
		}
		catch (FileNotFoundException e) {
			logger.error("File not found: " + seilparameterDatei.getAbsolutePath());
			e.printStackTrace();
		}
		finally {
			if (fr != null) {
				try {
					fr.close();
					frame.dispose();
				}
				catch (IOException e) {
					logger.error("IOException while closing file " + seilparameterDatei.getAbsolutePath()); //$NON-NLS-1$
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	/**
	 * Lade Seilparameter aus der Datei und speicher diese in der File
	 * und dem Tabellen Modell
	 */
	public static File seilparameterInitialLaden(String dateiname) {
		File seilparameterFile;
		try {
			URL url = Helper.getFileURL(dateiname);
			if (url == null) {
				seilparameterFile = new File(dateiname);
				seilparameterFile.createNewFile();
				logger.info("New file: " + seilparameterFile); //$NON-NLS-1$
				
				// create a rope parameter frame to get the default table model
				SeilParameterDialog frame = new SeilParameterDialog(null, null, null, false);
				TableModel parameterTableModel = frame.getParameterTable().getModel();
				
				// Default speichern
				seilparameterSpeichern(seilparameterFile, parameterTableModel);
			}
			else {
				// if file exists just give it back
				seilparameterFile = new File(url.toURI());
			}
			logger.info("Seilparameter initial aus " + seilparameterFile.getAbsolutePath() + " geladen.");
			return seilparameterFile;
		}
		catch (URISyntaxException ex) {
			Logger.getLogger(GUIUtilities.class.getName()).error(null, ex);
		}		
		catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
