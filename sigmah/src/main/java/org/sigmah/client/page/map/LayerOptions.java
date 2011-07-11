package org.sigmah.client.page.map;

import org.sigmah.client.i18n.I18N;
import org.sigmah.client.page.map.layerOptions.BubbleMapLayerOptions;
import org.sigmah.client.page.map.layerOptions.IconMapLayerOptions;
import org.sigmah.client.page.map.layerOptions.LayerOptionsWidget;
import org.sigmah.client.page.map.layerOptions.PiechartMapLayerOptions;
import org.sigmah.shared.report.model.layers.BubbleMapLayer;
import org.sigmah.shared.report.model.layers.IconMapLayer;
import org.sigmah.shared.report.model.layers.MapLayer;
import org.sigmah.shared.report.model.layers.PiechartMapLayer;

import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.Radio;
import com.extjs.gxt.ui.client.widget.form.RadioGroup;
import com.extjs.gxt.ui.client.widget.layout.AccordionLayout;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.user.client.Element;

public class LayerOptions extends LayoutContainer {
	// UI for the maplayer options the user can set
	private MapLayer selectedMapLayer;
	private BubbleMapLayerOptions bubbleMapLayerOptions = new BubbleMapLayerOptions();
	private IconMapLayerOptions iconMapLayerOptions = new IconMapLayerOptions();
	private PiechartMapLayerOptions piechartMapLayerOptions = new PiechartMapLayerOptions();
	private ContentPanel contentpanelLayerOptions = new ContentPanel();

	// Aggregation of elements on the map
	private FieldSet fieldsetAggregation = new FieldSet();
	private RadioGroup radiogroupAggregation = new RadioGroup();
	private Radio radioAdminLevelAggr = new Radio();
	private Radio radioAutomaticAggr = new Radio();
	private Radio radioNoAggr = new Radio();

	@Override
	protected void onRender(Element parent, int index) {
		super.onRender(parent, index);
		
		setLayout(new FitLayout());
		
		contentpanelLayerOptions.add(bubbleMapLayerOptions);
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
		contentpanelLayerOptions.add(contentpanelLayerOptions);
	}

	private void createAggregation() {
		fieldsetAggregation.setLayout(new RowLayout(Orientation.VERTICAL));
		fieldsetAggregation.setHeading(I18N.CONSTANTS.aggregation());
		fieldsetAggregation.setCollapsible(true);
		
		radioAdminLevelAggr.setBoxLabel(I18N.CONSTANTS.administrativeLevel());
		radioAutomaticAggr.setBoxLabel(I18N.CONSTANTS.automatic());
		radioNoAggr.setBoxLabel(I18N.CONSTANTS.none());

		radiogroupAggregation.add(radioAdminLevelAggr);
		radiogroupAggregation.add(radioAutomaticAggr);
		radiogroupAggregation.add(radioNoAggr);
		
		fieldsetAggregation.add(radioAdminLevelAggr);
		fieldsetAggregation.add(radioAutomaticAggr);
		fieldsetAggregation.add(radioNoAggr);
	}

	public MapLayer getMapLayer() {
		return selectedMapLayer;
	}
}
