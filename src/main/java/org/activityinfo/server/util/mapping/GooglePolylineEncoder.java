package org.activityinfo.server.util.mapping;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.activityinfo.shared.util.mapping.PolylineEncoded;

import com.google.common.collect.Maps;
import com.vividsolutions.jts.geom.Coordinate;

/**
 * Adapation of... Reimplementation of... Mark McClures Javascript
 * PolylineEncoder All the mathematical logic is more or less copied by McClure
 * 
 * @author Mark Rambow
 * @e-mail markrambow[at]gmail[dot]com
 * @version 0.1
 * 
 * 
 *          http://facstaff.unca.edu/mcmcclur/GoogleMaps/EncodePolyline/
 * 
 */
public class GooglePolylineEncoder {

    private int numLevels = 18;

    private int zoomFactor = 2;

    private double verySmall = 0.00001;

    private boolean forceEndpoints = true;

    private double[] zoomLevelBreaks;

    private HashMap<String, Double> bounds;

    // constructor
    public GooglePolylineEncoder(int numLevels, int zoomFactor, double verySmall,
        boolean forceEndpoints) {

        this.numLevels = numLevels;
        this.zoomFactor = zoomFactor;
        this.verySmall = verySmall;
        this.forceEndpoints = forceEndpoints;

        this.zoomLevelBreaks = new double[numLevels];

        for (int i = 0; i < numLevels; i++) {
            this.zoomLevelBreaks[i] = verySmall
                * Math.pow(this.zoomFactor, numLevels - i - 1);
        }
    }

    public GooglePolylineEncoder() {
        this.zoomLevelBreaks = new double[numLevels];

        for (int i = 0; i < numLevels; i++) {
            this.zoomLevelBreaks[i] = verySmall
                * Math.pow(this.zoomFactor, numLevels - i - 1);
        }
    }

    public int getNumLevels() {
        return numLevels;
    }

    public int getZoomFactor() {
        return zoomFactor;
    }

    /**
     * Douglas-Peucker algorithm, adapted for encoding
     * 
     * @return HashMap [EncodedPoints;EncodedLevels]
     * 
     */
    public PolylineEncoded dpEncode(Coordinate[] track) {
        int i, maxLoc = 0;
        Stack<int[]> stack = new Stack<int[]>();
        double[] dists = new double[track.length];
        double maxDist, absMaxDist = 0.0, temp = 0.0;
        int[] current;
        String encodedPoints, encodedLevels;

        if (track.length > 2) {
            int[] stackVal = new int[] { 0, (track.length - 1) };
            stack.push(stackVal);

            while (stack.size() > 0) {
                current = stack.pop();
                maxDist = 0;

                for (i = current[0] + 1; i < current[1]; i++) {
                    temp = this.distance(
                        track[i],
                        track[current[0]],
                        track[current[1]]);
                    if (temp > maxDist) {
                        maxDist = temp;
                        maxLoc = i;
                        if (maxDist > absMaxDist) {
                            absMaxDist = maxDist;
                        }
                    }
                }
                if (maxDist > this.verySmall) {
                    dists[maxLoc] = maxDist;
                    int[] stackValCurMax = { current[0], maxLoc };
                    stack.push(stackValCurMax);
                    int[] stackValMaxCur = { maxLoc, current[1] };
                    stack.push(stackValMaxCur);
                }
            }
        }

        encodedPoints = createEncodings(track, dists);
        encodedLevels = encodeLevels(track, dists, absMaxDist);

        return new PolylineEncoded(encodedPoints, encodedLevels);
    }

    /**
     * distance(p0, p1, p2) computes the distance between the point p0 and the
     * segment [p1,p2]. This could probably be replaced with something that is a
     * bit more numerically stable.
     * 
     * @param p0
     * @param p1
     * @param p2
     * @return
     */
    public double distance(Coordinate p0, Coordinate p1, Coordinate p2) {
        double u, out = 0.0;

        if (p1.y == p2.y
            && p1.x == p2.x) {
            out = Math.sqrt(Math.pow(p2.y - p0.y, 2)
                + Math.pow(p2.x - p0.x, 2));
        } else {
            u = ((p0.y - p1.y)
                * (p2.y - p1.y) + (p0
                .x - p1.x)
                * (p2.x - p1.x))
                / (Math.pow(p2.y - p1.y, 2) + Math
                    .pow(p2.x - p1.x, 2));

            if (u <= 0) {
                out = Math.sqrt(Math.pow(p0.y - p1.y,
                    2)
                    + Math.pow(p0.x - p1.x, 2));
            }
            if (u >= 1) {
                out = Math.sqrt(Math.pow(p0.y - p2.y,
                    2)
                    + Math.pow(p0.x - p2.x, 2));
            }
            if (0 < u && u < 1) {
                out = Math.sqrt(Math.pow(p0.y - p1.y
                    - u * (p2.y - p1.y), 2)
                    + Math.pow(p0.x - p1.x - u
                        * (p2.x - p1.x), 2));
            }
        }
        return out;
    }

    private static int floor1e5(double coordinate) {
        return (int) Math.floor(coordinate * 1e5);
    }

    private static String encodeSignedNumber(int num) {
        int sgn_num = num << 1;
        if (num < 0) {
            sgn_num = ~(sgn_num);
        }
        return (encodeNumber(sgn_num));
    }

    private static String encodeNumber(int num) {

        StringBuffer encodeString = new StringBuffer();

        while (num >= 0x20) {
            int nextValue = (0x20 | (num & 0x1f)) + 63;
            encodeString.append((char) (nextValue));
            num >>= 5;
        }

        num += 63;
        encodeString.append((char) (num));

        return encodeString.toString();
    }

    /**
     * Now we can use the previous function to march down the list of points and
     * encode the levels. Like createEncodings, we ignore points whose distance
     * (in dists) is undefined.
     */
    private String encodeLevels(Coordinate[] points, double[] dists,
        double absMaxDist) {
        int i;
        StringBuffer encoded_levels = new StringBuffer();

        if (this.forceEndpoints) {
            encoded_levels.append(encodeNumber(this.numLevels - 1));
        } else {
            encoded_levels.append(encodeNumber(this.numLevels
                - computeLevel(absMaxDist) - 1));
        }
        for (i = 1; i < points.length - 1; i++) {
            if (dists[i] != 0) {
                encoded_levels.append(encodeNumber(this.numLevels
                    - computeLevel(dists[i]) - 1));
            }
        }
        if (this.forceEndpoints) {
            encoded_levels.append(encodeNumber(this.numLevels - 1));
        } else {
            encoded_levels.append(encodeNumber(this.numLevels
                - computeLevel(absMaxDist) - 1));
        }
        // System.out.println("encodedLevels: " + encoded_levels);
        return encoded_levels.toString();
    }

    /**
     * This computes the appropriate zoom level of a point in terms of it's
     * distance from the relevant segment in the DP algorithm. Could be done in
     * terms of a logarithm, but this approach makes it a bit easier to ensure
     * that the level is not too large.
     */
    private int computeLevel(double absMaxDist) {
        int lev = 0;
        if (absMaxDist > this.verySmall) {
            lev = 0;
            while (absMaxDist < this.zoomLevelBreaks[lev]) {
                lev++;
            }
            return lev;
        }
        return lev;
    }

    private String createEncodings(Coordinate[] points, double[] dists) {
        StringBuffer encodedPoints = new StringBuffer();

        double maxlat = 0, minlat = 0, maxlon = 0, minlon = 0;

        int plat = 0;
        int plng = 0;

        for (int i = 0; i < points.length; i++) {

            // determin bounds (max/min lat/lon)
            if (i == 0) {
                maxlat = minlat = points[i].y;
                maxlon = minlon = points[i].x;
            } else {
                if (points[i].y > maxlat) {
                    maxlat = points[i].y;
                } else if (points[i].y < minlat) {
                    minlat = points[i].y;
                } else if (points[i].x > maxlon) {
                    maxlon = points[i].x;
                } else if (points[i].x < minlon) {
                    minlon = points[i].x;
                }
            }

            if (dists[i] != 0 || i == 0 || i == points.length - 1) {
                Coordinate point = points[i];

                int late5 = floor1e5(point.y);
                int lnge5 = floor1e5(point.x);

                int dlat = late5 - plat;
                int dlng = lnge5 - plng;

                plat = late5;
                plng = lnge5;

                encodedPoints.append(encodeSignedNumber(dlat));
                encodedPoints.append(encodeSignedNumber(dlng));

            }
        }

        HashMap<String, Double> bounds = new HashMap<String, Double>();
        bounds.put("maxlat", new Double(maxlat));
        bounds.put("minlat", new Double(minlat));
        bounds.put("maxlon", new Double(maxlon));
        bounds.put("minlon", new Double(minlon));

        this.setBounds(bounds);
        return encodedPoints.toString();
    }

    private void setBounds(HashMap<String, Double> bounds) {
        this.bounds = bounds;
    }

    public static Map<String, String> createEncodings(Coordinate[] track, int level, int step) {

        Map<String, String> resultMap = Maps.newHashMap();
        StringBuffer encodedPoints = new StringBuffer();
        StringBuffer encodedLevels = new StringBuffer();

        int plat = 0;
        int plng = 0;
        int counter = 0;

        int listSize = track.length;

        Coordinate trackpoint;

        for (int i = 0; i < listSize; i += step) {
            counter++;
            trackpoint = track[i];

            int late5 = floor1e5(trackpoint.y);
            int lnge5 = floor1e5(trackpoint.x);

            int dlat = late5 - plat;
            int dlng = lnge5 - plng;

            plat = late5;
            plng = lnge5;

            encodedPoints.append(encodeSignedNumber(dlat)).append(
                encodeSignedNumber(dlng));
            encodedLevels.append(encodeNumber(level));

        }

        System.out.println("listSize: " + listSize + " step: " + step
            + " counter: " + counter);

        resultMap.put("encodedPoints", encodedPoints.toString());
        resultMap.put("encodedLevels", encodedLevels.toString());

        return resultMap;
    }

    public HashMap<String, Double> getBounds() {
        return bounds;
    }
}
