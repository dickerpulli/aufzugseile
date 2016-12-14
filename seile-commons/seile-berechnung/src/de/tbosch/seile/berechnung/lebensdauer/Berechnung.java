package de.tbosch.seile.berechnung.lebensdauer;

import de.tbosch.seile.commons.CommonConstants;
import de.tbosch.seile.commons.elemente.DoppelUmlenkrolle;
import de.tbosch.seile.commons.elemente.Kapsel;
import de.tbosch.seile.commons.elemente.Rolle;
import de.tbosch.seile.commons.elemente.Seil;
import de.tbosch.seile.commons.elemente.SeilElement;
import de.tbosch.seile.commons.elemente.Seilaufhaenger;
import de.tbosch.seile.commons.elemente.Treibscheibe;
import de.tbosch.seile.commons.elemente.Umlenkrolle;
import java.io.Serializable;
import java.util.Map;
import java.util.Vector;
import org.apache.log4j.Logger;

/**
 * The Class Berechnung.
 */
public class Berechnung implements Serializable {
	
	/** Logger */
	private static final Logger logger = Logger.getLogger(Berechnung.class.getName());
	
	/** The physical parameter g. */
	public static double G_ERDBESCHL = 9.81;

	/** fs1: Reibung der Lastfuehrung. */
	private double fs1;
	
	/** fs3: parallele Seile. */
	private double fs3;

	/** fs4: Beschleunigung. */
	private double fs4;

	/** fs5: Schlaffseilstoss. */
	private double fs5;
	
	/** fn1: Seilschmierung. */
	private double fn1;
	
	/** Höhen */
	private int h1;
	private int h2;
	private int h3;
	private int ha;
	
	/** tolleranz */
	private double toleranz;
	
	/** The factor a as array of dimension NA's * factor count. Here: [2][3].  NA, NA10 * a0, a1, a2 */
	private static double a[][] = {{3.635, 0.671, 0.499}, // NA 
									{2.67, 0.671, 0.499}}; // NA10
	
	/** The divisor. */
	private int nt;
	
	/** The seil. */
	private Seil seil;
	
	/** The underseil. */
	private Seil unterseil;
	
	/** The kapsel1. */
	private Kapsel kapsel1;
	
	/** The kapsel2. */
	private Kapsel kapsel2;
	
	/** Korrekturparameter. */
	private Vector<double[]> bs = new Vector<double[]>();

	/** The rolls that are in the calculation. */
	private Vector<CalcRolle> calcRolls = new Vector<CalcRolle>();
	
	/** The already calculated rolls. */
	private Vector<Rolle> alreadyCalculated = new Vector<Rolle>();
	
	private boolean calculated = false;
	
	/**
	 * Ein Summenobjekt bestimmt die Massen, welche addiert werden müssen.
	 * Ein F_Q_M Objekt sagt, dass F, Q und M addiert werden müssen.
	 */
	public enum Summenobjekt {
		F_Q_M,
		F_M,
		Q_M,
		Q,
		M,
		NULL
	}
	
	/**
	 * The Constructor.
	 * BE CAREFULL: Initializations must be done later
	 */
	public Berechnung() {
	}
	
	/**
	 * The Constructor.
	 */
	public Berechnung(Seil seil, Seil unterseil, Kapsel kapsel1, Kapsel kapsel2,
			int nt, double toleranz, double fn1,
			double fs1, double fs3, double fs4, double fs5, double[] nb) {
		setElements(seil, kapsel1, kapsel2);
		setNt(nt);
		setToleranz(toleranz);
		setFn1(fn1);
		setFs1(fs1);
		setFs3(fs3);
		setFs4(fs4);
		this.fs5 = fs5;
		this.unterseil = unterseil;
		resetBsFn2MassFestigkeit();
		addBsFn2MassFestigkeit(nb);
	}
	
	/**
	 * Sets the elements.
	 * 
	 * @param kapsel2 the kapsel2
	 * @param kapsel1 the kapsel1
	 * @param seil the seil
	 */
	public void setElements(Seil seil, Kapsel kabine, Kapsel gewicht) {
		this.seil = seil;
		this.kapsel1 = kabine;
		this.kapsel2 = gewicht;
	}
	
	/**
	 * Sets the fs1.
	 * 
	 * @param fs1 the fs1
	 */
	public void setFs1(double fs1) {
		this.fs1 = fs1;
	}

	/**
	 * Sets the fs3.
	 * 
	 * @param fs3 the fs3
	 */
	public void setFs3(double fs3) {
		this.fs3 = fs3;
	}

	/**
	 * Sets the fs4.
	 * 
	 * @param fs4 the fs4
	 */
	public void setFs4(double fs4) {
		this.fs4 = fs4;
	}

	/**
	 * Sets the fn1.
	 * 
	 * @param fn1 the fn1
	 */
	public void setFn1(double fn1) {
		this.fn1 = fn1;
	}

	/**
	 * Gets the fn1.
	 * 
	 * @return the fn1
	 */
	public double getFn1() {
		return fn1;
	}
	
	/**
	 * Gets the fs5.
	 * 
	 * @return the fs5
	 */
	public double getFs5() {
		return fs5;
	}

	/**
	 * Sets the fs5
	 * 
	 * @param fn1 the fs5
	 */
	public void setFs5(double fs5) {
		this.fs5 = fs5;
	}
	
	/**
	 * Sets the nt.
	 * 
	 * @param aufhaengung the nt
	 */
	public void setNt(int aufhaengung) {
		if (seil != null) nt = aufhaengung * seil.getCount();
	}
	
	/**
	 * Calculates the rope force. 
	 * Including correction doubleParameters fs1 ... fs5
	 * 
	 * @param rolle the roll
	 * @param j the index of the rope
	 * @param s roll direction
	 * @param p in/out
	 * @param summenobjekte  Die Parameter für die relevante Masse von S und delS
	 * 
	 * @return the force in N
	 */
	private double seilkraft(Rolle rolle, int j, int s, int p, Summenobjekt[][] summenobjekte) {
		
		// schlaffseilstoss fs5: hier irrelevant
		// rope mass = mass (kg/100m/mm^2) / 100 * length (m) * width (mm) * rope count 
		//double seilmasse = (bs.get(j)[9] / 100) * schachthoehe * Math.pow(seil.getD(), 2) * seil.getCount();
		
		// rope mass = mass (kg/100m/mm^2) / 100 * length (m) * width (mm) * rope count 
		// int oberseilmasse = (int)(seil.getMassPerM() * seil.getLength() * seil.getCount());
		// int unterseilmasse = (int)(unterseil.getMassPerM() * unterseil.getLength() * unterseil.getCount());
		int seilmasseKabine = (int)((seil.getMassPerM() * (h1) * seil.getCount()) + (unterseil.getMassPerM() * (h2 + h3) * unterseil.getCount()));
		int seilmasseGewicht = (int)((seil.getMassPerM() * (h1) * seil.getCount()) - (unterseil.getMassPerM() * (h2 - h3 - ha) * unterseil.getCount()));
		long q = 0;
		//long qmin = 0;
		long delQ = 0;
		if (kapsel1 != null && kapsel2 != null) {
			int kabineMass = kapsel1.getMass();
			int gewichtMass = kapsel2.getMass();
			int kabineGesamtMass = kabineMass + (int)(kapsel1.getZuladung() * kapsel1.getDurchschnitt());
			int gewichtGesamtMass = gewichtMass + (int)(kapsel2.getZuladung() * kapsel2.getDurchschnitt());
			int kabineZuladung = ((int)(kapsel1.getZuladung() * kapsel1.getDurchschnitt()));
			int gewichtZuladung = ((int)(kapsel2.getZuladung() * kapsel2.getDurchschnitt()));
			
			if (rolle instanceof Treibscheibe && (rolle.isDoppelteUmschlingung() || rolle.isZweiteUmschlingung())) {
				// TODO: VERALLGEMEINERN
				logger.error("Ups! doch wieder mit Aufzugseilen. Methode anpassen.");
				//q = (long)Math.round(seilmasse + ((kabineGesamtMass + gewichtGesamtMass) / (double)2));
			}
			else if (rolle instanceof Treibscheibe) {
				if (kabineGesamtMass >= gewichtGesamtMass) {
					//q = (long)Math.round(seilmasseKabine + kabineGesamtMass);
					//qmin = q - kabineZuladung;
					q = holeRelevanteMasse(s, 1, p, 0, kabineMass, kabineZuladung, seilmasseKabine, summenobjekte);
					delQ = holeRelevanteMasse(s, 1, p, 1, kabineMass, kabineZuladung, seilmasseKabine, summenobjekte);
				}
				else {
					//q = (long)Math.round(seilmasseKabine + gewichtGesamtMass);
					//qmin = q - gewichtZuladung;
					q = holeRelevanteMasse(s, 1, p, 0, gewichtMass, gewichtZuladung, seilmasseGewicht, summenobjekte);
					delQ = holeRelevanteMasse(s, 1, p, 1, gewichtMass, gewichtZuladung, seilmasseGewicht, summenobjekte);
				}
			}
			else {
				if (rolle.getSeilElement(true) != null && rolle.getSeilElement(true).getPrev().getElement() instanceof Treibscheibe 
						&& ((Treibscheibe)rolle.getSeilElement(true).getPrev().getElement()).isDoppelteUmschlingung() 
						&& rolle.getSeilElement(true).getPrev().getElement().equals(rolle.getSeilElement(true).getNext().getElement())) {
					// TODO: VERALLGEMEINERN
					logger.error("Ups! doch wieder mit Aufzugseilen. Methode anpassen.");
					//q = (long)Math.round(seilmasse + ((kabineGesamtMass + gewichtGesamtMass) / (double)2));
				}
				else if (richtungGewicht(rolle)) {
					//if (s == 1) {
					//	gewichtGesamtMass = gewichtGesamtMass - gewichtZuladung;
					//}
					//q = (long)Math.round(seilmasseGewicht + gewichtGesamtMass);
					//qmin = q;
					q = holeRelevanteMasse(s, 0, p, 0, gewichtMass, gewichtZuladung, seilmasseGewicht, summenobjekte);
					delQ = holeRelevanteMasse(s, 0, p, 1, gewichtMass, gewichtZuladung, seilmasseGewicht, summenobjekte);
				}
				else {
					//if (s == 1) {
					//	kabineGesamtMass = kabineGesamtMass - kabineZuladung;
					//}
					//q = (long)Math.round(seilmasseGewicht + kabineGesamtMass);
					//qmin = q;
					q = holeRelevanteMasse(s, 0, p, 0, kabineMass, kabineZuladung, seilmasseKabine, summenobjekte);
					delQ = holeRelevanteMasse(s, 0, p, 1, kabineMass, kabineZuladung, seilmasseKabine, summenobjekte);
				}
			}
			
			logger.debug("Mass: "+rolle.getName()+rolle.getID()+" - "+q);
		}
		else {
			logger.error("Calculation should not be done!");
		}
				
		double fs = fs1 * rolle.getFs2() * fs3 * fs4;
		double S = ((q * G_ERDBESCHL)/ nt);
		//double Smin = ((qmin * G_ERDBESCHL)/ nt);
		double delS = ((delQ * G_ERDBESCHL)/ nt);
		//double delS = S - Smin;
		double N = S * fs * ermittleFs5(S, rolle, j, fs, delS);
		logger.debug("Newton: "+N);
		return N;
	}
	
	private long holeRelevanteMasse(int lauf, int rolle, int inout, int i, long f, long q, long m, Summenobjekt[][] summenobjekte) {
		long masse = 0;
		Summenobjekt summenobjekt = summenobjekte[i][(int)(Math.log10(Math.pow(lauf + 1, 4) * Math.pow(rolle + 1, 2) * Math.pow(inout + 1, 1)) / Math.log10(2))];
		switch (summenobjekt) {
			case F_M:
				masse = f + m;
				break;
			case F_Q_M:
				masse = f + q + m;
				break;
			case Q_M:
				masse = q + m;
				break;
			case Q:
				masse = q;
				break;
			case M:
				masse = m;
				break;
			case NULL:
				masse = 0;
				break;
		}
		return masse;
	}
	
	private boolean richtungGewicht(Rolle rolle) {
		if ((rolleVorTreibscheibe(rolle) && gewichtVorTreibscheibe())
				|| (!rolleVorTreibscheibe(rolle) && !gewichtVorTreibscheibe())) {
			return true;
		}
		return false;
	}
	
	private double ermittleFs5(double S, Rolle rolle, int j, double fs, double delS) {
		double axx = bs.get(j)[12];
		double D = rolle.getDurchmesser();
		double ddel = bs.get(j)[11];
		double d = seil.getD();
		double va = 0;
		
		if (delS / S <= 0.099) return 1;
		
		double zaehler = (1.31 - 0.0014 * axx * (delS * fs / Math.pow(d, 2))) * (1.1 * (delS * fs / Math.pow(d, 2)) - 0.1 * (S * fs / Math.pow(d, 2))) * axx;
		double nenner = 145000 * (1 / ddel) * (d / D) + 600 * (1 / ddel) + 0.2 * axx * (S * fs / Math.pow(d, 2));
		double ergebnis = 1 + ((zaehler) / (nenner)) + 0.5 * va;
		logger.debug("fs5: "+ergebnis);
		
		return ergebnis;
	}

	/**
	 * Checks, if the roll is before the treibscheibe in
	 * SeilElementVector of rope.
	 * 
	 * @param rolle the roll
	 * 
	 * @return true, if roll vor treibscheibe
	 */
	private boolean rolleVorTreibscheibe(Rolle rolle) {
		Vector<SeilElement> seilElementVector = seil.getSeilElementVector();
		for (int i = 0; i < seilElementVector.size(); i++) {
			if (seilElementVector.get(i).getElement() instanceof Treibscheibe) {
				break;
			}
			else if (seilElementVector.get(i).getElement().equals(rolle)
					|| (seilElementVector.get(i).getElement() instanceof DoppelUmlenkrolle
						&& ((DoppelUmlenkrolle)seilElementVector.get(i).getElement()).getRolle1().equals(rolle))
					|| (seilElementVector.get(i).getElement() instanceof DoppelUmlenkrolle
						&& ((DoppelUmlenkrolle)seilElementVector.get(i).getElement()).getRolle2().equals(rolle))) {
				logger.debug("Rolle "+rolle.getName()+rolle.getID()+" before Treibscheibe");
				return true;
			}
		}
		logger.debug("Rolle "+rolle.getName()+rolle.getID()+" not before Treibscheibe");
		return false;
	}
	
	/**
	 * Checks, if the counter weight is before the treibscheibe in
	 * SeilElementVector of rope
	 * 
	 * @return true, if kapsel2 vor treibscheibe
	 */
	private boolean gewichtVorTreibscheibe() {
		Vector<SeilElement> seilElementVector = seil.getSeilElementVector();
		for (int i = 0; i < seilElementVector.size(); i++) {
			if (seilElementVector.get(i).getElement() instanceof Treibscheibe) {
				break;
			}
			else if (seilElementVector.get(i).getElement() instanceof Seilaufhaenger) {
				if (((Seilaufhaenger)seilElementVector.get(i).getElement()).getHangingElement() != null 
						&& ((Seilaufhaenger)seilElementVector.get(i).getElement()).getHangingElement().equals(kapsel2)) {
					logger.debug("Gewicht before Treibscheibe");	
					return true;
				}
			}
			else if (seilElementVector.get(i).getElement() instanceof Umlenkrolle) {
				if (((Umlenkrolle)seilElementVector.get(i).getElement()).getHangingElement() != null 
						&& ((Umlenkrolle)seilElementVector.get(i).getElement()).getHangingElement().equals(kapsel2)) {
					logger.debug("Gewicht before Treibscheibe");	
					return true;
				}
			}
		}
		logger.debug("Gewicht not before Treibscheibe");
		return false;
	}
	
	/**
	 * Calculates the number of flexing cycles.
	 * 
	 * @param rolle the roll
	 * @param a the index of the factor to calculate (0 for 'N A' and 1 for 'N A10')
	 * @param j the index of the rope
	 * @param s roll direction
	 * @param p in/out
	 * @param summenobjekte  Die Parameter für die relevante Masse von S und delS
	 * 
	 * @return the flexing cycles
	 */
	private long biegewechsel(Rolle rolle, int j, int a, int s, int p, Summenobjekt[][] summenobjekte) {
		double D = rolle.getDurchmesser();
		double d = seil.getD();
		double S = seilkraft(rolle, j, s, p, summenobjekte);
		double R0 = bs.get(j)[10];
		double l = seil.getL();
		double b0 = bs.get(j)[1];
		if (a == 0) {
			b0 = bs.get(j)[0];
		}
		double b1 = bs.get(j)[2];
		double b2 = bs.get(j)[3];
		double b3 = bs.get(j)[4];
		double b4 = bs.get(j)[5];
		double b5 = bs.get(j)[7];
		if (a == 0) {
			b5 = bs.get(j)[6];
		}
		
		// S = korrigiereSeilkraftUmFs5(S, rolle, j);
		
		double logN = b0 + (b1 + b4 * Math.log10(D / d)) * (Math.log10(S / Math.pow(d, 2)) - 0.4 * Math.log10(R0 / 1770)) + b2 * Math.log10(D / d) + b3 * Math.log10(d) + (1 / (b5 + Math.log10(l / d)));
		
		long N = Math.round(Math.pow(10, logN));
		logger.debug("Biegewechel: "+N);
		return N;
	}
	
	/**
	 * Correct the flexing cycles with the correction factors fn1 ... fn4.
	 * 
	 * @param rolle the roll
	 * @param a the index of the factor to calculate (0 for 'N A' and 1 for 'N A10')
	 * @param j the index of the rope
	 * @param s roll direction
	 * @param p in/out
	 * @param summenobjekte  Die Parameter für die relevante Masse von S und delS
	 * 
	 * @return the corrected flexing cycles
	 */
	private long korrBiegewechsel(Rolle rolle, int j, int a, int s, int p, Summenobjekt[][] summenobjekte) {
		long bw = biegewechsel(rolle, j, a, s, p, summenobjekte);
		
		double fn2 = bs.get(j)[8];
		
		return Math.round(bw * fn1 * fn2 * rolle.getFn3() * rolle.getFn4());
	}
	
	/**
	 * Checks if all factor are given to calculate.
	 * 
	 * @return true, if input is OK
	 */
	public boolean inputOK() {
		if (seil != null
				&& fs1 != 0
				&& fs3 != 0
				&& fs4 != 0
				&& fn1 != 0
				&& nt != 0
				&& kapsel1 != null
				&& kapsel2 != null
				&& !bs.isEmpty()) {
			return true;
		}
		return false;
	}
	
	/**
	 * Calculates the flexing cycles and gives a solution array back.
	 * @param summenobjekte  Die Parameter für die relevante Masse von S und delS
	 * 
	 * @return the long[][]
	 */
	public long[][][] calculate(Summenobjekt[][] summenobjekte) {
		double fz = toleranz;
		double fahrten = kapsel1.getProfil();
		int as = 2;
		long ergArray[][][] = new long[as][bs.size()][2];

		// reset CalcRolls
		calcRolls.clear();
		
		for (int k = 0; k < as; k++) {
			for (int j = 0; j < bs.size(); j++) {	
				double summe = 0;
				double summeNk = 0;
				for (int s = 0; s < 2; s++) {
					for (int p = 0; p < 2; p++) {
						for (int i = 0; i < seil.getSeilElementVector().size(); i++) {
							if (seil.getSeilElementVector().get(i).getElement() instanceof Rolle) {
								Rolle rolle = (Rolle)seil.getSeilElementVector().get(i).getElement();
								if (rolle.isActivated()) {
									long korrB = korrBiegewechsel(rolle, j, k, s, p, summenobjekte);
									long korrBGeg = korrB;
									if (rolle.isGegenbiegung()) {
										korrBGeg = korrGegenbiegung(korrB, rolle, k);
									}
									summe += 1d / (korrBGeg * 2);
									summeNk += 1d / (korrBGeg * 2 * 8.37 * Math.pow(korrBGeg, -0.124));
									CalcRolle newCalcRolle = null;
									boolean old = false;
									for (int r = 0; r < calcRolls.size(); r++) {
										if (calcRolls.get(r).getRolle().equals(rolle)) {
											old = true;
											newCalcRolle = calcRolls.get(r);
											newCalcRolle.setLebensdauer(korrBGeg, k, j);
										}
									}
									if (!old) {
										logger.debug(rolle.getName()+rolle.getID() + " creates new CalcRolle.");
										newCalcRolle = new CalcRolle(rolle, as, bs.size());
										newCalcRolle.setLebensdauer(korrBGeg, k, j);
										calcRolls.add(newCalcRolle);
									}
								}						
							}
							if (seil.getSeilElementVector().get(i).getElement() instanceof DoppelUmlenkrolle) {
								DoppelUmlenkrolle doppelRolle = (DoppelUmlenkrolle)seil.getSeilElementVector().get(i).getElement();
								if (doppelRolle.isActivated()) {
									// 1st roll
									long korrB = korrBiegewechsel(doppelRolle.getRolle1(), j, k, s, p, summenobjekte);
									long korrBGeg = korrB;
									if (doppelRolle.getRolle1().isGegenbiegung()) {
										korrBGeg = korrGegenbiegung(korrB, doppelRolle.getRolle1(), k);
									}
									summe += 1d / (korrBGeg * 2);
									summeNk += 1d / (korrBGeg * 2 * 8.37 * Math.pow(korrBGeg, -0.124));

									// 2nd roll
									korrB = korrBiegewechsel(doppelRolle.getRolle2(), j, k, s, p, summenobjekte);
									korrBGeg = korrB;
									if (doppelRolle.getRolle2().isGegenbiegung()) {
										korrBGeg = korrGegenbiegung(korrB, doppelRolle.getRolle2(), k);
									}
									summe += 1d / (korrBGeg * 2);
									summeNk += 1d / (korrBGeg * 2 * 8.37 * Math.pow(korrBGeg, -0.124));

									CalcRolle newCalcRolle = null;
									boolean old1 = false;
									boolean old2 = false;
									for (int r = 0; r < calcRolls.size(); r++) {
										if (calcRolls.get(r).getRolle().equals(doppelRolle.getRolle1())) {
											old1 = true;
											newCalcRolle = calcRolls.get(r);
											newCalcRolle.setLebensdauer(korrBGeg, k, j);
										}
										if (calcRolls.get(r).getRolle().equals(doppelRolle.getRolle2())) {
											old2 = true;
											newCalcRolle = calcRolls.get(r);
											newCalcRolle.setLebensdauer(korrBGeg, k, j);
										}
									}
									if (!old1) {
										logger.debug(doppelRolle.getRolle1().getName() +doppelRolle.getRolle1().getID() + " creates new CalcRolle.");
										newCalcRolle = new CalcRolle(doppelRolle.getRolle1(), as, bs.size());
										newCalcRolle.setLebensdauer(korrBGeg, k, j);
										calcRolls.add(newCalcRolle);
									}
									if (!old2) {
										logger.debug(doppelRolle.getRolle2().getName() +doppelRolle.getRolle2().getID() + " creates new CalcRolle.");
										newCalcRolle = new CalcRolle(doppelRolle.getRolle2(), as, bs.size());
										newCalcRolle.setLebensdauer(korrBGeg, k, j);
										calcRolls.add(newCalcRolle);
									}
								}						
							}
						}
					}
				}
				alreadyCalculated.clear();
				long z = Math.round((fz / summe) / fahrten);
				long zNk = Math.round((fz / summeNk) / fahrten);
				ergArray[k][j][0] = z;
				ergArray[k][j][1] = zNk;
			}
		}
		
		calculated = true;
		
		return ergArray;
	}
	
	/**
	 * Correct flexing cycles if there is reverse bending.
	 * 
	 * @param rolle the roll
	 * @param indexA the index of the factor to calculate (0 for 'N A' and 1 for 'N A10')
	 * @param korrB the flexing cycles to correct
	 * 
	 * @return the long
	 */
	private long korrGegenbiegung(long korrB, Rolle rolle, int indexA) {
		double D = rolle.getDurchmesser();
		double d = seil.getD();
		
		long korr = Math.round(a[indexA][0] * Math.pow(korrB, a[indexA][1]) * Math.pow((D / d), a[indexA][2])); 
			
		return korr;
	}
	
	/**
	 * Reset bsfn2fs5massfestigkeit-vector.
	 * Just clear()
	 */
	public void resetBsFn2MassFestigkeit() {
		bs.clear();
	}

	/**
	 * Adds the bs, fn2, fs5, mass and festigkeit to the vector.
	 * 
	 * @param nb the nb
	 */
	public void addBsFn2MassFestigkeit(double nb[]) {
		bs.add(nb);
	}

	/**
	 * Gets the fs1.
	 * 
	 * @return the fs1
	 */
	public double getFs1() {
		return fs1;
	}

	/**
	 * Gets the fs3.
	 * 
	 * @return the fs3
	 */
	public double getFs3() {
		return fs3;
	}

	/**
	 * Gets the fs4.
	 * 
	 * @return the fs4
	 */
	public double getFs4() {
		return fs4;
	}

	/**
	 * Gets the calc rolls.
	 * 
	 * @return the calc rolls
	 */
	public Vector<CalcRolle> getCalcRolls() {
		return calcRolls;
	}

	/**
	 * Gets the bs.
	 * 
	 * @return the bs
	 */
	public Vector<double[]> getBs() {
		return bs;
	}

	public double getToleranz() {
		return toleranz;
	}

	public void setToleranz(double toleranz) {
		this.toleranz = toleranz;
	}
	
	/**
	 * Reset all calc rolls.
	 */
	public void resetCalcRolls() {
		calcRolls.clear();
		alreadyCalculated.clear();
	}

	public boolean isCalculated() {
		return calculated;
	}
	
	public void setDoubleParameters(Map<String, Double> doubleParameters) {
		fs1 = doubleParameters.get(CommonConstants.PARAMETER_NAME_FS1);
		// fs2 ist Rollenparameter
		fs3 = doubleParameters.get(CommonConstants.PARAMETER_NAME_FS3);
		fs4 = doubleParameters.get(CommonConstants.PARAMETER_NAME_FS4);
		fs5 = doubleParameters.get(CommonConstants.PARAMETER_NAME_FS5);
		fn1 = doubleParameters.get(CommonConstants.PARAMETER_NAME_FN1);
		// fn2 kommt von Seilparametern
		// fn3, fn4 sind Rollenparameter
	}
	
	public void setIntParameters(Map<String, Integer> intParameters) {
		h1 = intParameters.get(CommonConstants.PARAMETER_NAME_H1);
		h2 = intParameters.get(CommonConstants.PARAMETER_NAME_H2);
		h3 = intParameters.get(CommonConstants.PARAMETER_NAME_H3);
		ha = intParameters.get(CommonConstants.PARAMETER_NAME_A);
	}
	
}
