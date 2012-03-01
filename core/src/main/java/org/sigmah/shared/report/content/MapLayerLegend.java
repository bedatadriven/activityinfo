package org.sigmah.shared.report.content;

import java.io.Serializable;

import org.sigmah.shared.report.model.layers.MapLayer;

/**
 * Describes a generated map layer
 * 
 */
public class MapLayerLegend<L extends MapLayer> implements Serializable{

	private L definition;

	public MapLayerLegend() {
		
	}
	
	public MapLayerLegend(L definition) {
		this.definition = definition;
	}
	
	public L getDefinition() {
		return definition;
	}

	public void setDefinition(L definition) {
		this.definition = definition;
	}
}
