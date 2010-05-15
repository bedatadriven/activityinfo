package org.activityinfo.client.map;

import com.google.gwt.maps.client.Copyright;
import com.google.gwt.maps.client.CopyrightCollection;
import com.google.gwt.maps.client.TileLayer;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.geom.LatLngBounds;
import com.google.gwt.maps.client.geom.Point;

public class RgcTileLayer extends TileLayer {

	String baseUrl;
	
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
