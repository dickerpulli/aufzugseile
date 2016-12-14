package de.tbosch.seile.commons.utils;

import de.tbosch.seile.commons.CommonConstants;
import de.tbosch.seile.commons.elemente.Rolle;
import org.apache.log4j.Logger;
/**
 * @author Thomas Bosch (tbosch@gmx.de)
 *
 */
public class Utilities {
	
	/** Logger */
	private static final Logger logger = Logger.getLogger(Utilities.class.getName());

	/**
	 * Calculate the fn3 
	 * With linear ... take a value for the feyrer parameter
	 * 
	 * @param newUnterschnittwinkel
	 * @param newKeilwinkel
	 * @param newForm
	 * @return
	 */
	public static double calcFn3(int newUnterschnittwinkel, int newKeilwinkel, String newForm) {
		double fn3 = 0;
		
		if (newUnterschnittwinkel >= 0 && newUnterschnittwinkel <= 75) {
			fn3 = linearInterpolation(newUnterschnittwinkel, 0, 75, 1, 0.4);
		}
		else if (newUnterschnittwinkel > 75 && newUnterschnittwinkel <= 80) {
			fn3 = linearInterpolation(newUnterschnittwinkel, 75, 80, 0.4, 0.33);	
		}
		else if (newUnterschnittwinkel > 80 && newUnterschnittwinkel <= 85) {
			fn3 = linearInterpolation(newUnterschnittwinkel, 80, 85, 0.33, 0.26);
		}
		else if (newUnterschnittwinkel > 85 && newUnterschnittwinkel <= 90) {
			fn3 = linearInterpolation(newUnterschnittwinkel, 85, 90, 0.26, 0.2);
		}
		else if (newUnterschnittwinkel > 90 && newUnterschnittwinkel <= 95) {
			fn3 = linearInterpolation(newUnterschnittwinkel, 90, 95, 0.2, 0.15);
		}
		else if (newUnterschnittwinkel > 95 && newUnterschnittwinkel <= 100) {
			fn3 = linearInterpolation(newUnterschnittwinkel, 95, 100, 0.15, 0.1);
		}
		else if (newUnterschnittwinkel > 100 && newUnterschnittwinkel <= 105) {
			fn3 = linearInterpolation(newUnterschnittwinkel, 100, 105, 0.1, 0.066);
		}
		else if (newUnterschnittwinkel > 105 && newUnterschnittwinkel <= 360) {
			fn3 = linearInterpolation(newUnterschnittwinkel, 105, 360, 0.066, 0);
		}
		if (newForm.equals(Rolle.FORM_KEIL)) {
			if (newKeilwinkel >= 0 && newKeilwinkel <= 35) {
				fn3 = linearInterpolation(newKeilwinkel, 0, 35, 0, 0.054);
			}
			else if (newKeilwinkel > 35 && newKeilwinkel <= 36) {
				fn3 = linearInterpolation(newKeilwinkel, 35, 36, 0.054, 0.066);
			}
			else if (newKeilwinkel > 36 && newKeilwinkel <= 38) {
				fn3 = linearInterpolation(newKeilwinkel, 36, 38, 0.066, 0.095);
			}
			else if (newKeilwinkel > 38 && newKeilwinkel <= 40) {
				fn3 = linearInterpolation(newKeilwinkel, 38, 40, 0.095, 0.14);
			}
			else if (newKeilwinkel > 40 && newKeilwinkel <= 42) {
				fn3 = linearInterpolation(newKeilwinkel, 40, 42, 0.14, 0.18);
			}
			else if (newKeilwinkel > 42 && newKeilwinkel <= 45) {
				fn3 = linearInterpolation(newKeilwinkel, 42, 45, 0.18, 0.25);
			}
			else if (newKeilwinkel > 45 && newKeilwinkel <= 360) {
				fn3 = linearInterpolation(newKeilwinkel, 45, 360, 0.25, 1);
			}
		}
		
		return fn3;
	}
	
	private static double linearInterpolation(int winkel, double x1, double x2, double y1, double y2) {
		double a = (y2 - y1) / (x2 - x1);
		double b = y1 - (a * x1);
		
		return (a * winkel) + b;
	}
	
}
