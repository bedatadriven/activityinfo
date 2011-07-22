package org.sigmah.shared.map;

public class SateliteBaseMap extends BaseMap {

	// TODO: Add i18n when serverside i18n is implemented
	@Override
	public String getName() {
		return "Google satelite";
	}

	@Override
	public int getMinZoom() {
		return 1;
	}

	@Override
	public int getMaxZoom() {
		return 21;
	}

	@Override
	public String getCopyright() {
		return "Google inc.";
	}

	@Override
	public void setCopyright(String copyright) {
	}

}
