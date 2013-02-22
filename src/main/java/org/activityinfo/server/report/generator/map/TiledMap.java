

package org.activityinfo.server.report.generator.map;

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

import org.activityinfo.server.report.renderer.image.TileHandler;
import org.activityinfo.shared.report.content.AiLatLng;
import org.activityinfo.shared.report.content.Point;
import org.activityinfo.shared.util.mapping.Tile;
import org.activityinfo.shared.util.mapping.TileMath;

public class TiledMap {

	
	private static final int TILE_SIZE = 256;
	
	/**
	 * width in pixels of the map image
	 */
	private final int width;
	
	/**
	 * height in pixels of the map image
	 */
	private final int height;
	
	/**
	 * zoom level
	 */
	private final int zoom;
	
	
	/**
	 * The geographic center of the map
	 */
	private final AiLatLng geoCenter;
	

	/**
	 * The upper left hand corner of the image, in projected
	 * coordinate system units
	 */
	private Point origin;

	
	private Tile tileOrigin;
	
	
	public TiledMap(int width, int height, AiLatLng geographicCenter, int zoom) {
		this.width = width;
		this.height = height;
		this.zoom = zoom;
		this.geoCenter = geographicCenter;
		
		
		/*
		 * Calculate the center in pixels
		 */
		
		Point center = TileMath.fromLatLngToPixel(geographicCenter, zoom);
				
		origin = center.translate(-(width/2), -(height/2));
		
		
		tileOrigin = TileMath.tileForPoint(origin);
	}
	
	public void drawLayer(TileHandler drawer, TileProvider source)  {
		
		int x = -(origin.getX() % TILE_SIZE);		
		int tileX = tileOrigin.getX();
		
		while (x  < width) {

			int y = -(origin.getY() % TILE_SIZE);
			int tileY = tileOrigin.getY();
			
			while (y < height) {
				
				String url = source.getImageUrl(zoom, tileX, tileY);
				drawer.addTile(url, x, y, TILE_SIZE, TILE_SIZE);
				
				y += TILE_SIZE;
				tileY ++;
			}
			x += TILE_SIZE;
			tileX ++;
		}
	}
	
	public Point fromLatLngToPixel(AiLatLng latLng) {
		return TileMath.fromLatLngToPixel(latLng, this.zoom)
					.translate(-origin.getDoubleX(), -origin.getDoubleY());
	}
	
	public AiLatLng fromPixelToLatLng(Point px) {
		return TileMath.inverse(px.translate(origin.getDoubleX(), origin.getDoubleY()), this.zoom);
	}
		
	public AiLatLng fromPixelToLatLng(double x, double y) {
		return fromPixelToLatLng(new Point(x,y));
	}

	public int getWidth() {
		return width;
	}


	public int getHeight() {
		return height;
	}

	public int getZoom() {
		return zoom;
	}


	public AiLatLng getGeoCenter() {
		return geoCenter;
	}
}
