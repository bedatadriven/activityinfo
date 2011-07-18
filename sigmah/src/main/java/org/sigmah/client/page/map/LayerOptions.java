package org.sigmah.client.page.map;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.page.map.layerOptions.BubbleMapLayerOptions;
import org.sigmah.client.page.map.layerOptions.IconMapLayerOptions;
import org.sigmah.client.page.map.layerOptions.LayerOptionsWidget;
import org.sigmah.client.page.map.layerOptions.PiechartMapLayerOptions;
import org.sigmah.shared.report.model.layers.BubbleMapLayer;
import org.sigmah.shared.report.model.layers.IconMapLayer;
import org.sigmah.shared.report.model.layers.MapLayer;
import org.sigmah.shared.report.model.layers.PiechartMapLayer;

import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayout.VBoxLayoutAlign;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Widget;

public class LayerOptions extends ContentPanel implements HasValue<MapLayer> {
	private MapLayer selectedMapLayer;

	// Options for every supported MapLayer type
	private BubbleMapLayerOptions bubbleMapLayerOptions = new BubbleMapLayerOptions();
	private IconMapLayerOptions iconMapLayerOptions = new IconMapLayerOptions();
	private PiechartMapLayerOptions piechartMapLayerOptions;

	// Clustering options
	private ClusteringOptionsWidget clusteringOptions;

	// Have a nice box with optional collapsing around clustering/layeroptions
	private FieldSet fieldsetClustering = new FieldSet();
	private FieldSet fieldsetLayerSpecificOptions = new FieldSet();

	private Dispatcher service;

	public LayerOptions(Dispatcher service) {
		super();
	
		this.service = service;
		clusteringOptions = new ClusteringOptionsWidget(service);
	}

	@Override
	protected void onRender(Element parent, int index) {
		super.onRender(parent, index);
		
		createBubbleLayerOptionsWidget();
		createIconLayerOptionsWidget();
		createPiechartLayerOptionsWidget();

		initializeComponent();
	}

	private void createPiechartLayerOptionsWidget() {
		piechartMapLayerOptions = new PiechartMapLayerOptions(service);
		
		piechartMapLayerOptions.addValueChangeHandler(new ValueChangeHandler<PiechartMapLayer>() {
			@Override
			public void onValueChange(ValueChangeEvent<PiechartMapLayer> event) {
				ValueChangeEvent.fire(LayerOptions.this, event.getValue());
			}
		});
	}

	private void createIconLayerOptionsWidget() {
		iconMapLayerOptions.addValueChangeHandler(new ValueChangeHandler<IconMapLayer>() {
			@Override
			public void onValueChange(ValueChangeEvent<IconMapLayer> event) {
				ValueChangeEvent.fire(LayerOptions.this, event.getValue());
			}
		});
	}

	private void createBubbleLayerOptionsWidget() {
		bubbleMapLayerOptions.addValueChangeHandler(new ValueChangeHandler<BubbleMapLayer>() {
			@Override
			public void onValueChange(ValueChangeEvent<BubbleMapLayer> event) {
				ValueChangeEvent.fire(LayerOptions.this, event.getValue());
			}
		});
	}

	private void initializeComponent() {
		VBoxLayout vBoxLayout = new VBoxLayout();
		vBoxLayout.setVBoxLayoutAlign(VBoxLayoutAlign.STRETCH);
		setLayout(new FlowLayout());
		
		setFieldsetHeadingToLayerName();
		fieldsetLayerSpecificOptions.setCollapsible(true);
		fieldsetLayerSpecificOptions.setAutoWidth(true);
		fieldsetLayerSpecificOptions.setLayout(new FlowLayout());
		fieldsetClustering.setLayout(new FlowLayout());
		add(fieldsetLayerSpecificOptions);

		fieldsetClustering.setHeading(I18N.CONSTANTS.clustering());
		fieldsetClustering.setCollapsible(true);		
		fieldsetClustering.add(clusteringOptions);
		fieldsetClustering.setAutoWidth(true);
		add(fieldsetClustering);
	}

	private void setFieldsetHeadingToLayerName() {
		fieldsetLayerSpecificOptions.setHeading(getLayerTypeName() + " " + I18N.CONSTANTS.options());		
	}

	private String getLayerTypeName() {
		if (selectedMapLayer != null) {
			return selectedMapLayer.getTypeName();
		} else {
			return "[NONE]";
		}
	}

	/*
	 * Changes active widget showing layer options 
	 */
	public void setMapLayer(MapLayer mapLayer) {
		this.selectedMapLayer = mapLayer;
		
		if (mapLayer instanceof BubbleMapLayer) {
			bubbleMapLayerOptions.setValue((BubbleMapLayer) mapLayer);
			setActiveMapLayer(bubbleMapLayerOptions);
		}
		if (mapLayer instanceof IconMapLayer) {
			iconMapLayerOptions.setValue((IconMapLayer) mapLayer);
			setActiveMapLayer(iconMapLayerOptions);
		}
		if (mapLayer instanceof PiechartMapLayer) {
			piechartMapLayerOptions.setValue((PiechartMapLayer) mapLayer);
			setActiveMapLayer(piechartMapLayerOptions);
		}
	}
	
	private void setActiveMapLayer(LayerOptionsWidget layerOptionsWidget) {
		fieldsetLayerSpecificOptions.removeAll();
		fieldsetLayerSpecificOptions.add((Widget)layerOptionsWidget);
		fieldsetLayerSpecificOptions.layout();
		setFieldsetHeadingToLayerName();
	}

	/*
	 * Sets the selected options to the current MapLayer and returns the MapLayer
	 */
	public MapLayer getMapLayer() {
		selectedMapLayer.setClustering(clusteringOptions.getSelectedClustering());
		return selectedMapLayer;
	}

	@Override
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<MapLayer> handler) {
		return this.addHandler(handler, ValueChangeEvent.getType());
	}

	@Override
	public MapLayer getValue() {
		return selectedMapLayer;
	}

	@Override
	public void setValue(MapLayer value) {
		
	}

	@Override
	public void setValue(MapLayer value, boolean fireEvents) {
		
	}
}
