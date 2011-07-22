package org.sigmah.shared.map;

public class StreetBaseMap extends BaseMap {

	// TODO: Add i18n when serverside i18n is implemented
	@Override
	public String getName() {
		return "Goolge maps";
	}
	
	@Override
	public int getMinZoom() {
		return 1;
	}

	@Override
	public int getMaxZoom() {
		return 21;
	}

}