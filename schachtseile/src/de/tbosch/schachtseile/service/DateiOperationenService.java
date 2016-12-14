/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.tbosch.schachtseile.service;

import de.tbosch.commons.Persistenz;
import de.tbosch.schachtseile.SchachtseileApp;
import de.tbosch.schachtseile.model.FoerderschachtModel;
import de.tbosch.schachtseile.schacht.Foerderschacht;
import de.tbosch.seile.berechnung.lebensdauer.Berechnung;
import de.tbosch.seile.commons.elemente.Kapsel;
import de.tbosch.seile.commons.elemente.Seil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import org.apache.log4j.Logger;
import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;

/**
 *
 * @author Thomas Bosch (tbosch@gmx.de)
 */
public class DateiOperationenService {
	
	private static final Logger logger = Logger.getLogger(DateiOperationenService.class.getName());
	
	private ResourceMap resourceMap = Application.getInstance(SchachtseileApp.class).getContext().getResourceMap(DateiOperationenService.class);
	
	private boolean gespeichert = true;
	private String defaultDateiname = "unbekannt.slb";
	private File schachtdatei = null;
	private Foerderschacht foerderschacht;
	private Berechnung berechnung;
	
	private DialogService dialogService;
	
	public DateiOperationenService(JFrame mainFrame) {
		dialogService = new DialogService(mainFrame);
	}

	/**
	 * Lade einen Förderschacht aus einer Datei.
	 * 
	 * @param file Die zu lesende Datei.
	 */
	public void ladeAusDatei(File datei) {
		if (datei.canRead()) {
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(datei);

				// lese Objekte aus der Persistentz
				List<Object> objects = Persistenz.readObjectsFromXMLFile(datei);
				if (objects == null) {
					dialogService.showErrorDialog(resourceMap.getString("nichtLesbar.error") + " : " + datei.getAbsolutePath());
					throw new FileNotFoundException();
				}

				// Das erste Objekt ist, entsprechend der Speicherung ein Förderschacht
				FoerderschachtModel foerderschachtModel = (FoerderschachtModel)objects.get(0);
				foerderschacht = new Foerderschacht(foerderschachtModel);
				
				// Das zweite ist die Berechnung
				Berechnung gespeicherteBerechnung = (Berechnung) objects.get(1);
				Seil seil = foerderschachtModel.getSeil();
				Seil unterseil = foerderschachtModel.getUnterseil();
				Kapsel kapsel1 = foerderschachtModel.getKapsel1();
				Kapsel kapsel2 = foerderschachtModel.getKapsel2();
				int nt = 1; // Aufhängung immer 1:1
				double toleranz = gespeicherteBerechnung.getToleranz();
				double fn1 = gespeicherteBerechnung.getFn1();
				double fs1 = gespeicherteBerechnung.getFs1();
				double fs3 = gespeicherteBerechnung.getFs3();
				double fs4 = gespeicherteBerechnung.getFs4();
				double fs5 = gespeicherteBerechnung.getFs5();
				double[] nb = {};
				berechnung = new Berechnung(seil, unterseil, kapsel1, kapsel2, nt, toleranz, fn1, fs1, fs3, fs4, fs5, nb);
				berechnung.setDoubleParameters(foerderschachtModel.getDoubleFaktoren());
				berechnung.setIntParameters(foerderschachtModel.getIntFaktoren());
				
				// Reset Berechnung
				berechnung.resetCalcRolls();
				
				// Weiter Schritte nach dem Laden
				gespeichert = true;
				schachtdatei = datei;

				logger.info("Datei geöffnet : " + datei.getName());
			} catch (FileNotFoundException e) {
				logger.error("Datei nicht gefunden : " + datei.getAbsolutePath());
				dialogService.showErrorDialog(resourceMap.getString("nichtGefunden.error") + " : " + datei.getAbsolutePath());
			} finally {
				if (fis != null) {
					try {
						fis.close();
					} catch (IOException e) {
						logger.error("IOException während des Schließens von " + datei.getAbsolutePath());
						e.printStackTrace();
					}
				}
			}
		} else {
			logger.error("Datei nicht schreibbar : " + datei.getAbsolutePath());
			dialogService.showErrorDialog(resourceMap.getString("nichtSchreibbar.error") + " : " + datei.getAbsolutePath());
		}
	}
	
	/**
	 * Öffnet ein Datei-Auswahl-Fenster zum Öffnen einer neuen Datei.
	 */
	public boolean oeffnenVon() {
		File file = dialogService.showFileChooserOpen();
		if (file != null) {
			logger.info(file.getName() + " zum Öffnen geladen.");
			ladeAusDatei(file);
			schachtdatei = file;
			return true;
		}
		else {
			logger.info("Auswahl abgebrochen.");
			return false;
		}
	}
	
	/**
	 * Speichert die Datei als neue Datei ab.
	 * 
	 * @return true, wenn das Speichern erfolgreich war.
	 */
	public boolean speichernAls() {
		File file = dialogService.showFileChooserSave(defaultDateiname);
		if (file != null) {
			logger.info(file.getName() + " zum Speichern geladen.");
			schachtdatei = file;
			return speichernInDatei(file);
		} else {
			logger.info("Auswahl abgebrochen.");
			return false;
		}
	}

	/**
	 * Speichert den Förderschacht in eine Datei. Hierzu wird das
	 * Persistenz-Util benutzt,
	 * 
	 * @param file Die Datei,
	 * 
	 * @return true, wenn das Speichern erfolgreich war.
	 */
	public boolean speichernInDatei(File datei) {
		if (!datei.exists() || datei.canWrite()) {
			// persistiere die Objekte
			List<Object> objects = new ArrayList<Object>();
			objects.add(foerderschacht.getFoerderschachtModel());
			objects.add(berechnung);
			Persistenz.writeObjectsToXMLFile(objects, datei);

			// return successfull
			logger.info("Datei gespeichert : " + datei.getName());
			return true;
			
		} else {
			logger.error("File not writable : " + datei.getAbsolutePath());
			dialogService.showErrorDialog(resourceMap.getString("nichtSchreibbar.error") + " : " + datei.getAbsolutePath());
		}
		return false;
	}
	
	public boolean speichern() {
		boolean speichernErfolgreich;
		if (schachtdatei != null) {
			speichernErfolgreich = speichernInDatei(schachtdatei);
		} else {
			speichernErfolgreich = speichernAls();
		}
		if (speichernErfolgreich) {
			gespeichert = true;
		}
		return speichernErfolgreich;
	}

	public Berechnung getBerechnung() {
		return berechnung;
	}

	public Foerderschacht getFoerderschacht() {
		return foerderschacht;
	}

	public File getSchachtdatei() {
		return schachtdatei;
	}

	public void setBerechnung(Berechnung berechnung) {
		this.berechnung = berechnung;
	}

	public void setFoerderschacht(Foerderschacht foerderschacht) {
		gespeichert = false;
		schachtdatei = null;
		this.foerderschacht = foerderschacht;
	}

	public boolean isGespeichert() {
		return gespeichert;
	}
	
}
