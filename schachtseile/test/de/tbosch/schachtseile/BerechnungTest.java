/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.tbosch.schachtseile;

import static org.junit.Assert.*;

import de.tbosch.schachtseile.schacht.Foerderschacht;
import de.tbosch.schachtseile.schacht.FoerderschachtKapsel;
import de.tbosch.schachtseile.schacht.FoerderschachtSeil;
import de.tbosch.schachtseile.schacht.FoerderschachtTreibscheibe;
import de.tbosch.schachtseile.schacht.FoerderschachtUmlenkrolle;
import de.tbosch.seile.berechnung.lebensdauer.Berechnung;
import de.tbosch.seile.berechnung.lebensdauer.Berechnung.Summenobjekt;
import de.tbosch.seile.commons.CommonConstants;

import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

/**
 *
 * @author tbo
 */
public class BerechnungTest {
	
	@Test
	public void testBerechnungTurmfoerderungNurAufTSGrob() {
		Foerderschacht schacht = new Foerderschacht(Foerderschacht.TYP_1ROLLE);
		
		FoerderschachtSeil seil = new FoerderschachtSeil();
		FoerderschachtSeil unterseil = new FoerderschachtSeil();
		
		FoerderschachtKapsel kapsel1 = new FoerderschachtKapsel(schacht);
		kapsel1.setMass(19780);
		kapsel1.setZuladung(20000);
		
		FoerderschachtKapsel kapsel2 = new FoerderschachtKapsel(schacht);
		kapsel2.setMass(19780);
		kapsel2.setZuladung(20000);
		
		FoerderschachtUmlenkrolle umlenkrolle1 = new FoerderschachtUmlenkrolle(schacht);
		
		FoerderschachtTreibscheibe treibscheibe = new FoerderschachtTreibscheibe(schacht);
		
		Map<String, Double> doubleFaktoren = new HashMap<String, Double>();
		Map<String, Integer> intFaktoren = new HashMap<String, Integer>();
		
		seil.addElement(kapsel1);
		seil.addElement(treibscheibe);
		seil.addElement(umlenkrolle1);
		seil.addElement(kapsel2);
		
		seil.setBiegelaenge(25000);
		
		doubleFaktoren.put(CommonConstants.PARAMETER_NAME_FS1, 1.05);
		doubleFaktoren.put(CommonConstants.PARAMETER_NAME_FS3, 1.25);
		doubleFaktoren.put(CommonConstants.PARAMETER_NAME_FS4, 1.2);
		doubleFaktoren.put(CommonConstants.PARAMETER_NAME_FS5, 1.0);
		doubleFaktoren.put(CommonConstants.PARAMETER_NAME_FN1, 1.0);
		
		intFaktoren.put(CommonConstants.PARAMETER_NAME_H1, (int)(14000 / (0.14 * 4)));
		intFaktoren.put(CommonConstants.PARAMETER_NAME_H2, (int)(1000 / (0.14 * 4)));
		intFaktoren.put(CommonConstants.PARAMETER_NAME_H3, (int)(1000 / (0.14 * 4)));
		intFaktoren.put(CommonConstants.PARAMETER_NAME_A, (int)(2000 / (0.14 * 4)));
		
		// H1 + H2 + H3 = (int)(18000 / (0.14 * 4))
		// H1 - (H2 - H3 - A) = (int)(18000 / (0.14 * 4))
		intFaktoren.put(CommonConstants.PARAMETER_NAME_H1, (int)(28571));
		intFaktoren.put(CommonConstants.PARAMETER_NAME_H2, (int)(1786));
		intFaktoren.put(CommonConstants.PARAMETER_NAME_H3, (int)(1786));
		intFaktoren.put(CommonConstants.PARAMETER_NAME_A, (int)(3571));
		
		int nt = 1;
		double toleranz = 1.0;
		double fn1 = 1.0;
		double fs1 = 1.05;
		double fs3 = 1.25;
		double fs4 = 1.2;
		double fs5 = 1.0; // dummy
		double[] nb = new double[] {-1.194, -1.51, 1.322, 8.07, -0.32, -2.649, 1.2, 1.9, 0.94, 0.35, 1770, 18, 2.55};
		
		Berechnung berechnung = new Berechnung(seil, unterseil, kapsel1, kapsel2, nt, toleranz, fn1, fs1, fs3, fs4, fs5, nb);
		berechnung.setDoubleParameters(doubleFaktoren);
		berechnung.setIntParameters(intFaktoren);
		
		Summenobjekt[][] summenArrayNurAufTSGrob = {{Summenobjekt.F_Q_M, Summenobjekt.F_Q_M, Summenobjekt.F_Q_M, Summenobjekt.F_Q_M,
									Summenobjekt.F_M, Summenobjekt.F_M, Summenobjekt.F_Q_M, Summenobjekt.F_Q_M},
									{Summenobjekt.NULL, Summenobjekt.NULL, Summenobjekt.Q, Summenobjekt.Q,
									Summenobjekt.NULL, Summenobjekt.NULL, Summenobjekt.Q, Summenobjekt.Q}};
		
		long[][][] erg = berechnung.calculate(summenArrayNurAufTSGrob);
		
		assertNotNull(erg);
		// eigentlich 39.697, 78.599
		assertEquals(39697L, erg[1][0][0]);
		assertEquals(78600L, erg[1][0][1]);
	}
	
	@Test
	public void testBerechnungTurmfoerderungKeine() {
		Foerderschacht schacht = new Foerderschacht(Foerderschacht.TYP_1ROLLE);
		
		FoerderschachtSeil seil = new FoerderschachtSeil();
		FoerderschachtSeil unterseil = new FoerderschachtSeil();
		
		FoerderschachtKapsel kapsel1 = new FoerderschachtKapsel(schacht);
		kapsel1.setMass(19780);
		kapsel1.setZuladung(20000);
		
		FoerderschachtKapsel kapsel2 = new FoerderschachtKapsel(schacht);
		kapsel2.setMass(19780);
		kapsel2.setZuladung(20000);
		
		FoerderschachtUmlenkrolle umlenkrolle1 = new FoerderschachtUmlenkrolle(schacht);
		
		FoerderschachtTreibscheibe treibscheibe = new FoerderschachtTreibscheibe(schacht);
		
		Map<String, Double> doubleFaktoren = new HashMap<String, Double>();
		Map<String, Integer> intFaktoren = new HashMap<String, Integer>();
		
		seil.addElement(kapsel1);
		seil.addElement(treibscheibe);
		seil.addElement(umlenkrolle1);
		seil.addElement(kapsel2);
		
		seil.setBiegelaenge(25000);
		
		doubleFaktoren.put(CommonConstants.PARAMETER_NAME_FS1, 1.05);
		doubleFaktoren.put(CommonConstants.PARAMETER_NAME_FS3, 1.25);
		doubleFaktoren.put(CommonConstants.PARAMETER_NAME_FS4, 1.2);
		doubleFaktoren.put(CommonConstants.PARAMETER_NAME_FS5, 1.0);
		doubleFaktoren.put(CommonConstants.PARAMETER_NAME_FN1, 1.0);
		
		intFaktoren.put(CommonConstants.PARAMETER_NAME_H1, (int)(14000 / (0.14 * 4)));
		intFaktoren.put(CommonConstants.PARAMETER_NAME_H2, (int)(1000 / (0.14 * 4)));
		intFaktoren.put(CommonConstants.PARAMETER_NAME_H3, (int)(1000 / (0.14 * 4)));
		intFaktoren.put(CommonConstants.PARAMETER_NAME_A, (int)(2000 / (0.14 * 4)));
		
		// H1 + H2 + H3 = (int)(18000 / (0.14 * 4))
		// H1 - (H2 - H3 - A) = (int)(18000 / (0.14 * 4))
		intFaktoren.put(CommonConstants.PARAMETER_NAME_H1, (int)(28571));
		intFaktoren.put(CommonConstants.PARAMETER_NAME_H2, (int)(1786));
		intFaktoren.put(CommonConstants.PARAMETER_NAME_H3, (int)(1786));
		intFaktoren.put(CommonConstants.PARAMETER_NAME_A, (int)(3571));
		
		int nt = 1;
		double toleranz = 1.0;
		double fn1 = 1.0;
		double fs1 = 1.05;
		double fs3 = 1.25;
		double fs4 = 1.2;
		double fs5 = 1.0; // dummy
		double[] nb = new double[] {-1.194, -1.51, 1.322, 8.07, -0.32, -2.649, 1.2, 1.9, 0.94, 0.35, 1770, 18, 2.55};
		
		Berechnung berechnung = new Berechnung(seil, unterseil, kapsel1, kapsel2, nt, toleranz, fn1, fs1, fs3, fs4, fs5, nb);
		berechnung.setDoubleParameters(doubleFaktoren);
		berechnung.setIntParameters(intFaktoren);
		
		// DER Unterschied
		// -->
		
		Summenobjekt[][] summenArrayKeine = {{Summenobjekt.F_Q_M, Summenobjekt.F_Q_M, Summenobjekt.F_Q_M, Summenobjekt.F_Q_M,
									Summenobjekt.F_M, Summenobjekt.F_M, Summenobjekt.F_Q_M, Summenobjekt.F_Q_M},
									{Summenobjekt.NULL, Summenobjekt.NULL, Summenobjekt.NULL, Summenobjekt.NULL,
									Summenobjekt.NULL, Summenobjekt.NULL, Summenobjekt.NULL, Summenobjekt.NULL}};
		
		// <--
		
		long[][][] erg = berechnung.calculate(summenArrayKeine);
		
		assertNotNull(erg);
		// eigentlich 163.973, 264.161
		assertEquals(163978L, erg[1][0][0]);
		assertEquals(264168L, erg[1][0][1]);
	}
	
	@Test
	public void testBerechnungTurmfoerderungNurAufTS() {
		Foerderschacht schacht = new Foerderschacht(Foerderschacht.TYP_1ROLLE);
		
		FoerderschachtSeil seil = new FoerderschachtSeil();
		FoerderschachtSeil unterseil = new FoerderschachtSeil();
		
		FoerderschachtKapsel kapsel1 = new FoerderschachtKapsel(schacht);
		kapsel1.setMass(19780);
		kapsel1.setZuladung(20000);
		
		FoerderschachtKapsel kapsel2 = new FoerderschachtKapsel(schacht);
		kapsel2.setMass(19780);
		kapsel2.setZuladung(20000);
		
		FoerderschachtUmlenkrolle umlenkrolle1 = new FoerderschachtUmlenkrolle(schacht);
		
		FoerderschachtTreibscheibe treibscheibe = new FoerderschachtTreibscheibe(schacht);
		
		Map<String, Double> doubleFaktoren = new HashMap<String, Double>();
		Map<String, Integer> intFaktoren = new HashMap<String, Integer>();
		
		seil.addElement(kapsel1);
		seil.addElement(treibscheibe);
		seil.addElement(umlenkrolle1);
		seil.addElement(kapsel2);
		
		seil.setBiegelaenge(25000);
		
		doubleFaktoren.put(CommonConstants.PARAMETER_NAME_FS1, 1.05);
		doubleFaktoren.put(CommonConstants.PARAMETER_NAME_FS3, 1.25);
		doubleFaktoren.put(CommonConstants.PARAMETER_NAME_FS4, 1.2);
		doubleFaktoren.put(CommonConstants.PARAMETER_NAME_FS5, 1.0);
		doubleFaktoren.put(CommonConstants.PARAMETER_NAME_FN1, 1.0);
		
		intFaktoren.put(CommonConstants.PARAMETER_NAME_H1, (int)(14000 / (0.14 * 4)));
		intFaktoren.put(CommonConstants.PARAMETER_NAME_H2, (int)(1000 / (0.14 * 4)));
		intFaktoren.put(CommonConstants.PARAMETER_NAME_H3, (int)(1000 / (0.14 * 4)));
		intFaktoren.put(CommonConstants.PARAMETER_NAME_A, (int)(2000 / (0.14 * 4)));
		
		// H1 + H2 + H3 = (int)(18000 / (0.14 * 4))
		// H1 - (H2 - H3 - A) = (int)(18000 / (0.14 * 4))
		intFaktoren.put(CommonConstants.PARAMETER_NAME_H1, (int)(28571));
		intFaktoren.put(CommonConstants.PARAMETER_NAME_H2, (int)(1786));
		intFaktoren.put(CommonConstants.PARAMETER_NAME_H3, (int)(1786));
		intFaktoren.put(CommonConstants.PARAMETER_NAME_A, (int)(3571));
		
		int nt = 1;
		double toleranz = 1.0;
		double fn1 = 1.0;
		double fs1 = 1.05;
		double fs3 = 1.25;
		double fs4 = 1.2;
		double fs5 = 1.0; // dummy
		double[] nb = new double[] {-1.194, -1.51, 1.322, 8.07, -0.32, -2.649, 1.2, 1.9, 0.94, 0.35, 1770, 18, 2.55};
		
		Berechnung berechnung = new Berechnung(seil, unterseil, kapsel1, kapsel2, nt, toleranz, fn1, fs1, fs3, fs4, fs5, nb);
		berechnung.setDoubleParameters(doubleFaktoren);
		berechnung.setIntParameters(intFaktoren);
		
		// DER Unterschied
		// -->
		
		Summenobjekt[][] summenArrayNurAufTS = {{Summenobjekt.F_Q_M, Summenobjekt.F_Q_M, Summenobjekt.F_Q_M, Summenobjekt.F_Q_M,
									Summenobjekt.F_M, Summenobjekt.F_M, Summenobjekt.F_Q_M, Summenobjekt.F_Q_M},
									{Summenobjekt.NULL, Summenobjekt.NULL, Summenobjekt.NULL, Summenobjekt.Q,
									Summenobjekt.NULL, Summenobjekt.NULL, Summenobjekt.NULL, Summenobjekt.Q}};
		
		// <--
		
		long[][][] erg = berechnung.calculate(summenArrayNurAufTS);
		
		assertNotNull(erg);
		// eigentlich 63.919, 121.151
		assertEquals(63920L, erg[1][0][0]);
		assertEquals(121152L, erg[1][0][1]);
	}
	
	@Test
	public void testBerechnungTurmfoerderungNurBewegt() {
		Foerderschacht schacht = new Foerderschacht(Foerderschacht.TYP_1ROLLE);
		
		FoerderschachtSeil seil = new FoerderschachtSeil();
		FoerderschachtSeil unterseil = new FoerderschachtSeil();
		
		FoerderschachtKapsel kapsel1 = new FoerderschachtKapsel(schacht);
		kapsel1.setMass(19780);
		kapsel1.setZuladung(20000);
		
		FoerderschachtKapsel kapsel2 = new FoerderschachtKapsel(schacht);
		kapsel2.setMass(19780);
		kapsel2.setZuladung(20000);
		
		FoerderschachtUmlenkrolle umlenkrolle1 = new FoerderschachtUmlenkrolle(schacht);
		
		FoerderschachtTreibscheibe treibscheibe = new FoerderschachtTreibscheibe(schacht);
		
		Map<String, Double> doubleFaktoren = new HashMap<String, Double>();
		Map<String, Integer> intFaktoren = new HashMap<String, Integer>();
		
		seil.addElement(kapsel1);
		seil.addElement(treibscheibe);
		seil.addElement(umlenkrolle1);
		seil.addElement(kapsel2);
		
		seil.setBiegelaenge(25000);
		
		doubleFaktoren.put(CommonConstants.PARAMETER_NAME_FS1, 1.05);
		doubleFaktoren.put(CommonConstants.PARAMETER_NAME_FS3, 1.25);
		doubleFaktoren.put(CommonConstants.PARAMETER_NAME_FS4, 1.2);
		doubleFaktoren.put(CommonConstants.PARAMETER_NAME_FS5, 1.0);
		doubleFaktoren.put(CommonConstants.PARAMETER_NAME_FN1, 1.0);
		
		intFaktoren.put(CommonConstants.PARAMETER_NAME_H1, (int)(14000 / (0.14 * 4)));
		intFaktoren.put(CommonConstants.PARAMETER_NAME_H2, (int)(1000 / (0.14 * 4)));
		intFaktoren.put(CommonConstants.PARAMETER_NAME_H3, (int)(1000 / (0.14 * 4)));
		intFaktoren.put(CommonConstants.PARAMETER_NAME_A, (int)(2000 / (0.14 * 4)));
		
		// H1 + H2 + H3 = (int)(18000 / (0.14 * 4))
		// H1 - (H2 - H3 - A) = (int)(18000 / (0.14 * 4))
		intFaktoren.put(CommonConstants.PARAMETER_NAME_H1, (int)(28571));
		intFaktoren.put(CommonConstants.PARAMETER_NAME_H2, (int)(1786));
		intFaktoren.put(CommonConstants.PARAMETER_NAME_H3, (int)(1786));
		intFaktoren.put(CommonConstants.PARAMETER_NAME_A, (int)(3571));
		
		int nt = 1;
		double toleranz = 1.0;
		double fn1 = 1.0;
		double fs1 = 1.05;
		double fs3 = 1.25;
		double fs4 = 1.2;
		double fs5 = 1.0; // dummy
		double[] nb = new double[] {-1.194, -1.51, 1.322, 8.07, -0.32, -2.649, 1.2, 1.9, 0.94, 0.35, 1770, 18, 2.55};
		
		Berechnung berechnung = new Berechnung(seil, unterseil, kapsel1, kapsel2, nt, toleranz, fn1, fs1, fs3, fs4, fs5, nb);
		berechnung.setDoubleParameters(doubleFaktoren);
		berechnung.setIntParameters(intFaktoren);
		
		// DER Unterschied
		// -->
		
		Summenobjekt[][] summenArrayNurBewegt = {{Summenobjekt.F_Q_M, Summenobjekt.F_Q_M, Summenobjekt.F_Q_M, Summenobjekt.F_Q_M,
									Summenobjekt.F_M, Summenobjekt.F_M, Summenobjekt.F_Q_M, Summenobjekt.F_Q_M},
									{Summenobjekt.NULL, Summenobjekt.NULL, Summenobjekt.NULL, Summenobjekt.Q_M,
									Summenobjekt.NULL, Summenobjekt.NULL, Summenobjekt.M, Summenobjekt.Q}};
		
		// <--
		
		long[][][] erg = berechnung.calculate(summenArrayNurBewegt);
		
		assertNotNull(erg);
		assertEquals(30970L, erg[1][0][0]);
		assertEquals(66102L, erg[1][0][1]);
	}
	
	@Test
	public void testBerechnungTurmfoerderungAlle() {
		Foerderschacht schacht = new Foerderschacht(Foerderschacht.TYP_1ROLLE);
		
		FoerderschachtSeil seil = new FoerderschachtSeil();
		FoerderschachtSeil unterseil = new FoerderschachtSeil();
		
		FoerderschachtKapsel kapsel1 = new FoerderschachtKapsel(schacht);
		kapsel1.setMass(19780);
		kapsel1.setZuladung(20000);
		
		FoerderschachtKapsel kapsel2 = new FoerderschachtKapsel(schacht);
		kapsel2.setMass(19780);
		kapsel2.setZuladung(20000);
		
		FoerderschachtUmlenkrolle umlenkrolle1 = new FoerderschachtUmlenkrolle(schacht);
		
		FoerderschachtTreibscheibe treibscheibe = new FoerderschachtTreibscheibe(schacht);
		
		Map<String, Double> doubleFaktoren = new HashMap<String, Double>();
		Map<String, Integer> intFaktoren = new HashMap<String, Integer>();
		
		seil.addElement(kapsel1);
		seil.addElement(treibscheibe);
		seil.addElement(umlenkrolle1);
		seil.addElement(kapsel2);
		
		seil.setBiegelaenge(25000);
		
		doubleFaktoren.put(CommonConstants.PARAMETER_NAME_FS1, 1.05);
		doubleFaktoren.put(CommonConstants.PARAMETER_NAME_FS3, 1.25);
		doubleFaktoren.put(CommonConstants.PARAMETER_NAME_FS4, 1.2);
		doubleFaktoren.put(CommonConstants.PARAMETER_NAME_FS5, 1.0);
		doubleFaktoren.put(CommonConstants.PARAMETER_NAME_FN1, 1.0);
		
		intFaktoren.put(CommonConstants.PARAMETER_NAME_H1, (int)(14000 / (0.14 * 4)));
		intFaktoren.put(CommonConstants.PARAMETER_NAME_H2, (int)(1000 / (0.14 * 4)));
		intFaktoren.put(CommonConstants.PARAMETER_NAME_H3, (int)(1000 / (0.14 * 4)));
		intFaktoren.put(CommonConstants.PARAMETER_NAME_A, (int)(2000 / (0.14 * 4)));
		
		// H1 + H2 + H3 = (int)(18000 / (0.14 * 4))
		// H1 - (H2 - H3 - A) = (int)(18000 / (0.14 * 4))
		intFaktoren.put(CommonConstants.PARAMETER_NAME_H1, (int)(28571));
		intFaktoren.put(CommonConstants.PARAMETER_NAME_H2, (int)(1786));
		intFaktoren.put(CommonConstants.PARAMETER_NAME_H3, (int)(1786));
		intFaktoren.put(CommonConstants.PARAMETER_NAME_A, (int)(3571));
		
		int nt = 1;
		double toleranz = 1.0;
		double fn1 = 1.0;
		double fs1 = 1.05;
		double fs3 = 1.25;
		double fs4 = 1.2;
		double fs5 = 1.0; // dummy
		double[] nb = new double[] {-1.194, -1.51, 1.322, 8.07, -0.32, -2.649, 1.2, 1.9, 0.94, 0.35, 1770, 18, 2.55};
		
		Berechnung berechnung = new Berechnung(seil, unterseil, kapsel1, kapsel2, nt, toleranz, fn1, fs1, fs3, fs4, fs5, nb);
		berechnung.setDoubleParameters(doubleFaktoren);
		berechnung.setIntParameters(intFaktoren);
		
		// DER Unterschied
		// -->
		
		Summenobjekt[][] summenArrayAlle = {{Summenobjekt.F_Q_M, Summenobjekt.F_Q_M, Summenobjekt.F_Q_M, Summenobjekt.F_Q_M,
									Summenobjekt.F_M, Summenobjekt.F_M, Summenobjekt.F_Q_M, Summenobjekt.F_Q_M},
									{Summenobjekt.Q, Summenobjekt.NULL, Summenobjekt.NULL, Summenobjekt.Q_M,
									Summenobjekt.NULL, Summenobjekt.NULL, Summenobjekt.Q_M, Summenobjekt.Q}};
		
		// <--
		
		long[][][] erg = berechnung.calculate(summenArrayAlle);
		
		assertNotNull(erg);
		// eigentlich 19.407, 43.307
		assertEquals(19407L, erg[1][0][0]);
		assertEquals(43308L, erg[1][0][1]);
	}

}
