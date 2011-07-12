package org.sigmah.client.page.map.layerOptions;

import org.sigmah.client.page.common.widget.ColorField;
import org.sigmah.shared.report.model.layers.BubbleMapLayer;
import org.sigmah.shared.report.model.layers.MapLayer;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SliderEvent;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Slider;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.Element;

/*
 * Displays a list of options the user can choose to configure a BubbleMapLayer
 * TODO: replace the two min/max sliders with a RangeSlider (Slider with 2 knobs)
 */
public class BubbleMapLayerOptions extends ContentPanel implements LayerOptionsWidget {
	private BubbleMapLayer bubbleMapLayer;
	private ColorField colorPicker = new ColorField();
	private FormPanel panel = new FormPanel();
	private Slider sliderMinSize = new Slider();
	private Slider sliderMaxSize = new Slider();
	
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
		
		initializeComponent();

		createColorPicker();
		createMinMaxSliders();
		
	}

	private void createColorPicker() {
		add(colorPicker);
		colorPicker.setFieldLabel("woeo");

		// Set the selected color to the maplayer
		colorPicker.addListener(Events.Select, new Listener<FieldEvent>() {
			@Override
			public void handleEvent(FieldEvent be) {
				bubbleMapLayer.setLabelColor(colorPicker.getIntValue());
		}});
	}

	private void initializeComponent() {
		setAnimCollapse(false);
		setHeading("Bubbles");
	}

	private void createMinMaxSliders() {
		sliderMinSize.setMinValue(1);
		sliderMinSize.setMaxValue(20);
		add(sliderMinSize);

		sliderMaxSize.setMinValue(1);
		sliderMaxSize.setMaxValue(20);
		add(sliderMaxSize);
		
		sliderMinSize.addListener(Events.Change, new Listener<SliderEvent>() {
			@Override
			public void handleEvent(SliderEvent be) {
				checkMaxIsMoreThenMin();
		}});

		sliderMaxSize.addListener(Events.Change, new Listener<SliderEvent>() {
			@Override
			public void handleEvent(SliderEvent be) {
				checkMaxIsMoreThenMin();
		}});
	}

	/*
	 * Ensures the value of the MaxSize slider does not go below the value of MinSize slider
	 */
	protected void checkMaxIsMoreThenMin() {
		if (sliderMaxSize.getValue() < sliderMinSize.getValue()) {
			sliderMaxSize.setValue(sliderMinSize.getValue());
		}
	}
}
