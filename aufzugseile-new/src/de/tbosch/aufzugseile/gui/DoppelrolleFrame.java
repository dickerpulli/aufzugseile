package de.tbosch.aufzugseile.gui;

import de.tbosch.aufzugseile.gui.aufzug.AufzugschachtDoppelUmlenkrolle;
import de.tbosch.commons.Helper;
import de.tbosch.commons.gui.GUIHelper;
import de.tbosch.aufzugseile.gui.utils.Constants;
import de.tbosch.commons.gui.ReactOnWarningFrame;
import de.tbosch.commons.gui.WarningFrame;
import de.tbosch.seile.commons.CommonConstants;
import de.tbosch.seile.commons.elemente.DoppelUmlenkrolle;
import de.tbosch.seile.commons.elemente.Rolle;
import de.tbosch.seile.commons.utils.Utilities;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Enumeration;

import java.util.logging.Logger;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;



/**
 * The options frame for the umlenkrolle.
 * 
 * @author Thomas Bosch (tbosch@gmx.de)
 */
public class DoppelrolleFrame extends javax.swing.JFrame implements ReactOnWarningFrame {
    
    private static final Logger logger = Logger.getLogger(DoppelrolleFrame.class.getName());

	/** The umlenkrolle itself. */
	private DoppelUmlenkrolle umlenkrolle;

	/** The main frame. */
	private MainFrame mainFrame;
    
	/** The decimal format with point (double). */
	private DecimalFormat df_point, df_nopoint;
	
	/** values to set here */
	private int durchmesser;
	private double fs2, fn3, fn4;
	private boolean ohneWaelz;
	
	private String newForm;
	private int newUnterschnittwinkel;
	private int newKeilwinkel;

	/**
	 * Creates new form TreibscheibeFrame.
	 * 
	 * @param umlenkrolle The umlenkrolle itself
	 * @param mainFrame the main frame
	 */
	public DoppelrolleFrame(DoppelUmlenkrolle umlenkrolle, MainFrame mainFrame) {
		df_point = new DecimalFormat();
		df_point.setMaximumFractionDigits(2);
		df_point.setMinimumFractionDigits(0);
		df_point.setGroupingUsed(false);
		df_nopoint = new DecimalFormat();
		df_nopoint.setMaximumFractionDigits(0);
		df_nopoint.setGroupingUsed(false);
		initComponents();
		this.umlenkrolle = umlenkrolle;
		this.mainFrame = mainFrame;
		setButtonGroups();
		setValues();
    	
		ImageIcon imageIcon = new ImageIcon(Helper.getFileURL(Constants.PROGRAM_ICON));
		setIconImage(imageIcon.getImage());
	}
	
    /**
     * Sets the values.
     */
    private void setValues() {
    	durchmesser = umlenkrolle.getRolle1().getDurchmesser();
 
    	ohneWaelz = umlenkrolle.getRolle1().isOhneWaelzlagerung();
    	if (ohneWaelz) {
    		ohneWaelzlagerungCheckBox.setSelected(true);
    	}
    	
    	fs2 = umlenkrolle.getRolle1().getFs2();
		
		boolean rundrille = false;
		boolean keilrille = false;
		boolean sitzrille = false;
		
		newUnterschnittwinkel = 0;
		newKeilwinkel = 45;
		newForm = Rolle.FORM_RUND;
		
		double rille = umlenkrolle.getRolle1().getFn3();
		int unterschnittwinkel = umlenkrolle.getRolle1().getUnterschnittwinkel();
		int keilwinkel = umlenkrolle.getRolle1().getKeilwinkel();
		String form = umlenkrolle.getRolle1().getForm();
		
		if (form.equals(Rolle.FORM_RUND)) {
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
		else if (form.equals(Rolle.FORM_SITZ)) {
	    	sitzrille = true;
	    	unterschnittwinkelFormattedTextField.setText(df_nopoint.format(unterschnittwinkel));
	    	newUnterschnittwinkel = unterschnittwinkel;
		}
		else if (form.equals(Rolle.FORM_KEIL)) {
    		keilrille = true;
    		keilwinkelFormattedTextField.setText(df_nopoint.format(keilwinkel));
    		newKeilwinkel = keilwinkel;
		}
    	fn3 = rille;
    	if (rundrille) {
    		enableButtonGroup(false, keilwinkelButtonGroup);
    		enableButtonGroup(false, unterschnittButtonGroup);
    		newForm = Rolle.FORM_RUND;
    	}
    	else if (keilrille) {
    		keilrilleRadioButton.setSelected(true);
    		newForm = Rolle.FORM_KEIL;
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
    		newForm = Rolle.FORM_SITZ;
    	}
    	else {
    		logger.severe("Should not be - no rille");
    	}
		
		double schraeg = umlenkrolle.getRolle1().getFn4();
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
        titleLabel = new javax.swing.JLabel();
        durchmesserLabel = new javax.swing.JLabel();
        durchmesserEinheitLabel = new javax.swing.JLabel();
        errorLabel = new javax.swing.JLabel();
        cancelButton = new javax.swing.JButton();
        okButton = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        rd055RadioButton = new javax.swing.JRadioButton();
        rd060RadioButton = new javax.swing.JRadioButton();
        rd070RadioButton = new javax.swing.JRadioButton();
        rd080RadioButton = new javax.swing.JRadioButton();
        rd100RadioButton = new javax.swing.JRadioButton();
        rd053RadioButton = new javax.swing.JRadioButton();
        mitUnterschnittRadioButton = new javax.swing.JRadioButton();
        jLabel5 = new javax.swing.JLabel();
        schraegzugFormattedTextField = new javax.swing.JFormattedTextField();
        t0RadioButton = new javax.swing.JRadioButton();
        t1RadioButton = new javax.swing.JRadioButton();
        t2RadioButton = new javax.swing.JRadioButton();
        t3RadioButton = new javax.swing.JRadioButton();
        t4RadioButton = new javax.swing.JRadioButton();
        seiltriebwirkungsgradFormattedTextField = new javax.swing.JFormattedTextField(df_point);
        seilrilleFormattedTextField = new javax.swing.JFormattedTextField();
        durchmesserFormattedTextField = new javax.swing.JFormattedTextField(df_nopoint);
        jLabel4 = new javax.swing.JLabel();
        sitzrilleRadioButton = new javax.swing.JRadioButton();
        keilrilleRadioButton = new javax.swing.JRadioButton();
        jLabel7 = new javax.swing.JLabel();
        ohneUnterschnittRadioButton = new javax.swing.JRadioButton();
        jLabel8 = new javax.swing.JLabel();
        ohneWaelzlagerungCheckBox = new javax.swing.JCheckBox();
        jLabel9 = new javax.swing.JLabel();
        keilwinkelFormattedTextField = new javax.swing.JFormattedTextField(df_nopoint);
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        unterschnittwinkelFormattedTextField = new javax.swing.JFormattedTextField(df_nopoint);
        jLabel12 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Umlenkrollenoptionen");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        titleLabel.setText("Doppelte Umlenkrolle: Optionen - Parameter gelten für beide Rollen");

        durchmesserLabel.setText("Durchmesser:");

        durchmesserEinheitLabel.setText("mm");

        errorLabel.setForeground(new java.awt.Color(255, 0, 0));

        cancelButton.setText("Cancel");
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

        jLabel2.setText("Schrägzug (fn4):");

        jLabel6.setText("Seiltriebwirkungsgrad (fs2):");

        jLabel1.setText("Seilrille (fn3):");

        jLabel3.setText("Rundrille");

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

        rd053RadioButton.setText("Rillenradius r/d = 0,53");
        rd053RadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rd053RadioButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rd053RadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rd053RadioButtonActionPerformed(evt);
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

        jLabel5.setText("Keilrille");

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

        seiltriebwirkungsgradFormattedTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                seiltriebwirkungsgradFormattedTextFieldKeyReleased(evt);
            }
        });

        seilrilleFormattedTextField.setEditable(false);

        durchmesserFormattedTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                durchmesserFormattedTextFieldKeyReleased(evt);
            }
        });

        jLabel4.setText("Sitzrille/Keilrille mit/ohne Unterschnitt");

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

        jLabel7.setText("Unterschnitt- und Keilwinkel");

        ohneUnterschnittRadioButton.setText("ohne Unterschnitt");
        ohneUnterschnittRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        ohneUnterschnittRadioButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        ohneUnterschnittRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ohneUnterschnittRadioButtonActionPerformed(evt);
            }
        });

        jLabel8.setText("Ohne Wälzlagerung:");

        ohneWaelzlagerungCheckBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        ohneWaelzlagerungCheckBox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        ohneWaelzlagerungCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ohneWaelzlagerungCheckBoxActionPerformed(evt);
            }
        });

        jLabel9.setText("Keilwinkel:");

        keilwinkelFormattedTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                keilwinkelFormattedTextFieldKeyReleased(evt);
            }
        });

        jLabel10.setText("Grad");

        jLabel11.setText("Unterschnittwinkel:");

        unterschnittwinkelFormattedTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                unterschnittwinkelFormattedTextFieldKeyReleased(evt);
            }
        });

        jLabel12.setText("Grad");

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .addContainerGap()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(titleLabel)
                            .add(layout.createSequentialGroup()
                                .add(17, 17, 17)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                                    .add(jLabel1)
                                    .add(durchmesserLabel)
                                    .add(jLabel6))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(layout.createSequentialGroup()
                                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                                            .add(seilrilleFormattedTextField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                                            .add(seiltriebwirkungsgradFormattedTextField)
                                            .add(durchmesserFormattedTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 53, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(durchmesserEinheitLabel))
                                    .add(layout.createSequentialGroup()
                                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                            .add(rd053RadioButton)
                                            .add(rd055RadioButton)
                                            .add(rd060RadioButton)
                                            .add(rd070RadioButton)
                                            .add(rd080RadioButton)
                                            .add(rd100RadioButton)
                                            .add(jLabel3))
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 28, Short.MAX_VALUE)
                                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                            .add(layout.createSequentialGroup()
                                                .add(jLabel11)
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                .add(unterschnittwinkelFormattedTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 59, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                .add(jLabel12))
                                            .add(layout.createSequentialGroup()
                                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                                    .add(mitUnterschnittRadioButton)
                                                    .add(jLabel4)
                                                    .add(sitzrilleRadioButton)
                                                    .add(keilrilleRadioButton)
                                                    .add(jLabel7)
                                                    .add(ohneUnterschnittRadioButton)
                                                    .add(layout.createSequentialGroup()
                                                        .add(jLabel9)
                                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                        .add(keilwinkelFormattedTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 56, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                        .add(jLabel10)))
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                .add(jLabel5)))
                                        .add(40, 40, 40))))))
                    .add(layout.createSequentialGroup()
                        .add(70, 70, 70)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(jLabel2)
                            .add(jLabel8))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(schraegzugFormattedTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 44, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                                .add(t0RadioButton)
                                .add(t1RadioButton)
                                .add(t2RadioButton)
                                .add(t3RadioButton)
                                .add(t4RadioButton)
                                .add(org.jdesktop.layout.GroupLayout.LEADING, ohneWaelzlagerungCheckBox))))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .add(errorLabel)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(okButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(cancelButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(titleLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(durchmesserLabel)
                    .add(durchmesserFormattedTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(durchmesserEinheitLabel))
                .add(6, 6, 6)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel6)
                    .add(seiltriebwirkungsgradFormattedTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1)
                    .add(seilrilleFormattedTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
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
                        .add(rd100RadioButton))
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabel5)
                            .add(jLabel4))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(sitzrilleRadioButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(keilrilleRadioButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jLabel7)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabel9)
                            .add(keilwinkelFormattedTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jLabel10))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(ohneUnterschnittRadioButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(mitUnterschnittRadioButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabel11)
                            .add(unterschnittwinkelFormattedTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jLabel12))))
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                        .add(4, 4, 4)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(layout.createSequentialGroup()
                                .add(jLabel2)
                                .add(116, 116, 116)
                                .add(jLabel8))
                            .add(layout.createSequentialGroup()
                                .add(schraegzugFormattedTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
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
                                .add(15, 15, 15)
                                .add(ohneWaelzlagerungCheckBox)))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 40, Short.MAX_VALUE)
                        .add(errorLabel))
                    .add(layout.createSequentialGroup()
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(cancelButton)
                            .add(okButton))))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ohneWaelzlagerungCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ohneWaelzlagerungCheckBoxActionPerformed
    	if (ohneWaelzlagerungCheckBox.isSelected()) {
    		ohneWaelz = true;
    	} else {
    		ohneWaelz = false;
    	}
    }//GEN-LAST:event_ohneWaelzlagerungCheckBoxActionPerformed

    private void ohneUnterschnittRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ohneUnterschnittRadioButtonActionPerformed
    	newUnterschnittwinkel = 0;
    	unterschnittwinkelFormattedTextField.setText("0");
    	unterschnittwinkelFormattedTextField.setEnabled(false);
		refreshSeilrilleField();
    }//GEN-LAST:event_ohneUnterschnittRadioButtonActionPerformed

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
	
		newForm = Rolle.FORM_KEIL;
    }//GEN-LAST:event_keilrilleRadioButtonActionPerformed

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
	
    	newForm = Rolle.FORM_SITZ;
    }//GEN-LAST:event_sitzrilleRadioButtonActionPerformed

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

    private void mitUnterschnittRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitUnterschnittRadioButtonActionPerformed
    	if (sitzrilleRadioButton.isSelected()) {
    		fn3 = 0.4;
    		newForm = Rolle.FORM_SITZ;
    	}
		newUnterschnittwinkel = 75;
		unterschnittwinkelFormattedTextField.setText("75");
    	setUnterschnittWinkel();
    	setTextFieldEntries();
}//GEN-LAST:event_mitUnterschnittRadioButtonActionPerformed

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

    private void seiltriebwirkungsgradFormattedTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_seiltriebwirkungsgradFormattedTextFieldKeyReleased
    	try {
			fs2 = (df_point.parse(seiltriebwirkungsgradFormattedTextField.getText())).doubleValue();
		}
		catch (ParseException e) {
			// auto-corrected by FormattedTextField
		}
    }//GEN-LAST:event_seiltriebwirkungsgradFormattedTextFieldKeyReleased

    private void durchmesserFormattedTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_durchmesserFormattedTextFieldKeyReleased
    	try {
			durchmesser = (df_nopoint.parse(durchmesserFormattedTextField.getText())).intValue();
		}
		catch (ParseException e) {
			// auto-corrected by FormattedTextField
		}
    }//GEN-LAST:event_durchmesserFormattedTextFieldKeyReleased

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
		mainFrame.setEnabled(true);
		dispose();
    }//GEN-LAST:event_formWindowClosing

    /**
     * Cancel button action performed.
     * 
     * @param evt the evt
     */
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
    	if ((newUnterschnittwinkel >= 75 && newUnterschnittwinkel <= 105) || (newUnterschnittwinkel == 0 && ohneUnterschnittRadioButton.isSelected())
    			&& (newKeilwinkel >= 35 && newKeilwinkel <= 45)) {
    		yesClicked();
    	}
    	else {
    		WarningFrame warningFrame = new WarningFrame(this, "Werte liegen außerhalb des üblichen Bereichs. Möchten Sie die Änderungen trotzdem übernehmen?");
    		GUIHelper.showFrame(warningFrame, this);
    	}
    }//GEN-LAST:event_okButtonActionPerformed

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
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
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
    private javax.swing.JCheckBox ohneWaelzlagerungCheckBox;
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
    	if (umlenkrolle.getRolle1().getUnterschnittwinkel() == 0) {
    		ohneUnterschnittRadioButton.setSelected(true);
    		unterschnittwinkelFormattedTextField.setEnabled(false);
    		newUnterschnittwinkel = 0;
    	}
    	else {
    		mitUnterschnittRadioButton.setSelected(true);
    		newUnterschnittwinkel = umlenkrolle.getUnterschnittwinkel();
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
    	newForm = Rolle.FORM_RUND;
    	
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
				&& newKeilwinkel >= 0 && newKeilwinkel <= 360)  {
			if (!newForm.equals(Rolle.FORM_RUND)) {
				fn3 = Utilities.calcFn3(newUnterschnittwinkel, newKeilwinkel, newForm);
			}
			umlenkrolle.getRolle1().setDurchmesser(durchmesser);
			umlenkrolle.getRolle1().setSeilrille(fn3);
			umlenkrolle.getRolle1().setSchraegzug(fn4);
			umlenkrolle.getRolle1().setSeiltriebwirkungsgrad(fs2);
			umlenkrolle.getRolle1().setOhneWaelzlagerung(ohneWaelz);
			umlenkrolle.getRolle1().setUnterschnittwinkel(newUnterschnittwinkel);
			umlenkrolle.getRolle1().setKeilwinkel(newKeilwinkel);
			umlenkrolle.getRolle1().setForm(newForm);
			mainFrame.refreshTabbedPane(umlenkrolle.getRolle1());
			umlenkrolle.getRolle2().setDurchmesser(durchmesser);
			umlenkrolle.getRolle2().setSeilrille(fn3);
			umlenkrolle.getRolle2().setSchraegzug(fn4);
			umlenkrolle.getRolle2().setSeiltriebwirkungsgrad(fs2);
			umlenkrolle.getRolle2().setOhneWaelzlagerung(ohneWaelz);
			umlenkrolle.getRolle2().setUnterschnittwinkel(newUnterschnittwinkel);
			umlenkrolle.getRolle2().setKeilwinkel(newKeilwinkel);
			umlenkrolle.getRolle2().setForm(newForm);
			mainFrame.refreshTabbedPane(umlenkrolle.getRolle2());
			((AufzugschachtDoppelUmlenkrolle)umlenkrolle).refreshSize();
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
