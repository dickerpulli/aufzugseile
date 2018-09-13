package de.tbosch.aufzugseile.utils;

public class PrintEnvironmentVariables {
	
	@SuppressWarnings("unchecked")
	public static void main (String ars[]) {
		for ( java.util.Enumeration e = System.getProperties().propertyNames(); e.hasMoreElements(); ) {
			String elem = e.nextElement().toString();
			System.out.println(elem + ": " + System.getProperty(elem));
		}
	}
	
}
