package org.activityinfo.shared.map;

/**
 * BaseMap provided through the Google Maps API
 */
public final class GoogleBaseMap extends BaseMap {

	/**
	 *  specifies a standard roadmap image, as is normally shown on the Google Maps website
	 */
	public static final GoogleBaseMap ROADMAP = new GoogleBaseMap("Google.ROADMAP", "roadmap");
	
	/**
	 *  specifies a satellite image.
	 */
	public static final GoogleBaseMap SATELLITE = new GoogleBaseMap("Google.SATELLITE", "satellite");
	
	/**
	 * specifies a physical relief map image, showing terrain and vegetation.
	 */
	public static final GoogleBaseMap TERRAIN = new GoogleBaseMap("Google.TERRAIN", "terrain");

	/**
	 * specifies a hybrid of the satellite and roadmap image, showing a transparent layer of
	 * major streets and place names on the satellite image.
	 */
	public static final GoogleBaseMap HYBRID = new GoogleBaseMap("Google.HYBRID", "hybrid");
	
	private String id;
	private String formatId;
	
	private GoogleBaseMap() {
		
	}
	
	private GoogleBaseMap(String mapId, String formatId) {
		this.id = mapId;
		this.formatId = formatId;
	}
	
	@Override
	public String getId() {
		return id;
	}

	@Override
	public int getMinZoom() {
		return 1;
	}

	@Override
	public int getMaxZoom() {
		return 21;
	}
	
	/**
	 * 
	 * @return this GoogleBaseMap's format id, to be used in requests to the static map API
	 */
	public String getFormatId() {
		return formatId;
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		GoogleBaseMap other = (GoogleBaseMap) obj;
		return id.equals(other.id);
	}
	
	@Override
	public String toString() {
		return "Google " + formatId + " base map";
	}
}
