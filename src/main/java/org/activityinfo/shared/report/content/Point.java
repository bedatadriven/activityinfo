package org.activityinfo.shared.report.content;

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

/**
 * Point represents a point on the map by its pixel coordinates, that is to say
 * in the projected coordinate system.
 * 
 * In the projected coordinate system, the x coordinate increases to the right,
 * and the y coordinate increases downwards.
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
        return (int) Math.round(x);
    }

    public double getDoubleX() {
        return x;
    }

    public int getY() {
        return (int) Math.round(y);
    }

    public double getDoubleY() {
        return y;
    }

    public double distance(Point p) {
        return Math.sqrt((p.x - x) * (p.x - x) + (p.y - y) * (p.y - y));
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
