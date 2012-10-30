package org.activityinfo.shared.map;

import java.io.Serializable;

public class RgbColor implements Serializable {

	private int red, green, blue;

	private RgbColor() {
		
	}
	
	public RgbColor(int red, int green, int blue) {
		this.red = red;
		this.green = green;
		this.blue = blue;
	}
	
	public RgbColor(String hexString) {
		if(hexString.startsWith("#")) {
			hexString = hexString.substring(1);
		}
		this.red = Integer.parseInt(hexString.substring(0,2), 16);
		this.green = Integer.parseInt(hexString.substring(2,4), 16);
		this.blue = Integer.parseInt(hexString.substring(4,6), 16);	
	}

	public int getRed() {
		return red;
	}

	public int getGreen() {
		return green;
	}

	public int getBlue() {
		return blue;
	}
	
	public static RgbColor interpolate(RgbColor from, RgbColor to, double p) {
		return new RgbColor(
				interpolate(from.red, to.red, p),
				interpolate(from.green, to.green, p),
				interpolate(from.blue, to.blue, p));
	}
	
	private static int interpolate(int from, int to, double p) {
		return (int) (from + ((to-from) * p));
	}

	public String toHexString() {
		return toHexByte(red) + toHexByte(green) + toHexByte(blue);
	}
	
	private String toHexByte(int component) {
		if(component < 16) {
			return '0' + Integer.toString(component, 16);
		} else {
			return Integer.toString(component, 16);
		}
	}
}
