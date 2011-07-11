package org.sigmah.client.page.map.layerOptions;

import org.sigmah.client.page.common.widget.ColorField;
import org.sigmah.shared.report.model.layers.BubbleMapLayer;
import org.sigmah.shared.report.model.layers.MapLayer;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.Element;

/*
 * Displays a list of options the user can choose to configure a BubbleMapLayer
 */
public class BubbleMapLayerOptions extends ContentPanel implements LayerOptionsWidget {
	private BubbleMapLayer bubbleMapLayer;
	private ColorField colorPicker = new ColorField();
	private FormPanel panel = new FormPanel();
	
	@Override
	public void setMapLayer(MapLayer mapLayer) {
		if (mapLayer instanceof BubbleMapLayer) {
			bubbleMapLayer = (BubbleMapLayer) mapLayer;
		}
		else {
			// Wanna throw an exception?
		}
	}

	public BubbleMapLayerOptions() {
		super();
		
		add(colorPicker);
		setAnimCollapse(false);
		colorPicker.setFieldLabel("woeo");
		setHeading("Bubbles");
		
		// Set the selected color to the maplayer
		colorPicker.addListener(Events.Select, new Listener() {
			@Override
			public void handleEvent(BaseEvent be) {
				bubbleMapLayer.setLabelColor(colorPicker.getIntValue());
			}});
	}
}
