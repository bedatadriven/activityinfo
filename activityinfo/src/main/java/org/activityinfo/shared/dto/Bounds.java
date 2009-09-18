package org.activityinfo.shared.dto;

import java.io.Serializable;

public class Bounds implements Serializable  {
	public double x1;
	public double y1;
	public double x2;
	public double y2;
	
	public Bounds() {
		this.x1 = 180;
		this.y1 = 90;
		this.x2 = -180;
		this.y2 = -90;
	}
	
	public Bounds(double x1, double y1, double x2, double y2) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}
    
    public Bounds(Bounds bounds) {
        this.x1 = bounds.getX1();
        this.y1 = bounds.getY1();
        this.x2 = bounds.getX2();
        this.y2 = bounds.getY2();
    }
	
	public void grow(Bounds b) {
		if(b.x1 < x1) x1 = b.x1;
		if(b.y1 < y1) y1 = b.y1;
		if(b.x2 > x2) x2 = b.x2;
		if(b.y2 > y2) y2 = b.y2;
	}

    public void grow(double x, double y) {
        if(x < x1) x1 = x;
        if(x > x2) x2 = x;
        if(y < y1) y1 = y;
        if(y > y2) y2 = y;
    }
	
	public boolean isEmpty() {
		return x1>x2 || y1>y2;
	}
	
	public double getCenterX() {
		return (x1+x2)/2;
	}
	public double getCenterY() {
		return (y1+y2)/2;
	}
	
	public Bounds intersect(Bounds b) {
		return new Bounds(
				Math.max(x1, b.x1),
				Math.max(y1, b.y1), 
				Math.min(x2, b.x2),
				Math.min(y2, b.y2));
	}
	
	public boolean intersects(Bounds b) {
		return ! (b.x2 < x1 ||
				  b.x1 > x2 || 
				  b.y2 < y1 ||
				  b.y1 > y2 );		
	}
	
	public boolean contains(Bounds b) {
		return b.x1 >= x1 &&
			   b.x2 <= x2 &&
			   b.y1 >= y1 &&
			   b.y2 <= y2;
	}

    public boolean contains(double x, double y) {
        return x >= x1 && x <= x2 && y >= y1 && y <= y2;
    }

    public double clampX(double x) {
        if(x < x1)
            return x1;
        if(x > x2)
            return x2;
        return x;
    }

    public double clampY(double y) {
        if(y < y1)
            return y1;
        if(y > y2)
            return y2;
        return y;
    }

	public double distance(Bounds b) {
		double dx=0, dy=0;
		
		if(b.x2 < x1)
			dx = x1 - b.x2;
		else if(b.x1 > x2)
			dx = b.x1 - x2;
		
		if(b.y2 < y1) 
			dy = y1 - b.y2;
		else if(b.y1 > y2)
			dy = b.y1 - y2;
	
		return Math.sqrt(Math.pow(dx,2) + Math.pow(dy,2));
	}

	@Override
	public boolean equals(Object o) {
		if(o instanceof Bounds) {
			Bounds b = (Bounds)o;
			return b.x1 == x1 && b.y1 == y1 && b.x2 == x2 && b.y2 == y2;
		}
		return false;
	}

	public double getX1() {
		return x1;
	}

	public double getY1() {
		return y1;
	}

	public double getX2() {
		return x2;
	}

	public double getY2() {
		return y2;
	}

	public void setX1(double x1) {
		this.x1 = x1;
	}

	public void setY1(double y1) {
		this.y1 = y1;
	}

	public void setX2(double x2) {
		this.x2 = x2;
	}

	public void setY2(double y2) {
		this.y2 = y2;
	}

}
