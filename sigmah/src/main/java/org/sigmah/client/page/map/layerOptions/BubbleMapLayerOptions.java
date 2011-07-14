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
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
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
	private Slider sliderMinSize = new Slider();
	private Slider sliderMaxSize = new Slider();
	
	// TODO: replace images by a dynamic canvas element rendering the desired min/maxsize
	private Image imageMinSize = new Image();
	private Image imageMaxSize = new Image();

	public BubbleMapLayerOptions() {
		super();
		
		createColorPicker();
		createMinMaxSliders();
	}

	private void createColorPicker() {
		add(colorPicker);

		// Set the selected color to the maplayer
		colorPicker.addListener(Events.Change, new Listener<FieldEvent>() {
			@Override
			public void handleEvent(FieldEvent be) {
				bubbleMapLayer.setLabelColor(colorPicker.getIntValue());
				ValueChangeEvent.fire(BubbleMapLayerOptions.this, bubbleMapLayer);
		}});

		// Set the selected color to the maplayer
		colorPicker.addListener(Events.Select, new Listener<FieldEvent>() {
			@Override
			public void handleEvent(FieldEvent be) {
				bubbleMapLayer.setLabelColor(colorPicker.getIntValue());
				ValueChangeEvent.fire(BubbleMapLayerOptions.this, bubbleMapLayer);
		}});
	}

	private void createMinMaxSliders() {
		sliderMinSize.setMinValue(1);
		sliderMinSize.setMaxValue(20);
		sliderMinSize.setIncrement(1);
		sliderMinSize.setDraggable(true);
		sliderMinSize.setClickToChange(false);
		add(sliderMinSize);


		sliderMaxSize.setMinValue(1);
		sliderMaxSize.setMaxValue(20);
		sliderMaxSize.setIncrement(1);
		sliderMaxSize.setDraggable(true);
		sliderMaxSize.setClickToChange(false);
		add(sliderMaxSize);
		
//		// Ensure min can't be more then max, and max can't be less then min
//		sliderMinSize.addListener(Events.BeforeChange, new Listener<SliderEvent>() {
//			@Override
//			public void handleEvent(SliderEvent be) {
//				be.setCancelled(sliderMinSize.getValue() > sliderMaxSize.getValue());
//		}});
//
//		sliderMaxSize.addListener(Events.BeforeChange, new Listener<SliderEvent>() {
//			@Override
//			public void handleEvent(SliderEvent be) {
//				be.setCancelled(sliderMaxSize.getValue() < sliderMinSize.getValue());
//		}});
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
