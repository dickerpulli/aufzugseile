package de.tbosch.aufzugseile.gui;

import de.tbosch.commons.Helper;
import de.tbosch.aufzugseile.gui.utils.Constants;
import java.util.Enumeration;

import java.util.logging.Logger;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JRadioButton;
import javax.swing.table.TableModel;


/**
 *
 * @author Thomas Bosch (tbosch@gmx.de)
 */
public class ChooseSeiltypFrame extends javax.swing.JFrame {
    
    /** The Logger */
    private static final Logger logger = Logger.getLogger(ChooseSeiltypFrame.class.getName());
	
	/** The main frame. */
	private MainFrame mainFrame;
    
    /** Creates new form ChooseSeiltypFrame */
    public ChooseSeiltypFrame(TableModel parameterTableModel, MainFrame mainFrame) {
    	initComponents();
    	setSeiltypen(parameterTableModel);
    	this.mainFrame = mainFrame;
    	
		ImageIcon imageIcon = new ImageIcon(Helper.getFileURL(Constants.PROGRAM_ICON));
		setIconImage(imageIcon.getImage());
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        buttonGroup = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        typPanel = new javax.swing.JPanel();
        cancelButton = new javax.swing.JButton();
        okButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jLabel1.setText("Den Seiltyp w\u00e4hlen, f\u00fcr den eine Einzelbetrachtung vorgenommen");

        jLabel2.setText("vorgenommen werden soll.");

        jScrollPane2.setBorder(null);
        typPanel.setLayout(new java.awt.GridLayout(5, 1, 1, 1));

        typPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Seiltyp"));
        jScrollPane2.setViewportView(typPanel);

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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 412, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 142, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(okButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addComponent(cancelButton))
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelButton)
                    .addComponent(okButton))
                .addContainerGap())
        );
        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
    	cancelButtonActionPerformed(null);
    }//GEN-LAST:event_formWindowClosing

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
    	mainFrame.setEnabled(true);
    	dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
    	// set the rope type to show detailed
    	
    	Enumeration<AbstractButton> buttonEnum = buttonGroup.getElements(); 
    	int i = 0;
    	while (buttonEnum.hasMoreElements()) {
    		JRadioButton radioButton = (JRadioButton)buttonEnum.nextElement();
    		if (radioButton.isSelected()) {
    			break;
    		}
    		i++;
    	}
    	if (i <= buttonGroup.getButtonCount()-1) {
        	mainFrame.setIndex(i);
    	}
    	
    	mainFrame.setEnabled(true);
    	dispose();
    }//GEN-LAST:event_okButtonActionPerformed
   
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup;
    private javax.swing.JButton cancelButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton okButton;
    private javax.swing.JPanel typPanel;
    // End of variables declaration//GEN-END:variables
    
    private void setSeiltypen(TableModel parameterTableModel) {
    	for (int i = 0; i < parameterTableModel.getRowCount(); i++) {
			if ((Boolean)parameterTableModel.getValueAt(i, 0) != null && (Boolean)parameterTableModel.getValueAt(i, 0) == true) {
				if (parameterTableModel.getValueAt(i, 1) != null && parameterTableModel.getValueAt(i, 2) != null
						&& parameterTableModel.getValueAt(i, 3) != null && parameterTableModel.getValueAt(i, 4) != null
						&& parameterTableModel.getValueAt(i, 5) != null && parameterTableModel.getValueAt(i, 6) != null
						&& parameterTableModel.getValueAt(i, 7) != null && parameterTableModel.getValueAt(i, 8) != null
						&& parameterTableModel.getValueAt(i, 9) != null && parameterTableModel.getValueAt(i, 10) != null
						&& parameterTableModel.getValueAt(i, 11) != null && parameterTableModel.getValueAt(i, 12) != null) {
					JRadioButton radioButton = new JRadioButton((String)(parameterTableModel.getValueAt(i, 1)));
					typPanel.add(radioButton);
					buttonGroup.add(radioButton);
				}
				else {
					logger.warning("Partly chooing empty rows. Ignoring those rows.");
				}
			}
		}
    }
}
