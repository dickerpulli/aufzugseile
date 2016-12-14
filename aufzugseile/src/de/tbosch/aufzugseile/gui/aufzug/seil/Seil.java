package de.tbosch.aufzugseile.gui.aufzug.seil;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import de.tbosch.aufzugseile.gui.aufzug.Aufzugschacht;
import de.tbosch.aufzugseile.gui.aufzug.DoppelUmlenkrolle;
import de.tbosch.aufzugseile.gui.aufzug.Element;
import de.tbosch.aufzugseile.gui.aufzug.Rolle;
import de.tbosch.aufzugseile.gui.aufzug.Treibscheibe;
import de.tbosch.aufzugseile.gui.aufzug.Umlenkrolle;
import de.tbosch.aufzugseile.utils.Constants;
import de.tbosch.aufzugseile.utils.Helper;

// TODO: Auto-generated Javadoc
/**
 * The class of the rope.
 * 
 * @author Thomas Bosch (tbosch@gmx.de)
 */
// TODO: comment
public class Seil extends JComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2940860007490385716L;

	/** The vector of element hanging on the rope. */
	private Vector<Element> elementVector = new Vector<Element>();

	/** The seil element vector. */
	private Vector<SeilElement> seilElementVector = new Vector<SeilElement>();

	/** The graphical coordinates of the rope. */
	private Vector<Point> pointVector = new Vector<Point>();

	/** The elevator itself. */
	private Aufzugschacht aufzugschacht;

	/** The count of concatinated ropes. */
	private int count;

	/** The D : nenndurchmesser in millimeters. */
	private double nenndurchmesser;

	/** The l : biegelaenge in millimeters. */
	private int biegelaenge;

	/**
	 * Creates a new instance of Seil.
	 * 
	 * @param aufzugschacht
	 *            The elevator itself
	 */
	public Seil(Aufzugschacht aufzugschacht) {
		this.aufzugschacht = aufzugschacht;
		setLocation(0, 0);
		setSize(aufzugschacht.getSize());
		// defaults rope
		setCount(8);
		setNenndurchmesser(10);
		setBiegelaenge(6000);
		initListener();
	}

	/**
	 * Initialises the listener for this component.
	 */
	public void initListener() {
		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				mouseClickedSeil(evt);
			}

			public void mouseExited(MouseEvent evt) {
				mouseExitedElement(evt);
			}
		});
		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseMoved(MouseEvent evt) {
				mouseMovedSeil(evt);
			}
		});
	}

	/**
	 * Triggered if mouse exited seil area.
	 * 
	 * @param evt
	 *            The MouseEvent
	 */
	private void mouseExitedElement(MouseEvent evt) {
		aufzugschacht.mouseExitedArea(evt);
	}

	/**
	 * Triggered if mouse moved over rope area.
	 * 
	 * @param evt
	 *            The MouseEvent
	 */
	private void mouseMovedSeil(MouseEvent evt) {
		aufzugschacht.mouseMovedArea(evt);
	}

	/**
	 * Triggered if mouse button clicked on rope-area.
	 * 
	 * @param evt
	 *            The MouseEvent
	 */
	@SuppressWarnings("serial")
	public void mouseClickedSeil(MouseEvent evt) {
		if (evt.getButton() == Constants.RIGHT_MOUSE_BUTTON && mouseOverSeil(evt.getX(), evt.getY())) {
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
			popup.add(parameter);
			popup.add(delete);
			popup.show(this, evt.getX(), evt.getY());
		} else {
			aufzugschacht.mouseClickedArea(evt);
		}
	}

	/**
	 * Triggered if popup activates 'delete'.
	 */
	private void popupDelete() {
		aufzugschacht.removeSeil();
	}

	/**
	 * Popup modify.
	 */
	private void popupModify() {
		aufzugschacht.mainFrameShowSeilOptionsFrame(this);
	}

	/**
	 * Overrides the paint-method.
	 * 
	 * @param g
	 *            The Graphics object
	 */
	public void paint(Graphics g) {
		super.paint(g);
		createPolygonWithElements();
		Point prevPoint;
		if (pointVector.size() > 0) {
			prevPoint = pointVector.elementAt(0);
			for (int i = 1; i < pointVector.size(); i++) {
				Line2D line = new Line2D.Double(prevPoint, pointVector.get(i));
				g.drawLine((int) line.getX1(), (int) line.getY1(), (int) line.getX2(), (int) line.getY2());
				prevPoint = pointVector.get(i);
			}
		}
		refreshUmschlingungswinkel();
	}

	/**
	 * Adds an element to the rope.
	 * 
	 * @param element
	 *            The element to add
	 */
	public void addElement(Element element) {
		if (!elementVector.contains(element)) {
			element.resetSeilElements();
			if (element instanceof Rolle) {
				Rolle rolle = (Rolle) element;
				if (rolle.isActivated()) {
					aufzugschacht.mainFrameNewTabbedPane(rolle);
				}
			}
		}

		elementVector.add(element);
		SeilElement seilElement = new SeilElement(element, this);
		element.setSeilElement(seilElement);
		seilElementVector.add(seilElement);
		int index = seilElementVector.indexOf(seilElement);
		if (seilElementVector.size() > 1) {
			seilElementVector.get(index - 1).setNext(seilElement);
			seilElement.setPrev(seilElementVector.get(index - 1));
		}
		aufzugschacht.setLast(element);
	}

	/**
	 * Adds the all tabbed panes after loading an alb-file.
	 */
	public void addAllTabbedPanes() {
		for (int i = 0; i < elementVector.size(); i++) {
			if (elementVector.get(i) instanceof Rolle && ((Rolle) elementVector.get(i)).isActivated()) {
				aufzugschacht.mainFrameNewTabbedPane((Rolle) elementVector.get(i));
			} else if (elementVector.get(i) instanceof DoppelUmlenkrolle && ((DoppelUmlenkrolle) elementVector.get(i)).isActivated()) {
				aufzugschacht.mainFrameNewTabbedPane(((DoppelUmlenkrolle) elementVector.get(i)).getRolle1());
				aufzugschacht.mainFrameNewTabbedPane(((DoppelUmlenkrolle) elementVector.get(i)).getRolle2());
			}
		}
	}

	/**
	 * Replace an element by another one.
	 * 
	 * @param element
	 *            the element
	 * @param toReplace
	 *            the element to replace
	 */
	public void replaceElement(Element element, Element toReplace) {
		if (elementVector.contains(toReplace)) {
			SeilElement seilElement = new SeilElement(element, this);
			seilElement.setPrev(toReplace.getSeilElement(true).getPrev());
			toReplace.getSeilElement(true).getPrev().setNext(seilElement);
			seilElement.setNext(toReplace.getSeilElement(true).getNext());
			toReplace.getSeilElement(true).getNext().setPrev(seilElement);
			int index = seilElementVector.indexOf(toReplace.getSeilElement(true));
			seilElementVector.remove(index);
			seilElementVector.add(index, seilElement);
			int index2 = elementVector.indexOf(toReplace);
			elementVector.remove(index2);
			elementVector.add(index2, element);
			element.setSeilElement(seilElement);
		}
	}

	/**
	 * Removes an element from the rope.
	 * 
	 * @param element
	 *            The element to remove
	 */
	public void removeElement(Element element) {
		if (elementVector.contains(element)) {
			elementVector.remove(element);
			int len = seilElementVector.size();
			for (int i = 0; i < len; i++) {
				if (seilElementVector.get(i).getElement().equals(element)) {
					seilElementVector.remove(i);
					if (seilElementVector.size() != 0) {
						if (i == 0) {
							seilElementVector.get(i).setPrev(null);
						} else if (i == len - 1) {
							seilElementVector.get(i - 1).setNext(null);
						} else {
							seilElementVector.get(i).setPrev(seilElementVector.get(i - 1));
							seilElementVector.get(i - 1).setNext(seilElementVector.get(i));
						}
					}
					len--;
				}
			}
			if (element instanceof Rolle && ((Rolle) element).isDoppelteUmschlingung()) {
				removeElement(element);
			}
		}
	}

	/**
	 * Creates the graphical polygon wich interpretes the rope.
	 */
	private void createPolygonWithElements() {
		pointVector.clear();
		for (int i = 0; i < seilElementVector.size(); i++) {
			pointVector.add(seilElementVector.get(i).getInPoint());
			pointVector.add(seilElementVector.get(i).getOutPoint());
			if (seilElementVector.get(i).getElement() instanceof Rolle) {
				Rolle rolle = (Rolle) seilElementVector.get(i).getElement();
				aufzugschacht.mainFrameRefreshTabbedPane(rolle);
			}
			if (seilElementVector.get(i).getElement() instanceof DoppelUmlenkrolle) {
				aufzugschacht.mainFrameRefreshTabbedPane(((DoppelUmlenkrolle) seilElementVector.get(i).getElement()).getRolle1());
				aufzugschacht.mainFrameRefreshTabbedPane(((DoppelUmlenkrolle) seilElementVector.get(i).getElement()).getRolle2());
			}
		}
	}

	/**
	 * Checks if mouse is in a defined area (6x6 rectangle) over the polygon.
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
		if (pointVector.size() > 0) {
			prevPoint = pointVector.get(0);
			for (int i = 0; i < pointVector.size(); i++) {
				Line2D line = new Line2D.Double(prevPoint, pointVector.get(i));
				if (line.intersects(mouseRect))
					return true;
				prevPoint = pointVector.get(i);
			}
		}

		return false;
	}

	/**
	 * Return the count of elements on the rope.
	 * 
	 * @return Count of elements
	 */
	public int getElementCount() {
		return elementVector.size();
	}

	/**
	 * Refresh seil.
	 */
	public void refreshSeil() {
		for (int j = 0; j < seilElementVector.size(); j++) {
			for (int i = 0; i < seilElementVector.size(); i++) {
				SeilElement seilElement = seilElementVector.get(i);
				if (seilElement.getElement() instanceof Umlenkrolle) {
					Umlenkrolle rolle = (Umlenkrolle) seilElement.getElement();
					if (rolle.isFree())
						seilElement.letSeilHang();
				}
			}
			for (int i = 0; i < seilElementVector.size(); i++) {
				SeilElement seilElement = seilElementVector.get(i);
				if (seilElement.getElement() instanceof DoppelUmlenkrolle) {
					seilElement.letSeilHang();
				}
			}
			for (int i = 0; i < seilElementVector.size(); i++) {
				if (seilElementVector.get(i).getElement() instanceof Seilaufhaenger) {
					Seilaufhaenger aufhaenger = (Seilaufhaenger) seilElementVector.get(i).getElement();
					if (aufhaenger.isFree())
						seilElementVector.get(i).letSeilHang();
				}
			}
			for (int i = 0; i < seilElementVector.size(); i++) {
				if (seilElementVector.get(i).getElement() instanceof Seilaufhaenger) {
					Seilaufhaenger aufhaenger = (Seilaufhaenger) seilElementVector.get(i).getElement();
					if (!aufhaenger.isFree())
						seilElementVector.get(i).letSeilHang();
				}
			}
		}
	}

	/**
	 * Refresh the umschlingungswinkel of the treibscheibe.
	 */
	public void refreshUmschlingungswinkel() {
		Treibscheibe tr = null;
		double winkelRad = 0;

		for (int j = 0; j < seilElementVector.size(); j++) {
			if (seilElementVector.get(j).getElement() instanceof Treibscheibe) {
				tr = (Treibscheibe) seilElementVector.get(j).getElement();
				winkelRad += seilElementVector.get(j).getUmschlingungswinkel();
			}
		}

		if (tr != null && winkelRad != 0) {
			double winkelDeg = Math.toDegrees(winkelRad);
			String winkelString = Double.toString(winkelDeg);
			if (winkelString.indexOf(".") != -1) {
				if (winkelString.substring(winkelString.indexOf(".")).length() > 3) {
					winkelString = winkelString.substring(0, winkelString.indexOf(".") + 3);
				}

			}
			aufzugschacht.mainFrameRefreshUmschlWinkel(winkelDeg);
			if (!tr.isDoppelteUmschlingung()) {
				tr.setToolTipText("<html>" + tr.getName() + tr.getID() + "<br>" + winkelString + "\u00b0</html>");
			} else {
				tr.setToolTipText("<html>" + tr.getName() + tr.getID() + "/" + tr.getRolle2teUmschlingung().getName() + tr.getRolle2teUmschlingung().getID()
						+ "<br>" + winkelString + "\u00b0</html>");
			}
		}
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
	 * Gets the count of ropes in one big rope.
	 * 
	 * @return the count
	 */
	public int getCount() {
		return count;
	}

	/**
	 * Sets the count of ropes in one big rope.
	 * 
	 * @param count
	 *            the count to set
	 */
	public void setCount(int count) {
		this.count = count;
	}

	/**
	 * Checks if is element.
	 * 
	 * @param element
	 *            the element
	 * 
	 * @return true, if is element
	 */
	public boolean isElement(Element element) {
		if (elementVector.contains(element))
			return true;
		return false;
	}

	/**
	 * Gets the biegelaenge.
	 * 
	 * @return the biegelaenge
	 */
	public int getL() {
		return biegelaenge;
	}

	/**
	 * Sets the biegelaenge.
	 * 
	 * @param biegelaenge
	 *            the biegelaenge to set
	 */
	public void setBiegelaenge(int biegelaenge) {
		this.biegelaenge = biegelaenge;
	}

	/**
	 * Gets the nenndurchmesser in millimeters.
	 * 
	 * @return the nenndurchmesser
	 */
	public double getD() {
		return nenndurchmesser;
	}

	/**
	 * Sets the nenndurchmesser in millimeters.
	 * 
	 * @param nenndurchmesser
	 *            the nenndurchmesser to set
	 */
	public void setNenndurchmesser(double nenndurchmesser) {
		this.nenndurchmesser = nenndurchmesser;
	}

	/**
	 * @return the elementVector
	 */
	public Vector<Element> getElementVector() {
		return elementVector;
	}

	/**
	 * @return the seilElementVector
	 */
	public Vector<SeilElement> getSeilElementVector() {
		return seilElementVector;
	}

	/**
	 * Checks if the elementVector already contains two elements.
	 * 
	 * @param element
	 *            the element
	 * 
	 * @return true, if has already two elements
	 */
	public boolean hasAlreadyTwoElements(Element element) {
		if (elementVector.indexOf(element) == elementVector.lastIndexOf(element))
			return false;
		else
			return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Component#toString()
	 */
	public String toString() {
		String string = "";

		string += "SeilElementVector:\n";
		for (int s = 0; s < seilElementVector.size(); s++) {
			string += seilElementVector.get(s).getElement().getName() + seilElementVector.get(s).getElement().getID();
			if (s < seilElementVector.size() - 1)
				string += "->";
		}

		return string;
	}
}
