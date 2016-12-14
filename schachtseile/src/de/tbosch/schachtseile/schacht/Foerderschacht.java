/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.tbosch.schachtseile.schacht;

import de.tbosch.commons.IDPool;
import de.tbosch.commons.gui.AbstractEingabeLabelUndText;
import de.tbosch.commons.gui.ImagePanel;
import de.tbosch.schachtseile.gui.EingabeFormattedTextField;
import de.tbosch.schachtseile.gui.LabelCheckbox;
import de.tbosch.schachtseile.gui.events.FoerderschachtEvent;
import de.tbosch.schachtseile.gui.events.FoerderschachtListener;
import de.tbosch.schachtseile.model.FoerderschachtModel;
import de.tbosch.seile.commons.CommonConstants;
import de.tbosch.seile.commons.elemente.Schacht;
import de.tbosch.seile.commons.elemente.Seil;
import de.tbosch.seile.commons.elemente.Treibscheibe;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 *
 * @author Thomas Bosch (tbosch@gmx.de)
 */
public class Foerderschacht extends ImagePanel implements Schacht {
	
	/** Logger */
	private static final Logger logger = Logger.getLogger(Foerderschacht.class.getName());

	public static final String TYP_1ROLLE = "1 Rolle";
	public static final String TYP_2ROLLEN = "2 Rollen";
	
	private String typ;
	private FoerderschachtModel foerderschachtModel;
	private DecimalFormat df_point;
	private DecimalFormat df_nopoint;
	private Map<Integer, EingabeFormattedTextField> textFieldListe = new HashMap<Integer, EingabeFormattedTextField>();

	public Foerderschacht(String typ) {
		super();
		this.typ = typ;
		String imagePath;
		if (typ.equals(TYP_1ROLLE)) {
			imagePath = "/de/tbosch/schachtseile/resources/images/konstruktion1rolle.png";
		}
		else {
			imagePath = "/de/tbosch/schachtseile/resources/images/konstruktion2rollen.png";
		}
		super.setImage(imagePath);
		initialiesiereDecimalFormats();
		initialisiereElemente();
	}
	
	public Foerderschacht(FoerderschachtModel foerderschachtModel) {
		super();
		this.foerderschachtModel = foerderschachtModel;
		String imagePath;
		if (foerderschachtModel.getTyp().equals(TYP_1ROLLE)) {
			imagePath = "/de/tbosch/schachtseile/resources/images/konstruktion1rolle.png";
		}
		else {
			imagePath = "/de/tbosch/schachtseile/resources/images/konstruktion2rollen.png";
		}
		super.setImage(imagePath);
		initialiesiereDecimalFormats();
		addEingabeLabels();
	}
	
	private void initialiesiereDecimalFormats() {
		df_point = new DecimalFormat();
		df_point.setMaximumFractionDigits(2);
		df_point.setMinimumFractionDigits(2);
		df_point.setMaximumIntegerDigits(1);
		
		df_nopoint = new DecimalFormat();
		df_nopoint.setMaximumFractionDigits(0);
		df_nopoint.setMinimumFractionDigits(0);
	}
	
	private void initialisiereElemente() {
		FoerderschachtSeil seil = new FoerderschachtSeil();
		FoerderschachtTreibscheibe treibscheibe = new FoerderschachtTreibscheibe(this);
		seil.addElement(treibscheibe);
		FoerderschachtUmlenkrolle umlenkrolle1 = new FoerderschachtUmlenkrolle(this);
		seil.addElement(umlenkrolle1);
		FoerderschachtUmlenkrolle umlenkrolle2 = null;
		if (typ.equals(TYP_2ROLLEN)) {
			umlenkrolle2 = new FoerderschachtUmlenkrolle(this);
			seil.addElement(umlenkrolle2);
		}
		FoerderschachtKapsel kapsel1 = new FoerderschachtKapsel(this);
		FoerderschachtKapsel kapsel2 = new FoerderschachtKapsel(this);
		
		// TODO: Annahmne: Unterseil = Tragseil
		FoerderschachtSeil unterseil = new FoerderschachtSeil();
		unterseil.setCount(seil.getCount());
		unterseil.setNenndurchmesser(seil.getD());
		unterseil.setMassPerM(seil.getMassPerM());
		unterseil.setLength(seil.getLength());
		
		Map<String, Double> doubleFaktoren = new HashMap<String, Double>();
		doubleFaktoren.put(CommonConstants.PARAMETER_NAME_FS1, 1.1);
		doubleFaktoren.put(CommonConstants.PARAMETER_NAME_FS2, 1.0);
		doubleFaktoren.put(CommonConstants.PARAMETER_NAME_FS3, 1.25);
		doubleFaktoren.put(CommonConstants.PARAMETER_NAME_FS4, 1.2);
		doubleFaktoren.put(CommonConstants.PARAMETER_NAME_FS5, 1.0);
		doubleFaktoren.put(CommonConstants.PARAMETER_NAME_FN1, 1.0);
		doubleFaktoren.put(CommonConstants.PARAMETER_NAME_FN2, 0.94);
		doubleFaktoren.put(CommonConstants.PARAMETER_NAME_FN3, 1.0);
		doubleFaktoren.put(CommonConstants.PARAMETER_NAME_FN4, 1.0);
		doubleFaktoren.put(CommonConstants.PARAMETER_NAME_FN5, 1.0);
		
		Map<String, Integer> intFaktoren = new HashMap<String, Integer>();
		intFaktoren.put(CommonConstants.PARAMETER_NAME_LM, 1);
		intFaktoren.put(CommonConstants.PARAMETER_NAME_V, 1);
		intFaktoren.put(CommonConstants.PARAMETER_NAME_AP, 1);
		intFaktoren.put(CommonConstants.PARAMETER_NAME_AM, 1);
		intFaktoren.put(CommonConstants.PARAMETER_NAME_H1, 700);
		intFaktoren.put(CommonConstants.PARAMETER_NAME_H2, 60);
		intFaktoren.put(CommonConstants.PARAMETER_NAME_H3, 10);
		intFaktoren.put(CommonConstants.PARAMETER_NAME_A, 30);
		
		foerderschachtModel = new FoerderschachtModel(seil, unterseil, treibscheibe, umlenkrolle1, umlenkrolle2, kapsel1, kapsel2, typ, doubleFaktoren, intFaktoren);
	}
	
	/**
	 * Fügt einen FoerderschachtListener zum Foerderschacht hinzu,
	 * der auf Events wartet.
	 * @param listener
	 */
	public void addFoerderschachtListener(FoerderschachtListener listener) {
		listenerList.add(FoerderschachtListener.class, listener);
	}
	
	public void addEingabeLabels() {
		addEingabeLabel(EingabeFormattedTextField.EINGABE_PARAMETER_T_D, 35, 11, AbstractEingabeLabelUndText.OHNE_PUNKT, foerderschachtModel.getTreibscheibe().getDurchmesser());
		addEingabeLabel(EingabeFormattedTextField.EINGABE_PARAMETER_T_GD2, 35, 44, AbstractEingabeLabelUndText.OHNE_PUNKT, 0);
		addEingabeLabel(EingabeFormattedTextField.EINGABE_PARAMETER_A, 27, 178, AbstractEingabeLabelUndText.OHNE_PUNKT, foerderschachtModel.getIntFaktoren().get(CommonConstants.PARAMETER_NAME_A));
		addEingabeLabel(EingabeFormattedTextField.EINGABE_PARAMETER_O, 27, 327, AbstractEingabeLabelUndText.OHNE_PUNKT, 0);
		addEingabeLabel(EingabeFormattedTextField.EINGABE_PARAMETER_H1, 2, 271, AbstractEingabeLabelUndText.OHNE_PUNKT, foerderschachtModel.getIntFaktoren().get(CommonConstants.PARAMETER_NAME_H1));
		addEingabeLabel(EingabeFormattedTextField.EINGABE_PARAMETER_H2, 2, 423, AbstractEingabeLabelUndText.OHNE_PUNKT, foerderschachtModel.getIntFaktoren().get(CommonConstants.PARAMETER_NAME_H2));
		addEingabeLabel(EingabeFormattedTextField.EINGABE_PARAMETER_U_M, 23, 661, AbstractEingabeLabelUndText.MIT_PUNKT, foerderschachtModel.getUnterseil().getMassPerM());
		addEingabeLabel(EingabeFormattedTextField.EINGABE_PARAMETER_U_DIM, 23, 694, AbstractEingabeLabelUndText.OHNE_PUNKT, foerderschachtModel.getUnterseil().getD());
		addEingabeLabel(EingabeFormattedTextField.EINGABE_PARAMETER_U_N, 23, 727, AbstractEingabeLabelUndText.OHNE_PUNKT, foerderschachtModel.getUnterseil().getCount());
		addEingabeLabel(EingabeFormattedTextField.EINGABE_PARAMETER_R1_D, 264, 345, AbstractEingabeLabelUndText.OHNE_PUNKT, foerderschachtModel.getUmlenkrolle1().getDurchmesser());
		addEingabeLabel(EingabeFormattedTextField.EINGABE_PARAMETER_R1_GD2, 264, 378, AbstractEingabeLabelUndText.OHNE_PUNKT, 0);
		addEingabeLabel(EingabeFormattedTextField.EINGABE_PARAMETER_K1_MC, 264, 422, AbstractEingabeLabelUndText.OHNE_PUNKT, foerderschachtModel.getKapsel1().getMass());
		addEingabeLabel(EingabeFormattedTextField.EINGABE_PARAMETER_K1_MP, 264, 455, AbstractEingabeLabelUndText.OHNE_PUNKT, foerderschachtModel.getKapsel1().getZuladung());
		addEingabeLabel(EingabeFormattedTextField.EINGABE_PARAMETER_S_M, 264, 501, AbstractEingabeLabelUndText.MIT_PUNKT, foerderschachtModel.getSeil().getMassPerM());
		addEingabeLabel(EingabeFormattedTextField.EINGABE_PARAMETER_S_DIM, 264, 534, AbstractEingabeLabelUndText.OHNE_PUNKT, foerderschachtModel.getSeil().getD());
		addEingabeLabel(EingabeFormattedTextField.EINGABE_PARAMETER_S_N, 264, 567, AbstractEingabeLabelUndText.OHNE_PUNKT, foerderschachtModel.getSeil().getCount());
		addEingabeLabel(EingabeFormattedTextField.EINGABE_PARAMETER_K2_MC, 264, 608, AbstractEingabeLabelUndText.OHNE_PUNKT, foerderschachtModel.getKapsel2().getMass());
		addEingabeLabel(EingabeFormattedTextField.EINGABE_PARAMETER_K2_MP, 264, 641, AbstractEingabeLabelUndText.OHNE_PUNKT, foerderschachtModel.getKapsel2().getZuladung());
		addEingabeLabel(EingabeFormattedTextField.EINGABE_PARAMETER_H3, 264, 743, AbstractEingabeLabelUndText.OHNE_PUNKT, foerderschachtModel.getIntFaktoren().get(CommonConstants.PARAMETER_NAME_H3));
		addEingabeLabel(EingabeFormattedTextField.EINGABE_PARAMETER_LM, 424, 373, AbstractEingabeLabelUndText.OHNE_PUNKT, foerderschachtModel.getIntFaktoren().get(CommonConstants.PARAMETER_NAME_LM));	
		addEingabeLabel(EingabeFormattedTextField.EINGABE_PARAMETER_V, 424, 406, AbstractEingabeLabelUndText.OHNE_PUNKT, foerderschachtModel.getIntFaktoren().get(CommonConstants.PARAMETER_NAME_V));	
		addEingabeLabel(EingabeFormattedTextField.EINGABE_PARAMETER_AP, 424, 439, AbstractEingabeLabelUndText.OHNE_PUNKT, foerderschachtModel.getIntFaktoren().get(CommonConstants.PARAMETER_NAME_AP));	
		addEingabeLabel(EingabeFormattedTextField.EINGABE_PARAMETER_AM, 424, 472, AbstractEingabeLabelUndText.OHNE_PUNKT, foerderschachtModel.getIntFaktoren().get(CommonConstants.PARAMETER_NAME_AM));
		
		if (foerderschachtModel.getTyp().equals(TYP_1ROLLE)) { 
			addLabelCheckbox(LabelCheckbox.LABEL_CHECKBOX_G1, 305, 159, foerderschachtModel.getUmlenkrolle1().isGegenbiegung());
		}
		
		if (foerderschachtModel.getTyp().equals(TYP_2ROLLEN)) {
			addEingabeLabel(EingabeFormattedTextField.EINGABE_PARAMETER_R2_D, 424, 340, AbstractEingabeLabelUndText.OHNE_PUNKT, foerderschachtModel.getUmlenkrolle2().getDurchmesser());	
			addEingabeLabel(EingabeFormattedTextField.EINGABE_PARAMETER_A2, 305, 159, AbstractEingabeLabelUndText.OHNE_PUNKT, 0);	
			addEingabeLabel(EingabeFormattedTextField.EINGABE_PARAMETER_A3, 305, 11, AbstractEingabeLabelUndText.OHNE_PUNKT, 0);	
			addLabelCheckbox(LabelCheckbox.LABEL_CHECKBOX_G2_1, 337, 56, foerderschachtModel.getUmlenkrolle1().isGegenbiegung());
			addLabelCheckbox(LabelCheckbox.LABEL_CHECKBOX_G2_2, 367, 303, foerderschachtModel.getUmlenkrolle2().isGegenbiegung());
		}
	}
	
	private void addEingabeLabel(int id, int x, int y, int formatId, Number number) {
		String numberAsString;
		if (formatId == AbstractEingabeLabelUndText.OHNE_PUNKT) {
			numberAsString = df_nopoint.format(number);
		}
		else {
			numberAsString = df_point.format(number);
		}
		final int f_id = id;
		final EingabeFormattedTextField label = new EingabeFormattedTextField(id, formatId);
		label.getFormattedTextField().setText(numberAsString);
		label.setLocation(x, y);
		label.getFormattedTextField().addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				textFieldKeyReleased(f_id, label.getFormattedTextField().getText());
			}
		});
		add(label);
		textFieldListe.put(id, label);
	}
	
	/**
	 * Setzt den Wert a
	 */
	public void setEingabeLabelWert(int id, Number number) {
		EingabeFormattedTextField label = textFieldListe.get(id);
		int formatId = label.getFormatId();
		String numberAsString;
		if (formatId == AbstractEingabeLabelUndText.OHNE_PUNKT) {
			numberAsString = df_nopoint.format(number);
		}
		else {
			numberAsString = df_point.format(number);
		}
		label.getFormattedTextField().setText(numberAsString);
	}
	
	private void addLabelCheckbox(int id, int x, int y, boolean wert) {
		final int f_id = id;
		final LabelCheckbox label = new LabelCheckbox(id);
		label.getCheckbox().setSelected(wert);
		label.setLocation(x, y);
		label.getCheckbox().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				checkboxActionPerformed(f_id, label.getCheckbox().isSelected());
			}
		});
		add(label);
	}
	
	private void checkboxActionPerformed(int id, boolean selected) {
		boolean gegenbiegung = selected;
		switch (id) {
			case LabelCheckbox.LABEL_CHECKBOX_G1:
				foerderschachtModel.getUmlenkrolle1().setGegenbiegung(gegenbiegung);
				break;
			case LabelCheckbox.LABEL_CHECKBOX_G2_1:
				foerderschachtModel.getUmlenkrolle1().setGegenbiegung(gegenbiegung);
				break;
			case LabelCheckbox.LABEL_CHECKBOX_G2_2:
				foerderschachtModel.getUmlenkrolle2().setGegenbiegung(gegenbiegung);
				break;
		}
		parameterGeaendert();
	}
	
	private void textFieldKeyReleased(int id, String text) {
		try {
			double textAsDouble = df_point.parse(text).doubleValue();
			int textAsInt = df_nopoint.parse(text).intValue();
			switch (id) {
				case EingabeFormattedTextField.EINGABE_PARAMETER_A:
					foerderschachtModel.getIntFaktoren().put(CommonConstants.PARAMETER_NAME_A, textAsInt);
					break;
				case EingabeFormattedTextField.EINGABE_PARAMETER_A2:
					break;
				case EingabeFormattedTextField.EINGABE_PARAMETER_A3:
					break;
				case EingabeFormattedTextField.EINGABE_PARAMETER_H1:
					//foerderschachtModel.getSeil().setLength(textAsInt);
					foerderschachtModel.getIntFaktoren().put(CommonConstants.PARAMETER_NAME_H1, textAsInt);
					break;
				case EingabeFormattedTextField.EINGABE_PARAMETER_H2:
					foerderschachtModel.getIntFaktoren().put(CommonConstants.PARAMETER_NAME_H2, textAsInt);
					break;
				case EingabeFormattedTextField.EINGABE_PARAMETER_H3:
					foerderschachtModel.getIntFaktoren().put(CommonConstants.PARAMETER_NAME_H3, textAsInt);
					break;
				case EingabeFormattedTextField.EINGABE_PARAMETER_K1_MC:
					foerderschachtModel.getKapsel1().setMass(textAsInt);
					break;
				case EingabeFormattedTextField.EINGABE_PARAMETER_K1_MP:
					foerderschachtModel.getKapsel1().setZuladung(textAsInt);
					break;
				case EingabeFormattedTextField.EINGABE_PARAMETER_K2_MC:
					foerderschachtModel.getKapsel2().setMass(textAsInt);
					break;
				case EingabeFormattedTextField.EINGABE_PARAMETER_K2_MP:
					foerderschachtModel.getKapsel2().setZuladung(textAsInt);
					break;
				case EingabeFormattedTextField.EINGABE_PARAMETER_O:
					break;
				case EingabeFormattedTextField.EINGABE_PARAMETER_R1_D:
					foerderschachtModel.getUmlenkrolle1().setDurchmesser(textAsInt);
					break;
				case EingabeFormattedTextField.EINGABE_PARAMETER_R1_GD2:
					break;
				case EingabeFormattedTextField.EINGABE_PARAMETER_AM:
					foerderschachtModel.getIntFaktoren().put(CommonConstants.PARAMETER_NAME_AM, textAsInt);
					break;
				case EingabeFormattedTextField.EINGABE_PARAMETER_AP:
					foerderschachtModel.getIntFaktoren().put(CommonConstants.PARAMETER_NAME_AP, textAsInt);
					break;
				case EingabeFormattedTextField.EINGABE_PARAMETER_R2_D:
					foerderschachtModel.getUmlenkrolle2().setDurchmesser(textAsInt);
					break;
				case EingabeFormattedTextField.EINGABE_PARAMETER_LM:
					foerderschachtModel.getIntFaktoren().put(CommonConstants.PARAMETER_NAME_LM, textAsInt);
					break;
				case EingabeFormattedTextField.EINGABE_PARAMETER_V:
					foerderschachtModel.getIntFaktoren().put(CommonConstants.PARAMETER_NAME_V, textAsInt);
					break;
				case EingabeFormattedTextField.EINGABE_PARAMETER_S_DIM:
					foerderschachtModel.getSeil().setNenndurchmesser(textAsInt);
					break;
				case EingabeFormattedTextField.EINGABE_PARAMETER_S_M:
					foerderschachtModel.getSeil().setMassPerM(textAsDouble);
					break;
				case EingabeFormattedTextField.EINGABE_PARAMETER_S_N:
					foerderschachtModel.getSeil().setCount(textAsInt);
					break;
				case EingabeFormattedTextField.EINGABE_PARAMETER_T_D:
					foerderschachtModel.getTreibscheibe().setDurchmesser(textAsInt);
					break;
				case EingabeFormattedTextField.EINGABE_PARAMETER_T_GD2:
					break;
				case EingabeFormattedTextField.EINGABE_PARAMETER_U_DIM:
					foerderschachtModel.getUnterseil().setNenndurchmesser(textAsInt);
					break;
				case EingabeFormattedTextField.EINGABE_PARAMETER_U_M:
					foerderschachtModel.getUnterseil().setMassPerM(textAsDouble);
					break;
				case EingabeFormattedTextField.EINGABE_PARAMETER_U_N:
					foerderschachtModel.getUnterseil().setCount(textAsInt);
					break;
			}
		} catch (ParseException ex) {
			logger.warn("FormattedTextField übernimmt die Format-Prüfung");
		}
		parameterGeaendert();
	}
	
	/**
	 * Wird aufgerufen, wenn ein Textfeld oder eine Checkbox geändert wurde.
	 */
	private void parameterGeaendert() {
		fireEvent(new FoerderschachtEvent(this, FoerderschachtEvent.PARAMETER_GEAENDERT_ID));
	}
	
	/**
	 * Fire event.
	 * 
	 * @param evt the evt
	 */
	public void fireEvent(FoerderschachtEvent evt) {
		Object[] listeners = listenerList.getListenerList();
        // Each listener occupies two elements - the first is the listener class
        // and the second is the listener instance
        for (int i = 0; i < listeners.length; i += 2) {
            if (listeners[i] == FoerderschachtListener.class) {
                ((FoerderschachtListener)listeners[i+1]).eventOccurred(evt);
            }
        }
	}
	
	/** 
	 * Liefert die Seiltrieb- und Biegewechselfaktoren
	 * 
	 * @return die Map von Faktoren
	 */
	public Map<String, Double> getDoubleParameters() {
		return foerderschachtModel.getDoubleFaktoren();
	}
	
	/** 
	 * Liefert die Seiltrieb- und Biegewechselfaktoren
	 * 
	 * @return die Map von Faktoren
	 */
	public void setDoubleParameters(Map<String, Double> parameters) {
		foerderschachtModel.setDoubleFaktoren(parameters);
	}
	
	/** 
	 * Liefert die Höhen
	 * 
	 * @return die Map von Faktoren
	 */
	public Map<String, Integer> getIntParameters() {
		return foerderschachtModel.getIntFaktoren();
	}
	
	/** 
	 * Setzt die Höhen
	 * 
	 */
	public void setIntParameters(Map<String, Integer> parameters) {
		foerderschachtModel.setIntFaktoren(parameters);
	}

	/**
	 * Liefert das Datenmodel des Foederschachts
	 * 
	 * @return
	 */
	public FoerderschachtModel getFoerderschachtModel() {
		return foerderschachtModel;
	}

	/**
	 * Setzt nach dem Laden das Model des Foederschachtes
	 * 
	 * @param foerderschachtModel
	 */
	public void setFoerderschachtModel(FoerderschachtModel foerderschachtModel) {
		this.foerderschachtModel = foerderschachtModel;
	}

	public IDPool getIDPoolTreibscheibe() {
		return iDPoolTreibscheibe;
	}

	public IDPool getIDPoolUmlenkrolle() {
		return iDPoolUmlenkrolle;
	}

	public Treibscheibe getTreibscheibe() {
		return foerderschachtModel.getTreibscheibe();
	}

	public Seil getSeil() {
		return foerderschachtModel.getSeil();
	}
	
}
