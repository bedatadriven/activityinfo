package org.activityinfo.server.report.generator.map;

import org.activityinfo.shared.report.content.Extents;
import org.activityinfo.shared.report.content.Marker;
import org.activityinfo.shared.report.content.SiteGeoData;

import java.util.List;
import java.util.Collection;
import java.util.LinkedList;
import java.util.ArrayList;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class MarkerUtil {


    public static <T extends SiteGeoData> Extents calcExtents(Collection<T> sites) {
        Extents extents = Extents.emptyExtents();

        for(T site : sites) {
            if(site.hasLatLong()) {
                extents.grow(site.getLatitude(), site.getLongitude());
            }
        }

        return extents;
    }

    public static <T extends SiteGeoData> List<Marker<T>> cluster(TiledMap map, Collection<T> sites, int radius) {

        List<Marker<T>> markers = new LinkedList<Marker<T>>();

        for(T site : sites) {
            if(site.hasLatLong()) {
                markers.add(new Marker<T>(site));
            }
        }

		/*
		 * Project Markers
		 */

		int index = 1;
		for(Marker<T> marker : markers ) {
			marker.setSymbol(Integer.toString(index++));
			marker.addPoint( map.fromLatLngToPixel(marker.getLatLng()) );
		}

		ArrayList<Marker<T>> origMarkers = new ArrayList<Marker<T>>(markers);


		/*
		 * Cluster together
		 */

		boolean clustered;

		do {

			clustered = false;
			double diameter = ((double)radius)*2.0;

			for(int i=0; i!=markers.size(); ++i) {

				int j = i + 1;
				while(j < markers.size()) {

					if( markers.get(i).center().distance(
							markers.get(j).center()) < diameter )
					{
						markers.get(i).merge(markers.get(j));
						markers.remove(j);

						clustered=true;
					} else {
						j++;
					}
				}
			}
		} while(clustered);

        return markers;
    }
}
