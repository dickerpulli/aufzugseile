/*
 * Pointer.java
 *
 * Created on 13. April 2007, 19:35
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package de.tbosch.aufzugseile.gui.utils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JComponent;

import de.tbosch.aufzugseile.utils.Constants;

/**
 * The helper top-class for placeing-rectangle and -circle.
 * 
 * @author Thomas Bosch (tbosch@gmx.de)
 */
public class Pointer extends JComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8634947001085038641L;

	/** The actual color. */
	private Color color;

	/** The saved size at hiding. */
	private Dimension savedSize;

	/** Is it hidden?. */
	private boolean hidden = true;

	/** The width at scale=1. */
	private int widthRel;

	/** The height at scale=1. */
	private int heightRel;

	/**
	 * Creates a new instance of Rectangle.
	 * 
	 * @param scale
	 *            The scale of width and height
	 * @param height
	 *            The height
	 * @param width
	 *            The width
	 */
	public Pointer(int width, int height, double scale) {
		setSize(width, height);
		color = getForeground();
		savedSize = getSize();
		widthRel = (int) ((double) width / scale);
		heightRel = (int) ((double) height / scale);
	}

	/**
	 * Sets the scale parameter.
	 * 
	 * @param scale
	 *            The scale of width and height
	 */
	public void setScale(double scale) {
		int width = (int) (widthRel * scale);
		int height = (int) (heightRel * scale);

		if (hidden)
			savedSize = new Dimension(width, height);
		else
			setSize(width, height);
	}

	/**
	 * Overrides the setSize-method.
	 * 
	 * @param d
	 *            The Dimension object
	 */
	public void setSize(Dimension d) {
		super.setSize(d);
		savedSize = d;
	}

	/**
	 * Overrides the paint-method.
	 * 
	 * @param g
	 *            The Graphics object
	 */
	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(color);
	}

	/**
	 * Sets the color if placing is abled.
	 */
	public void setAbleColor() {
		color = Constants.ABLE_COLOR;
	}

	/**
	 * Sets the color if placing is unabled.
	 */
	public void setUnableColor() {
		color = Constants.UNABLE_COLOR;
	}

	/**
	 * Sets the color if placing is at the border.
	 */
	public void setOnBorderColor() {
		color = Constants.SETBORDER_COLOR;
	}

	/**
	 * Hides the Object and saves its size.
	 */
	public void erase() {
		if (!hidden) {
			savedSize = getSize();
			setSize(0, 0);
			hidden = true;
		}
	}

	/**
	 * Unhides the object and restores size.
	 */
	public void unerase() {
		if (hidden) {
			setSize(savedSize);
			hidden = false;
		}
	}

}
