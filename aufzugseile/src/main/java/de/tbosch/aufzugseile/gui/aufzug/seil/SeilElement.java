package de.tbosch.aufzugseile.gui.aufzug.seil;

import java.awt.Point;
import java.io.Serializable;
import java.util.logging.Level;

import de.tbosch.aufzugseile.gui.aufzug.DoppelUmlenkrolle;
import de.tbosch.aufzugseile.gui.aufzug.Element;
import de.tbosch.aufzugseile.gui.aufzug.Gewicht;
import de.tbosch.aufzugseile.gui.aufzug.Kabine;
import de.tbosch.aufzugseile.gui.aufzug.Rolle;
import de.tbosch.aufzugseile.gui.aufzug.Treibscheibe;
import de.tbosch.aufzugseile.gui.aufzug.Umlenkrolle;
import de.tbosch.aufzugseile.utils.Constants;
import de.tbosch.utilities.trigonometry.TangentLines;

/**
 * The Class SeilElement. Every element on the rope is an SeilElement. This class calculates the incoming and outgoing point of the rope
 * 
 * @author Thomas Bosch (tbosch@gmx.de)
 */
public class SeilElement implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 146086683336467052L;

	/** The Element. */
	private Element element;

	/** The previous SeilElement. */
	private SeilElement prev;

	/** The next SeilElement. */
	private SeilElement next;

	/** The if the rope goes over the upper point of the element. */
	private boolean upperPoint = true;

	/** The ident string for getOutPoint. */
	private static String OUTPOINT = "out";

	/** The ident string for getInPoint. */
	private static String INPOINT = "in";

	/** The seil. */
	private Seil seil;

	/**
	 * The Constructor.
	 * 
	 * @param element
	 *            the element
	 * @param seil
	 *            the seil
	 */
	public SeilElement(Element element, Seil seil) {
		this.element = element;
		this.seil = seil;
	}

	/**
	 * Gets the point in which the rope leaves the element.
	 * 
	 * @return the outgoing point
	 */
	public Point getOutPoint() {
		// if the actual element is a roll
		if (element instanceof Rolle) {
			Rolle thisRolle = (Rolle) element;
			// if the next element is a rope holder
			if (next != null && next.getElement() instanceof Seilaufhaenger) {
				Seilaufhaenger seilaufhaenger = (Seilaufhaenger) next.getElement();
				// if there is hanging something at the rope holder
				if (seilaufhaenger.getHangingElement() instanceof Kabine || seilaufhaenger.getHangingElement() instanceof Gewicht) {
					// catch loop in getInPoint/getOutPoint
					if (prev != null && prev.getPrev() != null && !(prev.getPrev().getElement() instanceof Seilaufhaenger)) {
						if (thisRolle.getMPoint().x <= prev.getOutPoint().x) {
							upperPoint = false;
							return new Point(thisRolle.getX(), thisRolle.getMPoint().y);
						} else {
							upperPoint = true;
							return new Point(thisRolle.getX() + thisRolle.getWidth(), thisRolle.getMPoint().y);
						}
					} else if (prev != null && thisRolle.getMPoint().x <= prev.getElement().getMPoint().x) {
						upperPoint = false;
						return new Point(thisRolle.getX(), thisRolle.getMPoint().y);
					} else {
						upperPoint = true;
						return new Point(thisRolle.getX() + thisRolle.getWidth(), thisRolle.getMPoint().y);
					}
				}
				// if there is nothing hanging at the rope holder
				else {
					Point[] tangentPoints = new Point[2];
					// tangent line between rope holder and roll
					tangentPoints = TangentLines.getTangentPoints(thisRolle.getMPoint(), thisRolle.getRadius(), seilaufhaenger.getMPoint());
					if (prev != null) {
						int tangent;
						// check out wich tangent point is the correct one
						if ((tangent = wichTangentPoint(prev, next, OUTPOINT)) == 0)
							return tangentPoints[1];
						else if (tangent == 1)
							return tangentPoints[0];
						else if (tangent == 2)
							return tangentPoints[1];
						else if (tangent == 3)
							return tangentPoints[0];
					} else
						return tangentPoints[0];
				}
			} else if (next != null && next.getElement() instanceof Rolle) {
				Rolle nextRolle = (Rolle) next.getElement();
				Point[] tangentPoints = new Point[8];
				// if the next element is also a roll -> create tangent lines on both circles
				tangentPoints = TangentLines.getTangentPoints(thisRolle.getMPoint(), thisRolle.getRadius(), nextRolle.getMPoint(), nextRolle.getRadius());
				if (prev != null) {
					Element prevElement = prev.getElement();
					Element nextElement = next.getElement();
					if (next.getNext() != null && thisRolle.getRolle2teUmschlingung() != null
							&& next.getNext().equals(thisRolle.getRolle2teUmschlingung().getSeilElement(true)) && prev.getElement() instanceof Rolle
							&& ((Rolle) prev.getElement()).getRolle2teUmschlingung() != null
							&& next.equals(((Rolle) prev.getElement()).getRolle2teUmschlingung().getSeilElement(true))) {
						// doppelte umschlingung
						// erste Umschlingung der zweiten Rolle bei eingang != ausgang
						thisRolle.setDoppelteUmschlingung(true);
						int intangent = wichTangentPoint(prev, next, INPOINT);
						int outtangent = wichTangentPoint(prev, next, OUTPOINT);

						if (outtangent == 0) {
							if (intangent == 2)
								return tangentPoints[0];
							if (intangent == 0)
								return tangentPoints[1];
						}
						// ot == 1 not reachable ???
						else if (outtangent == 1)
							return tangentPoints[0];
						else if (outtangent == 2) {
							if (intangent == 0)
								return tangentPoints[1];
							if (intangent == 2)
								return tangentPoints[0];
						} else if (outtangent == 3) {
							if (intangent == 3)
								return tangentPoints[1];
							if (intangent == 1)
								return tangentPoints[0];
						}
					} else if (next.getNext() != null && thisRolle.getRolle2teUmschlingung() != null
							&& next.getNext().equals(thisRolle.getRolle2teUmschlingung().getSeilElement(true))) {
						// doppelte umschlingung
						// erste umschlingung der ersten rolle
						thisRolle.setDoppelteUmschlingung(true);
						int outtangent = wichTangentPoint(prev, next, OUTPOINT);

						if (outtangent == 0)
							return tangentPoints[1];
						else if (outtangent == 1)
							return tangentPoints[0];
						else if (outtangent == 2)
							return tangentPoints[1];
						else if (outtangent == 3)
							return tangentPoints[0];
					} else if (prev.getElement() instanceof Rolle && nextElement instanceof Rolle && ((Rolle) nextElement).getRolle1teUmschlingung() != null
							&& prev.equals(((Rolle) nextElement).getRolle1teUmschlingung().getSeilElement(true)) && prev.getPrev() != null
							&& thisRolle.getRolle1teUmschlingung() != null && prev.getPrev().equals(thisRolle.getRolle1teUmschlingung().getSeilElement(true))) {
						// doppelte umschlingung
						// zweite Umschlingung der ersten Rolle bei eingang != ausgang
						int outtangent = wichTangentPoint(prev, next, OUTPOINT);
						int previntangent = prev.wichTangentPoint(prev.getPrev(), prev.getNext(), INPOINT);

						if (outtangent == 0) {
							if (previntangent == 0)
								return tangentPoints[1];
							if (previntangent == 3)
								return tangentPoints[1];
							if (previntangent == 2)
								return tangentPoints[4];
							if (previntangent == 1)
								return tangentPoints[4];
						} else if (outtangent == 1) {
							if (previntangent == 0)
								return tangentPoints[5];
							if (previntangent == 1)
								return tangentPoints[5];
							if (previntangent == 2)
								return tangentPoints[0];
							if (previntangent == 3)
								return tangentPoints[0];
						} else if (outtangent == 2) {
							if (previntangent == 0)
								return tangentPoints[5];
							if (previntangent == 3)
								return tangentPoints[5];
							if (previntangent == 2)
								return tangentPoints[0];
							if (previntangent == 1)
								return tangentPoints[0];
						} else if (outtangent == 3) {
							if (previntangent == 0)
								return tangentPoints[1];
							if (previntangent == 2)
								return tangentPoints[4];
						}
						Constants.LOGGER.severe("tangent point not reached");
					} else if (prev.getElement() instanceof Rolle && nextElement instanceof Rolle && ((Rolle) nextElement).getRolle1teUmschlingung() != null
							&& prev.equals(((Rolle) nextElement).getRolle1teUmschlingung().getSeilElement(true))) {
						// prev = next
						// erste Umschlingung der zweiten rolle bei eingang = ausgang
						thisRolle.setDoppelteUmschlingung(true);
						int intangent = wichTangentPoint(prev, next, INPOINT);
						int outtangent = wichTangentPoint(prev, next, OUTPOINT);

						if (intangent == 0) {
							if (outtangent == 0)
								return tangentPoints[1];
							if (outtangent == 2)
								return tangentPoints[5];
						} else if (intangent == 1) {
							if (outtangent == 1)
								return tangentPoints[0];
							if (outtangent == 3)
								return tangentPoints[4];
						} else if (intangent == 2) {
							if (outtangent == 2)
								return tangentPoints[0];
							if (outtangent == 0)
								return tangentPoints[4];
						} else if (intangent == 3) {
							if (outtangent == 3)
								return tangentPoints[1];
							if (outtangent == 1)
								return tangentPoints[5];
						}
					}
					// if there is a rope holder as previous element
					else if (prevElement instanceof Seilaufhaenger) {
						Seilaufhaenger seilaufhaenger = (Seilaufhaenger) prevElement;
						// if there is hanging something at the rope holder
						if (seilaufhaenger.getHangingElement() instanceof Kabine || seilaufhaenger.getHangingElement() instanceof Gewicht) {
							// catch loop exception
							if (next.getNext() != null && !(next.getNext().getElement() instanceof Seilaufhaenger)) {
								if (prevElement.getMPoint().x <= next.getInPoint().x + thisRolle.getRadius()) {
									upperPoint = true;
									if (next.isUpperPoint())
										return tangentPoints[1];
									else
										return tangentPoints[5];
								} else {
									upperPoint = false;
									if (next.isUpperPoint())
										return tangentPoints[4];
									else
										return tangentPoints[0];
								}
							} else if (prevElement.getMPoint().x <= next.getElement().getMPoint().x + thisRolle.getRadius()) {
								upperPoint = true;
								if (next.isUpperPoint())
									return tangentPoints[1];
								else
									return tangentPoints[5];
							} else {
								upperPoint = false;
								if (next.isUpperPoint())
									return tangentPoints[4];
								else
									return tangentPoints[0];
							}
						}
						// previous element is a rope holder -> tangent lines between both circles (this and next)
						// and get the correct tangent point
						else {
							int tangent;
							if ((tangent = wichTangentPoint(prev, next, OUTPOINT)) == 0)
								return tangentPoints[1];
							else if (tangent == 1)
								return tangentPoints[0];
							else if (tangent == 2)
								return tangentPoints[5];
							else if (tangent == 3)
								return tangentPoints[4];
						}
					}
					// if there is a double roll on a cabin as previous element
					else if (prevElement instanceof DoppelUmlenkrolle) {
						int prevOutX = 0;
						if (prev.getPrev() != null && prev.getPrev().getElement().getMPoint().x > prev.getElement().getMPoint().x) {
							prevOutX = prev.getElement().getMPoint().x - prev.getElement().getWidth() / 2;
						} else {
							prevOutX = prev.getElement().getMPoint().x + prev.getElement().getWidth() / 2;
						}
						if (next.getInPoint().x <= prevOutX) {
							upperPoint = false;
							if (next.isUpperPoint())
								return tangentPoints[4];
							else
								return tangentPoints[0];
						} else {
							upperPoint = true;
							if (next.isUpperPoint())
								return tangentPoints[1];
							else
								return tangentPoints[5];
						}
					} else {
						int tangent = wichTangentPoint(prev, next, OUTPOINT);
						// previous element is a roll -> tangent lines between both circles
						// and get the correct tangent point
						if (prev.getElement() instanceof Umlenkrolle && ((Umlenkrolle) prev.getElement()).isFree()) {
							int prevOutX = 0;
							if (prev.getPrev() != null && prev.getPrev().getElement().getMPoint().x > prev.getElement().getMPoint().x) {
								prevOutX = prev.getElement().getMPoint().x - prev.getElement().getWidth() / 2;
							} else {
								prevOutX = prev.getElement().getMPoint().x + prev.getElement().getWidth() / 2;
							}
							if (element.getMPoint().x <= prevOutX) {
								upperPoint = false;
								if (next.isUpperPoint())
									return tangentPoints[4];
								else
									return tangentPoints[0];
							} else {
								upperPoint = true;
								if (next.isUpperPoint())
									return tangentPoints[1];
								else
									return tangentPoints[5];
							}
						}
						if (next.getElement() instanceof Umlenkrolle && ((Umlenkrolle) next.getElement()).isFree()) {
							if (next.getInPoint().x <= prev.getOutPoint().x) {
								upperPoint = false;
								if (next.isUpperPoint())
									return tangentPoints[4];
								else
									return tangentPoints[0];
							} else {
								upperPoint = true;
								if (next.isUpperPoint())
									return tangentPoints[1];
								else
									return tangentPoints[5];
							}
						} else {
							if (tangent == 0) {
								if (!thisRolle.isOtherRope()) {
									return tangentPoints[1];
								} else {
									upperPoint = false;
									return tangentPoints[4];
								}
							} else if (tangent == 1) {
								if (!thisRolle.isOtherRope()) {
									return tangentPoints[0];
								} else {
									upperPoint = true;
									return tangentPoints[5];
								}
							} else if (tangent == 2) {
								if (!thisRolle.isOtherRope()) {
									return tangentPoints[5];
								} else {
									upperPoint = false;
									return tangentPoints[0];
								}
							} else if (tangent == 3) {
								if (!thisRolle.isOtherRope()) {
									return tangentPoints[4];
								} else {
									upperPoint = true;
									return tangentPoints[1];
								}
							}
						}
					}
				} else
					return tangentPoints[0];
			}
			// next element is a double roll at a cabin
			else if (next != null && next.getElement() instanceof DoppelUmlenkrolle) {
				if (prev != null) {
					if (next.getInPoint().x <= prev.getOutPoint().x) {
						upperPoint = false;
						return new Point(thisRolle.getX(), thisRolle.getMPoint().y);
					} else {
						upperPoint = true;
						return new Point(thisRolle.getX() + thisRolle.getWidth(), thisRolle.getMPoint().y);
					}
				}
			}
		}
		// get the outPoint of the double roll
		// only the x-coordinate can vary because the rope is hanging vertically
		else if (element instanceof DoppelUmlenkrolle) {
			DoppelUmlenkrolle rolle = (DoppelUmlenkrolle) element;
			if (next != null && prev != null) {
				if (next.getElement() instanceof Rolle) {
					if (prev.getElement() instanceof Seilaufhaenger) {
						return new Point(next.getInPoint().x, rolle.getMPoint().y);
					}
				}
			}
			if (next != null && prev != null) {
				int prevOutX = 0;
				if (prev.getPrev() != null) {
					if (prev.getPrev().getElement().getMPoint().x > prev.getElement().getMPoint().x) {
						prevOutX = prev.getElement().getMPoint().x - prev.getElement().getWidth() / 2;
					} else {
						prevOutX = prev.getElement().getMPoint().x + prev.getElement().getWidth() / 2;
					}
				} else {
					prevOutX = prev.getOutPoint().x;
				}
				if ((prevOutX < rolle.getMPoint().x)) {
					return new Point(rolle.getMPoint().x + (rolle.getWidth() / 2), rolle.getMPoint().y);
				} else {
					return new Point(rolle.getMPoint().x - (rolle.getWidth() / 2), rolle.getMPoint().y);
				}
			}
		}
		// otherwise return the middle point
		return new Point(element.getSeilX(), element.getSeilY());
	}

	/**
	 * Gets the point in which the rope goes into the element. Depends on the previous element. Sometimes also on the next element.
	 * 
	 * @return the ingoing point
	 */
	public Point getInPoint() {
		Constants.LOGGER.log(Level.FINER, "Get inPoint of element " + element);
		// if this element is a roll
		if (element instanceof Rolle) {
			Rolle thisRolle = (Rolle) element;
			String rollName = thisRolle.getName() + thisRolle.getID();
			// if previous
			// no reverse bend possible
			if (prev != null && prev.getElement() instanceof Seilaufhaenger) {
				Constants.LOGGER.log(Level.FINEST, "No reverse bend before " + thisRolle.getToolTipText() + " because previous element is a wire holder");
				thisRolle.setGegenbiegung(false);
				Seilaufhaenger seilaufhaenger = (Seilaufhaenger) prev.getElement();
				// if there is hanging something at the rope holder
				if (seilaufhaenger.getHangingElement() instanceof Kabine || seilaufhaenger.getHangingElement() instanceof Gewicht) {
					// catch loop exception
					if (next != null && next.getNext() != null && !(next.getNext().getElement() instanceof Seilaufhaenger)) {
						if (thisRolle.getMPoint().x <= next.getInPoint().x) {
							// set the roll variable upperPoint, because the inPoint is set directly and not
							// over the method 'wichTangentPoint()'
							upperPoint = false;
							return new Point(thisRolle.getX(), thisRolle.getMPoint().y);
						} else {
							upperPoint = true;
							return new Point(thisRolle.getX() + thisRolle.getWidth(), thisRolle.getMPoint().y);
						}
					} else if (next != null && thisRolle.getMPoint().x <= next.getElement().getMPoint().x) {
						upperPoint = false;
						return new Point(thisRolle.getX(), thisRolle.getMPoint().y);
					} else {
						upperPoint = true;
						return new Point(thisRolle.getX() + thisRolle.getWidth(), thisRolle.getMPoint().y);
					}
				} else {
					Point[] tangentPoints = new Point[2];
					// if there is nothing hanging at the rope holder, build tangent lines
					// between the middle point of the rope holder and this roll
					tangentPoints = TangentLines.getTangentPoints(thisRolle.getMPoint(), thisRolle.getRadius(), seilaufhaenger.getMPoint());
					if (next != null) {
						int tangent;
						// get the correct Tangent Point
						if ((tangent = wichTangentPoint(prev, next, INPOINT)) == 0)
							return tangentPoints[0];
						else if (tangent == 1)
							return tangentPoints[1];
						else if (tangent == 2)
							return tangentPoints[0];
						else if (tangent == 3)
							return tangentPoints[1];
					}
					// otherwise take some point
					else
						return tangentPoints[0];
				}
			}
			// if the previous element is also a roll
			else if (prev != null && prev.getElement() instanceof Rolle) {
				Rolle prevRolle = (Rolle) prev.getElement();
				Point[] tangentPoints = new Point[8];
				// build the tangent lines between this and the previous roll
				tangentPoints = TangentLines.getTangentPoints(thisRolle.getMPoint(), thisRolle.getRadius(), prevRolle.getMPoint(), prevRolle.getRadius());
				if (next != null) {
					Element nextElement = next.getElement();
					if (prev.getPrev() != null && thisRolle.getRolle1teUmschlingung() != null
							&& prev.getPrev().equals(thisRolle.getRolle1teUmschlingung().getSeilElement(true)) && nextElement instanceof Rolle
							&& ((Rolle) nextElement).getRolle1teUmschlingung() != null
							&& prev.equals(((Rolle) nextElement).getRolle1teUmschlingung().getSeilElement(true))) {
						// doppelte umschlingung
						// zweite umschlingung der ersten rolle bei eingang != ausgang
						Constants.LOGGER.log(Level.FINEST, "No reverse bend before " + rollName);
						thisRolle.setGegenbiegung(false);
						int intangent = wichTangentPoint(prev, next, INPOINT);
						int previntangent = prev.wichTangentPoint(prev.getPrev(), prev.getNext(), INPOINT);

						if (intangent == 0) {
							if (previntangent == 0)
								return tangentPoints[0];
							if (previntangent == 2)
								return tangentPoints[1];
						} else if (intangent == 1)
							return tangentPoints[1];
						else if (intangent == 2) {
							if (previntangent == 1)
								return tangentPoints[1];
							if (previntangent == 3)
								return tangentPoints[0];
						} else if (intangent == 3) {
							if (previntangent == 0)
								return tangentPoints[0];
							if (previntangent == 2)
								return tangentPoints[1];
						}
					} else if (prev.getPrev() != null && thisRolle.getRolle1teUmschlingung() != null && prev.getPrev().getPrev().getElement() instanceof Rolle
							&& ((Rolle) prev.getPrev().getPrev().getElement()).getRolle2teUmschlingung() != null
							&& prev.getPrev().equals(thisRolle.getRolle1teUmschlingung().getSeilElement(true)) && prev.getPrev().getPrev() != null
							&& prev.getPrev().getPrev().getElement() instanceof Rolle
							&& prev.equals(((Rolle) prev.getPrev().getPrev().getElement()).getRolle2teUmschlingung().getSeilElement(true))) {
						// doppelte umschlingung
						// zweite umschlingung der zweiten rolle bei eingang != ausgang
						int intangent = wichTangentPoint(prev, next, INPOINT);
						int previntangent = prev.getPrev().wichTangentPoint(prev.getPrev().getPrev(), prev.getPrev().getNext(), INPOINT);

						if (intangent == 0) {
							if (previntangent == 0) {
								Constants.LOGGER.log(Level.FINEST, "No reverse bend before " + rollName);
								thisRolle.setGegenbiegung(false);
								return tangentPoints[0];
							}
							if (previntangent == 3) {
								Constants.LOGGER.log(Level.FINEST, "No reverse bend before " + rollName);
								thisRolle.setGegenbiegung(false);
								return tangentPoints[0];
							}
							if (previntangent == 2) {
								Constants.LOGGER.log(Level.FINEST, "No reverse bend before " + rollName);
								thisRolle.setGegenbiegung(false);
								return tangentPoints[4];
							}
							if (previntangent == 1) {
								Constants.LOGGER.log(Level.FINEST, "Reverse bend before " + rollName);
								thisRolle.setGegenbiegung(true);
								return tangentPoints[4];
							}
						} else if (intangent == 1) {
							if (previntangent == 2) {
								Constants.LOGGER.log(Level.FINEST, "No reverse bend before " + rollName);
								thisRolle.setGegenbiegung(false);
								return tangentPoints[1];
							}
							if (previntangent == 0) {
								Constants.LOGGER.log(Level.FINEST, "Reverse bend before " + rollName);
								thisRolle.setGegenbiegung(true);
								return tangentPoints[5];
							}
						} else if (intangent == 2) {
							if (previntangent == 0) {
								Constants.LOGGER.log(Level.FINEST, "No reverse bend before " + rollName);
								thisRolle.setGegenbiegung(false);
								return tangentPoints[0];
							}
							if (previntangent == 2) {
								Constants.LOGGER.log(Level.FINEST, "Reverse bend before " + rollName);
								thisRolle.setGegenbiegung(true);
								return tangentPoints[4];
							}
						} else if (intangent == 3) {
							if (previntangent == 2) {
								Constants.LOGGER.log(Level.FINEST, "No reverse bend before " + rollName);
								thisRolle.setGegenbiegung(false);
								return tangentPoints[1];
							}
							if (previntangent == 1) {
								Constants.LOGGER.log(Level.FINEST, "No reverse bend before " + rollName);
								thisRolle.setGegenbiegung(false);
								return tangentPoints[1];
							}
							if (previntangent == 0) {
								Constants.LOGGER.log(Level.FINEST, "Reverse bend before " + rollName);
								thisRolle.setGegenbiegung(true);
								return tangentPoints[5];
							}
							if (previntangent == 3) {
								Constants.LOGGER.log(Level.FINEST, "No reverse bend before " + rollName);
								thisRolle.setGegenbiegung(false);
								return tangentPoints[5];
							}
						}
						Constants.LOGGER.severe("tangent point not reached");
					} else if (prev.getPrev() != null && thisRolle.getRolle1teUmschlingung() != null
							&& prev.getPrev().equals(thisRolle.getRolle1teUmschlingung().getSeilElement(true))) {
						// doppelte umschlingung
						// zweite umschlingung der ersten rolle bei eingang = ausgang
						int intangent = wichTangentPoint(prev, next, INPOINT);
						int previntangent = prev.wichTangentPoint(prev.getPrev(), prev.getNext(), INPOINT);

						if (intangent == 0) {
							if (previntangent == 0) {
								Constants.LOGGER.log(Level.FINEST, "No reverse bend before " + rollName);
								thisRolle.setGegenbiegung(false);
								return tangentPoints[0];
							}
							if (previntangent == 2) {
								Constants.LOGGER.log(Level.FINEST, "Reverse bend before " + rollName);
								thisRolle.setGegenbiegung(true);
								return tangentPoints[4];
							}
						} else if (intangent == 1) {
							if (previntangent == 1) {
								Constants.LOGGER.log(Level.FINEST, "No reverse bend before " + rollName);
								thisRolle.setGegenbiegung(false);
								return tangentPoints[1];
							}
							if (previntangent == 3) {
								Constants.LOGGER.log(Level.FINEST, "Reverse bend before " + rollName);
								thisRolle.setGegenbiegung(true);
								return tangentPoints[5];
							}
						} else if (intangent == 2) {
							if (previntangent == 3) {
								Constants.LOGGER.log(Level.FINEST, "No reverse bend before " + rollName);
								thisRolle.setGegenbiegung(false);
								return tangentPoints[0];
							}
							if (previntangent == 1) {
								Constants.LOGGER.log(Level.FINEST, "Reverse bend before " + rollName);
								thisRolle.setGegenbiegung(true);
								return tangentPoints[4];
							}
						} else if (intangent == 3) {
							if (previntangent == 2) {
								Constants.LOGGER.log(Level.FINEST, "No reverse bend before " + rollName);
								thisRolle.setGegenbiegung(false);
								return tangentPoints[1];
							}
							if (previntangent == 0) {
								Constants.LOGGER.log(Level.FINEST, "Reverse bend before " + rollName);
								thisRolle.setGegenbiegung(true);
								return tangentPoints[5];
							}
						}
					} else if (next.getElement() instanceof Rolle && prev.getElement() instanceof Rolle
							&& ((Rolle) prev.getElement()).getRolle2teUmschlingung() != null
							&& next.equals(((Rolle) prev.getElement()).getRolle2teUmschlingung().getSeilElement(true))) {
						// prev = next
						// erste umschlingung der zweiten rolle
						thisRolle.setDoppelteUmschlingung(true);
						Constants.LOGGER.log(Level.FINEST, "No reverse bend before " + rollName + " because it is a double enlacement");
						thisRolle.setGegenbiegung(false);
						int tangent = wichTangentPoint(prev, next, INPOINT);
						if (tangent == 0)
							return tangentPoints[0];
						else if (tangent == 1)
							return tangentPoints[1];
						else if (tangent == 2)
							return tangentPoints[1];
						else if (tangent == 3)
							return tangentPoints[0];
					}
					// if the next element is a rope holder
					else if (nextElement instanceof Seilaufhaenger) {
						Seilaufhaenger seilaufhaenger = (Seilaufhaenger) nextElement;
						// if there is hanging something at the rope holder
						if (seilaufhaenger.getHangingElement() instanceof Kabine || seilaufhaenger.getHangingElement() instanceof Gewicht) {
							// catch loop exception
							if (prev.getPrev() != null && !(prev.getPrev().getElement() instanceof Seilaufhaenger)) {
								if (nextElement.getMPoint().x <= prev.getOutPoint().x + thisRolle.getRadius()) {
									if (prev.isUpperPoint()) {
										Constants.LOGGER.log(Level.FINEST, "Reverse bend before " + thisRolle.getToolTipText());
										thisRolle.setGegenbiegung(true);
										return tangentPoints[5];
									} else {
										Constants.LOGGER.log(Level.FINEST, "No reverse bend before " + thisRolle.getToolTipText());
										thisRolle.setGegenbiegung(false);
										return tangentPoints[1];
									}
								} else {
									if (prev.isUpperPoint()) {
										Constants.LOGGER.log(Level.FINEST, "No reverse bend before " + thisRolle.getToolTipText());
										thisRolle.setGegenbiegung(false);
										return tangentPoints[0];
									} else {
										Constants.LOGGER.log(Level.FINEST, "Reverse bend before " + thisRolle.getToolTipText());
										thisRolle.setGegenbiegung(false);
										return tangentPoints[4];
									}
								}
							} else if (nextElement.getMPoint().x <= prev.getElement().getMPoint().x + thisRolle.getRadius()) {
								if (prev.isUpperPoint()) {
									Constants.LOGGER.log(Level.FINEST, "Reverse bend before " + thisRolle.getToolTipText());
									thisRolle.setGegenbiegung(true);
									return tangentPoints[5];
								} else {
									Constants.LOGGER.log(Level.FINEST, "No reverse bend before " + thisRolle.getToolTipText());
									thisRolle.setGegenbiegung(false);
									return tangentPoints[1];
								}
							} else {
								if (prev.isUpperPoint()) {
									Constants.LOGGER.log(Level.FINEST, "No reverse bend before " + thisRolle.getToolTipText());
									thisRolle.setGegenbiegung(false);
									return tangentPoints[0];
								} else {
									Constants.LOGGER.log(Level.FINEST, "Reverse bend before " + thisRolle.getToolTipText());
									thisRolle.setGegenbiegung(false);
									return tangentPoints[4];
								}
							}
						} else {
							int tangent;
							// the next element is a rope holder with no element hanging at it
							thisRolle.setGegenbiegung(false);
							tangent = wichTangentPoint(prev, next, INPOINT);
							if (element instanceof Umlenkrolle && ((Umlenkrolle) element).isFree()) {
								// get tangent points
								// free roll -> no reverse bend at all
								if (tangent == 0) {
									Constants.LOGGER.log(Level.FINEST, "No reverse bend before " + thisRolle.getToolTipText());
									thisRolle.setGegenbiegung(false);
									return tangentPoints[0];
								} else if (tangent == 1) {
									Constants.LOGGER.log(Level.FINEST, "No reverse bend before " + thisRolle.getToolTipText());
									thisRolle.setGegenbiegung(false);
									return tangentPoints[1];
								} else if (tangent == 2) {
									Constants.LOGGER.log(Level.FINEST, "No reverse bend before " + thisRolle.getToolTipText());
									thisRolle.setGegenbiegung(false);
									return tangentPoints[4];
								} else if (tangent == 3) {
									Constants.LOGGER.log(Level.FINEST, "No reverse bend before " + thisRolle.getToolTipText());
									thisRolle.setGegenbiegung(false);
									return tangentPoints[5];
								}
							} else {
								// !!! not possible any more
								Constants.LOGGER.log(Level.WARNING, "Next element of a roll is a fix wire holder. I thought, this is not possible any more ?!");
							}
						}
					}
					// if the next element is a double roll on the top of a cabin
					else if (nextElement instanceof DoppelUmlenkrolle) {
						if (next.getInPoint().x <= prev.getOutPoint().x) {
							if (prev.isUpperPoint()) {
								Constants.LOGGER.log(Level.FINEST, "Reverse bend before " + thisRolle.getToolTipText());
								thisRolle.setGegenbiegung(false);
								return tangentPoints[5];
							} else {
								Constants.LOGGER.log(Level.FINEST, "No reverse bend before " + thisRolle.getToolTipText());
								thisRolle.setGegenbiegung(false);
								return tangentPoints[1];
							}
						} else {
							if (prev.isUpperPoint()) {
								Constants.LOGGER.log(Level.FINEST, "No reverse bend before " + thisRolle.getToolTipText());
								thisRolle.setGegenbiegung(false);
								return tangentPoints[0];
							} else {
								Constants.LOGGER.log(Level.FINEST, "Reverse bend before " + thisRolle.getToolTipText());
								thisRolle.setGegenbiegung(true);
								return tangentPoints[4];
							}
						}
					} else {
						// next element is also a roll
						int tangent = wichTangentPoint(prev, next, INPOINT);
						// prev is a hanging umlenkrolle
						if (prev.getElement() instanceof Umlenkrolle && ((Umlenkrolle) prev.getElement()).isFree()) {
							if (next.getInPoint().x <= prev.getOutPoint().x) {
								if (prev.isUpperPoint()) {
									Constants.LOGGER.log(Level.FINEST, "No reverse bend before " + thisRolle.getToolTipText());
									thisRolle.setGegenbiegung(false);
									return tangentPoints[5];
								} else {
									Constants.LOGGER.log(Level.FINEST, "No reverse bend before " + thisRolle.getToolTipText());
									thisRolle.setGegenbiegung(false);
									return tangentPoints[1];
								}
							} else {
								if (prev.isUpperPoint()) {
									Constants.LOGGER.log(Level.FINEST, "No reverse bend before " + thisRolle.getToolTipText());
									thisRolle.setGegenbiegung(false);
									return tangentPoints[0];
								} else {
									Constants.LOGGER.log(Level.FINEST, "No reverse bend before " + thisRolle.getToolTipText());
									thisRolle.setGegenbiegung(false);
									return tangentPoints[4];
								}
							}
						}
						// next is a hanging umlenkrolle
						if (next.getElement() instanceof Umlenkrolle && ((Umlenkrolle) next.getElement()).isFree()) {
							int prevOutX = 0;
							if (prev.getPrev() != null && prev.getPrev().getElement().getMPoint().x > prev.getElement().getMPoint().x) {
								prevOutX = prev.getElement().getMPoint().x - prev.getElement().getWidth() / 2;
							} else {
								prevOutX = prev.getElement().getMPoint().x + prev.getElement().getWidth() / 2;
							}
							if (next.getInPoint().x <= prevOutX) {
								upperPoint = false;
								if (prev.isUpperPoint()) {
									Constants.LOGGER.log(Level.FINEST, "Reverse bend before " + thisRolle.getToolTipText());
									thisRolle.setGegenbiegung(true);
									return tangentPoints[5];
								} else {
									Constants.LOGGER.log(Level.FINEST, "No reverse bend before " + thisRolle.getToolTipText());
									thisRolle.setGegenbiegung(false);
									return tangentPoints[1];
								}
							} else {
								upperPoint = true;
								if (prev.isUpperPoint()) {
									Constants.LOGGER.log(Level.FINEST, "No reverse bend before " + thisRolle.getToolTipText());
									thisRolle.setGegenbiegung(false);
									return tangentPoints[0];
								} else {
									Constants.LOGGER.log(Level.FINEST, "Reverse bend before " + thisRolle.getToolTipText());
									thisRolle.setGegenbiegung(true);
									return tangentPoints[4];
								}
							}
						} else {
							// all other rolls
							// maybe first enlancement of first roll
							if (tangent == 0) {
								if (!thisRolle.isOtherRope()) {
									Constants.LOGGER.log(Level.FINEST, "No reverse bend before " + rollName);
									thisRolle.setGegenbiegung(false);
									return tangentPoints[0];
								} else {
									upperPoint = false;
									Constants.LOGGER.log(Level.FINEST, "Reverse bend before " + rollName);
									thisRolle.setGegenbiegung(true);
									return tangentPoints[5];
								}
							} else if (tangent == 1) {
								if (!thisRolle.isOtherRope()) {
									Constants.LOGGER.log(Level.FINEST, "No reverse bend before " + rollName);
									thisRolle.setGegenbiegung(false);
									return tangentPoints[1];
								} else {
									upperPoint = true;
									Constants.LOGGER.log(Level.FINEST, "Reverse bend before " + rollName);
									thisRolle.setGegenbiegung(true);
									return tangentPoints[4];
								}
							} else if (tangent == 2) {
								if (!thisRolle.isOtherRope()) {
									Constants.LOGGER.log(Level.FINEST, "Reverse bend before " + rollName);
									thisRolle.setGegenbiegung(true);
									return tangentPoints[4];
								} else {
									upperPoint = false;
									Constants.LOGGER.log(Level.FINEST, "No reverse bend before " + rollName);
									thisRolle.setGegenbiegung(false);
									return tangentPoints[1];
								}
							} else if (tangent == 3) {
								if (!thisRolle.isOtherRope()) {
									Constants.LOGGER.log(Level.FINEST, "Reverse bend before " + rollName);
									thisRolle.setGegenbiegung(true);
									return tangentPoints[5];
								} else {
									upperPoint = true;
									Constants.LOGGER.log(Level.FINEST, "No reverse bend before " + rollName);
									thisRolle.setGegenbiegung(false);
									return tangentPoints[0];
								}
							}
						}
					}
				} else
					return tangentPoints[0];
			}
			// if previous element is a double roll at a cabin
			else if (prev != null && prev.getElement() instanceof DoppelUmlenkrolle) {
				Constants.LOGGER.log(Level.FINEST, "No reverse bend before " + rollName);
				thisRolle.setGegenbiegung(false);
				if (next != null) {
					int prevOutX = 0;
					if (prev.getPrev() != null && prev.getPrev().getElement().getMPoint().x > prev.getElement().getMPoint().x) {
						prevOutX = prev.getElement().getMPoint().x - prev.getElement().getWidth() / 2;
					} else {
						prevOutX = prev.getElement().getMPoint().x + prev.getElement().getWidth() / 2;
					}
					if (next.getInPoint().x <= prevOutX) {
						return new Point(thisRolle.getX() + thisRolle.getWidth(), thisRolle.getMPoint().y);
					} else {
						return new Point(thisRolle.getX(), thisRolle.getMPoint().y);
					}
				}
			}
		}
		// if this actual element is a double roll
		else if (element instanceof DoppelUmlenkrolle) {
			DoppelUmlenkrolle rolle = (DoppelUmlenkrolle) element;
			Constants.LOGGER.log(Level.FINEST, "No reverse bend before " + rolle.getToolTipText() + " because of free roll");
			rolle.getRolle1().setGegenbiegung(false);
			rolle.getRolle2().setGegenbiegung(false);
			if (prev != null && next != null) {
				if (next.getElement() instanceof Rolle) {
					if (prev.getElement() instanceof Seilaufhaenger) {
						if (getOutPoint().x < rolle.getMPoint().x) {
							return new Point(rolle.getMPoint().x + rolle.getWidth() / 2, rolle.getMPoint().y);
						} else {
							return new Point(rolle.getMPoint().x - rolle.getWidth() / 2, rolle.getMPoint().y);
						}
					}
				}
			}
			if (prev != null && next != null) {
				int prevOutX = 0;
				if (prev.getPrev() != null && prev.getPrev().getElement().getMPoint().x > prev.getElement().getMPoint().x) {
					prevOutX = prev.getElement().getMPoint().x - prev.getElement().getWidth() / 2;
				} else {
					prevOutX = prev.getElement().getMPoint().x + prev.getElement().getWidth() / 2;
				}
				if ((prevOutX < rolle.getMPoint().x)) {
					return new Point(rolle.getMPoint().x - (rolle.getWidth() / 2), rolle.getMPoint().y);
				} else {
					return new Point(rolle.getMPoint().x + (rolle.getWidth() / 2), rolle.getMPoint().y);
				}
			}
		}
		// otherwise return the middle point
		return new Point(element.getSeilX(), element.getSeilY());
	}

	/**
	 * Wich tangent point is the right one. inner? outer? upper? downer?
	 * 
	 * --- / 0 2\ - / \ / \ \ / \ / \ 1 3/ - ---
	 * 
	 * @param ident
	 *            the identity of the element is the comparing one
	 * @param prevElement
	 *            the previous element
	 * @param nextElement
	 *            the next element
	 * 
	 * @return the integer mask of the tangent point
	 */
	// TODO: comment
	private int wichTangentPoint(SeilElement prevElement, SeilElement nextElement, String ident) {
		Point prev = prevElement.getElement().getMPoint();
		Point next = nextElement.getElement().getMPoint();

		SeilElement relevantElement;
		if (ident.equals(OUTPOINT))
			relevantElement = nextElement;
		else
			relevantElement = prevElement;

		Umlenkrolle rollePrev, rolleNext;
		if (prevElement.getElement() instanceof Umlenkrolle) {
			rollePrev = (Umlenkrolle) prevElement.getElement();
		} else
			rollePrev = null;
		if (nextElement.getElement() instanceof Umlenkrolle) {
			rolleNext = (Umlenkrolle) nextElement.getElement();
		} else
			rolleNext = null;

		DoppelUmlenkrolle doppelRollePrev, doppelRolleNext;
		if (prevElement.getElement() instanceof DoppelUmlenkrolle) {
			doppelRollePrev = (DoppelUmlenkrolle) prevElement.getElement();
		} else
			doppelRollePrev = null;
		if (nextElement.getElement() instanceof DoppelUmlenkrolle) {
			doppelRolleNext = (DoppelUmlenkrolle) nextElement.getElement();
		} else
			doppelRolleNext = null;

		int tangentPoint = 0;

		Point vecPrev = new Point(this.getElement().getMPoint().x - prev.x, -this.getElement().getMPoint().y - -prev.y);
		Point vecNext = new Point(next.x - this.getElement().getMPoint().x, -next.y - -this.getElement().getMPoint().y);

		float steigungVecPrev = (float) vecPrev.y / vecPrev.x;
		float steigungVecNext = (float) vecNext.y / vecNext.x;

		if (getQuadrant(prev, this.getElement().getMPoint()) == 3) {
			if (getQuadrant(next, this.getElement().getMPoint()) == 4) {
				if (isThisUpperPoint(relevantElement, ident))
					tangentPoint = 3;
				else
					tangentPoint = 1;
				upperPoint = false;
			}
			if (getQuadrant(next, this.getElement().getMPoint()) == 2) {
				if (isThisUpperPoint(relevantElement, ident))
					tangentPoint = 0;
				else
					tangentPoint = 2;
				upperPoint = true;
			}
			if (getQuadrant(next, this.getElement().getMPoint()) == 1) {
				if (steigungVecNext < steigungVecPrev || (rollePrev != null && rollePrev.isFree()) || doppelRollePrev != null) {
					if (isThisUpperPoint(relevantElement, ident))
						tangentPoint = 0;
					else
						tangentPoint = 2;
					upperPoint = true;
				} else {
					if (isThisUpperPoint(relevantElement, ident))
						tangentPoint = 3;
					else
						tangentPoint = 1;
					upperPoint = false;
				}
			}
			if (getQuadrant(next, this.getElement().getMPoint()) == 3) {
				if (steigungVecNext >= steigungVecPrev) {
					if (isThisUpperPoint(relevantElement, ident))
						tangentPoint = 0;
					else
						tangentPoint = 2;
					upperPoint = true;
				} else {
					if (isThisUpperPoint(relevantElement, ident))
						tangentPoint = 3;
					else
						tangentPoint = 1;
					upperPoint = false;
				}
			}
		}
		if (getQuadrant(prev, this.getElement().getMPoint()) == 4) {
			if (getQuadrant(next, this.getElement().getMPoint()) == 1) {
				if (isThisUpperPoint(relevantElement, ident))
					tangentPoint = 3;
				else
					tangentPoint = 1;
				upperPoint = false;
			}
			if (getQuadrant(next, this.getElement().getMPoint()) == 3) {
				if (isThisUpperPoint(relevantElement, ident))
					tangentPoint = 0;
				else
					tangentPoint = 2;
				upperPoint = true;
			}
			if (getQuadrant(next, this.getElement().getMPoint()) == 2) {
				if (steigungVecNext < steigungVecPrev || (rolleNext != null && rolleNext.isFree()) || doppelRolleNext != null) {
					if (isThisUpperPoint(relevantElement, ident))
						tangentPoint = 0;
					else
						tangentPoint = 2;
					upperPoint = true;
				} else {
					if (isThisUpperPoint(relevantElement, ident))
						tangentPoint = 3;
					else
						tangentPoint = 1;
					upperPoint = false;
				}
			}
			if (getQuadrant(next, this.getElement().getMPoint()) == 4) {
				if (steigungVecNext >= steigungVecPrev) {
					if (isThisUpperPoint(relevantElement, ident))
						tangentPoint = 0;
					else
						tangentPoint = 2;
					upperPoint = true;
				} else {
					if (isThisUpperPoint(relevantElement, ident))
						tangentPoint = 3;
					else
						tangentPoint = 1;
					upperPoint = false;
				}
			}
		}
		if (getQuadrant(prev, this.getElement().getMPoint()) == 1) {
			if (getQuadrant(next, this.getElement().getMPoint()) == 2) {
				if (isThisUpperPoint(relevantElement, ident))
					tangentPoint = 3;
				else
					tangentPoint = 1;
				upperPoint = false;
			}
			if (getQuadrant(next, this.getElement().getMPoint()) == 4) {
				if (isThisUpperPoint(relevantElement, ident))
					tangentPoint = 0;
				else
					tangentPoint = 2;
				upperPoint = true;
			}
			if (getQuadrant(next, this.getElement().getMPoint()) == 3) {
				if ((steigungVecNext > steigungVecPrev && steigungVecPrev != Float.NEGATIVE_INFINITY) || (rolleNext != null && rolleNext.isFree())
						|| doppelRolleNext != null) {
					if (isThisUpperPoint(relevantElement, ident))
						tangentPoint = 3;
					else
						tangentPoint = 1;
					upperPoint = false;
				} else {
					if (isThisUpperPoint(relevantElement, ident))
						tangentPoint = 0;
					else
						tangentPoint = 2;
					upperPoint = true;
				}
			}
			if (getQuadrant(next, this.getElement().getMPoint()) == 1) {
				if (steigungVecNext <= steigungVecPrev || steigungVecPrev == Float.NEGATIVE_INFINITY) {
					if (isThisUpperPoint(relevantElement, ident))
						tangentPoint = 3;
					else
						tangentPoint = 1;
					upperPoint = false;
				} else {
					if (isThisUpperPoint(relevantElement, ident))
						tangentPoint = 0;
					else
						tangentPoint = 2;
					upperPoint = true;
				}
			}
		}
		if (getQuadrant(prev, this.getElement().getMPoint()) == 2) {
			if (getQuadrant(next, this.getElement().getMPoint()) == 3) {
				if (isThisUpperPoint(relevantElement, ident))
					tangentPoint = 3;
				else
					tangentPoint = 1;
				upperPoint = false;
			}
			if (getQuadrant(next, this.getElement().getMPoint()) == 1) {
				if (isThisUpperPoint(relevantElement, ident))
					tangentPoint = 0;
				else
					tangentPoint = 2;
				upperPoint = true;
			}
			if (getQuadrant(next, this.getElement().getMPoint()) == 4) {
				if ((steigungVecNext > steigungVecPrev || steigungVecPrev == Float.POSITIVE_INFINITY) || (rollePrev != null && rollePrev.isFree())
						|| doppelRollePrev != null) {
					if (isThisUpperPoint(relevantElement, ident))
						tangentPoint = 3;
					else
						tangentPoint = 1;
					upperPoint = false;
				} else {
					if (isThisUpperPoint(relevantElement, ident))
						tangentPoint = 0;
					else
						tangentPoint = 2;
					upperPoint = true;
				}
			}
			if (getQuadrant(next, this.getElement().getMPoint()) == 2) {
				if (steigungVecNext >= steigungVecPrev || steigungVecPrev == Float.POSITIVE_INFINITY) {
					if (isThisUpperPoint(relevantElement, ident))
						tangentPoint = 0;
					else
						tangentPoint = 2;
					upperPoint = true;
				} else {
					if (isThisUpperPoint(relevantElement, ident))
						tangentPoint = 3;
					else
						tangentPoint = 1;
					upperPoint = false;
				}
			}
		}
		Constants.LOGGER.log(Level.FINEST, "In " + ident + " the calculated tangent point for previous element = " + prevElement + " and next element = "
				+ nextElement + " is " + tangentPoint);
		return tangentPoint;
	}

	/**
	 * Checks if is this upper point. Determines the upperPoint parameter dependent on whether it is an OutPoint or InPoint
	 * 
	 * @param relevantElement
	 *            the relevant element
	 * @param ident
	 *            the identifier IN- or OUTPOINT
	 * 
	 * @return true, if is this upper point
	 */
	private boolean isThisUpperPoint(SeilElement relevantElement, String ident) {
		if (relevantElement.isUpperPoint() && ident.equals(OUTPOINT) || relevantElement.isUpperPoint() && ident.equals(INPOINT))
			return true;
		else
			return false;
	}

	/**
	 * Gets the quadrand, relative to the Point rel, wich is the null-point of the coordinate system
	 * 
	 * ^ | 4 | 1 ---|------> 3 | 2 |
	 * 
	 * Quadrand 1 and three are owners of the axes.
	 * 
	 * @param p
	 *            the Point to check
	 * @param rel
	 *            the relative Point
	 * 
	 * @return the quadrand
	 */
	private int getQuadrant(Point p, Point rel) {
		int ret = 0;

		if (p.x >= rel.x) {
			if (-p.y >= -rel.y)
				ret = 1;
			else
				ret = 2;
		} else if (-p.y <= -rel.y)
			ret = 3;
		else
			ret = 4;

		Constants.LOGGER.log(Level.FINEST, "Quadrant " + ret + " at point " + p + " relative to point " + rel);
		return ret;
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
	 * Sets the next.
	 * 
	 * @param next
	 *            the next to set
	 */
	public void setNext(SeilElement next) {
		this.next = next;
		seil.refreshSeil();
	}

	/**
	 * Sets the prev.
	 * 
	 * @param prev
	 *            the prev to set
	 */
	public void setPrev(SeilElement prev) {
		this.prev = prev;
		seil.refreshSeil();
	}

	/**
	 * Let the rope hang.
	 */
	// TODO: comment
	public void letSeilHang() {
		Constants.LOGGER.log(Level.FINER, "Let the wire hang at its cabine or weight");
		if (element instanceof Seilaufhaenger) {
			Seilaufhaenger seilaufhaenger = (Seilaufhaenger) element;
			if (seilaufhaenger.isFree()) {
				if (prev != null && prev.getElement() instanceof Rolle) {
					Rolle rolle = (Rolle) prev.getElement();
					seilaufhaenger.getHangingElement().setLocation(
							rolle.getSeilElement(false).getOutPoint().x - (seilaufhaenger.getHangingElement().getWidth() / 2),
							seilaufhaenger.getHangingElement().getY());
				}
				if (next != null && next.getElement() instanceof Rolle) {
					Rolle rolle = (Rolle) next.getElement();
					seilaufhaenger.getHangingElement().setLocation(
							rolle.getSeilElement(true).getInPoint().x - (seilaufhaenger.getHangingElement().getWidth() / 2),
							seilaufhaenger.getHangingElement().getY());
				}
				if (prev != null && prev.getElement() instanceof Seilaufhaenger) {
					Seilaufhaenger aufhaenger = (Seilaufhaenger) prev.getElement();
					seilaufhaenger.getHangingElement().setLocation(
							aufhaenger.getSeilElement(false).getOutPoint().x - (seilaufhaenger.getHangingElement().getWidth() / 2),
							seilaufhaenger.getHangingElement().getY());
				}
				if (next != null && next.getElement() instanceof Seilaufhaenger) {
					Seilaufhaenger aufhaenger = (Seilaufhaenger) next.getElement();
					seilaufhaenger.getHangingElement().setLocation(
							aufhaenger.getSeilElement(true).getInPoint().x - (seilaufhaenger.getHangingElement().getWidth() / 2),
							seilaufhaenger.getHangingElement().getY());
				}
			}
		} else if (element instanceof Umlenkrolle) {
			Umlenkrolle umlenkrolle = (Umlenkrolle) element;
			if (umlenkrolle.isFree()) {
				if (prev != null && prev.getElement() instanceof Seilaufhaenger) {
					Seilaufhaenger aufhaenger = (Seilaufhaenger) prev.getElement();
					aufhaenger.setLocation(getInPoint().x - (aufhaenger.getWidth() / 2), aufhaenger.getY());
				}
				if (next != null && next.getElement() instanceof Seilaufhaenger) {
					Seilaufhaenger aufhaenger = (Seilaufhaenger) next.getElement();
					aufhaenger.setLocation(getOutPoint().x - (aufhaenger.getWidth() / 2), aufhaenger.getY());
				}
				if (prev != null && prev.getElement() instanceof Rolle) {
					Rolle rolle = (Rolle) prev.getElement();
					if (getInPoint().x < element.getMPoint().x) {
						umlenkrolle.getHangingElement().setLocation(
								rolle.getSeilElement(false).getOutPoint().x - (umlenkrolle.getHangingElement().getWidth() / 2) + (umlenkrolle.getWidth() / 2),
								umlenkrolle.getHangingElement().getY());
					} else {
						umlenkrolle.getHangingElement().setLocation(
								rolle.getSeilElement(false).getOutPoint().x - (umlenkrolle.getHangingElement().getWidth() / 2) - (umlenkrolle.getWidth() / 2),
								umlenkrolle.getHangingElement().getY());
					}
				}
				if (next != null && next.getElement() instanceof Rolle) {
					Rolle rolle = (Rolle) next.getElement();
					if (getOutPoint().x < element.getMPoint().x) {
						umlenkrolle.getHangingElement().setLocation(
								rolle.getSeilElement(true).getInPoint().x - (umlenkrolle.getHangingElement().getWidth() / 2) + (umlenkrolle.getWidth() / 2),
								umlenkrolle.getHangingElement().getY());
					} else {
						umlenkrolle.getHangingElement().setLocation(
								rolle.getSeilElement(true).getInPoint().x - (umlenkrolle.getHangingElement().getWidth() / 2) - (umlenkrolle.getWidth() / 2),
								umlenkrolle.getHangingElement().getY());
					}
				}
			}
		}
		// TODO: haengt manchmal mit schraegen seilen !!!
		else if (element instanceof DoppelUmlenkrolle) {
			DoppelUmlenkrolle umlenkrolle = (DoppelUmlenkrolle) element;
			if (prev != null && prev.getElement() instanceof Rolle) {
				Rolle rolle = (Rolle) prev.getElement();
				if (getInPoint().x < element.getMPoint().x) {
					umlenkrolle.getHangingElement().setLocation(
							rolle.getSeilElement(false).getOutPoint().x - (umlenkrolle.getHangingElement().getWidth() / 2) + (umlenkrolle.getWidth() / 2),
							umlenkrolle.getHangingElement().getY());
				} else {
					umlenkrolle.getHangingElement().setLocation(
							rolle.getSeilElement(false).getOutPoint().x - (umlenkrolle.getHangingElement().getWidth() / 2) - (umlenkrolle.getWidth() / 2),
							umlenkrolle.getHangingElement().getY());
				}
			} else if (next != null && next.getElement() instanceof Rolle) {
				Rolle rolle = (Rolle) next.getElement();
				if (getOutPoint().x < element.getMPoint().x) {
					umlenkrolle.getHangingElement().setLocation(
							rolle.getSeilElement(true).getInPoint().x - (umlenkrolle.getHangingElement().getWidth() / 2) + (umlenkrolle.getWidth() / 2),
							umlenkrolle.getHangingElement().getY());
				} else {
					umlenkrolle.getHangingElement().setLocation(
							rolle.getSeilElement(true).getInPoint().x - (umlenkrolle.getHangingElement().getWidth() / 2) - (umlenkrolle.getWidth() / 2),
							umlenkrolle.getHangingElement().getY());
				}
			}
			if (prev != null && prev.getElement() instanceof Seilaufhaenger) {
				Seilaufhaenger aufhaenger = (Seilaufhaenger) prev.getElement();
				aufhaenger.setLocation(getInPoint().x - (aufhaenger.getWidth() / 2), aufhaenger.getY());
			} else if (next != null && next.getElement() instanceof Seilaufhaenger) {
				Seilaufhaenger aufhaenger = (Seilaufhaenger) next.getElement();
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
	 * Gets the next.
	 * 
	 * @return the next
	 */
	public SeilElement getNext() {
		return next;
	}

	/**
	 * Gets the prev.
	 * 
	 * @return the prev
	 */
	public SeilElement getPrev() {
		return prev;
	}

	/**
	 * Gets the enlancement angle Always the smaller angle of the vector rule => cos(alpha) = (a * b) / (|a| * |b|)
	 * 
	 * @return the enlancement angle in radians (0 ... pi)
	 */
	public double getUmschlingungswinkel() {
		double alpha = 0;
		int method = 0;

		if (getElement() instanceof Treibscheibe && prev != null && next != null) {
			if (method == 0) {
				Rolle rolle = (Rolle) getElement();
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
			} else if (method == 1) {
				Treibscheibe treibscheibe = (Treibscheibe) getElement();
				if (seil.isElement(getElement())) {
					if (getPrev() != null && getPrev().getElement() instanceof Umlenkrolle) {
						Umlenkrolle umlenkrolle = (Umlenkrolle) getPrev().getElement();
						alpha = Math.PI
								- TangentLines.getAngleRadOfYcmAndXcm(treibscheibe.getDurchmesser() / 2, umlenkrolle.getDurchmesser() / 2, umlenkrolle
										.getHorizEntf(), umlenkrolle.getVertEntf());
					} else if (getNext() != null && getNext().getElement() instanceof Umlenkrolle) {
						Umlenkrolle umlenkrolle = (Umlenkrolle) getNext().getElement();
						alpha = Math.PI
								- TangentLines.getAngleRadOfYcmAndXcm(treibscheibe.getDurchmesser() / 2, umlenkrolle.getDurchmesser() / 2, umlenkrolle
										.getHorizEntf(), umlenkrolle.getVertEntf());
					} else if (getNext() != null && getNext().getElement() instanceof Seilaufhaenger || getPrev() != null
							&& getPrev().getElement() instanceof Seilaufhaenger) {
						alpha = Math.PI;
					} else {
						Constants.LOGGER.severe("Enlancement angle is null ???");
					}
				}
			}
		} else {
			Constants.LOGGER.warning("Enlancement angle is only possible on the leading sheave and if it has a previous and next element");
		}

		return alpha;
	}

	/**
	 * Checks, if at least one in- or out-point of the next or previous element is above the line over the in- and out-point of this element. Above means, in
	 * relation to the middle-point of this element.
	 * 
	 * @return true, if at least one is above line
	 */
	private boolean oneIsAboveLine() {
		// line over in- and out-point
		// y = m * x + b;
		// negation of all y-values because of the other coordination system
		double m = (double) (-getInPoint().y - -getOutPoint().y) / (getInPoint().x - getOutPoint().x);
		double b = -getInPoint().y - m * getInPoint().x;

		boolean middlepointIsAboveLine = true;
		if (m * getElement().getMPoint().x + b >= -getElement().getMPoint().y) {
			middlepointIsAboveLine = false;
		}

		boolean prevIsAboveLine = true;
		if (m * prev.getOutPoint().x + b >= -prev.getOutPoint().y) {
			prevIsAboveLine = false;
		}

		boolean nextIsAboveLine = true;
		if (m * next.getInPoint().x + b >= -next.getInPoint().y) {
			nextIsAboveLine = false;
		}

		// decision !!!
		if (middlepointIsAboveLine && nextIsAboveLine && prevIsAboveLine) {
			return false;
		} else if (middlepointIsAboveLine) {
			return true;
		} else if (!middlepointIsAboveLine && nextIsAboveLine && prevIsAboveLine) {
			return true;
		} else {
			return false;
		}
	}

}
