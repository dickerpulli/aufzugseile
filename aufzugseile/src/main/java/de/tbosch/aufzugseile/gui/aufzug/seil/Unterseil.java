package de.tbosch.aufzugseile.gui.aufzug.seil;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import de.tbosch.aufzugseile.gui.aufzug.Aufzugschacht;
import de.tbosch.aufzugseile.gui.aufzug.EndElement;
import de.tbosch.aufzugseile.gui.aufzug.Gewicht;
import de.tbosch.aufzugseile.gui.aufzug.Kabine;
import de.tbosch.aufzugseile.gui.aufzug.Treibscheibe;
import de.tbosch.aufzugseile.utils.Constants;
import de.tbosch.aufzugseile.utils.Helper;
import de.tbosch.utilities.trigonometry.Circles;

/**
 * Rope hanging under the cabin and counter weight to compensate the weight of the hanging rope.
 * 
 * @author Thomas Bosch (tbosch@gmx.de)
 */
public class Unterseil extends JComponent {

	/** Serial id */
	private static final long serialVersionUID = 8039734139503699978L;

	/** The elevator */
	private Aufzugschacht aufzugschacht;

	/** All points building the line for this gui element */
	transient private List<Point> points;

	/** The starting width of the roll */
	private int rollWidthRel;
	private int rollWidth;

	/** The starting height of the roll */
	private int rollHeightRel;
	private int rollHeight;

	/** the actual scale for width and height calculation */
	private double scale;

	/** Scale in relation to the treibscheibe */
	private double relScale;

	/** The icon of the roll */
	transient private ImageIcon imageIcon;

	/** the square of the roll */
	private int durchmesser;

	/** The disance between the rope and the bottom of the elevator */
	private int rollDistance = 20;

	/** The polygon for the spanning weight */
	transient private Polygon weight;

	/** The mass of the spanning weight in kg - 0 is defined as "without weight" */
	private int weightMass;

	/**
	 * The constructor
	 * 
	 * @param aufzugschacht
	 *            The elevator
	 */
	public Unterseil(Aufzugschacht aufzugschacht) {
		this.aufzugschacht = aufzugschacht;
		setLocation(0, 0);
		setSize(aufzugschacht.getSize());
		initListener();
		weightMass = 0;
		durchmesser = 200;
		rollWidthRel = 32;
		rollHeightRel = 32;
		scale = 1;
		rollWidth = (int) (scale * rollWidthRel);
		rollHeight = (int) (scale * rollHeightRel);
		// we assume treibscheibe already exists
		relScale = (double) getRollendurchmesser() / aufzugschacht.getTreibscheibe().getDurchmesser();
		setRollSize((int) (aufzugschacht.getTreibscheibe().getWidth() * relScale), (int) (aufzugschacht.getTreibscheibe().getHeight() * relScale));
	}

	/**
	 * @see javax.swing.JComponent#paint(java.awt.Graphics)
	 */
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		createPolygon();
		Point prevPoint;
		if (points.size() > 0) {
			prevPoint = points.get(0);
			for (int i = 1; i < points.size(); i++) {
				Line2D line = new Line2D.Double(prevPoint, points.get(i));
				g.drawLine((int) line.getX1(), (int) line.getY1(), (int) line.getX2(), (int) line.getY2());
				prevPoint = points.get(i);
			}
		}
		if (weightMass > 0) {
			drawRolls(g);
		}
	}

	/**
	 * Draws the rolls for this rope.
	 * 
	 * @param g
	 */
	private void drawRolls(Graphics g) {
		Kabine kabine = aufzugschacht.getKabine();
		Gewicht gewicht = aufzugschacht.getGewicht();
		// First proof, with end element is on the right and with on the left side
		EndElement left = kabine;
		EndElement right = gewicht;
		if (kabine.getX() > gewicht.getX()) {
			left = gewicht;
			right = kabine;
		}
		int leftX = left.getX() + left.getWidth() / 2;
		int rightX = right.getX() + right.getWidth() / 2;
		if (imageIcon == null) {
			imageIcon = new ImageIcon(Helper.getFileURL(Constants.UMLENKROLLE_ELEMENT_ICON_32));
		}
		Image image = imageIcon.getImage();
		g.drawImage(image, leftX, aufzugschacht.getHeight() - rollDistance - rollHeight, rollWidth, rollHeight, this);
		g.drawImage(image, rightX - rollWidth, aufzugschacht.getHeight() - rollDistance - rollHeight, rollWidth, rollHeight, this);

		// Draw the spanning weight between the rolls
		weight = new Polygon();
		int size = 5;
		int diff = (size - 1) / 2;
		int pLeft = leftX + rollWidth / 2 - diff;
		int pRight = rightX - rollWidth / 2 + diff;
		int pLower = aufzugschacht.getHeight() - rollDistance - rollHeight / 2 + diff;
		int pUpper = aufzugschacht.getHeight() - rollDistance - rollHeight / 2 - diff;
		// between rolls
		weight.addPoint(pLeft, pLower);
		weight.addPoint(pLeft, pUpper);
		weight.addPoint(pRight, pUpper);
		weight.addPoint(pRight, pLower);
		weight.addPoint(pLeft + (pRight - pLeft) / 2 + diff, pLower);
		weight.addPoint(pLeft + (pRight - pLeft) / 2 + diff, pLower + 10);
		weight.addPoint(pLeft + (pRight - pLeft) / 2 + diff + 10, pLower + 10);
		weight.addPoint(pLeft + (pRight - pLeft) / 2 + diff + 10, pLower + 10 + 10);
		weight.addPoint(pLeft + (pRight - pLeft) / 2 - diff - 10, pLower + 10 + 10);
		weight.addPoint(pLeft + (pRight - pLeft) / 2 - diff - 10, pLower + 10);
		weight.addPoint(pLeft + (pRight - pLeft) / 2 - diff, pLower + 10);
		weight.addPoint(pLeft + (pRight - pLeft) / 2 - diff, pLower);
		g.fillPolygon(weight);
	}

	/**
	 * Scales the width of the roll for this rope. For example for printing view.
	 * 
	 * @param scale
	 */
	public void setScale(double scale) {
		if (this.scale != scale) {
			this.scale = scale;

			// calculate the absolute coordinates and dimensions on the base
			// of the given scale parameters
			rollWidth = (int) (rollWidthRel * scale);
			rollHeight = (int) (rollHeightRel * scale);
		}
	}

	/**
	 * Initialize the event listeners.
	 */
	public void initListener() {
		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				mouseClickedUnterseil(evt);
			}
		});
	}

	/**
	 * Called when the object is clicked by the mouse.
	 * 
	 * @param evt
	 *            The mouse event
	 */
	@SuppressWarnings("serial")
	private void mouseClickedUnterseil(MouseEvent evt) {
		if (evt.getButton() == Constants.RIGHT_MOUSE_BUTTON && !aufzugschacht.mainFrameIsAnyButtonSelected && mouseOverWeight(evt.getX(), evt.getY())) {
			JPopupMenu popup = new JPopupMenu("Label");
			JMenuItem settings = new JMenuItem(new AbstractAction("Einstellungen", new ImageIcon(Helper.getFileURL(Constants.MENU_GLOBAL_ICON))) {
				public void actionPerformed(ActionEvent e) {
					popupSettings();
				}
			});
			popup.add(settings);

			// shows the menu if it has any entries
			popup.show(this, evt.getX(), evt.getY());
		} else if (evt.getButton() == Constants.RIGHT_MOUSE_BUTTON && !aufzugschacht.mainFrameIsAnyButtonSelected && mouseOverSeil(evt.getX(), evt.getY())) {
			JPopupMenu popup = new JPopupMenu("Label");
			JMenuItem delete = new JMenuItem(new AbstractAction("L" + Constants.OE_K + "schen", new ImageIcon(Helper.getFileURL(Constants.DELETE_ICON))) {
				public void actionPerformed(ActionEvent e) {
					popupDelete();
				}
			});
			String title;
			if (weightMass > 0) {
				title = "Ohne Spanngewicht";
			} else {
				title = "Mit Spanngewicht";
			}
			JMenuItem change = new JMenuItem(new AbstractAction(title, new ImageIcon(Helper.getFileURL(Constants.CHANGE_ICON))) {
				public void actionPerformed(ActionEvent e) {
					popupChange();
				}
			});
			popup.add(delete);
			popup.add(change);

			// shows the menu if it has any entries
			popup.show(this, evt.getX(), evt.getY());
		} else {
			aufzugschacht.getSeil().mouseClickedSeil(evt);
		}
	}

	/**
	 * Popup delete.
	 */
	private void popupDelete() {
		aufzugschacht.deleteUnterseil();
	}

	/**
	 * Settings for the spanning weight.
	 */
	private void popupSettings() {
		aufzugschacht.mainFrameShowSpanngewichtOptions(this);
		aufzugschacht.mainFrameSetMassUnterseilGewicht(weightMass);
	}

	/**
	 * Toggles the existence of the spanning weight.
	 */
	private void popupChange() {
		if (weightMass == 0) {
			weightMass = 500;
		} else {
			weightMass = 0;
		}
		aufzugschacht.mainFrameSetMassUnterseilGewicht(weightMass);
		repaint();
	}

	/**
	 * Checks if mouse is in a defined area (6x6 rectangle) over the rope points.
	 * 
	 * @param mx
	 *            The mouse x-coordinates
	 * @param my
	 *            The mouse y-coordinates
	 * 
	 * @return True if mouse is over the rope
	 */
	private boolean mouseOverSeil(int mx, int my) {
		int sens = 3;
		Rectangle2D mouseRect = new Rectangle2D.Double(mx - sens, my - sens, sens * 2, sens * 2);

		Point prevPoint;
		if (points.size() > 0) {
			prevPoint = points.get(0);
			for (int i = 0; i < points.size(); i++) {
				Line2D line = new Line2D.Double(prevPoint, points.get(i));
				if (line.intersects(mouseRect))
					return true;
				prevPoint = points.get(i);
			}
		}

		return false;
	}

	/**
	 * Checks if mouse is in a defined area (6x6 rectangle) over the polygon of the spanning weight.
	 * 
	 * @param mx
	 *            The mouse x-coordinates
	 * @param my
	 *            The mouse y-coordinates
	 * 
	 * @return True if mouse is over the weight
	 */
	private boolean mouseOverWeight(int mx, int my) {
		int sens = 3;
		Rectangle2D mouseRect = new Rectangle2D.Double(mx - sens, my - sens, sens * 2, sens * 2);

		if (weight != null && weight.intersects(mouseRect)) {
			return true;
		}

		return false;
	}

	/**
	 * Creates the poygon for this line representing the rope. Starting point is the down side of the cabin. Then the next is the ground of the elevator with
	 * its own roll for the cabin. Then the rope goes next to the roll for the counterweight on the ground. After this the rope goes up to the down side of the
	 * counter weight.
	 */
	private void createPolygon() {
		points = new ArrayList<Point>();
		Kabine kabine = aufzugschacht.getKabine();
		Gewicht gewicht = aufzugschacht.getGewicht();
		// First proof, with end element is on the right and with on the left side
		EndElement left = kabine;
		EndElement right = gewicht;
		if (kabine.getX() > gewicht.getX()) {
			left = gewicht;
			right = kabine;
		}
		int leftX = left.getX() + left.getWidth() / 2;
		int leftY = left.getY() + left.getHeight();
		int rightX = right.getX() + right.getWidth() / 2;
		int rightY = right.getY() + right.getHeight();
		points.add(new Point(leftX, leftY));
		// The rope goes different ways with or without rolls on the ground
		// Start- and Emdpoint are the same
		if (weightMass > 0) {
			int radius = rollWidth / 2;
			Point[] circlePoints = Circles.getCirclePoints(radius, new Point(leftX + radius, aufzugschacht.getHeight() - radius - rollDistance));
			for (int i = 180; i > 90; i--) {
				points.add(circlePoints[i]);
			}
			points.add(new Point(rightX - rollWidth, circlePoints[90].y));
			circlePoints = Circles.getCirclePoints(radius, new Point(rightX - radius, aufzugschacht.getHeight() - radius - rollDistance));
			for (int i = 90; i > 0; i--) {
				points.add(circlePoints[i]);
			}
		} else {
			int radius = (rightX - leftX) / 2;
			Point[] circlePoints = Circles.getCirclePoints(radius, new Point(leftX + radius, aufzugschacht.getHeight() - radius - rollDistance));
			for (int i = 180; i > 0; i--) {
				points.add(circlePoints[i]);
			}
		}
		points.add(new Point(rightX, rightY));
	}

	/**
	 * Gets the square of the roll.
	 * 
	 * @return
	 */
	public int getRollendurchmesser() {
		return durchmesser;
	}

	/**
	 * Sets the size of the roll.
	 * 
	 * @param nw
	 * @param nh
	 */
	public void setRollSize(int nw, int nh) {
		rollWidth = nw;
		rollHeight = nh;
		if (rollWidth > 0 && rollWidth <= 16) {
			setImage(Constants.UMLENKROLLE_ELEMENT_ICON_16);
		} else if (rollWidth > 16 && rollWidth <= 32) {
			setImage(Constants.UMLENKROLLE_ELEMENT_ICON_32);
		} else if (rollWidth > 32 && rollWidth <= 48) {
			setImage(Constants.UMLENKROLLE_ELEMENT_ICON_48);
		} else {
			setImage(Constants.UMLENKROLLE_ELEMENT_ICON_64);
		}
	}

	/**
	 * Sets the new image for the icon.
	 * 
	 * @param path
	 */
	public void setImage(String path) {
		imageIcon = new ImageIcon(Helper.getFileURL(path));
	}

	/**
	 * Refreshes the size of the roll.
	 */
	public void refreshSize() {
		if (aufzugschacht != null && aufzugschacht.getTreibscheibe() != null) {
			Treibscheibe scheibe = aufzugschacht.getTreibscheibe();
			relScale = (double) getRollendurchmesser() / scheibe.getDurchmesser();
			setRollSize((int) (scheibe.getWidth() * relScale), (int) (scheibe.getHeight() * relScale));
		}
	}

	// Getter / Setter

	public int getWeightMass() {
		return weightMass;
	};

	public void setWeightMass(int weightMass) {
		this.weightMass = weightMass;
	}

}
