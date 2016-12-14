package de.tbosch.seile.berechnung.treibfaehigkeit;

import de.tbosch.seile.berechnung.lebensdauer.Berechnung;
import de.tbosch.seile.commons.CommonConstants;
import de.tbosch.seile.commons.elemente.Element;
import de.tbosch.seile.commons.elemente.Kapsel;
import de.tbosch.seile.commons.elemente.Rolle;
import de.tbosch.seile.commons.elemente.SeilElement;
import de.tbosch.seile.commons.elemente.Treibscheibe;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.logging.Logger;
/**
 * The Class Treibfaehigkeit.
 * 
 * @author Thomas Bosch (tbosch@gmx.de)
 */
public class Treibfaehigkeit implements Serializable {
	
	/** Logger */
	private static final Logger logger = Logger.getLogger(Treibfaehigkeit.class.getName());
	
	/** The treibscheibe keil. */
	private boolean treibscheibeKeil = false;
	
	/** The treibscheibe unterschnitt. */
	private boolean treibscheibeSitz = false;
	
	/** The treibscheibe rund. */
	private boolean treibscheibeRund = false;
	
	/** The keil winkel rad. */
	private double keilwinkelRad;
	
	/** The winkel rad. */
	private double unterschnittwinkelRad;
	
	/** The mu. */
	private double mu = 0.09;
	
	/** umschlingungswinkel in radians. */
	private double beta;
	
	/** gewichtskraft der seile in N. */
	private double s;
	
	/** gewichtskraft des fahrkorbes in N. */
	private double F;
	
	/** gewichtskraft des haengekabels in N. */
	private double Hk;
	
	/** tragkraft in N. */
	private double Q;
	
	/** gegengewichtskaft in N. */
	private double G;
	
	/** wirkungsfaktor der beschleunigung bzw. bremsung */
	private double phi;
	
	/** The mass of the rope in kg/100m. */
	private double seilmasse;
	
	/** The count of parallel ropes. */
	private int seilcount;
	
	/** The seildurchmesser in mm. */
	private double seildurchmesser;
	
	/** The treibscheibe durchmesser in mm. */
	private int treibscheibeDurchmesser;
	
	/** The hubhoehe. */
	private double hubhoehe;
	
	/** The main frame. */
	private double mainFrameUmschlWinkel;
	
	/** The is B correct. 0 = false, 1 = true */
	private int isBCorrect = 1;
	
	/** The is gamma correct. 0 = false, 1 = true */
	private int isGammaCorrect = 1;
	
	/** The typ: fassade, kleingueter, sonstige. */
	private String typ = CommonConstants.TYP_SONSTIGER;
	
	/** The fassade correct. */
	private int fassadeCorrect = 1;
	
	/** The fass fak. */
	private double fassFak = 1.0;
	
	/** The geschw. */
	private double geschw;
	
	/** The treibscheibe. */
	private Treibscheibe treibscheibe;
    
    private int aufhaengung;
    private int ohneWalzCount;
	 
	/**
	 * The Constructor.
	 * 
	 * @param mainFrame the main frame
	 */
	public Treibfaehigkeit(double mainFrameUmschlWinkel, double seilmasse, int aufhaengung, int ohneWalzCount) {
		this.seilmasse = seilmasse;
        this.mainFrameUmschlWinkel = mainFrameUmschlWinkel;
        this.aufhaengung = aufhaengung;
        this.ohneWalzCount = ohneWalzCount;
		
		// default: DRAKO 250 T 8 mm durchm.
		if (seilmasse == 0) seilmasse = 27.1;
		
		// default: from beschl < 0,3 as default
		phi = 1.1;
	}
	
	/**
	 * Treibfaehigkeit treibscheibe.
	 * 
	 * @return the double[]
	 */
	public double[] treibfaehigkeitTreibscheibe() {
		double t2t1 = T2durchT1();
		double s2s1 = S2durchS1();

		double erg[] = new double[6];
		erg[0] = t2t1;
		erg[1] = s2s1;
		erg[2] = phi;
		erg[3] = isBCorrect;
		erg[4] = isGammaCorrect;
		erg[5] = fassadeCorrect;

		return erg;
	}
	
	/**
	 * Pressure (Spezifische Pressung).
	 * 
	 * @param unterschnittenkeilpressung the unterschnittenkeilpressung
	 * 
	 * @return the pressure in N/qcm
	 */
	public double pressure(boolean unterschnittenkeilpressung) {
		double k = 0;
		double z = seilcount;
		double d = ((double)seildurchmesser / 10);
		double D = ((double)treibscheibeDurchmesser / 10);
		double locQ = this.Q;
		double locF = this.F;
		double locs = this.s;
		
		// 2:1 fuehrt zu kleineren Kraeften
		if (aufhaengung == 2) {
			locQ = locQ/2;
			locF = locF/2;
			locs = locs/2;
		}
		
		if (unterschnittenkeilpressung) {
			double alpha = unterschnittwinkelRad;
			k = ((locF + locQ + locs) / (z * d * D)) * ((8 * Math.cos(alpha/2)) / (Math.PI - alpha - Math.sin(alpha)));
		}
		else { 
			if (treibscheibeSitz)  {
				double alpha = unterschnittwinkelRad;
				k = ((locF + locQ + locs) / (z * d * D)) * ((8 * Math.cos(alpha/2)) / (Math.PI - alpha - Math.sin(alpha)));
			}
			else if (treibscheibeKeil) {
				double gamma = keilwinkelRad;
				k = ((locF + locQ + locs) / (z * d * D)) * (1 / Math.sin(gamma / 2));
			}
			else if (treibscheibeRund) {
				double alpha = 0;
				k = ((locF + locQ + locs) / (z * d * D)) * ((8 * Math.cos(alpha/2)) / (Math.PI - alpha - Math.sin(alpha)));
			}
			else {
				logger.fine("Treibscheibe failure");
			}
		}
		
		return k;
	}
	
	/**
	 * S2 durch S1.
	 * 
	 * @return the double
	 */
	private double S2durchS1() {
		double s2ds1 = (G + s) / (F + Hk);
		
		return s2ds1;
	}
	
	/**
	 * Calc g.
	 * 
	 * @param locF the locF
	 * @param locQ the locQ
	 * 
	 * @return the double
	 */
	private double calcG(double F, double Q) {
		return F + Q/2;
	}
	
	/**
	 * T2 durch T1.
	 * 
	 * @return the double
	 */
	private double T2durchT1() {
		// Reibungszahl bei Reibwert mu
		double f_mu = reibwert(mu);
		
		double t2dt1 = Math.pow(Math.E, f_mu * beta);
		
		return t2dt1;
	}
	
	/**
	 * Reibwert.
	 * 
	 * @param mu the mu
	 * 
	 * @return the double
	 */
	private double reibwert(double mu) {
		double f = 0;
		if (treibscheibeSitz  
				|| (treibscheibeKeil && (typ.equals(CommonConstants.TYP_KLEINLAST) || geschw > 1.25))) {
			double alpha = unterschnittwinkelRad;
			f = (4 * mu * (1 - Math.sin(alpha/2))) / (Math.PI - alpha - Math.sin(alpha));
			checkB(alpha);
		}
		else if (treibscheibeKeil) {
			// TODO: gehaertete Rille?! TRA nur gehaertet
			double gamma = keilwinkelRad;
			double alpha = unterschnittwinkelRad;
			f = mu / Math.sin(gamma/2);
			checkGamma(gamma);
			checkB(alpha);
		}
		else if (treibscheibeRund) {
			if (Math.toDegrees(keilwinkelRad) == 45) {
				// oeffnungswinkel gamma von 45 grad 
				f = 0.09 * 1.21;
			}
			else {
				logger.severe("Angle not 45 degrees");
			}
		}
		else {
			logger.severe("Treibscheibe not set! ???");
		}
		return f;
	}
	
	/**
	 * Check Unterschnittbreite B.
	 * TRA: 2.3.2 (1)
	 * 
	 * @param alpha the alpha
	 */
	private void checkB(double alpha) {
		double B = seildurchmesser * Math.sin(alpha/2);
		if (seildurchmesser >= 8) {
			if (B > 0.8 * seildurchmesser) {
				isBCorrect = 0;
			}
			else {
				isBCorrect = 1;
			}
		}
		else {
			if (B > 0.75 * seildurchmesser) {
				isBCorrect = 0;
			}
			else {
				isBCorrect = 1;
			}
		}
	}
	
	/**
	 * Check Keilwinkel gamma.
	 * TRA: 2.3.2 (2)
	 * 
	 * @param gamma the gamma
	 */
	private void checkGamma(double gamma) {
		if (typ.equals(CommonConstants.TYP_SONSTIGER)) {
			if (Math.toDegrees(gamma) >= 35) {
				isGammaCorrect = 1;
			}
			else {
				isGammaCorrect = 0;
			}
		}
		else if (typ.equals(CommonConstants.TYP_FASSADE) || typ.equals(CommonConstants.TYP_KLEINLAST)) {
			if (Math.toDegrees(gamma) >= 30) {
				isGammaCorrect = 1;
			}
			else {
				isGammaCorrect = 0;
			}
		}
		else {
			logger.severe("Wrong elevator type!");
		}
		
	}
	
	/**
	 * Refresh typ.
	 * 
	 * @param typ the typ
	 */
	public void refreshTyp(String typ) {
		this.typ = typ;
		if (typ.equals(CommonConstants.TYP_FASSADE)) {
			fassFak = 1.5;
		}
		else {
			fassFak = 1;
		}
		G = calcG(F, Q * fassFak);
		setPhi(geschw);
	}
	
//	private double T1EN81() {
//		double summe = ((P + locQ + Mcrcar + Mtrav) * (gn +- a)) / r;
//		summe += (Mcomp / (2 * r)) * gn;
//		summe += Msrcar * (gn +- r * a);
//		if (fahrkorbOben) {
//			summe +-= ((2 * mptd) / r) * a;
//		}
//		if (ablenkrolleAufGKSeite) {
//			summe +-= mdp * r * a;
//		}
//		if (einscherungGr1) {
//			double summeAllR = 0;
//			for (int i = 1; i <=  r - 1; i++) {
//				summeAllR += mpcar * ipcar * a;
//			}
//			summe +-= Msrcar * a * ((r * r - 2 * r) / 2) +- summeAllR;
//		}
//		summe +-= frcar / r;
//		
//		return summe; 
//	}
//	
//	private double T2EN81() {
//		double summe = (Mcwt * (gn + a)) / r;
//		summe += (Mcomp / (2 * r)) * gn;
//		summe += Msrcwt * (gn +- r * a);
//		summe += (Mcrcwt / r) * (gn +- a);
//		if (gewichtOben) {
//			summe +-= ((2 * mptd) / r) * a;
//		}
//		if (ablenkrolleAufGKSeite) {
//			summe +-= mdp * r * a;
//		}
//		if (einscherungGr1) {
//			double summeAllR = 0;
//			for (int i = 1; i <=  r - 1; i++) {
//				summeAllR += mpcwt * ipcwt * a;
//			}
//			summe +-= Msrcwt * a * ((r * r - 2 * r) / 2) +- summeAllR;
//		}
//		summe +-= frcwt / r;
//			
//		return summe;
//	}
	
	/**
 * Sets the treibscheibe parameters.
 * 
 * @param treibscheibe the treibscheibe
 * 
 * @return true, if set treibscheibe was successfull
 */
	public boolean setTreibscheibe(Treibscheibe treibscheibe) {
		this.treibscheibe = treibscheibe;
		String form = treibscheibe.getForm();
		treibscheibeDurchmesser = treibscheibe.getDurchmesser();
		this.keilwinkelRad = Math.toRadians(treibscheibe.getKeilwinkel());
		this.unterschnittwinkelRad = Math.toRadians(treibscheibe.getUnterschnittwinkel());
		
		SeilElement se = ((Element)treibscheibe).getSeilElement(true);
		if (se == null) {
			logger.fine("No rope!");
			return false;
		}
		DecimalFormat df_point = new DecimalFormat();
		df_point.setMaximumFractionDigits(2);
		df_point.setMinimumFractionDigits(0);
		df_point.setGroupingUsed(false);
		
        double betaDeg = mainFrameUmschlWinkel;
		this.beta = Math.toRadians(betaDeg);
		
		if (form.equals(Rolle.FORM_KEIL)) {
			treibscheibeKeil = true;
			treibscheibeSitz = false;
			treibscheibeRund = false;
		}
		else if (form.equals(Rolle.FORM_SITZ)) {
			treibscheibeKeil = false;
			treibscheibeSitz = true;
			treibscheibeRund = false;
		}
		else if (form.equals(Rolle.FORM_RUND)) {
			treibscheibeKeil = false;
			treibscheibeSitz = false;
			treibscheibeRund = true;
		}
		else {
			logger.severe("Wrong angle!");
			return false;
		}
		
		return true;
	}
	
	/**
	 * Sets the gewichte.
	 * 
	 * @param hoehe the hoehe
	 * @param seilcount the seilcount
	 * @param kabine the kabine
	 * @param seildurchmesser the seildurchmesser
	 */
	public void setGewichte(Kapsel kabine, double hoehe, int seilcount, double seildurchmesser) {
		F = kabine.getMass() * Berechnung.G_ERDBESCHL;
		Q = kabine.getZuladung() * Berechnung.G_ERDBESCHL;
		G = calcG(F, Q);
		hubhoehe = hoehe;
		this.seilcount = seilcount;
		this.seildurchmesser = seildurchmesser;
		refreshSeilmasse();
	}
	
	/**
	 * Refresh seilmasse.
	 */
	public void refreshSeilmasse() {
		s = (hubhoehe / 100) * seilmasse * seilcount * Berechnung.G_ERDBESCHL;
		Hk = s / 2;
	}

	/**
	 * Gets the hubhoehe.
	 * 
	 * @return the hubhoehe
	 */
	public double getHubhoehe() {
		return hubhoehe;
	}

	/**
	 * Gets the phi.
	 * 
	 * @return the phi
	 */
	public double getPhi() {
		return phi;
	}

	/**
	 * Sets the phi.
	 * 
	 * @param geschw the Geschwindigkeit
	 */
	public void setPhi(double geschw) {
		this.geschw = geschw;
		
		if (treibscheibe.getForm().equals(Rolle.FORM_SITZ)
				|| treibscheibe.getForm().equals(Rolle.FORM_RUND)) {
			// 2.3.1.1
			if (typ.equals(CommonConstants.TYP_KLEINLAST)) {
				this.phi = 1.2;
			}
			else {
				if (geschw <= 0.5) {
					this.phi = 1.1;
				}
				else if (geschw <= 1.5) {
					this.phi = 1.15;
				}
				else {
					this.phi = 1.2;
				}
			}
		}
		else if (treibscheibe.getForm().equals(Rolle.FORM_KEIL)) {
			// TODO: 2.3.3.2 ???
			this.phi = 1.05;
			
			// 2.3.1.2/3
			if (ohneWalzCount == 0) {
				this.phi = 1.33;
			}
			else if (ohneWalzCount == 1) {
				this.phi = 1.23;
			}
			else if (ohneWalzCount > 1) {
				this.phi = 1.15;
			}
		}
		else {
			logger.severe("PHI could not be set!");
		}
	}

	/**
	 * Gets the q.
	 * 
	 * @return the locQ
	 */
	public double getQ() {
		return Q;
	}

	/**
	 * Gets the f.
	 * 
	 * @return the locF
	 */
	public double getF() {
		return F;
	}

	/**
	 * Gets the hk.
	 * 
	 * @return the hk
	 */
	public double getHk() {
		return Hk;
	}

	/**
	 * Gets the locs.
	 * 
	 * @return the S
	 */
	public double getS() {
		return s;
	}

	/**
	 * Gets the g.
	 * 
	 * @return the G
	 */
	public double getG() {
		return G;
	}

	/**
	 * Gets the rope weight.
	 * 
	 * @return the rope weight.
	 */
	public double getSeilmasse() {
		return seilmasse;
	}

	/**
	 * Sets the rope weight.
	 * 
	 * @param seilmasse the rope weight
	 */
	public void setSeilmasse(double seilmasse) {
		this.seilmasse = seilmasse;
	}

	/**
	 * Checks if is treibscheibe keil.
	 * 
	 * @return true, if is treibscheibe keil
	 */
	public boolean isTreibscheibeKeil() {
		return treibscheibeKeil;
	}

	/**
	 * Checks if is treibscheibe rund.
	 * 
	 * @return true, if is treibscheibe rund
	 */
	public boolean isTreibscheibeRund() {
		return treibscheibeRund;
	}

	/**
	 * Checks if is treibscheibe sitz.
	 * 
	 * @return true, if is treibscheibe sitz
	 */
	public boolean isTreibscheibeSitz() {
		return treibscheibeSitz;
	}

	/**
	 * Gets the enlancement angle beta.
	 * 
	 * @return the beta
	 */
	public double getBeta() {
		return beta;
	}

	/**
	 * Sets the enlancement angle beta.
	 * 
	 * @param beta the beta
	 */
	public void setBeta(double beta) {
		this.beta = beta;
	}

	/**
	 * Gets the rope count.
	 * 
	 * @return the count
	 */
	public int getSeilcount() {
		return seilcount;
	}
}
