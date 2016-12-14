/*
 * TreibscheibeFrame.java
 *
 * Created on 12. April 2007, 11:28
 */

package de.tbosch.aufzugseile.gui;

import java.text.DecimalFormat;
import java.text.ParseException;

import javax.swing.ImageIcon;

import de.tbosch.aufzugseile.gui.aufzug.Gewicht;
import de.tbosch.aufzugseile.utils.Constants;
import de.tbosch.aufzugseile.utils.Helper;

/**
 * The options frame for the gewicht.
 * 
 * @author Thomas Bosch (tbosch@gmx.de)
 */
public class GewichtFrame extends javax.swing.JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6923655998630369091L;

	/** The gewicht itself. */
	private Gewicht gewicht;
	
	/** The main frame. */
	private MainFrame mainFrame;
	
	/** The decimal formats without a point (int). */
	private DecimalFormat df_nopoint;

	/**
	 * Creates new form TreibscheibeFrame.
	 * 
	 * @param gewicht
	 *            The gewicht itself
	 */
	public GewichtFrame(Gewicht gewicht, MainFrame mainFrame) {
		df_nopoint = new DecimalFormat();
		df_nopoint.setMaximumFractionDigits(0);
		df_nopoint.setGroupingUsed(false);
		initComponents();
		this.gewicht = gewicht;
		this.mainFrame = mainFrame;
		if (mainFrame.getAufzugschacht().getKabine() != null) {
			masseFormattedTextField.setText(df_nopoint.format(gewicht.getMass()));
    	}
		anteilFormattedTextField.setText(df_nopoint.format((int)(gewicht.getPart()*100)));
    	
		ImageIcon imageIcon = new ImageIcon(Helper.getFileURL(Constants.PROGRAM_ICON));
		setIconImage(imageIcon.getImage());
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed" desc=" Generated Code
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        cancelButton = new javax.swing.JButton();
        okButton = new javax.swing.JButton();
        titleLabel = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        errorLabel = new javax.swing.JLabel();
        masseFormattedTextField = new javax.swing.JFormattedTextField(df_nopoint);
        jLabel3 = new javax.swing.JLabel();
        anteilFormattedTextField = new javax.swing.JFormattedTextField(df_nopoint);
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Gewichtoptionen");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        cancelButton.setText("Cancel");
        cancelButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cancelButtonMouseClicked(evt);
            }
        });

        okButton.setText("OK");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        titleLabel.setText("Gewicht: Optionen");

        jLabel1.setText("Masse:");

        jLabel2.setText("kg");

        errorLabel.setForeground(new java.awt.Color(255, 0, 0));

        masseFormattedTextField.setEditable(false);

        jLabel3.setText("Zuladungsanteil:");

        anteilFormattedTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                anteilFormattedTextFieldKeyReleased(evt);
            }
        });

        jLabel4.setText("%");

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(errorLabel)
                            .add(layout.createSequentialGroup()
                                .add(10, 10, 10)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                                    .add(layout.createSequentialGroup()
                                        .add(jLabel3)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(anteilFormattedTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 61, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                    .add(layout.createSequentialGroup()
                                        .add(jLabel1)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(masseFormattedTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 61, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))))
                        .add(7, 7, 7)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel2)
                            .add(jLabel4)))
                    .add(titleLabel))
                .add(177, 177, 177))
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap(253, Short.MAX_VALUE)
                .add(okButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(cancelButton)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(titleLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel3)
                    .add(anteilFormattedTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel4))
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, errorLabel)
                    .add(layout.createSequentialGroup()
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(masseFormattedTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jLabel1)
                            .add(jLabel2))))
                .add(27, 27, 27)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(cancelButton)
                    .add(okButton))
                .addContainerGap())
        );
        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void anteilFormattedTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_anteilFormattedTextFieldKeyReleased
    	if (mainFrame.getAufzugschacht().getKabine() != null) {
    		double anteil = 0;
			try {
				anteil = (double)df_nopoint.parse(anteilFormattedTextField.getText()).intValue() / 100;
			}
			catch (ParseException e) {
				// auto-corrected by FormattedTextField
			}
    		int mass = mainFrame.getAufzugschacht().getKabine().getMass() + (int)(anteil * mainFrame.getAufzugschacht().getKabine().getZuladung());
			masseFormattedTextField.setText(df_nopoint.format(mass));
    	}
    }//GEN-LAST:event_anteilFormattedTextFieldKeyReleased

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
		mainFrame.setEnabled(true);
		dispose();
    }//GEN-LAST:event_formWindowClosing

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
    	int mass = 0;
    	int part = 0;
		try {
			mass = df_nopoint.parse(masseFormattedTextField.getText()).intValue();
			part = df_nopoint.parse(anteilFormattedTextField.getText()).intValue();
		}
		catch (ParseException e) {
			// auto-corrected by FormattedTextField
		}
    	if (mass > 0) {
    		gewicht.setMass(mass);
    		gewicht.setPart((double)part / 100);
    		mainFrame.setMassGewicht(mass, part);
    		mainFrame.setEnabled(true);
    		mainFrame.parameterChanged();
    		dispose();
    	}
    	else {
    		errorLabel.setText("Keine negative Masse erlaubt!");
    	}
    }//GEN-LAST:event_okButtonActionPerformed

	/**
	 * Triggered if cancel button is clicked.
	 * 
	 * @param evt
	 *            The MouseEvent
	 */
	private void cancelButtonMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_cancelButtonMouseClicked
		mainFrame.setEnabled(true);
		dispose();
	}// GEN-LAST:event_cancelButtonMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JFormattedTextField anteilFormattedTextField;
    private javax.swing.JButton cancelButton;
    private javax.swing.JLabel errorLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JFormattedTextField masseFormattedTextField;
    private javax.swing.JButton okButton;
    private javax.swing.JLabel titleLabel;
    // End of variables declaration//GEN-END:variables

}
