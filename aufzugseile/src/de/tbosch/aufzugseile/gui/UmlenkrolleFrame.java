package de.tbosch.aufzugseile.gui;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;

import de.tbosch.aufzugseile.gui.aufzug.Aufzugschacht;
import de.tbosch.aufzugseile.gui.aufzug.Umlenkrolle;
import de.tbosch.aufzugseile.gui.utils.ReactOnWarningFrame;
import de.tbosch.aufzugseile.gui.utils.Utilities;
import de.tbosch.aufzugseile.gui.utils.WarningFrame;
import de.tbosch.aufzugseile.utils.Constants;
import de.tbosch.aufzugseile.utils.Helper;

// TODO: Auto-generated Javadoc
/**
 * The options frame for the umlenkrolle.
 * 
 * @author Thomas Bosch (tbosch@gmx.de)
 */
public class UmlenkrolleFrame extends javax.swing.JFrame implements ReactOnWarningFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5081191768698884120L;

	/** The umlenkrolle itself. */
	private Umlenkrolle umlenkrolle;

	/** The main frame. */
	private MainFrame mainFrame;
	
	/** The elevator. */
	private Aufzugschacht aufzugschacht;
    
	/** The decimal format with point (double). */
	private DecimalFormat df_point, df_nopoint;
	
	/** values to set here */
	private int durchmesser, horiz, vert;
	private double fs2, fn3, fn4;
	private boolean ohneWaelz;
	
	private String newForm;
	private int newUnterschnittwinkel;
	private int newKeilwinkel;

	/**
	 * Creates new form TreibscheibeFrame.
	 * 
	 * @param umlenkrolle The umlenkrolle itself
	 * @param aufzugschacht the aufzugschacht
	 * @param mainFrame the main frame
	 */
	public UmlenkrolleFrame(Umlenkrolle umlenkrolle, MainFrame mainFrame, Aufzugschacht aufzugschacht, boolean rolle2) {
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
		this.aufzugschacht = aufzugschacht;
		setButtonGroups();
		setValues();
		if (rolle2 || aufzugschacht.getTreibscheibe() == null) {
			vertEntfFormattedTextField.setEnabled(false);
			horizEntfFormattedTextField.setEnabled(false);
		}
    	
		ImageIcon imageIcon = new ImageIcon(Helper.getFileURL(Constants.PROGRAM_ICON));
		setIconImage(imageIcon.getImage());
	}
    
    /**
     * Sets the values.
     */
    private void setValues() {
    	durchmesser = umlenkrolle.getDurchmesser();
    	horiz = umlenkrolle.getHorizEntf();
    	vert = umlenkrolle.getVertEntf();
 
    	ohneWaelz = umlenkrolle.isOhneWaelzlagerung();
    	if (ohneWaelz) {
    		ohneWaelzlagerungCheckBox.setSelected(true);
    	}
    	
    	fs2 = umlenkrolle.getFs2();
		
		boolean rundrille = false;
		boolean keilrille = false;
		boolean sitzrille = false;
		
		newUnterschnittwinkel = 0;
		newKeilwinkel = 45;
		newForm = Constants.FORM_RUND;
		
		double rille = umlenkrolle.getFn3();
		int unterschnittwinkel = umlenkrolle.getUnterschnittwinkel();
		int keilwinkel = umlenkrolle.getKeilwinkel();
		String form = umlenkrolle.getForm();
		
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
		
		double schraeg = umlenkrolle.getFn4();
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
    	if (aufzugschacht.getTreibscheibe() != null) {
    		horizEntfFormattedTextField.setText(df_nopoint.format(horiz));
    		vertEntfFormattedTextField.setText(df_nopoint.format(vert));
    	}
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
        jPanel1 = new javax.swing.JPanel();
        entfHorizLabel = new javax.swing.JLabel();
        entfVertLabel = new javax.swing.JLabel();
        durchmesserLabel = new javax.swing.JLabel();
        vertEntfFormattedTextField = new javax.swing.JFormattedTextField(df_nopoint);
        horizEntfFormattedTextField = new javax.swing.JFormattedTextField(df_nopoint);
        durchmesserFormattedTextField = new javax.swing.JFormattedTextField(df_nopoint);
        entfVertEinheitLabel = new javax.swing.JLabel();
        durchmesserEinheitLabel = new javax.swing.JLabel();
        entfHorizEinheitLabel = new javax.swing.JLabel();
        errorLabel = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        seilrilleFormattedTextField = new javax.swing.JFormattedTextField();
        seiltriebwirkungsgradFormattedTextField = new javax.swing.JFormattedTextField(df_point);
        rd053RadioButton = new javax.swing.JRadioButton();
        rd055RadioButton = new javax.swing.JRadioButton();
        rd060RadioButton = new javax.swing.JRadioButton();
        rd070RadioButton = new javax.swing.JRadioButton();
        rd080RadioButton = new javax.swing.JRadioButton();
        rd100RadioButton = new javax.swing.JRadioButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        sitzrilleRadioButton = new javax.swing.JRadioButton();
        keilrilleRadioButton = new javax.swing.JRadioButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        keilwinkelFormattedTextField = new javax.swing.JFormattedTextField(df_nopoint);
        jLabel9 = new javax.swing.JLabel();
        ohneUnterschnittRadioButton = new javax.swing.JRadioButton();
        mitUnterschnittRadioButton = new javax.swing.JRadioButton();
        jLabel10 = new javax.swing.JLabel();
        unterschnittwinkelFormattedTextField = new javax.swing.JFormattedTextField(df_nopoint);
        jLabel11 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        t0RadioButton = new javax.swing.JRadioButton();
        t1RadioButton = new javax.swing.JRadioButton();
        t2RadioButton = new javax.swing.JRadioButton();
        t3RadioButton = new javax.swing.JRadioButton();
        t4RadioButton = new javax.swing.JRadioButton();
        schraegzugFormattedTextField = new javax.swing.JFormattedTextField();
        jLabel7 = new javax.swing.JLabel();
        ohneWaelzlagerungCheckBox = new javax.swing.JCheckBox();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Umlenkrollenoptionen");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        titleLabel.setText("Umlenkrolle: Optionen");

        entfHorizLabel.setText("Entfernung zur Treibscheibe (rechts):");

        entfVertLabel.setText("Entfernung zur Treibscheibe (unten):");

        durchmesserLabel.setText("Durchmesser:");

        vertEntfFormattedTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                vertEntfFormattedTextFieldKeyReleased(evt);
            }
        });

        horizEntfFormattedTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                horizEntfFormattedTextFieldKeyReleased(evt);
            }
        });

        durchmesserFormattedTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                durchmesserFormattedTextFieldKeyReleased(evt);
            }
        });

        entfVertEinheitLabel.setText("cm");

        durchmesserEinheitLabel.setText("mm");

        entfHorizEinheitLabel.setText("cm");

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(entfHorizLabel)
                    .add(entfVertLabel)
                    .add(durchmesserLabel))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(vertEntfFormattedTextField)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, horizEntfFormattedTextField)
                    .add(durchmesserFormattedTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 57, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(durchmesserEinheitLabel)
                    .add(entfHorizEinheitLabel)
                    .add(entfVertEinheitLabel))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(durchmesserLabel)
                    .add(durchmesserFormattedTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(durchmesserEinheitLabel))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(entfHorizLabel)
                    .add(horizEntfFormattedTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(entfHorizEinheitLabel))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(entfVertLabel)
                    .add(vertEntfFormattedTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(entfVertEinheitLabel)))
        );

        errorLabel.setForeground(new java.awt.Color(255, 0, 0));

        jLabel1.setText("Seilrille (fn3):");

        jLabel6.setText("Seiltriebwirkungsgrad (fs2):");

        seilrilleFormattedTextField.setEditable(false);

        seiltriebwirkungsgradFormattedTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                seiltriebwirkungsgradFormattedTextFieldKeyReleased(evt);
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

        jLabel3.setText("Rundrille");

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

        jLabel5.setText("Unterschnitt- und Keilwinkel");

        jLabel8.setText("Keilwinkel:");

        keilwinkelFormattedTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                keilwinkelFormattedTextFieldKeyReleased(evt);
            }
        });

        jLabel9.setText("Grad");

        ohneUnterschnittRadioButton.setText("ohne Unterschnitt");
        ohneUnterschnittRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        ohneUnterschnittRadioButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        ohneUnterschnittRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ohneUnterschnittRadioButtonActionPerformed(evt);
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

        jLabel10.setText("Unterschnittwinkel:");

        unterschnittwinkelFormattedTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                unterschnittwinkelFormattedTextFieldKeyReleased(evt);
            }
        });

        jLabel11.setText("Grad");

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jLabel1)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jLabel6))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(rd053RadioButton)
                            .add(rd055RadioButton)
                            .add(rd060RadioButton)
                            .add(rd070RadioButton)
                            .add(rd080RadioButton)
                            .add(rd100RadioButton)
                            .add(jLabel3))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 38, Short.MAX_VALUE)
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(ohneUnterschnittRadioButton)
                            .add(mitUnterschnittRadioButton)
                            .add(jPanel2Layout.createSequentialGroup()
                                .add(jLabel10)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(unterschnittwinkelFormattedTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 59, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jLabel11))
                            .add(jLabel5)
                            .add(keilrilleRadioButton)
                            .add(sitzrilleRadioButton)
                            .add(jLabel4)
                            .add(jPanel2Layout.createSequentialGroup()
                                .add(jLabel8)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(keilwinkelFormattedTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 55, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jLabel9)))
                        .add(43, 43, 43))
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, seilrilleFormattedTextField)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, seiltriebwirkungsgradFormattedTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 46, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(387, Short.MAX_VALUE))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel6)
                    .add(seiltriebwirkungsgradFormattedTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1)
                    .add(seilrilleFormattedTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
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
                .addContainerGap(42, Short.MAX_VALUE))
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(62, Short.MAX_VALUE)
                .add(jLabel4)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(sitzrilleRadioButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(keilrilleRadioButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel5)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel8)
                    .add(jLabel9)
                    .add(keilwinkelFormattedTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(ohneUnterschnittRadioButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(mitUnterschnittRadioButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel10)
                    .add(unterschnittwinkelFormattedTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel11))
                .addContainerGap())
        );

        jLabel2.setText("Schrägzug (fn4):");

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

        schraegzugFormattedTextField.setEditable(false);

        jLabel7.setText("Ohne Wälzlagerung:");

        ohneWaelzlagerungCheckBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        ohneWaelzlagerungCheckBox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        ohneWaelzlagerungCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ohneWaelzlagerungCheckBoxActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel3Layout.createSequentialGroup()
                        .add(jLabel2)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                                .add(t0RadioButton)
                                .add(t1RadioButton)
                                .add(t2RadioButton)
                                .add(t3RadioButton)
                                .add(t4RadioButton))
                            .add(schraegzugFormattedTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 44, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                    .add(jPanel3Layout.createSequentialGroup()
                        .add(jLabel7)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(ohneWaelzlagerungCheckBox)))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(schraegzugFormattedTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel2))
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
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel7)
                    .add(ohneWaelzlagerungCheckBox))
                .addContainerGap(27, Short.MAX_VALUE))
        );

        okButton.setText("OK");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        cancelButton.setText("Abbrechen");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap(473, Short.MAX_VALUE)
                .add(errorLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(okButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(cancelButton)
                .addContainerGap())
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(titleLabel)
                .addContainerGap(514, Short.MAX_VALUE))
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(320, Short.MAX_VALUE))
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(29, Short.MAX_VALUE))
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(378, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(titleLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(errorLabel)
                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(okButton)
                        .add(cancelButton)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
	
    	newForm = Constants.FORM_KEIL;
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
	
    	newForm = Constants.FORM_SITZ;
    }//GEN-LAST:event_sitzrilleRadioButtonActionPerformed

    private void ohneWaelzlagerungCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ohneWaelzlagerungCheckBoxActionPerformed
    	if (ohneWaelzlagerungCheckBox.isSelected()) {
    		ohneWaelz = true;
    	}
    	else {
    		ohneWaelz = false;
    	}
    }//GEN-LAST:event_ohneWaelzlagerungCheckBoxActionPerformed

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

    private void vertEntfFormattedTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_vertEntfFormattedTextFieldKeyReleased
    	try {
			vert = (df_nopoint.parse(vertEntfFormattedTextField.getText())).intValue();
		}
		catch (ParseException e) {
			// auto-corrected by FormattedTextField
		}
    }//GEN-LAST:event_vertEntfFormattedTextFieldKeyReleased

    private void horizEntfFormattedTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_horizEntfFormattedTextFieldKeyReleased
    	try {
			horiz = (df_nopoint.parse(horizEntfFormattedTextField.getText())).intValue();
		}
		catch (ParseException e) {
			// auto-corrected by FormattedTextField
		}
    }//GEN-LAST:event_horizEntfFormattedTextFieldKeyReleased

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
    private javax.swing.JLabel entfHorizEinheitLabel;
    private javax.swing.JLabel entfHorizLabel;
    private javax.swing.JLabel entfVertEinheitLabel;
    private javax.swing.JLabel entfVertLabel;
    private javax.swing.JLabel errorLabel;
    private javax.swing.JFormattedTextField horizEntfFormattedTextField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
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
    private javax.swing.JFormattedTextField vertEntfFormattedTextField;
    // End of variables declaration//GEN-END:variables
    
    /**
     * Outside area.
     * 
     * @param horiz the horiz
     * @param vert the vert
     * 
     * @return true, if outside area
     */
    private boolean outsideArea(int horiz, int vert) {
    	if (aufzugschacht.getTreibscheibe() != null) {
    		int nx = aufzugschacht.getTreibscheibe().getMPoint().x + ((horiz) / aufzugschacht.getMmPerPixel()) + umlenkrolle.getRadius();
    		int ny = aufzugschacht.getTreibscheibe().getMPoint().y + ((vert) / aufzugschacht.getMmPerPixel()) + umlenkrolle.getRadius();
    		if (nx > aufzugschacht.getWidth() || ny > aufzugschacht.getHeight() || nx < 1 || ny < 1) {
    			return true;
    		}
    	}
    	return false;
    }
    
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
    	if (umlenkrolle.getUnterschnittwinkel() == 0) {
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
				&& !outsideArea(horiz, vert)
				&& newUnterschnittwinkel >= 0 && newUnterschnittwinkel <= 360
				&& newKeilwinkel >= 0 && newKeilwinkel <= 360)  {
			if (!newForm.equals(Constants.FORM_RUND)) {
				fn3 = Utilities.calcFn3(newUnterschnittwinkel, newKeilwinkel, newForm);
			}
			umlenkrolle.setDurchmesser(durchmesser);
			umlenkrolle.setVertEntf(vert);
			umlenkrolle.setHorizEntf(horiz);
			umlenkrolle.setSeilrille(fn3);
			umlenkrolle.setOhneWaelzlagerung(ohneWaelz);
			umlenkrolle.setUnterschnittwinkel(newUnterschnittwinkel);
			umlenkrolle.setKeilwinkel(newKeilwinkel);
			umlenkrolle.setForm(newForm);
			umlenkrolle.setSchraegzug(fn4);
			umlenkrolle.setSeiltriebwirkungsgrad(fs2);
			umlenkrolle.refreshSize();
			umlenkrolle.refreshPosition();
			mainFrame.refreshTabbedPane(umlenkrolle);
			mainFrame.setEnabled(true);
    		mainFrame.parameterChanged();
			dispose();
		}
		else if (durchmesser <= 0) {
			errorLabel.setText("Durchmesser darf nicht negativ sein!");
		}
		else if (outsideArea(horiz, vert)) {
			errorLabel.setText("Distanzwerte außerhalb der Begrenzung!");
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
