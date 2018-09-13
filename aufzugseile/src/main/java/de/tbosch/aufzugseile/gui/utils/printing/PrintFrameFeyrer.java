package de.tbosch.aufzugseile.gui.utils.printing;

import java.awt.Graphics;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.util.Vector;
import java.util.logging.Level;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RepaintManager;
import javax.swing.table.DefaultTableModel;

import de.tbosch.aufzugseile.berechnung.lebensdauer.CalcRolle;
import de.tbosch.aufzugseile.gui.MainFrame;
import de.tbosch.aufzugseile.utils.Constants;
import de.tbosch.aufzugseile.utils.Helper;
import de.tbosch.aufzugseile.utils.LongGrouping;

/**
 *
 * @author Thomas Bosch (tbosch@gmx.de)
 */
public class PrintFrameFeyrer extends javax.swing.JFrame implements Printable {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -2423439924613588748L;
	
	/** Creates new form PrintFrameFeyrer */
    public PrintFrameFeyrer() {
    	initComponents();
    	setResizable(false);
		setTitle("Preview 2 - DO NOT CLOSE"); //$NON-NLS-1$
		jLabel1.setIcon(new ImageIcon(Helper.getFileURL("images/drako_200.jpg"))); //$NON-NLS-1$
    }
    
    /* (non-Javadoc)
     * @see java.awt.print.Printable#print(java.awt.Graphics, java.awt.print.PageFormat, int)
     */
    public int print(Graphics g, PageFormat pageFormat, int pageIndex) {
		int x = (int)pageFormat.getImageableX() + 1;
		int y = (int)pageFormat.getImageableY() + 1;
		g.translate(x, y);
		RepaintManager currentManager = RepaintManager.currentManager(this);
		currentManager.setDoubleBufferingEnabled(false);
		paint(g);
		currentManager.setDoubleBufferingEnabled(true);
		return (PAGE_EXISTS);
	}
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    @SuppressWarnings("serial") //$NON-NLS-1$
	private void initComponents() {
        mainPanel = new javax.swing.JPanel();
        uPanel = new javax.swing.JPanel();
        tableScrollPane = new javax.swing.JScrollPane();
        feyrerTable = new javax.swing.JTable();
        oPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        bezeichnungLabel = new javax.swing.JLabel();
        uuPanel = new javax.swing.JPanel();
        uuuPanel = new javax.swing.JPanel();
        nutzerLabel = new javax.swing.JLabel();
        dateiLabel = new javax.swing.JLabel();
        datumLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setResizable(false);
        mainPanel.setBackground(new java.awt.Color(255, 255, 255));
        uPanel.setLayout(new java.awt.GridLayout(1, 0));

        uPanel.setBackground(new java.awt.Color(255, 255, 255));
        uPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder(Messages.getString("PrintFrameFeyrer.3")))); //$NON-NLS-1$
        tableScrollPane.setBackground(new java.awt.Color(255, 255, 255));
        feyrerTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                Messages.getString("PrintFrameFeyrer.4"), "b0 / N A", "b0 / N A10", "b1", "b2", "b3", "b4", "b5 / N A", "b5 / N A10" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$
            }
        ) {
            @SuppressWarnings("unchecked") //$NON-NLS-1$
			Class[] types = new Class [] {
                java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            @SuppressWarnings("unchecked") //$NON-NLS-1$
			public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableScrollPane.setViewportView(feyrerTable);

        uPanel.add(tableScrollPane);

        oPanel.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setIcon(new javax.swing.ImageIcon("/home/bobo/projekte/java/Aufzugseile/images/drako_200.jpg")); //$NON-NLS-1$

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(Messages.getString("PrintFrameFeyrer.15"))); //$NON-NLS-1$

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(bezeichnungLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 318, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(bezeichnungLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 12, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout oPanelLayout = new javax.swing.GroupLayout(oPanel);
        oPanel.setLayout(oPanelLayout);
        oPanelLayout.setHorizontalGroup(
            oPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, oPanelLayout.createSequentialGroup()
                .addContainerGap(340, Short.MAX_VALUE)
                .addComponent(jLabel1))
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        oPanelLayout.setVerticalGroup(
            oPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        uuPanel.setLayout(new java.awt.GridLayout(2, 1));

        uuPanel.setBackground(new java.awt.Color(255, 255, 255));
        uuPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(Messages.getString("PrintFrameFeyrer.16"))); //$NON-NLS-1$

        uuuPanel.setLayout(new java.awt.GridLayout());

        uuuPanel.setBackground(new java.awt.Color(255, 255, 255));
        nutzerLabel.setText(Messages.getString("PrintFrameFeyrer.17")); //$NON-NLS-1$
        uuuPanel.add(nutzerLabel);

        dateiLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        dateiLabel.setText(Messages.getString("PrintFrameFeyrer.18")); //$NON-NLS-1$
        uuuPanel.add(dateiLabel);

        datumLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        datumLabel.setText(Messages.getString("PrintFrameFeyrer.19")); //$NON-NLS-1$
        uuuPanel.add(datumLabel);

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(oPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(uuuPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 540, Short.MAX_VALUE)
            .addComponent(uuPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 540, Short.MAX_VALUE)
            .addComponent(uPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 540, Short.MAX_VALUE)
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addComponent(oPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(uPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(uuPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 404, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(uuuPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel bezeichnungLabel;
    private javax.swing.JLabel dateiLabel;
    private javax.swing.JLabel datumLabel;
    private javax.swing.JTable feyrerTable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JLabel nutzerLabel;
    private javax.swing.JPanel oPanel;
    private javax.swing.JScrollPane tableScrollPane;
    private javax.swing.JPanel uPanel;
    private javax.swing.JPanel uuPanel;
    private javax.swing.JPanel uuuPanel;
    // End of variables declaration//GEN-END:variables
    
    /**
     * Insert used feyrer parameters.
     * 
     * @param mainFrame the main frame
     */
    public void insertUsedFeyrerParameters(MainFrame mainFrame) {
		feyrerTable.getTableHeader().setFont(new java.awt.Font("Dialog", 0, 10)); //$NON-NLS-1$
		feyrerTable.setFont(new java.awt.Font("Dialog", 0, 10)); //$NON-NLS-1$
    	for (int i = 0; i < feyrerTable.getColumnCount(); i++) {
    		int width = 0;
    		if (i == 0) {
    			width = 92;
    		}
    		else {
    			width = 52;
    		}
			feyrerTable.getColumnModel().getColumn(i).setPreferredWidth(width);
			feyrerTable.getColumnModel().getColumn(i).setMaxWidth(width);
			feyrerTable.getColumnModel().getColumn(i).setMinWidth(width);
    	}
    	DefaultTableModel feyrerTableModel = (DefaultTableModel)feyrerTable.getModel();
		for (int r = 0; r < mainFrame.getParameterTableModel().getRowCount(); r++) {
			if ((Boolean)mainFrame.getParameterTableModel().getValueAt(r, 0) == true) {
				Vector<Object> rowData = new Vector<Object>();
				for (int i = 0; i < feyrerTableModel.getColumnCount(); i++) {
					rowData.add(mainFrame.getParameterTableModel().getValueAt(r, i + 1));
				}
				feyrerTableModel.addRow(rowData);
			}
		}
		feyrerTable.setRowHeight(12);
    }

    /**
     * Creates the einzel ergebnis tables.
     * 
     * @param mainFrame the main frame
     */
    public void createEinzelErgebnisTables(MainFrame mainFrame) {
    	DefaultTableModel parameterTableModel = (DefaultTableModel)mainFrame.getParameterTableModel();
    	Vector<String> seiltypVector = new Vector<String>();
    	
    	for (int i = 0; i < mainFrame.getParameterTableModel().getRowCount(); i++) {
    		if ((Boolean)parameterTableModel.getValueAt(i, 0) != null && (Boolean)parameterTableModel.getValueAt(i, 0) == true) {
				if (parameterTableModel.getValueAt(i, 1) != null && parameterTableModel.getValueAt(i, 2) != null
						&& parameterTableModel.getValueAt(i, 3) != null && parameterTableModel.getValueAt(i, 4) != null
						&& parameterTableModel.getValueAt(i, 5) != null && parameterTableModel.getValueAt(i, 6) != null
						&& parameterTableModel.getValueAt(i, 7) != null && parameterTableModel.getValueAt(i, 8) != null
						&& parameterTableModel.getValueAt(i, 9) != null && parameterTableModel.getValueAt(i, 10) != null
						&& parameterTableModel.getValueAt(i, 11) != null && parameterTableModel.getValueAt(i, 12) != null) {
					seiltypVector.add((String)parameterTableModel.getValueAt(i, 1));
				}
				else {
					Constants.LOGGER.log(Level.WARNING, "Partly chooing empty rows. Ignoring those rows."); //$NON-NLS-1$
				}
			}
    	}
    	for (int j = 0; j < seiltypVector.size(); j++) {
    		createEinzelErgebnisTable(mainFrame, j, seiltypVector.size(), seiltypVector.get(j));
    	}
    }
    
	/**
	 * Creates the einzel ergebnis table.
	 * Same as the table creation above
	 * 
	 * @param index the index
	 * @param count the count
	 * @param mainFrame the main frame
	 * @param typ the typ
	 */
	@SuppressWarnings({ "unchecked", "serial" }) //$NON-NLS-1$ //$NON-NLS-2$
	private void createEinzelErgebnisTable(MainFrame mainFrame, int index, int count, String typ) {
		Vector<String> nameVector = new Vector<String>();
		Vector<Class> typeVector = new Vector<Class>();
		Vector<Boolean> editVector = new Vector<Boolean>();
		nameVector.add(Messages.getString("PrintFrameFeyrer.24")); //$NON-NLS-1$
		typeVector.add(String.class);
		editVector.add(false);
		for (int i = 0; i < mainFrame.getBerechnung().getCalcRolls().size(); i++) {
			CalcRolle rolle = mainFrame.getBerechnung().getCalcRolls().get(i);
			nameVector.add(rolle.getName());
			typeVector.add(LongGrouping.class);
			editVector.add(false);
		}
		String nameArray[] = new String[nameVector.size()];
		nameVector.copyInto(nameArray);
		final Class typeArray[] = new Class[typeVector.size()];
		typeVector.copyInto(typeArray);
		final Boolean editArray[] = new Boolean[editVector.size()];
		editVector.copyInto(editArray);

		JTable einzelErgebnisTable = new JTable();
		einzelErgebnisTable.setModel(new DefaultTableModel(null, nameArray) {
			Class[] types = typeArray;

			Boolean[] canEdit = editArray;

			public Class getColumnClass(int columnIndex) {
				return types[columnIndex];
			}

			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return canEdit[columnIndex];
			}
		});
		einzelErgebnisTable.getTableHeader().setReorderingAllowed(false);
		einzelErgebnisTable.setRowSelectionAllowed(false);
		for (int i = 0; i < einzelErgebnisTable.getColumnCount(); i++) {
    		int width = 0;
    		if (i == 0) {
    			width = 50;
    		}
    		else {
    			width = 65;
    		}
    		einzelErgebnisTable.getColumnModel().getColumn(i).setPreferredWidth(width);
    		einzelErgebnisTable.getColumnModel().getColumn(i).setMaxWidth(width);
    		einzelErgebnisTable.getColumnModel().getColumn(i).setMinWidth(width);
    	}
		einzelErgebnisTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		einzelErgebnisTable.getTableHeader().setFont(new java.awt.Font("Dialog", 0, 10)); //$NON-NLS-1$
		einzelErgebnisTable.setFont(new java.awt.Font("Dialog", 0, 10)); //$NON-NLS-1$
		
		JPanel tablePanel = new JPanel();
		tablePanel.setLayout(new java.awt.GridLayout(1, 0));
		tablePanel.setBackground(new java.awt.Color(255, 255, 255));
		
		tablePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(Messages.getString("PrintFrameFeyrer.28")+" "+typ)); //$NON-NLS-1$
		
		JScrollPane einzelErgebnisTableScrollPane = new JScrollPane();
		einzelErgebnisTableScrollPane.setViewportView(einzelErgebnisTable);
		
		tablePanel.add(einzelErgebnisTableScrollPane);
		uuPanel.setLayout(new java.awt.GridLayout(count, 1));
		uuPanel.add(tablePanel);
		
		setEinzelErgebnisTableValues(einzelErgebnisTable, mainFrame.getBerechnung().getCalcRolls(), index);
	}
	

	/**
	 * Sets the einzel ergebnis table values.
	 * 
	 * @param index the index
	 * @param einzelErgebnisTable the einzel ergebnis table
	 * @param calcRolle the einzel ergebnis table values
	 */
	@SuppressWarnings("unchecked") //$NON-NLS-1$
	private void setEinzelErgebnisTableValues(JTable einzelErgebnisTable, Vector<CalcRolle> calcRolle, int index) {
		DefaultTableModel einzelErgebnisTableModel = (DefaultTableModel)einzelErgebnisTable.getModel();

		// reset einzelErgebnis table
		for (int i = einzelErgebnisTableModel.getRowCount() - 1; i >= 0; i--) {
			einzelErgebnisTableModel.removeRow(i);
		}

		// fill with solution array
		for (int j = 0; j < calcRolle.get(0).getLebensdauer().length; j++) {
			Vector rowData = new Vector();
			if (j == 0) rowData.add("N A"); //$NON-NLS-1$
			if (j == 1) rowData.add("N A10"); //$NON-NLS-1$
			for (int i = 0; i < calcRolle.size(); i++) {
				rowData.add(new LongGrouping(calcRolle.get(i).getLebensdauer()[j][index], true));
			}
			einzelErgebnisTableModel.addRow(rowData);
		}
	}

	public javax.swing.JLabel getBezeichnungLabel() {
		return bezeichnungLabel;
	}

	public javax.swing.JLabel getDateiLabel() {
		return dateiLabel;
	}

	public javax.swing.JLabel getDatumLabel() {
		return datumLabel;
	}

	public javax.swing.JLabel getNutzerLabel() {
		return nutzerLabel;
	}
}	