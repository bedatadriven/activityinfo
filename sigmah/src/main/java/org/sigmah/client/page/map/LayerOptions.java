package org.sigmah.client.page.map;

import org.sigmah.client.page.map.layerOptions.BubbleMapLayerOptions;
import org.sigmah.client.page.map.layerOptions.IconMapLayerOptions;
import org.sigmah.client.page.map.layerOptions.LayerOptionsWidget;
import org.sigmah.client.page.map.layerOptions.PiechartMapLayerOptions;
import org.sigmah.shared.report.model.layers.BubbleMapLayer;
import org.sigmah.shared.report.model.layers.IconMapLayer;
import org.sigmah.shared.report.model.layers.MapLayer;
import org.sigmah.shared.report.model.layers.PiechartMapLayer;

import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;

public class LayerOptions extends LayoutContainer {
	// UI for the maplayer options the user can set
	private MapLayer selectedMapLayer;

	// Options for every supported MapLayer type
	private BubbleMapLayerOptions bubbleMapLayerOptions = new BubbleMapLayerOptions();
	private IconMapLayerOptions iconMapLayerOptions = new IconMapLayerOptions();
	private PiechartMapLayerOptions piechartMapLayerOptions = new PiechartMapLayerOptions();
	private ContentPanel contentpanelLayerOptions = new ContentPanel();

	// Aggregation options
	private AggregationOptionsWidget aggregationOptions = new AggregationOptionsWidget();

	@Override
	protected void onRender(Element parent, int index) {
		super.onRender(parent, index);
		
		setLayout(new FitLayout());
		
		contentpanelLayerOptions.add(bubbleMapLayerOptions);
		
		add(contentpanelLayerOptions);
		add(aggregationOptions);
	}

	/*
	 * Changes active widget showing layer options 
	 */
	public void setMapLayer(MapLayer mapLayer) {
		if (mapLayer instanceof BubbleMapLayer) {
			setActiveMapLayer(bubbleMapLayerOptions);
			bubbleMapLayerOptions.setMapLayer(mapLayer);
		}
		if (mapLayer instanceof IconMapLayer) {
			setActiveMapLayer(iconMapLayerOptions);
			bubbleMapLayerOptions.setMapLayer(mapLayer);
		}
		if (mapLayer instanceof PiechartMapLayer) {
			setActiveMapLayer(piechartMapLayerOptions);
			bubbleMapLayerOptions.setMapLayer(mapLayer);
		}
	}
	
	private void setActiveMapLayer(LayerOptionsWidget layerOptionsWidget) {
		contentpanelLayerOptions.removeAll();
		contentpanelLayerOptions.add((Widget)layerOptionsWidget);
	}

	public MapLayer getMapLayer() {
		selectedMapLayer.setClustering(aggregationOptions.getSelectedClustering());
		return selectedMapLayer;
	}
}
