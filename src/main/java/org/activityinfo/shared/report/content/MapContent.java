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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.activityinfo.shared.dto.IndicatorDTO;
import org.activityinfo.shared.map.BaseMap;
import org.activityinfo.shared.util.mapping.Extents;

import com.google.common.collect.Lists;

/*
 * Model of a fully generated and realized map. 
 * 
 */
public class MapContent implements Content {
    private BaseMap baseMap;
    private List<FilterDescription> filterDescriptions;
    private List<MapLayerLegend> legends = new ArrayList<MapLayerLegend>();
    private List<MapMarker> markers = new ArrayList<MapMarker>();
    private Set<Integer> unmappedSites = new HashSet<Integer>();
    private Set<IndicatorDTO> indicators = new HashSet<IndicatorDTO>();
    private List<AdminOverlay> adminOverlays = Lists.newArrayList();
    private int zoomLevel;
    private AiLatLng center;
    private Extents extents;

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

    public int getZoomLevel() {
        return zoomLevel;
    }

    public void setZoomLevel(int zoomLevel) {
        this.zoomLevel = zoomLevel;
    }

    public BaseMap getBaseMap() {
        return baseMap;
    }

    public AiLatLng getCenter() {
        return center;
    }

    public void setCenter(AiLatLng center) {
        this.center = center;
    }

    public void setBaseMap(BaseMap baseMap) {
        this.baseMap = baseMap;
    }

    public Set<IndicatorDTO> getIndicators() {
        return indicators;
    }

    public void setIndicators(Set<IndicatorDTO> indicators) {
        this.indicators = indicators;
    }

    public IndicatorDTO getIndicatorById(int indicatorId) {
        for (IndicatorDTO indicator : indicators) {
            if (indicator.getId() == indicatorId) {
                return indicator;
            }
        }
        return null;
    }

    public List<MapLayerLegend> getLegends() {
        return legends;
    }

    public void setLegends(List<MapLayerLegend> legends) {
        this.legends = legends;
    }

    public void addLegend(MapLayerLegend legend) {
        this.legends.add(legend);
    }

    public List<AdminOverlay> getAdminOverlays() {
        return adminOverlays;
    }

    public void setAdminOverlays(List<AdminOverlay> adminOverlays) {
        this.adminOverlays = adminOverlays;
    }

    public Map<Integer, String> siteLabelMap() {
        Map<Integer, String> map = new HashMap<Integer, String>();
        for (MapMarker marker : getMarkers()) {
            if (marker instanceof BubbleMapMarker
                && ((BubbleMapMarker) marker).getLabel() != null) {
                for (Integer siteId : marker.getSiteIds()) {
                    map.put(siteId, ((BubbleMapMarker) marker).getLabel());
                }
            }
        }
        return map;
    }

    public Extents getExtents() {
        return extents;
    }

    public void setExtents(Extents extents) {
        this.extents = extents;
    }

}
