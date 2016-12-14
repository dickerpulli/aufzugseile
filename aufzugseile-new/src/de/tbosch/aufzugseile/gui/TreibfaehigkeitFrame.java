package de.tbosch.aufzugseile.gui;

import java.awt.Color;
import java.awt.Frame;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.text.DecimalFormat;
import java.text.ParseException;

import java.util.logging.Level;
import javax.swing.ImageIcon;

import de.tbosch.aufzugseile.gui.aufzug.Gewicht;
import de.tbosch.aufzugseile.gui.aufzug.Kabine;
import de.tbosch.commons.gui.ErrorFrame;
import de.tbosch.aufzugseile.gui.utils.printing.PrintDialogTreib;
import de.tbosch.aufzugseile.gui.utils.printing.PrintFrameTreib;
import de.tbosch.commons.Helper;
import de.tbosch.commons.SystemConstants;
import de.tbosch.commons.gui.GUIHelper;
import de.tbosch.aufzugseile.gui.utils.Constants;
import de.tbosch.commons.gui.Reactor;
import de.tbosch.seile.berechnung.treibfaehigkeit.Treibfaehigkeit;
import de.tbosch.seile.commons.CommonConstants;
import de.tbosch.seile.commons.elemente.Treibscheibe;
import java.awt.Component;
import java.util.logging.Logger;

/**
 *
 * @author Thomas Bosch (tbosch@gmx.de)
 */
public class TreibfaehigkeitFrame extends javax.swing.JFrame implements Reactor {
	
	/** Logger */
	private static final Logger logger = Logger.getLogger(TreibfaehigkeitFrame.class.getName());

	/** The Treibfaehigkeit calculation. */
	private Treibfaehigkeit trf;
	
	private DecimalFormat df_point;
	
	private MainFrame mainFrame;
	
	private boolean pressIO, TSphiIO, sonstIO, calced;

	/** Creates new form TreibfaehigkeitFrame */
    public TreibfaehigkeitFrame(MainFrame mainFrame, Treibscheibe treibscheibe, Kabine kabine, Gewicht gewicht, int hoehe, double geschw, int seilcount, double seildurchmesser) {
        try {
            df_point = new DecimalFormat();
            df_point.setMaximumFractionDigits(2);
            df_point.setMinimumFractionDigits(0);
            df_point.setGroupingUsed(false);
            initComponents();
            trf = new Treibfaehigkeit(df_point.parse(mainFrame.getUmschlWinkelFormattedTextField().getText()).doubleValue(), 
                    mainFrame.getSeilmasse(), 
                    mainFrame.getAufzugschacht().getAufhaengung(), 
                    mainFrame.getAufzugschacht().getSeilrollenOhneWaelzlagerungCount());
            if (trf.setTreibscheibe(treibscheibe) == false) {
                logger.warning("setTreibscheibe failed!");
            }
            trf.setGewichte(kabine, hoehe, seilcount, seildurchmesser);
            trf.setPhi(geschw);

            typButtonGroup.add(sonstigerRadioButton);
            typButtonGroup.add(fassadeRadioButton);
            typButtonGroup.add(kleingueterRadioButton);

            setValues();
            this.mainFrame = mainFrame;
            //System.out.println(seildurchmesser);
            ImageIcon imageIcon = new ImageIcon(Helper.getFileURL(Constants.PROGRAM_ICON));
            setIconImage(imageIcon.getImage());
        } catch (ParseException ex) {
            Logger.getLogger(TreibfaehigkeitFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Sets the changed treibscheibe.
     * 
     * @param treibscheibe the new treibscheibe
     */
    public void setNewTreibscheibe(Treibscheibe treibscheibe) {
    	if (trf.setTreibscheibe(treibscheibe) == false) { 
			logger.warning("setTreibscheibe failed!");
		}
    	else {
            double degrees = Math.toDegrees(trf.getBeta());
        	umschlWinkelFormattedTextField.setText(df_point.format(degrees));
        	calcButtonActionPerformed(null);
    	}
    }
    
    /**
     * Check if the gegengewichtsparameter is 0.5 like in TRA
     * otherwise a warning will pop-up.
     */
    public void checkGegengewichtsparameter(Gewicht gewicht) {
    	if (gewicht.getPart() != 0.5) {
    		ErrorFrame errorFrame = new ErrorFrame(this, "Der Gegegenbewichtsparameter ist ungleich '0,5', d.h. nicht mit TRA kompatibel! Ggf. "+SystemConstants.AE_K+"ndern, da sonst Treibf"+SystemConstants.AE_K+"hihkeitsberechnung und Aufzugkonstruktion nicht "+SystemConstants.UE_K+"bereinstimmen.");
    		GUIHelper.showFrame(errorFrame, this);
    	}
    }
    
    /**
     * Sets the values of the text-fields.
     */
    private void setValues() {
    	seilmasseFormattedTextField.setText(df_point.format(trf.getSeilmasse()));
    	kabinekraftFormattedTextField.setText(df_point.format(trf.getF()));
    	gewichtkraftFormattedTextField.setText(df_point.format(trf.getG()));
    	hkkraftFormattedTextField.setText(df_point.format(trf.getHk()));
    	tskraftFormattedTextField.setText(df_point.format(trf.getS()));
    	hubhoeheFormattedTextField.setText(df_point.format(trf.getHubhoehe()));
    	umschlWinkelFormattedTextField.setText(df_point.format(Math.toDegrees(trf.getBeta())));
    	seilanzahlTextField.setText(Integer.toString(trf.getSeilcount()));
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        typButtonGroup = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        seilmasseFormattedTextField = new javax.swing.JFormattedTextField(df_point);
        kabinekraftFormattedTextField = new javax.swing.JFormattedTextField();
        gewichtkraftFormattedTextField = new javax.swing.JFormattedTextField();
        hkkraftFormattedTextField = new javax.swing.JFormattedTextField();
        tskraftFormattedTextField = new javax.swing.JFormattedTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        hubhoeheFormattedTextField = new javax.swing.JFormattedTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        t2t1FormattedTextField = new javax.swing.JFormattedTextField();
        s2s1FormattedTextField = new javax.swing.JFormattedTextField();
        phiFormattedTextField = new javax.swing.JFormattedTextField();
        cancelButton = new javax.swing.JButton();
        calcButton = new javax.swing.JButton();
        ergPanel = new javax.swing.JPanel();
        ergLabel = new javax.swing.JLabel();
        kleingueterRadioButton = new javax.swing.JRadioButton();
        fassadeRadioButton = new javax.swing.JRadioButton();
        sonstigerRadioButton = new javax.swing.JRadioButton();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        pressungFormattedTextField = new javax.swing.JFormattedTextField();
        jLabel20 = new javax.swing.JLabel();
        pressErgPanel = new javax.swing.JPanel();
        pressErgLabel = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        bemerkungenTextArea = new javax.swing.JTextArea();
        jLabel22 = new javax.swing.JLabel();
        umschlWinkelFormattedTextField = new javax.swing.JFormattedTextField(df_point);
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        sonstErgPanel = new javax.swing.JPanel();
        sonstErgLabel = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        druckButton = new javax.swing.JButton();
        seilanzahlTextField = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Treibfähigkeit");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jLabel1.setText("Berechnung der Treibfähigkeit nach TRA003");

        jLabel2.setText("Tragseilgewichtskraft:");

        jLabel3.setText("Kabinengewichtskraft:");

        jLabel4.setText("Gegengewichtskraft:");

        jLabel5.setText("Hängekabelgewichtskraft:");

        jLabel6.setText("Seilmasse:");

        seilmasseFormattedTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                seilmasseFormattedTextFieldKeyReleased(evt);
            }
        });

        kabinekraftFormattedTextField.setEditable(false);

        gewichtkraftFormattedTextField.setEditable(false);

        hkkraftFormattedTextField.setEditable(false);

        tskraftFormattedTextField.setEditable(false);

        jLabel7.setText("N");

        jLabel8.setText("N");

        jLabel9.setText("N");

        jLabel10.setText("N");

        jLabel11.setText("kg/100m");

        jLabel12.setText("Hubhöhe:");

        hubhoeheFormattedTextField.setEditable(false);

        jLabel13.setText("m");

        jLabel14.setText("T2 / T1:");

        jLabel15.setText("S2 / S1:");

        jLabel16.setText("phi:");

        jLabel17.setText("S2 / S1 * phi <= T2 / T1 ?");

        t2t1FormattedTextField.setEditable(false);

        s2s1FormattedTextField.setEditable(false);

        phiFormattedTextField.setEditable(false);

        cancelButton.setText("Abbrechen");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        calcButton.setText("Berechnen");
        calcButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                calcButtonActionPerformed(evt);
            }
        });

        ergPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        ergPanel.setLayout(new java.awt.GridLayout(1, 0));

        ergLabel.setText("JA/NEIN");
        ergPanel.add(ergLabel);

        kleingueterRadioButton.setText("Kleingüteraufzug");
        kleingueterRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        kleingueterRadioButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        kleingueterRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kleingueterRadioButtonActionPerformed(evt);
            }
        });

        fassadeRadioButton.setText("Fassadenaufzug");
        fassadeRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        fassadeRadioButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        fassadeRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fassadeRadioButtonActionPerformed(evt);
            }
        });

        sonstigerRadioButton.setSelected(true);
        sonstigerRadioButton.setText("sonstiger Aufzug");
        sonstigerRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        sonstigerRadioButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        sonstigerRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sonstigerRadioButtonActionPerformed(evt);
            }
        });

        jLabel18.setText("Aufzugtyp");

        jLabel19.setText("spez. Pressung:");

        pressungFormattedTextField.setEditable(false);

        jLabel20.setText("Pressung i.O.?");

        pressErgPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        pressErgPanel.setLayout(new java.awt.GridLayout(1, 0));

        pressErgLabel.setText("JA/NEIN");
        pressErgPanel.add(pressErgLabel);

        jLabel21.setText("N / qcm");

        bemerkungenTextArea.setColumns(20);
        bemerkungenTextArea.setRows(5);
        jScrollPane1.setViewportView(bemerkungenTextArea);

        jLabel22.setText("Umschlingungswinkel:");

        umschlWinkelFormattedTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                umschlWinkelFormattedTextFieldKeyReleased(evt);
            }
        });

        jLabel23.setText("Grad");

        jLabel24.setText("sonstige Bedingungen i.O.?");

        sonstErgPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        sonstErgPanel.setLayout(new java.awt.GridLayout(1, 0));

        sonstErgLabel.setText("JA/NEIN");
        sonstErgPanel.add(sonstErgLabel);

        jLabel25.setText("Bemerkungen:");

        druckButton.setText("Drucken");
        druckButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                druckButtonActionPerformed(evt);
            }
        });

        seilanzahlTextField.setEditable(false);

        jLabel26.setText("Seilanzahl:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel14)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(jLabel2)
                            .addComponent(jLabel6)
                            .addComponent(jLabel12)
                            .addComponent(jLabel15)
                            .addComponent(jLabel18)
                            .addComponent(jLabel16)
                            .addComponent(jLabel19)
                            .addComponent(jLabel22)
                            .addComponent(jLabel26))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(fassadeRadioButton)
                            .addComponent(kleingueterRadioButton)
                            .addComponent(sonstigerRadioButton)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(umschlWinkelFormattedTextField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE)
                                    .addComponent(gewichtkraftFormattedTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE)
                                    .addComponent(hubhoeheFormattedTextField, javax.swing.GroupLayout.Alignment.LEADING, 0, 0, Short.MAX_VALUE)
                                    .addComponent(hkkraftFormattedTextField, javax.swing.GroupLayout.Alignment.LEADING, 0, 0, Short.MAX_VALUE)
                                    .addComponent(seilmasseFormattedTextField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE)
                                    .addComponent(kabinekraftFormattedTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE)
                                    .addComponent(tskraftFormattedTextField, javax.swing.GroupLayout.Alignment.LEADING, 0, 0, Short.MAX_VALUE)
                                    .addComponent(seilanzahlTextField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel23)
                                    .addComponent(jLabel13)
                                    .addComponent(jLabel11)
                                    .addComponent(jLabel10)
                                    .addComponent(jLabel9)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel7)))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(phiFormattedTextField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                                    .addComponent(s2s1FormattedTextField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                                    .addComponent(t2t1FormattedTextField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                                    .addComponent(pressungFormattedTextField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel21))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(druckButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(calcButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelButton))
                    .addComponent(jLabel1)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel24)
                            .addComponent(jLabel20)
                            .addComponent(jLabel17))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(ergPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(sonstErgPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pressErgPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 112, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 351, Short.MAX_VALUE)
                    .addComponent(jLabel25))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(seilmasseFormattedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(seilanzahlTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel26))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel7)
                    .addComponent(kabinekraftFormattedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel8)
                    .addComponent(gewichtkraftFormattedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(hkkraftFormattedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel10)
                    .addComponent(tskraftFormattedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel12)
                            .addComponent(hubhoeheFormattedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel22)
                            .addComponent(umschlWinkelFormattedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel23))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(kleingueterRadioButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fassadeRadioButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sonstigerRadioButton)))
                .addGap(21, 21, 21)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(t2t1FormattedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(s2s1FormattedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(phiFormattedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(jLabel21)
                    .addComponent(pressungFormattedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel17)
                    .addComponent(ergPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel20)
                    .addComponent(pressErgPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel24)
                    .addComponent(sonstErgPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel25)
                .addGap(2, 2, 2)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelButton)
                    .addComponent(calcButton)
                    .addComponent(druckButton))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void sonstigerRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sonstigerRadioButtonActionPerformed
    	if (sonstigerRadioButton.isSelected()) {
    		trf.refreshTyp(CommonConstants.TYP_SONSTIGER);
			calcButtonActionPerformed(null);
    	}
    }//GEN-LAST:event_sonstigerRadioButtonActionPerformed

    private void fassadeRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fassadeRadioButtonActionPerformed
    	if (fassadeRadioButton.isSelected()) {
    		trf.refreshTyp(CommonConstants.TYP_FASSADE);
			calcButtonActionPerformed(null);
    	}
    }//GEN-LAST:event_fassadeRadioButtonActionPerformed

    private void kleingueterRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kleingueterRadioButtonActionPerformed
    	if (kleingueterRadioButton.isSelected()) {
    		trf.refreshTyp(CommonConstants.TYP_KLEINLAST);
			calcButtonActionPerformed(null);
    	}
    }//GEN-LAST:event_kleingueterRadioButtonActionPerformed

    private void seilmasseFormattedTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_seilmasseFormattedTextFieldKeyReleased
    	try {
			double masse = df_point.parse(seilmasseFormattedTextField.getText()).doubleValue();
			trf.setSeilmasse(masse);
			trf.refreshSeilmasse();
			setValues();
			calcButtonActionPerformed(null);
		}
		catch (ParseException e) {
			// auto-corrected by FormattedTextField
		}
    }//GEN-LAST:event_seilmasseFormattedTextFieldKeyReleased

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
    	cancelButtonActionPerformed(null);
    }//GEN-LAST:event_formWindowClosing

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
    	mainFrame.setEnabled(true);
    	dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void calcButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_calcButtonActionPerformed
    	double t2t1 = trf.treibfaehigkeitTreibscheibe()[0];		
    	double s2s1 = trf.treibfaehigkeitTreibscheibe()[1];		
    	double phi = trf.treibfaehigkeitTreibscheibe()[2];		
    	double isBCorrect = trf.treibfaehigkeitTreibscheibe()[3];	
    	double isGammaCorrect = trf.treibfaehigkeitTreibscheibe()[4];	
    	double fassadeCorrect = trf.treibfaehigkeitTreibscheibe()[5];
    	
    	double pressure = trf.pressure(false);
    	
    	t2t1FormattedTextField.setText(df_point.format(t2t1));
    	s2s1FormattedTextField.setText(df_point.format(s2s1));
    	phiFormattedTextField.setText(df_point.format(phi));
    	pressungFormattedTextField.setText(df_point.format(pressure));
    	bemerkungenTextArea.setText("");
    	
		if (t2t1 >= s2s1 * phi) {
			ergLabel.setText("Erf"+SystemConstants.UE_K+"llt");
			ergPanel.setBackground(Color.GREEN);
			TSphiIO = true;
		}
		else {
			ergLabel.setText("Nicht erf"+SystemConstants.UE_K+"llt");
			ergPanel.setBackground(Color.RED);
			TSphiIO = false;
		}
		
		if (isBCorrect == 1 && isGammaCorrect == 1 && fassadeCorrect == 1) {
			sonstErgLabel.setText("Ja");
			sonstErgPanel.setBackground(Color.GREEN);
			sonstIO = true;
		}
		else {
			sonstErgLabel.setText("Nein");
			sonstErgPanel.setBackground(Color.RED);
			sonstIO = false;
		}
		
		if (isBCorrect == 0) {
			bemerkungenTextArea.setText(bemerkungenTextArea.getText()+"Unterschnittbreite zu gering\n");
		}
		if (isGammaCorrect == 0) {
			bemerkungenTextArea.setText(bemerkungenTextArea.getText()+"Keilwinkel zu klein\n");
		}
		if (fassadeCorrect == 0) {
			bemerkungenTextArea.setText(bemerkungenTextArea.getText()+"Fassadenaufzug mit 1,5-facher Nutzlast\n");
		}
		
		if (trf.isTreibscheibeKeil() && !fassadeRadioButton.isSelected() && pressure <= 200) {
			double press2 = trf.pressure(true);
	    	pressungFormattedTextField.setText(df_point.format(pressure)+" / "+df_point.format(press2));
			if (press2 <= 900) {
				pressErgLabel.setText("Ja");
				pressErgPanel.setBackground(Color.GREEN);
				pressIO = true;
			}
			else {
				pressErgLabel.setText("Nein");
				bemerkungenTextArea.setText(bemerkungenTextArea.getText()+"Unterschnittene Pressung > 900 N / qcm\n");
				pressErgPanel.setBackground(Color.RED);
				pressIO = false;
			}
		}
		else if (trf.isTreibscheibeKeil() && !fassadeRadioButton.isSelected()) {
			pressErgLabel.setText("Nein");
			bemerkungenTextArea.setText(bemerkungenTextArea.getText()+"Pressung > 200 N / qcm\n");
			pressErgPanel.setBackground(Color.RED);
			pressIO = false;
			double press2 = trf.pressure(true);
	    	pressungFormattedTextField.setText(df_point.format(pressure)+" / "+df_point.format(press2));
			if (press2 > 900) {
				bemerkungenTextArea.setText(bemerkungenTextArea.getText()+"Unterschnittene Pressung > 900 N / qcm\n");
			}
		}
		else if (trf.isTreibscheibeKeil() && fassadeRadioButton.isSelected() && pressure <= 600) {
			double press2 = trf.pressure(true);
	    	pressungFormattedTextField.setText(df_point.format(pressure)+" | "+df_point.format(press2));
			if (press2 <= 900) {
				pressErgLabel.setText("Ja");
				pressErgPanel.setBackground(Color.GREEN);
				pressIO = true;
			}
			else {
				pressErgLabel.setText("Nein");
				bemerkungenTextArea.setText(bemerkungenTextArea.getText()+"Unterschnittene Pressung > 900 N / qcm\n");
				pressErgPanel.setBackground(Color.RED);
				pressIO = false;
			}
		}
		else if (trf.isTreibscheibeKeil() && fassadeRadioButton.isSelected()) {
			pressErgLabel.setText("Nein");
			bemerkungenTextArea.setText(bemerkungenTextArea.getText()+"Pressung > 600 N / qcm (Fassadenaufzug)\n");
			pressErgPanel.setBackground(Color.RED);
			pressIO = false;
			double press2 = trf.pressure(true);
	    	pressungFormattedTextField.setText(df_point.format(pressure)+" / "+df_point.format(press2));
			if (press2 > 900) {
				bemerkungenTextArea.setText(bemerkungenTextArea.getText()+"Unterschnittene Pressung > 900 N / qcm\n");
			}
		}
		else if (trf.isTreibscheibeSitz() && pressure <= 900) {
			pressErgLabel.setText("Ja");
			pressErgPanel.setBackground(Color.GREEN);
			pressIO = true;
		}
		else if (trf.isTreibscheibeSitz()) {
			pressErgLabel.setText("Nein");
			pressErgPanel.setBackground(Color.RED);
			pressIO = false;
			bemerkungenTextArea.setText(bemerkungenTextArea.getText()+"Pressung > 900 N / qcm\n");
		}
		else if (trf.isTreibscheibeRund() && pressure <= 900) {
			pressErgLabel.setText("Ja");
			pressErgPanel.setBackground(Color.GREEN);
			pressIO = true;
		}
		else if (trf.isTreibscheibeRund()) {
			pressErgLabel.setText("Nein");
			pressErgPanel.setBackground(Color.RED);
			pressIO = false;
			bemerkungenTextArea.setText(bemerkungenTextArea.getText()+"Pressung > 900 N / qcm\n");
		}
		
		calced = true;
		mainFrame.setEnabled(true);
		setAlwaysOnTop(true);
    }//GEN-LAST:event_calcButtonActionPerformed

    private void umschlWinkelFormattedTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_umschlWinkelFormattedTextFieldKeyReleased
        try {
			double betaDeg = df_point.parse(umschlWinkelFormattedTextField.getText()).doubleValue();
			double betaRad = Math.toRadians(betaDeg);
			trf.setBeta(betaRad);
			calcButtonActionPerformed(null);
		}
		catch (ParseException e) {
			// auto-catched by formattedtextfield
		}
    }//GEN-LAST:event_umschlWinkelFormattedTextFieldKeyReleased

    private void druckButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_druckButtonActionPerformed
    	print();
    }//GEN-LAST:event_druckButtonActionPerformed
  
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea bemerkungenTextArea;
    private javax.swing.JButton calcButton;
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton druckButton;
    private javax.swing.JLabel ergLabel;
    private javax.swing.JPanel ergPanel;
    private javax.swing.JRadioButton fassadeRadioButton;
    private javax.swing.JFormattedTextField gewichtkraftFormattedTextField;
    private javax.swing.JFormattedTextField hkkraftFormattedTextField;
    private javax.swing.JFormattedTextField hubhoeheFormattedTextField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JFormattedTextField kabinekraftFormattedTextField;
    private javax.swing.JRadioButton kleingueterRadioButton;
    private javax.swing.JFormattedTextField phiFormattedTextField;
    private javax.swing.JLabel pressErgLabel;
    private javax.swing.JPanel pressErgPanel;
    private javax.swing.JFormattedTextField pressungFormattedTextField;
    private javax.swing.JFormattedTextField s2s1FormattedTextField;
    private javax.swing.JTextField seilanzahlTextField;
    private javax.swing.JFormattedTextField seilmasseFormattedTextField;
    private javax.swing.JLabel sonstErgLabel;
    private javax.swing.JPanel sonstErgPanel;
    private javax.swing.JRadioButton sonstigerRadioButton;
    private javax.swing.JFormattedTextField t2t1FormattedTextField;
    private javax.swing.JFormattedTextField tskraftFormattedTextField;
    private javax.swing.ButtonGroup typButtonGroup;
    private javax.swing.JFormattedTextField umschlWinkelFormattedTextField;
    // End of variables declaration//GEN-END:variables
    
    /**
     * Prints the now.
     * 
     * @param bk the bk
     * @param job the job
     * @param frame1 the frame1
     */
	private void printNow(Book bk, PrinterJob job, Frame frame1) {
		job.setPageable(bk);
		if (job.printDialog()) {
			try {
				job.print();
			}
			catch (PrinterException e) {
				e.printStackTrace();
			}
		}
		// close print frames after success or no success
		frame1.dispose();
	}
	
	/**
	 * Print.
	 */
	private void print() {
		PrinterJob job = PrinterJob.getPrinterJob();
		PageFormat pageFormat = job.defaultPage();
		pageFormat.setOrientation(PageFormat.PORTRAIT);
		Paper paper = new Paper();
		
		// default: 72, 72, 451.27xx, 697.88xx
		// Set to A4 size.
        paper.setSize(594.936, 841.536);
        // Set the margins. 
		paper.setImageableArea(20, 0, 594.936, 841.536);
		pageFormat.setPaper(paper);
		Book bk = new Book();
		
		// main print frame: with the elevator, parameters and solution
		PrintFrameTreib printFrameTreib = new PrintFrameTreib();
		printFrameTreib.insertUsedTreibParameters(this);
		printFrameTreib.setVisible(true);
		printFrameTreib.setExtendedState(ICONIFIED);
		bk.append(printFrameTreib, pageFormat);
		
		// show the printing dialog
		PrintDialogTreib printDialog = new PrintDialogTreib(this, bk, job, mainFrame);
		GUIHelper.showFrame(printDialog, this);	
	}

	@Override
	public void noClicked(Component child) {
		setEnabled(true);
	}

	@Override
	public void okClicked(Component child) {
		setEnabled(true);
		if (child instanceof PrintDialogTreib) {
			PrintDialogTreib dialog = (PrintDialogTreib)child;
			printNow(dialog.getBook(), dialog.getJob(), dialog.getPrintFrameTreib());
		}
	}

	public javax.swing.JTextArea getBemerkungenTextArea() {
		return bemerkungenTextArea;
	}

	public javax.swing.JRadioButton getFassadeRadioButton() {
		return fassadeRadioButton;
	}

	public javax.swing.JFormattedTextField getGewichtkraftFormattedTextField() {
		return gewichtkraftFormattedTextField;
	}

	public javax.swing.JFormattedTextField getHkkraftFormattedTextField() {
		return hkkraftFormattedTextField;
	}

	public javax.swing.JFormattedTextField getHubhoeheFormattedTextField() {
		return hubhoeheFormattedTextField;
	}

	public javax.swing.JFormattedTextField getKabinekraftFormattedTextField() {
		return kabinekraftFormattedTextField;
	}

	public javax.swing.JRadioButton getKleingueterRadioButton() {
		return kleingueterRadioButton;
	}

	public javax.swing.JFormattedTextField getPhiFormattedTextField() {
		return phiFormattedTextField;
	}

	public javax.swing.JFormattedTextField getPressungFormattedTextField() {
		return pressungFormattedTextField;
	}

	public javax.swing.JFormattedTextField getS2s1FormattedTextField() {
		return s2s1FormattedTextField;
	}

	public javax.swing.JFormattedTextField getSeilmasseFormattedTextField() {
		return seilmasseFormattedTextField;
	}

	public javax.swing.JRadioButton getSonstigerRadioButton() {
		return sonstigerRadioButton;
	}

	public javax.swing.JFormattedTextField getT2t1FormattedTextField() {
		return t2t1FormattedTextField;
	}

	public javax.swing.JFormattedTextField getTskraftFormattedTextField() {
		return tskraftFormattedTextField;
	}

	public javax.swing.JFormattedTextField getUmschlWinkelFormattedTextField() {
		return umschlWinkelFormattedTextField;
	}

	public boolean isPressIO() {
		return pressIO;
	}

	public boolean isTSphiIO() {
		return TSphiIO;
	}

	public boolean isSonstIO() {
		return sonstIO;
	}

	public boolean isCalced() {
		return calced;
	}

	public javax.swing.JTextField getSeilanzahlTextField() {
		return seilanzahlTextField;
	}

	public void somethingChanged() {
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
