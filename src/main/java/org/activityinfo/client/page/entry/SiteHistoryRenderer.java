package org.activityinfo.client.page.entry;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.i18n.I18N;
import org.activityinfo.shared.dto.AttributeDTO;
import org.activityinfo.shared.dto.IndicatorDTO;
import org.activityinfo.shared.dto.LocationDTO;
import org.activityinfo.shared.dto.SchemaDTO;
import org.activityinfo.shared.dto.SiteDTO;
import org.activityinfo.shared.dto.SiteHistoryDTO;

import com.google.common.collect.Lists;

public class SiteHistoryRenderer {

	public String render(SchemaDTO schema, List<LocationDTO> locations, SiteDTO site, List<SiteHistoryDTO> histories) {
		StringBuilder html = new StringBuilder();
		
		if (histories.size() > 0) {
			List<String> historyHtmlItems = Lists.newArrayList();
			
			Context ctx = new Context(schema, locations, histories.get(0).getJsonMap());
			boolean first = true;
			for (SiteHistoryDTO history : histories) {
				ctx.setHistory(history);
				StringBuilder item = new StringBuilder();
				item.append("<p>");
				if (first) {
					// TODO get values from site. User & date of first historyentry are not always from creation but from first update
					String msg = I18N.MESSAGES.siteHistoryCreated(history.getDateCreated(), history.getUserName(), history.getUserEmail());
					item.append(renderHeader(msg));
					first = false;
				} else {
					String msg = I18N.MESSAGES.siteHistoryUpdated(history.getDateCreated(), history.getUserName(), history.getUserEmail());
					item.append(renderHeader(msg));
					item.append(renderUpdates(ctx));
				}
				item.append("</p>");
				historyHtmlItems.add(item.toString());
			}
			
			Collections.reverse(historyHtmlItems);
			
			for (String historyItem: historyHtmlItems) {
				html.append(historyItem);
			}
		}
		return html.toString();
	}

	private String renderHeader(String msg) {
		StringBuilder header = new StringBuilder();
		header.append("<span style='color: #15428B; font-weight: bold;'>");
		header.append(msg);
		header.append("</span><br/>");
		return header.toString();
	}
	
	private String renderUpdates(Context ctx) {
		StringBuilder updates = new StringBuilder();
		updates.append("<ul style='margin:0px 0px 10px 20px; font-size: 11px;'>");
		for (Map.Entry<String, Object> entry : ctx.getHistory().getJsonMap().entrySet()) {
			updates.append("<li>");
			updates.append(renderUpdate(ctx, entry));
			updates.append("</li>");
		}
		updates.append("</ul>");
		return updates.toString();
	}
	
	private String renderUpdate(Context ctx, Map.Entry<String, Object> entry) {
		Map<String, Object> state = ctx.getState();
		SchemaDTO schema = ctx.getSchema();
		
		String key = entry.getKey();
		final Object oldValue = state.get(key);
		final Object newValue = entry.getValue();
		state.put(key, newValue);
		
		final StringBuilder sb = new StringBuilder();
		
		// basic
		if (key.equals("date1")) {
			addValues(sb, I18N.CONSTANTS.startDate(), oldValue, newValue);
		}

		else if (key.equals("date2")) {
			addValues(sb, I18N.CONSTANTS.endDate(), oldValue, newValue);
		}
		
		else if (key.equals("comments")) {
			addValues(sb, I18N.CONSTANTS.comments(), oldValue, newValue);
		}
		
		// schema lookups
		else if (key.equals("locationId")) {
			String oldName = null;
			if (oldValue != null) {
				oldName = ctx.getLocation(toInt(oldValue)).getName();
			}
			String newName = ctx.getLocation(toInt(newValue)).getName();
			addValues(sb, I18N.CONSTANTS.location(), oldName, newName);
		}
		
		else if (key.equals("projectId")) {
			String oldName = null;
			if (oldValue != null) {
				oldName = schema.getProjectById(toInt(oldValue)).getName();
			}
			String newName = schema.getProjectById(toInt(newValue)).getName();
			addValues(sb, I18N.CONSTANTS.project(), oldName, newName);
		}		

		else if (key.equals("partnerId")) {
			String oldName = null;
			if (oldValue != null) {
				oldName = schema.getPartnerById(toInt(oldValue)).getName();
			}
			String newName = schema.getPartnerById(toInt(newValue)).getName();
			addValues(sb, I18N.CONSTANTS.partner(), oldName, newName);
		}		

		// custom
		else if (key.startsWith(IndicatorDTO.PROPERTY_PREFIX)) {
			int id = IndicatorDTO.indicatorIdForPropertyName(key);
			IndicatorDTO dto = schema.getIndicatorById(id);
			addValues(sb, dto.getName(), oldValue, newValue, dto.getUnits());
		}
				
		else if (key.startsWith(AttributeDTO.PROPERTY_PREFIX)) {
			int id = AttributeDTO.idForPropertyName(key);
			AttributeDTO dto = schema.getAttributeById(id);
			if (Boolean.parseBoolean(newValue.toString())) {
				sb.append(I18N.MESSAGES.siteHistoryAttrAdd(dto.getName()));
			} else {
				sb.append(I18N.MESSAGES.siteHistoryAttrRemove(dto.getName()));
			}
		}
		
		
		// fallback
		else {
			addValues(sb, key, oldValue, newValue);
		}
		
		return sb.toString();
	}

	private void addValues(StringBuilder sb, String key, Object oldValue, Object newValue) {
		addValues(sb, key, oldValue, newValue, null);
	}
	
	private void addValues(StringBuilder sb, String key, Object oldValue, Object newValue, String units) {
		sb.append(key);
		sb.append(": ");
		sb.append(newValue);
		
		if (units != null) {
			sb.append(" ");
			sb.append(units);
		}

		if (!equals(oldValue, newValue)) {
			sb.append(" (");
			if (oldValue == null) {
				sb.append(I18N.MESSAGES.siteHistoryOldValueBlank());
			} else {
				sb.append(I18N.MESSAGES.siteHistoryOldValue(oldValue));
			}
			sb.append(")");
		}
	}

    private boolean equals(Object oldValue, Object newValue) {
        if (oldValue == newValue) {
            return true;
        }
        if ((oldValue == null) || (newValue == null)) {
            return false;
        }
        return oldValue.equals(newValue);
    }
    
    private int toInt(Object val) {
    	return Integer.parseInt(val.toString());
    }
    
    private class Context {
    	private SchemaDTO schema;
    	private Map<Integer, LocationDTO> locations;
    	private SiteHistoryDTO history;
    	private Map<String, Object> state;

		public Context(SchemaDTO schema, List<LocationDTO> locations, Map<String, Object> state) {
			super();
			this.schema = schema;
			this.locations = new HashMap<Integer, LocationDTO>();
			for (LocationDTO dto : locations) {
				this.locations.put(dto.getId(), dto);
			}
			this.state = state;
		}
		public SchemaDTO getSchema() {
			return schema;
		}
		public LocationDTO getLocation(Integer locationId) {
			return locations.get(locationId);
		}
		public SiteHistoryDTO getHistory() {
			return history;
		}
		public void setHistory(SiteHistoryDTO history) {
			this.history = history;
		}
		public Map<String, Object> getState() {
			return state;
		}
    }
}
