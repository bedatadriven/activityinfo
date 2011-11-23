/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.map;

import com.google.gwt.maps.client.geom.Point;
import com.google.gwt.maps.client.geom.Size;
import com.google.gwt.maps.client.overlay.Icon;

/**
 *  This gives you static functions for creating dynamically
 *     sized and colored marker icons using the Google Charts API marker output.
 */
public class GoogleChartsIconBuilder {
	
	public final static String SHAPE_CIRCLE = "circle";
	public final static String SHAPE_ROUNDRECT = "roundrect";
	
	private int width = 32;
	private int height = 32;
	private String primaryColor = "#ff0000";
	private String cornerColor = "#ffffff";
	private String strokeColor = "#000000";
	private String shadowColor = "#000000";
	private String label = "";
	private String labelColor = "#000000";
	private int labelSize = 0;

	private String shape = SHAPE_CIRCLE;

	/**
	 * Specifies, in pixels, the width of the icon.
	 *     The width may include some blank space on the side, depending on the
	 *     height of the icon, as the icon will scale its shape proportionately
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * Specifies, in pixels, the height of the icon.
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * Specifies, as a hexadecimal
	 *     string, the color used for the majority of the icon body.
	 */
	public void setPrimaryColor(String primaryColor) {
		this.primaryColor = primaryColor;
	}

	/**
	 * Specifies, as a hexadecimal
	 *     string, the color used for the top corner of the icon. If you'd like the
	 *     icon to have a consistent color, make the this the same as the
	 *     <code>primaryColor</code>.
	 */
	public void setCornerColor(String cornerColor) {
		this.cornerColor = cornerColor;
	}
	
	/**
	 *  Specifies, as a hexadecimal
	 *     string, the color used for the outside line (stroke) of the icon.
	 */
	public void setStrokeColor(String strokeColor) {
		this.strokeColor = strokeColor;
	}

	/**
	 * Specifies, as a hexadecimal
	 *     string, the color used for the shadow of the icon.
	 */
	public void setShadowColor(String shadowColor) {
		this.shadowColor = shadowColor;
	}


	/**
	 *  Specifies a character or string to display
	 *     inside the body of the icon. Generally, one or two characters looks best.
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * Specifies, as a hexadecimal
	 *     string, the color used for the label text.
	 */
	public void setLabelColor(String labelColor) {
		this.labelColor = labelColor;
	}


	/**
	 * Specifies, in pixels, the size of the label
	 *     text. If set to 0, the text auto-sizes to fit the icon body.
	 */
	public void setLabelSize(int labelSize) {
		this.labelSize = labelSize;
	}

	/**
	 * Specifies shape of the icon. Current
	 *     options are "circle" for a circle or "roundrect" for a rounded rectangle.
	 */
	public void setShape(String shape) {
		this.shape = shape;
	}

	
	/**
	 * Creates an icon based on the specified options
	 *   Supported options are: width, height, primaryColor,
	 *   strokeColor, and cornerColor.
     *
     * @return Icon for GoogleMaps
	 */
	public Icon createMarkerIcon() {

		String baseUrl = "http://chart.apis.google.com/chart?cht=mm";
		String iconUrl = baseUrl + "&chs=" + width + "x" + height +
		"&chco=" + cornerColor.replace("#", "") + "," +
		primaryColor.replace("#", "") + "," +
		strokeColor.replace("#", "") + "&ext=.png";

		Icon icon = Icon.newInstance(iconUrl);
		icon.setIconSize(Size.newInstance(width, height));
		icon.setShadowSize(Size.newInstance((int) Math.floor(width * 1.6), height));
		icon.setIconAnchor(Point.newInstance(width / 2, height));
		icon.setInfoWindowAnchor(Point.newInstance(width / 2, (int) Math.floor(height / 12)));
		icon.setPrintImageURL(iconUrl + "&chof=gif");
		icon.setMozPrintImageURL(iconUrl + "&chf=bg,s,ECECD8" + "&chof=gif");

		iconUrl = baseUrl + "&chs=" + width + "x" + height +
		"&chco=" + cornerColor.replace("#", "") + "," +
		primaryColor.replace("#", "") + "," +
		strokeColor.replace("#", "");
		icon.setTransparentImageURL(iconUrl + "&chf=a,s,ffffff11&ext=.png");
		icon.setImageMap( new int[] {
				width / 2, height,
				(int)((7d / 16d) * width), (int)((5d / 8d) * height),
				(int)((5d / 16d) * width), (int)((7d / 16d) * height),
				(int)((7d / 32d) * width), (int)((5d / 16d) * height),
				(int)((5d / 16d) * width), (int)((1d / 8d) * height),
				(int)((1d / 2d) * width), 0,
				(int)((11d / 16d) * width), (int)((1d / 8d) * height),
				(int)((25d / 32d) * width), (int)((5d / 16d) * height),
				(int)((11d / 16d) * width), (int)((7d / 16d) * height),
				(int)((9d / 16d) * width), (int)((5d / 8d) * height) });

		return icon;
	}
	
	public String composePinUrl() {
		return "http://chart.apis.google.com/chart?chst=d_map_pin_letter&chld=" +
		 	label.charAt(0) + "|" + 
		 	primaryColor.replace("#", "") + "|" +
		 	labelColor.replace("#", "") + "&ext=.png";
	}
	
	public Icon createPinUrl() {
		Icon icon = Icon.newInstance(composePinUrl());
		icon.setIconSize(Size.newInstance(21, 34));
		icon.setShadowSize(Size.newInstance(0,0));
		icon.setIconAnchor(Point.newInstance(10, 34));
		icon.setInfoWindowAnchor(Point.newInstance(21 / 2, (int) Math.floor(34 / 12)));
		icon.setImageMap( new int[] {
				width / 2, height,
				(int)((7d / 16d) * width), (int)((5d / 8d) * height),
				(int)((5d / 16d) * width), (int)((7d / 16d) * height),
				(int)((7d / 32d) * width), (int)((5d / 16d) * height),
				(int)((5d / 16d) * width), (int)((1d / 8d) * height),
				(int)((1d / 2d) * width), 0,
				(int)((11d / 16d) * width), (int)((1d / 8d) * height),
				(int)((25d / 32d) * width), (int)((5d / 16d) * height),
				(int)((11d / 16d) * width), (int)((7d / 16d) * height),
				(int)((9d / 16d) * width), (int)((5d / 8d) * height) });

		return icon;
		
	}


    /**
	 * Creates a flat icon based on the specified options
	 *     Supported options are: width, height, primaryColor,
	 *     shadowColor, label, labelColor, labelSize, and shape..
     *
     * @return Icon object for use in GoogleMaps
	 */
	public Icon createFlatIcon() {
		String iconUrl = composeFlatIconUrl();
		Icon icon = Icon.newInstance(Icon.DEFAULT_ICON);
		icon.setImageURL(iconUrl + "&chf=bg,s,00000000" + "&ext=.png");
		icon.setIconSize(Size.newInstance(width, height));
		icon.setShadowSize(Size.newInstance(0, 0));
		icon.setIconAnchor(Point.newInstance(width / 2, height / 2));
		icon.setInfoWindowAnchor(Point.newInstance(width / 2, height / 2));
		icon.setPrintImageURL(iconUrl + "&chof=gif");
		icon.setMozPrintImageURL(iconUrl + "&chf=bg,s,ECECD8" + "&chof=gif");
		icon.setTransparentImageURL(iconUrl + "&chf=a,s,ffffff01&ext=.png");

		if (!"circle".equals(shape)) {
			icon.setImageMap(new int[] { 0, 0, width, 0, width, height, 0, height } );
		} else {
			icon.setImageMap(createCircleImageMap(width, height, 8));
		}

		return icon;
	}

	public String composeFlatIconUrl() {
		String baseUrl = "http://chart.apis.google.com/chart?cht=" + shapeCode();
		String iconUrl = baseUrl + "&chs=" + width + "x" + height +
			"&chco=" + primaryColor.replace("#", "") + "," +
			shadowColor.replace("#", "") + "ff,ffffff01" +
			"&chl=" + label + "&chx=" + labelColor.replace("#", "") +
			"," + labelSize;
		return iconUrl;
	}

	private String shapeCode() {
		return ("circle".equals(shape)) ? "it" : "itr";
	}

	public static int[] createCircleImageMap(int width, int height, int polyNumSides) {
		double polySideLength = 360 / polyNumSides;
		double polyRadius = Math.min(width, height) / 2;
		int[] imageMap = new int[(polyNumSides+1)*2];
		int i=0;
		for (int a = 0; a < (polyNumSides + 1); a++) {
			double aRad = polySideLength * a * (Math.PI / 180);
			double pixelX = polyRadius + polyRadius * Math.cos(aRad);
			double pixelY = polyRadius + polyRadius * Math.sin(aRad);

			imageMap[i++] = (int)pixelX;
			imageMap[i++] = (int)pixelY;
		}

		return imageMap;
	}
}
