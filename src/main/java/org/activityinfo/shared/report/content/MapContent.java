/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.shared.report.content;

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
			if (indicator.getId() ==  indicatorId) {
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
