package de.tbosch.aufzugseile.gui.aufzug;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.Calendar;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import de.tbosch.aufzugseile.gui.aufzug.seil.SeilElement;
import de.tbosch.aufzugseile.gui.aufzug.seil.Seilaufhaenger;
import de.tbosch.aufzugseile.utils.Constants;
import de.tbosch.aufzugseile.utils.Helper;

/**
 * The element class. All elements of the elevator are implementet over this class. Moving, dragging are also implemented as popupmenus and scaling.
 * 
 * @author Thomas Bosch (tbosch@gmx.de)
 */
public class Element extends JComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -298929306526124852L;

	/** The image of the element. */
	transient private ImageIcon imageIcon;

	private String iconPath;

	/** The scale of width and height. */
	private double scale = 1;

	/** The scale of the x-position. */
	private double xscale = 1;

	/** The scale of the y-position. */
	private double yscale = 1;

	/** The x-coordinate at scale=1. */
	private int xRel;

	/** The y-coordinate at scale=1. */
	private int yRel;

	/** The width at scale=1. */
	private int widthRel;

	/** The height at scale=1. */
	private int heightRel;

	/** The rope-clip-y at scale=1. */
	private int seilxRel;

	/** The rope-clip-x at scale=1. */
	private int seilyRel;

	/** At first move omx and omy are set. */
	private boolean firstMove = true;

	/** Saved x-coordintes for dragging. */
	private int omx;

	/** Saved y-coordintes for dragging. */
	private int omy;

	/** The elevator itself. */
	private Aufzugschacht aufzugschacht;

	/** If the element is actually moving/dragging. */
	private boolean dragging = false;

	/** The seil element. */
	private Vector<SeilElement> seilElementVector = new Vector<SeilElement>();

	/** If element clicked one time. */
	private boolean clicked = false;

	/** The old time from the first click. */
	private Calendar oldTime;

	/** Is the element draggable */
	private boolean draggable = true;

	/**
	 * Creates a new instance of Element.
	 * 
	 * @param scale
	 *            The scale of width and heigth
	 * @param iconpath
	 *            The path of the icon
	 * @param height
	 *            Height
	 * @param aufzugschacht
	 *            The elevator itself
	 * @param width
	 *            Width
	 * @param seilx
	 *            Relative x-coordinates of rope-clipping-point
	 * @param yscale
	 *            The scale of y-coordinates
	 * @param seily
	 *            Relative y-coordinates of rope-clipping-point
	 * @param xscale
	 *            The scale of x-coordinates
	 * @param y
	 *            y-position
	 * @param x
	 *            x-position
	 */
	public Element(int x, int y, int width, int height, int seilx, int seily, double scale, double xscale, double yscale, String iconpath,
			Aufzugschacht aufzugschacht) {
		setSize(width, height);
		setLocation(x, y);
		this.aufzugschacht = aufzugschacht;
		this.iconPath = iconpath;
		if (iconpath != null) {
			imageIcon = new ImageIcon(Helper.getFileURL(iconpath));
		}
		this.scale = scale;
		this.xscale = xscale;
		this.yscale = yscale;
		xRel = (int) ((double) x / xscale);
		yRel = (int) ((double) y / yscale);
		widthRel = (int) ((double) width / scale);
		heightRel = (int) ((double) height / scale);
		seilxRel = (int) ((double) seilx / xscale);
		seilyRel = (int) ((double) seily / yscale);
		initListener();
	}

	/**
	 * Gets the default ID. Could be overriden by inheriting class
	 * 
	 * @return the ID
	 */
	public int getID() {
		return 0;
	}

	/**
	 * Inits the listeners.
	 */
	public void initListener() {
		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				mouseClickedElement(evt);
			}

			public void mouseReleased(MouseEvent evt) {
				mouseReleasedElement(evt);
			}
		});
		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseMoved(MouseEvent evt) {
				mouseMovedElement(evt);
			}

			public void mouseDragged(MouseEvent evt) {
				mouseDraggedElement(evt);
			}
		});
	}

	/**
	 * Triggered if mouse clicked on element.
	 * 
	 * @param evt
	 *            The MouseEvent
	 */
	@SuppressWarnings("serial")
	private void mouseClickedElement(MouseEvent evt) {

		// if the right mouse button is clicked and no button is selected,
		// create a popup-menu
		if (evt.getButton() == Constants.RIGHT_MOUSE_BUTTON && !aufzugschacht.mainFrameIsAnyButtonSelected) {
			JPopupMenu popup = new JPopupMenu("Label");
			JMenuItem delete = null;
			delete = new JMenuItem(new AbstractAction("L" + Constants.OE_K + "schen", new ImageIcon(Helper.getFileURL(Constants.DELETE_ICON))) {
				public void actionPerformed(ActionEvent e) {
					popupDelete();
				}
			});
			JMenuItem parameter = null;
			parameter = new JMenuItem(new AbstractAction("Parameter", new ImageIcon(Helper.getFileURL(Constants.MODIFY_ICON))) {
				public void actionPerformed(ActionEvent e) {
					popupModify();
				}
			});
			JMenuItem parameter2te = null;
			parameter2te = new JMenuItem(new AbstractAction("Parameter (zweite Umschlingung)", new ImageIcon(Helper.getFileURL(Constants.MODIFY_ICON))) {
				public void actionPerformed(ActionEvent e) {
					popupModify2te();
				}
			});
			JMenuItem deactivate = null;
			deactivate = new JMenuItem(new AbstractAction("Deaktivieren", new ImageIcon(Helper.getFileURL(Constants.DEACTIVATE_ICON))) {
				public void actionPerformed(ActionEvent e) {
					popupDeactivate();
				}
			});
			JMenuItem activate = null;
			activate = new JMenuItem(new AbstractAction("Aktivieren", new ImageIcon(Helper.getFileURL(Constants.ACTIVATE_ICON))) {
				public void actionPerformed(ActionEvent e) {
					popupActivate();
				}
			});
			JMenuItem seilAnders = null;
			seilAnders = new JMenuItem(new AbstractAction("Seilverlauf " + Constants.AE_K + "ndern", new ImageIcon(Helper.getFileURL(Constants.CHANGE_ICON))) {
				public void actionPerformed(ActionEvent e) {
					popupChangeSeil();
				}
			});

			// every element but the rope holder has paramters to modify
			if (!(this instanceof Seilaufhaenger)) {
				popup.add(parameter);
				if (this instanceof Rolle) {
					Rolle rolle = (Rolle) this;
					if (rolle.isDoppelteUmschlingung()) {
						popup.add(parameter2te);
					}
				}
				if (this instanceof Umlenkrolle) {
					Rolle rolle = (Rolle) this;
					if (rolle.isActivated())
						popup.add(deactivate);
					else
						popup.add(activate);
				}
			}

			// Rolle anders umschlingen
			if (this instanceof Rolle) {
				Rolle rolle = (Rolle) this;
				if (!rolle.isDoppelteUmschlingung() && rolle.getSeilElement(true) != null && rolle.getSeilElement(true).getPrev() != null
						&& rolle.getSeilElement(true).getPrev().getElement() instanceof Rolle && rolle.getSeilElement(true).getNext() != null
						&& rolle.getSeilElement(true).getNext().getElement() instanceof Rolle) {
					popup.add(seilAnders);
				}
			}

			popup.add(delete);

			// shows the menu if it has any entries
			popup.show(this, evt.getX(), evt.getY());
		}
		// other clicks are given to the parent
		else if (isDoubleClickedElement(evt) && this instanceof Kabine) {
			if (aufzugschacht.kabinenRolleExists()) {
				aufzugschacht.switchToDoppelKabinenRolle();
			} else if (aufzugschacht.kabinenDoppelRolleExists()) {
				aufzugschacht.switchToKabinenRolle();
			}
		} else if (!aufzugschacht.mainFrameIsAnyButtonSelected) {
			if (this instanceof Rolle) {
				Rolle thisRolle = (Rolle) this;
				aufzugschacht.mainFrameActivateTabbedPane(thisRolle);
			}
			if (this instanceof DoppelUmlenkrolle) {
				DoppelUmlenkrolle thisRolle = (DoppelUmlenkrolle) this;
				aufzugschacht.mainFrameActivateTabbedPane(thisRolle.getRolle1());
			}
		} else
			aufzugschacht.mouseClickedArea(evt);
	}

	/**
	 * Checks if element is clicked with a double-click.
	 * 
	 * @param evt
	 *            the MouseEvent
	 * 
	 * @return true, if element is double-clicked
	 */
	private boolean isDoubleClickedElement(MouseEvent evt) {
		if (evt.getButton() == Constants.LEFT_MOUSE_BUTTON && !aufzugschacht.mainFrameIsAnyButtonSelected) {
			Calendar thisTime = Calendar.getInstance();
			if (clicked) {
				long millis = thisTime.getTimeInMillis();
				if (millis - oldTime.getTimeInMillis() < 300) {
					clicked = false;
					oldTime = thisTime;
					return true;
				}
				oldTime = thisTime;
			} else {
				clicked = true;
				oldTime = thisTime;
			}
		}
		return false;
	}

	/**
	 * Triggered if 'delete' is clicked at popup-menu.
	 */
	private void popupDelete() {
		aufzugschacht.removeElement(this);
	}

	/**
	 * Triggered if 'modify' is clicked at popup-menu.
	 */
	private void popupChangeSeil() {
		((Rolle) this).changeSeil();
		if (aufzugschacht.getSeil() != null) {
			aufzugschacht.repaint();
		}
	}

	/**
	 * Triggered if 'modify' is clicked at popup-menu.
	 */
	private void popupModify() {
		boolean doNotShow = false;
		if (this instanceof Umlenkrolle && ((Umlenkrolle) this).isFree()) {
			doNotShow = true;
		} else if (this instanceof DoppelUmlenkrolle) {
			doNotShow = true;
		}
		aufzugschacht.mainFrameShowOptionsFrame(this, doNotShow);
	}

	/**
	 * Triggered if 'modify' is clicked at popup-menu.
	 */
	private void popupModify2te() {
		Rolle rolle = (Rolle) this;
		aufzugschacht.mainFrameShowOptionsFrame(rolle.getRolle2teUmschlingung(), true);
	}

	/**
	 * Popup activate.
	 */
	private void popupActivate() {
		Rolle rolle = (Rolle) this;
		rolle.setActivated(true);
		if (aufzugschacht.getSeil() != null && aufzugschacht.getSeil().isElement(this)) {
			aufzugschacht.mainFrameNewTabbedPane(rolle);
		}
	}

	/**
	 * Popup deactivate.
	 */
	private void popupDeactivate() {
		Rolle rolle = (Rolle) this;
		rolle.setActivated(false);
		aufzugschacht.mainFrameDeleteTabbedPane(rolle);
	}

	/**
	 * Triggered if mouse is moved over element.
	 * 
	 * @param evt
	 *            The MouseEvent
	 */
	private void mouseMovedElement(MouseEvent evt) {
		aufzugschacht.mouseMovedArea(evt);
	}

	/**
	 * Triggered if mouse button released.
	 * 
	 * @param evt
	 *            The MouseEvent
	 */
	private void mouseReleasedElement(MouseEvent evt) {
		// if the element is beeing dragged firstMove is set
		if (dragging && draggable) {
			firstMove = true;
			dragging = false;
		} else if (dragging) {
			// If the mouse is still on the element -> click is assumed to be the right thing
			if (isUnderMouse(evt.getX(), evt.getY())) {
				mouseClickedElement(evt);
			}
		}
	}

	public void setDraggable(boolean draggable) {
		this.draggable = draggable;
	}

	/**
	 * Triggered if element is dragged.
	 * 
	 * @param evt
	 *            The MouseEvent
	 */
	private void mouseDraggedElement(MouseEvent evt) {
		if (draggable) {
			// if the element is rope holder or umlenkrolle wich is not standing
			// alone. then it could not be dragged -> return without doing anything
			if (this instanceof Seilaufhaenger) {
				return;
			} else if (this instanceof Umlenkrolle) {
				Umlenkrolle umlenkrolle = (Umlenkrolle) this;
				if (umlenkrolle.isFree())
					return;
			} else if (this instanceof DoppelUmlenkrolle) {
				return;
			}
			if (!dragging) {
				aufzugschacht.eraseCircRect();
				dragging = true;
			}
			int mx = evt.getX();
			int my = evt.getY();
			if (firstMove) {
				// at first move the old-mx and -my are set to the actual mx and my
				omx = mx;
				omy = my;
				firstMove = false;
			}
			// the difference between old and new mouse coordinates
			int dx = mx - omx;
			int dy = my - omy;

			// the old coordintes of the element
			int ox = getX();
			int oy = getY();

			// getting the new coordinates of the element
			Point xy;
			if (this instanceof Seilaufhaenger)
				xy = getDraggingXYAufhaenger(ox, oy, dx, dy);
			else
				xy = getDraggingXY(ox, oy, dx, dy);
			int x = (int) xy.getX();
			int y = (int) xy.getY();

			// move only if new coordinates are not on existing element
			int cy = y;
			int ch = getHeight();
			// if the element has a umlenkrolle or rope holder, than make the
			// searching area bigger
			if (this instanceof EndElement) {
				EndElement endElement = (EndElement) this;
				if (endElement.getUmlenkrolle() != null) {
					cy = cy - endElement.getUmlenkrolle().getHeight();
					ch = ch + endElement.getUmlenkrolle().getHeight();
				}
				if (endElement.getDoppelUmlenkrolle() != null) {
					cy = cy - endElement.getDoppelUmlenkrolle().getHeight();
					ch = ch + endElement.getDoppelUmlenkrolle().getHeight();
				}
				if (endElement.getSeilaufhaenger() != null) {
					cy = cy - endElement.getSeilaufhaenger().getHeight();
					ch = ch + endElement.getSeilaufhaenger().getHeight();
				}
			}
			Vector<Element> elementVector = aufzugschacht.getElementAt(x, cy, getWidth(), ch);
			boolean onlyElement = true;
			for (int i = 0; i < elementVector.size(); i++) {
				if (!elementVector.elementAt(i).equals(this))
					if (this instanceof EndElement) {
						// the own clipped elements does not interfere the own move
						EndElement endElement = (EndElement) this;
						if (!elementVector.elementAt(i).equals(endElement.getSeilaufhaenger())
								&& !elementVector.elementAt(i).equals(endElement.getUmlenkrolle())
								&& !elementVector.get(i).equals(endElement.getDoppelUmlenkrolle()))
							onlyElement = false;
					} else
						onlyElement = false;
			}
			if (onlyElement) {
				// if the new location is not reserved by another element, the
				// element
				// is moved there and the rope is repainted
				setLocation(x, y);
				xRel = (int) ((double) x / xscale);
				yRel = (int) ((double) y / yscale);
				omx = mx - dx;
				omy = my - dy;
				if (aufzugschacht.getSeil() != null) {
					aufzugschacht.getSeil().refreshSeil();
					aufzugschacht.getSeil().repaint();
				}
			}
		} else {
			dragging = true;
		}
	}

	/**
	 * Override the paint-method.
	 * 
	 * @param g
	 *            The Graphics object
	 */
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (imageIcon == null) {
			imageIcon = new ImageIcon(Helper.getFileURL(iconPath));
		}
		Image image = imageIcon.getImage();
		g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
	}

	/**
	 * Sets all scale parameters and scales the element to the new size. The new position is also calculated
	 * 
	 * @param scale
	 *            The scale of width and height
	 * @param yscale
	 *            The scale of the y-coordinates
	 * @param xscale
	 *            The scale of the x-coordinates
	 */
	public void setScale(double scale, double xscale, double yscale) {
		if (this.scale != scale || this.xscale != xscale || this.yscale != yscale) {
			this.scale = scale;
			this.xscale = xscale;
			this.yscale = yscale;

			// calculate the absolute coordinates and dimensions on the base
			// of the given scale parameters
			int x = (int) (xRel * xscale);
			int y = (int) (yRel * yscale);
			int width = (int) (widthRel * scale);
			int height = (int) (heightRel * scale);

			// the border rope holders must always be at the border
			// small rounding failures are eliminated here
			if (this instanceof Seilaufhaenger) {
				Seilaufhaenger aufhaenger = (Seilaufhaenger) this;
				if (!aufhaenger.isUnderTreibscheibe()) {
					if (aufhaenger.getHimmel() == Seilaufhaenger.NORD) {
						if (x > aufzugschacht.getWidth() - width - 1)
							x = aufzugschacht.getWidth() - width - 1;
						if (x < 1)
							x = 1;
						y = 1;
					} else if (aufhaenger.getHimmel() == Seilaufhaenger.SUED) {
						if (x > aufzugschacht.getWidth() - width - 1)
							x = aufzugschacht.getWidth() - width - 1;
						if (x < 1)
							x = 1;
						y = aufzugschacht.getHeight() - height - 1;
					} else if (aufhaenger.getHimmel() == Seilaufhaenger.WEST) {
						if (y > aufzugschacht.getHeight() - width - 1)
							y = aufzugschacht.getHeight() - height - 1;
						if (y < 1)
							y = 1;
						x = 1;
					} else {
						if (y > aufzugschacht.getHeight() - width - 1)
							y = aufzugschacht.getHeight() - height - 1;
						if (y < 1)
							y = 1;
						x = aufzugschacht.getWidth() - width - 1;
					}
					// moves and resizes the element
					setSize(width, height);
					setLocation(x, y);
				} else {
					if (x > aufzugschacht.getWidth() - width - 1)
						x = aufzugschacht.getWidth() - width - 1;
					if (x < 1)
						x = 1;
					// moves and resizes the element
					setSize(width, height);
					// setLocation(x, y);
				}
			} else {
				if (x > aufzugschacht.getWidth() - width - 1)
					x = aufzugschacht.getWidth() - width - 1;
				if (y > aufzugschacht.getHeight() - width - 1)
					y = aufzugschacht.getHeight() - height - 1;
				if (x < 1)
					x = 1;
				if (y < 1)
					y = 1;
				// moves and resizes the element
				setSize(width, height);
				setLocation(x, y);
			}
		}
	}

	/**
	 * Checks if the element is under the given mouse coordinates.
	 * 
	 * @param y
	 *            The mouse y-coordinates
	 * @param x
	 *            The mouse x-coordinates
	 * 
	 * @return True if the element is under the mouse coordinates
	 */
	public boolean isUnderMouse(int x, int y) {
		if (x >= getX() && x <= getX() + getWidth() && y >= getY() && y <= getY() + getHeight())
			return true;
		else
			return false;
	}

	/**
	 * Gives the x-coordinates of the rope-clipping-point.
	 * 
	 * @return The x-coordinates of the rope-clipping-point
	 */
	public int getSeilX() {
		if (this instanceof Seilaufhaenger) {
			if (((Seilaufhaenger) this).getHangingElement() != null) {
				Element hangingElement = ((Seilaufhaenger) this).getHangingElement();
				return hangingElement.getMPoint().x;
			}
		}
		return (getX() + (int) (seilxRel * xscale));
	}

	/**
	 * Gives the y-coordinates of the rope-clipping-point.
	 * 
	 * @return The y-coordinates of the rope-clipping-point
	 */
	public int getSeilY() {
		return (getY() + (int) ((double) seilyRel * yscale));
	}

	/**
	 * Gives the corrected coordinates for dragging an element, so that the element does not leave the elevator area.
	 * 
	 * @param dx
	 *            The x-difference
	 * @param dy
	 *            The y-difference
	 * @param ox
	 *            The old x-coordinate
	 * @param oy
	 *            The old y-coordinate
	 * 
	 * @return The new corrected Point
	 */
	private Point getDraggingXY(int ox, int oy, int dx, int dy) {
		int x, y;

		// if the element has a rope holder or umlenkrolle on top of its head
		// the element is higher than normal -> extraHeight
		int extraHeight = 0;
		if (this instanceof EndElement) {
			EndElement endElement = (EndElement) this;
			if (endElement.getUmlenkrolle() != null)
				extraHeight = endElement.getUmlenkrolle().getHeight();
			if (endElement.getSeilaufhaenger() != null)
				extraHeight = endElement.getSeilaufhaenger().getHeight();
		}

		// if the old coordinate + difference is not outside the area border it
		// is okay
		// otherwise the coordinates are set at the border
		if (ox + dx >= 1 && ox + dx <= aufzugschacht.getWidth() - 1 - getWidth())
			x = ox + dx;
		else if (ox + dx < 1)
			x = 1;
		else
			x = aufzugschacht.getWidth() - 1 - getWidth();

		if (oy + dy - extraHeight >= 1 && oy + dy <= aufzugschacht.getHeight() - 1 - getHeight())
			y = oy + dy;
		else if (oy + dy - extraHeight < 1)
			y = 1 + extraHeight;
		else
			y = aufzugschacht.getHeight() - 1 - getHeight();

		return new Point(x, y);
	}

	/**
	 * Gives the corrected coordinates for dragging an rope-holder, so that the rope-holder does not leave the elevator area.
	 * 
	 * @param dx
	 *            The x-difference
	 * @param dy
	 *            The y-difference
	 * @param ox
	 *            The old x-coordinate
	 * @param oy
	 *            The old y-coordinate
	 * 
	 * @return The new corrected Point
	 */
	private Point getDraggingXYAufhaenger(int ox, int oy, int dx, int dy) {
		int x, y;
		Seilaufhaenger aufhaenger = (Seilaufhaenger) this;

		// rope holder can only be moved at its border line
		// horizontally or vertically
		if (aufhaenger.getHimmel() == Seilaufhaenger.WEST || aufhaenger.getHimmel() == Seilaufhaenger.OST) {
			x = ox;
			if (oy + dy >= 0 && oy + dy <= aufzugschacht.getHeight() - 2 - getHeight() - 1)
				y = oy + dy;
			else if (oy + dy < 0)
				y = 1;
			else
				y = aufzugschacht.getHeight() - 2 - getHeight() - 2;
		} else {
			y = oy;
			if (ox + dx >= 0 && ox + dx <= aufzugschacht.getWidth() - 2 - getWidth() - 1)
				x = ox + dx;
			else if (ox + dx <= 0)
				x = 1;
			else
				x = aufzugschacht.getWidth() - 2 - getWidth() - 2;
		}

		return new Point(x, y);
	}

	/**
	 * Gets the middle point.
	 * 
	 * @return the middle point of the element
	 */
	public Point getMPoint() {
		return new Point(getX() + (getWidth() / 2), getY() + (getHeight() / 2));
	}

	/**
	 * Gets the seil element.
	 * 
	 * @param first
	 *            get the first one?
	 * 
	 * @return the first seilElement if first is true. otherwise the last element
	 */
	public SeilElement getSeilElement(boolean first) {
		if (seilElementVector.size() == 0) {
			return null;
		} else if (first) {
			return seilElementVector.get(0);
		} else {
			return seilElementVector.get(seilElementVector.size() - 1);
		}
	}

	/**
	 * Sets the seil element.
	 * 
	 * @param seilElement
	 *            the seilElement to set
	 */
	public void setSeilElement(SeilElement seilElement) {
		seilElementVector.add(seilElement);
	}

	/**
	 * Reset seil elements.
	 */
	public void resetSeilElements() {
		seilElementVector.clear();
	}

	/**
	 * Sets the image of the element.
	 * 
	 * @param path
	 *            the image path
	 */
	public void setImage(String path) {
		imageIcon = new ImageIcon(Helper.getFileURL(path));
	}

	/**
	 * Gets the aufzugschacht.
	 * 
	 * @return the aufzugschacht
	 */
	public Aufzugschacht getAufzugschacht() {
		return aufzugschacht;
	}

	/**
	 * Gets the yscale.
	 * 
	 * @return the yscale
	 */
	public double getYscale() {
		return yscale;
	}

}
