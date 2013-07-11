package de.atlassoft.util;

import org.eclipse.swt.graphics.Color;

/**
 * This class defines new colors and provides
 * get methods to reach them.
 * 
 * @author Silvan Haeussermann
 */
public class ColorHelper {

	private static Color darkRed = new Color(null, 205, 0, 0);
	private static Color red = new Color(null, 255, 0, 0);
	private static Color darkOrange = new Color(null, 255, 127, 0);
	private static Color orange = new Color(null, 255, 158, 15);
	private static Color yellow = new Color(null, 255, 255, 0);

	/**
	 * Returns the color which the user wants
	 * 
	 * @param name
	 * 		The name of the color.
	 * @return
	 * 		The color.
	 */
	public static Color getColor(String name) {
		
		switch (name) {
		case "darkRed": return darkRed;
		case "red": return red;
		case "darkOrange": return darkOrange;
		case "orange": return orange;
		case "yellow": return yellow;
		default: return null;
		}
	}
}
