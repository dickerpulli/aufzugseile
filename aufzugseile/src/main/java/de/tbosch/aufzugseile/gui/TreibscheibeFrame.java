/*
 * TreibscheibeFrame.java
 *
 * Created on 12. April 2007, 11:28
 */

package de.tbosch.aufzugseile.gui;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;

import de.tbosch.aufzugseile.gui.aufzug.Treibscheibe;
import de.tbosch.aufzugseile.gui.utils.ReactOnWarningFrame;
import de.tbosch.aufzugseile.gui.utils.Utilities;
import de.tbosch.aufzugseile.gui.utils.WarningFrame;
import de.tbosch.aufzugseile.utils.Constants;
import de.tbosch.aufzugseile.utils.Helper;

/**
 * The options frame for the treibscheibe.
 *
 * @author Thomas Bosch (tbosch@gmx.de)
 */
public class TreibscheibeFrame extends javax.swing.JFrame implements ReactOnWarningFrame {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 366275246095608147L;

	/** The treibscheibe itself. */
    private Treibscheibe treibscheibe;
    
    /** The main frame. */
    private MainFrame mainFrame;
    
	/** The decimal format with point (double). */
	private DecimalFormat df_point, df_nopoint;
	
	private int durchmesser;
	private double fs2, fn3, fn4;
	
	private String newForm;
	private int newKeilwinkel;
	private int newUnterschnittwinkel;
    
    /**
     * Creates new form TreibscheibeFrame.
     *
     * @param treibscheibe The treibscheibe itself
     */
    public TreibscheibeFrame(Treibscheibe treibscheibe, MainFrame mainFrame) {
    	df_point = new DecimalFormat();
		df_point.setMaximumFractionDigits(3);
		df_point.setMinimumFractionDigits(0);
		df_point.setGroupingUsed(false);
		df_nopoint = new DecimalFormat();
		df_nopoint.setMaximumFractionDigits(0);
		df_nopoint.setGroupingUsed(false);
		initComponents();
		this.treibscheibe = treibscheibe;
		this.mainFrame = mainFrame;
		setButtonGroups();
		setValues();
    	
		ImageIcon imageIcon = new ImageIcon(Helper.getFileURL(Constants.PROGRAM_ICON));
		setIconImage(imageIcon.getImage());
    }
    
    private void setValues() {
    	durchmesser = treibscheibe.getDurchmesser();
    	
    	fs2 = treibscheibe.getFs2();
		
		boolean rundrille = false;
		boolean keilrille = false;
		boolean sitzrille = false;
		
		newUnterschnittwinkel = 0;
		newKeilwinkel = 45;
		newForm = Constants.FORM_RUND;
		
		double rille = treibscheibe.getFn3();
		int unterschnittwinkel = treibscheibe.getUnterschnittwinkel();
		int keilwinkel = treibscheibe.getKeilwinkel();
		String form = treibscheibe.getForm();
		
		if (form.equals(Constants.FORM_RUND)) {
	    	if (rille == 1) {
	    		rd053RadioButton.setSelected(true);
	    		rundrille = true;
	    	}
	    	else if (rille == 0.79) {
	    		rd055RadioButton.setSelected(true);
	    		rundrille = true;
	    	}
	    	else if (rille == 0.66) {
	    		rd060RadioButton.setSelected(true);
	    		rundrille = true;
	    	}
	    	else if (rille == 0.54) {
	    		rd070RadioButton.setSelected(true);
	    		rundrille = true;
	    	}
	    	else if (rille == 0.51) {
	    		rd080RadioButton.setSelected(true);
	    		rundrille = true;
	    	}
	    	else if (rille == 0.48) {
	    		rd100RadioButton.setSelected(true);
	    		rundrille = true;
	    	}
		}
		else if (form.equals(Constants.FORM_SITZ)) {
	    	sitzrille = true;
	    	unterschnittwinkelFormattedTextField.setText(df_nopoint.format(unterschnittwinkel));
	    	newUnterschnittwinkel = unterschnittwinkel;
		}
		else if (form.equals(Constants.FORM_KEIL)) {
    		keilrille = true;
    		keilwinkelFormattedTextField.setText(df_nopoint.format(keilwinkel));
    		newKeilwinkel = keilwinkel;
		}
    	fn3 = rille;
    	if (rundrille) {
    		enableButtonGroup(false, keilwinkelButtonGroup);
    		enableButtonGroup(false, unterschnittButtonGroup);
    		newForm = Constants.FORM_RUND;
    	}
    	else if (keilrille) {
    		keilrilleRadioButton.setSelected(true);
    		newForm = Constants.FORM_KEIL;
    		setUnterschnittWinkelButton();
    	}
    	else if (sitzrille) {
    		sitzrilleRadioButton.setSelected(true);
    		enableButtonGroup(false, keilwinkelButtonGroup);
    		ohneUnterschnittRadioButton.setEnabled(false);
    		mitUnterschnittRadioButton.setSelected(true);
    		newKeilwinkel = 45;
    		keilwinkelFormattedTextField.setText("45");
    		keilwinkelFormattedTextField.setEnabled(false);
    		newForm = Constants.FORM_SITZ;
    	}
    	else {
    		Constants.LOGGER.severe("Should not be - no rille");
    	}
		
		double schraeg = treibscheibe.getFn4();
		if (schraeg == 1) {
			t0RadioButton.setSelected(true);
		}
		else if (schraeg == 0.9) {
			t1RadioButton.setSelected(true);
		}
		else if (schraeg == 0.75) {
			t2RadioButton.setSelected(true);
		}
		else if (schraeg == 0.70) {
			t3RadioButton.setSelected(true);
		}
		else if (schraeg == 0.67) {
			t4RadioButton.setSelected(true);
		}
		fn4 = schraeg;
		
		setTextFieldEntries();
    }
    
    /**
     * Sets the text field entries.
     */
    private void setTextFieldEntries() {
    	durchmesserFormattedTextField.setText(df_nopoint.format(durchmesser));
		seiltriebwirkungsgradFormattedTextField.setText(df_point.format(fs2));
		seilrilleFormattedTextField.setText(df_point.format(fn3));
		schraegzugFormattedTextField.setText(df_point.format(fn4));
    }
    
    /**
     * Sets the button groups.
     */
    private void setButtonGroups() {
    	seilrilleButtonGroup.add(rd053RadioButton);
		seilrilleButtonGroup.add(rd055RadioButton);
		seilrilleButtonGroup.add(rd060RadioButton);
		seilrilleButtonGroup.add(rd070RadioButton);
		seilrilleButtonGroup.add(rd080RadioButton);
		seilrilleButtonGroup.add(rd100RadioButton);
		seilrilleButtonGroup.add(sitzrilleRadioButton);
		seilrilleButtonGroup.add(keilrilleRadioButton);
		
		unterschnittButtonGroup.add(mitUnterschnittRadioButton);
		unterschnittButtonGroup.add(ohneUnterschnittRadioButton);
		
		schraegzugButtonGroup.add(t0RadioButton);
		schraegzugButtonGroup.add(t1RadioButton);
		schraegzugButtonGroup.add(t2RadioButton);
		schraegzugButtonGroup.add(t3RadioButton);
		schraegzugButtonGroup.add(t4RadioButton);
    }
    
    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        seilrilleButtonGroup = new javax.swing.ButtonGroup();
        schraegzugButtonGroup = new javax.swing.ButtonGroup();
        keilwinkelButtonGroup = new javax.swing.ButtonGroup();
        unterschnittButtonGroup = new javax.swing.ButtonGroup();
        cancelButton = new javax.swing.JButton();
        okButton = new javax.swing.JButton();
        titleLabel = new javax.swing.JLabel();
        durchmesserLabel = new javax.swing.JLabel();
        durchmesserEinheitLabel = new javax.swing.JLabel();
        errorLabel = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        rd053RadioButton = new javax.swing.JRadioButton();
        rd055RadioButton = new javax.swing.JRadioButton();
        rd060RadioButton = new javax.swing.JRadioButton();
        seilrilleFormattedTextField = new javax.swing.JFormattedTextField();
        rd070RadioButton = new javax.swing.JRadioButton();
        rd080RadioButton = new javax.swing.JRadioButton();
        rd100RadioButton = new javax.swing.JRadioButton();
        mitUnterschnittRadioButton = new javax.swing.JRadioButton();
        jLabel2 = new javax.swing.JLabel();
        schraegzugFormattedTextField = new javax.swing.JFormattedTextField();
        t0RadioButton = new javax.swing.JRadioButton();
        t1RadioButton = new javax.swing.JRadioButton();
        t2RadioButton = new javax.swing.JRadioButton();
        t3RadioButton = new javax.swing.JRadioButton();
        t4RadioButton = new javax.swing.JRadioButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        seiltriebwirkungsgradFormattedTextField = new javax.swing.JFormattedTextField(df_point);
        durchmesserFormattedTextField = new javax.swing.JFormattedTextField(df_nopoint);
        sitzrilleRadioButton = new javax.swing.JRadioButton();
        keilrilleRadioButton = new javax.swing.JRadioButton();
        jLabel5 = new javax.swing.JLabel();
        ohneUnterschnittRadioButton = new javax.swing.JRadioButton();
        unterschnittwinkelFormattedTextField = new javax.swing.JFormattedTextField(df_nopoint);
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        keilwinkelFormattedTextField = new javax.swing.JFormattedTextField(df_nopoint);
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Treibscheibenoptionen");
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

        titleLabel.setText("Treibscheibe: Optionen");

        durchmesserLabel.setText("Durchmesser:");

        durchmesserEinheitLabel.setText("mm");

        errorLabel.setForeground(new java.awt.Color(255, 0, 0));

        jLabel1.setText("Seilrille (fn3):");

        rd053RadioButton.setText("Rillenradius r/d = 0,53");
        rd053RadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rd053RadioButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rd053RadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rd053RadioButtonActionPerformed(evt);
            }
        });

        rd055RadioButton.setText("Rillenradius r/d = 0,55");
        rd055RadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rd055RadioButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rd055RadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rd055RadioButtonActionPerformed(evt);
            }
        });

        rd060RadioButton.setText("Rillenradius r/d = 0,60");
        rd060RadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rd060RadioButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rd060RadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rd060RadioButtonActionPerformed(evt);
            }
        });

        seilrilleFormattedTextField.setEditable(false);

        rd070RadioButton.setText("Rillenradius r/d = 0,70");
        rd070RadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rd070RadioButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rd070RadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rd070RadioButtonActionPerformed(evt);
            }
        });

        rd080RadioButton.setText("Rillenradius r/d = 0,80");
        rd080RadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rd080RadioButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rd080RadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rd080RadioButtonActionPerformed(evt);
            }
        });

        rd100RadioButton.setText("Rillenradius r/d = 1,00");
        rd100RadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rd100RadioButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rd100RadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rd100RadioButtonActionPerformed(evt);
            }
        });

        mitUnterschnittRadioButton.setText("mit Unterschnitt");
        mitUnterschnittRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        mitUnterschnittRadioButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        mitUnterschnittRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitUnterschnittRadioButtonActionPerformed(evt);
            }
        });

        jLabel2.setText("Schrägzug (fn4):");

        schraegzugFormattedTextField.setEditable(false);

        t0RadioButton.setText("Ablenkwinkel teta=0°");
        t0RadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        t0RadioButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        t0RadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t0RadioButtonActionPerformed(evt);
            }
        });

        t1RadioButton.setText("Ablenkwinkel teta=1°");
        t1RadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        t1RadioButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        t1RadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t1RadioButtonActionPerformed(evt);
            }
        });

        t2RadioButton.setText("Ablenkwinkel teta=2°");
        t2RadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        t2RadioButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        t2RadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t2RadioButtonActionPerformed(evt);
            }
        });

        t3RadioButton.setText("Ablenkwinkel teta=3°");
        t3RadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        t3RadioButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        t3RadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t3RadioButtonActionPerformed(evt);
            }
        });

        t4RadioButton.setText("Ablenkwinkel teta=4°");
        t4RadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        t4RadioButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        t4RadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t4RadioButtonActionPerformed(evt);
            }
        });

        jLabel3.setText("Rundrille");

        jLabel4.setText("Sitzrille/Keilrille mit/ohne Unterschnitt");

        jLabel6.setText("Seiltriebwirkungsgrad (fs2):");

        seiltriebwirkungsgradFormattedTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                seiltriebwirkungsgradFormattedTextFieldKeyReleased(evt);
            }
        });

        durchmesserFormattedTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                durchmesserFormattedTextFieldKeyReleased(evt);
            }
        });

        sitzrilleRadioButton.setText("Sitzrille");
        sitzrilleRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        sitzrilleRadioButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        sitzrilleRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sitzrilleRadioButtonActionPerformed(evt);
            }
        });

        keilrilleRadioButton.setText("Keilrille");
        keilrilleRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        keilrilleRadioButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        keilrilleRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                keilrilleRadioButtonActionPerformed(evt);
            }
        });

        jLabel5.setText("Unterschnitt- und Keilwinkel");

        ohneUnterschnittRadioButton.setText("ohne Unterschnitt");
        ohneUnterschnittRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        ohneUnterschnittRadioButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        ohneUnterschnittRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ohneUnterschnittRadioButtonActionPerformed(evt);
            }
        });

        unterschnittwinkelFormattedTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                unterschnittwinkelFormattedTextFieldKeyReleased(evt);
            }
        });

        jLabel7.setText("Unterschnittwinkel:");

        jLabel8.setText("Keilwinkel:");

        keilwinkelFormattedTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                keilwinkelFormattedTextFieldKeyReleased(evt);
            }
        });

        jLabel9.setText("Grad");

        jLabel10.setText("Grad");

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(16, 16, 16)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(titleLabel)
                            .add(layout.createSequentialGroup()
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(layout.createSequentialGroup()
                                        .add(12, 12, 12)
                                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                                            .add(durchmesserLabel)
                                            .add(jLabel6)
                                            .add(jLabel1))
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                                            .add(rd055RadioButton)
                                            .add(rd060RadioButton)
                                            .add(rd053RadioButton)
                                            .add(rd070RadioButton)
                                            .add(rd080RadioButton)
                                            .add(rd100RadioButton)
                                            .add(jLabel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                                                    .add(org.jdesktop.layout.GroupLayout.LEADING, seiltriebwirkungsgradFormattedTextField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 56, Short.MAX_VALUE)
                                                    .add(durchmesserFormattedTextField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 56, Short.MAX_VALUE)
                                                    .add(org.jdesktop.layout.GroupLayout.LEADING, seilrilleFormattedTextField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 56, Short.MAX_VALUE))
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                .add(durchmesserEinheitLabel)
                                                .add(62, 62, 62))))
                                    .add(layout.createSequentialGroup()
                                        .add(70, 70, 70)
                                        .add(jLabel2)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                            .add(t2RadioButton)
                                            .add(t3RadioButton)
                                            .add(t4RadioButton)
                                            .add(t0RadioButton)
                                            .add(t1RadioButton)
                                            .add(schraegzugFormattedTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 46, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                                .add(54, 54, 54)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(mitUnterschnittRadioButton)
                                    .add(jLabel5)
                                    .add(layout.createSequentialGroup()
                                        .add(jLabel7)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(unterschnittwinkelFormattedTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 49, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(jLabel10))
                                    .add(keilrilleRadioButton)
                                    .add(sitzrilleRadioButton)
                                    .add(layout.createSequentialGroup()
                                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                                            .add(layout.createSequentialGroup()
                                                .add(jLabel8)
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                .add(keilwinkelFormattedTextField))
                                            .add(org.jdesktop.layout.GroupLayout.LEADING, ohneUnterschnittRadioButton))
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(jLabel9))
                                    .add(jLabel4)))))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .add(errorLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE)
                        .add(215, 215, 215)
                        .add(okButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(cancelButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(titleLabel)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(durchmesserLabel)
                            .add(durchmesserFormattedTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(durchmesserEinheitLabel))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabel6)
                            .add(seiltriebwirkungsgradFormattedTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabel1)
                            .add(seilrilleFormattedTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .add(6, 6, 6)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(layout.createSequentialGroup()
                                .add(jLabel3)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(rd053RadioButton)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(rd055RadioButton)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(rd060RadioButton)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(rd070RadioButton)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(rd080RadioButton)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(rd100RadioButton)
                                .add(18, 18, 18)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                    .add(jLabel2)
                                    .add(schraegzugFormattedTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(t0RadioButton)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(t1RadioButton)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(t2RadioButton)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(t3RadioButton)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(t4RadioButton)
                                .add(12, 12, 12)
                                .add(errorLabel))
                            .add(layout.createSequentialGroup()
                                .add(jLabel4)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(sitzrilleRadioButton)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(keilrilleRadioButton)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jLabel5)
                                .add(2, 2, 2)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                    .add(jLabel8)
                                    .add(keilwinkelFormattedTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .add(jLabel9))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(ohneUnterschnittRadioButton)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(mitUnterschnittRadioButton)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                    .add(jLabel7)
                                    .add(unterschnittwinkelFormattedTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .add(jLabel10))))
                        .add(24, 24, 24))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(cancelButton)
                            .add(okButton))
                        .addContainerGap())))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ohneUnterschnittRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ohneUnterschnittRadioButtonActionPerformed
    	newUnterschnittwinkel = 0;
    	unterschnittwinkelFormattedTextField.setText("0");
    	unterschnittwinkelFormattedTextField.setEnabled(false);
		refreshSeilrilleField();
    }//GEN-LAST:event_ohneUnterschnittRadioButtonActionPerformed

    private void sitzrilleRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sitzrilleRadioButtonActionPerformed
    	enableButtonGroup(true, unterschnittButtonGroup);
    	mitUnterschnittRadioButton.setSelected(true);
    	//a75RadioButtonActionPerformed(null);
    	
		ohneUnterschnittRadioButton.setSelected(false);
		ohneUnterschnittRadioButton.setEnabled(false);
		newUnterschnittwinkel = 75;
		unterschnittwinkelFormattedTextField.setText("75");
		unterschnittwinkelFormattedTextField.setEnabled(true);
		newKeilwinkel = 45;
		keilwinkelFormattedTextField.setText("45");
		keilwinkelFormattedTextField.setEnabled(false);
		refreshSeilrilleField();
		
    	enableButtonGroup(false, keilwinkelButtonGroup);

    	newForm = Constants.FORM_SITZ;
    }//GEN-LAST:event_sitzrilleRadioButtonActionPerformed

    private void keilrilleRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_keilrilleRadioButtonActionPerformed
    	enableButtonGroup(true, keilwinkelButtonGroup);
    	enableButtonGroup(true, unterschnittButtonGroup);
    	keilwinkelFormattedTextField.setText("35");
    	newKeilwinkel = 35;
    	//g35RadioButtonActionPerformed(null);

		ohneUnterschnittRadioButton.setEnabled(true);
    	ohneUnterschnittRadioButton.setSelected(true);
    	newUnterschnittwinkel = 0;
    	unterschnittwinkelFormattedTextField.setText("0");
		unterschnittwinkelFormattedTextField.setEnabled(false);
		keilwinkelFormattedTextField.setEnabled(true);
		refreshSeilrilleField();
    	
    	newForm = Constants.FORM_KEIL;
    }//GEN-LAST:event_keilrilleRadioButtonActionPerformed

    private void durchmesserFormattedTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_durchmesserFormattedTextFieldKeyReleased
    	try {
			durchmesser = (df_nopoint.parse(durchmesserFormattedTextField.getText())).intValue();
		}
		catch (ParseException e) {
			// auto-corrected by FormattedTextField
		}
    }//GEN-LAST:event_durchmesserFormattedTextFieldKeyReleased

    private void seiltriebwirkungsgradFormattedTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_seiltriebwirkungsgradFormattedTextFieldKeyReleased
    	try {
			fs2 = (df_point.parse(seiltriebwirkungsgradFormattedTextField.getText())).doubleValue();
		}
		catch (ParseException e) {
			// auto-corrected by FormattedTextField
		}
    }//GEN-LAST:event_seiltriebwirkungsgradFormattedTextFieldKeyReleased

    private void t4RadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t4RadioButtonActionPerformed
    	fn4 = 0.67;
    	setTextFieldEntries();
    }//GEN-LAST:event_t4RadioButtonActionPerformed

    private void t3RadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t3RadioButtonActionPerformed
    	fn4 = 0.7;
    	setTextFieldEntries();
    }//GEN-LAST:event_t3RadioButtonActionPerformed

    private void t2RadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t2RadioButtonActionPerformed
    	fn4 = 0.75;
    	setTextFieldEntries();
    }//GEN-LAST:event_t2RadioButtonActionPerformed

    private void t1RadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t1RadioButtonActionPerformed
    	fn4 = 0.9;
    	setTextFieldEntries();
    }//GEN-LAST:event_t1RadioButtonActionPerformed

    private void t0RadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t0RadioButtonActionPerformed
    	fn4 = 1;
    	setTextFieldEntries();
    }//GEN-LAST:event_t0RadioButtonActionPerformed

    private void rd100RadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rd100RadioButtonActionPerformed
    	fn3 = 0.48;
    	setRundrille();
    }//GEN-LAST:event_rd100RadioButtonActionPerformed

    private void rd080RadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rd080RadioButtonActionPerformed
    	fn3 = 0.51;
    	setRundrille();
    }//GEN-LAST:event_rd080RadioButtonActionPerformed

    private void rd070RadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rd070RadioButtonActionPerformed
    	fn3 = 0.54;
    	setRundrille();
    }//GEN-LAST:event_rd070RadioButtonActionPerformed

    private void rd060RadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rd060RadioButtonActionPerformed
    	fn3 = 0.66;
    	setRundrille();
    }//GEN-LAST:event_rd060RadioButtonActionPerformed

    private void rd055RadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rd055RadioButtonActionPerformed
    	fn3 = 0.79;
    	setRundrille();
    }//GEN-LAST:event_rd055RadioButtonActionPerformed

    private void rd053RadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rd053RadioButtonActionPerformed
    	fn3 = 1;
    	setRundrille();
    }//GEN-LAST:event_rd053RadioButtonActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
		mainFrame.setEnabled(true);
		dispose();
    }//GEN-LAST:event_formWindowClosing

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
		mainFrame.setEnabled(true);
		dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
    	if ((newUnterschnittwinkel >= 75 && newUnterschnittwinkel <= 105) || (newUnterschnittwinkel == 0 && ohneUnterschnittRadioButton.isSelected())
    			&& (newKeilwinkel >= 35 && newKeilwinkel <= 45)) {
    		yesClicked();
    	}
    	else {
    		WarningFrame warningFrame = new WarningFrame(this, "Werte liegen außerhalb des üblichen Bereichs. Möchten Sie die Änderungen trotzdem übernehmen?");
    		Helper.showFrame(warningFrame, this);
    	}
    }//GEN-LAST:event_okButtonActionPerformed
    
    private void mitUnterschnittRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitUnterschnittRadioButtonActionPerformed
        if (sitzrilleRadioButton.isSelected()) {
            fn3 = 0.4;
            newForm = Constants.FORM_SITZ;
        }
		newUnterschnittwinkel = 75;
		unterschnittwinkelFormattedTextField.setText("75");
        setUnterschnittWinkel();
        setTextFieldEntries();
}//GEN-LAST:event_mitUnterschnittRadioButtonActionPerformed

    private void keilwinkelFormattedTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_keilwinkelFormattedTextFieldKeyReleased
        try {
			newKeilwinkel = df_nopoint.parse(keilwinkelFormattedTextField.getText()).intValue();
			refreshSeilrilleField();
		}
		catch (ParseException e) {
			// handled by formattedtextfield
			//e.printStackTrace();
		}
    }//GEN-LAST:event_keilwinkelFormattedTextFieldKeyReleased

    private void unterschnittwinkelFormattedTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_unterschnittwinkelFormattedTextFieldKeyReleased
        try {
			newUnterschnittwinkel = df_nopoint.parse(unterschnittwinkelFormattedTextField.getText()).intValue();
			refreshSeilrilleField();
		}
		catch (ParseException e) {
			// handled by formattedtextfield
			//e.printStackTrace();
		}
    }//GEN-LAST:event_unterschnittwinkelFormattedTextFieldKeyReleased
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JLabel durchmesserEinheitLabel;
    private javax.swing.JFormattedTextField durchmesserFormattedTextField;
    private javax.swing.JLabel durchmesserLabel;
    private javax.swing.JLabel errorLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JRadioButton keilrilleRadioButton;
    private javax.swing.ButtonGroup keilwinkelButtonGroup;
    private javax.swing.JFormattedTextField keilwinkelFormattedTextField;
    private javax.swing.JRadioButton mitUnterschnittRadioButton;
    private javax.swing.JRadioButton ohneUnterschnittRadioButton;
    private javax.swing.JButton okButton;
    private javax.swing.JRadioButton rd053RadioButton;
    private javax.swing.JRadioButton rd055RadioButton;
    private javax.swing.JRadioButton rd060RadioButton;
    private javax.swing.JRadioButton rd070RadioButton;
    private javax.swing.JRadioButton rd080RadioButton;
    private javax.swing.JRadioButton rd100RadioButton;
    private javax.swing.ButtonGroup schraegzugButtonGroup;
    private javax.swing.JFormattedTextField schraegzugFormattedTextField;
    private javax.swing.ButtonGroup seilrilleButtonGroup;
    private javax.swing.JFormattedTextField seilrilleFormattedTextField;
    private javax.swing.JFormattedTextField seiltriebwirkungsgradFormattedTextField;
    private javax.swing.JRadioButton sitzrilleRadioButton;
    private javax.swing.JRadioButton t0RadioButton;
    private javax.swing.JRadioButton t1RadioButton;
    private javax.swing.JRadioButton t2RadioButton;
    private javax.swing.JRadioButton t3RadioButton;
    private javax.swing.JRadioButton t4RadioButton;
    private javax.swing.JLabel titleLabel;
    private javax.swing.ButtonGroup unterschnittButtonGroup;
    private javax.swing.JFormattedTextField unterschnittwinkelFormattedTextField;
    // End of variables declaration//GEN-END:variables

    /**
     * Sets the unterschnitt winkel.
     */
    private void setUnterschnittWinkel() {
    	if (ohneUnterschnittRadioButton.isSelected()) {
    		newUnterschnittwinkel = 0;
    	}
    	else {
    		try {
				newUnterschnittwinkel = df_nopoint.parse(unterschnittwinkelFormattedTextField.getText()).intValue();
			}
			catch (ParseException e) {
				// handled by formattedtextfield
				//e.printStackTrace();
			}
    	}
    	unterschnittwinkelFormattedTextField.setEnabled(true);
		refreshSeilrilleField();
    }
    
    /**
     * Sets the unterschnitt winkel button.
     */
    private void setUnterschnittWinkelButton() {
    	if (treibscheibe.getUnterschnittwinkel() == 0) {
    		ohneUnterschnittRadioButton.setSelected(true);
    		unterschnittwinkelFormattedTextField.setEnabled(false);
    		newUnterschnittwinkel = 0;
    	}
    	else {
    		mitUnterschnittRadioButton.setSelected(true);
    		newUnterschnittwinkel = treibscheibe.getUnterschnittwinkel();
    		unterschnittwinkelFormattedTextField.setText(df_nopoint.format(newUnterschnittwinkel));
    	}
		refreshSeilrilleField();
    }
    
    /**
     * Ables button group.
     * 
     * @param enable boolean value enable or disable
     * @param group the buttongroup
     */
    private void enableButtonGroup(boolean enable, ButtonGroup group) {
    	group.clearSelection();
    	Enumeration<AbstractButton> enumeration = group.getElements();
    	while (enumeration.hasMoreElements()) {
    		AbstractButton button = enumeration.nextElement();
    		button.setEnabled(enable);
    	}
    }
    
	private void setRundrille() {
		newKeilwinkel = 45;
    	newUnterschnittwinkel = 0;
    	newForm = Constants.FORM_RUND;
    	
    	enableButtonGroup(false, unterschnittButtonGroup);
    	enableButtonGroup(false, keilwinkelButtonGroup);
    	keilwinkelFormattedTextField.setText("45");
    	unterschnittwinkelFormattedTextField.setText("0");
    	keilwinkelFormattedTextField.setEnabled(false);
    	unterschnittwinkelFormattedTextField.setEnabled(false);
    	
    	setTextFieldEntries();
	}

	public void noClicked() {
		setEnabled(true);
		// do change now :-)
	}

	public void yesClicked() {
		setEnabled(true);
		// get changed values
		if (durchmesser > 0
				&& newUnterschnittwinkel >= 0 && newUnterschnittwinkel <= 360
				&& newKeilwinkel >= 0 && newKeilwinkel <= 360) {
			if (!newForm.equals(Constants.FORM_RUND)) {
				fn3 = Utilities.calcFn3(newUnterschnittwinkel, newKeilwinkel, newForm);
			}
		    treibscheibe.setDurchmesser(durchmesser);
		    treibscheibe.setSeilrille(fn3);
		    treibscheibe.setSchraegzug(fn4);
		    treibscheibe.setSeiltriebwirkungsgrad(fs2);
		    treibscheibe.setForm(newForm);
		    treibscheibe.setKeilwinkel(newKeilwinkel);
		    treibscheibe.setUnterschnittwinkel(newUnterschnittwinkel);
		    mainFrame.refreshTabbedPane(treibscheibe);
		    mainFrame.setEnabled(true);
    		mainFrame.parameterChanged();
		    dispose();
		} 
		else if (durchmesser <= 0) {
		    errorLabel.setText("Durchmesser darf nicht negativ sein!");
		}
		else {
			errorLabel.setText("Winkel nur zwischen 0 und 360!");
		}
	}
	
	private void refreshSeilrilleField() {
		fn3 = Utilities.calcFn3(newUnterschnittwinkel, newKeilwinkel, newForm);
		seilrilleFormattedTextField.setText(df_point.format(fn3));
	}
}
