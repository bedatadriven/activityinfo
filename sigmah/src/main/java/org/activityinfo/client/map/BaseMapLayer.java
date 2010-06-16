package org.activityinfo.client.map;

import com.google.gwt.maps.client.Copyright;
import com.google.gwt.maps.client.CopyrightCollection;
import com.google.gwt.maps.client.TileLayer;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.geom.LatLngBounds;
import com.google.gwt.maps.client.geom.Point;
import org.activityinfo.shared.map.BaseMap;

public class BaseMapLayer extends TileLayer {

	private BaseMap baseMap;

	public BaseMapLayer(BaseMap baseMap) {
		super(createCopyRights(baseMap), baseMap.getMinZoom(), baseMap.getMaxZoom());
		this.baseMap = baseMap;
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
        String url = baseMap.getTileUrl(zoomLevel, tile.getX(), tile.getY());
//        GWT.log("Tile: " + url, null);
        return url;
	}

	@Override
	public boolean isPng() {
		return true;
	}

}
