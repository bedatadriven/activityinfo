package org.activityinfo.client.page.entry.sitehistory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activityinfo.shared.dto.LocationDTO;
import org.activityinfo.shared.dto.SchemaDTO;
import org.activityinfo.shared.dto.SiteDTO;
import org.activityinfo.shared.dto.SiteHistoryDTO;

class RenderContext {
	private SchemaDTO schema;
	private Map<Integer, LocationDTO> locations;
	private SiteDTO site;
	private SiteHistoryDTO history;
	private Map<String, Object> state;

	RenderContext(SchemaDTO schema, List<LocationDTO> locations, SiteDTO site, Map<String, Object> baselineState) {
		this.schema = schema;
		this.locations = new HashMap<Integer, LocationDTO>();
		for (LocationDTO dto : locations) {
			this.locations.put(dto.getId(), dto);
		}
		this.site = site;
		this.state = baselineState;
	}
	
	SchemaDTO getSchema() {
		return schema;
	}
	
	LocationDTO getLocation(Integer locationId) {
		return locations.get(locationId);
	}
	
	SiteDTO getSite() {
		return site;
	}
	
	SiteHistoryDTO getHistory() {
		return history;
	}
	
	void setHistory(SiteHistoryDTO history) {
		this.history = history;
	}
	
	Map<String, Object> getState() {
		return state;
	}
}