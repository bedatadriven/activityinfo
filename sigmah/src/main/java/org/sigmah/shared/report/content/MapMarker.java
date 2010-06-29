package org.sigmah.shared.report.content;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author Alex Bertram
 */
public class MapMarker implements Serializable {

    private int x;
    private int y;
    private double lat;
    private double lng;
    private List<Integer> siteIds = new ArrayList<Integer>();

    public MapMarker() {

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

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public List<Integer> getSiteIds() {
        return siteIds;
    }

    public void setSiteIds(List<Integer> siteIds) {
        this.siteIds = siteIds;
    }

    public int getSize() {
        return 0;
    }

    /**
     * Sorts markers left to right, top to bottom.
     */
    public static class LRTBComparator implements Comparator<MapMarker> {
        @Override
        public int compare(MapMarker m1, MapMarker m2) {
            int y1 = m1.getY() / 5;
            int y2 = m2.getY() / 5;

            if(y1 < y2) {
                return -1;
            } else if(y1 > y2) {
                return 1;
            } else {
                if(m1.getX() < m2.getX()) {
                    return -1;
                } else if(m1.getX() > m2.getX()) {
                    return 1;
                } else {
                    return 0;
                }
            }
        }
    }

    @Override
    public String toString() {
        return "MapMarker{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
