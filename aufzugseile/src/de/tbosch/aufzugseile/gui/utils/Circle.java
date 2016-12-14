/*
 * Circle.java
 *
 * Created on 13. April 2007, 19:33
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package de.tbosch.aufzugseile.gui.utils;

import java.awt.Graphics;

/**
 * A helper class for the placing-circle.
 * 
 * @author Thomas Bosch (tbosch@gmx.de)
 */
public class Circle extends Pointer {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8914239355517154504L;

	/**
	 * Creates a new instance of Circle.
	 * 
	 * @param scale
	 *            The scale of width and height
	 * @param height
	 *            The height
	 * @param width
	 *            The width
	 */
	public Circle(int width, int height, double scale) {
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
		g.drawOval(0, 0, getWidth() - 1, getHeight() - 1);
	}

}
