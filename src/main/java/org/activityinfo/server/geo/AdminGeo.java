package org.activityinfo.server.geo;

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

import com.vividsolutions.jts.geom.Geometry;

/**
 * Geometry of an administrative entity, in the WGS84 geographic coordinate
 * system.
 */
public class AdminGeo {
    private int id;
    private Geometry geometry;

    public AdminGeo(int id, Geometry geometry) {
        super();
        this.id = id;
        this.geometry = geometry;
    }

    /**
     * 
     * @return the id of the administrative entity ({@code AdminEntityId}
     */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * 
     * @return the geometry of the administrative entity in the WGS84 geographic
     *         coordinate system
     */
    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

}
