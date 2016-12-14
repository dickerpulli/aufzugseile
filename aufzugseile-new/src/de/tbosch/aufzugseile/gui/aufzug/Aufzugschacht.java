package de.tbosch.aufzugseile.gui.aufzug;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.Vector;

import de.tbosch.aufzugseile.gui.utils.AufzugschachtEvent;
import de.tbosch.aufzugseile.gui.utils.AufzugschachtListener;
import de.tbosch.commons.gui.Circle;
import de.tbosch.commons.gui.Rectangle;
import de.tbosch.commons.IDPool;
import de.tbosch.aufzugseile.gui.utils.Constants;
import de.tbosch.commons.gui.GUICommonsConstants;
import de.tbosch.seile.commons.elemente.DoppelUmlenkrolle;
import de.tbosch.seile.commons.elemente.Element;
import de.tbosch.seile.commons.elemente.Rolle;
import de.tbosch.seile.commons.elemente.Schacht;
import de.tbosch.seile.commons.elemente.Seil;
import de.tbosch.seile.commons.elemente.Seilaufhaenger;
import de.tbosch.seile.commons.elemente.Treibscheibe;
import de.tbosch.seile.commons.elemente.Umlenkrolle;
import javax.swing.JPanel;

/**
 * The elevator area including all relevant elements.
 * 
 * @author Thomas Bosch (tbosch@gmx.de)
 */
public class Aufzugschacht extends JPanel implements Schacht {

	/** The maximum number of umlenkrollen. */
	private int maxUmlenkrollen = 10;

	/** The treibscheibe. */
	private AufzugschachtTreibscheibe treibscheibe;

	/** The cabine. */
	private Kabine kabine;

	/** The umlenkrolle sitting on top of the kabine. */
	private AufzugschachtUmlenkrolle kabinenRolle;
	
	/** The second umlenkrolle on the top of the kabine. */
	private AufzugschachtDoppelUmlenkrolle kabinenDoppelRolle;

	/** the gewicht. */
	private Gewicht gewicht;

	/** The umlenkrolle sitting on top of the gewicht. */
	private AufzugschachtUmlenkrolle gewichtsRolle;

	/** The rope. */
	private AufzugschachtSeil seil;

	/** One rope holder. */
	private AufzugschachtSeilaufhaenger seilaufhaenger1;

	/** The other rope holder. */
	private AufzugschachtSeilaufhaenger seilaufhaenger2;

	/** The vector of all drawed elements. */
	private Vector<AufzugschachtElement> existingElements = new Vector<AufzugschachtElement>();

	/** The vector of umlenkrollen. */
	private Vector<Umlenkrolle> umlenkrollenVector = new Vector<Umlenkrolle>();

	/** The initial width for setting scale after resize. */
	private int startwidth;

	/** The initial height. */
	private int startheight;

	/** If there is something placeable under the mouse. */
	private boolean placeable = true;

	/** The scale of width and height. */
	private double scale = 1;

	/** The scale of x-coordinates. */
	private double xscale = 1;

	/** The scale of y-coordinates. */
	private double yscale = 1;

	/** The placing-rectangle for all elements but not umlenkrollen and rope. */
	private Rectangle rectangle;

	/** The placing-circle for the rope and the rope-holders. */
	private Circle circle;
	
	/** The roll count. */
	private int umlenkrolleCount;
	
	/** The roll count. */
	private int treibscheibeCount;
	
	/** The aufhaengung: '2' if 2to1 and '1' if 1to1. */
	private int aufhaengung = 2;
	
	/** The last inserted element at the rope. */
	private AufzugschachtElement last;
	
	// main frame variables set by mainFrame
	/** The main frame is any button selected. */
	public boolean mainFrameIsAnyButtonSelected;
	
	/** The main frame is seil button selected. */
	private boolean mainFrameIsSeilButtonSelected;
	
	/** The main frame is umlenkrolle button selected. */
	private boolean mainFrameIsUmlenkrolleButtonSelected;
	
	/** The main frame is treibscheibe button selected. */
	private boolean mainFrameIsTreibscheibeButtonSelected;
	
	/** The main frame is kabine button selected. */
	private boolean mainFrameIsKabineButtonSelected;
	
	/** The main frame is gewicht button selected. */
	private boolean mainFrameIsGewichtButtonSelected;
	
	/** The main frame marked button. */
	private String mainFrameMarkedButton;
	
	/** The zickzack. */
	private Zickzack zickzack;
	
	/** The ID pool for the umlenkrolle. */
	private IDPool IDPoolUmlenkrolle = new IDPool(16);
	
	/** The ID pool for the treibscheibe. */
	private IDPool IDPoolTreibscheibe = new IDPool(16);
	
	/** Lastgeschwindigkeit */
	private double geschwindigkeit;	
    
    /** The real width of the elevator in millimeters. */
	private int breite;
	
	/** The real hoehe of the elevator in meters. */
	private int hoehe;

	/**
	 * Creates a new instance of Aufzugschacht.
	 * 
	 * @param height the height
	 * @param width the width
	 */
	public Aufzugschacht(int width, int height) {
		setBackground(Constants.BACKGROUND_COLOR);
		setLayout(null);
		setStartValues(width, height);
		initListener();
	}
	
	/**
	 * The Constructor. 
	 * !!! Important !!!!
	 * Use setStartValues() for initializing
	 */
	public Aufzugschacht() {
		setBackground(Constants.BACKGROUND_COLOR);
		setLayout(null);
		initListener();
	}
	
	/**
	 * Sets the start values.
	 * 
	 * @param height the height
	 * @param width the width
	 */
	public void setStartValues(int width, int height) {
		startwidth = width;
		startheight = height;
	}

	/**
	 * Clones the actual elevator and all its elements. It do NOT clone everything,
	 * only those things wich are important for printing the elevator
	 * !!! Only for printing !!!
	 * 
	 * @return the aufzugschacht
	 */
        @Override
	public Aufzugschacht clone() {
		Aufzugschacht copy = new Aufzugschacht(startwidth, startheight);
		copy.breite = new Integer(breite);
		copy.hoehe = new Integer(hoehe);
		copy.setSize(getWidth(), getHeight());
		
		if (treibscheibe != null) {
			copy.treibscheibe = new AufzugschachtTreibscheibe(treibscheibe.getX(), treibscheibe.getY(), treibscheibe.getWidth(), treibscheibe.getHeight(), scale, xscale, yscale, Constants.TREIBSCHEIBE_ELEMENT_ICON_64, copy);
			copy.add(copy.treibscheibe);
			copy.existingElements.add(copy.treibscheibe);
                        AufzugschachtRolle rolle = (AufzugschachtRolle) treibscheibe;
                        AufzugschachtRolle copyrolle = copy.treibscheibe;
			if (rolle.isOtherRope()) {
				copyrolle.setOtherRope(true);
			}
			if (treibscheibe.isDoppelteUmschlingung()) {
				copy.treibscheibe.setDoppelteUmschlingung(true);
			}
		}
		for (int i = 0; i < umlenkrollenVector.size(); i++) {
			AufzugschachtUmlenkrolle rolle = (AufzugschachtUmlenkrolle)umlenkrollenVector.get(i);
			AufzugschachtUmlenkrolle cloneRolle = new AufzugschachtUmlenkrolle(rolle.getX(), rolle.getY(), rolle.getWidth(), rolle.getHeight(), scale, xscale, yscale, Constants.UMLENKROLLE_ELEMENT_ICON_64, copy, rolle.isFree(), (AufzugschachtElement)rolle.getHangingElement(), rolle.isPartOfDoppelUmlenkrolle(), (AufzugschachtDoppelUmlenkrolle)rolle.getParentDoubleRoll());
			copy.umlenkrollenVector.add(cloneRolle);
			copy.add(cloneRolle);
			copy.existingElements.add(cloneRolle);
			if (rolle.isDoppelteUmschlingung()) {
				cloneRolle.setDoppelteUmschlingung(true);
			}
		}
		if (zickzack != null) {
			copy.zickzack = new Zickzack(copy, (double)zickzack.getLocation().y / copy.getHeight(), Constants.ZICKZACK_ICON);
			copy.add(copy.zickzack);
		}
		if (kabine != null) {
			copy.kabine = new Kabine(kabine.getX(), kabine.getY(), kabine.getWidth(), kabine.getHeight(), scale, xscale, yscale, Constants.KABINE_ELEMENT_ICON, copy);
			copy.add(copy.kabine);
			copy.existingElements.add(copy.kabine);
		}
		if (kabinenRolle != null) {
			copy.kabinenRolle = new AufzugschachtUmlenkrolle(kabinenRolle.getX(), kabinenRolle.getY(), kabinenRolle.getWidth(), kabinenRolle.getHeight(), scale, xscale, yscale, Constants.UMLENKROLLE_ELEMENT_ICON_64, copy, kabinenRolle.isFree(), copy.kabine, false, null);
			copy.kabine.setUmlenkrolle(copy.kabinenRolle);
			copy.add(copy.kabinenRolle);
		}
		if (kabinenDoppelRolle != null) {
			copy.kabinenDoppelRolle = new AufzugschachtDoppelUmlenkrolle(kabinenDoppelRolle.getX(), kabinenDoppelRolle.getY(), kabinenDoppelRolle.getWidth(), kabinenDoppelRolle.getHeight(), scale, xscale, yscale, Constants.DOPPELUMLENKROLLE_ELEMENT_ICON_64, copy, copy.kabine);
			AufzugschachtUmlenkrolle rolle1 = new AufzugschachtUmlenkrolle(((AufzugschachtDoppelUmlenkrolle)kabinenDoppelRolle.getRolle1()).getX(), ((AufzugschachtDoppelUmlenkrolle)kabinenDoppelRolle.getRolle1()).getY(), ((AufzugschachtDoppelUmlenkrolle)kabinenDoppelRolle.getRolle1()).getWidth(), ((AufzugschachtDoppelUmlenkrolle)kabinenDoppelRolle.getRolle1()).getHeight(), scale, xscale, yscale, Constants.UMLENKROLLE_ELEMENT_ICON_64, copy, kabinenDoppelRolle.getRolle1().isFree(), copy.kabine, true, (AufzugschachtDoppelUmlenkrolle)kabinenDoppelRolle.getRolle1().getParentDoubleRoll());
			AufzugschachtUmlenkrolle rolle2 = new AufzugschachtUmlenkrolle(((AufzugschachtDoppelUmlenkrolle)kabinenDoppelRolle.getRolle2()).getX(), ((AufzugschachtDoppelUmlenkrolle)kabinenDoppelRolle.getRolle2()).getY(), ((AufzugschachtDoppelUmlenkrolle)kabinenDoppelRolle.getRolle2()).getWidth(), ((AufzugschachtDoppelUmlenkrolle)kabinenDoppelRolle.getRolle2()).getHeight(), scale, xscale, yscale, Constants.UMLENKROLLE_ELEMENT_ICON_64, copy, kabinenDoppelRolle.getRolle2().isFree(), copy.kabine, true, (AufzugschachtDoppelUmlenkrolle)kabinenDoppelRolle.getRolle2().getParentDoubleRoll());
			copy.kabinenDoppelRolle.setRolle1(rolle1);
			copy.kabinenDoppelRolle.setRolle2(rolle2);
			copy.kabine.setDoppelUmlenkrolle(copy.kabinenDoppelRolle);
			copy.add(copy.kabinenDoppelRolle);
		}
		if (gewicht != null) {
			copy.gewicht = new Gewicht(gewicht.getX(), gewicht.getY(), gewicht.getWidth(), gewicht.getHeight(), scale, xscale, yscale, Constants.GEWICHT_ELEMENT_ICON, copy);
			copy.add(copy.gewicht);
			copy.existingElements.add(copy.gewicht);
		}
		if (gewichtsRolle != null) {
			copy.gewichtsRolle = new AufzugschachtUmlenkrolle(gewichtsRolle.getX(), gewichtsRolle.getY(), gewichtsRolle.getWidth(), gewichtsRolle.getHeight(), scale, xscale, yscale, Constants.UMLENKROLLE_ELEMENT_ICON_64, copy, gewichtsRolle.isFree(), copy.gewicht, false, null);
			copy.gewicht.setUmlenkrolle(copy.gewichtsRolle);
			copy.add(copy.gewichtsRolle);
		}
		if (seilaufhaenger1 != null) {
			if (seilaufhaenger1.isFree()) {
				if (seilaufhaenger1.getHangingElement().equals(kabine)) {
					copy.seilaufhaenger1 = new AufzugschachtSeilaufhaenger(seilaufhaenger1.getX(), seilaufhaenger1.getY(), seilaufhaenger1.getWidth(), seilaufhaenger1.getHeight(), scale, xscale, yscale, Constants.AUFHAENGER_ICON, copy, true, copy.kabine, seilaufhaenger1.isUnderTreibscheibe());
					copy.kabine.setSeilaufhaenger(copy.seilaufhaenger1);
				}
				else {
					copy.seilaufhaenger1 = new AufzugschachtSeilaufhaenger(seilaufhaenger1.getX(), seilaufhaenger1.getY(), seilaufhaenger1.getWidth(), seilaufhaenger1.getHeight(), scale, xscale, yscale, Constants.AUFHAENGER_ICON, copy, true, copy.gewicht, seilaufhaenger1.isUnderTreibscheibe());
					copy.gewicht.setSeilaufhaenger(copy.seilaufhaenger1);
				}
			}
			else {
				copy.seilaufhaenger1 = new AufzugschachtSeilaufhaenger(seilaufhaenger1.getX(), seilaufhaenger1.getY(), seilaufhaenger1.getWidth(), seilaufhaenger1.getHeight(), scale, xscale, yscale, Constants.AUFHAENGER_ICON, copy, false, null, seilaufhaenger1.isUnderTreibscheibe());
				copy.existingElements.add(copy.seilaufhaenger1);
			}
			copy.add(copy.seilaufhaenger1);
		}
		if (seilaufhaenger2 != null) {
			if (seilaufhaenger2.isFree()) {
				if (seilaufhaenger2.getHangingElement().equals(kabine)) {
					copy.seilaufhaenger2 = new AufzugschachtSeilaufhaenger(seilaufhaenger2.getX(), seilaufhaenger2.getY(), seilaufhaenger2.getWidth(), seilaufhaenger2.getHeight(), scale, xscale, yscale, Constants.AUFHAENGER_ICON, copy, true, copy.kabine, seilaufhaenger2.isUnderTreibscheibe());
					copy.kabine.setSeilaufhaenger(copy.seilaufhaenger2);
				}
				else {
					copy.seilaufhaenger2 = new AufzugschachtSeilaufhaenger(seilaufhaenger2.getX(), seilaufhaenger2.getY(), seilaufhaenger2.getWidth(), seilaufhaenger2.getHeight(), scale, xscale, yscale, Constants.AUFHAENGER_ICON, copy, true, copy.gewicht, seilaufhaenger2.isUnderTreibscheibe());
					copy.gewicht.setSeilaufhaenger(copy.seilaufhaenger2);
				}
			}
			else {
				copy.seilaufhaenger2 = new AufzugschachtSeilaufhaenger(seilaufhaenger2.getX(), seilaufhaenger2.getY(), seilaufhaenger2.getWidth(), seilaufhaenger2.getHeight(), scale, xscale, yscale, Constants.AUFHAENGER_ICON, copy, false, null, seilaufhaenger2.isUnderTreibscheibe());
				copy.existingElements.add(copy.seilaufhaenger2);
			}
			copy.add(copy.seilaufhaenger2);
		}
		if (seil != null) {
			copy.seil = new AufzugschachtSeil(copy);
			copy.add(copy.seil);
			for (int i = 0; i < seil.getElementVector().size(); i++) {
				Element element = seil.getElementVector().get(i);
				if (element.equals(treibscheibe)) {
					copy.seil.addElement(copy.treibscheibe);
				}
				if (element.equals(treibscheibe.getRolle2teUmschlingung())) {
					copy.seil.addElement(copy.treibscheibe.getRolle2teUmschlingung());
				}
				if (element.equals(kabinenRolle)) {
					copy.seil.addElement(copy.kabinenRolle);
				}
				if (element.equals(kabinenDoppelRolle)) {
					copy.seil.addElement(copy.kabinenDoppelRolle);
				}
				if (element.equals(gewichtsRolle)) {
					copy.seil.addElement(copy.gewichtsRolle);
				}
				if (element.equals(seilaufhaenger1)) {
					copy.seil.addElement(copy.seilaufhaenger1);
				}
				if (element.equals(seilaufhaenger2)) {
					copy.seil.addElement(copy.seilaufhaenger2);
				}
				for (int j = 0; j < umlenkrollenVector.size(); j++) {
					if (element.equals(umlenkrollenVector.get(j))) {
						copy.seil.addElement(copy.umlenkrollenVector.get(j));
					}
					if (element.equals(umlenkrollenVector.get(j).getRolle2teUmschlingung())) {
						copy.seil.addElement(copy.umlenkrollenVector.get(j).getRolle2teUmschlingung());
					}
				}
			}
			seil.refreshSeil();
		}
		return copy;
	}

	/**
	 * Inits the event listeners. Usefull to create the listeners
	 * after loading a new file. It also initializes the existing
	 * elements.
	 */
	public void initListener() {
		addMouseListener(new MouseAdapter() {
            @Override
			public void mouseClicked(MouseEvent evt) {
				mouseClickedArea(evt);
			}

            @Override
			public void mouseExited(MouseEvent evt) {
				mouseExitedArea(evt);
			}
		});
		addMouseMotionListener(new MouseMotionAdapter() {
            @Override
			public void mouseMoved(MouseEvent evt) {
				mouseMovedArea(evt);
			}
		});
		addComponentListener(new ComponentAdapter() {
            @Override
			public void componentResized(ComponentEvent evt) {
				componentResizedArea(evt);
			}
		});
		addContainerListener(new ContainerAdapter() {
            @Override
			public void componentAdded(ContainerEvent evt) {
				if (seil != null)
					setComponentZOrder(seil, getComponentCount() - 1);
			}
		});
		
		// init listeners of included elements
		if (treibscheibe != null) treibscheibe.initListener();
		if (kabine != null) kabine.initListener();
		if (gewicht != null) gewicht.initListener();
		if (seil != null) seil.initListener();
		for (int i = 0; i < umlenkrollenVector.size(); i++) {
			((AufzugschachtUmlenkrolle)umlenkrollenVector.get(i)).initListener();
		}
		if (kabinenRolle != null) kabinenRolle.initListener();
		if (kabinenDoppelRolle != null) kabinenDoppelRolle.initListener();
		if (gewichtsRolle != null) gewichtsRolle.initListener();
		if (seilaufhaenger1 != null) seilaufhaenger1.initListener();
		if (seilaufhaenger2 != null) seilaufhaenger2.initListener();
		if (zickzack != null) zickzack.initListener();
	}
	
	/**
	 * Sets the measures in millimeters (width) and meters (height).
	 * 
	 * @param hoehe the height in meters
	 * @param breite the width in centimeters
	 */
	public void setMeasures(int breite, int hoehe) {
		this.breite = breite * 10;
		this.hoehe = hoehe;
		mainFrameGetBerechnungSetSchachthoehe(hoehe);
		moveAllSeilaufhaenger();
		moveAllUmlenkrolle();
	}
	
	/**
	 * Adds the zickzack.
	 */
	public void addZickzack() {
		zickzack = new Zickzack(this, 0.5, Constants.ZICKZACK_ICON);
		add(zickzack);
		repaint();
	}
	
	/**
	 * Delete zickzack.
	 */
	public void deleteZickzack() {
		remove(zickzack);
		zickzack = null;
		mainFrameChangeButton(Constants.ZICKZACK_NAME, true);
		repaint();
	}

	/**
	 * Triggered if the elevator is resized.
	 * 
	 * @param evt The ComponentEvent
	 */
	private void componentResizedArea(ComponentEvent evt) {
		scaleAllElements();
		sizeAllUmlenkrolle();
		if (seil != null) seil.refreshSeil();
	}

	/**
	 * Overrides paint method.
	 * 
	 * @param g The Graphics object
	 */
    @Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Constants.FOREGROUND_COLOR);
		g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
	}

	/**
	 * Scales all existing elements and rope after resize of the elevator.
	 */
	private void scaleAllElements() {

		// calculate the scale of the window to scale all existing elements
		int diffwidth = getWidth() - startwidth;
		int diffheight = getHeight() - startheight;
		if (diffheight < diffwidth)
			scale = (double) getHeight() / (double) startheight;
		else
			scale = (double) getWidth() / (double) startwidth;
		xscale = (double) getWidth() / (double) startwidth;
		yscale = (double) getHeight() / (double) startheight;

		// scale existing elements
		if (existingElements != null) {
			for (int i = 0; i < existingElements.size(); i++) {
				existingElements.get(i).setScale(scale, xscale, yscale);
			}
		}

		// scale other components
		if (rectangle != null)
			rectangle.setScale(scale);
		if (circle != null)
			circle.setScale(scale);

		if (seil != null) {
			seil.setSize(getSize());
			seil.setLocation(0, 0);
		}
		
		// scale divider
		if (zickzack != null) {
			int yPos = (int)(getHeight() * zickzack.getYRel());
			if (yPos < 1) yPos = 1;
			if (yPos > getHeight() - 1 - zickzack.getHeight()) yPos =  getHeight() - 1 - zickzack.getHeight();
			zickzack.setWidth(getWidth());
			zickzack.setLocation(0, yPos);
		}
	}
    
        /**
	 * Move all elements of umlenkrollenVector in relation to the treibscheibe.
	 */
	public void moveAllUmlenkrolle() {
		if (treibscheibe != null) {
			for (int i = 0; i < umlenkrollenVector.size(); i++) {
				AufzugschachtUmlenkrolle rolle = (AufzugschachtUmlenkrolle)umlenkrollenVector.get(i);
				if (!rolle.isFree()) {
					int nx = treibscheibe.getMPoint().x + ((((AufzugschachtUmlenkrolle)rolle).getHorizEntf()) / getMmPerPixel()) - ((AufzugschachtUmlenkrolle)rolle).getRadius();
					int ny = treibscheibe.getMPoint().y + ((((AufzugschachtUmlenkrolle)rolle).getVertEntf()) / getMmPerPixel()) - ((AufzugschachtUmlenkrolle)rolle).getRadius();
					rolle.setLocation(nx, ny);
				}
			}
		}
		if (seil != null) seil.refreshSeil();
		repaint();
	}
    	
    /**
	 * Size all umlenkrolle.
	 */
	public void sizeAllUmlenkrolle() {
		if (treibscheibe != null) {
			for (int i = 0; i < umlenkrollenVector.size(); i++) {
				AufzugschachtUmlenkrolle rolle = (AufzugschachtUmlenkrolle)umlenkrollenVector.get(i);
				if (!rolle.isFree()) {
					int nw = treibscheibe.getWidth() * rolle.getDurchmesser() / treibscheibe.getDurchmesser();
					int nh = treibscheibe.getHeight() * rolle.getDurchmesser() / treibscheibe.getDurchmesser();
					rolle.setSize(nw, nh);
				}
			}
			if (kabinenRolle != null) {
				int nw = treibscheibe.getWidth() * kabinenRolle.getDurchmesser() / treibscheibe.getDurchmesser();
				int nh = treibscheibe.getHeight() * kabinenRolle.getDurchmesser() / treibscheibe.getDurchmesser();
				kabinenRolle.setSize(nw, nh);
			}
			if (gewichtsRolle != null) {
				int nw = treibscheibe.getWidth() * gewichtsRolle.getDurchmesser() / treibscheibe.getDurchmesser();
				int nh = treibscheibe.getHeight() * gewichtsRolle.getDurchmesser() / treibscheibe.getDurchmesser();
				gewichtsRolle.setSize(nw, nh);
			}
			if (kabinenDoppelRolle != null) {
				int nw = treibscheibe.getWidth() * kabinenDoppelRolle.getDurchmesser() / treibscheibe.getDurchmesser();
				int nh = treibscheibe.getHeight() * kabinenDoppelRolle.getDurchmesser() / treibscheibe.getDurchmesser();
				kabinenDoppelRolle.setSize(nw*2, nh);
			}
		}
		moveAllUmlenkrolle();
	}

	/**
	 * Triggered if mouse exited elevator area.
	 * Delete placing circle of
	 * 
	 * @param evt The MouseEvent
	 */
	public void mouseExitedArea(MouseEvent evt) {
		eraseCircRect();
	}

	/**
	 * Triggered if mouse is moving on the elevator area.
	 * 
	 * @param evt The MouseEvent
	 */
	public void mouseMovedArea(MouseEvent evt) {
		int mx, my;

		// if the event comes from an Element, than add
		// the element coordinates to the event coordinates
		if (evt.getSource() instanceof AufzugschachtElement) {
			AufzugschachtElement element = (AufzugschachtElement) evt.getSource();
			mx = evt.getX() + element.getX();
			my = evt.getY() + element.getY();
		} else {
			mx = evt.getX();
			my = evt.getY();
		}

		// calculate the new coordinates for the placing rectangle or circle
		Point xy;
		xy = getNewXY(mx, my);
		int x = (int) xy.getX();
		int y = (int) xy.getY();

		// only draw placing element if any button is selected
		if (mainFrameIsAnyButtonSelected && !mainFrameIsSeilButtonSelected) {
			// if no rope has to be placed the pointer is a rectangle, small or
			// big
			Vector<Element> elementVector;
			Rectangle rect;
			Dimension rectDim = getDimension();

			// create rectangle in the dimension of the actual clicked button's element
			elementVector = getElementAt(x, y, rectDim.width, rectDim.height);
			if (rectangle == null) {
				rectangle = new Rectangle(rectDim.width, rectDim.height, scale);
				add(rectangle, 0);
			} else {
				rectangle.setSize(rectDim.width, rectDim.height);
			}
			rectangle.unerase();
			rect = rectangle;

			// set the color in dependence of the elements under the rectangle
			if (elementVector != null && elementVector.size() == 0) {
				rect.setAbleColor();
				placeable = true;
			} else {
				rect.setUnableColor();
				placeable = false;
			}

			rect.setLocation(x, y);
		} else if (mainFrameIsSeilButtonSelected) {
			// create the rope
			Element element = getElementAt(mx, my);
			if (circle == null) {
				circle = new Circle((int) (Constants.SEILAUFHAENGER_SIZE_W * scale), (int) (Constants.SEILAUFHAENGER_SIZE_H * scale), scale);
				add(circle, 0);
			} else
				circle.unerase();

			// set the color in dependence of the elements under the circle
			// heavy :-) ... I know
			// TODO: describe the decisions
			if (element != null && (element.equals(last)
					|| (element instanceof Seilaufhaenger)
					|| ((element instanceof Umlenkrolle && ((Umlenkrolle)element).isFree()) || element instanceof DoppelUmlenkrolle)
					|| (element instanceof Rolle && !rollAllowed())
					|| (last != null && (element.equals(kabine) && (last.equals(gewichtsRolle) || last.equals(kabinenRolle) || last.equals(kabinenDoppelRolle))))
					|| (last != null && (element.equals(gewicht) && (last.equals(gewichtsRolle) || last.equals(kabinenRolle) || last.equals(kabinenDoppelRolle))))
					|| (last != null && (element.equals(gewicht) && last instanceof Seilaufhaenger && ((Seilaufhaenger)last).isFree()))
					|| (element.equals(kabine) && (gewichtsRolle != null || (seilaufhaenger1 != null && aufhaengung == 1)) && seil != null && !seil.isElement(treibscheibe))
					|| (element.equals(gewicht) && (kabinenRolleExists() || kabinenDoppelRolleExists() || (seilaufhaenger1 != null && aufhaengung == 1)) && seil != null && !seil.isElement(treibscheibe))
					|| (element.equals(kabine) && seilaufhaenger1 != null && seilaufhaenger1.getHangingElement() != null && seilaufhaenger1.getHangingElement().equals(kabine))
					|| (element.equals(gewicht) && seilaufhaenger1 != null && seilaufhaenger1.getHangingElement() != null && seilaufhaenger1.getHangingElement().equals(gewicht))
					|| (element.equals(kabine) && seil != null && ((kabinenRolleExists() && seil.isElement(kabinenRolle)) || (kabinenDoppelRolleExists() && seil.isElement(kabinenDoppelRolle))))
					|| (element.equals(gewicht) && seil != null && (gewichtsRolle != null && seil.isElement(gewichtsRolle)))
					|| (element instanceof Rolle && last != null && last instanceof Rolle && ((Rolle)last).isDoppelteUmschlingung() && seil.hasAlreadyTwoElements(element))
					//|| (element instanceof Umlenkrolle && last != null && ((last.equals(kabinenRolle) || last.equals(kabinenDoppelRolle)) || (last instanceof Seilaufhaenger && ((Seilaufhaenger)last).getHangingElement() != null && ((Seilaufhaenger)last).getHangingElement().equals(kabine))) ) 
					//|| (last != null && last.equals(treibscheibe) && !element.equals(kabine) && kabine != null && !seil.isElement(kabine.getSeilaufhaenger()) && !seil.isElement(kabine.getUmlenkrolle()) && !seil.isElement(kabine.getDoppelUmlenkrolle()) )
					)) {
				circle.setUnableColor();
				placeable = false;
			}
			else if (isOnBorder(x, y) == 2 && element != null) {
				// circle is under treibscheibe and over an element
				circle.setAbleColor();
				placeable = true;
			}
			else if (element == null && isOnBorder(x, y) == 0) {
				// circle is not at border and not over an element
				circle.setUnableColor();
				placeable = false;
			}
			else if (isOnBorder(x, y) != 0 && (last == null
					|| (last.equals(gewichtsRolle) && (kabinenRolleExists() || kabinenDoppelRolleExists()))
					|| ((last.equals(kabinenRolle) || last.equals(kabinenDoppelRolle)) && gewichtsRolle != null))) {
				// circle is on border
				circle.setOnBorderColor();
				placeable = true;
			}
			else if (isOnBorder(x, y) != 0) {
				// circle is on border or under treibscheibe
				circle.setUnableColor();
				placeable = false;
			}			
			else {
				// otherwise: ABLE
				circle.setAbleColor();
				placeable = true;
			}

			circle.setLocation(x, y);
		}
	}
	
	/**
	 * Roll allowed.
	 * 
	 * @return true, if roll allowed
	 */
	private boolean rollAllowed() {
		if ((gewichtsRolle != null ^ (kabinenRolleExists() || kabinenDoppelRolleExists())) || aufhaengung == 1) {
			return true;
		}
		return false;
	}

	/**
	 * Checks if the given coordinates are on the border.
	 * 
	 * @param y The y-coordinate
	 * @param x The x-coordinate
	 * 
	 * @return 0 if (x,y) is not on border, 1 if is one border, 2 if under the treibscheibe
	 */
	private int isOnBorder(int x, int y) {
		if (y < 2) {
			// on the upper border
			return 1;
		}
		else if (treibscheibe != null) {
			if (y > treibscheibe.getY() + treibscheibe.getHeight() + 10 
					&& y < treibscheibe.getY() + treibscheibe.getHeight() + 20) {
				// under the treibscheibe
				return 2;
			}
		}
		return 0;
	}

	/**
	 * Triggered if mouse is clicked on the elevator area.
	 * 
	 * @param evt The MouseEvent
	 */
	public void mouseClickedArea(MouseEvent evt) {
		int mx, my;

		// if the event comes from an Element, than add
		// the element coordinates to the event coordinates
		if (evt.getSource() instanceof AufzugschachtElement) {
			AufzugschachtElement element = (AufzugschachtElement) evt.getSource();
			mx = evt.getX() + element.getX();
			my = evt.getY() + element.getY();
		} else {
			mx = evt.getX();
			my = evt.getY();
		}

		Point xy;
		xy = getNewXY(mx, my);
		int x = (int) xy.getX();
		int y = (int) xy.getY();

		// if the area under the mouse is okay, that means there is no other
		// element
		// under the mouse, new element can be added
		if (placeable) {
			if (getMarkedButton().equals(Constants.TREIBSCHEIBE_NAME) && treibscheibe == null && evt.getButton() == GUICommonsConstants.LEFT_MOUSE_BUTTON) {
				treibscheibe = new AufzugschachtTreibscheibe(x, y, (int) (Constants.TREIBSCHEIBE_SIZE_W * scale), (int) (Constants.TREIBSCHEIBE_SIZE_H * scale), scale,
						xscale, yscale, Constants.TREIBSCHEIBE_ELEMENT_ICON_64, this);
				
				locateUmlenkrollen();

				// add the element to the panel
				add(treibscheibe);
				
				// adds a new Tab to the tabbedPanel
				treibscheibeCount++;

				// add it to the elements vector to resize it correct
				existingElements.add(treibscheibe);

				// unable the button
				deleteButton(Constants.TREIBSCHEIBE_NAME);
			} else if (getMarkedButton().equals(Constants.KABINE_NAME) && kabine == null && evt.getButton() == GUICommonsConstants.LEFT_MOUSE_BUTTON) {
				kabine = new Kabine(x, y, (int) (Constants.KABINE_SIZE_W * scale), (int) (Constants.KABINE_SIZE_H * scale), scale, xscale, yscale,
						Constants.KABINE_ELEMENT_ICON, this);
				add(kabine);
				fireEventsForParameters();
				existingElements.add(kabine);
				deleteButton(Constants.KABINE_NAME);
			} else if (getMarkedButton().equals(Constants.GEWICHT_NAME) && gewicht == null && evt.getButton() == GUICommonsConstants.LEFT_MOUSE_BUTTON) {
				gewicht = new Gewicht(x, y, (int) (Constants.GEWICHT_SIZE_W * scale), (int) (Constants.GEWICHT_SIZE_H * scale), scale, xscale, yscale,
						Constants.GEWICHT_ELEMENT_ICON, this);
				add(gewicht);
				fireEventsForParameters();
				existingElements.add(gewicht);
				deleteButton(Constants.GEWICHT_NAME);
			} else if (getMarkedButton().equals(Constants.UMLENKROLLE_NAME) && umlenkrollenVector.size() < maxUmlenkrollen
					&& evt.getButton() == GUICommonsConstants.LEFT_MOUSE_BUTTON) {
				AufzugschachtUmlenkrolle umlenkrolle = new AufzugschachtUmlenkrolle(x, y, (int) (Constants.UMLENKROLLE_SIZE_W * scale), (int) (Constants.UMLENKROLLE_SIZE_H * scale),
						scale, xscale, yscale, Constants.UMLENKROLLE_ELEMENT_ICON_64, this, false, null, false, null);
				add(umlenkrolle);
				existingElements.add(umlenkrolle);
				umlenkrollenVector.add(umlenkrolle);
				umlenkrolleCount++;
				if (umlenkrollenVector.size() == maxUmlenkrollen)
					deleteButton(Constants.UMLENKROLLE_NAME);
			} else if (getMarkedButton().equals(Constants.SEIL_NAME) && seil == null && evt.getButton() == GUICommonsConstants.LEFT_MOUSE_BUTTON) {
				// seil is constructed new
				seil = new AufzugschachtSeil(this);
				add(seil);

				// seil is always under all other elements
				setComponentZOrder(seil, getComponentCount() - 1);
				Element element = getElementAt(mx, my);
				if (isOnBorder(x, y) != 0 || (element != null && !element.equals(kabine) && !element.equals(gewicht))) {
					boolean underTreibscheibe = false;
					if (isOnBorder(x, y) == 2) {
						underTreibscheibe = true;
					}
					// at the border there will be added a rope holder
					if (x == 0)
						x = 1;
					if (y == 0)
						y = 1;
					if (x == getWidth() - Constants.SEILAUFHAENGER_SIZE_W)
						x = getWidth() - (int) (Constants.SEILAUFHAENGER_SIZE_W * scale) - 1;
					if (y == getHeight() - Constants.SEILAUFHAENGER_SIZE_H)
						y = getHeight() - (int) (Constants.SEILAUFHAENGER_SIZE_H * scale) - 1;
					seilaufhaenger1 = new AufzugschachtSeilaufhaenger(x, y, (int) (Constants.SEILAUFHAENGER_SIZE_W * scale),
							(int) (Constants.SEILAUFHAENGER_SIZE_H * scale), scale, xscale, yscale, Constants.AUFHAENGER_ICON, this, false, null, underTreibscheibe);
					add(seilaufhaenger1);
					existingElements.add(seilaufhaenger1);
					seil.addElement(seilaufhaenger1);
					// set 2to1
					aufhaengung = 2;
					mainFrameSetAufhaengung(aufhaengung);
				} else {
					// ... otherwise the element under the mouse will be added
					// to the rope. kabine and gewicht get there own rope holder
					// because they are at the beginning at the rope
					if (element.equals(kabine)) {
						int sx = kabine.getX() + ((kabine.getWidth() - (int) (Constants.SEILAUFHAENGER_SIZE_W * scale)) / 2);
						int sy = kabine.getY() - (int) (Constants.SEILAUFHAENGER_SIZE_H * scale);
						seilaufhaenger1 = new AufzugschachtSeilaufhaenger(sx, sy, (int) (Constants.SEILAUFHAENGER_SIZE_W * scale),
								(int) (Constants.SEILAUFHAENGER_SIZE_H * scale), scale, xscale, yscale, Constants.AUFHAENGER_ICON, this, true, kabine, false);
						add(seilaufhaenger1);
						kabine.setSeilaufhaenger(seilaufhaenger1);
						seil.addElement(seilaufhaenger1);
						// set 1to1
						aufhaengung = 1;
						mainFrameSetAufhaengung(aufhaengung);
					} else if (element.equals(gewicht)) {
						int sx = gewicht.getX() + ((gewicht.getWidth() - (int) (Constants.SEILAUFHAENGER_SIZE_W * scale)) / 2);
						int sy = gewicht.getY() - (int) (Constants.SEILAUFHAENGER_SIZE_H * scale);
						seilaufhaenger1 = new AufzugschachtSeilaufhaenger(sx, sy, (int) (Constants.SEILAUFHAENGER_SIZE_W * scale),
								(int) (Constants.SEILAUFHAENGER_SIZE_H * scale), scale, xscale, yscale, Constants.AUFHAENGER_ICON, this, true, gewicht, false);
						add(seilaufhaenger1);
						gewicht.setSeilaufhaenger(seilaufhaenger1);
						seil.addElement(seilaufhaenger1);
						// set 1to1
						aufhaengung = 1;
						mainFrameSetAufhaengung(aufhaengung);
					}
				}
			} else if (getMarkedButton().equals(Constants.SEIL_NAME)) {
				// the seil is existing now and the circle is on border and aufhaengung is 2to1
				// -> create the second rope holder and exit rope set
				if (isOnBorder(x, y) != 0 && evt.getButton() == GUICommonsConstants.LEFT_MOUSE_BUTTON && aufhaengung == 2 && getElementAt(mx, my) == null) {
					boolean underTreibscheibe = false;
					if (isOnBorder(x, y) == 2) {
						underTreibscheibe = true;
					}
					if (x == 0)
						x = 1;
					if (y == 0)
						y = 1;
					if (x == getWidth() - Constants.SEILAUFHAENGER_SIZE_W)
						x = getWidth() - (int) (Constants.SEILAUFHAENGER_SIZE_W * scale) - 1;
					if (y == getHeight() - Constants.SEILAUFHAENGER_SIZE_H)
						y = getHeight() - (int) (Constants.SEILAUFHAENGER_SIZE_H * scale) - 1;
					seilaufhaenger2 = new AufzugschachtSeilaufhaenger(x, y, (int) (Constants.SEILAUFHAENGER_SIZE_W * scale),
							(int) (Constants.SEILAUFHAENGER_SIZE_H * scale), scale, xscale, yscale, Constants.AUFHAENGER_ICON, this, false, null, underTreibscheibe);
					add(seilaufhaenger2);
					existingElements.add(seilaufhaenger2);
					seil.addElement(seilaufhaenger2);
					deleteButton(Constants.SEIL_NAME);
					mainFrameGetBerechnungSetElements(seil, kabine, gewicht);
					mainFrameGetBerechnungSetSchachthoehe(hoehe);
					fireEventsForParameters();
				} else if (seil != null && evt.getButton() == GUICommonsConstants.LEFT_MOUSE_BUTTON && aufhaengung == 1 && (getElementAt(mx, my).equals(kabine) || getElementAt(mx, my).equals(gewicht))) {
					// end-element
					// but only if it is a kabine or gewicht
					Element element = getElementAt(mx, my);
					if (element != null && element.equals(kabine)) {
						int sx = kabine.getX() + ((kabine.getWidth() - (int) (Constants.SEILAUFHAENGER_SIZE_W * scale)) / 2);
						int sy = kabine.getY() - (int) (Constants.SEILAUFHAENGER_SIZE_H * scale);
						seilaufhaenger2 = new AufzugschachtSeilaufhaenger(sx, sy, (int) (Constants.SEILAUFHAENGER_SIZE_W * scale),
								(int) (Constants.SEILAUFHAENGER_SIZE_H * scale), scale, xscale, yscale, Constants.AUFHAENGER_ICON, this, true, kabine, false);
						add(seilaufhaenger2);
						kabine.setSeilaufhaenger(seilaufhaenger2);
						seil.addElement(seilaufhaenger2);
						if (kabinenRolle != null) {
							seil.removeElement(kabinenRolle);
							remove(kabinenRolle);
							kabine.setUmlenkrolle(null);
							kabinenRolle = null;
						}
						deleteButton(Constants.SEIL_NAME);
						mainFrameGetBerechnungSetElements(seil, kabine, gewicht);
						fireEventsForParameters();
					} else if (element != null && element.equals(gewicht)) {
						int sx = gewicht.getX() + ((gewicht.getWidth() - (int) (Constants.SEILAUFHAENGER_SIZE_W * scale)) / 2);
						int sy = gewicht.getY() - (int) (Constants.SEILAUFHAENGER_SIZE_H * scale);
						seilaufhaenger2 = new AufzugschachtSeilaufhaenger(sx, sy, (int) (Constants.SEILAUFHAENGER_SIZE_W * scale),
								(int) (Constants.SEILAUFHAENGER_SIZE_H * scale), scale, xscale, yscale, Constants.AUFHAENGER_ICON, this, true, gewicht, false);
						add(seilaufhaenger2);
						gewicht.setSeilaufhaenger(seilaufhaenger2);
						seil.addElement(seilaufhaenger2);
						if (gewichtsRolle != null) {
							seil.removeElement(gewichtsRolle);
							remove(gewichtsRolle);
							gewicht.setUmlenkrolle(null);
							gewichtsRolle = null;
						}
						deleteButton(Constants.SEIL_NAME);
						mainFrameGetBerechnungSetElements(seil, kabine, gewicht);
						fireEventsForParameters();
					}
				} else if (evt.getButton() == GUICommonsConstants.RIGHT_MOUSE_BUTTON) {
					// if the mouse button is another one than the left one than
					// deselect all buttons from main frame
					mainFrameDeselectAllButtons();
				} else {
					// if element is not the first or last one on the rope
					Element element = getElementAt(mx, my);
					if (element.equals(kabine)) {
						int sx = kabine.getX() + (kabine.getWidth() / 2) - ((int)(Constants.UMLENKROLLE_SIZE_W * scale) / 2);
						int sy = kabine.getY() - (int) (Constants.UMLENKROLLE_SIZE_H * scale);
						kabinenRolle = new AufzugschachtUmlenkrolle(sx, sy, (int) (Constants.UMLENKROLLE_SIZE_W * scale), (int) (Constants.UMLENKROLLE_SIZE_H * scale),
								scale, xscale, yscale, Constants.UMLENKROLLE_ELEMENT_ICON_64, this, true, kabine, false, null);

						add(kabinenRolle);
						kabine.setUmlenkrolle(kabinenRolle);
						umlenkrolleCount++;
						kabinenRolle.setActivated(true);
						seil.addElement(kabinenRolle);
					} else if (element.equals(gewicht)) {
						int sx = gewicht.getX() + (gewicht.getWidth() / 2) - ((int) (Constants.UMLENKROLLE_SIZE_W * scale) / 2);
						int sy = gewicht.getY() - (int) (Constants.UMLENKROLLE_SIZE_H * scale);
						gewichtsRolle = new AufzugschachtUmlenkrolle(sx, sy, (int) (Constants.UMLENKROLLE_SIZE_W * scale), (int) (Constants.UMLENKROLLE_SIZE_H * scale),
								scale, xscale, yscale, Constants.UMLENKROLLE_ELEMENT_ICON_64, this, true, gewicht, false, null);
						add(gewichtsRolle);
						umlenkrolleCount++;
						gewicht.setUmlenkrolle(gewichtsRolle);
						gewichtsRolle.setActivated(true);
						seil.addElement(gewichtsRolle);
					} else {
						if (element instanceof Rolle) {
							Rolle rolle = (Rolle)element;
							rolle.setActivated(true);
							if (seil.isElement(rolle)) {
								// doppelte umschlingung
								rolle.setDoppelteUmschlingung(true);
								element = rolle.getRolle2teUmschlingung();
							}
						}
						seil.addElement(element);
					}
				}
			}
			else if (evt.getButton() == GUICommonsConstants.RIGHT_MOUSE_BUTTON) {
				// if the mouse button is another one than the left one than
				// deselect all buttons from main frame
				mainFrameDeselectAllButtons();
			}
		} else if (evt.getButton() == GUICommonsConstants.RIGHT_MOUSE_BUTTON) {
			// if the mouse button is another one than the left one than
			// deselect all buttons from main frame
			mainFrameDeselectAllButtons();
		}
		placeable = false;
		repaint();
		eraseCircRect();
	}

	/**
	 * Get the corrected coordinates wich does not leave the area.
	 * 
	 * @param mx The mouse x-coordinate
	 * @param my The mouse y-coordinate
	 * 
	 * @return The corrected point
	 */
	private Point getNewXY(int mx, int my) {
		int nx, ny;
		int ewidth, eheight;

		if (mainFrameIsSeilButtonSelected) {
			return getNewXYCirc(mx, my);
		} else {
			ewidth = getDimension().width;
			eheight = getDimension().height;
		}

		// calculate the maximum and minimum x- and y-coordinate
		if (mx > ewidth / 2 && mx < getWidth() - (ewidth / 2))
			nx = mx - (ewidth / 2);
		else if (mx <= ewidth / 2)
			nx = 1;
		else
			nx = getWidth() - ewidth - 1;

		if (my > eheight / 2 && my < getHeight() - (eheight / 2))
			ny = my - (eheight / 2);
		else if (my <= eheight / 2)
			ny = 1;
		else
			ny = getHeight() - eheight - 1;

		return new Point(nx, ny);
	}

	/**
	 * Gets the corrected new moving coordinate for circle wich does niot leave the elevator Same as getNewXY() but for circle as element.
	 * 
	 * @param mx The mouse x-coordinate
	 * @param my The mouse y-coordinate
	 * 
	 * @return The corrected point
	 */
	private Point getNewXYCirc(int mx, int my) {
		int nx, ny;

		int circwidth = (int) (Constants.SEILAUFHAENGER_SIZE_W * scale);
		int circheight = (int) (Constants.SEILAUFHAENGER_SIZE_H * scale);

		if (mx > circwidth / 2 && mx < getWidth() - (circwidth / 2))
			nx = mx - circwidth / 2;
		else if (mx <= circwidth / 2)
			nx = 0;
		else
			nx = getWidth() - circwidth;

		if (my > circheight / 2 && my < getHeight() - (circheight / 2))
			ny = my - circheight / 2;
		else if (my <= circheight / 2)
			ny = 0;
		else
			ny = getHeight() - circheight;

		return new Point(nx, ny);
	}

	/**
	 * Gives the selected button from main frame.
	 * 
	 * @return The name of the button
	 */
	private String getMarkedButton() {
		return mainFrameMarkedButton;
	}

	/**
	 * Gives the element under the given coordinates to check existance of element under mouse.
	 * 
	 * @param y The y-coordinate
	 * @param x The x-coordinate
	 * 
	 * @return The element under the coordinates or null
	 */
	private Element getElementAt(int x, int y) {
		Element marked = null;

		if (treibscheibe != null && treibscheibe.isUnderMouse(x, y))
			marked = treibscheibe;
		if (kabine != null && kabine.isUnderMouse(x, y))
			marked = kabine;
		if (gewicht != null && gewicht.isUnderMouse(x, y))
			marked = gewicht;
		for (int i = 0; i < umlenkrollenVector.size(); i++) {
			if (((AufzugschachtElement)umlenkrollenVector.elementAt(i)).isUnderMouse(x, y))
				marked = umlenkrollenVector.elementAt(i);
		}
		if (seilaufhaenger1 != null && seilaufhaenger1.isUnderMouse(x, y))
			marked = seilaufhaenger1;
		if (seilaufhaenger2 != null && seilaufhaenger2.isUnderMouse(x, y))
			marked = seilaufhaenger2;
		if (kabinenRolle != null && kabinenRolle.isUnderMouse(x, y))
			marked = kabinenRolle;
		if (kabinenDoppelRolle != null && kabinenDoppelRolle.isUnderMouse(x, y))
			marked = kabinenDoppelRolle;
		if (gewichtsRolle != null && gewichtsRolle.isUnderMouse(x, y))
			marked = gewichtsRolle;

		return marked;
	}

	/**
	 * Gives a whole vector of element under the given rectangle.
	 * 
	 * @param w The width
	 * @param h The height
	 * @param y The upper left y-coordinate
	 * @param x The upper left x-coordinate
	 * 
	 * @return All elements under this rectangle
	 */
	public Vector<Element> getElementAt(int x, int y, int w, int h) {
		Vector<Element> elementVector = new Vector<Element>();
		Element element;

		for (int i = x; i <= x + w; i++) {
			for (int j = y; j <= y + h; j++) {
				if ((element = getElementAt(i, j)) != null && !elementVector.contains(element)) {
					elementVector.add(element);
				}
			}
		}

		return elementVector;
	}

	/**
	 * Deletes the button with the given name.
	 * 
	 * @param name The button name
	 */
	private void deleteButton(String name) {
		mainFrameChangeButton(name, false);
	}

	/**
	 * Removes the element from the elevator panel.
	 * 
	 * @param element The element to remove
	 */
	public void removeElement(Element element) {

		// removing element from the panel
		remove((AufzugschachtElement)element);

		// remove it from the elements-vector
		existingElements.remove(element);

		// remove it from the rope
		if (seil != null)
			seil.removeElement(element);

		// reset the local variables
		if (element.equals(treibscheibe)) {
			removeSeil();
			IDPoolTreibscheibe.removeID(treibscheibe.getID());
			if (treibscheibe.isDoppelteUmschlingung() || treibscheibe.isZweiteUmschlingung()) {
				IDPoolTreibscheibe.removeID(treibscheibe.getRolle2teUmschlingung().getID());
			}
			treibscheibe = null;
			addButton(Constants.TREIBSCHEIBE_NAME);
		}
		if (element.equals(kabine)) {		
			removeSeil();
			kabine = null;	
			addButton(Constants.KABINE_NAME);
			mainFrameKabineRemoved();
		}
		if (element.equals(gewicht)) {
			removeSeil();
			gewicht = null;
			addButton(Constants.GEWICHT_NAME);
			mainFrameGewichtRemoved();
		}
		if (element.equals(seilaufhaenger1)) {
			removeSeil();
			seilaufhaenger1 = null;
			addButton(Constants.SEIL_NAME);
		}
		if (element.equals(seilaufhaenger2)) {
			removeSeil();
			seilaufhaenger2 = null;
			addButton(Constants.SEIL_NAME);
		}
		for (int i = 0; i < umlenkrollenVector.size(); i++) {
			if (element.equals(umlenkrollenVector.get(i))) {
				IDPoolUmlenkrolle.removeID(umlenkrollenVector.get(i).getID());
				if (umlenkrollenVector.get(i).isDoppelteUmschlingung() || umlenkrollenVector.get(i).isZweiteUmschlingung()) {
					IDPoolUmlenkrolle.removeID(umlenkrollenVector.get(i).getRolle2teUmschlingung().getID());
					removeSeil();
				}
				mainFrameDeleteTabbedPane(umlenkrollenVector.get(i));
				umlenkrollenVector.remove(i);
				addButton(Constants.UMLENKROLLE_NAME);
			}
		}
		if (element.equals(kabinenRolle)) {
			removeSeil();
			IDPoolUmlenkrolle.removeID(kabinenRolle.getID());
			kabinenRolle = null;
			addButton(Constants.SEIL_NAME);
		}
		if (element.equals(kabinenDoppelRolle)) {
			removeSeil();
			IDPoolUmlenkrolle.removeID(kabinenDoppelRolle.getRolle1().getID());
			IDPoolUmlenkrolle.removeID(kabinenDoppelRolle.getRolle2().getID());
			kabinenDoppelRolle = null;
			addButton(Constants.SEIL_NAME);
		}
		if (element.equals(gewichtsRolle)) {
			removeSeil();
			IDPoolUmlenkrolle.removeID(gewichtsRolle.getID());
			gewichtsRolle = null;
			addButton(Constants.SEIL_NAME);
		}

		repaint();
	}

	/**
	 * Removes the rope.
	 */
	public void removeSeil() {
		if (seil != null) remove(seil);
		seil = null;
		aufhaengung = 2;
		last = null;
		
		mainFrameDeleteAllTabbedPane();
		if (treibscheibe != null) {
			if (treibscheibe.isDoppelteUmschlingung() || treibscheibe.isZweiteUmschlingung()) {
				IDPoolTreibscheibe.removeID(treibscheibe.getRolle2teUmschlingung().getID());
			}
			treibscheibe.setDoppelteUmschlingung(false);
		}
		for (int i = 0; i < umlenkrollenVector.size(); i++) {
			if (umlenkrollenVector.get(i).isDoppelteUmschlingung() || umlenkrollenVector.get(i).isZweiteUmschlingung()) {
				IDPoolUmlenkrolle.removeID(umlenkrollenVector.get(i).getRolle2teUmschlingung().getID());
			}
			umlenkrollenVector.get(i).setDoppelteUmschlingung(false);
		}

		// remove every part that is directly connected to the rope
		if (seilaufhaenger1 != null) {
			remove(seilaufhaenger1);
			if (kabine != null)
				kabine.setSeilaufhaenger(null);
			if (gewicht != null)
				gewicht.setSeilaufhaenger(null);
			existingElements.remove(seilaufhaenger1);
			seilaufhaenger1 = null;
		}
		if (seilaufhaenger2 != null) {
			remove(seilaufhaenger2);
			if (kabine != null)
				kabine.setSeilaufhaenger(null);
			if (gewicht != null)
				gewicht.setSeilaufhaenger(null);
			existingElements.remove(seilaufhaenger2);
			seilaufhaenger2 = null;
		}
		if (kabinenRolle != null) {
			remove(kabinenRolle);
			kabine.setUmlenkrolle(null);
			mainFrameDeleteTabbedPane(kabinenRolle);
			IDPoolUmlenkrolle.removeID(kabinenRolle.getID());
			kabinenRolle = null;
		}
		if (kabinenDoppelRolle != null) {
			remove(kabinenDoppelRolle);
			kabine.setDoppelUmlenkrolle(null);
			mainFrameDeleteTabbedPane(kabinenDoppelRolle.getRolle1());
			IDPoolUmlenkrolle.removeID(kabinenDoppelRolle.getRolle1().getID());
			mainFrameDeleteTabbedPane(kabinenDoppelRolle.getRolle2());
			IDPoolUmlenkrolle.removeID(kabinenDoppelRolle.getRolle2().getID());
			kabinenDoppelRolle = null;
		}
		if (gewichtsRolle != null) {
			remove(gewichtsRolle);
			gewicht.setUmlenkrolle(null);
			mainFrameDeleteTabbedPane(gewichtsRolle);
			IDPoolUmlenkrolle.removeID(gewichtsRolle.getID());
			gewichtsRolle = null;
		}
		
		mainFrameSeilRemoved();
		addButton(Constants.SEIL_NAME);
		repaint();
	}

	/**
	 * Re-addes the button at main frame.
	 * 
	 * @param name The name of the button
	 */
	private void addButton(String name) {
		mainFrameChangeButton(name, true);
	}

	/**
	 * Erases all placing-rectangles and -circles.
	 */
	public void eraseCircRect() {
		if (rectangle != null)
			rectangle.erase();
		if (circle != null)
			circle.erase();
	}

	/**
	 * Gets the dimension of the placing element.
	 * 
	 * @return the dimension
	 */
	private Dimension getDimension() {
		int width, height;

		if (mainFrameIsUmlenkrolleButtonSelected) {
			width = (int) (Constants.UMLENKROLLE_SIZE_W * scale);
			height = (int) (Constants.UMLENKROLLE_SIZE_H * scale);
		} else if (mainFrameIsTreibscheibeButtonSelected) {
			width = (int) (Constants.TREIBSCHEIBE_SIZE_W * scale);
			height = (int) (Constants.TREIBSCHEIBE_SIZE_H * scale);
		} else if (mainFrameIsKabineButtonSelected) {
			width = (int) (Constants.KABINE_SIZE_W * scale);
			height = (int) (Constants.KABINE_SIZE_H * scale);
		} else if (mainFrameIsGewichtButtonSelected) {
			width = (int) (Constants.GEWICHT_SIZE_W * scale);
			height = (int) (Constants.GEWICHT_SIZE_H * scale);
		} else {
			width = (int) (Constants.DEFAULT_ELEMENT_SIZE_W * scale);
			height = (int) (Constants.DEFAULT_ELEMENT_SIZE_H * scale);
		}

		return new Dimension(width, height);
	}
	
	/**
	 * Switch to doppel kabinen rolle.
	 */
	public void switchToDoppelKabinenRolle() {
		if (kabinenDoppelRolle == null) {
			int sx = kabine.getX() + (kabine.getWidth() / 2) - ((int) (Constants.DOPPELUMLENKROLLE_SIZE_W * scale) / 2);
			int sy = kabine.getY() - (int) (Constants.DOPPELUMLENKROLLE_SIZE_H * scale);
			
			kabinenDoppelRolle = new AufzugschachtDoppelUmlenkrolle(sx, sy, (int) (Constants.DOPPELUMLENKROLLE_SIZE_W * scale), (int) (Constants.DOPPELUMLENKROLLE_SIZE_H * scale),
					scale, xscale, yscale, Constants.DOPPELUMLENKROLLE_ELEMENT_ICON_64, this, kabine);

			// add the double roll
			add(kabinenDoppelRolle);
			
			// create tabs
			mainFrameNewTabbedPane(kabinenDoppelRolle.getRolle1());
			mainFrameNewTabbedPane(kabinenDoppelRolle.getRolle2());
			
			// set the double roll and add it to the rope
			kabine.setDoppelUmlenkrolle(kabinenDoppelRolle);
			seil.replaceElement(kabinenDoppelRolle, kabinenRolle);
			kabinenDoppelRolle.setActivated(true);
			
			// remove the single roll
			if (kabinenRolle != null) {
				remove(kabinenRolle);
				kabine.setUmlenkrolle(null);
				mainFrameDeleteTabbedPane(kabinenRolle);
				IDPoolUmlenkrolle.removeID(kabinenRolle.getID());
				kabinenRolle = null;
			}
			
			seil.refreshSeil();
			repaint();
		}
	}
	
	/**
	 * Switch to single kabinen rolle.
	 */
	public void switchToKabinenRolle() {
		if (kabinenRolle == null) {
			int sx = kabine.getX() + (kabine.getWidth() / 2) - ((int) (Constants.UMLENKROLLE_SIZE_W * scale) / 2);
			int sy = kabine.getY() - (int) (Constants.UMLENKROLLE_SIZE_H * scale);
			
			kabinenRolle = new AufzugschachtUmlenkrolle(sx, sy, (int) (Constants.UMLENKROLLE_SIZE_W * scale), (int) (Constants.UMLENKROLLE_SIZE_H * scale),
					scale, xscale, yscale, Constants.UMLENKROLLE_ELEMENT_ICON_64, this, true, kabine, false, null);

			// add single roll
			add(kabinenRolle);
			mainFrameNewTabbedPane(kabinenRolle);
			
			// set single roll and put it on the rope
			kabine.setUmlenkrolle(kabinenRolle);
			seil.replaceElement(kabinenRolle, kabinenDoppelRolle);
			kabinenRolle.setActivated(true);
			
			// remove double roll
			if (kabinenDoppelRolle != null) {
				remove(kabinenDoppelRolle);
				kabine.setDoppelUmlenkrolle(null);
				mainFrameDeleteTabbedPane(kabinenDoppelRolle.getRolle1());
				mainFrameDeleteTabbedPane(kabinenDoppelRolle.getRolle2());
				IDPoolUmlenkrolle.removeID(kabinenDoppelRolle.getRolle1().getID());
				IDPoolUmlenkrolle.removeID(kabinenDoppelRolle.getRolle2().getID());
				kabinenDoppelRolle = null;
			}

			seil.refreshSeil();
			repaint();
		}
	}
	
	/**
	 * Returns if kabinenRolle exists.
	 * 
	 * @return true, if kabinenRolle exists
	 */
	public boolean kabinenRolleExists() {
		if (kabinenRolle != null) return true;
		else return false;
	}
	
	/**
	 * Returns if kabinenDoppelRolle exists.
	 * 
	 * @return true, if kabinenDoppelRolle exists
	 */
	public boolean kabinenDoppelRolleExists() {
		if (kabinenDoppelRolle != null) return true;
		else return false;
	}

	/**
	 * Move all wire holders.
	 */
	public void moveAllSeilaufhaenger() {
		if (treibscheibe != null) {
			if (seilaufhaenger1 != null && seilaufhaenger1.isUnderTreibscheibe()) {
				seilaufhaenger1.setLocation(seilaufhaenger1.getX(), treibscheibe.getY() + seilaufhaenger1.getY_underTreibscheibe());
			}
			if (seilaufhaenger2 != null && seilaufhaenger2.isUnderTreibscheibe()) {
				seilaufhaenger2.setLocation(seilaufhaenger2.getX(), treibscheibe.getY() + seilaufhaenger2.getY_underTreibscheibe());
			}
		}
	}

	/**
	 * Locate umlenkrollen.
	 */
	private void locateUmlenkrollen() {
		for (int i = 0; i < umlenkrollenVector.size(); i++) {
			AufzugschachtUmlenkrolle rolle = (AufzugschachtUmlenkrolle)umlenkrollenVector.get(i);
			if (!rolle.isFree()) {
				rolle.setHorizEntf((rolle.getMPoint().x - treibscheibe.getMPoint().x) * getMmPerPixel());
				rolle.setVertEntf((rolle.getMPoint().y - treibscheibe.getMPoint().y) * getMmPerPixel());
			}
		}
	}

	/**
	 * Gets the aufhaengung.
	 * 1 = 1:1
	 * 2 = 2:1
	 * 
	 * @return the aufhaengung
	 */
	public int getAufhaengung() {
		return aufhaengung;
	}

	/**
	 * Sets the last.
	 * 
	 * @param last the last to set
	 */
	public void setLast(Element last) {
		this.last = (AufzugschachtElement)last;
	}

	/**
	 * Gets the gewicht.
	 * 
	 * @return the gewicht
	 */
	public Gewicht getGewicht() {
		return gewicht;
	}

	/**
	 * Gets the kabine.
	 * 
	 * @return the kabine
	 */
	public Kabine getKabine() {
		return kabine;
	}

	/**
	 * Sets the treibscheibe.
	 * 
	 * @param treibscheibe the treibscheibe
	 */
	public void setTreibscheibe(AufzugschachtTreibscheibe treibscheibe) {
		this.treibscheibe = treibscheibe;
	}

	/**
	 * Sets the main frame marked button.
	 * 
	 * @param mainFrameMarkedButton the main frame marked button
	 */
	public void setMainFrameMarkedButton(String mainFrameMarkedButton) {
		this.mainFrameMarkedButton = mainFrameMarkedButton;
	}
	
	/**
	 * Sets the main frame button selected.
	 * 
	 * @param isSelected the is selected
	 * @param name the name
	 */
	public void setMainFrameButtonSelected(String name, boolean isSelected) {
		if (name.equals(Constants.SEIL_NAME)) {
			mainFrameIsSeilButtonSelected = isSelected;
		}
		if (name.equals(Constants.TREIBSCHEIBE_NAME)) {
			mainFrameIsTreibscheibeButtonSelected = isSelected;
		}
		if (name.equals(Constants.UMLENKROLLE_NAME)) {
			mainFrameIsUmlenkrolleButtonSelected = isSelected;
		}
		if (name.equals(Constants.KABINE_NAME)) {
			mainFrameIsKabineButtonSelected = isSelected;
		}
		if (name.equals(Constants.GEWICHT_NAME)) {
			mainFrameIsGewichtButtonSelected = isSelected;
		}
	}
	
	/**
	 * Sets the main frame is any button selected.
	 * 
	 * @param mainFrameIsAnyButtonSelected the main frame is any button selected
	 */
	public void setMainFrameIsAnyButtonSelected(boolean mainFrameIsAnyButtonSelected) {
		this.mainFrameIsAnyButtonSelected = mainFrameIsAnyButtonSelected;
	}
	
	/**
	 * Main frame set aufhaengung.
	 * 
	 * @param aufhaengung the aufhaengung
	 */
	private void mainFrameSetAufhaengung(int aufhaengung) {
		Object[] parameters = new Object[1];
		parameters[0] = aufhaengung;
		fireEvent(new AufzugschachtEvent(this, 0, AufzugschachtEvent.SET_AUFHAENGUNG, parameters));
	}
	
	/**
	 * Main frame get berechnung set elements.
	 * 
	 * @param gewicht the gewicht
	 * @param seil the seil
	 * @param kabine the kabine
	 */
	private void mainFrameGetBerechnungSetElements(Seil seil, Kabine kabine, Gewicht gewicht) {
		Object[] parameters = new Object[3];
		parameters[0] = seil;
		parameters[1] = kabine;
		parameters[2] = gewicht;
		fireEvent(new AufzugschachtEvent(this, 0, AufzugschachtEvent.SET_ELEMENTS_OF_BERECHNUNG, parameters));
	}
	
	/**
	 * Main frame get berechnung set elements.
	 * 
	 * @param schachthoehe the schachthoehe
	 */
	private void mainFrameGetBerechnungSetSchachthoehe(int schachthoehe) {
		Object[] parameters = new Object[1];
		parameters[0] = schachthoehe;
		fireEvent(new AufzugschachtEvent(this, 0, AufzugschachtEvent.SET_SCHACHTHOEHE_OF_BERECHNUNG, parameters));
	}
	
	/**
	 * Main frame deselect all buttons.
	 */
	private void mainFrameDeselectAllButtons() {
		fireEvent(new AufzugschachtEvent(this, 0, AufzugschachtEvent.DESELECT_ALL_BUTTONS, null));
	}
	
	/**
	 * Main frame change button.
	 * 
	 * @param name the name
	 * @param value the value
	 */
	private void mainFrameChangeButton(String name, boolean value) {
		Object[] parameters = new Object[2];
		parameters[0] = name;
		parameters[1] = value;
		fireEvent(new AufzugschachtEvent(this, 0, AufzugschachtEvent.CHANGE_BUTTON, parameters));
	}
	
	/**
	 * Main frame delete tabbed pane.
	 * 
	 * @param rolle the rolle
	 */
	public void mainFrameDeleteTabbedPane(Rolle rolle) {
		Object[] parameters = new Object[1];
		parameters[0] = rolle;
		fireEvent(new AufzugschachtEvent(this, 0, AufzugschachtEvent.DELETE_TABBED_PANE, parameters));
	}
	
	/**
	 * Main frame delete all tabbed pane.
	 */
	private void mainFrameDeleteAllTabbedPane() {
		fireEvent(new AufzugschachtEvent(this, 0, AufzugschachtEvent.DELETE_ALL_TABBED_PANE, null));
	}
	
	/**
	 * Main frame new tabbed pane.
	 * 
	 * @param rolle the rolle
	 */
	public void mainFrameNewTabbedPane(Rolle rolle) {
		Object[] parameters = new Object[1];
		parameters[0] = rolle;
		fireEvent(new AufzugschachtEvent(this, 0, AufzugschachtEvent.NEW_TABBED_PANE, parameters));
	}
	
	/**
	 * Main frame activate tabbed pane.
	 * 
	 * @param rolle the rolle
	 */
	public void mainFrameActivateTabbedPane(Rolle rolle) {
		Object[] parameters = new Object[1];
		parameters[0] = rolle;
		fireEvent(new AufzugschachtEvent(this, 0, AufzugschachtEvent.ACTIVATE_TABBED_PANE, parameters));
	}
	
	/**
	 * Main frame refresh tabbed pane.
	 * 
	 * @param rolle the rolle
	 */
	public void mainFrameRefreshTabbedPane(Rolle rolle) {
		Object[] parameters = new Object[1];
		parameters[0] = rolle;
		fireEvent(new AufzugschachtEvent(this, 0, AufzugschachtEvent.REFRESH_TABBED_PANE, parameters));
	}
	
	/**
	 * Main frame show options frame.
	 * 
	 * @param element the element
	 * @param rolle2 the rolle2
	 */
	public void mainFrameShowOptionsFrame(Element element, boolean rolle2) {
		Object[] parameters = new Object[2];
		parameters[0] = element;
		parameters[1] = rolle2;
		fireEvent(new AufzugschachtEvent(this, 0, AufzugschachtEvent.SHOW_OPTIONS_FRAME, parameters));
	}
	
	/**
	 * Main frame show seil options frame.
	 * 
	 * @param seil the seil
	 */
	public void mainFrameShowSeilOptionsFrame(Seil seil) {
		Object[] parameters = new Object[1];
		parameters[0] = seil;
		fireEvent(new AufzugschachtEvent(this, 0, AufzugschachtEvent.SHOW_SEIL_OPTIONS_FRAME, parameters));
	}
	
	/**
	 * Main frame set mass.
	 * 
	 * @param mass the mass
	 */
	public void mainFrameSetMassKabine(int mass) {
		Object[] parameters = new Object[1];
		parameters[0] = mass;
		fireEvent(new AufzugschachtEvent(this, 0, AufzugschachtEvent.SET_MASS, parameters));
	}
	
	/**
	 * Main frame set mass.
	 * 
	 * @param mass the mass
	 * @param part the part
	 */
	public void mainFrameSetMassGewicht(int mass, int part) {
		Object[] parameters = new Object[2];
		parameters[0] = mass;
		parameters[1] = part;
		fireEvent(new AufzugschachtEvent(this, 0, AufzugschachtEvent.SET_MASS_WEIGHT, parameters));
	}
	
	/**
	 * Main frame refresh seil.
	 * 
	 * @param seil the seil
	 */
	public void mainFrameRefreshSeil(Seil seil) {
		Object[] parameters = new Object[1];
		parameters[0] = seil;
		fireEvent(new AufzugschachtEvent(this, 0, AufzugschachtEvent.REFRESH_SEIL, parameters));
	}
	
	/**
	 * Main frame refresh seil.
	 * 
	 * @param zuladung the zuladung
	 */
	public void mainFrameSetZuladung(int zuladung) {
		Object[] parameters = new Object[1];
		parameters[0] = zuladung;
		fireEvent(new AufzugschachtEvent(this, 0, AufzugschachtEvent.SET_ZULADUNG, parameters));
	}
	
	/**
	 * Main frame enable all buttons.
	 */
	public void mainFrameEnableAllButtons() {
		fireEvent(new AufzugschachtEvent(this, 0, AufzugschachtEvent.ENABLE_ALL_BUTTONS, null));
	}

	/**
	 * Main frame seil removed.
	 */
	public void mainFrameSeilRemoved() {
		fireEvent(new AufzugschachtEvent(this, 0, AufzugschachtEvent.SEIL_REMOVED, null));
	}
	
	/**
	 * Main frame kabine removed.
	 */
	public void mainFrameKabineRemoved() {
		fireEvent(new AufzugschachtEvent(this, 0, AufzugschachtEvent.KABINE_REMOVED, null));
	}
	
	/**
	 * Main frame gewicht removed.
	 */
	public void mainFrameGewichtRemoved() {
		fireEvent(new AufzugschachtEvent(this, 0, AufzugschachtEvent.GEWICHT_REMOVED, null));
	}
	
	/**
	 * Main frame set durchschnitt.
	 * 
	 * @param durchschnitt the durchschnitt
	 */
	public void mainFrameSetDurchschnitt(double durchschnitt) {
		Object[] parameters = new Object[1];
		parameters[0] = durchschnitt;
		fireEvent(new AufzugschachtEvent(this, 0, AufzugschachtEvent.SET_DURCHSCHNITT, parameters));
	}
	
	/**
	 * Main frame set profile.
	 * 
	 * @param profil the profil
	 */
	public void mainFrameSetProfil(double profil) {
		Object[] parameters = new Object[1];
		parameters[0] = profil;
		fireEvent(new AufzugschachtEvent(this, 0, AufzugschachtEvent.SET_PROFIL, parameters));
	}
	
	/**
	 * Main frame refresh enlancement angle.
	 * 
	 * @param angle the angle
	 */
	public void mainFrameRefreshUmschlWinkel(double angle) {
		Object[] parameters = new Object[1];
		parameters[0] = angle;
		fireEvent(new AufzugschachtEvent(this, 0, AufzugschachtEvent.REFRESH_UMSCHL, parameters));
	}
	
	/**
	 * Adds the aufzugschacht listener.
	 * 
	 * @param listener the listener
	 */
	public void addAufzugschachtListener(AufzugschachtListener listener) {
		listenerList.add(AufzugschachtListener.class, listener);
	}
	
	/**
	 * Fire event.
	 * 
	 * @param evt the evt
	 */
	public void fireEvent(AufzugschachtEvent evt) {
		Object[] listeners = listenerList.getListenerList();
        // Each listener occupies two elements - the first is the listener class
        // and the second is the listener instance
        for (int i = 0; i < listeners.length; i += 2) {
            if (listeners[i] == AufzugschachtListener.class) {
                ((AufzugschachtListener)listeners[i+1]).eventOccurred(evt);
            }
        }
	}
	
	/**
	 * Fire events for buttons after loading an alb-file
	 * Sets the buttons disabled, whose elements are shown.
	 */
	public void fireEventsForButtons() {
		if (treibscheibe != null) mainFrameChangeButton(Constants.TREIBSCHEIBE_NAME, false);
		if (kabine != null) mainFrameChangeButton(Constants.KABINE_NAME, false);
		if (gewicht != null) mainFrameChangeButton(Constants.GEWICHT_NAME, false);
		if (seilaufhaenger2 != null) mainFrameChangeButton(Constants.SEIL_NAME, false);
		if (umlenkrollenVector.size() >= maxUmlenkrollen) mainFrameChangeButton(Constants.UMLENKROLLE_NAME, false);
		if (zickzack != null) mainFrameChangeButton(Constants.ZICKZACK_NAME, false);
	}
	
	/**
	 * Fire events for parameters after loading an alb-file
	 * Sets the parameters of the existing elements to the
	 * main frame text fields.
	 */
	public void fireEventsForParameters() {
		if (kabine != null) {
			mainFrameSetMassKabine(kabine.getMass());
			mainFrameSetZuladung(kabine.getZuladung());
			mainFrameSetDurchschnitt(kabine.getDurchschnitt());
			mainFrameSetProfil(kabine.getProfil());
		}
		if (gewicht != null) {
			mainFrameSetMassGewicht(gewicht.getMass(), (int)(gewicht.getPart()*100));
		}
		if (seil != null) {
			mainFrameRefreshSeil(seil);
		}
	}
	
	/**
	 * Fire events for tabbed panes. Adds new tabbed panes
	 * after loading an alb-file
	 */
	public void fireEventsForTabbedPanes() {
		if (seil != null) {
			seil.addAllTabbedPanes();
		}
	}
	
	/**
	 * Sets the seil vordergrund.
	 */
	public void setSeilVordergrund() {
		if (seil != null) {
			setComponentZOrder(seil, 0);
		}
	}
	
	/**
	 * Sets the seil hintergrund.
	 */
	public void setSeilHintergrund() {
		if (seil != null) {
			setComponentZOrder(seil, getComponentCount() - 1);
		}
	}

	/**
	 * Gets the umlenkrollen vector.
	 * 
	 * @return the umlenkrollen vector
	 */
	public Vector<Umlenkrolle> getUmlenkrollenVector() {
		return umlenkrollenVector;
	}


	/**
	 * Gets the height of the elevator in meters.
	 * 
	 * @return the height in meters
	 */
	public int getHoehe() {
		return hoehe;
	}


	/**
	 * Gets the width in centimeters.
	 * 
	 * @return the width
	 */
	public int getBreite() {
		return breite / 10;
	}


	/**
	 * Gets the weight roll.
	 * 
	 * @return the weight roll
	 */
	public Umlenkrolle getGewichtsRolle() {
		return gewichtsRolle;
	}


	/**
	 * Gets the cabine double roll.
	 * 
	 * @return the cabine double roll
	 */
	public DoppelUmlenkrolle getKabinenDoppelRolle() {
		return kabinenDoppelRolle;
	}


	/**
	 * Gets the cabine roll.
	 * 
	 * @return the cabine roll
	 */
	public Umlenkrolle getKabinenRolle() {
		return kabinenRolle;
	}

	/**
	 * Gets the count of rolls without waelzlagerung.
	 * 
	 * @return the count of rolls without waelzlagerung
	 */
	public int getSeilrollenOhneWaelzlagerungCount() {
		int z = 0;
		
		for (int i = 0; i < umlenkrollenVector.size(); i++) {
			if (umlenkrollenVector.get(i).isOhneWaelzlagerung()) z++;
		}
		if (kabinenRolle != null && kabinenRolle.isOhneWaelzlagerung()) z++;
		if (kabinenDoppelRolle != null && kabinenDoppelRolle.getRolle1().isOhneWaelzlagerung()) z++;
		if (kabinenDoppelRolle != null && kabinenDoppelRolle.getRolle2().isOhneWaelzlagerung()) z++;
		if (gewichtsRolle != null && gewichtsRolle.isOhneWaelzlagerung()) z++;
		
		return z;
	}
    
    /**
	 * Gets the mm per pixel.
	 * 
	 * @return the mm per pixel
	 */
	public int getMmPerPixel() {
		if (breite  < getWidth()) return 1;
		else return breite  / getWidth();
	}

	public double getGeschwindigkeit() {
		return geschwindigkeit;
	}

	public void setGeschwindigkeit(double geschwindigkeit) {
		this.geschwindigkeit = geschwindigkeit;
	}
    
    public IDPool getIDPoolTreibscheibe() {
        return iDPoolTreibscheibe;
    }
    
    public IDPool getIDPoolUmlenkrolle() {
        return iDPoolUmlenkrolle;
    }

    public Treibscheibe getTreibscheibe() {
        return treibscheibe;
    }

    public Seil getSeil() {
        return seil;
    }
}
