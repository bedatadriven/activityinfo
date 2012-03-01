/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.report.generator.map;

import org.sigmah.shared.report.content.Point;

public final class CircleMath {

	private CircleMath() {}
	
    public static double area(double radius) {
        return Math.PI * radius * radius;
    }


    /**
     *
     * Calculates the area of intersection of two circles.
     *
     * @param a  The first point
     * @param b  The second point
     * @param r0  The radius of the first point
     * @param r1  The radius of the second point
     * @return  Area of the intersection
     */
    public static double intersectionArea(Point a, Point b, double r0, double r1) {

        double c = a.distance(b);
        if(c >= r0+r1) {
            return 0; // no intersection
        }
        if(c < r0 && c + r1 < r0) {
            return area(r1); // the first circle completely contains the second
        }
        if(c < r1 && c + r0 < r1) {
            return area(r0); // the second circle completely contains the first
        }

        double cba = Math.acos( ((r1*r1) + (c*c) - (r0*r0)) / (2.0*r1*c));
        double cbd = cba * 2.0;

        double cab = Math.acos( ((r0*r0) + (c*c) - (r1*r1))/(2.0*r0*c) );
        double cad = cab * 2.0;

        return    (0.5)*(cbd)*(r1*r1) - (0.5)*(r1*r1)*Math.sin(cbd)
                + (0.5)*(cad)*(r0*r0) - (0.5)*(r0*r0*Math.sin(cad));

    }

}
