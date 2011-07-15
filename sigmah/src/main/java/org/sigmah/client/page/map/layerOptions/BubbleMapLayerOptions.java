package org.sigmah.client.page.map.layerOptions;

import org.sigmah.client.page.common.widget.ColorField;
import org.sigmah.shared.report.model.layers.BubbleMapLayer;
import org.sigmah.shared.report.model.layers.MapLayer;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ColorPaletteEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SliderEvent;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Slider;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.SliderField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Image;

/*
 * Displays a list of options the user can choose to configure a BubbleMapLayer
 * TODO: replace the two min/max sliders with a RangeSlider (Slider with 2 knobs)
 */
public class BubbleMapLayerOptions extends LayoutContainer implements LayerOptionsWidget<BubbleMapLayer> {
	private BubbleMapLayer bubbleMapLayer;
	private ColorField colorPicker = new ColorField();
	private FormPanel panel = new FormPanel();
	private SliderField sliderfieldMinSize;
	private SliderField sliderfieldMaxSize;
	private Slider sliderMinSize = new Slider();
	private Slider sliderMaxSize = new Slider();
	private FormData formData = new FormData("5");
	
	// TODO: replace images by a dynamic canvas element rendering the desired min/maxsize
	private Image imageMinSize = new Image();
	private Image imageMaxSize = new Image();

	public BubbleMapLayerOptions() {
		super();
		
		createColorPicker();
		createMinMaxSliders();
		
		panel.setHeaderVisible(false);
		add(panel);
	}

	private void createColorPicker() {
		// Set the selected color to the maplayer
		colorPicker.addListener(Events.Select, new Listener<FieldEvent>() {
			@Override
			public void handleEvent(FieldEvent be) {
				bubbleMapLayer.setLabelColor(colorPicker.getIntValue());
				ValueChangeEvent.fire(BubbleMapLayerOptions.this, bubbleMapLayer);
		}});

		colorPicker.setFieldLabel("Color");
		
		panel.add(colorPicker, formData);
	}

	private void createMinMaxSliders() {
		sliderMinSize.setMinValue(16);
		sliderMinSize.setMaxValue(48);
		sliderMinSize.setIncrement(1);
		sliderMinSize.setDraggable(true);

		sliderMaxSize.setMinValue(16);
		sliderMaxSize.setMaxValue(48);
		sliderMaxSize.setIncrement(1);
		sliderMaxSize.setDraggable(true);
		
		sliderfieldMinSize = new SliderField(sliderMinSize);
		sliderfieldMinSize.setFieldLabel("Minimum");
		sliderfieldMaxSize = new SliderField(sliderMaxSize);
		sliderfieldMaxSize.setFieldLabel("Maximum");
		panel.add(sliderfieldMinSize, formData);
		panel.add(sliderfieldMaxSize, formData);
		
		// Ensure min can't be more then max, and max can't be less then min
		sliderMinSize.addListener(Events.Change, new Listener<SliderEvent>() {
			@Override
			public void handleEvent(SliderEvent be) {
				if (sliderMinSize.getValue() > sliderMaxSize.getValue()) {
					sliderMinSize.setValue(sliderMaxSize.getValue());
				}
				bubbleMapLayer.setMinRadius(sliderMinSize.getValue());
				ValueChangeEvent.fire(BubbleMapLayerOptions.this, bubbleMapLayer);
		}});

		sliderMaxSize.addListener(Events.Change, new Listener<SliderEvent>() {
			@Override
			public void handleEvent(SliderEvent be) {
				if (sliderMaxSize.getValue() < sliderMinSize.getValue()) {
					sliderMaxSize.setValue(sliderMinSize.getValue());
				}
				bubbleMapLayer.setMinRadius(sliderMinSize.getValue());
				ValueChangeEvent.fire(BubbleMapLayerOptions.this, bubbleMapLayer);
		}});
	}

	@Override
	public BubbleMapLayer getValue() {
		return bubbleMapLayer;
	}

	@Override
	public void setValue(BubbleMapLayer value) {
		this.bubbleMapLayer=value;
	}

	// TODO: fireevent
	@Override
	public void setValue(BubbleMapLayer value, boolean fireEvents) {
		setValue(value);
	}

	@Override
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<BubbleMapLayer> handler) {
		return this.addHandler(handler, ValueChangeEvent.getType());
	}
}
