package org.sigmah.client.page.map.layerOptions;

import java.util.ArrayList;
import java.util.List;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.page.common.widget.ColorField;
import org.sigmah.shared.command.GetSchema;
import org.sigmah.shared.domain.Indicator;
import org.sigmah.shared.dto.IndicatorDTO;
import org.sigmah.shared.dto.SchemaDTO;
import org.sigmah.shared.report.content.PieMapMarker.SliceValue;
import org.sigmah.shared.report.model.layers.PiechartMapLayer;

import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SliderEvent;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.ListView;
import com.extjs.gxt.ui.client.widget.Slider;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.SliderField;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayoutData;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayout.VBoxLayoutAlign;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;

/*
 * Displays a list of options to configure a PiechartMapLayer
 */
public class PiechartMapLayerOptions extends LayoutContainer implements LayerOptionsWidget<PiechartMapLayer> {
	private Dispatcher service;
	private PiechartMapLayer piechartMapLayer;
	private SchemaDTO schema;
	private Grid<IndicatorDTO> gridIndicatorOptions;
	private ListStore<IndicatorDTO> indicatorsStore = new ListStore<IndicatorDTO>();
	private SliderField sliderfieldMinSize;
	private SliderField sliderfieldMaxSize;
	private Slider sliderMinSize = new Slider();
	private Slider sliderMaxSize = new Slider();
	private FormData formData = new FormData("5");
	private FormPanel panel = new FormPanel();

	public PiechartMapLayerOptions(Dispatcher service) {
		super();
		
		this.service=service;
		
		initializeComponent();
		
		loadData();
		
		createMinMaxSliders();

		setupIndicatorOptionsGrid();
	}

	private void initializeComponent() {
		panel.setHeaderVisible(false);
		add(panel);
	}
	
	private void createMinMaxSliders() {
		sliderMinSize.setMinValue(1);
		sliderMinSize.setMaxValue(20);
		sliderMinSize.setIncrement(1);
		sliderMinSize.setDraggable(true);

		sliderMaxSize.setMinValue(1);
		sliderMaxSize.setMaxValue(20);
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
				piechartMapLayer.setMinRadius(sliderMinSize.getValue());
		}});

		sliderMaxSize.addListener(Events.Change, new Listener<SliderEvent>() {
			@Override
			public void handleEvent(SliderEvent be) {
				if (sliderMaxSize.getValue() < sliderMinSize.getValue()) {
					sliderMaxSize.setValue(sliderMinSize.getValue());
				}
				piechartMapLayer.setMaxRadius(sliderMaxSize.getValue());
		}});
	}

	private void setupIndicatorOptionsGrid() {
		List<ColumnConfig> columnConfigs = new ArrayList<ColumnConfig>();
		
		ColumnConfig columnName = new ColumnConfig();
	    columnName.setId("name");
	    columnName.setDataIndex("name");
	    columnName.setHeader("Indicators");
	    columnConfigs.add(columnName);

	    ColorField colorField = new ColorField();
	    
		ColumnConfig columnColor = new ColumnConfig();
	    columnColor.setId("color");
	    columnColor.setDataIndex("color");
	    columnColor.setHeader("Color");
	    columnColor.setWidth(30);
	    columnColor.setEditor(new CellEditor(colorField));
	    columnConfigs.add(columnColor);

		ColumnModel columnmodelIndicators = new ColumnModel(columnConfigs);

		gridIndicatorOptions = new Grid<IndicatorDTO>(indicatorsStore, columnmodelIndicators);
		gridIndicatorOptions.setBorders(false);
		gridIndicatorOptions.setAutoExpandColumn("name");
		gridIndicatorOptions.setAutoWidth(true);
		gridIndicatorOptions.setHeight(200);

		VBoxLayoutData vbld = new VBoxLayoutData();
		vbld.setFlex(1);
		
		panel.add(gridIndicatorOptions);
	}

	private void loadData() {
		service.execute(new GetSchema(), null, new AsyncCallback<SchemaDTO>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onSuccess(SchemaDTO result) {
				schema=result;
				populateColorPickerWidget();
			}
		});
	}

	private void populateColorPickerWidget() {
		if (piechartMapLayer !=null &&
				piechartMapLayer.getIndicatorIds() != null &&
				piechartMapLayer.getIndicatorIds().size() > 0) {
			for (Integer id : piechartMapLayer.getIndicatorIds()) {
				IndicatorDTO indicator = schema.getIndicatorById(id);
				indicatorsStore.add(indicator);
			}
		}
		layout(true);
	}

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
}
