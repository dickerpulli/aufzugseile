package de.tbosch.seile.commons.elemente;

/**
 * The class of roll. The Umlenkrolle and Treibscheibe are both rolls.
 * 
 * @author Thomas Bosch (tbosch@gmx.de)
 */
public interface Rolle extends Element {
	
	public static String FORM_SITZ = "unterschnitt";
	
	public static String FORM_KEIL = "keil";
	
	public static String FORM_RUND = "rund";

	/**
	 * Gets the radius.
	 * 
	 * @return the radius
	 */
	public int getRadius();

	/**
	 * Gets the durchmesser.
	 * 
	 * @return the durchmesser
	 */
	public int getDurchmesser();

	/**
	 * Sets the durchmesser.
	 * 
	 * @param durchmesser the durchmesser to set
	 */
	public void setDurchmesser(int durchmesser);

	/**
	 * Gets the fn4 : schraegzug.
	 * 
	 * @return the fn4 : schraegzug
	 */
	public double getFn4();

	/**
	 * Sets the schraegzug.
	 * 
	 * @param schraegzug the schraegzug to set
	 */
	public void setSchraegzug(double schraegzug);

	/**
	 * Gets the fs2 : seiltriebwirkungsgrad.
	 * 
	 * @return the fs2 : seiltriebwirkungsgrad
	 */
	public double getFs2();

	/**
	 * Sets the seiltriebwirkungsgrad.
	 * 
	 * @param seiltriebwirkungsgrad the seiltriebwirkungsgrad to set
	 */
	public void setSeiltriebwirkungsgrad(double seiltriebwirkungsgrad);

	/**
	 * Gets the fn3 : Seilrille.
	 * 
	 * @return the fn3
	 */
	public double getFn3();

	/**
	 * Sets the seilrille.
	 * 
	 * @param seilrille the seilrille
	 */
	public void setSeilrille(double seilrille);

	/**
	 * Checks if is doppelte umschlingung.
	 * 
	 * @return the doppelteUmschlingung
	 */
	public boolean isDoppelteUmschlingung();

	/**
	 * Sets the doppelte umschlingung.
	 * 
	 * @param doppelteUmschlingung the doppelteUmschlingung to set
	 */
	public void setDoppelteUmschlingung(boolean doppelteUmschlingung);

	/**
	 * Gets the rolle2te umschlingung.
	 * 
	 * @return the rolle2teUmschlingung
	 */
	public Rolle getRolle2teUmschlingung();

	/**
	 * Gets the ID.
	 * 
	 * @return the ID
	 */
	public int getID();

	/**
	 * Sets the ID.
	 * 
	 * @param id the ID
	 */
	public void setID(int id);

	/**
	 * Checks if the roll is activated for calculation.
	 * 
	 * @return true, if is activated
	 */
	public boolean isActivated();

	/**
	 * Sets the activated.
	 * 
	 * @param activated the activated
	 */
	public void setActivated(boolean activated);

	/**
	 * Checks if is gegenbiegung.
	 * 
	 * @return true, if is gegenbiegung
	 */
	public boolean isGegenbiegung();

	/**
	 * Sets the gegenbiegung.
	 * 
	 * @param gegenbiegung the gegenbiegung
	 */
	public void setGegenbiegung(boolean gegenbiegung);

	/**
	 * Checks if is part of doppel umlenkrolle.
	 * 
	 * @return true, if is part of doppel umlenkrolle
	 */
	public boolean isPartOfDoppelUmlenkrolle();
    
	/**
	 * Gets the brother.
	 * 
	 * @return the brother
	 */
	public Umlenkrolle getBrother();

	/**
	 * Sets the brother.
	 * 
	 * @param brother the brother
	 */
	public void setBrother(Umlenkrolle brother);

	/**
	 * Gets the keilwinkel.
	 * 
	 * @return the keilwinkel
	 */
	public int getKeilwinkel();

	/**
	 * Sets the keilwinkel.
	 * 
	 * @param keilwinkel the keilwinkel
	 */
	public void setKeilwinkel(int keilwinkel);

	/**
	 * Gets the unterschnittwinkel.
	 * 
	 * @return the unterschnittwinkel
	 */
	public int getUnterschnittwinkel();

	/**
	 * Sets the unterschnittwinkel.
	 * 
	 * @param unterschnittwinkel the unterschnittwinkel
	 */
	public void setUnterschnittwinkel(int unterschnittwinkel);
	
	/**
	 * Gets the form.
	 * 
	 * @return the form
	 */
	public String getForm();

	/**
	 * Sets the form.
	 * 
	 * @param form the form
	 */
	public void setForm(String form);

	/**
	 * Checks if is zweite umschlingung.
	 * 
	 * @return true, if is zweite umschlingung
	 */
	public boolean isZweiteUmschlingung();

	/**
	 * Sets the zweite umschlingung.
	 * 
	 * @param zweiteUmschlingung the zweite umschlingung
	 */
	public void setZweiteUmschlingung(boolean zweiteUmschlingung);

	/**
	 * Gets the parent double roll, if existing.
	 * 
	 * @return the parent double roll if existing
	 */
	public DoppelUmlenkrolle getParentDoubleRoll();

	public Rolle getRolle1teUmschlingung();

	public void setRolle1teUmschlingung(Rolle rolle1teUmschlingung);

}
