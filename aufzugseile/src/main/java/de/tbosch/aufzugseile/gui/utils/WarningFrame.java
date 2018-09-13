/*
 * WarningFrame.java
 *
 * Created on 26. November 2007, 20:25
 */

package de.tbosch.aufzugseile.gui.utils;

/**
 *
 * @author  bobo
 */
public class WarningFrame extends javax.swing.JFrame {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 2454703055407805547L;
	
	private ReactOnWarningFrame reactingFrame;
	
	/** Creates new form WarningFrame */
    public WarningFrame(ReactOnWarningFrame reactingFrame, String warningText) {
        initComponents();
        this.reactingFrame = reactingFrame;
        this.warnTextArea.setText(warningText);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        noButton = new javax.swing.JButton();
        yesButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        warnTextArea = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Warnung");

        noButton.setText("Nein");
        noButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                noButtonActionPerformed(evt);
            }
        });

        yesButton.setText("Ja");
        yesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                yesButtonActionPerformed(evt);
            }
        });

        jScrollPane1.setBorder(null);

        warnTextArea.setBackground(new java.awt.Color(238, 238, 238));
        warnTextArea.setColumns(20);
        warnTextArea.setEditable(false);
        warnTextArea.setLineWrap(true);
        warnTextArea.setRows(5);
        warnTextArea.setText("<warntext>");
        warnTextArea.setBorder(null);
        jScrollPane1.setViewportView(warnTextArea);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(yesButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(noButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 79, Short.MAX_VALUE)
                .addGap(5, 5, 5)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(noButton)
                    .addComponent(yesButton))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void yesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_yesButtonActionPerformed
        reactingFrame.yesClicked();
        dispose();
    }//GEN-LAST:event_yesButtonActionPerformed

    private void noButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_noButtonActionPerformed
        reactingFrame.noClicked();
        dispose();
    }//GEN-LAST:event_noButtonActionPerformed
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton noButton;
    private javax.swing.JTextArea warnTextArea;
    private javax.swing.JButton yesButton;
    // End of variables declaration//GEN-END:variables
    
}