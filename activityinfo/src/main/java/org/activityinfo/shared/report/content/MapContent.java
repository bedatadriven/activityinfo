package org.activityinfo.shared.report.content;

import org.activityinfo.shared.map.BaseMap;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
}
