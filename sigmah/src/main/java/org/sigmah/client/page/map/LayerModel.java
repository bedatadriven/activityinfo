package org.sigmah.client.page.map;

import org.sigmah.shared.report.model.layers.MapLayer;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class LayerModel extends BaseModelData {
	private transient MapLayer mapLayer;
	public String getName() {
		return get("name");
	}

	public void setName(String name) {
		set("name", name);
	}

	public boolean isVisible() {
		return (Boolean) get("visible");
	}

	public void setVisible(boolean isVisible) {
		set("visible", isVisible);
	}

	public void setMapLayer(MapLayer mapLayer) {
		this.mapLayer = mapLayer;
	}

	public MapLayer getMapLayer() {
		return mapLayer;
	}
	
	public String getLayerType() {
		return get("type");
	}
	
	public void setLayerType(String type) {
		set("type", type);
	}
}