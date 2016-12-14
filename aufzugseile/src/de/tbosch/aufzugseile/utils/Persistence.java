package de.tbosch.aufzugseile.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.StreamException;
import com.thoughtworks.xstream.io.xml.DomDriver;

import de.tbosch.aufzugseile.berechnung.lebensdauer.Berechnung;
import de.tbosch.aufzugseile.gui.aufzug.Aufzugschacht;

/**
 * @author Thomas Bosch (tbosch@gmx.de)
 *
 */
public class Persistence {
	
	/**
	 * Writes the given objects to the file in xml-style
	 * 
	 * @param aufzugschacht
	 * @param berechnung
	 * @param file
	 */
	public static void writeObjectsToXMLFile(Aufzugschacht aufzugschacht, Berechnung berechnung, File file) {
		try {
			XStream xstream = new XStream(new DomDriver());
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			
			//xstream.alias("aufzugschacht", Aufzugschacht.class);
			//xstream.alias("umlenkrollen", Vector.class);
			//xstream.alias("treibscheibe", Treibscheibe.class);
			ObjectOutputStream oos = xstream.createObjectOutputStream(bw);
			oos.writeObject(aufzugschacht);
			oos.writeObject(berechnung);
			oos.close();
			//fw.write(xstream.toXML(aufzugschacht));
			bw.close();
		}
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Read the objects from xml file
	 * 
	 * @param file
	 * @return objects[] (index0: Aufzugschacht, index1: Berechnung)
	 */
	public static Object[] readObjectsFromXMLFile(File file) {
		Object[] objects = new Object[2];
		
		try {
			XStream xstream = new XStream(new DomDriver());
			BufferedReader br = new BufferedReader(new FileReader(file));
			ObjectInputStream ois = xstream.createObjectInputStream(br);

			objects[0] = ois.readObject();
			objects[1] = ois.readObject();
			ois.close();
			br.close();
		}
		catch (StreamException e) {
			Constants.LOGGER.severe("XML file could not be read - skip loading file");
			return null;
		}
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return objects;
	}
	
	
}
