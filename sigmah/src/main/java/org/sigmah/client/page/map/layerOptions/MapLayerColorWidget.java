package org.sigmah.client.page.map.layerOptions;

import org.sigmah.client.page.common.widget.ColorField;
import org.sigmah.shared.domain.Indicator;

import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.Label;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasValue;

public class MapLayerColorWidget extends HorizontalPanel implements HasValue<Indicator> {
	private Indicator indicator;
	private ColorField colorPicker = new ColorField();
	private Label labelName = new Label();
	
	public MapLayerColorWidget() {
		super();
		
		add(labelName);
		add(colorPicker);
	}
	
	
	@Override
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<Indicator> handler) {
	
		// TODO Auto-generated method stub
		return null;
	
	
	
	}

	@Override
	public Indicator getValue() {
		return indicator;
	}

	@Override
	public void setValue(Indicator value) {
		setValue(value, false);
	}

	@Override
	public void setValue(Indicator value, boolean fireEvents) {
		this.indicator=value;
	}

	private void updateUI() {
		labelName.setText(indicator.getName());
		//colorPicker.setValue(indicator.get)
	}
	
	
}
