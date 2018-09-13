package de.tbosch.aufzugseile.utils;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;

// TODO: Auto-generated Javadoc
/**
 * The Class IDPool. 
 */
public class IDPool implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7102987918935156478L;

	/** The known I ds. */
	private HashSet<Integer> knownIDs;
	
	/** The given I ds. */
	private HashSet<Integer> givenIDs;
	
	/** The max ID. */
	private int maxID;
	
	/**
	 * The Constructor.
	 */
	public IDPool(int maxID) {
		knownIDs = new HashSet<Integer>();
		givenIDs = new HashSet<Integer>();
		for (int i = 0; i < maxID; i++) {
			knownIDs.add(i);
		}
		this.maxID = maxID;
	}
	
	/**
	 * Gets the ID.
	 * 
	 * @return the ID. If no ID is available then it returns -1
	 */
	public int getID() {
		int id = -1;
		
		for (Iterator<Integer> it = knownIDs.iterator(); it.hasNext();) {
			int i = (Integer) it.next();
			if (!givenIDs.contains(i)) {
				id = i;
				givenIDs.add(i);
				break;
			}
		}
		
		return id;
	}
	
	/**
	 * Clears the whole IDPool.
	 */
	public void clear() {
		knownIDs = new HashSet<Integer>();
		givenIDs = new HashSet<Integer>();
		for (int i = 0; i < maxID; i++) {
			knownIDs.add(i);
		}
	}
	
	/**
	 * Removes the ID.
	 * 
	 * @param id the id
	 */
	public void removeID(int id) {
		if (givenIDs.contains(id)) {
			givenIDs.remove(id);
			knownIDs.add(id);
		}
	}
}
