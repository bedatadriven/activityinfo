package org.sigmah.server.report.generator.map;

/**
 * Simply encapsulates the index of map tile
 * 
 * @author Alex Bertram
 *
 */
public class Tile {

	private int x;
	private int y;
	
	public Tile(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	
}
