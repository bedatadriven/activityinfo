package org.sigmah.server.util;

import java.awt.Color;

public final class ColorUtil {
	
	private ColorUtil() {}
	
	public static Color colorFromString(String color) {
		if (color.startsWith("#")) {
			color = color.substring(1);
		}

		Color result = new Color(0, 255, 0);
		try {
			result = new Color(Integer.parseInt(color));
		} catch (NumberFormatException e) {
			result = Color.decode("0x" + color);
		}
		return result;
	}

	public static int toInteger(String color) {
		return colorFromString(color).getRGB();
	}
}
