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
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayout.VBoxLayoutAlign;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;

public class LayerOptions extends ContentPanel {
	private MapLayer selectedMapLayer;

	// Options for every supported MapLayer type
	private BubbleMapLayerOptions bubbleMapLayerOptions = new BubbleMapLayerOptions();
	private IconMapLayerOptions iconMapLayerOptions = new IconMapLayerOptions();
	private PiechartMapLayerOptions piechartMapLayerOptions = new PiechartMapLayerOptions();

	// Clustering options
	private ClusteringOptionsWidget clusteringOptions = new ClusteringOptionsWidget();

	// Have a nice box with optional collapsing around clustering/layeroptions
	private FieldSet fieldsetClustering = new FieldSet();
	private FieldSet fieldsetLayerSpecificOptions = new FieldSet();

	@Override
	protected void onRender(Element parent, int index) {
		super.onRender(parent, index);
		
		initializeComponent();
	}

	private void initializeComponent() {
		VBoxLayout vBoxLayout = new VBoxLayout();
		vBoxLayout.setVBoxLayoutAlign(VBoxLayoutAlign.STRETCH);
		setLayout(new FlowLayout());
		
		setHeading("Layer options");
		
		//fieldsetLayerSpecificOptions.setLayout(new FitLayout());
		fieldsetLayerSpecificOptions.setHeading(getLayerTypeName() + " " + "options");
		fieldsetLayerSpecificOptions.setCollapsible(true);
		add(fieldsetLayerSpecificOptions);

		//fieldsetClustering.setLayout(new FitLayout());
		fieldsetClustering.setHeading(I18N.CONSTANTS.aggregation());
		fieldsetClustering.setCollapsible(true);		
		fieldsetClustering.add(clusteringOptions);
		add(fieldsetClustering);
	}

	private String getLayerTypeName() {
		if (selectedMapLayer != null) {
			return "LAYERTYPE";
		} else {
			return "[NONE]";
		}
			
	}

	/*
	 * Changes active widget showing layer options 
	 */
	public void setMapLayer(MapLayer mapLayer) {
		if (mapLayer instanceof BubbleMapLayer) {
			setActiveMapLayer(bubbleMapLayerOptions);
			bubbleMapLayerOptions.setValue((BubbleMapLayer) mapLayer);
		}
		if (mapLayer instanceof IconMapLayer) {
			setActiveMapLayer(iconMapLayerOptions);
			iconMapLayerOptions.setValue((IconMapLayer) mapLayer);
		}
		if (mapLayer instanceof PiechartMapLayer) {
			setActiveMapLayer(piechartMapLayerOptions);
			piechartMapLayerOptions.setValue((PiechartMapLayer) mapLayer);
		}
	}
	
	private void setActiveMapLayer(LayerOptionsWidget layerOptionsWidget) {
		fieldsetLayerSpecificOptions.removeAll();
		fieldsetLayerSpecificOptions.add((Widget)layerOptionsWidget);
		fieldsetLayerSpecificOptions.layout();
	}

	/*
	 * Sets the selected options to the current MapLayer and returns the MapLayer
	 */
	public MapLayer getMapLayer() {
		selectedMapLayer.setClustering(clusteringOptions.getSelectedClustering());
		return selectedMapLayer;
	}
}
