package de.tbosch.aufzugseile.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;
import java.util.logging.Level;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import de.tbosch.aufzugseile.gui.SeilParameterFrame;

public class FileHelper {

	/**
	 * Copy.
	 * 
	 * @param src
	 *            the src
	 * @param dst
	 *            the dst
	 * 
	 * @throws IOException
	 *             the IO exception
	 */
	public static void copy(File src, File dst) throws IOException {
		InputStream in = new FileInputStream(src);
		OutputStream out = new FileOutputStream(dst);

		// Transfer bytes from in to out
		byte[] buf = new byte[1024];
		int len;
		while ((len = in.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
		in.close();
		out.close();
	}

	/**
	 * Loades the parameters from a given file into the frame and table.
	 * 
	 * @param file
	 *            The file to load from
	 * @param frame
	 *            The frame to fill
	 * @param dtm
	 *            The Table-Model to fill
	 * @param parameterTableModel
	 *            The parameters Table-Model
	 * @throws FileParseException
	 */
	public static void parseSeilparametersFile(File file, SeilParameterFrame frame, DefaultTableModel dtm, TableModel parameterTableModel)
			throws FileParseException {
		FileReader fr = null;
		try {
			fr = new FileReader(file);
			int r;
			int i = 0;
			int j = 0;
			String word = ""; //$NON-NLS-1$
			try {
				// while not end
				while ((r = fr.read()) != -1) {
					Vector<Object> nullVector = new Vector<Object>();
					if (dtm.getRowCount() < 1)
						dtm.addRow(nullVector);
					char c = (char) r;
					// parse word per word
					if (c != ';' && c != '\n') {
						word += c;
					} else {
						if (j == 0) {
							if (word.equals("true")) { //$NON-NLS-1$
								parameterTableModel.setValueAt(true, i, j);
							} else if (word.equals("false")) { //$NON-NLS-1$
								parameterTableModel.setValueAt(false, i, j);
							} else
								throw new FileParseException();
						} else if (j == 1) {
							if (word.equals("null")) { //$NON-NLS-1$
								parameterTableModel.setValueAt("", i, j); //$NON-NLS-1$
							} else {
								parameterTableModel.setValueAt(word, i, j);
							}
						} else {
							if (word.equals("null")) { //$NON-NLS-1$
								parameterTableModel.setValueAt(null, i, j);
							} else {
								try {
									parameterTableModel.setValueAt(Double.parseDouble(word), i, j);
								} catch (NumberFormatException e) {
									throw new FileParseException();
								}
							}
						}
						if (c == ';') {
							// jump to next column if ';' is found
							j++;
						} else {
							// if '\n' is found: create new row in table
							dtm.addRow(nullVector);
							i++;
							j = 0;
						}
						word = ""; //$NON-NLS-1$
					}
				}
				if (dtm.getRowCount() > 0)
					dtm.removeRow(dtm.getRowCount() - 1);
			} catch (IOException e) {
				Constants.LOGGER.severe("IOException while reading file " + file.getAbsolutePath()); //$NON-NLS-1$
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			Constants.LOGGER.severe("File not found: " + file.getAbsolutePath()); //$NON-NLS-1$
			e.printStackTrace();
		} finally {
			if (fr != null) {
				try {
					fr.close();
					frame.dispose();
				} catch (IOException e) {
					Constants.LOGGER.severe("IOException while closing file " + file.getAbsolutePath()); //$NON-NLS-1$
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Saves the parameters in a file.
	 * 
	 * @param file
	 *            The file to save to
	 * @param parameterTableModel
	 *            The model to save
	 * @throws FileSetWritableException
	 */
	public static void saveSeilparameterFile(File file, TableModel parameterTableModel) throws FileSetWritableException {
		FileWriter fw = null;
		try {
			if (file.setWritable(true) == false)
				throw new FileSetWritableException();
			fw = new FileWriter(file);
			String string = ""; //$NON-NLS-1$
			for (int i = 0; i < parameterTableModel.getRowCount(); i++) {
				// row per row
				for (int j = 0; j < parameterTableModel.getColumnCount(); j++) {
					// first column: boolean value
					if (j == 0) {
						if (parameterTableModel.getValueAt(i, j) != null && (Boolean) parameterTableModel.getValueAt(i, j) == true)
							string += "true;"; //$NON-NLS-1$
						else
							string += "false;"; //$NON-NLS-1$
					}
					// second column: name of type
					else if (j == 1) {
						string += (String) parameterTableModel.getValueAt(i, j) + ";"; //$NON-NLS-1$
					}
					// rest of columns: values
					else if (j == parameterTableModel.getColumnCount() - 1) {
						Object object = parameterTableModel.getValueAt(i, j);
						if (object == null) {
							string += "null\n"; //$NON-NLS-1$
						} else {
							string += ((Double) object).toString() + "\n"; //$NON-NLS-1$
						}
					} else {
						Object object = parameterTableModel.getValueAt(i, j);
						if (object == null) {
							string += "null;"; //$NON-NLS-1$
						} else {
							string += ((Double) object).toString() + ";"; //$NON-NLS-1$
						}
					}
				}
			}
			// write sting to file
			fw.write(string);
		} catch (IOException e) {
			Constants.LOGGER.severe("IOException while saving file " + file.getAbsolutePath()); //$NON-NLS-1$
			e.printStackTrace();
		} finally {
			if (fw != null) {
				try {
					fw.close();
					Constants.LOGGER.log(Level.INFO, "Save file: " + file); //$NON-NLS-1$
					// seilparameterFile.setReadOnly();
				} catch (IOException e) {
					Constants.LOGGER.severe("IOException while closing file " + file.getAbsolutePath()); //$NON-NLS-1$
					e.printStackTrace();
				}
			}
		}
	}

}
