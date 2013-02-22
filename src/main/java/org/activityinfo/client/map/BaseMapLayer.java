package org.activityinfo.client.map;

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

import org.activityinfo.shared.map.TileBaseMap;

import com.google.gwt.maps.client.Copyright;
import com.google.gwt.maps.client.CopyrightCollection;
import com.google.gwt.maps.client.TileLayer;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.geom.LatLngBounds;
import com.google.gwt.maps.client.geom.Point;

/**
 * Implementation of GoogleMaps TileLayer for an ActivityInfo BaseMap
 */
class BaseMapLayer extends TileLayer {

    private TileBaseMap baseMap;

    public BaseMapLayer(TileBaseMap baseMap) {
        super(createCopyrights(baseMap), baseMap.getMinZoom(), baseMap
            .getMaxZoom());
        this.baseMap = baseMap;
    }

    public static CopyrightCollection createCopyrights(TileBaseMap baseMap) {
        Copyright copyright = new Copyright(1,
            LatLngBounds.newInstance(
                LatLng.newInstance(-90, -180),
                LatLng.newInstance(90, 180)),
            0, baseMap.getCopyright());

        CopyrightCollection copyrights = new CopyrightCollection();
        copyrights.addCopyright(copyright);

        return copyrights;
    }

    @Override
    public double getOpacity() {
        return 1;
    }

    @Override
    public String getTileURL(Point tile, int zoomLevel) {
        return baseMap.getTileUrl(zoomLevel, tile.getX(), tile.getY());
    }

    @Override
    public boolean isPng() {
        return true;
    }
}
