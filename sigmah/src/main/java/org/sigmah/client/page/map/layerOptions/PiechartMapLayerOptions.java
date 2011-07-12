package org.sigmah.client.page.map.layerOptions;

import java.util.ArrayList;
import java.util.List;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.page.common.widget.ColorField;
import org.sigmah.shared.command.GetSchema;
import org.sigmah.shared.domain.Indicator;
import org.sigmah.shared.dto.IndicatorDTO;
import org.sigmah.shared.report.content.PieMapMarker.Slice;
import org.sigmah.shared.report.model.layers.PiechartMapLayer;

import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.ListView;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;

/*
 * Displays a list of options to configure a PiechartMapLayer
 */
public class PiechartMapLayerOptions extends ContentPanel implements LayerOptionsWidget<PiechartMapLayer> {
	private Dispatcher service;
	private PiechartMapLayer piechartMapLayer;
	private ListView listviewPiechartPies;
	private List<SliceEditWidget> indicatorColorPickers = new ArrayList<SliceEditWidget>();
	private ContentPanel contentpanelColorPickers = new ContentPanel();

	public PiechartMapLayerOptions() {
		super();
		
		initializeComponent();
		add(contentpanelColorPickers);
	}


	private void initializeComponent() {
		setAnimCollapse(false);
		setHeading("Piechart");
	}

	private void populateColorPickerWidget() {
		contentpanelColorPickers.removeAll();
		
		for (Slice slice : piechartMapLayer.getSlices()) {
			contentpanelColorPickers.add(new SliceEditWidget(slice));
		}
	};

	@Override
	public PiechartMapLayer getValue() {
		return piechartMapLayer;
	}

	@Override
	public void setValue(PiechartMapLayer value) {
		this.piechartMapLayer = value;
		populateColorPickerWidget();
	}

	@Override
	public void setValue(PiechartMapLayer value, boolean fireEvents) {
		setValue(value);
	}

	@Override
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<PiechartMapLayer> handler) {
		return this.addHandler(handler, ValueChangeEvent.getType());
	}
	
	/* 
	 * Displays name of an indicator and a colorpicker to configure the color used per indicator
	 */
	private class SliceEditWidget extends ContentPanel {
		private Slice slice;
		private ColorField colorPicker;
		private Label labelName;
		
		public SliceEditWidget(Slice slice) {
			this.slice=slice;
			
			add(labelName);
			colorPicker.setWidth("32px");
			add(colorPicker);
			
			colorPicker.addListener(Events.Change, new Listener<FieldEvent>(){
				@Override
				public void handleEvent(FieldEvent be) {
					//change color
			}});
		}

		public Slice getIndicator() {
			return slice;
		}
	}
}
