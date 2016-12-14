package de.tbosch.aufzugseile.gui;

import de.tbosch.commons.Helper;
import de.tbosch.commons.gui.GUIHelper;
import de.tbosch.aufzugseile.gui.utils.Constants;
import de.tbosch.seile.commons.elemente.Seil;
import java.text.DecimalFormat;
import java.text.ParseException;

import javax.swing.ImageIcon;


/**
 * The options frame for the gewicht.
 * 
 * @author Thomas Bosch (tbosch@gmx.de)
 */
public class SeilFrame extends javax.swing.JFrame {

	/** The gewicht itself. */
	private Seil seil;
	
	/** The main frame. */
	private MainFrame mainFrame;
	
	/** The decimal format with no point (like integer). */
	private DecimalFormat df_nopoint, df_point;

	/**
	 * Creates new form SeilFrame.
	 * 
	 * @param mainFrame the main frame
	 * @param seil the seil
	 */
	public SeilFrame(Seil seil, MainFrame mainFrame) {
		df_nopoint = new DecimalFormat();
		df_nopoint.setMaximumFractionDigits(0);
		df_nopoint.setGroupingUsed(false);
		df_point = new DecimalFormat();
		df_point.setMaximumFractionDigits(2);
		df_point.setMinimumFractionDigits(0);
		df_point.setGroupingUsed(false);
		initComponents();
		this.seil = seil;
		this.mainFrame = mainFrame;
		
		// set existing values
		countTextField.setText(Integer.toString(seil.getCount()));
		seilbiegelaengeFormattedTextField.setText(df_nopoint.format(seil.getL()));
		seilnenndurchmesserFormattedTextField.setText(df_point.format(seil.getD()));
		
		mainFrame.setEnabled(false);
    	
		ImageIcon imageIcon = new ImageIcon(Helper.getFileURL(Constants.PROGRAM_ICON));
		setIconImage(imageIcon.getImage());
	}

	
	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        cancelButton = new javax.swing.JButton();
        okButton = new javax.swing.JButton();
        titleLabel = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        countTextField = new javax.swing.JTextField();
        errorLabel = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        seilnenndurchmesserFormattedTextField = new javax.swing.JFormattedTextField(df_point);
        jLabel3 = new javax.swing.JLabel();
        seilbiegelaengeFormattedTextField = new javax.swing.JFormattedTextField(df_nopoint);
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Seiloptionen");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        cancelButton.setText("Abbrechen");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        okButton.setText("OK");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        titleLabel.setText("Seil: Optionen");

        jLabel1.setText("Anzahl der Seile:");

        errorLabel.setForeground(new java.awt.Color(255, 0, 0));

        jLabel2.setText("Seilnenndurchmesser:");

        jLabel3.setText("Seilbiegel\u00e4nge:");

        jLabel5.setText("mm");

        jLabel6.setText("mm");

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(layout.createSequentialGroup()
                                .add(12, 12, 12)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                                    .add(jLabel1)
                                    .add(jLabel2)
                                    .add(jLabel3))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                                    .add(seilbiegelaengeFormattedTextField)
                                    .add(org.jdesktop.layout.GroupLayout.LEADING, countTextField)
                                    .add(org.jdesktop.layout.GroupLayout.LEADING, seilnenndurchmesserFormattedTextField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 56, Short.MAX_VALUE))
                                .add(4, 4, 4)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(jLabel5)
                                    .add(jLabel6)))
                            .add(titleLabel))
                        .add(58, 58, 58))
                    .add(layout.createSequentialGroup()
                        .add(errorLabel)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(okButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)))
                .add(cancelButton)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(titleLabel)
                .add(12, 12, 12)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel2)
                    .add(seilnenndurchmesserFormattedTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel5))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1)
                    .add(countTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel3)
                    .add(seilbiegelaengeFormattedTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel6))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 29, Short.MAX_VALUE)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(cancelButton)
                        .add(okButton))
                    .add(errorLabel))
                .addContainerGap())
        );
        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
    	mainFrame.setEnabled(true);
    	dispose();
    }//GEN-LAST:event_formWindowClosing

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
    	mainFrame.setEnabled(true);
    	dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed

    /**
     * Ok button action performed.
     * 
     * @param evt the evt
     */
    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
    	int count = GUIHelper.getIntFromTextField(countTextField);
		double durchmesser = seil.getD();
		int biegelaenge = seil.getL();
		try {
			durchmesser = (df_nopoint.parse(seilnenndurchmesserFormattedTextField.getText())).doubleValue();
			biegelaenge = (df_nopoint.parse(seilbiegelaengeFormattedTextField.getText())).intValue();
		}
		catch (ParseException e) {
			// auto-corrected by FormattedTextField
		}
    	if (count > 0) {
    		seil.setCount(count);
    		seil.setNenndurchmesser(durchmesser);
    		seil.setBiegelaenge(biegelaenge);
    		mainFrame.refreshSeil(seil);
    		mainFrame.setEnabled(true);
    		mainFrame.parameterChanged();
    		dispose();
    	}
    	else {
    		errorLabel.setText("Anzahl ist nicht korrekt!");
    	}
    }//GEN-LAST:event_okButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JTextField countTextField;
    private javax.swing.JLabel errorLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JButton okButton;
    private javax.swing.JFormattedTextField seilbiegelaengeFormattedTextField;
    private javax.swing.JFormattedTextField seilnenndurchmesserFormattedTextField;
    private javax.swing.JLabel titleLabel;
    // End of variables declaration//GEN-END:variables

}
