package de.tbosch.schachtseile.gui;

import de.tbosch.commons.gui.AbstractEingabeLabelUndText;

/**
 *
 * @author Thomas Bosch (tbosch@gmx.de)
 */
public class EingabeFormattedTextField extends AbstractEingabeLabelUndText {
	
	public static final int EINGABE_PARAMETER_T_D = 1;
	public static final int EINGABE_PARAMETER_T_GD2 = 2;
	public static final int EINGABE_PARAMETER_R1_D = 3;
	public static final int EINGABE_PARAMETER_R1_GD2 = 4;
	public static final int EINGABE_PARAMETER_R2_D = 5;
	public static final int EINGABE_PARAMETER_LM = 6;
	public static final int EINGABE_PARAMETER_K1_MC = 7;
	public static final int EINGABE_PARAMETER_K1_MP = 8;
	public static final int EINGABE_PARAMETER_K2_MC = 9;
	public static final int EINGABE_PARAMETER_K2_MP = 10;
	public static final int EINGABE_PARAMETER_S_M = 11;
	public static final int EINGABE_PARAMETER_S_DIM = 12;
	public static final int EINGABE_PARAMETER_S_N = 13;
	public static final int EINGABE_PARAMETER_U_M = 14;
	public static final int EINGABE_PARAMETER_U_DIM = 15;
	public static final int EINGABE_PARAMETER_U_N = 16;
	public static final int EINGABE_PARAMETER_H1 = 17;
	public static final int EINGABE_PARAMETER_H2 = 18;
	public static final int EINGABE_PARAMETER_H3 = 19;
	public static final int EINGABE_PARAMETER_O = 20;
	public static final int EINGABE_PARAMETER_A = 21;
	public static final int EINGABE_PARAMETER_A2 = 22;
	public static final int EINGABE_PARAMETER_A3 = 23;
	public static final int EINGABE_PARAMETER_V = 24;
	public static final int EINGABE_PARAMETER_AP = 25;
	public static final int EINGABE_PARAMETER_AM = 26;
	
	private static final String EINGABE_PARAMETER_T_D_LABEL = "D";
	private static final String EINGABE_PARAMETER_T_D_EINHEIT = "mm";
	private static final String EINGABE_PARAMETER_T_GD2_LABEL = "GD²";
	private static final String EINGABE_PARAMETER_T_GD2_EINHEIT = "";
	private static final String EINGABE_PARAMETER_R1_D_LABEL = "D";
	private static final String EINGABE_PARAMETER_R1_D_EINHEIT = "mm";
	private static final String EINGABE_PARAMETER_R1_GD2_LABEL = "GD²";
	private static final String EINGABE_PARAMETER_R1_GD2_EINHEIT = "";
	private static final String EINGABE_PARAMETER_R2_D_LABEL = "D";
	private static final String EINGABE_PARAMETER_R2_D_EINHEIT = "mm";
	private static final String EINGABE_PARAMETER_L_LABEL = "LM";
	private static final String EINGABE_PARAMETER_L_EINHEIT = "";
	private static final String EINGABE_PARAMETER_K1_MC_LABEL = "Mc";
	private static final String EINGABE_PARAMETER_K1_MC_EINHEIT = "kg";
	private static final String EINGABE_PARAMETER_K1_MP_LABEL = "Mp";
	private static final String EINGABE_PARAMETER_K1_MP_EINHEIT = "kg";
	private static final String EINGABE_PARAMETER_K2_MC_LABEL = "Mc";
	private static final String EINGABE_PARAMETER_K2_MC_EINHEIT = "kg";
	private static final String EINGABE_PARAMETER_K2_MP_LABEL = "Mp";
	private static final String EINGABE_PARAMETER_K2_MP_EINHEIT = "kg";
	private static final String EINGABE_PARAMETER_S_M_LABEL = "m";
	private static final String EINGABE_PARAMETER_S_M_EINHEIT = "kg/m";
	private static final String EINGABE_PARAMETER_S_DIM_LABEL = "dim";
	private static final String EINGABE_PARAMETER_S_DIM_EINHEIT = "mm";
	private static final String EINGABE_PARAMETER_S_N_LABEL = "n";
	private static final String EINGABE_PARAMETER_S_N_EINHEIT = "";
	private static final String EINGABE_PARAMETER_U_M_LABEL = "m";
	private static final String EINGABE_PARAMETER_U_M_EINHEIT = "kg/m";
	private static final String EINGABE_PARAMETER_U_DIM_LABEL = "dim";
	private static final String EINGABE_PARAMETER_U_DIM_EINHEIT = "mm";
	private static final String EINGABE_PARAMETER_U_N_LABEL = "n";
	private static final String EINGABE_PARAMETER_U_N_EINHEIT = "";
	private static final String EINGABE_PARAMETER_H1_LABEL = "Höhe";
	private static final String EINGABE_PARAMETER_H1_EINHEIT = "m";
	private static final String EINGABE_PARAMETER_H2_LABEL = "Höhe";
	private static final String EINGABE_PARAMETER_H2_EINHEIT = "m";
	private static final String EINGABE_PARAMETER_H3_LABEL = "Höhe";
	private static final String EINGABE_PARAMETER_H3_EINHEIT = "m";
	private static final String EINGABE_PARAMETER_O_LABEL = "";
	private static final String EINGABE_PARAMETER_O_EINHEIT = "";
	private static final String EINGABE_PARAMETER_A_LABEL = "Höhe";
	private static final String EINGABE_PARAMETER_A_EINHEIT = "m";
	private static final String EINGABE_PARAMETER_A2_LABEL = "";
	private static final String EINGABE_PARAMETER_A2_EINHEIT = "";
	private static final String EINGABE_PARAMETER_A3_LABEL = "";
	private static final String EINGABE_PARAMETER_A3_EINHEIT = "";
	private static final String EINGABE_PARAMETER_V_LABEL = "V";
	private static final String EINGABE_PARAMETER_V_EINHEIT = "m/s";
	private static final String EINGABE_PARAMETER_AP_LABEL = "a+";
	private static final String EINGABE_PARAMETER_AP_EINHEIT = "m/s²";
	private static final String EINGABE_PARAMETER_AM_LABEL = "a-";
	private static final String EINGABE_PARAMETER_AM_EINHEIT = "m/s²";
	
	public EingabeFormattedTextField(int id, int formatId) {
		super(id, formatId);
	}
	
	public void setzeLabelUndEinheitLautId(int id) {
		switch(id) {
			case EINGABE_PARAMETER_T_D:
				label = EINGABE_PARAMETER_T_D_LABEL;
				einheit = EINGABE_PARAMETER_T_D_EINHEIT;
				break;
			case EINGABE_PARAMETER_T_GD2:
				label = EINGABE_PARAMETER_T_GD2_LABEL;
				einheit = EINGABE_PARAMETER_T_GD2_EINHEIT;
				break;
			case EINGABE_PARAMETER_R1_D:
				label = EINGABE_PARAMETER_R1_D_LABEL;
				einheit = EINGABE_PARAMETER_R1_D_EINHEIT;
				break;
			case EINGABE_PARAMETER_R1_GD2:
				label = EINGABE_PARAMETER_R1_GD2_LABEL;
				einheit = EINGABE_PARAMETER_R1_GD2_EINHEIT;
				break;
			case EINGABE_PARAMETER_R2_D:
				label = EINGABE_PARAMETER_R2_D_LABEL;
				einheit = EINGABE_PARAMETER_R2_D_EINHEIT;
				break;
			case EINGABE_PARAMETER_LM:
				label = EINGABE_PARAMETER_L_LABEL;
				einheit = EINGABE_PARAMETER_L_EINHEIT;
				break;
			case EINGABE_PARAMETER_K1_MC:
				label = EINGABE_PARAMETER_K1_MC_LABEL;
				einheit = EINGABE_PARAMETER_K1_MC_EINHEIT;
				break;
			case EINGABE_PARAMETER_K1_MP:
				label = EINGABE_PARAMETER_K1_MP_LABEL;
				einheit = EINGABE_PARAMETER_K1_MP_EINHEIT;
				break;
			case EINGABE_PARAMETER_K2_MC:
				label = EINGABE_PARAMETER_K2_MC_LABEL;
				einheit = EINGABE_PARAMETER_K2_MC_EINHEIT;
				break;
			case EINGABE_PARAMETER_K2_MP:
				label = EINGABE_PARAMETER_K2_MP_LABEL;
				einheit = EINGABE_PARAMETER_K2_MP_EINHEIT;
				break;
			case EINGABE_PARAMETER_S_M:
				label = EINGABE_PARAMETER_S_M_LABEL;
				einheit = EINGABE_PARAMETER_S_M_EINHEIT;
				break;
			case EINGABE_PARAMETER_S_DIM:
				label = EINGABE_PARAMETER_S_DIM_LABEL;
				einheit = EINGABE_PARAMETER_S_DIM_EINHEIT;
				break;
			case EINGABE_PARAMETER_S_N:
				label = EINGABE_PARAMETER_S_N_LABEL;
				einheit = EINGABE_PARAMETER_S_N_EINHEIT;
				break;
			case EINGABE_PARAMETER_U_M:
				label = EINGABE_PARAMETER_U_M_LABEL;
				einheit = EINGABE_PARAMETER_U_M_EINHEIT;
				break;
			case EINGABE_PARAMETER_U_DIM:
				label = EINGABE_PARAMETER_U_DIM_LABEL;
				einheit = EINGABE_PARAMETER_U_DIM_EINHEIT;
				break;
			case EINGABE_PARAMETER_U_N:
				label = EINGABE_PARAMETER_U_N_LABEL;
				einheit = EINGABE_PARAMETER_U_N_EINHEIT;
				break;
			case EINGABE_PARAMETER_H1:
				label = EINGABE_PARAMETER_H1_LABEL;
				einheit = EINGABE_PARAMETER_H1_EINHEIT;
				break;
			case EINGABE_PARAMETER_H2:
				label = EINGABE_PARAMETER_H2_LABEL;
				einheit = EINGABE_PARAMETER_H2_EINHEIT;
				break;
			case EINGABE_PARAMETER_H3:
				label = EINGABE_PARAMETER_H3_LABEL;
				einheit = EINGABE_PARAMETER_H3_EINHEIT;
				break;
			case EINGABE_PARAMETER_O:
				label = EINGABE_PARAMETER_O_LABEL;
				einheit = EINGABE_PARAMETER_O_EINHEIT;
				break;
			case EINGABE_PARAMETER_A:
				label = EINGABE_PARAMETER_A_LABEL;
				einheit = EINGABE_PARAMETER_A_EINHEIT;
				break;
			case EINGABE_PARAMETER_A2:
				label = EINGABE_PARAMETER_A2_LABEL;
				einheit = EINGABE_PARAMETER_A2_EINHEIT;
				break;
			case EINGABE_PARAMETER_A3:
				label = EINGABE_PARAMETER_A3_LABEL;
				einheit = EINGABE_PARAMETER_A3_EINHEIT;
				break;
			case EINGABE_PARAMETER_V:
				label = EINGABE_PARAMETER_V_LABEL;
				einheit = EINGABE_PARAMETER_V_EINHEIT;
				break;
			case EINGABE_PARAMETER_AP:
				label = EINGABE_PARAMETER_AP_LABEL;
				einheit = EINGABE_PARAMETER_AP_EINHEIT;
				break;
			case EINGABE_PARAMETER_AM:
				label = EINGABE_PARAMETER_AM_LABEL;
				einheit = EINGABE_PARAMETER_AM_EINHEIT;
				break;
		}
	}
	
}
