package org.activityinfo.shared.map;

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
 * BaseMap provided through the Google Maps API
 */
public final class GoogleBaseMap extends BaseMap {

    /**
     * specifies a standard roadmap image, as is normally shown on the Google
     * Maps website
     */
    public static final GoogleBaseMap ROADMAP = new GoogleBaseMap(
        "Google.ROADMAP", "roadmap");

    /**
     * specifies a satellite image.
     */
    public static final GoogleBaseMap SATELLITE = new GoogleBaseMap(
        "Google.SATELLITE", "satellite");

    /**
     * specifies a physical relief map image, showing terrain and vegetation.
     */
    public static final GoogleBaseMap TERRAIN = new GoogleBaseMap(
        "Google.TERRAIN", "terrain");

    /**
     * specifies a hybrid of the satellite and roadmap image, showing a
     * transparent layer of major streets and place names on the satellite
     * image.
     */
    public static final GoogleBaseMap HYBRID = new GoogleBaseMap(
        "Google.HYBRID", "hybrid");

    private String id;
    private String formatId;

    private GoogleBaseMap() {

    }

    private GoogleBaseMap(String mapId, String formatId) {
        this.id = mapId;
        this.formatId = formatId;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public int getMinZoom() {
        return 1;
    }

    @Override
    public int getMaxZoom() {
        return 21;
    }

    /**
     * 
     * @return this GoogleBaseMap's format id, to be used in requests to the
     *         static map API
     */
    public String getFormatId() {
        return formatId;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        GoogleBaseMap other = (GoogleBaseMap) obj;
        return id.equals(other.id);
    }

    @Override
    public String toString() {
        return "Google " + formatId + " base map";
    }
}
