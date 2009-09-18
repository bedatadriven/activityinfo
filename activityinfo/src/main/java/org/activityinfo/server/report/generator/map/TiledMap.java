package org.activityinfo.server.report.generator.map;

import org.activityinfo.shared.report.content.LatLng;
import org.activityinfo.shared.report.content.Point;

import java.awt.Graphics2D;
import java.awt.Image;

public class TiledMap {

	
	private final static int TILE_SIZE = 256;
	
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
	 * The width and height of the projected coordinate system
	 */
	private final int size;
	
	/**
	 * The geographic center of the map
	 */
	private final LatLng geoCenter;
	
	/**
	 * The center of the map in the projected coordinate system
	 * (pixels)
	 */
	private Point center;
	

	/**
	 * The upper left hand corner of the image, in projected
	 * coordinate system units
	 */
	private Point origin;

	
	private Tile tileOrigin;
	
	
	public TiledMap(int width, int height, LatLng geographicCenter, int zoom) {
		this.width = width;
		this.height = height;
		this.zoom = zoom;
		this.geoCenter = geographicCenter;
		
		
		/*
		 * Calculate the width/height of our projected coordinate system
		 */
		size = TileMath.size(zoom);
		
		/*
		 * Calculate the center in pixels
		 */
		
		center = TileMath.fromLatLngToPixel(geographicCenter, zoom);
	
		
		
		origin = new Point(center.x - (width/2),  
						   center.y - (height/2));
		
		
		tileOrigin = TileMath.tileForPoint(origin);
	}
	
	
	public void drawLayer(final Graphics2D g2d, TileProvider source)  {
		drawLayer(new TileDrawer() {

			@Override
			public void drawImage(Image image, int x, int y) {
				g2d.drawImage(image, x, y, TILE_SIZE, TILE_SIZE, null);
			}
			
		}, source);
	}
	
	public void drawLayer(TileDrawer drawer, TileProvider source)  {
		
		int x = -(origin.x % TILE_SIZE);		
		int tileX = tileOrigin.getX();
		
		while (x  < width) {

			int y = -(origin.y % TILE_SIZE);
			int tileY = tileOrigin.getY();
			
			while (y < height) {
				
				Image image = source.getImage(zoom, tileX, tileY);
				
				if(image == null) {
					
				} else {
					drawer.drawImage(image, x, y);
				}
				
				y += TILE_SIZE;
				tileY ++;
			}
			x += TILE_SIZE;
			tileX ++;
		}
	}
	
	public Point fromLatLngToPixel(LatLng latlng) {
	
		Point p = TileMath.fromLatLngToPixel(latlng, this.zoom);
		
		return new Point(p.x - origin.x, p.y - origin.y);	
	}
	
}
