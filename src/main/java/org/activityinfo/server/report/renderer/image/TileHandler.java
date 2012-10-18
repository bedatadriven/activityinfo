package org.activityinfo.server.report.renderer.image;



public interface TileHandler {

	/**
	 * Renders a tile to the map canvas.
	 * The coordinate space has its origin in the UPPER-LEFT hand corner of the 
	 * canvas. (Y increasing downwards)
	 * 
	 * @param tileUrl the url of the tile
	 * @param x the horizontal position to place the tile image (left)
	 * @param y the vertical position to place the tile image (top)
	 * @param width
	 * @param height
	 */
	void addTile(String tileUrl, int x, int y, int width, int height);

}
