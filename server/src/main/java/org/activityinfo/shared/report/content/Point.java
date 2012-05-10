/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

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

	private final double x;
	private final double y;
	
	public Point(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	public Point(double x, double y) {
		super();
		this.x = x;
		this.y = y;
	}

    public int getX() {
		return (int)Math.round(x);
	}
	
    public double getDoubleX() {
    	return x;
    }
    
	public int getY() {
		return (int)Math.round(y);
	}
	
	public double getDoubleY() {
		return y;
	}
	
	public double distance(Point p) {
		return Math.sqrt( (p.x-x)*(p.x-x) + (p.y-y)*(p.y-y) );	
	}
	
	public Point translate(double tx, double ty) {
		return new Point(x + tx, y + ty);
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Point point = (Point) o;

        // compare integers, ignore the extra precision
        // of the doubles
        
        if (getX() != point.getX()) {
            return false;
        }
        if (getY() != point.getY()) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = getX();
        result = 31 * result + getY();
        return result;
    }

	@Override
	public String toString() {
        return "(" + x + ", " + y + ")";                
    }
}
