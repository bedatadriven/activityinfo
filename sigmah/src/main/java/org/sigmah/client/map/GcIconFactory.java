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
public class GcIconFactory {



	/**
	 * Specifies, in pixels, the width of the icon.
	 *     The width may include some blank space on the side, depending on the
	 *     height of the icon, as the icon will scale its shape proportionately
	 */
	public int width = 32;


	/**
	 * Specifies, in pixels, the height of the icon.
	 */
	public int height = 32;

	/**
	 * Specifies, as a hexadecimal
	 *     string, the color used for the majority of the icon body.
	 */
	public String primaryColor = "#ff0000";

	/**
	 * Specifies, as a hexadecimal
	 *     string, the color used for the top corner of the icon. If you'd like the
	 *     icon to have a consistent color, make the this the same as the
	 *     <code>primaryColor</code>.
	 */
	public String cornerColor = "#ffffff";

	/**
	 *  Specifies, as a hexadecimal
	 *     string, the color used for the outside line (stroke) of the icon.
	 */
	public String strokeColor = "#000000";

	/**
	 * Specifies, as a hexadecimal
	 *     string, the color used for the shadow of the icon.
	 */
	public String shadowColor = "#000000";

	/**
	 *  Specifies a character or string to display
	 *     inside the body of the icon. Generally, one or two characters looks best.
	 */
	public String label = "";

	/**
	 * Specifies, as a hexadecimal
	 *     string, the color used for the label text.
	 */
	public String labelColor = "#000000";

	/**
	 * Specifies, in pixels, the size of the label
	 *     text. If set to 0, the text auto-sizes to fit the icon body.
	 */
	public int labelSize = 0;


	public final static String SHAPE_CIRCLE = "circle";
	public final static String SHAPE_ROUNDRECT = "roundrect";

	/**
	 * Specifies shape of the icon. Current
	 *     options are "circle" for a circle or "roundrect" for a rounded rectangle.
	 */
	public String shape = SHAPE_CIRCLE;

	/**
	 * Specifies whether to add a star to the
	 *     edge of the icon.
	 */
	public boolean addStar = false;

	/**
	 *  Specifies, as a hexadecimal
	 *     string, the color used for the star body.
	 */
	public String starPrimaryColor = "#FFFF00";

	/**
	 *  Specifies, as a hexadecimal
	 *     string, the color used for the outside line (stroke) of the star.
	 */
	public String starStrokeColor = "#0000FF";


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


    /**
	 * Creates a flat icon based on the specified options
	 *     Supported options are: width, height, primaryColor,
	 *     shadowColor, label, labelColor, labelSize, and shape..
     *
     * @return Icon object for use in GoogleMaps
	 */
	public Icon createFlatIcon() {
		String shapeCode = ("circle".equals(shape)) ? "it" : "itr";

		String baseUrl = "http://chart.apis.google.com/chart?cht=" + shapeCode;
		String iconUrl = baseUrl + "&chs=" + width + "x" + height +
			"&chco=" + primaryColor.replace("#", "") + "," +
			shadowColor.replace("#", "") + "ff,ffffff01" +
			"&chl=" + label + "&chx=" + labelColor.replace("#", "") +
			"," + labelSize;
		Icon icon = Icon.newInstance(Icon.DEFAULT_ICON);
		icon.setImageURL(iconUrl + "&chf=bg,s,00000000" + "&ext=.png");
		icon.setIconSize(Size.newInstance(width, height));
		icon.setShadowSize(Size.newInstance(0, 0));
		icon.setIconAnchor(Point.newInstance(width / 2, height / 2));
		icon.setInfoWindowAnchor(Point.newInstance(width / 2, height / 2));
		icon.setPrintImageURL(iconUrl + "&chof=gif");
		icon.setMozPrintImageURL(iconUrl + "&chf=bg,s,ECECD8" + "&chof=gif");
		icon.setTransparentImageURL(iconUrl + "&chf=a,s,ffffff01&ext=.png");

		if (shapeCode.equals("itr")) {
			icon.setImageMap(new int[] { 0, 0, width, 0, width, height, 0, height } );
		} else {
			icon.setImageMap(createCircleImageMap(width, height, 8));
		}

		return icon;
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
