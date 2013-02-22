package org.activityinfo.shared.map;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

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
