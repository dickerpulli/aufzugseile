package de.tbosch.aufzugseile.berechnung.lebensdauer;

import java.io.Serializable;
import java.util.Vector;

import de.tbosch.aufzugseile.gui.aufzug.DoppelUmlenkrolle;
import de.tbosch.aufzugseile.gui.aufzug.Gewicht;
import de.tbosch.aufzugseile.gui.aufzug.Kabine;
import de.tbosch.aufzugseile.gui.aufzug.Rolle;
import de.tbosch.aufzugseile.gui.aufzug.Treibscheibe;
import de.tbosch.aufzugseile.gui.aufzug.Umlenkrolle;
import de.tbosch.aufzugseile.gui.aufzug.seil.Seil;
import de.tbosch.aufzugseile.gui.aufzug.seil.SeilElement;
import de.tbosch.aufzugseile.gui.aufzug.seil.Seilaufhaenger;
import de.tbosch.aufzugseile.gui.aufzug.seil.Unterseil;
import de.tbosch.aufzugseile.utils.Constants;

// TODO: Auto-generated Javadoc
/**
 * The Class Berechnung.
 */
public class Berechnung implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2004289344699576453L;

	/** fs1: Reibung der Lastfuehrung. */
	private double fs1;

	/** fs3: parallele Seile. */
	private double fs3;

	/** fs4: Beschleunigung. */
	private double fs4;

	/** fn1: Seilschmierung. */
	private double fn1;

	/** tolleranz */
	private double toleranz;

	/** The factor a as array of dimension NA's * factor count. Here: [2][3]. NA, NA10 * a0, a1, a2 */
	private static double a[][] = { { 3.635, 0.671, 0.499 }, // NA
			{ 2.67, 0.671, 0.499 } }; // NA10

	/** The divisor. */
	private int nt;

	/** The seil. */
	private Seil seil;

	/** The kabine. */
	private Kabine kabine;

	/** The gewicht. */
	private Gewicht gewicht;

	/** The unterseil */
	private Unterseil unterseil;

	/** Korrekturparameter. */
	private Vector<double[]> bs = new Vector<double[]>();

	/** The rolls that are in the calculation. */
	private Vector<CalcRolle> calcRolls = new Vector<CalcRolle>();

	/** The already calculated rolls. */
	private Vector<Rolle> alreadyCalculated = new Vector<Rolle>();

	/** The schachthoehe. */
	private int schachthoehe;

	private boolean calculated = false;

	/**
	 * The Constructor.
	 */
	public Berechnung() {
	}

	/**
	 * Sets the elements.
	 * 
	 * @param gewicht
	 *            the gewicht
	 * @param kabine
	 *            the kabine
	 * @param seil
	 *            the seil
	 */
	public void setElements(Seil seil, Kabine kabine, Gewicht gewicht, Unterseil unterseil) {
		this.seil = seil;
		this.kabine = kabine;
		this.gewicht = gewicht;
		this.unterseil = unterseil;
	}

	/**
	 * Sets the schachthoehe.
	 * 
	 * @param schachthoehe
	 *            the schachthoehe
	 */
	public void setSchachthoehe(int schachthoehe) {
		this.schachthoehe = schachthoehe;
	}

	/**
	 * Sets the fs1.
	 * 
	 * @param fs1
	 *            the fs1
	 */
	public void setFs1(double fs1) {
		this.fs1 = fs1;
	}

	/**
	 * Sets the fs3.
	 * 
	 * @param fs3
	 *            the fs3
	 */
	public void setFs3(double fs3) {
		this.fs3 = fs3;
	}

	/**
	 * Sets the fs4.
	 * 
	 * @param fs4
	 *            the fs4
	 */
	public void setFs4(double fs4) {
		this.fs4 = fs4;
	}

	/**
	 * Sets the fn1.
	 * 
	 * @param fn1
	 *            the fn1
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
	 * Sets the nt.
	 * 
	 * @param aufhaengung
	 *            the nt
	 */
	public void setNt(int aufhaengung) {
		if (seil != null)
			nt = aufhaengung * seil.getCount();
	}

	/**
	 * Calculates the rope force. Including correction parameters fs1 ... fs5
	 * 
	 * @param rolle
	 *            the roll
	 * @param j
	 *            the index of the rope
	 * 
	 * @return the force in N
	 */
	private double seilkraft(Rolle rolle, int j) {
		// schlaffseilstoss fs5: hier irrelevant
		double fs5 = 1;
		// rope mass = mass (kg/100m/mm^2) / 100 * length (m) * width (mm) * rope count
		double seilmasse = (bs.get(j)[9] / 100) * schachthoehe * Math.pow(seil.getD(), 2) * seil.getCount();
		long q = 0;
		if (kabine != null && gewicht != null) {
			int kabineMass = kabine.getMass() + (int) (kabine.getZuladung() * kabine.getDurchschnitt());
			int gewichtMass = gewicht.getMass();

			// Add mass of weight at the ground rope, if there is one
			if (unterseil != null) {
				int mass = unterseil.getWeightMass();
				int massK = mass / 2;
				int massG = mass - massK;
				kabineMass += massK;
				gewichtMass += massG;
			}

			if (rolle instanceof Treibscheibe && (rolle.isDoppelteUmschlingung() || rolle.isZweiteUmschlingung())) {
				q = (long) Math.round(seilmasse + ((kabineMass + gewichtMass) / (double) 2));
			} else if (rolle instanceof Treibscheibe) {
				if (kabineMass >= gewichtMass) {
					q = (long) Math.round(seilmasse + kabineMass);
				} else {
					q = (long) Math.round(seilmasse + gewichtMass);
				}
			} else {
				if (rolle.getSeilElement(true) != null && rolle.getSeilElement(true).getPrev().getElement() instanceof Treibscheibe
						&& ((Treibscheibe) rolle.getSeilElement(true).getPrev().getElement()).isDoppelteUmschlingung()
						&& rolle.getSeilElement(true).getPrev().getElement().equals(rolle.getSeilElement(true).getNext().getElement())) {
					q = (long) Math.round(seilmasse + ((kabineMass + gewichtMass) / (double) 2));
				} else if (richtungGewicht(rolle)) {
					q = (long) Math.round(seilmasse + gewichtMass);
				} else {
					q = (long) Math.round(seilmasse + kabineMass);
				}
			}

			Constants.LOGGER.finest("Mass: " + rolle.getName() + rolle.getID() + " - " + q);
		} else {
			Constants.LOGGER.severe("Calculation should not be done!");
		}
		double N = ((q * Constants.G_ERDBESCHL) / nt) * fs1 * rolle.getFs2() * fs3 * fs4 * fs5;
		Constants.LOGGER.finest("Newton: " + N);
		return N;
	}

	private boolean richtungGewicht(Rolle rolle) {
		if ((rolleVorTreibscheibe(rolle) && gewichtVorTreibscheibe()) || (!rolleVorTreibscheibe(rolle) && !gewichtVorTreibscheibe())) {
			return true;
		}
		return false;
	}

	/**
	 * Checks, if the roll is before the treibscheibe in SeilElementVector of rope.
	 * 
	 * @param rolle
	 *            the roll
	 * 
	 * @return true, if roll vor treibscheibe
	 */
	private boolean rolleVorTreibscheibe(Rolle rolle) {
		Vector<SeilElement> seilElementVector = seil.getSeilElementVector();
		for (int i = 0; i < seilElementVector.size(); i++) {
			if (seilElementVector.get(i).getElement() instanceof Treibscheibe) {
				break;
			} else if (seilElementVector.get(i).getElement().equals(rolle)
					|| (seilElementVector.get(i).getElement() instanceof DoppelUmlenkrolle && ((DoppelUmlenkrolle) seilElementVector.get(i).getElement())
							.getRolle1().equals(rolle))
					|| (seilElementVector.get(i).getElement() instanceof DoppelUmlenkrolle && ((DoppelUmlenkrolle) seilElementVector.get(i).getElement())
							.getRolle2().equals(rolle))) {
				Constants.LOGGER.finest("Rolle " + rolle.getName() + rolle.getID() + " before Treibscheibe");
				return true;
			}
		}
		Constants.LOGGER.finest("Rolle " + rolle.getName() + rolle.getID() + " not before Treibscheibe");
		return false;
	}

	/**
	 * Checks, if the counter weight is before the treibscheibe in SeilElementVector of rope
	 * 
	 * @return true, if gewicht vor treibscheibe
	 */
	private boolean gewichtVorTreibscheibe() {
		Vector<SeilElement> seilElementVector = seil.getSeilElementVector();
		for (int i = 0; i < seilElementVector.size(); i++) {
			if (seilElementVector.get(i).getElement() instanceof Treibscheibe) {
				break;
			} else if (seilElementVector.get(i).getElement() instanceof Seilaufhaenger) {
				if (((Seilaufhaenger) seilElementVector.get(i).getElement()).getHangingElement() != null
						&& ((Seilaufhaenger) seilElementVector.get(i).getElement()).getHangingElement().equals(gewicht)) {
					Constants.LOGGER.finest("Gewicht before Treibscheibe");
					return true;
				}
			} else if (seilElementVector.get(i).getElement() instanceof Umlenkrolle) {
				if (((Umlenkrolle) seilElementVector.get(i).getElement()).getHangingElement() != null
						&& ((Umlenkrolle) seilElementVector.get(i).getElement()).getHangingElement().equals(gewicht)) {
					Constants.LOGGER.finest("Gewicht before Treibscheibe");
					return true;
				}
			}
		}
		Constants.LOGGER.finest("Gewicht not before Treibscheibe");
		return false;
	}

	/**
	 * Calculates the number of flexing cycles.
	 * 
	 * @param rolle
	 *            the roll
	 * @param a
	 *            the index of the factor to calculate (0 for 'N A' and 1 for 'N A10')
	 * @param j
	 *            the index of the rope
	 * 
	 * @return the flexing cycles
	 */
	private long biegewechsel(Rolle rolle, int j, int a) {
		double D = rolle.getDurchmesser();
		double d = seil.getD();
		double S = seilkraft(rolle, j);
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
		double logN = b0 + (b1 + b4 * Math.log10(D / d)) * (Math.log10(S / Math.pow(d, 2)) - 0.4 * Math.log10(R0 / 1770)) + b2 * Math.log10(D / d) + b3
				* Math.log10(d) + (1 / (b5 + Math.log10(l / d)));

		long N = Math.round(Math.pow(10, logN));
		Constants.LOGGER.finest("Biegewechel: " + N);
		return N;
	}

	/**
	 * Correct the flexing cycles with the correction factors fn1 ... fn4.
	 * 
	 * @param rolle
	 *            the roll
	 * @param a
	 *            the index of the factor to calculate (0 for 'N A' and 1 for 'N A10')
	 * @param j
	 *            the index of the rope
	 * 
	 * @return the corrected flexing cycles
	 */
	private long korrBiegewechsel(Rolle rolle, int j, int a) {
		long bw = biegewechsel(rolle, j, a);

		double fn2 = bs.get(j)[8];

		return Math.round(bw * fn1 * fn2 * rolle.getFn3() * rolle.getFn4());
	}

	/**
	 * Checks if all factor are given to calculate.
	 * 
	 * @return true, if input is OK
	 */
	public boolean inputOK() {
		if (seil != null && fs1 != 0 && fs3 != 0 && fs4 != 0 && fn1 != 0 && nt != 0 && kabine != null && gewicht != null && !bs.isEmpty()) {
			return true;
		}
		return false;
	}

	/**
	 * Calculates the flexing cycles and gives a solution array back.
	 * 
	 * @return the long[][]
	 */
	public long[][] calculate() {
		double fz = toleranz;
		double fahrten = kabine.getProfil();
		int as = 2;
		long ergArray[][] = new long[as][bs.size()];

		// reset CalcRolls
		calcRolls.clear();

		for (int k = 0; k < as; k++) {
			for (int j = 0; j < bs.size(); j++) {
				double summe = 0;
				for (int i = 0; i < seil.getSeilElementVector().size(); i++) {
					if (seil.getSeilElementVector().get(i).getElement() instanceof Rolle) {
						Rolle rolle = (Rolle) seil.getSeilElementVector().get(i).getElement();
						if (rolle.isActivated()) {
							long korrB = korrBiegewechsel(rolle, j, k);
							long korrBGeg = korrB;
							if (rolle.isGegenbiegung()) {
								korrBGeg = korrGegenbiegung(korrB, rolle, k);
							}
							summe += 1d / korrBGeg;

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
								Constants.LOGGER.fine(rolle.getName() + rolle.getID() + " creates new CalcRolle.");
								newCalcRolle = new CalcRolle(rolle, as, bs.size());
								newCalcRolle.setLebensdauer(korrBGeg, k, j);
								calcRolls.add(newCalcRolle);
							}
						}
					}
					if (seil.getSeilElementVector().get(i).getElement() instanceof DoppelUmlenkrolle) {
						DoppelUmlenkrolle doppelRolle = (DoppelUmlenkrolle) seil.getSeilElementVector().get(i).getElement();
						if (doppelRolle.isActivated()) {
							// 1st roll
							long korrB = korrBiegewechsel(doppelRolle.getRolle1(), j, k);
							long korrBGeg = korrB;
							if (doppelRolle.getRolle1().isGegenbiegung()) {
								korrBGeg = korrGegenbiegung(korrB, doppelRolle.getRolle1(), k);
							}
							summe += 1d / korrBGeg;

							// 2nd roll
							korrB = korrBiegewechsel(doppelRolle.getRolle2(), j, k);
							korrBGeg = korrB;
							if (doppelRolle.getRolle2().isGegenbiegung()) {
								korrBGeg = korrGegenbiegung(korrB, doppelRolle.getRolle2(), k);
							}
							summe += 1d / korrBGeg;

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
								Constants.LOGGER.fine(doppelRolle.getRolle1().getName() + doppelRolle.getRolle1().getID() + " creates new CalcRolle.");
								newCalcRolle = new CalcRolle(doppelRolle.getRolle1(), as, bs.size());
								newCalcRolle.setLebensdauer(korrBGeg, k, j);
								calcRolls.add(newCalcRolle);
							}
							if (!old2) {
								Constants.LOGGER.fine(doppelRolle.getRolle2().getName() + doppelRolle.getRolle2().getID() + " creates new CalcRolle.");
								newCalcRolle = new CalcRolle(doppelRolle.getRolle2(), as, bs.size());
								newCalcRolle.setLebensdauer(korrBGeg, k, j);
								calcRolls.add(newCalcRolle);
							}
						}
					}
				}
				alreadyCalculated.clear();
				long z = Math.round((fz / summe) / fahrten);
				ergArray[k][j] = z;
			}
		}

		calculated = true;

		return ergArray;
	}

	/**
	 * Correct flexing cycles if there is reverse bending.
	 * 
	 * @param rolle
	 *            the roll
	 * @param indexA
	 *            the index of the factor to calculate (0 for 'N A' and 1 for 'N A10')
	 * @param korrB
	 *            the flexing cycles to correct
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
	 * Reset bsfn2fs5massfestigkeit-vector. Just clear()
	 */
	public void resetBsFn2MassFestigkeit() {
		bs.clear();
	}

	/**
	 * Adds the bs, fn2, fs5, mass and festigkeit to the vector.
	 * 
	 * @param nb
	 *            the nb
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
}
