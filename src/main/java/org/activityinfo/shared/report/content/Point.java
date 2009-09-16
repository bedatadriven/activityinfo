package org.activityinfo.shared.report.content;

/**
 *  Point represents a point on the map by its pixel coordinates, that is to say in 
 *  the projected coordinate system.  
 *  
 *  In the projected coordinate system,  the x coordinate increases to the right, and the 
 *  y coordinate increases downwards.
 *  
 * @author Alex Bertram
 *
 */
public class Point {

	public Point(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	public final int x;
	public final int y;


    public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public double distance(Point p) {
		return Math.sqrt( (p.x-x)*(p.x-x) + (p.y-y)*(p.y-y) );	
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Point point = (Point) o;

        if (x != point.x) return false;
        if (y != point.y) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }

    public String toString() {
        return "(" + x + ", " + y + ")";                
    }
}
