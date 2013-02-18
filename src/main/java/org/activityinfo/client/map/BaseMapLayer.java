/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.client.map;

import org.activityinfo.shared.map.TileBaseMap;

import com.google.gwt.maps.client.Copyright;
import com.google.gwt.maps.client.CopyrightCollection;
import com.google.gwt.maps.client.TileLayer;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.geom.LatLngBounds;
import com.google.gwt.maps.client.geom.Point;

/**
 * Implementation of GoogleMaps TileLayer for an ActivityInfo BaseMap
 */
class BaseMapLayer extends TileLayer {

	private TileBaseMap baseMap;

	public BaseMapLayer(TileBaseMap baseMap) {
		super(createCopyrights(baseMap), baseMap.getMinZoom(), baseMap.getMaxZoom());
		this.baseMap = baseMap;
	}

    public static CopyrightCollection createCopyrights(TileBaseMap baseMap) {
		Copyright copyright = new Copyright(1,
				LatLngBounds.newInstance(
						LatLng.newInstance(-90, -180),
						LatLng.newInstance(90, 180)),
				0, baseMap.getCopyright());

        CopyrightCollection copyrights = new CopyrightCollection();
		copyrights.addCopyright(copyright);

		return copyrights;
	}

	@Override
	public double getOpacity() {
		return 1;
	}

	@Override
	public String getTileURL(Point tile, int zoomLevel) {
		return baseMap.getTileUrl(zoomLevel, tile.getX(), tile.getY());
	}

	@Override
	public boolean isPng() {
		return true;
	}
}
