

package org.activityinfo.client.map;

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

import com.google.gwt.maps.client.Copyright;
import com.google.gwt.maps.client.CopyrightCollection;
import com.google.gwt.maps.client.TileLayer;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.geom.LatLngBounds;
import com.google.gwt.maps.client.geom.Point;

class RgcTileLayer extends TileLayer {
	private String baseUrl;
	
	public RgcTileLayer(String baseUrl, int minZoom, int maxZoom) {
		super(createCopyRights(), minZoom, maxZoom);
		this.baseUrl = baseUrl;
	}
	
	public static CopyrightCollection createCopyRights() { 
		CopyrightCollection copyrights = new CopyrightCollection();
		copyrights.addCopyright(createRgcCopyright());
		
		return copyrights;
	}
	
	public static Copyright createRgcCopyright() {
		return new Copyright(1, 
				LatLngBounds.newInstance(
						LatLng.newInstance(-13.45599996, 12.18794184),
						LatLng.newInstance(5.386098154, 31.306)),
				0, "Le R&eacute;f&eacute;rentiel G&eacute;ographique Commun (RGC)");
	}

	
	@Override
	public double getOpacity() {
		return 1;
	}

	@Override
	public String getTileURL(Point tile, int zoomLevel) {
		return baseUrl + "/z" + zoomLevel + "/" + tile.getX() + "x" + tile.getY() + ".png";
	}

	@Override
	public boolean isPng() {
		return true;
	}
}
