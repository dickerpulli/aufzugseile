/**
 * 
 */
package de.tbosch.aufzugseile.utils;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Point;
import java.io.File;
import java.net.URL;

import javax.swing.JFrame;
import javax.swing.JTextField;

/**
 * @author Thomas Bosch (tbosch@gmx.de)
 * 
 */
public class Helper {

	/**
	 * Gets the file URL.
	 * 
	 * @param resource
	 *            the resource
	 * 
	 * @return the file URL
	 */
	public static URL getFileURL(String resource) {
		try {
			String path = getProgramPath();

			String filename = "";
			if (path == "") {
				filename = resource;
			} else {
				filename = path + Constants.FILESEP + resource;
			}
			File file = new File(filename);
			if (file.exists()) {
				URL toURL = file.toURI().toURL();
				return toURL;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		resource = resource.replace("\\", "/");
		return Helper.class.getClassLoader().getResource(resource);
	}

	/**
	 * Gets the correct integer value from a text field.
	 * 
	 * @param textField
	 *            the text field
	 * 
	 * @return the correct value as integer, or Integer.MIN_VALUE if the textfield contains no number
	 */
	public static int getIntFromTextField(JTextField textField) {
		String text = textField.getText().replace(" ", "");

		int number;
		try {
			number = Integer.parseInt(text);
		} catch (NumberFormatException e) {
			return Integer.MIN_VALUE;
		}

		return number;
	}

	/**
	 * Gets the correct double value from a text field.
	 * 
	 * @param textField
	 *            the text field
	 * 
	 * @return the correct value as double, or Double.MIN_VALUE if the textfield contains no number
	 */
	public static double getDoubleFromTextField(JTextField textField) {
		String text = textField.getText().replace(" ", "");

		double number;
		try {
			number = Double.parseDouble(text);
		} catch (NumberFormatException e) {
			return Double.MIN_VALUE;
		}

		return number;
	}

	/**
	 * Shows the child frame in the middle of the parent frame The parent is set disabled.
	 * 
	 * @param child
	 *            the child
	 * @param parent
	 *            the parent
	 */
	public static void showFrame(JFrame child, JFrame parent) {
		Point mainLocation = parent.getLocation();
		Dimension mainDimension = parent.getSize();
		Dimension frameDimension = child.getSize();

		// sets the location of the child frame at the middle of the main
		// windows and alyways on top

		child.setLocation((int) (mainLocation.getX() + ((mainDimension.getWidth() - frameDimension.getWidth()) / 2)),
				(int) (mainLocation.getY() + ((mainDimension.getHeight() - frameDimension.getHeight()) / 2)));
		// child.setAlwaysOnTop(true);
		child.setVisible(true);

		// disable parent window
		parent.setEnabled(false);
	}

	/**
	 * Shows the child dialog in the middle of the parent frame The parent is set disabled.
	 * 
	 * @param child
	 *            the child
	 * @param parent
	 *            the parent
	 */
	public static void showDialog(Dialog child, JFrame parent) {
		Point mainLocation = parent.getLocation();
		Dimension mainDimension = parent.getSize();
		Dimension frameDimension = child.getSize();

		// sets the location of the child dialog at the middle of the parent
		// window and alyways on top

		child.setLocation((int) (mainLocation.getX() + ((mainDimension.getWidth() - frameDimension.getWidth()) / 2)),
				(int) (mainLocation.getY() + ((mainDimension.getHeight() - frameDimension.getHeight()) / 2)));
		// child.setAlwaysOnTop(true);
		child.setVisible(true);

		// disable parent window
		parent.setEnabled(false);
	}

	public static String getProgramPath() {
		String path = Constants.CLASSPATH;
		if (path.indexOf("Aufzugseile.jar") != -1) {
			if (path.lastIndexOf(Constants.FILESEP) != -1) {
				path = path.substring(0, path.lastIndexOf(Constants.FILESEP));
			} else {
				path = "";
			}
		} else if (path.indexOf("Aufzugseile.exe") != -1) {
			if (path.lastIndexOf(Constants.FILESEP) != -1) {
				path = path.substring(0, path.lastIndexOf(Constants.FILESEP));
			} else {
				path = "";
			}
		} else {
			String searchString = "lib" + Constants.FILESEP + "swing-layout-1.0.jar";
			if (path.indexOf(searchString) != -1) {
				path = path.substring(0, path.indexOf(searchString));
				if (path.indexOf(Constants.PATHSEP) != -1) {
					path = path.substring(path.lastIndexOf(Constants.PATHSEP) + 1);
				}
				if (path.lastIndexOf(Constants.FILESEP) != -1) {
					path = path.substring(0, path.lastIndexOf(Constants.FILESEP));
				}
			}
		}
		return path;
	}
}
