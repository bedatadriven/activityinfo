package org.sigmah.client.page.map;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class MapLayerModel extends BaseModelData {

	public String getName() {
		return get("name");
	}

	public void setName(String name) {
		set("name", name);
	}

	public boolean isVisible() {
		return get("visible");
	}

	public void setVisible(boolean isVisible) {
		set("visible", isVisible);
	}
}