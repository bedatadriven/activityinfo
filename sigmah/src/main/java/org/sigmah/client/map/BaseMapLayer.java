/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.map;

import com.google.gwt.maps.client.Copyright;
import com.google.gwt.maps.client.CopyrightCollection;
import com.google.gwt.maps.client.TileLayer;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.geom.LatLngBounds;
import com.google.gwt.maps.client.geom.Point;

import org.sigmah.shared.map.BaseMap;
import org.sigmah.shared.map.TileBaseMap;

/**
 * Implementation of GoogleMaps TileLayer for an ActivityInfo BaseMap
 */
class BaseMapLayer extends TileLayer {

	private BaseMap baseMap;

	public BaseMapLayer(BaseMap baseMap2) {
		super(createCopyRights(baseMap2), baseMap2.getMinZoom(), baseMap2.getMaxZoom());
		this.baseMap = baseMap2;
	}

    public static CopyrightCollection createCopyRights(BaseMap baseMap) {
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
		if (baseMap instanceof TileBaseMap) {
			return ((TileBaseMap) baseMap).getTileUrl(zoomLevel, tile.getX(), tile.getY());
		}
		
		// TODO: fix
		return null;
	}

	@Override
	public boolean isPng() {
		return true;
	}
}
