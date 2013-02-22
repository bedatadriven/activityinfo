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

import org.activityinfo.shared.dto.CountryDTO;
import org.activityinfo.shared.map.BaseMap;
import org.activityinfo.shared.map.GoogleBaseMap;
import org.activityinfo.shared.map.TileBaseMap;

import com.google.gwt.maps.client.MapType;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.TileLayer;
import com.google.gwt.maps.client.geom.MercatorProjection;
import com.google.gwt.maps.client.geom.Projection;

/**
 * Utility class for creating standard GoogleMap MapTypes
 */
public final class MapTypeFactory {

    private MapTypeFactory() {
    }

    /**
     * Creates the default MapType for "localisation", that is selecting the
     * geographic position of sites.
     * 
     * @param country
     *            The country for which to return the MapType
     * @return a GoogleMaps MapType
     */
    public static MapType createLocalisationMapType(CountryDTO country) {
        // TODO: generalize
        // There are probably other cases where we need to add custom tiles
        // for poorly covered countries
        if (country != null && country.getName().equals("RDC")) {
            TileLayer[] layers = new TileLayer[1];
            layers[0] = new RgcTileLayer("http://tiles.aimaps.net/admin", 6, 10);
            Projection projection = new MercatorProjection(11);
            return new MapType(layers, projection, "Admin");
        } else {
            return MapType.getNormalMap();
        }
    }

    /**
     * Creates a GoogleMaps MapType for a given ActivityInfo BaseMap object.
     * 
     * @param baseMap
     * @return
     */
    public static MapType mapTypeForBaseMap(BaseMap baseMap) {
        if (baseMap instanceof TileBaseMap) {
            TileLayer[] layers = new TileLayer[1];
            layers[0] = new BaseMapLayer((TileBaseMap) baseMap);
            Projection projection = new MercatorProjection(
                baseMap.getMaxZoom() + 1);
            return new MapType(layers, projection,
                ((TileBaseMap) baseMap).getName());

        } else if (baseMap.equals(GoogleBaseMap.SATELLITE)) {
            return MapType.getSatelliteMap();

        } else if (baseMap.equals(GoogleBaseMap.ROADMAP)) {
            return MapType.getNormalMap();
        } else if (baseMap.equals(GoogleBaseMap.HYBRID)) {
            return MapType.getHybridMap();
        } else if (baseMap.equals(GoogleBaseMap.TERRAIN)) {
            return MapType.getPhysicalMap();
        }
        return null;
    }

    /**
     * Removes all existing MapTypes from a GoogleMaps MapWidget
     * 
     * @param widget
     */
    public static void clear(MapWidget widget) {
        for (MapType t : widget.getMapTypes()) {
            widget.removeMapType(t);
        }
    }
}
