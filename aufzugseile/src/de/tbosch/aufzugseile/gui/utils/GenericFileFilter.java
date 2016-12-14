package de.tbosch.aufzugseile.gui.utils;

import java.io.File;
import javax.swing.filechooser.FileFilter;

// TODO: Auto-generated Javadoc
/**
 * The Class GenericFileFilter.
 */
public class GenericFileFilter extends FileFilter {

	/** The extension. */
	private String extension = null;

	/** The description. */
	private String description = null;

	/**
	 * The Constructor.
	 * 
	 * @param description the description
	 * @param ext the ext
	 */
	public GenericFileFilter(String description, String ext) {
		this.extension = ext;
		this.description = description;
	}

	/* (non-Javadoc)
	 * @see javax.swing.filechooser.FileFilter#accept(java.io.File)
	 */
	public boolean accept(File file) {
		String ext = file.getName();
		int i = ext.lastIndexOf('.');
		if (i > 0 && i < ext.length() - 1) {
			ext = ext.substring(i + 1).toLowerCase();
		}
		if (ext.equals(this.extension)) {
			return true;
		}
		return false;
	}

	/**
	 * Gets the extension.
	 * 
	 * @return the extension
	 */
	public String getExtension() {
		return this.extension;
	}

	/* (non-Javadoc)
	 * @see javax.swing.filechooser.FileFilter#getDescription()
	 */
	public String getDescription() {
		return this.description;
	}
	// ...
}
