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

/**
 * LatLng is a point in geographical coordinates longitude and latitude
 * 
 * This class is immutable
 * 
 * @author Alex Bertram
 * 
 */
public class AiLatLng implements Serializable {
    public AiLatLng() {
        lat = 0;
        lng = 0;
    }

    public AiLatLng(double lat, double lon) {
        super();
        this.lat = lat;
        this.lng = lon;
    }

    private double lat;
    private double lng;

    /**
     * 
     * @return The latitude of the point (y-axis)
     */
    public double getLat() {
        return lat;
    }

    /**
     * 
     * @return The longitude of the point (x-axis)
     */
    public double getLng() {
        return lng;
    }
}
