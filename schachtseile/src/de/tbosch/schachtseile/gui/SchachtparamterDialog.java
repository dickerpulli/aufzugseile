package de.tbosch.schachtseile.gui;

import de.tbosch.seile.commons.CommonConstants;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 *
 * @author Thomas Bosch (tbosch@gmx.de)
 */
public class SchachtparamterDialog extends javax.swing.JDialog {
	
	/** der Logger */
	private Logger logger = Logger.getLogger(SchachtparamterDialog.class);
	
	private DecimalFormat df_point;
	private DecimalFormat df_nopoint;
	private Map<String, Double> parameters;
	private int biegelaenge;
	private SchachtseileView mainView;
	
	/** Creates new form SchachtparamterDialog */
	public SchachtparamterDialog(java.awt.Frame parent, boolean modal, SchachtseileView mainView) {
		super(parent, modal);
		this.mainView = mainView;
		df_point = new DecimalFormat();
		df_point.setMaximumFractionDigits(2);
		df_point.setMinimumFractionDigits(2);
		df_point.setMaximumIntegerDigits(1);
		df_nopoint = new DecimalFormat();
		df_nopoint.setMaximumFractionDigits(0);
		df_nopoint.setMinimumFractionDigits(0);
		initComponents();
	}
	
	public void setParamters(Map<String, Double> parameters, int biegelaenge) {
		this.parameters = parameters;
		this.biegelaenge = biegelaenge;
		fs1FormattedTextField.setText(df_point.format(parameters.get(CommonConstants.PARAMETER_NAME_FS1)));
		fs2FormattedTextField.setText(df_point.format(parameters.get(CommonConstants.PARAMETER_NAME_FS2)));
		fs3FormattedTextField.setText(df_point.format(parameters.get(CommonConstants.PARAMETER_NAME_FS3)));
		fs4FormattedTextField.setText(df_point.format(parameters.get(CommonConstants.PARAMETER_NAME_FS4)));
		fs5FormattedTextField.setText(df_point.format(parameters.get(CommonConstants.PARAMETER_NAME_FS5)));
		fn1FormattedTextField.setText(df_point.format(parameters.get(CommonConstants.PARAMETER_NAME_FN1)));
		fn2FormattedTextField.setText(df_point.format(parameters.get(CommonConstants.PARAMETER_NAME_FN2)));
		fn3FormattedTextField.setText(df_point.format(parameters.get(CommonConstants.PARAMETER_NAME_FN3)));
		fn4FormattedTextField.setText(df_point.format(parameters.get(CommonConstants.PARAMETER_NAME_FN4)));
		fn5FormattedTextField.setText(df_point.format(parameters.get(CommonConstants.PARAMETER_NAME_FN5)));
		biegelaengeFormattedTextField.setText(df_nopoint.format(biegelaenge));
	}
	
	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        fs1FormattedTextField = new javax.swing.JFormattedTextField(df_point);
        fs2FormattedTextField = new javax.swing.JFormattedTextField(df_point);
        fs3FormattedTextField = new javax.swing.JFormattedTextField(df_point);
        fs4FormattedTextField = new javax.swing.JFormattedTextField(df_point);
        fs5FormattedTextField = new javax.swing.JFormattedTextField(df_point);
        jPanel2 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        fn1FormattedTextField = new javax.swing.JFormattedTextField(df_point);
        fn2FormattedTextField = new javax.swing.JFormattedTextField(df_point);
        fn3FormattedTextField = new javax.swing.JFormattedTextField(df_point);
        fn4FormattedTextField = new javax.swing.JFormattedTextField(df_point);
        fn5FormattedTextField = new javax.swing.JFormattedTextField(df_point);
        jLabel12 = new javax.swing.JLabel();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jPanel3 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        biegelaengeFormattedTextField = new javax.swing.JFormattedTextField(df_nopoint);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(de.tbosch.schachtseile.SchachtseileApp.class).getContext().getResourceMap(SchachtparamterDialog.class);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setName("Form"); // NOI18N
        setResizable(false);

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        jPanel1.setName("jPanel1"); // NOI18N
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 1, 4, 1);
        jPanel1.add(jLabel2, gridBagConstraints);

        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 1, 4, 1);
        jPanel1.add(jLabel4, gridBagConstraints);

        jLabel5.setText(resourceMap.getString("jLabel5.text")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 1, 4, 1);
        jPanel1.add(jLabel5, gridBagConstraints);

        jLabel6.setText(resourceMap.getString("jLabel6.text")); // NOI18N
        jLabel6.setName("jLabel6"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 1, 4, 1);
        jPanel1.add(jLabel6, gridBagConstraints);

        jLabel11.setText(resourceMap.getString("jLabel11.text")); // NOI18N
        jLabel11.setName("jLabel11"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 1, 4, 1);
        jPanel1.add(jLabel11, gridBagConstraints);

        fs1FormattedTextField.setText(resourceMap.getString("fs1FormattedTextField.text")); // NOI18N
        fs1FormattedTextField.setName("fs1FormattedTextField"); // NOI18N
        fs1FormattedTextField.setPreferredSize(new java.awt.Dimension(50, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(fs1FormattedTextField, gridBagConstraints);

        fs2FormattedTextField.setEditable(false);
        fs2FormattedTextField.setName("fs2FormattedTextField"); // NOI18N
        fs2FormattedTextField.setPreferredSize(new java.awt.Dimension(50, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(fs2FormattedTextField, gridBagConstraints);

        fs3FormattedTextField.setName("fs3FormattedTextField"); // NOI18N
        fs3FormattedTextField.setPreferredSize(new java.awt.Dimension(50, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(fs3FormattedTextField, gridBagConstraints);

        fs4FormattedTextField.setName("fs4FormattedTextField"); // NOI18N
        fs4FormattedTextField.setPreferredSize(new java.awt.Dimension(50, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(fs4FormattedTextField, gridBagConstraints);

        fs5FormattedTextField.setName("fs5FormattedTextField"); // NOI18N
        fs5FormattedTextField.setPreferredSize(new java.awt.Dimension(50, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(fs5FormattedTextField, gridBagConstraints);

        jPanel2.setName("jPanel2"); // NOI18N
        jPanel2.setLayout(new java.awt.GridBagLayout());

        jLabel10.setText(resourceMap.getString("jLabel10.text")); // NOI18N
        jLabel10.setName("jLabel10"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 1, 4, 1);
        jPanel2.add(jLabel10, gridBagConstraints);

        jLabel7.setText(resourceMap.getString("jLabel7.text")); // NOI18N
        jLabel7.setName("jLabel7"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 1, 4, 1);
        jPanel2.add(jLabel7, gridBagConstraints);

        jLabel9.setText(resourceMap.getString("jLabel9.text")); // NOI18N
        jLabel9.setName("jLabel9"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 1, 4, 1);
        jPanel2.add(jLabel9, gridBagConstraints);

        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 1, 4, 1);
        jPanel2.add(jLabel3, gridBagConstraints);

        jLabel8.setText(resourceMap.getString("jLabel8.text")); // NOI18N
        jLabel8.setName("jLabel8"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 1, 4, 1);
        jPanel2.add(jLabel8, gridBagConstraints);

        fn1FormattedTextField.setText(resourceMap.getString("fn1FormattedTextField.text")); // NOI18N
        fn1FormattedTextField.setName("fn1FormattedTextField"); // NOI18N
        fn1FormattedTextField.setPreferredSize(new java.awt.Dimension(50, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel2.add(fn1FormattedTextField, gridBagConstraints);

        fn2FormattedTextField.setEditable(false);
        fn2FormattedTextField.setName("fn2FormattedTextField"); // NOI18N
        fn2FormattedTextField.setPreferredSize(new java.awt.Dimension(50, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel2.add(fn2FormattedTextField, gridBagConstraints);

        fn3FormattedTextField.setEditable(false);
        fn3FormattedTextField.setName("fn3FormattedTextField"); // NOI18N
        fn3FormattedTextField.setPreferredSize(new java.awt.Dimension(50, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel2.add(fn3FormattedTextField, gridBagConstraints);

        fn4FormattedTextField.setEditable(false);
        fn4FormattedTextField.setName("fn4FormattedTextField"); // NOI18N
        fn4FormattedTextField.setPreferredSize(new java.awt.Dimension(50, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel2.add(fn4FormattedTextField, gridBagConstraints);

        fn5FormattedTextField.setEditable(false);
        fn5FormattedTextField.setName("fn5FormattedTextField"); // NOI18N
        fn5FormattedTextField.setPreferredSize(new java.awt.Dimension(50, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel2.add(fn5FormattedTextField, gridBagConstraints);

        jLabel12.setText(resourceMap.getString("jLabel12.text")); // NOI18N
        jLabel12.setName("jLabel12"); // NOI18N

        okButton.setText(resourceMap.getString("okButton.text")); // NOI18N
        okButton.setName("okButton"); // NOI18N
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        cancelButton.setText(resourceMap.getString("cancelButton.text")); // NOI18N
        cancelButton.setName("cancelButton"); // NOI18N
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        jScrollPane1.setBorder(null);
        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jTextArea1.setBackground(resourceMap.getColor("jTextArea1.background")); // NOI18N
        jTextArea1.setColumns(20);
        jTextArea1.setEditable(false);
        jTextArea1.setRows(3);
        jTextArea1.setText(resourceMap.getString("jTextArea1.text")); // NOI18N
        jTextArea1.setBorder(null);
        jTextArea1.setName("jTextArea1"); // NOI18N
        jScrollPane1.setViewportView(jTextArea1);

        jPanel3.setName("jPanel3"); // NOI18N
        jPanel3.setLayout(new java.awt.GridBagLayout());

        jLabel13.setText(resourceMap.getString("jLabel13.text")); // NOI18N
        jLabel13.setName("jLabel13"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 1, 4, 1);
        jPanel3.add(jLabel13, gridBagConstraints);

        jLabel14.setText(resourceMap.getString("jLabel14.text")); // NOI18N
        jLabel14.setName("jLabel14"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel3.add(jLabel14, gridBagConstraints);

        biegelaengeFormattedTextField.setText(resourceMap.getString("biegelaengeFormattedTextField.text")); // NOI18N
        biegelaengeFormattedTextField.setName("biegelaengeFormattedTextField"); // NOI18N
        biegelaengeFormattedTextField.setPreferredSize(new java.awt.Dimension(50, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        jPanel3.add(biegelaengeFormattedTextField, gridBagConstraints);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 226, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(38, 38, 38)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel12)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(cancelButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(okButton))
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 226, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(okButton)
                    .addComponent(cancelButton))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

	private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
		dispose();
	}//GEN-LAST:event_cancelButtonActionPerformed

	private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
		speicherParameter();
		mainView.getDateiOperationenService().getFoerderschacht().setDoubleParameters(parameters);
		mainView.getDateiOperationenService().getFoerderschacht().getSeil().setBiegelaenge(biegelaenge);
		mainView.getDateiOperationenService().getBerechnung().setDoubleParameters(parameters);
		mainView.somethingChanged();
		dispose();
	}//GEN-LAST:event_okButtonActionPerformed
	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JFormattedTextField biegelaengeFormattedTextField;
    private javax.swing.JButton cancelButton;
    private javax.swing.JFormattedTextField fn1FormattedTextField;
    private javax.swing.JFormattedTextField fn2FormattedTextField;
    private javax.swing.JFormattedTextField fn3FormattedTextField;
    private javax.swing.JFormattedTextField fn4FormattedTextField;
    private javax.swing.JFormattedTextField fn5FormattedTextField;
    private javax.swing.JFormattedTextField fs1FormattedTextField;
    private javax.swing.JFormattedTextField fs2FormattedTextField;
    private javax.swing.JFormattedTextField fs3FormattedTextField;
    private javax.swing.JFormattedTextField fs4FormattedTextField;
    private javax.swing.JFormattedTextField fs5FormattedTextField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JButton okButton;
    // End of variables declaration//GEN-END:variables
	
	/**
	 * Setzt die geänderten Paramter im HashMap
	 */
	private void speicherParameter() {
		try {
			parameters.put(CommonConstants.PARAMETER_NAME_FS1, df_point.parse(fs1FormattedTextField.getText()).doubleValue());
			parameters.put(CommonConstants.PARAMETER_NAME_FS2, df_point.parse(fs2FormattedTextField.getText()).doubleValue());
			parameters.put(CommonConstants.PARAMETER_NAME_FS3, df_point.parse(fs3FormattedTextField.getText()).doubleValue());
			parameters.put(CommonConstants.PARAMETER_NAME_FS4, df_point.parse(fs4FormattedTextField.getText()).doubleValue());
			parameters.put(CommonConstants.PARAMETER_NAME_FS5, df_point.parse(fs5FormattedTextField.getText()).doubleValue());
			parameters.put(CommonConstants.PARAMETER_NAME_FN1, df_point.parse(fn1FormattedTextField.getText()).doubleValue());
			parameters.put(CommonConstants.PARAMETER_NAME_FN2, df_point.parse(fn2FormattedTextField.getText()).doubleValue());
			parameters.put(CommonConstants.PARAMETER_NAME_FN3, df_point.parse(fn3FormattedTextField.getText()).doubleValue());
			parameters.put(CommonConstants.PARAMETER_NAME_FN4, df_point.parse(fn4FormattedTextField.getText()).doubleValue());
			parameters.put(CommonConstants.PARAMETER_NAME_FN5, df_point.parse(fn5FormattedTextField.getText()).doubleValue());
			biegelaenge = df_nopoint.parse(biegelaengeFormattedTextField.getText()).intValue();
		}
		catch (ParseException e) {
			logger.error("Parsing wird eigentlicht von FormattedTextField behandelt ???", e);
		}
	}
	
}
