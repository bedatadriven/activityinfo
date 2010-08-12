/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.map;

import com.google.gwt.maps.client.MapType;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.TileLayer;
import com.google.gwt.maps.client.geom.MercatorProjection;
import com.google.gwt.maps.client.geom.Projection;
import org.sigmah.shared.dto.CountryDTO;
import org.sigmah.shared.map.BaseMap;

/**
 * Utility class for creating standard GoogleMap MapTypes
 */
public class MapTypeFactory {

    private MapTypeFactory() {}

    /**
     * Creates the default MapType for "localisation", that is selecting
     * the geographic position of sites.
     *
     * @param country  The country for which to return the MapType
     * @return  a GoogleMaps MapType
     */
    public static MapType createLocalisationMapType(CountryDTO country) {
        // TODO: generalize
        // There are probably other cases where we need to add custom tiles
        // for poorly covered countries
        if(country.getName().equals("RDC")) {
            TileLayer[] layers = new TileLayer[1];
            layers[0] = new RgcTileLayer("http://www.pear.cd/ActivityInfo/tiles/admin", 6, 10);
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
        TileLayer[] layers = new TileLayer[1];
        layers[0] = new BaseMapLayer(baseMap);
        Projection projection = new MercatorProjection(baseMap.getMaxZoom()+1);

        return new MapType(layers, projection, baseMap.getName());
    }

    /**
     * Removes all existing MapTypes from a GoogleMaps MapWidget
     *
     * @param widget
     */
    public static void clear(MapWidget widget) {
        for(MapType t : widget.getMapTypes()) {
            widget.removeMapType(t);
        }
    }
}
