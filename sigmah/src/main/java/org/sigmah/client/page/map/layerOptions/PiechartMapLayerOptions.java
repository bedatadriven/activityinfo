package org.sigmah.client.page.map.layerOptions;

import org.sigmah.client.page.common.widget.ColorField;
import org.sigmah.client.page.map.MapResources;
import org.sigmah.shared.report.model.layers.MapLayer;
import org.sigmah.shared.report.model.layers.PiechartMapLayer;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.ListView;

/*
 * Displays a list of options to configure a PiechartMapLayer
 */
public class PiechartMapLayerOptions extends ContentPanel implements LayerOptionsWidget {
	private PiechartMapLayer piechartMapLayer;
	private ListView listviewPiechartPies;
	private ColorField colorPicker = new ColorField();

	public PiechartMapLayerOptions() {
		super();
		
		add(colorPicker);
		setAnimCollapse(false);

		colorPicker.addListener(Events.Select, new Listener() {

			@Override
			public void handleEvent(BaseEvent be) {
				System.out.println();
			}});
		
		setHeading("woei");
	}

	
	@Override
	public void setMapLayer(MapLayer mapLayer) {
		if (mapLayer instanceof PiechartMapLayer) {
			this.piechartMapLayer = (PiechartMapLayer) mapLayer;
		}
		else {
			//
		}
	}

}
