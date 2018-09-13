package de.tbosch.aufzugseile.utils;

public class LongGrouping {
	
	private long value;
	
	private boolean grouping;
	
	public LongGrouping(long value) {
		this.value = value;
		this.grouping = false;
	}
	
	public LongGrouping(long value, boolean grouping) {
		this.value = value;
		this.grouping = grouping;
	}
	
	public String toString() {
		String string = Long.toString(value);
		
		if (grouping) {
			double digits = Math.floor(Math.log10(value));
			double points = Math.floor(digits / 3);
			for (int i = 0; i < points; i++) {
				String last = string.substring(string.length() - (i + 1) * 3 - i, string.length());
				String first = string.substring(0, string.length() - (i + 1) * 3 - i);
				string = first + "." + last;
			}
		}		
		return string;
	}

}
