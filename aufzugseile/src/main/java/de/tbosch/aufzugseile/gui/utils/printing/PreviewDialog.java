/*
 *  Preview Dialog - A Preview Dialog for your Swing Applications
 *
 *  Copyright (C) 2003 Jens Kaiser.
 *
 *  Written by: 2003 Jens Kaiser <jens.kaiser@web.de>
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Library General Public License
 *  as published by the Free Software Foundation; either version 2 of
 *  the License, or (at your option) any later version.
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Library General Public License for more details.
 *
 *  You should have received a copy of the GNU Library General Public
 *  License along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package de.tbosch.aufzugseile.gui.utils.printing;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.print.PageFormat;
import java.awt.print.Pageable;
import java.awt.print.Printable;
import java.net.URL;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;

public class PreviewDialog extends JDialog implements ActionListener {
    /**
	 * 
	 */
	private static final long serialVersionUID = -8310324093291931760L;
	
	private final static double DEFAULT_ZOOM_FACTOR_STEP = 0.1;

    public PreviewDialog(String title, JFrame owner, Pageable pageable, double zoom) {
        super(owner, title, true);
        this.pageable = pageable;        
        Preview preview = new Preview(pageable, zoom);
        JScrollPane scrollPane = new JScrollPane(preview);
        getContentPane().add(scrollPane, "Center");
        JToolBar toolbar = new JToolBar();
        //toolbar.setRollover(true);
        getContentPane().add(toolbar, "North");
        toolbar.add(getButton("Back24.png", new BrowseAction(preview, -1)));
        toolbar.add(getButton("Forward24.png", new BrowseAction(preview, 1)));
        toolbar.add(new JToolBar.Separator());
        toolbar.add(getButton("ZoomIn24.png", new ZoomAction(preview, DEFAULT_ZOOM_FACTOR_STEP))); 
        toolbar.add(getButton("ZoomOut24.png", new ZoomAction(preview, -DEFAULT_ZOOM_FACTOR_STEP))); 
        toolbar.add(new JToolBar.Separator());
        JPanel dialog = new JPanel();
        dialog.setLayout(new FlowLayout(FlowLayout.RIGHT));
        JButton ok = new JButton("OK");
        ok.addActionListener(this);
        dialog.add(ok);
        getContentPane().add(dialog, "South");
        pack();
        addComponentListener(new ComponentAdapter() {
        	public void componentResized(ComponentEvent evt) {
        		repaint();
        	}
        });
    }
    
    public PreviewDialog(String title, JFrame owner, Pageable pageable) {
        this(title, owner, pageable, 0.0);
    }

    public PreviewDialog(String title, JFrame owner, Printable printable, PageFormat format, int pages, double zoom) {
        this(title, owner, new MyPageable(printable, format, pages), zoom);
    }

    public PreviewDialog(String title, JFrame owner, Printable printable, PageFormat format, int pages) {
        this(title, owner, printable, format, pages, 0.0);
    }
    
    private static class MyPageable implements Pageable {
        public MyPageable(Printable printable, PageFormat format, int pages) {
            this.printable = printable;
            this.format = format;
            this.pages = pages;
        }
        
        public int getNumberOfPages() { 
            return pages; 
        }
        
        public Printable getPrintable(int index) {
            if (index >= pages) throw new IndexOutOfBoundsException();
            return printable;
        }
        
        public PageFormat getPageFormat(int index) {
            if (index >= pages) throw new IndexOutOfBoundsException();
            return format;
        }
        
        private Printable printable;
        private PageFormat format;
        private int pages;
    }
    
    private JButton getButton(String iconName, AbstractAction action) {
        return getButton(null, iconName, action);
    }
    
    private JButton getButton(String name, String iconName, AbstractAction action) {
        JButton result = null;

        ImageIcon icon = null;        
        URL imageURL = getClass().getClassLoader().getResource("icons/" + iconName);
        if (imageURL != null)
            icon = new ImageIcon(imageURL);

        if (action != null) {
            if (icon != null) action.putValue(Action.SMALL_ICON, new ImageIcon(imageURL));
            if (name != null) action.putValue(Action.NAME, name);    
            result = new JButton(action);
        } else 
            result = new JButton(name, icon);
        
        return result;
    }
    
    public void actionPerformed(ActionEvent e) {
        dispose();
    }
    
    protected Pageable pageable;
}
