/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.tbosch.schachtseile.service;

import de.tbosch.commons.gui.ErrorDialog;
import de.tbosch.commons.gui.GenericFileFilter;
import de.tbosch.commons.gui.SmartFileChooser;
import de.tbosch.schachtseile.SchachtseileApp;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;

/**
 *
 * @author bobo
 */
public class DialogService {
	
	private static ResourceMap resourceMap = Application.getInstance(SchachtseileApp.class).getContext().getResourceMap(DialogService.class);
	
	private JFrame parentFrame;
	
	public DialogService(JFrame parentFrame) {
		this.parentFrame = parentFrame;
	}
	
	public void showErrorDialog(String message) {
		ErrorDialog errorDialog = new ErrorDialog(parentFrame, true, message);
		errorDialog.setLocationRelativeTo(parentFrame);
		SchachtseileApp.getApplication().show(errorDialog);
	}
	
	public File showFileChooserOpen() {
		SmartFileChooser fileChooser = new SmartFileChooser();
		fileChooser.setDialogTitle(resourceMap.getString("oeffnenDialog.text"));
		fileChooser.setFileFilter(new GenericFileFilter(resourceMap.getString("oeffnenDialog.filtertext"), resourceMap.getString("oeffnenDialog.filter")));
		int state = fileChooser.showOpenDialog(parentFrame);
		if (state == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			return file;
		} else {
			return null;
		}
	}
	
	public File showFileChooserSave(String selectedFile) {
		SmartFileChooser fileChooser = new SmartFileChooser();
		fileChooser.setDialogTitle(resourceMap.getString("speichernAlsDialog.text"));
		fileChooser.setSelectedFile(new File(selectedFile));
		fileChooser.setFileFilter(new GenericFileFilter(resourceMap.getString("speichernAlsDialog.filtertext"), resourceMap.getString("speichernAlsDialog.filter")));
		int state = fileChooser.showSaveDialog(parentFrame);
		if (state == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			return file;
		} else {
			return null;
		}
	}

}
