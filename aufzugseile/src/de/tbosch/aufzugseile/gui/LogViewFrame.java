package de.tbosch.aufzugseile.gui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.ImageIcon;

import de.tbosch.aufzugseile.utils.Constants;
import de.tbosch.aufzugseile.utils.Helper;

/**
 * 
 * @author Thomas Bosch (tbosch@gmx.de)
 */
public class LogViewFrame extends javax.swing.JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -533808265248238556L;

	private MainFrame mainFrame;

	/** Creates new form LogViewFrame */
	public LogViewFrame(MainFrame mainFrame, String logfilepath) {
		initComponents();
		this.mainFrame = mainFrame;

		readLogFile(logfilepath);

		ImageIcon imageIcon = new ImageIcon(Helper.getFileURL(Constants.MENU_LOGVIEW_ICON));
		setIconImage(imageIcon.getImage());
	}

	private void readLogFile(String logfilepath) {
		File logFile = new File(logfilepath);
		String log = "";

		try {
			FileReader fr = new FileReader(logFile);
			int r;
			try {
				while ((r = fr.read()) != -1) {
					char c = (char) r;
					log += c;
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					fr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		logTextArea.setText(log);
	}

	/**
	 * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
	private void initComponents() {
		okButton = new javax.swing.JButton();
		jScrollPane1 = new javax.swing.JScrollPane();
		logTextArea = new javax.swing.JTextArea();

		setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
		setTitle("LogViewer");
		addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent evt) {
				formWindowClosing(evt);
			}
		});

		okButton.setText("OK");
		okButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				okButtonActionPerformed(evt);
			}
		});

		logTextArea.setColumns(20);
		logTextArea.setRows(5);
		jScrollPane1.setViewportView(logTextArea);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				layout.createSequentialGroup().addContainerGap().addGroup(
						layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(okButton, javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jScrollPane1,
										javax.swing.GroupLayout.DEFAULT_SIZE, 606, Short.MAX_VALUE)).addContainerGap()));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				javax.swing.GroupLayout.Alignment.TRAILING,
				layout.createSequentialGroup().addContainerGap().addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 301, Short.MAX_VALUE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(okButton).addContainerGap()));
		pack();
	}// </editor-fold>//GEN-END:initComponents

	private void formWindowClosing(java.awt.event.WindowEvent evt) {// GEN-FIRST:event_formWindowClosing
		okButtonActionPerformed(null);
	}// GEN-LAST:event_formWindowClosing

	private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_okButtonActionPerformed
		mainFrame.setEnabled(true);
		dispose();
	}// GEN-LAST:event_okButtonActionPerformed

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JTextArea logTextArea;
	private javax.swing.JButton okButton;
	// End of variables declaration//GEN-END:variables

}