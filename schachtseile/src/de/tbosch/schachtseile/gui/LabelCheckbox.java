/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.tbosch.schachtseile.gui;

import de.tbosch.commons.gui.AbstractLabelUndCheckbox;

/**
 *
 * @author Thomas Bosch (tbosch@gmx.de)
 */
public class LabelCheckbox extends AbstractLabelUndCheckbox {

	public static final int LABEL_CHECKBOX_G1 = 1;
	public static final int LABEL_CHECKBOX_G2_1 = 2;
	public static final int LABEL_CHECKBOX_G2_2 = 3;
	
	private static final String LABEL_CHECKBOX_G1_LABEL = "Gegenbiegung ";
	private static final String LABEL_CHECKBOX_G2_1_LABEL = "Gegenbiegung ";
	private static final String LABEL_CHECKBOX_G2_2_LABEL = "Gegenbiegung ";
	
	public LabelCheckbox(int id) {
		super(id);
	}
	
	@Override
	public void setzeLabelLautId(int id) {
		switch (id) {
			case LABEL_CHECKBOX_G1:
				label = LABEL_CHECKBOX_G1_LABEL;
				break;
			case LABEL_CHECKBOX_G2_1:
				label = LABEL_CHECKBOX_G2_1_LABEL;
				break;
			case LABEL_CHECKBOX_G2_2:
				label = LABEL_CHECKBOX_G2_2_LABEL;
				break;
		}
	}

}
