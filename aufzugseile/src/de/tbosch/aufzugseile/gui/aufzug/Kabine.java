package de.tbosch.aufzugseile.gui.aufzug;

/**
 * The class of kabine.
 * 
 * @author Thomas Bosch (tbosch@gmx.de)
 */
public class Kabine extends EndElement {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6342366019044114909L;

	/** The mass in kilogramms. */
	private int mass;
	
	/** The zuladung. */
	private int zuladung;
	
	/** The durchschnitt. */
	private double durchschnitt; 
	
	/** user profile */
	private double profil;
	
	/**
	 * Creates a new instance of Kabine Extends the element class.
	 * 
	 * @param w The width
	 * @param s The scale of width and height
	 * @param ys The scale of the y-coordinates
	 * @param a The elevator itself
	 * @param xs The scale of the x-coordinates
	 * @param h The height
	 * @param y The y-coordinates
	 * @param i The path of the icon
	 * @param x The x-coordinates
	 */
	public Kabine(int x, int y, int w, int h, double s, double xs, double ys, String i, Aufzugschacht a) {
		super(x, y, w, h, (w / 2), 0, s, xs, ys, i, a);
		setMass(1000);
		setZuladung(1000);
		setDurchschnitt(0.5);
		setProfil(0.8);
		setName("Kabine");
	}

	/**
	 * Gets the mass.
	 * 
	 * @return the mass
	 */
	public int getMass() {
		return mass;
	}

	/**
	 * Sets the mass.
	 * 
	 * @param mass the mass
	 */
	public void setMass(int mass) {
		this.mass = mass;
	}

	/**
	 * Gets the zuladung.
	 * 
	 * @return the zuladung
	 */
	public int getZuladung() {
		return zuladung;
	}

	/**
	 * Sets the zuladung.
	 * 
	 * @param zuladung the zuladung to set
	 */
	public void setZuladung(int zuladung) {
		this.zuladung = zuladung;
	}

	/**
	 * Gets the durchschnitt.
	 * 
	 * @return the durchschnitt
	 */
	public double getDurchschnitt() {
		return durchschnitt;
	}

	/**
	 * Sets the durchschnitt.
	 * 
	 * @param durchschnitt the durchschnitt
	 */
	public void setDurchschnitt(double durchschnitt) {
		this.durchschnitt = durchschnitt;
	}

	/**
	 * Gets the profile.
	 * 
	 * @return the profile
	 */
	public double getProfil() {
		return profil;
	}

	/**
	 * Sets the profile.
	 * 
	 * @param profil the profile
	 */
	public void setProfil(double profil) {
		this.profil = profil;
	}

}
