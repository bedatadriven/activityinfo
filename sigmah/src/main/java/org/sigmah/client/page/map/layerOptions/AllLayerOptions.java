package org.sigmah.client.page.map.layerOptions;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.i18n.I18N;
import org.sigmah.shared.report.model.clustering.Clustering;
import org.sigmah.shared.report.model.layers.BubbleMapLayer;
import org.sigmah.shared.report.model.layers.IconMapLayer;
import org.sigmah.shared.report.model.layers.MapLayer;
import org.sigmah.shared.report.model.layers.PiechartMapLayer;

import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Widget;

public class AllLayerOptions extends ContentPanel implements HasValue<MapLayer> {
	private MapLayer selectedMapLayer;

	// Options for every supported MapLayer type
	private BubbleLayerOptions bubbleMapLayerOptions = new BubbleLayerOptions();
	private IconLayerOptions iconMapLayerOptions = new IconLayerOptions();
	private PiechartLayerOptions piechartMapLayerOptions;

	// Clustering options
	private ClusteringOptionsWidget clusteringOptions;

	// Have a nice box with optional collapsing around clustering/layeroptions
	private FieldSet fieldsetClustering = new FieldSet();
	private FieldSet fieldsetLayerSpecificOptions = new FieldSet();

	private Dispatcher service;

	public AllLayerOptions(Dispatcher service) {
		super();
	
		this.service = service;

		initializeComponent();
		
		createBubbleLayerOptionsWidget();
		createIconLayerOptionsWidget();
		createPiechartLayerOptionsWidget();

		createClusteringOptions(service);
	}

	private void createClusteringOptions(Dispatcher service) {
		clusteringOptions = new ClusteringOptionsWidget(service);
		clusteringOptions.addValueChangeHandler(new ValueChangeHandler<Clustering>() {
			@Override
			public void onValueChange(ValueChangeEvent<Clustering> event) {
				if (selectedMapLayer != null) {
					selectedMapLayer.setClustering(event.getValue());
					ValueChangeEvent.fire(AllLayerOptions.this, selectedMapLayer);
				}
			}
		});
		fieldsetClustering.add(clusteringOptions);
	}

	private void createPiechartLayerOptionsWidget() {
		piechartMapLayerOptions = new PiechartLayerOptions(service);
		piechartMapLayerOptions.addValueChangeHandler(new ValueChangeHandler<PiechartMapLayer>() {
			@Override
			public void onValueChange(ValueChangeEvent<PiechartMapLayer> event) {
				ValueChangeEvent.fire(AllLayerOptions.this, event.getValue());
			}
		});
	}

	private void createIconLayerOptionsWidget() {
		iconMapLayerOptions.addValueChangeHandler(new ValueChangeHandler<IconMapLayer>() {
			@Override
			public void onValueChange(ValueChangeEvent<IconMapLayer> event) {
				ValueChangeEvent.fire(AllLayerOptions.this, event.getValue());
			}
		});
	}

	private void createBubbleLayerOptionsWidget() {
		bubbleMapLayerOptions.addValueChangeHandler(new ValueChangeHandler<BubbleMapLayer>() {
			@Override
			public void onValueChange(ValueChangeEvent<BubbleMapLayer> event) {
				ValueChangeEvent.fire(AllLayerOptions.this, event.getValue());
			}
		});
	}

	private void initializeComponent() {
		setLayout(new FlowLayout());
		setHeaderVisible(false);
		
		setFieldsetHeadingToLayerName();
		fieldsetLayerSpecificOptions.setCollapsible(true);
		fieldsetLayerSpecificOptions.setAutoWidth(true);
		fieldsetLayerSpecificOptions.setLayout(new FlowLayout());
		fieldsetClustering.setLayout(new FlowLayout());
		add(fieldsetLayerSpecificOptions);

		fieldsetClustering.setHeading(I18N.CONSTANTS.clustering());
		fieldsetClustering.setCollapsible(true);
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
		LayerOptionsWidget layerOptionsWidget = fromLayer(mapLayer);
		
		if (mapLayer instanceof BubbleMapLayer) {
			bubbleMapLayerOptions.setValue((BubbleMapLayer) mapLayer);
		}
		if (mapLayer instanceof IconMapLayer) {
			iconMapLayerOptions.setValue((IconMapLayer) mapLayer);
		}
		if (mapLayer instanceof PiechartMapLayer) {
			piechartMapLayerOptions.setValue((PiechartMapLayer) mapLayer);
		}
		
		setActiveMapLayer(layerOptionsWidget);
		clusteringOptions.setValue(mapLayer.getClustering(), false);
	}
	
	private LayerOptionsWidget fromLayer(MapLayer mapLayer) {
		if (mapLayer instanceof BubbleMapLayer) {
			return bubbleMapLayerOptions;
		}
		if (mapLayer instanceof IconMapLayer) {
			return iconMapLayerOptions;
		}
		if (mapLayer instanceof PiechartMapLayer) {
			return piechartMapLayerOptions;
		}
		
		return null;
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

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		
		bubbleMapLayerOptions.setEnabled(enabled);
		piechartMapLayerOptions.setEnabled(enabled);
		iconMapLayerOptions.setEnabled(enabled);
		clusteringOptions.setEnabled(enabled);
	}
}
