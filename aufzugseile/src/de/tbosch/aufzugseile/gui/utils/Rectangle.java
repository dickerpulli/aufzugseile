/*
 * Rectangle.java
 *
 * Created on 13. April 2007, 17:28
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package de.tbosch.aufzugseile.gui.utils;

import java.awt.Graphics;

/**
 * The helper-class for the placing-rectangle.
 * 
 * @author Thomas Bosch (tbosch@gmx.de)
 */
public class Rectangle extends Pointer {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7187569097678264241L;

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
	public Rectangle(int width, int height, double scale) {
		super(width, height, scale);
	}

	/**
	 * Overrides the paint-method.
	 * 
	 * @param g
	 *            The Graphics object
	 */
	public void paint(Graphics g) {
		super.paint(g);
		g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
	}

}
