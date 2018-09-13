package de.tbosch.aufzugseile.gui;

import java.text.DecimalFormat;
import java.text.ParseException;

import javax.swing.ImageIcon;

import de.tbosch.aufzugseile.utils.Constants;
import de.tbosch.aufzugseile.utils.Helper;

/**
 *
 * @author  schulung
 */
public class GlobalParametersFrame extends javax.swing.JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7481407904030143647L;

	/** The main frame. */
	private MainFrame mainFrame;    
	
	/** The decimal format with point (double). */
	private DecimalFormat df_point, df_nopoint;
	
	/** The global parameters to set here. */
	private double fs1, fs3, fs4, fn1, tolleranz, v;
    
    /** Creates new form GlobalParametersFrame */
    public GlobalParametersFrame(MainFrame mainFrame) {
    	df_point = new DecimalFormat();
		df_point.setMaximumFractionDigits(2);
		df_point.setMinimumFractionDigits(0);
		df_point.setGroupingUsed(false);
		df_nopoint = new DecimalFormat();
		df_nopoint.setMaximumFractionDigits(0);
		df_nopoint.setGroupingUsed(false);
        initComponents();
        this.mainFrame = mainFrame;
        setValues();
        setButtonGroups();
    	
		ImageIcon imageIcon = new ImageIcon(Helper.getFileURL(Constants.PROGRAM_ICON));
		setIconImage(imageIcon.getImage());
    }
    
    /**
     * Sets the values for the parameters in the TextFields.
     */
    private void setValues() {
        // if something do ....................
        // get values from main frame
        try {
        	double belastung = (df_point.parse(mainFrame.getFs3FormattedTextField().getText())).doubleValue();
			if (belastung == 1) {
        		getrWippeRadioButton.setSelected(true);
        	}
	        else if (belastung == 1.1) {
	        	getrKWippeRadioButton.setSelected(true);
	        }
	        else if (belastung == 1.15) {
	        	gemZweiRadioButton.setSelected(true);
	        }
	        else if (belastung == 1.25) {
	        	gemMehrRadioButton.setSelected(true);
	        }
        	fs3 = belastung;
		}
		catch (ParseException e) {
	    	// set defaults
			fs3 = 1.25;
			gemMehrRadioButton.setSelected(true);
		}
		if (fs3 == 0) {
	    	// set defaults
			fs3 = 1.25;
			gemMehrRadioButton.setSelected(true);
		}
        
        try {
        	double fuehrung = (df_point.parse(mainFrame.getFs1FormattedTextField().getText())).doubleValue();
        	if (fuehrung == 1.1) {
				gleitfuehrungRadioButton.setSelected(true);
			}
			else if (fuehrung == 1.05) {
				rollenfuehrungRadioButton.setSelected(true);
			}
			fs1 = fuehrung;
		}
		catch (ParseException e) {
			// set defaults
			fs1 = 1.05;
			rollenfuehrungRadioButton.setSelected(true);
		}
		if (fs1 == 0) {
	    	// set defaults
			fs1 = 1.05;
			rollenfuehrungRadioButton.setSelected(true);
		}
		        
		try {
			double beschl = (df_point.parse(mainFrame.getFs4FormattedTextField().getText())).doubleValue();
			fs4 = beschl;
		}
		catch (ParseException e) {
			// set defaults
			fs4 = 1.15;
		}    
		if (fs4 == 0) {
	    	// set defaults
			fs4 = 1.15;
		} 
		
		double geschw = mainFrame.getAufzugschacht().getGeschwindigkeit(); 
		if (geschw != 0) {
			v = geschw;
			geschwFormattedTextFieldKeyReleased(null);
		}
		else {
			// default
			v = 1.5;
			geschwFormattedTextFieldKeyReleased(null);
		}
		
		try {
			double geschm = (df_point.parse(mainFrame.getFn1FormattedTextField().getText())).doubleValue();
			if (geschm == 1) {
				geschmiertRadioButton.setSelected(true);
			}
			else if (geschm == 0.2) {
				ungeschmiertRadioButton.setSelected(true);
			}
			fn1 = geschm;
		}
		catch (ParseException e) {
			// set defaults
			fn1 = 1;
			geschmiertRadioButton.setSelected(true);
		}    
		if (fn1 == 0) {
			// set defaults
			fn1 = 1;
			geschmiertRadioButton.setSelected(true);
		} 
		
		try {
			tolleranz = (double)df_point.parse(mainFrame.getTolleranzFormattedTextField().getText()).intValue() / 100;
		}
		catch (ParseException e) {
			// set defaults
			tolleranz = 0.8;
		}
		if (tolleranz == 0) {
			// set defaults
			tolleranz = 0.8;
		} 
        
        setTextFieldEntries();
    }
    
    /**
     * Sets the text field entries.
     */
    private void setTextFieldEntries() {
		belastungFormattedTextField.setText(df_point.format(fs3));
		fuehrungFormattedTextField.setText(df_point.format(fs1));
        beschleunigungFormattedTextField.setText(df_point.format(fs4));
        schmierungFormattedTextField.setText(df_point.format(fn1));
        tolleranzFormattedTextField.setText(df_nopoint.format(tolleranz*100));
        geschwFormattedTextField.setText(df_point.format(v));
    }
    
    /**
     * Sets the button groups.
     */
    private void setButtonGroups() {
    	// button groups
        belastungButtonGroup.add(getrKWippeRadioButton);
        belastungButtonGroup.add(getrWippeRadioButton);
        belastungButtonGroup.add(gemMehrRadioButton);
        belastungButtonGroup.add(gemZweiRadioButton);
        
        fuehrungButtonGroup.add(gleitfuehrungRadioButton);
        fuehrungButtonGroup.add(rollenfuehrungRadioButton);
        
        schmierungButtonGroup.add(geschmiertRadioButton);
        schmierungButtonGroup.add(ungeschmiertRadioButton);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        belastungButtonGroup = new javax.swing.ButtonGroup();
        fuehrungButtonGroup = new javax.swing.ButtonGroup();
        schmierungButtonGroup = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        cancelButton = new javax.swing.JButton();
        okButton = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        rollenfuehrungRadioButton = new javax.swing.JRadioButton();
        gleitfuehrungRadioButton = new javax.swing.JRadioButton();
        fuehrungFormattedTextField = new javax.swing.JFormattedTextField();
        belastungFormattedTextField = new javax.swing.JFormattedTextField();
        getrWippeRadioButton = new javax.swing.JRadioButton();
        getrKWippeRadioButton = new javax.swing.JRadioButton();
        beschleunigungFormattedTextField = new javax.swing.JFormattedTextField();
        schmierungFormattedTextField = new javax.swing.JFormattedTextField();
        geschmiertRadioButton = new javax.swing.JRadioButton();
        ungeschmiertRadioButton = new javax.swing.JRadioButton();
        jLabel7 = new javax.swing.JLabel();
        gemZweiRadioButton = new javax.swing.JRadioButton();
        gemMehrRadioButton = new javax.swing.JRadioButton();
        jLabel6 = new javax.swing.JLabel();
        tolleranzFormattedTextField = new javax.swing.JFormattedTextField(df_nopoint);
        jLabel8 = new javax.swing.JLabel();
        geschwFormattedTextField = new javax.swing.JFormattedTextField(df_point);
        jLabel9 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Globale Parameter");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jLabel1.setText("Globale Parameter");

        jLabel2.setText("Reibung der Lastf"+Constants.UE_K+"hrung (fs1):");

        jLabel4.setText("Belastung der parallelen Seile (fs3):");

        jLabel5.setText("Beschleunigungsweg (fs4):");

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

        jLabel3.setText("Seilschmierung (fn1):");

        rollenfuehrungRadioButton.setText("Rollenf"+Constants.UE_K+"hrung");
        rollenfuehrungRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rollenfuehrungRadioButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rollenfuehrungRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rollenfuehrungRadioButtonActionPerformed(evt);
            }
        });

        gleitfuehrungRadioButton.setText("Gleitf"+Constants.UE_K+"hrung");
        gleitfuehrungRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        gleitfuehrungRadioButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gleitfuehrungRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gleitfuehrungRadioButtonActionPerformed(evt);
            }
        });

        fuehrungFormattedTextField.setEditable(false);

        belastungFormattedTextField.setEditable(false);

        getrWippeRadioButton.setText("auf getrennten Seilscheiben mit Wippe (Ausgleichsrolle)");
        getrWippeRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        getrWippeRadioButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        getrWippeRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getrWippeRadioButtonActionPerformed(evt);
            }
        });

        getrKWippeRadioButton.setText("auf getrennten Seilscheiben ohne Wippe");
        getrKWippeRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        getrKWippeRadioButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        getrKWippeRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getrKWippeRadioButtonActionPerformed(evt);
            }
        });

        beschleunigungFormattedTextField.setEditable(false);

        schmierungFormattedTextField.setEditable(false);

        geschmiertRadioButton.setText("geschmiert");
        geschmiertRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        geschmiertRadioButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        geschmiertRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                geschmiertRadioButtonActionPerformed(evt);
            }
        });

        ungeschmiertRadioButton.setText("ungeschmiert");
        ungeschmiertRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        ungeschmiertRadioButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        ungeschmiertRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ungeschmiertRadioButtonActionPerformed(evt);
            }
        });

        jLabel7.setText("Lastgeschwindigkeit");

        gemZweiRadioButton.setText("auf gemeinsamer Seilscheibe, zwei Seile, ohne Wippe");
        gemZweiRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        gemZweiRadioButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gemZweiRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gemZweiRadioButtonActionPerformed(evt);
            }
        });

        gemMehrRadioButton.setText("auf gemeinsamer Seilscheibe, mehrere Seile");
        gemMehrRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        gemMehrRadioButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gemMehrRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gemMehrRadioButtonActionPerformed(evt);
            }
        });

        jLabel6.setText("Seilablage bei:");

        tolleranzFormattedTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tolleranzFormattedTextFieldKeyReleased(evt);
            }
        });

        jLabel8.setText("%");

        geschwFormattedTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                geschwFormattedTextFieldKeyReleased(evt);
            }
        });

        jLabel9.setText("m/s:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(461, Short.MAX_VALUE)
                .addComponent(okButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cancelButton)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(513, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel5)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(geschwFormattedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel9))
                    .addComponent(jLabel7)
                    .addComponent(fuehrungFormattedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rollenfuehrungRadioButton)
                    .addComponent(gleitfuehrungRadioButton)
                    .addComponent(belastungFormattedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(getrWippeRadioButton)
                    .addComponent(getrKWippeRadioButton)
                    .addComponent(gemZweiRadioButton)
                    .addComponent(gemMehrRadioButton)
                    .addComponent(beschleunigungFormattedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(91, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(91, 91, 91)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel6)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(schmierungFormattedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(tolleranzFormattedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel8))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(geschmiertRadioButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ungeschmiertRadioButton)))
                .addContainerGap(231, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(fuehrungFormattedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rollenfuehrungRadioButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(gleitfuehrungRadioButton)))
                .addGap(19, 19, 19)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(belastungFormattedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(getrWippeRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(getrKWippeRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(gemZweiRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(gemMehrRadioButton)
                .addGap(17, 17, 17)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(beschleunigungFormattedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(geschwFormattedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(schmierungFormattedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(geschmiertRadioButton)
                    .addComponent(ungeschmiertRadioButton))
                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(tolleranzFormattedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelButton)
                    .addComponent(okButton))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void geschwFormattedTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_geschwFormattedTextFieldKeyReleased
    	try {
			double geschw = df_point.parse(geschwFormattedTextField.getText()).doubleValue();
			v = geschw;
	    	if (v <= 0.3) {
	    		fs4 = 1.05;
	    	}
	    	else if (v <= 0.8) {
	    		fs4 = 1.1;
	    	}
	    	else if (v <= 1.6) {
	    		fs4 = 1.15;
	    	}
	    	else {
	    		fs4 = 1.2;
	    	}
	    	beschleunigungFormattedTextField.setText(df_point.format(fs4));
		}
		catch (ParseException e) {
			// Auto-corrected by formattedtextfield
		}
    }//GEN-LAST:event_geschwFormattedTextFieldKeyReleased

    private void ungeschmiertRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ungeschmiertRadioButtonActionPerformed
    	fn1 = 0.2;
    	setTextFieldEntries();
    }//GEN-LAST:event_ungeschmiertRadioButtonActionPerformed

    private void geschmiertRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_geschmiertRadioButtonActionPerformed
    	fn1 = 1;
    	setTextFieldEntries();
    }//GEN-LAST:event_geschmiertRadioButtonActionPerformed

    private void gemMehrRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gemMehrRadioButtonActionPerformed
    	fs3 = 1.25;
    	setTextFieldEntries();
    }//GEN-LAST:event_gemMehrRadioButtonActionPerformed

    private void gemZweiRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gemZweiRadioButtonActionPerformed
    	fs3 = 1.15;
    	setTextFieldEntries();
    }//GEN-LAST:event_gemZweiRadioButtonActionPerformed

    private void getrKWippeRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_getrKWippeRadioButtonActionPerformed
    	fs3 = 1.1;
    	setTextFieldEntries();
    }//GEN-LAST:event_getrKWippeRadioButtonActionPerformed

    private void getrWippeRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_getrWippeRadioButtonActionPerformed
    	fs3 = 1;
    	setTextFieldEntries();
    }//GEN-LAST:event_getrWippeRadioButtonActionPerformed

    private void gleitfuehrungRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gleitfuehrungRadioButtonActionPerformed
    	fs1 = 1.1;
    	setTextFieldEntries();
    }//GEN-LAST:event_gleitfuehrungRadioButtonActionPerformed

    private void rollenfuehrungRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rollenfuehrungRadioButtonActionPerformed
    	fs1 = 1.05;
    	setTextFieldEntries();
    }//GEN-LAST:event_rollenfuehrungRadioButtonActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
    	mainFrame.setEnabled(true);
    	dispose();
    }//GEN-LAST:event_formWindowClosing

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
    	mainFrame.setEnabled(true);
    	dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
    	mainFrame.setParameters(fs1, fs3, fs4, fn1, tolleranz);
    	mainFrame.getAufzugschacht().setGeschwindigkeit(v);
    	mainFrame.setEnabled(true);
		mainFrame.parameterChanged();
    	dispose();
    }//GEN-LAST:event_okButtonActionPerformed

    private void tolleranzFormattedTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tolleranzFormattedTextFieldKeyReleased
    	try {
			double toleranz = df_nopoint.parse(tolleranzFormattedTextField.getText()).doubleValue();
			tolleranz = toleranz / 100;
		}
		catch (ParseException e) {
			// auto-catched by formattedtextfield
			e.printStackTrace();
		}
    }//GEN-LAST:event_tolleranzFormattedTextFieldKeyReleased
        
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup belastungButtonGroup;
    private javax.swing.JFormattedTextField belastungFormattedTextField;
    private javax.swing.JFormattedTextField beschleunigungFormattedTextField;
    private javax.swing.JButton cancelButton;
    private javax.swing.ButtonGroup fuehrungButtonGroup;
    private javax.swing.JFormattedTextField fuehrungFormattedTextField;
    private javax.swing.JRadioButton gemMehrRadioButton;
    private javax.swing.JRadioButton gemZweiRadioButton;
    private javax.swing.JRadioButton geschmiertRadioButton;
    private javax.swing.JFormattedTextField geschwFormattedTextField;
    private javax.swing.JRadioButton getrKWippeRadioButton;
    private javax.swing.JRadioButton getrWippeRadioButton;
    private javax.swing.JRadioButton gleitfuehrungRadioButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JButton okButton;
    private javax.swing.JRadioButton rollenfuehrungRadioButton;
    private javax.swing.ButtonGroup schmierungButtonGroup;
    private javax.swing.JFormattedTextField schmierungFormattedTextField;
    private javax.swing.JFormattedTextField tolleranzFormattedTextField;
    private javax.swing.JRadioButton ungeschmiertRadioButton;
    // End of variables declaration//GEN-END:variables
}
