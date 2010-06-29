package org.sigmah.shared.report.content;

import org.sigmah.shared.map.BaseMap;

import java.util.*;
/*
 * @author Alex Bertram
 */

public class MapContent implements Content {


    private BaseMap baseMap;
    private List<FilterDescription> filterDescriptions;
    private List<MapMarker> markers = new ArrayList<MapMarker>();
    private java.util.Set<Integer> unmappedSites = new HashSet<Integer>();
    private Extents extents;
    private int zoomLevel;

    public MapContent() {

    }

    public List<FilterDescription> getFilterDescriptions() {
        return filterDescriptions;
    }

    public void setFilterDescriptions(List<FilterDescription> filterDescriptions) {
        this.filterDescriptions = filterDescriptions;
    }

    public List<MapMarker> getMarkers() {
        return markers;
    }

    public void setMarkers(List<MapMarker> markers) {
        this.markers = markers;
    }

    public Set<Integer> getUnmappedSites() {
        return unmappedSites;
    }

    public void setUnmappedSites(Set<Integer> unmappedSites) {
        this.unmappedSites = unmappedSites;
    }

    public Extents getExtents() {
        return extents;
    }

    public void setExtents(Extents extents) {
        this.extents = extents;
    }

    public int getZoomLevel() {
        return zoomLevel;
    }

    public void setZoomLevel(int zoomLevel) {
        this.zoomLevel = zoomLevel;
    }

    public BaseMap getBaseMap() {
        return baseMap;
    }

    public void setBaseMap(BaseMap baseMap) {
        this.baseMap = baseMap;
    }

    public Map<Integer, String> siteLabelMap() {
        Map<Integer, String> map = new HashMap<Integer, String>();
        for(MapMarker marker : getMarkers()) {
            if(marker instanceof BubbleMapMarker && ((BubbleMapMarker) marker).getLabel() != null) {
                for (Integer siteId : marker.getSiteIds()) {
                    map.put(siteId, ((BubbleMapMarker) marker).getLabel());
                }
            }
        }
        return map;
    }
}
