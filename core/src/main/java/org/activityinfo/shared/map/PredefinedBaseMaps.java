package org.activityinfo.shared.map;

import java.util.HashMap;
import java.util.Map;

public final class PredefinedBaseMaps {

	private PredefinedBaseMaps() {}
	
	
	private static Map<String, BaseMap> maps; 
	
	static {
		maps = new HashMap<String, BaseMap>();
		maps.put(GoogleBaseMap.ROADMAP.getId(), GoogleBaseMap.ROADMAP);
		maps.put(GoogleBaseMap.SATELLITE.getId(), GoogleBaseMap.SATELLITE);
		maps.put(GoogleBaseMap.HYBRID.getId(), GoogleBaseMap.HYBRID);
		maps.put(GoogleBaseMap.TERRAIN.getId(), GoogleBaseMap.TERRAIN);
	}
	
	public static boolean isPredefinedMap(String id) {
		return maps.containsKey(id);
	}
	
	/**
	 * 
	 * @param id
	 * @return a reference to the predefined base map identified by {@code id},
	 * or {@code null} if there is no predefined basemap with this id.
	 */
	public static BaseMap forId(String id) {
		return maps.get(id);
	}
	
}
