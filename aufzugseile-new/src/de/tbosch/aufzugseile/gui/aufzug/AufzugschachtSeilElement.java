package de.tbosch.aufzugseile.gui.aufzug;

import de.tbosch.seile.commons.elemente.DoppelUmlenkrolle;
import de.tbosch.seile.commons.elemente.Element;
import de.tbosch.seile.commons.elemente.Kapsel;
import de.tbosch.seile.commons.elemente.Rolle;
import de.tbosch.seile.commons.elemente.SeilElement;
import de.tbosch.seile.commons.elemente.Seilaufhaenger;
import de.tbosch.seile.commons.elemente.Treibscheibe;
import de.tbosch.seile.commons.elemente.Umlenkrolle;
import java.awt.Point;
import java.util.logging.Level;

import java.util.logging.Logger;
import org.trigonometry.TangentLines;


/**
 * The Class SeilElement. Every element on the rope is an SeilElement. This class calculates
 * the incoming and outgoing point of the rope
 * 
 * @author Thomas Bosch (tbosch@gmx.de)
 */
public class AufzugschachtSeilElement implements SeilElement {
    
    private static final Logger logger = Logger.getLogger(AufzugschachtSeilElement.class.getName());
	
	/** The if the rope goes over the upper point of the element. */
	private boolean upperPoint = true;
	
	/** The ident string for getOutPoint. */
	private static String OUTPOINT = "out";
	
	/** The ident string for getInPoint. */
	private static String INPOINT = "in";

	/** The Element. */
	private AufzugschachtElement element;

	/** The previous SeilElement. */
	private AufzugschachtSeilElement prev;

	/** The nextLoc SeilElement. */
	private AufzugschachtSeilElement next;
	
	/** The seil. */
	private AufzugschachtSeil seil;

	/**
	 * The Constructor.
	 * 
	 * @param element the element
	 * @param seil the seil
	 */
	public AufzugschachtSeilElement(AufzugschachtElement element, AufzugschachtSeil seil) {
		this.element = element;
		this.seil = seil;
	}

	/**
	 * Gets the point in which the rope leaves the element.
	 * 
	 * @return the outgoing point
	 */
	public Point getOutPoint() {
        AufzugschachtSeilElement prev_loc = (AufzugschachtSeilElement)prev;
        AufzugschachtSeilElement next_loc = (AufzugschachtSeilElement)next;
        
		// if the actual element is a roll
		if (element instanceof AufzugschachtRolle) {
			AufzugschachtRolle thisRolle = (AufzugschachtRolle)element;
			// if the nextLoc element is a rope holder 
			if (next_loc != null && next_loc.getElement() instanceof Seilaufhaenger) {
				Seilaufhaenger seilaufhaenger = (Seilaufhaenger) next_loc.getElement();
				// if there is hanging something at the rope holder
				if (seilaufhaenger.getHangingElement() instanceof Kapsel) {
					// catch loop in getInPoint/getOutPoint
					if (prev_loc != null && prev_loc.getPrev() != null && !(prev_loc.getPrev().getElement() instanceof Seilaufhaenger)) {
						if (thisRolle.getMPoint().x <= prev_loc.getOutPoint().x) {
							upperPoint = false;
							return new Point(thisRolle.getX(), thisRolle.getMPoint().y);
						}
						else {
							upperPoint = true;
							return new Point(thisRolle.getX() + thisRolle.getWidth(), thisRolle.getMPoint().y);
						}
					} 
					else if (prev_loc != null && thisRolle.getMPoint().x <= ((AufzugschachtElement)prev_loc.getElement()).getMPoint().x) {
						upperPoint = false;
						return new Point(thisRolle.getX(), thisRolle.getMPoint().y);
					}
					else {
						upperPoint = true;
						return new Point(thisRolle.getX() + thisRolle.getWidth(), thisRolle.getMPoint().y);
					}
				}
				// if there is nothing hanging at the rope holder
				else {
					Point[] tangentPoints = new Point[2];
					// tangent line between rope holder and roll
					tangentPoints = TangentLines.getTangentPoints(thisRolle.getMPoint(), thisRolle.getRadius(), ((AufzugschachtSeilaufhaenger)seilaufhaenger).getMPoint());
					if (prev_loc != null) {
						int tangent;
						// check out wich tangent point is the correct one
						if ((tangent = wichTangentPoint(prev_loc, next_loc, OUTPOINT)) == 0) return tangentPoints[1];
						else if (tangent == 1) return tangentPoints[0];
						else if (tangent == 2) return tangentPoints[1];
						else if (tangent == 3) return tangentPoints[0];
					}
					else return tangentPoints[0];
				}
			}
			else if (next_loc != null && next_loc.getElement() instanceof AufzugschachtRolle) {
				AufzugschachtRolle nextRolle = (AufzugschachtRolle)next_loc.getElement();
				Point[] tangentPoints = new Point[8];
				// if the nextLoc element is also a roll -> create tangent lines on both circles
				tangentPoints = TangentLines.getTangentPoints(thisRolle.getMPoint(), thisRolle.getRadius(), nextRolle.getMPoint(), nextRolle.getRadius());
				if (prev_loc != null) {
					AufzugschachtElement prevElement = (AufzugschachtElement)prev_loc.getElement();
					Element nextElement = next_loc.getElement();
					if (next_loc.getNext() != null 
							&& thisRolle.getRolle2teUmschlingung() != null
							&& next_loc.getNext().equals(thisRolle.getRolle2teUmschlingung().getSeilElement(true)) 
							&& prev_loc.getElement() instanceof Rolle
							&& ((Rolle)prev_loc.getElement()).getRolle2teUmschlingung() != null
							&& next_loc.equals(((Rolle)prev_loc.getElement()).getRolle2teUmschlingung().getSeilElement(true))) {
						// doppelte umschlingung
						// erste Umschlingung der zweiten Rolle bei eingang != ausgang
						thisRolle.setDoppelteUmschlingung(true);
						int intangent = wichTangentPoint(prev_loc, next_loc, INPOINT);
						int outtangent = wichTangentPoint(prev_loc, next_loc, OUTPOINT);
						
						if (outtangent == 0) {
							if (intangent == 2) return tangentPoints[0];
							if (intangent == 0) return tangentPoints[1];
						}
						// ot == 1 not reachable ??? 
						else if (outtangent == 1) return tangentPoints[0];
						else if (outtangent == 2) {
							if (intangent == 0) return tangentPoints[1];
							if (intangent == 2) return tangentPoints[0];
						}
						else if (outtangent == 3) {
							if (intangent == 3) return tangentPoints[1];
							if (intangent == 1) return tangentPoints[0];
						}
					}
					else if (next_loc.getNext() != null 
							&& thisRolle.getRolle2teUmschlingung() != null
							&& next_loc.getNext().equals(thisRolle.getRolle2teUmschlingung().getSeilElement(true))) {
						// doppelte umschlingung
						// erste umschlingung der ersten rolle
						thisRolle.setDoppelteUmschlingung(true);
						int outtangent = wichTangentPoint(prev_loc, next_loc, OUTPOINT);
						
						if (outtangent == 0) return tangentPoints[1];
						else if (outtangent == 1) return tangentPoints[0];
						else if (outtangent == 2) return tangentPoints[1];
						else if (outtangent == 3) return tangentPoints[0];
					}
					else if (prev_loc.getElement() instanceof Rolle 
							&& nextElement instanceof Rolle
							&& ((Rolle)nextElement).getRolle1teUmschlingung() != null
							&& prev_loc.equals(((Rolle)nextElement).getRolle1teUmschlingung().getSeilElement(true)) 
							&& prev_loc.getPrev() != null 
							&& thisRolle.getRolle1teUmschlingung() != null
							&& prev_loc.getPrev().equals(thisRolle.getRolle1teUmschlingung().getSeilElement(true))) {
						// doppelte umschlingung
						// zweite Umschlingung der ersten Rolle bei eingang != ausgang
						int outtangent = wichTangentPoint(prev_loc, next_loc, OUTPOINT);
						int previntangent = prev_loc.wichTangentPoint((AufzugschachtSeilElement)prev_loc.getPrev(), (AufzugschachtSeilElement)prev_loc.getNext(), INPOINT);
						
						if (outtangent == 0) {
							if (previntangent == 0) return tangentPoints[1];
							if (previntangent == 3) return tangentPoints[1];
							if (previntangent == 2) return tangentPoints[4];
							if (previntangent == 1) return tangentPoints[4];
						}
						else if (outtangent == 1) {
							if (previntangent == 0) return tangentPoints[5];
							if (previntangent == 1) return tangentPoints[5];
							if (previntangent == 2) return tangentPoints[0];
							if (previntangent == 3) return tangentPoints[0];
						}
						else if (outtangent == 2) {
							if (previntangent == 0) return tangentPoints[5];
							if (previntangent == 3) return tangentPoints[5];
							if (previntangent == 2) return tangentPoints[0];
							if (previntangent == 1) return tangentPoints[0];
						}
						else if (outtangent == 3) {
							if (previntangent == 0) return tangentPoints[1];
							if (previntangent == 2) return tangentPoints[4];
						}
						logger.severe("tangent point not reached");
					}
					else if (prev_loc.getElement() instanceof Rolle 
							&& nextElement instanceof Rolle
							&& ((Rolle)nextElement).getRolle1teUmschlingung() != null
							&& prev_loc.equals(((Rolle)nextElement).getRolle1teUmschlingung().getSeilElement(true)) ) {
						// prevLoc = nextLoc
						// erste Umschlingung der zweiten rolle bei eingang = ausgang
						thisRolle.setDoppelteUmschlingung(true);
						int intangent = wichTangentPoint(prev_loc, next_loc, INPOINT);
						int outtangent = wichTangentPoint(prev_loc, next_loc, OUTPOINT);
						
						if (intangent == 0) {
							if (outtangent == 0) return tangentPoints[1];
							if (outtangent == 2) return tangentPoints[5];
						}
						else if (intangent == 1) {
							if (outtangent == 1) return tangentPoints[0];
							if (outtangent == 3) return tangentPoints[4];
						}
						else if (intangent == 2) {
							if (outtangent == 2) return tangentPoints[0];
							if (outtangent == 0) return tangentPoints[4];
						}
						else if (intangent == 3) {
							if (outtangent == 3) return tangentPoints[1];
							if (outtangent == 1) return tangentPoints[5];
						}
					}
					// if there is a rope holder as previous element
					else if (prevElement instanceof AufzugschachtSeilaufhaenger) {
						AufzugschachtSeilaufhaenger seilaufhaenger = (AufzugschachtSeilaufhaenger)prevElement;
						// if there is hanging something at the rope holder
						if (seilaufhaenger.getHangingElement() instanceof Kapsel) {
							// catch loop exception
							if (next_loc.getNext() != null && !(next_loc.getNext().getElement() instanceof AufzugschachtSeilaufhaenger)) {
								if (prevElement.getMPoint().x <= next_loc.getInPoint().x + thisRolle.getRadius()) {
									upperPoint = true;
									if (next_loc.isUpperPoint()) return tangentPoints[1];
									else return tangentPoints[5];
								}
								else {
									upperPoint = false;
									if (next_loc.isUpperPoint()) return tangentPoints[4];
									else return tangentPoints[0];
								}
							}
							else if (prevElement.getMPoint().x <= ((AufzugschachtElement)next_loc.getElement()).getMPoint().x + thisRolle.getRadius()) {
								upperPoint = true;
								if (next_loc.isUpperPoint()) return tangentPoints[1];
								else return tangentPoints[5];
							}
							else {
								upperPoint = false;
								if (next_loc.isUpperPoint()) return tangentPoints[4];
								else return tangentPoints[0];
							}
						}
						// previous element is a rope holder -> tangent lines between both circles (this and nextLoc)
						// and get the correct tangent point
						else {
							int tangent;
							if ((tangent = wichTangentPoint(prev_loc, next_loc, OUTPOINT)) == 0) return tangentPoints[1];
							else if (tangent == 1) return tangentPoints[0];
							else if (tangent == 2) return tangentPoints[5];
							else if (tangent == 3) return tangentPoints[4];
						}
					}
					// if there is a double roll on a cabin as previous element
					else if (prevElement instanceof DoppelUmlenkrolle) {
						int prevOutX = 0;
						if (prev_loc.getPrev() != null && ((AufzugschachtElement)prev_loc.getPrev().getElement()).getMPoint().x > ((AufzugschachtElement)prev_loc.getElement()).getMPoint().x) {
							prevOutX = ((AufzugschachtElement)prev_loc.getElement()).getMPoint().x - ((AufzugschachtElement)prev_loc.getElement()).getWidth() / 2; 
						} 
						else {
							prevOutX = ((AufzugschachtElement)prev_loc.getElement()).getMPoint().x + ((AufzugschachtElement)prev_loc.getElement()).getWidth() / 2;
						}
						if (next_loc.getInPoint().x <= prevOutX) {
							upperPoint = false;
							if (next_loc.isUpperPoint()) return tangentPoints[4];
							else return tangentPoints[0];
						}
						else {
							upperPoint = true;
							if (next_loc.isUpperPoint()) return tangentPoints[1];
							else return tangentPoints[5];
						}
					}
					else {
						int tangent = wichTangentPoint(prev_loc, next_loc, OUTPOINT);
						// previous element is a roll -> tangent lines between both circles
						// and get the correct tangent point
						if (prev_loc.getElement() instanceof Umlenkrolle && ((Umlenkrolle)prev_loc.getElement()).isFree()) {
							int prevOutX = 0;
							if (prev_loc.getPrev() != null && ((AufzugschachtElement)prev_loc.getPrev().getElement()).getMPoint().x > ((AufzugschachtElement)prev_loc.getElement()).getMPoint().x) {
								prevOutX = ((AufzugschachtElement)prev_loc.getElement()).getMPoint().x - ((AufzugschachtElement)prev_loc.getElement()).getWidth() / 2; 
							} 
							else {
								prevOutX = ((AufzugschachtElement)prev_loc.getElement()).getMPoint().x + ((AufzugschachtElement)prev_loc.getElement()).getWidth() / 2;
							}
							if (element.getMPoint().x <= prevOutX) {
								upperPoint = false;
								if (next_loc.isUpperPoint()) return tangentPoints[4];
								else return tangentPoints[0];
							}
							else {
								upperPoint = true;
								if (next_loc.isUpperPoint()) return tangentPoints[1];
								else return tangentPoints[5];
							}
						}
						if (next_loc.getElement() instanceof Umlenkrolle && ((Umlenkrolle)next_loc.getElement()).isFree()) {
							if (next_loc.getInPoint().x <= prev_loc.getOutPoint().x) {
								upperPoint = false;
								if (next_loc.isUpperPoint()) return tangentPoints[4];
								else return tangentPoints[0];
							}
							else {
								upperPoint = true;
								if (next_loc.isUpperPoint()) return tangentPoints[1];
								else return tangentPoints[5];
							}
						}
						else {
							if (tangent == 0) {
								if (!thisRolle.isOtherRope()) {
									return tangentPoints[1];
								}
								else {
									upperPoint = false;
									return tangentPoints[4];
								}
							}
							else if (tangent == 1) {
								if (!thisRolle.isOtherRope()) {
									return tangentPoints[0];
								}
								else {
									upperPoint = true;
									return tangentPoints[5];
								}
							}
							else if (tangent == 2) {
								if (!thisRolle.isOtherRope()) {
									return tangentPoints[5];
								}
								else {
									upperPoint = false;
									return tangentPoints[0];
								}
							}
							else if (tangent == 3) {
								if (!thisRolle.isOtherRope()) {
									return tangentPoints[4];
								}
								else {
									upperPoint = true;
									return tangentPoints[1];
								}
							}
						}
					}
				}
				else return tangentPoints[0];
			}
			// nextLoc element is a double roll at a cabin
			else if (next_loc != null && next_loc.getElement() instanceof DoppelUmlenkrolle) {
				if (prev_loc != null) {
					if (next_loc.getInPoint().x <= prev_loc.getOutPoint().x) {
						upperPoint = false;
						return new Point(thisRolle.getX(), thisRolle.getMPoint().y);
					}
					else {
						upperPoint = true;
						return new Point(thisRolle.getX() + thisRolle.getWidth(), thisRolle.getMPoint().y);
					}
				}
			}		
		}
		// get the outPoint of the double roll
		// only the x-coordinate can vary because the rope is hanging vertically
		else if (element instanceof AufzugschachtDoppelUmlenkrolle) {
			AufzugschachtDoppelUmlenkrolle rolle = (AufzugschachtDoppelUmlenkrolle)element;
			if (next_loc != null && prev_loc != null) {
				if (next_loc.getElement() instanceof Rolle) {
					if (prev_loc.getElement() instanceof Seilaufhaenger) {
						return new Point(next_loc.getInPoint().x, rolle.getMPoint().y);
					}
				}
			}
			if (next_loc != null && prev_loc != null) {
				int prevOutX = 0;
				if (prev_loc.getPrev() != null) {
					if (((AufzugschachtElement)prev_loc.getPrev().getElement()).getMPoint().x > ((AufzugschachtElement)prev_loc.getElement()).getMPoint().x) {
						prevOutX = ((AufzugschachtElement)prev_loc.getElement()).getMPoint().x - ((AufzugschachtElement)prev_loc.getElement()).getWidth() / 2; 
					} 
					else {
						prevOutX = ((AufzugschachtElement)prev_loc.getElement()).getMPoint().x + ((AufzugschachtElement)prev_loc.getElement()).getWidth() / 2;
					}
				}
				else {
					prevOutX = prev_loc.getOutPoint().x;
				}
				if ((prevOutX < rolle.getMPoint().x )) {
					return new Point(rolle.getMPoint().x + (rolle.getWidth() / 2), rolle.getMPoint().y);
				}
				else {
					return new Point(rolle.getMPoint().x - (rolle.getWidth() / 2), rolle.getMPoint().y);
				}
			}
		}
		// otherwise return the middle point
		return new Point(element.getSeilX(), element.getSeilY());
	}

	/**
	 * Gets the point in which the rope goes into the element.
	 * Depends on the previous element. Sometimes also on the nextLoc element.
	 * 
	 * @return the ingoing point
	 */
	public Point getInPoint() {
        AufzugschachtSeilElement prev_loc = (AufzugschachtSeilElement)prev;
        AufzugschachtSeilElement next_loc = (AufzugschachtSeilElement)next;
        
		logger.log(Level.FINER, "Get inPoint of element "+element);
		// if this element is a roll 
		if (element instanceof AufzugschachtRolle) {
			AufzugschachtRolle thisRolle = (AufzugschachtRolle)element;
			String rollName = thisRolle.getName()+thisRolle.getID();
			// if previous
			// no reverse bend possible
			if (prev_loc != null && prev_loc.getElement() instanceof Seilaufhaenger) {
				logger.log(Level.FINEST, "No reverse bend before "+thisRolle.getToolTipText()+" because previous element is a wire holder");
				thisRolle.setGegenbiegung(false);
				Seilaufhaenger seilaufhaenger = (Seilaufhaenger) prev_loc.getElement();
				// if there is hanging something at the rope holder
				if (seilaufhaenger.getHangingElement() instanceof Kapsel) {
					// catch loop exception
					if (next_loc != null && next_loc.getNext() != null && !(next_loc.getNext().getElement() instanceof Seilaufhaenger)) {
						if (thisRolle.getMPoint().x <= next_loc.getInPoint().x) {
							// set the roll variable upperPoint, because the inPoint is set directly and not
							// over the method 'wichTangentPoint()'
							upperPoint = false;
							return new Point(thisRolle.getX(), thisRolle.getMPoint().y);
						}
						else {
							upperPoint = true;
							return new Point(thisRolle.getX() + thisRolle.getWidth(), thisRolle.getMPoint().y);
						}
					}
					else if (next_loc != null && thisRolle.getMPoint().x <= ((AufzugschachtElement)next_loc.getElement()).getMPoint().x) {
						upperPoint = false;
						return new Point(thisRolle.getX(), thisRolle.getMPoint().y);
					} 
					else {
						upperPoint = true;
						return new Point(thisRolle.getX() + thisRolle.getWidth(), thisRolle.getMPoint().y);
					}
				}
				else {
					Point[] tangentPoints = new Point[2];
					// if there is nothing hanging at the rope holder, build tangent lines
					// between the middle point of the rope holder and this roll
					tangentPoints = TangentLines.getTangentPoints(thisRolle.getMPoint(), thisRolle.getRadius(), ((AufzugschachtSeilaufhaenger)seilaufhaenger).getMPoint());
					if (next_loc != null) {
						int tangent;
						// get the correct Tangent Point
						if ((tangent = wichTangentPoint(prev_loc, next_loc, INPOINT)) == 0) return tangentPoints[0];
						else if (tangent == 1) return tangentPoints[1];
						else if (tangent == 2) return tangentPoints[0];
						else if (tangent == 3) return tangentPoints[1];
					}
					// otherwise take some point  
					else return tangentPoints[0];
				}
			}
			// if the previous element is also a roll
			else if (prev_loc != null && prev_loc.getElement() instanceof AufzugschachtRolle) {
				AufzugschachtRolle prevRolle = (AufzugschachtRolle)prev_loc.getElement();
				Point[] tangentPoints = new Point[8];
				// build the tangent lines between this and the previous roll
				tangentPoints = TangentLines.getTangentPoints(thisRolle.getMPoint(), thisRolle.getRadius(), prevRolle.getMPoint(), prevRolle.getRadius());
				if (next_loc != null) {
					AufzugschachtElement nextElement = (AufzugschachtElement)next_loc.getElement();
					if (prev_loc.getPrev() != null 
							&& thisRolle.getRolle1teUmschlingung() != null
							&& prev_loc.getPrev().equals(thisRolle.getRolle1teUmschlingung().getSeilElement(true)) 
							&& nextElement instanceof Rolle 
							&& ((Rolle)nextElement).getRolle1teUmschlingung() != null
							&& prev_loc.equals(((Rolle)nextElement).getRolle1teUmschlingung().getSeilElement(true))) {
						// doppelte umschlingung
						// zweite umschlingung der ersten rolle bei eingang != ausgang
						logger.log(Level.FINEST, "No reverse bend before "+rollName);
						thisRolle.setGegenbiegung(false);
						int intangent = wichTangentPoint(prev_loc, next_loc, INPOINT);
						int previntangent = prev_loc.wichTangentPoint((AufzugschachtSeilElement)prev_loc.getPrev(), (AufzugschachtSeilElement)prev_loc.getNext(), INPOINT);
						
						if (intangent == 0) {
							if (previntangent == 0) return tangentPoints[0];
							if (previntangent == 2) return tangentPoints[1];
						}
						else if (intangent == 1) return tangentPoints[1];
						else if (intangent == 2) {
							if (previntangent == 1) return tangentPoints[1];
							if (previntangent == 3) return tangentPoints[0];
						}
						else if (intangent == 3) {
							if (previntangent == 0) return tangentPoints[0];
							if (previntangent == 2) return tangentPoints[1];
						}
					}
					else if (prev_loc.getPrev() != null 
							&& thisRolle.getRolle1teUmschlingung() != null
							&& prev_loc.getPrev().getPrev().getElement() instanceof Rolle
							&& ((Rolle)prev_loc.getPrev().getPrev().getElement()).getRolle2teUmschlingung() != null
							&& prev_loc.getPrev().equals(thisRolle.getRolle1teUmschlingung().getSeilElement(true)) 
							&& prev_loc.getPrev().getPrev() != null 
							&& prev_loc.getPrev().getPrev().getElement() instanceof Rolle
							&& prev_loc.equals(((Rolle)prev_loc.getPrev().getPrev().getElement()).getRolle2teUmschlingung().getSeilElement(true))) {
						// doppelte umschlingung
						// zweite umschlingung der zweiten rolle bei eingang != ausgang
						int intangent = wichTangentPoint(prev_loc, next_loc, INPOINT);
						int previntangent = ((AufzugschachtSeilElement)prev_loc.getPrev()).wichTangentPoint((AufzugschachtSeilElement)prev_loc.getPrev().getPrev(), (AufzugschachtSeilElement)prev_loc.getPrev().getNext(), INPOINT);

						if (intangent == 0) {
							if (previntangent == 0) {
								logger.log(Level.FINEST, "No reverse bend before "+rollName);
								thisRolle.setGegenbiegung(false);
								return tangentPoints[0];
							}
							if (previntangent == 3) {
								logger.log(Level.FINEST, "No reverse bend before "+rollName);
								thisRolle.setGegenbiegung(false);
								return tangentPoints[0];
							}
							if (previntangent == 2) {
								logger.log(Level.FINEST, "Reverse bend before "+rollName);
								thisRolle.setGegenbiegung(true);
								return tangentPoints[4];
							}
							if (previntangent == 1) {
								logger.log(Level.FINEST, "Reverse bend before "+rollName);
								thisRolle.setGegenbiegung(true);
								return tangentPoints[4];
							}
						}
						else if (intangent == 1) {
							if (previntangent == 2) {
								logger.log(Level.FINEST, "No reverse bend before "+rollName);
								thisRolle.setGegenbiegung(false);
								return tangentPoints[1];
							}
							if (previntangent == 0) {
								logger.log(Level.FINEST, "Reverse bend before "+rollName);
								thisRolle.setGegenbiegung(true);
								return tangentPoints[5];
							}
						}
						else if (intangent == 2) {
							if (previntangent == 0) {
								logger.log(Level.FINEST, "No reverse bend before "+rollName);
								thisRolle.setGegenbiegung(false);
								return tangentPoints[0];
							}
							if (previntangent == 2) {
								logger.log(Level.FINEST, "Reverse bend before "+rollName);
								thisRolle.setGegenbiegung(true);
								return tangentPoints[4];
							}
						}
						else if (intangent == 3) {
							if (previntangent == 2) {
								logger.log(Level.FINEST, "No reverse bend before "+rollName);
								thisRolle.setGegenbiegung(false);
								return tangentPoints[1];
							}
							if (previntangent == 1) {
								logger.log(Level.FINEST, "No reverse bend before "+rollName);
								thisRolle.setGegenbiegung(false);
								return tangentPoints[1];
							}
							if (previntangent == 0) {
								logger.log(Level.FINEST, "Reverse bend before "+rollName);
								thisRolle.setGegenbiegung(true);
								return tangentPoints[5];
							}
							if (previntangent == 3) {
								logger.log(Level.FINEST, "Reverse bend before "+rollName);
								thisRolle.setGegenbiegung(true);
								return tangentPoints[5];
							}
						}
						logger.severe("tangent point not reached");
					}
					else if (prev_loc.getPrev() != null 
							&& thisRolle.getRolle1teUmschlingung() != null
							&& prev_loc.getPrev().equals(thisRolle.getRolle1teUmschlingung().getSeilElement(true)) ) {
						// doppelte umschlingung
						// zweite umschlingung der ersten rolle bei eingang = ausgang
						int intangent = wichTangentPoint(prev_loc, next_loc, INPOINT);
						int previntangent = prev_loc.wichTangentPoint((AufzugschachtSeilElement)prev_loc.getPrev(), (AufzugschachtSeilElement)prev_loc.getNext(), INPOINT);

						if (intangent == 0) {
							if (previntangent == 0) {
								logger.log(Level.FINEST, "No reverse bend before "+rollName);
								thisRolle.setGegenbiegung(false);
								return tangentPoints[0];
							}
							if (previntangent == 2) {
								logger.log(Level.FINEST, "Reverse bend before "+rollName);
								thisRolle.setGegenbiegung(true);
								return tangentPoints[4];
							}
						}
						else if (intangent == 1) {
							if (previntangent == 1) {
								logger.log(Level.FINEST, "No reverse bend before "+rollName);
								thisRolle.setGegenbiegung(false);
								return tangentPoints[1];
							}
							if (previntangent == 3) {
								logger.log(Level.FINEST, "Reverse bend before "+rollName);
								thisRolle.setGegenbiegung(true);
								return tangentPoints[5];
							}
						}
						else if (intangent == 2) {
							if (previntangent == 3) {
								logger.log(Level.FINEST, "No reverse bend before "+rollName);
								thisRolle.setGegenbiegung(false);
								return tangentPoints[0];
							}
							if (previntangent == 1) {
								logger.log(Level.FINEST, "Reverse bend before "+rollName);
								thisRolle.setGegenbiegung(true);
								return tangentPoints[4];
							}
						}
						else if (intangent == 3) {
							if (previntangent == 2) {
								logger.log(Level.FINEST, "No reverse bend before "+rollName);
								thisRolle.setGegenbiegung(false);
								return tangentPoints[1];
							}
							if (previntangent == 0) {
								logger.log(Level.FINEST, "Reverse bend before "+rollName);
								thisRolle.setGegenbiegung(true);
								return tangentPoints[5];
							}
						}
					}
					else if (next_loc.getElement() instanceof Rolle
							&& prev_loc.getElement() instanceof Rolle
							&& ((Rolle)prev_loc.getElement()).getRolle2teUmschlingung() != null
							&& next_loc.equals(((Rolle)prev_loc.getElement()).getRolle2teUmschlingung().getSeilElement(true))) {
						// prevLoc = nextLoc
						// erste umschlingung der zweiten rolle
						thisRolle.setDoppelteUmschlingung(true);
						logger.log(Level.FINEST, "No reverse bend before "+rollName+" because it is a double enlacement");
						thisRolle.setGegenbiegung(false);
						int tangent = wichTangentPoint(prev_loc, next_loc, INPOINT);
						if (tangent == 0) return tangentPoints[0];
						else if (tangent == 1) return tangentPoints[1];
						else if (tangent == 2) return tangentPoints[1];
						else if (tangent == 3) return tangentPoints[0];
					}
					// if the nextLoc element is a rope holder
					else if (nextElement instanceof AufzugschachtSeilaufhaenger) {
						AufzugschachtSeilaufhaenger seilaufhaenger = (AufzugschachtSeilaufhaenger)nextElement;
						// if there is hanging something at the rope holder
						if (seilaufhaenger.getHangingElement() instanceof Kapsel) {
							// catch loop exception
							if (prev_loc.getPrev() != null && !(prev_loc.getPrev().getElement() instanceof Seilaufhaenger)) {
								if (nextElement.getMPoint().x <= prev_loc.getOutPoint().x + thisRolle.getRadius()) {
									if (prev_loc.isUpperPoint()) {
										logger.log(Level.FINEST, "Reverse bend before "+thisRolle.getToolTipText());
										thisRolle.setGegenbiegung(true);
										return tangentPoints[5];
									}
									else {
										logger.log(Level.FINEST, "No reverse bend before "+thisRolle.getToolTipText());
										thisRolle.setGegenbiegung(false);
										return tangentPoints[1];
									}
								}
								else {
									if (prev_loc.isUpperPoint()) {
										logger.log(Level.FINEST, "No reverse bend before "+thisRolle.getToolTipText());
										thisRolle.setGegenbiegung(false);
										return tangentPoints[0];
									}
									else {
										logger.log(Level.FINEST, "Reverse bend before "+thisRolle.getToolTipText());
										thisRolle.setGegenbiegung(false);
										return tangentPoints[4];
									}
								}
							}
							else if (nextElement.getMPoint().x <= ((AufzugschachtElement)prev_loc.getElement()).getMPoint().x + thisRolle.getRadius()) {
								if (prev_loc.isUpperPoint()) {
									logger.log(Level.FINEST, "Reverse bend before "+thisRolle.getToolTipText());
									thisRolle.setGegenbiegung(true);
									return tangentPoints[5];
								}
								else {
									logger.log(Level.FINEST, "No reverse bend before "+thisRolle.getToolTipText());
									thisRolle.setGegenbiegung(false);
									return tangentPoints[1];
								}
							}
							else {
								if (prev_loc.isUpperPoint()) {
									logger.log(Level.FINEST, "No reverse bend before "+thisRolle.getToolTipText());
									thisRolle.setGegenbiegung(false);
									return tangentPoints[0];
								}
								else {
									logger.log(Level.FINEST, "Reverse bend before "+thisRolle.getToolTipText());
									thisRolle.setGegenbiegung(false);
									return tangentPoints[4];
								}
							}	
						}
						else {
							int tangent;
							// the nextLoc element is a rope holder with no element hanging at it
							thisRolle.setGegenbiegung(false);
							tangent = wichTangentPoint(prev_loc, next_loc, INPOINT);
							if (element instanceof Umlenkrolle && ((Umlenkrolle)element).isFree()) {
								// get tangent points
								// free roll -> no reverse bend at all
								if (tangent == 0) {
									logger.log(Level.FINEST, "No reverse bend before "+thisRolle.getToolTipText());
									thisRolle.setGegenbiegung(false);
									return tangentPoints[0];
								}
								else if (tangent == 1) {
									logger.log(Level.FINEST, "No reverse bend before "+thisRolle.getToolTipText());
									thisRolle.setGegenbiegung(false);
									return tangentPoints[1];
								}
								else if (tangent == 2) {
									logger.log(Level.FINEST, "No reverse bend before "+thisRolle.getToolTipText());
									thisRolle.setGegenbiegung(false);
									return tangentPoints[4];
								}
								else if (tangent == 3) {
									logger.log(Level.FINEST, "No reverse bend before "+thisRolle.getToolTipText());
									thisRolle.setGegenbiegung(false);
									return tangentPoints[5];
								}
							}
							else {
								// !!! not possible any more
								logger.log(Level.WARNING, "Next element of a roll is a fix wire holder. I thought, this is not possible any more ?!");
							}
						}
					}
					// if the nextLoc element is a double roll on the top of a cabin
					else if (nextElement instanceof DoppelUmlenkrolle) {
						if (next_loc.getInPoint().x <= prev_loc.getOutPoint().x) {
							if (prev_loc.isUpperPoint()) {
								logger.log(Level.FINEST, "Reverse bend before "+thisRolle.getToolTipText());
								thisRolle.setGegenbiegung(false);
								return tangentPoints[5];
							}
							else {
								logger.log(Level.FINEST, "No reverse bend before "+thisRolle.getToolTipText());
								thisRolle.setGegenbiegung(false);
								return tangentPoints[1];
							}
						}
						else {
							if (prev_loc.isUpperPoint()) {
								logger.log(Level.FINEST, "No reverse bend before "+thisRolle.getToolTipText());
								thisRolle.setGegenbiegung(false);
								return tangentPoints[0];
							}
							else {
								logger.log(Level.FINEST, "Reverse bend before "+thisRolle.getToolTipText());
								thisRolle.setGegenbiegung(true);
								return tangentPoints[4];
							}
						}	
					}
					else {
						// nextLoc element is also a roll
						int tangent = wichTangentPoint(prev_loc, next_loc, INPOINT);
						// prevLoc is a hanging umlenkrolle
						if (prev_loc.getElement() instanceof Umlenkrolle && ((Umlenkrolle)prev_loc.getElement()).isFree()) {
							if (next_loc.getInPoint().x <= prev_loc.getOutPoint().x) {
								if (prev_loc.isUpperPoint()) {
									logger.log(Level.FINEST, "No reverse bend before "+thisRolle.getToolTipText());
									thisRolle.setGegenbiegung(false);
									return tangentPoints[5];
								}
								else {
									logger.log(Level.FINEST, "No reverse bend before "+thisRolle.getToolTipText());
									thisRolle.setGegenbiegung(false);
									return tangentPoints[1];
								}
							}
							else {
								if (prev_loc.isUpperPoint()) {
									logger.log(Level.FINEST, "No reverse bend before "+thisRolle.getToolTipText());
									thisRolle.setGegenbiegung(false);
									return tangentPoints[0];
								}
								else {
									logger.log(Level.FINEST, "No reverse bend before "+thisRolle.getToolTipText());
									thisRolle.setGegenbiegung(false);
									return tangentPoints[4];
								}
							}	
						}
						// nextLoc is a hanging umlenkrolle
						if (next_loc.getElement() instanceof Umlenkrolle && ((Umlenkrolle)next_loc.getElement()).isFree()) {
							int prevOutX = 0;
							if (prev_loc.getPrev() != null && ((AufzugschachtElement)prev_loc.getPrev().getElement()).getMPoint().x > ((AufzugschachtElement)prev_loc.getElement()).getMPoint().x) {
								prevOutX = ((AufzugschachtElement)prev_loc.getElement()).getMPoint().x - ((AufzugschachtElement)prev_loc.getElement()).getWidth() / 2; 
							} 
							else {
								prevOutX = ((AufzugschachtElement)prev_loc.getElement()).getMPoint().x + ((AufzugschachtElement)prev_loc.getElement()).getWidth() / 2;
							}
							if (next_loc.getInPoint().x <= prevOutX) {
								upperPoint = false;
								if (prev_loc.isUpperPoint()) {
									logger.log(Level.FINEST, "Reverse bend before "+thisRolle.getToolTipText());
									thisRolle.setGegenbiegung(true);
									return tangentPoints[5];
								}
								else {
									logger.log(Level.FINEST, "No reverse bend before "+thisRolle.getToolTipText());
									thisRolle.setGegenbiegung(false);
									return tangentPoints[1];
								}
							}
							else {
								upperPoint = true;
								if (prev_loc.isUpperPoint()) {
									logger.log(Level.FINEST, "No reverse bend before "+thisRolle.getToolTipText());
									thisRolle.setGegenbiegung(false);
									return tangentPoints[0];
								}
								else {
									logger.log(Level.FINEST, "Reverse bend before "+thisRolle.getToolTipText());
									thisRolle.setGegenbiegung(true);
									return tangentPoints[4];
								}
							}	
						}
						else {
							// all other rolls
							// maybe first enlancement of first roll
							if (tangent == 0) {
								if (!thisRolle.isOtherRope()) { 
									logger.log(Level.FINEST, "No reverse bend before "+rollName);
									thisRolle.setGegenbiegung(false);
									return tangentPoints[0];
								}
								else {
									upperPoint = false;
									logger.log(Level.FINEST, "Reverse bend before "+rollName);
									thisRolle.setGegenbiegung(true);
									return tangentPoints[5];
								}
							}
							else if (tangent == 1) {
								if (!thisRolle.isOtherRope()) { 
									logger.log(Level.FINEST, "No reverse bend before "+rollName);
									thisRolle.setGegenbiegung(false);
									return tangentPoints[1];
								}
								else {
									upperPoint = true;
									logger.log(Level.FINEST, "Reverse bend before "+rollName);
									thisRolle.setGegenbiegung(true);
									return tangentPoints[4];
								}
							}
							else if (tangent == 2) {
								if (!thisRolle.isOtherRope()) { 
									logger.log(Level.FINEST, "Reverse bend before "+rollName);
									thisRolle.setGegenbiegung(true);
									return tangentPoints[4];
								}
								else {
									upperPoint = false;
									logger.log(Level.FINEST, "No reverse bend before "+rollName);
									thisRolle.setGegenbiegung(false);
									return tangentPoints[1];
								}
							}
							else if (tangent == 3) {
								if (!thisRolle.isOtherRope()) { 
									logger.log(Level.FINEST, "Reverse bend before "+rollName);
									thisRolle.setGegenbiegung(true);
									return tangentPoints[5];
								}
								else {
									upperPoint = true;
									logger.log(Level.FINEST, "No reverse bend before "+rollName);
									thisRolle.setGegenbiegung(false);
									return tangentPoints[0];
								}
							}
						}
					}
				}
				else return tangentPoints[0];
			}
			// if previous element is a double roll at a cabin
			else if (prev_loc != null && prev_loc.getElement() instanceof DoppelUmlenkrolle) {
				logger.log(Level.FINEST, "No reverse bend before "+rollName);
				thisRolle.setGegenbiegung(false);
				if (next_loc != null) {
					int prevOutX = 0;
					if (prev_loc.getPrev() != null && ((AufzugschachtElement)prev_loc.getPrev().getElement()).getMPoint().x > ((AufzugschachtElement)prev_loc.getElement()).getMPoint().x) {
						prevOutX = ((AufzugschachtElement)prev_loc.getElement()).getMPoint().x - ((AufzugschachtElement)prev_loc.getElement()).getWidth() / 2; 
					} 
					else {
						prevOutX = ((AufzugschachtElement)prev_loc.getElement()).getMPoint().x + ((AufzugschachtElement)prev_loc.getElement()).getWidth() / 2;
					}
					if (next_loc.getInPoint().x <= prevOutX) {
						return new Point(thisRolle.getX() + thisRolle.getWidth(), thisRolle.getMPoint().y);
					}
					else {
						return new Point(thisRolle.getX(), thisRolle.getMPoint().y);
					}
				}
			}
		}
		// if this actual element is a double roll
		else if (element instanceof DoppelUmlenkrolle) {
			AufzugschachtDoppelUmlenkrolle rolle = (AufzugschachtDoppelUmlenkrolle)element;
			logger.log(Level.FINEST, "No reverse bend before "+rolle.getToolTipText()+" because of free roll");
			rolle.getRolle1().setGegenbiegung(false);
			rolle.getRolle2().setGegenbiegung(false);
			if (prev_loc != null && next_loc != null) {
				if (next_loc.getElement() instanceof Rolle) {
					if (prev_loc.getElement() instanceof Seilaufhaenger) {
						if (getOutPoint().x < rolle.getMPoint().x) {
							return new Point(rolle.getMPoint().x + rolle.getWidth() / 2, rolle.getMPoint().y);
						}
						else {
							return new Point(rolle.getMPoint().x - rolle.getWidth() / 2, rolle.getMPoint().y);
						}
					}
				}
			}
			if (prev_loc != null && next_loc != null) {
				int prevOutX = 0;
				if (prev_loc.getPrev() != null && ((AufzugschachtElement)prev_loc.getPrev().getElement()).getMPoint().x > ((AufzugschachtElement)prev_loc.getElement()).getMPoint().x) {
					prevOutX = ((AufzugschachtElement)prev_loc.getElement()).getMPoint().x - ((AufzugschachtElement)prev_loc.getElement()).getWidth() / 2; 
				} 
				else {
					prevOutX = ((AufzugschachtElement)prev_loc.getElement()).getMPoint().x + ((AufzugschachtElement)prev_loc.getElement()).getWidth() / 2;
				}
				if ((prevOutX < rolle.getMPoint().x )) {
					return new Point(rolle.getMPoint().x - (rolle.getWidth() / 2), rolle.getMPoint().y);
				}
				else {
					return new Point(rolle.getMPoint().x + (rolle.getWidth() / 2), rolle.getMPoint().y);
				}
			}
		}
		// otherwise return the middle point
		return new Point(element.getSeilX(), element.getSeilY());
	}
	
	/**
	 * Wich tangent point is the right one.
	 * inner? outer? upper? downer?
	 * 
	 * ---
	 * / 0  2\           -
	 * /       \        /   \
	 * \       /        \   /
	 * \ 1  3/           -
	 * ---
	 * 
	 * @param ident the identity of the element is the comparing one
	 * @param prevElement the previous element
	 * @param nextElement the nextLoc element
	 * 
	 * @return the integer mask of the tangent point
	 */
	//TODO: comment
	private int wichTangentPoint(AufzugschachtSeilElement prevElement, AufzugschachtSeilElement nextElement, String ident) {
		Point prevLoc = ((AufzugschachtElement)prevElement.getElement()).getMPoint();
		Point nextLoc = ((AufzugschachtElement)nextElement.getElement()).getMPoint();
		
		AufzugschachtSeilElement relevantElement;
		if (ident.equals(OUTPOINT)) relevantElement = nextElement;
		else relevantElement = prevElement;

		Umlenkrolle rollePrev, rolleNext;
		if (prevElement.getElement() instanceof Umlenkrolle) {
			rollePrev = (Umlenkrolle)prevElement.getElement();
		}
		else rollePrev = null;
		if (nextElement.getElement() instanceof Umlenkrolle) {
			rolleNext = (Umlenkrolle)nextElement.getElement();
		}
		else rolleNext = null;
		
		DoppelUmlenkrolle doppelRollePrev, doppelRolleNext;
		if (prevElement.getElement() instanceof DoppelUmlenkrolle) {
			doppelRollePrev = (DoppelUmlenkrolle)prevElement.getElement();
		}
		else doppelRollePrev = null;
		if (nextElement.getElement() instanceof DoppelUmlenkrolle) {
			doppelRolleNext = (DoppelUmlenkrolle)nextElement.getElement();
		}
		else doppelRolleNext = null;
		
				
		int tangentPoint = 0;
        AufzugschachtElement thisElement = (AufzugschachtElement)this.getElement();
		
		Point vecPrev = new Point(thisElement.getMPoint().x - prevLoc.x,  -thisElement.getMPoint().y - -prevLoc.y);
		Point vecNext = new Point(nextLoc.x - thisElement.getMPoint().x, -nextLoc.y - -thisElement.getMPoint().y);
		
		float steigungVecPrev = (float)vecPrev.y / vecPrev.x;
		float steigungVecNext = (float)vecNext.y / vecNext.x;
        
		if (getQuadrant(prevLoc, thisElement.getMPoint()) == 3) {
			if (getQuadrant(nextLoc, thisElement.getMPoint()) == 4) {
				if (isThisUpperPoint(relevantElement, ident)) tangentPoint = 3;
				else tangentPoint = 1;
				upperPoint = false;
			}
			if (getQuadrant(nextLoc, thisElement.getMPoint()) == 2) {
				if (isThisUpperPoint(relevantElement, ident)) tangentPoint = 0;
				else tangentPoint = 2;
				upperPoint = true;
			}
			if (getQuadrant(nextLoc, thisElement.getMPoint()) == 1) {
				if (steigungVecNext < steigungVecPrev
						|| (rollePrev != null && rollePrev.isFree())
						|| doppelRollePrev != null) {
					if (isThisUpperPoint(relevantElement, ident)) tangentPoint = 0;
					else tangentPoint = 2;
					upperPoint = true;
				}
				else {
					if (isThisUpperPoint(relevantElement, ident)) tangentPoint = 3;
					else tangentPoint = 1;
					upperPoint = false;
				}
			}
			if (getQuadrant(nextLoc, thisElement.getMPoint()) == 3) {
				if (steigungVecNext >= steigungVecPrev) {
					if (isThisUpperPoint(relevantElement, ident)) tangentPoint = 0;
					else tangentPoint = 2;
					upperPoint = true;
				}
				else {
					if (isThisUpperPoint(relevantElement, ident)) tangentPoint = 3;
					else tangentPoint = 1;
					upperPoint = false;
				}
			}
		}
		if (getQuadrant(prevLoc, thisElement.getMPoint()) == 4) {
			if (getQuadrant(nextLoc, thisElement.getMPoint()) == 1) {
				if (isThisUpperPoint(relevantElement, ident)) tangentPoint = 3;
				else tangentPoint = 1;
				upperPoint = false;
			}
			if (getQuadrant(nextLoc, thisElement.getMPoint()) == 3) {
				if (isThisUpperPoint(relevantElement, ident)) tangentPoint = 0;
				else tangentPoint = 2;
				upperPoint = true;
			}
			if (getQuadrant(nextLoc, thisElement.getMPoint()) == 2) {
				if (steigungVecNext < steigungVecPrev
						|| (rolleNext != null && rolleNext.isFree())
						|| doppelRolleNext != null) {
					if (isThisUpperPoint(relevantElement, ident)) tangentPoint = 0;
					else tangentPoint = 2;
					upperPoint = true;
				}
				else {
					if (isThisUpperPoint(relevantElement, ident)) tangentPoint = 3;
					else tangentPoint = 1;
					upperPoint = false;
				}
			}
			if (getQuadrant(nextLoc, thisElement.getMPoint()) == 4) {
				if (steigungVecNext >= steigungVecPrev) {
					if (isThisUpperPoint(relevantElement, ident)) tangentPoint = 0;
					else tangentPoint = 2;
					upperPoint = true;
				}
				else {
					if (isThisUpperPoint(relevantElement, ident)) tangentPoint = 3;
					else tangentPoint = 1;
					upperPoint = false;
				}
			}
		}
		if (getQuadrant(prevLoc, thisElement.getMPoint()) == 1) {
			if (getQuadrant(nextLoc, thisElement.getMPoint()) == 2) {
				if (isThisUpperPoint(relevantElement, ident)) tangentPoint = 3;
				else tangentPoint = 1;
				upperPoint = false;
			}
			if (getQuadrant(nextLoc, thisElement.getMPoint()) == 4) {
				if (isThisUpperPoint(relevantElement, ident)) tangentPoint = 0;
				else tangentPoint = 2;
				upperPoint = true;
			}
			if (getQuadrant(nextLoc, thisElement.getMPoint()) == 3) {
				if ((steigungVecNext > steigungVecPrev && steigungVecPrev != Float.NEGATIVE_INFINITY)
						|| (rolleNext != null && rolleNext.isFree())
						|| doppelRolleNext != null) {
					if (isThisUpperPoint(relevantElement, ident)) tangentPoint = 3;
					else tangentPoint = 1;
					upperPoint = false;
				}
				else {
					if (isThisUpperPoint(relevantElement, ident)) tangentPoint = 0;
					else tangentPoint = 2;
					upperPoint = true;
				}
			}
			if (getQuadrant(nextLoc, thisElement.getMPoint()) == 1) {
				if (steigungVecNext <= steigungVecPrev || steigungVecPrev == Float.NEGATIVE_INFINITY) {
					if (isThisUpperPoint(relevantElement, ident)) tangentPoint = 3;
					else tangentPoint = 1;
					upperPoint = false;
				}
				else {
					if (isThisUpperPoint(relevantElement, ident)) tangentPoint = 0;
					else tangentPoint = 2;
					upperPoint = true;
				}
			}
		}
		if (getQuadrant(prevLoc, thisElement.getMPoint()) == 2) {
			if (getQuadrant(nextLoc, thisElement.getMPoint()) == 3) {
				if (isThisUpperPoint(relevantElement, ident)) tangentPoint = 3;
				else tangentPoint = 1;
				upperPoint = false;
			}
			if (getQuadrant(nextLoc, thisElement.getMPoint()) == 1) {
				if (isThisUpperPoint(relevantElement, ident)) tangentPoint = 0;
				else tangentPoint = 2;
				upperPoint = true;
			}
			if (getQuadrant(nextLoc, thisElement.getMPoint()) == 4) {
				if ((steigungVecNext > steigungVecPrev || steigungVecPrev == Float.POSITIVE_INFINITY)
						|| (rollePrev != null && rollePrev.isFree())
						|| doppelRollePrev != null) {
					if (isThisUpperPoint(relevantElement, ident)) tangentPoint = 3;
					else tangentPoint = 1;
					upperPoint = false;
				}
				else {
					if (isThisUpperPoint(relevantElement, ident)) tangentPoint = 0;
					else tangentPoint = 2;
					upperPoint = true;
				}
			}
			if (getQuadrant(nextLoc, thisElement.getMPoint()) == 2) {
				if (steigungVecNext >= steigungVecPrev || steigungVecPrev == Float.POSITIVE_INFINITY) {
					if (isThisUpperPoint(relevantElement, ident)) tangentPoint = 0;
					else tangentPoint = 2;
					upperPoint = true;
				}
				else {
					if (isThisUpperPoint(relevantElement, ident)) tangentPoint = 3;
					else tangentPoint = 1;
					upperPoint = false;
				}
			}
		}
		logger.log(Level.FINEST, "In "+ident+" the calculated tangent point for previous element = "
				+prevElement+" and next element = "+nextElement+" is "+tangentPoint);
		return tangentPoint;
	}
	
	/**
	 * Checks if is this upper point. Determines the upperPoint parameter dependent on
	 * whether it is an OutPoint or InPoint
	 * 
	 * @param relevantElement the relevant element
	 * @param ident the identifier IN- or OUTPOINT
	 * 
	 * @return true, if is this upper point
	 */
	private boolean isThisUpperPoint(AufzugschachtSeilElement relevantElement, String ident) {
		if (relevantElement.isUpperPoint() && ident.equals(OUTPOINT)
				|| relevantElement.isUpperPoint() && ident.equals(INPOINT)) 
			return true;
		else return false;
	}
	
	/**
	 * Gets the quadrand, relative to the Point rel, wich is the null-point of
	 * the coordinate system
	 * 
	 *    ^
	 *    |
	 * 4  |  1
	 * ---|------>
	 * 3  |  2
	 *    |
	 * 
	 * Quadrand 1 and three are owners of the axes.
	 * 
	 * @param p the Point to check
	 * @param rel the relative Point
	 * 
	 * @return the quadrand
	 */
	private int getQuadrant(Point p, Point rel) {
		int ret = 0;
		
		if (p.x >= rel.x) {
			if (-p.y >= -rel.y) ret = 1;
			else ret = 2;
		}
		else if (-p.y <= -rel.y) ret = 3;
		else ret = 4;
		
		logger.log(Level.FINEST, "Quadrant "+ret+" at point "+p+ " relative to point "+rel);
		return ret;
    }
	
	/**
	 * Let the rope hang.
	 */
	// TODO: comment
	public void letSeilHang() {
		logger.log(Level.FINER, "Let the wire hang at its cabine or weight");
		if (element instanceof AufzugschachtSeilaufhaenger) {
			AufzugschachtSeilaufhaenger seilaufhaenger = (AufzugschachtSeilaufhaenger)element;
			if (seilaufhaenger.isFree()) {
				if (prev != null && prev.getElement() instanceof AufzugschachtRolle) {
					AufzugschachtRolle rolle = (AufzugschachtRolle)prev.getElement();
					((AufzugschachtElement)seilaufhaenger.getHangingElement()).setLocation(((AufzugschachtSeilElement)rolle.getSeilElement(false)).getOutPoint().x - (((AufzugschachtElement)seilaufhaenger.getHangingElement()).getWidth() / 2), ((AufzugschachtElement)seilaufhaenger.getHangingElement()).getY());
				}
				if (next != null && next.getElement() instanceof AufzugschachtRolle) {
					AufzugschachtRolle rolle = (AufzugschachtRolle)next.getElement();
					((AufzugschachtElement)seilaufhaenger.getHangingElement()).setLocation(((AufzugschachtSeilElement)rolle.getSeilElement(true)).getInPoint().x - (((AufzugschachtElement)seilaufhaenger.getHangingElement()).getWidth() / 2), ((AufzugschachtElement)seilaufhaenger.getHangingElement()).getY());
				}
				if (prev != null && prev.getElement() instanceof AufzugschachtSeilaufhaenger) {
					AufzugschachtSeilaufhaenger aufhaenger = (AufzugschachtSeilaufhaenger)prev.getElement();
					((AufzugschachtElement)seilaufhaenger.getHangingElement()).setLocation(((AufzugschachtSeilElement)aufhaenger.getSeilElement(false)).getOutPoint().x - (((AufzugschachtElement)seilaufhaenger.getHangingElement()).getWidth() / 2), ((AufzugschachtElement)seilaufhaenger.getHangingElement()).getY());
				}
				if (next != null && next.getElement() instanceof AufzugschachtSeilaufhaenger) {
					AufzugschachtSeilaufhaenger aufhaenger = (AufzugschachtSeilaufhaenger)next.getElement();
					((AufzugschachtElement)seilaufhaenger.getHangingElement()).setLocation(((AufzugschachtSeilElement)aufhaenger.getSeilElement(true)).getInPoint().x - (((AufzugschachtElement)seilaufhaenger.getHangingElement()).getWidth() / 2), ((AufzugschachtElement)seilaufhaenger.getHangingElement()).getY());
				}
			}
		}
		else if (element instanceof AufzugschachtUmlenkrolle) {
			AufzugschachtUmlenkrolle umlenkrolle = (AufzugschachtUmlenkrolle)element;
			if (umlenkrolle.isFree()) {
				if (prev != null && prev.getElement() instanceof AufzugschachtSeilaufhaenger) {
					AufzugschachtSeilaufhaenger aufhaenger = (AufzugschachtSeilaufhaenger)prev.getElement();
					aufhaenger.setLocation(getInPoint().x - (aufhaenger.getWidth() / 2), aufhaenger.getY());
				}
				if (next != null && next.getElement() instanceof AufzugschachtSeilaufhaenger) {
					AufzugschachtSeilaufhaenger aufhaenger = (AufzugschachtSeilaufhaenger)next.getElement();
					aufhaenger.setLocation(getOutPoint().x - (aufhaenger.getWidth() / 2), aufhaenger.getY());
				}
				if (prev != null && prev.getElement() instanceof AufzugschachtRolle) {
					AufzugschachtRolle rolle = (AufzugschachtRolle)prev.getElement();
					if (getInPoint().x < element.getMPoint().x) {
						((AufzugschachtElement)umlenkrolle.getHangingElement()).setLocation(((AufzugschachtSeilElement)rolle.getSeilElement(false)).getOutPoint().x - (((AufzugschachtElement)umlenkrolle.getHangingElement()).getWidth() / 2) + (umlenkrolle.getWidth() / 2), ((AufzugschachtElement)umlenkrolle.getHangingElement()).getY());
					}
					else {
						((AufzugschachtElement)umlenkrolle.getHangingElement()).setLocation(((AufzugschachtSeilElement)rolle.getSeilElement(false)).getOutPoint().x - (((AufzugschachtElement)umlenkrolle.getHangingElement()).getWidth() / 2) - (umlenkrolle.getWidth() / 2), ((AufzugschachtElement)umlenkrolle.getHangingElement()).getY());
					}
				}
				if (next != null && next.getElement() instanceof AufzugschachtRolle) {
					AufzugschachtRolle rolle = (AufzugschachtRolle)next.getElement();	
					if (getOutPoint().x < element.getMPoint().x) {
						((AufzugschachtElement)umlenkrolle.getHangingElement()).setLocation(((AufzugschachtSeilElement)rolle.getSeilElement(true)).getInPoint().x - (((AufzugschachtElement)umlenkrolle.getHangingElement()).getWidth() / 2) + (umlenkrolle.getWidth() / 2), ((AufzugschachtElement)umlenkrolle.getHangingElement()).getY());
					}
					else {
						((AufzugschachtElement)umlenkrolle.getHangingElement()).setLocation(((AufzugschachtSeilElement)rolle.getSeilElement(true)).getInPoint().x - (((AufzugschachtElement)umlenkrolle.getHangingElement()).getWidth() / 2) - (umlenkrolle.getWidth() / 2), ((AufzugschachtElement)umlenkrolle.getHangingElement()).getY());
					}
				}
			}
		}
		// TODO: haengt manchmal mit schraegen seilen !!!
		else if (element instanceof AufzugschachtDoppelUmlenkrolle) {
			AufzugschachtDoppelUmlenkrolle umlenkrolle = (AufzugschachtDoppelUmlenkrolle)element;
			if (prev != null && prev.getElement() instanceof AufzugschachtRolle) {
				AufzugschachtRolle rolle = (AufzugschachtRolle)prev.getElement();
				if (getInPoint().x < element.getMPoint().x) {
					((AufzugschachtElement)umlenkrolle.getHangingElement()).setLocation(((AufzugschachtSeilElement)rolle.getSeilElement(false)).getOutPoint().x - (((AufzugschachtElement)umlenkrolle.getHangingElement()).getWidth() / 2) + (umlenkrolle.getWidth() / 2), ((AufzugschachtElement)umlenkrolle.getHangingElement()).getY());
				}
				else {
					((AufzugschachtElement)umlenkrolle.getHangingElement()).setLocation(((AufzugschachtSeilElement)rolle.getSeilElement(false)).getOutPoint().x - (((AufzugschachtElement)umlenkrolle.getHangingElement()).getWidth() / 2) - (umlenkrolle.getWidth() / 2), ((AufzugschachtElement)umlenkrolle.getHangingElement()).getY());
				}
			}
			else if (next != null && next.getElement() instanceof AufzugschachtRolle) {
				AufzugschachtRolle rolle = (AufzugschachtRolle)next.getElement();	
				if (getOutPoint().x < element.getMPoint().x) {
					((AufzugschachtElement)umlenkrolle.getHangingElement()).setLocation(((AufzugschachtSeilElement)rolle.getSeilElement(true)).getInPoint().x - (((AufzugschachtElement)umlenkrolle.getHangingElement()).getWidth() / 2) + (umlenkrolle.getWidth() / 2), ((AufzugschachtElement)umlenkrolle.getHangingElement()).getY());
				}
				else {
					((AufzugschachtElement)umlenkrolle.getHangingElement()).setLocation(((AufzugschachtSeilElement)rolle.getSeilElement(true)).getInPoint().x - (((AufzugschachtElement)umlenkrolle.getHangingElement()).getWidth() / 2) - (umlenkrolle.getWidth() / 2), ((AufzugschachtElement)umlenkrolle.getHangingElement()).getY());
				}
			}
			if (prev != null && prev.getElement() instanceof AufzugschachtSeilaufhaenger) {
				AufzugschachtSeilaufhaenger aufhaenger = (AufzugschachtSeilaufhaenger)prev.getElement();
				aufhaenger.setLocation(getInPoint().x - (aufhaenger.getWidth() / 2), aufhaenger.getY());
			}
			else if (next != null && next.getElement() instanceof Seilaufhaenger) {
				AufzugschachtSeilaufhaenger aufhaenger = (AufzugschachtSeilaufhaenger)next.getElement();
				aufhaenger.setLocation(getOutPoint().x - (aufhaenger.getWidth() / 2), aufhaenger.getY());
			}
		}
	}

	/**
	 * Checks if is upper point.
	 * 
	 * @return the upperPoint
	 */
	public boolean isUpperPoint() {
		return upperPoint;
	}
	
	/**
	 * Gets the enlancement angle
	 * Always the smaller angle of the vector rule
	 * => cos(alpha) = (a * b) / (|a| * |b|) 
	 * 
	 * @return the enlancement angle in radians (0 ... pi)
	 */
	public double getUmschlingungswinkel() {
		double alpha = 0;
		int method = 0;
		
		if (getElement() instanceof Treibscheibe && prev != null && next != null) {
			if (method == 0) {
				AufzugschachtRolle rolle = (AufzugschachtRolle)getElement();
				Point vec1 = new Point(getInPoint().x - rolle.getMPoint().x, getInPoint().y - rolle.getMPoint().y);
				Point vec2 = new Point(getOutPoint().x - rolle.getMPoint().x, getOutPoint().y - rolle.getMPoint().y);
				double lenv1 = Math.sqrt(Math.pow(vec1.x, 2) + Math.pow(vec1.y, 2));
				double lenv2 = Math.sqrt(Math.pow(vec2.x, 2) + Math.pow(vec2.y, 2));

				// cut the number after 4 decimals
				double round = (vec1.x * vec2.x + vec1.y * vec2.y) / (lenv1 * lenv2);
				String roundString = Double.toString(round);
				if (roundString.indexOf(".") != -1 && roundString.substring(roundString.indexOf(".")).length() > 5) {
					roundString = roundString.substring(0, roundString.indexOf(".") + 5);
				}
				round = Double.parseDouble(roundString);
				// <--
				
				alpha = Math.acos(round);
				
				if (oneIsAboveLine()) {
					alpha = 2 * Math.PI - alpha;
				}
			}
			else if (method == 1) {
				Treibscheibe treibscheibe = (Treibscheibe)getElement();
				if (seil.isElement(getElement())) {
					if (getPrev() != null && getPrev().getElement() instanceof AufzugschachtUmlenkrolle) {
						AufzugschachtUmlenkrolle umlenkrolle = (AufzugschachtUmlenkrolle)getPrev().getElement();
						alpha = Math.PI - TangentLines.getAngleRadOfYcmAndXcm(treibscheibe.getDurchmesser()/2, umlenkrolle.getDurchmesser()/2, umlenkrolle.getHorizEntf(), umlenkrolle.getVertEntf());
					}
					else if (getNext() != null && getNext().getElement() instanceof AufzugschachtUmlenkrolle) {
						AufzugschachtUmlenkrolle umlenkrolle = (AufzugschachtUmlenkrolle)getNext().getElement();
						alpha = Math.PI - TangentLines.getAngleRadOfYcmAndXcm(treibscheibe.getDurchmesser()/2, umlenkrolle.getDurchmesser()/2, umlenkrolle.getHorizEntf(), umlenkrolle.getVertEntf());
					}
					else if (getNext() != null && getNext().getElement() instanceof Seilaufhaenger
							|| getPrev() != null && getPrev().getElement() instanceof Seilaufhaenger) {
						alpha = Math.PI;
					}
					else {
						logger.severe("Enlancement angle is null ???");
					}
				}
			}
		}
		else {
			logger.warning("Enlancement angle is only possible on the leading sheave and if it has a previous and next element");
		}
		
		return alpha;
	}
	
	/**
	 * Checks, if at least one in- or out-point of the nextLoc or previous
	 * element is above the line over the in- and out-point of this element.
	 * Above means, in relation to the middle-point of this element.
	 * 
	 * @return true, if at least one is above line
	 */
	private boolean oneIsAboveLine() {
        AufzugschachtSeilElement prev_loc = (AufzugschachtSeilElement)prev;
        AufzugschachtSeilElement next_loc = (AufzugschachtSeilElement)next;
        
		// line over in- and out-point
		// y = m * x + b;
		// negation of all y-values because of the other coordination system
		double m = (double)(-getInPoint().y - -getOutPoint().y) / (getInPoint().x - getOutPoint().x);
		double b = -getInPoint().y - m * getInPoint().x;
		
		boolean middlepointIsAboveLine = true;
		if (m * element.getMPoint().x + b >= -element.getMPoint().y) {
			middlepointIsAboveLine = false;
		}
		
		boolean prevIsAboveLine = true;
		if (m * prev_loc.getOutPoint().x + b >= -prev_loc.getOutPoint().y) {
			prevIsAboveLine = false;
		}
		
		boolean nextIsAboveLine = true;
		if (m * next_loc.getInPoint().x + b >= -next_loc.getInPoint().y) {
			nextIsAboveLine = false;
		}
		
		// decision !!!
		if (middlepointIsAboveLine && nextIsAboveLine && prevIsAboveLine) {
			return false;
		}
		else if (middlepointIsAboveLine) {
			return true;
		}
		else if (!middlepointIsAboveLine && nextIsAboveLine && prevIsAboveLine) {
			return true;
		}
		else {
			return false;
		}
	}
		
    /**
	 * Sets the nextLoc.
	 * 
	 * @param nextLoc the nextLoc to set
	 */
    @Override
	public void setNext(SeilElement next) {
		this.next = (AufzugschachtSeilElement)next;
		((AufzugschachtSeil)seil).refreshSeil();
	}

	/**
	 * Sets the prevLoc.
	 * 
	 * @param prevLoc the prevLoc to set
	 */
    @Override
	public void setPrev(SeilElement prev) {
		this.prev = (AufzugschachtSeilElement)prev;
		((AufzugschachtSeil)seil).refreshSeil();
	}
    
    /**
	 * Gets the element.
	 * 
	 * @return the element
	 */
	public Element getElement() {
		return element;
	}

	/**
	 * Gets the nextLoc.
	 * 
	 * @return the nextLoc
	 */
	public SeilElement getNext() {
		return next;
	}

	/**
	 * Gets the prevLoc.
	 * 
	 * @return the prevLoc
	 */
	public SeilElement getPrev() {
		return prev;
	}
}
