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
    private String title;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Sorts markers left to right, top to bottom.
     */
    public static class LRTBComparator implements Comparator<MapMarker> {
        @Override
        public int compare(MapMarker m1, MapMarker m2) {
            int y1 = m1.getY() / 5;
            int y2 = m2.getY() / 5;

            if (y1 < y2) {
                return -1;
            } else if (y1 > y2) {
                return 1;
            } else {
                if (m1.getX() < m2.getX()) {
                    return -1;
                } else if (m1.getX() > m2.getX()) {
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
