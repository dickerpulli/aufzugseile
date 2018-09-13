package de.tbosch.aufzugseile.gui.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import de.tbosch.aufzugseile.gui.aufzug.Element;


public class MyToolTip extends JLabel {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 4540449951691112384L;
	
	private Element element;

	public MyToolTip(JPanel aPanel) {
		setBackground(Color.white);
		setOpaque(true);
		setBorder(new LineBorder(Color.black));
		setForeground(Color.black);
		setFont(new Font("Dialog", 0, 8));
	}
	
	@Override
	public void setText(String text) {
		super.setText(text);		
		if (getFont() != null) {
			setSize(text.length() * (getFont().getSize() * 3/4) + 2, getFont().getSize() + 2);
		}
	}
	
	public void setElement(Element element, String name) {
		this.element = element;
		element.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentMoved(ComponentEvent evt) {
				elementChanged();
			}
			@Override
			public void componentResized(ComponentEvent evt) {
				elementChanged();
			}
		});
		setText(name);
		// move element one up and one down
		element.setLocation(element.getX(), element.getY() + 1);
		element.setLocation(element.getX(), element.getY() - 1);
		// just for beeing moved :-) ...
	}
	
	public void elementChanged() {
		setLocation(element.getX(), element.getY() - getHeight());
	}
}
