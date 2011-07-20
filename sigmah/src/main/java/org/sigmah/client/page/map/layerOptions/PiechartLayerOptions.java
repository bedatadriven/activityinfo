package org.sigmah.client.page.map.layerOptions;

import java.util.ArrayList;
import java.util.List;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.page.common.widget.ColorField;
import org.sigmah.shared.command.GetSchema;
import org.sigmah.shared.dto.SchemaDTO;
import org.sigmah.shared.report.model.layers.PiechartMapLayer;
import org.sigmah.shared.report.model.layers.PiechartMapLayer.Slice;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SliderEvent;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Slider;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.SliderField;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.extjs.gxt.ui.client.widget.grid.CellSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayoutData;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;

/*
 * Displays a list of options to configure a PiechartMapLayer
 */
public class PiechartLayerOptions extends LayoutContainer implements LayerOptionsWidget<PiechartMapLayer> {
	private Dispatcher service;
	private PiechartMapLayer piechartMapLayer;
	private SchemaDTO schema;
	private Grid<NamedSlice> gridIndicatorOptions;
	private ListStore<NamedSlice> indicatorsStore = new ListStore<NamedSlice>();
	private SliderField sliderfieldMinSize;
	private SliderField sliderfieldMaxSize;
	private Slider sliderMinSize = new Slider();
	private Slider sliderMaxSize = new Slider();
	private Timer timerMinSlider;
	private Timer timerMaxSlider;
	private FormData formData = new FormData("5");
	private FormPanel panel = new FormPanel();

	public PiechartLayerOptions(Dispatcher service) {
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
	
	private void setSliderDefaults(Slider slider) {
		slider.setMinValue(1);
		slider.setMaxValue(60);
		slider.setIncrement(1);
		slider.setDraggable(true);
		slider.setAutoWidth(true);
	}
	
	private void createMinMaxSliders() {
		setSliderDefaults(sliderMinSize);
		setSliderDefaults(sliderMaxSize);

		sliderMinSize.setValue(16);
		sliderMaxSize.setValue(48);
		
		sliderfieldMinSize = new SliderField(sliderMinSize);
		sliderfieldMinSize.setFieldLabel(I18N.CONSTANTS.radiusMinimum());
		sliderfieldMaxSize = new SliderField(sliderMaxSize);
		sliderfieldMaxSize.setFieldLabel(I18N.CONSTANTS.radiusMaximum());
		panel.add(sliderfieldMinSize, formData);
		panel.add(sliderfieldMaxSize, formData);
		
		// Ensure min can't be more then max, and max can't be less then min
		sliderMinSize.addListener(Events.Change, new Listener<SliderEvent>() {
			@Override
			public void handleEvent(SliderEvent be) {
				timerMinSlider.cancel();
				timerMinSlider.schedule(250);
		}});

		sliderMaxSize.addListener(Events.Change, new Listener<SliderEvent>() {
			@Override
			public void handleEvent(SliderEvent be) {
				timerMinSlider.cancel();
				timerMaxSlider.schedule(250);
		}});
		timerMinSlider = new Timer() {
			@Override
			public void run() {
				if (sliderMinSize.getValue() > sliderMaxSize.getValue()) {
					sliderMinSize.setValue(sliderMaxSize.getValue());
				}
				piechartMapLayer.setMinRadius(sliderMinSize.getValue());
				ValueChangeEvent.fire(PiechartLayerOptions.this, piechartMapLayer);
			}
		};
		timerMaxSlider = new Timer() {
			@Override
			public void run() {
				if (sliderMaxSize.getValue() < sliderMinSize.getValue()) {
					sliderMaxSize.setValue(sliderMinSize.getValue());
				}
				piechartMapLayer.setMaxRadius(sliderMaxSize.getValue());
				ValueChangeEvent.fire(PiechartLayerOptions.this, piechartMapLayer);
			}
		};
	}

	private void setupIndicatorOptionsGrid() {
		List<ColumnConfig> columnConfigs = new ArrayList<ColumnConfig>();
		
		ColumnConfig columnName = new ColumnConfig();
	    columnName.setId("name");
	    columnName.setDataIndex("name");
	    columnName.setHeader(I18N.CONSTANTS.indicators());
	    columnConfigs.add(columnName);
	    ColorField colorField = new ColorField();
	    
		ColumnConfig columnColor = new ColumnConfig();
	    columnColor.setId("color");
	    columnColor.setDataIndex("color");
	    columnColor.setHeader(I18N.CONSTANTS.color());
	    columnColor.setWidth(30);
	    
	    CellEditor colorEditor = new CellEditor(colorField) {
			@Override
			public Object postProcessValue(Object value) {
				return super.postProcessValue(value);
			}

			@Override
			public Object preProcessValue(Object value) {
				return super.preProcessValue(value);
			}
	    };
	    
	    columnColor.setEditor(colorEditor);
	    columnConfigs.add(columnColor);

		ColumnModel columnmodelIndicators = new ColumnModel(columnConfigs);

		gridIndicatorOptions = new Grid<NamedSlice>(indicatorsStore, columnmodelIndicators);
		gridIndicatorOptions.setBorders(false);
		gridIndicatorOptions.setAutoExpandColumn("name");
		gridIndicatorOptions.setAutoWidth(true);
		gridIndicatorOptions.setHeight(200);
		gridIndicatorOptions.setSelectionModel(new CellSelectionModel<PiechartLayerOptions.NamedSlice>());

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
			for (Slice slice : piechartMapLayer.getSlices()) {
				String name = schema.getIndicatorById(slice.getIndicatorId()).getName();
				indicatorsStore.add(new NamedSlice(slice.getColor(), slice.getIndicatorId(), name));
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
		updateUI();
	}

	private void updateUI() {
		populateColorPickerWidget();
		sliderMinSize.setValue(piechartMapLayer.getMinRadius(), true);
		sliderMaxSize.setValue(piechartMapLayer.getMaxRadius(), true);
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
	
	public static class NamedSlice extends BaseModelData {
		public NamedSlice() {
		}

		public NamedSlice(int color, int indicatorId, String name) {
			super();
			
			setColor(color);
			setIndicatorId(indicatorId);
			setName(name);
		}
		
		public int getColor() {
			return (Integer)get("color");
		}
		
		public void setColor(int color) {
			set("color", color);
		}
		
		public int getIndicatorId() {
			return (Integer)get("indicatorId");
		}
		
		public void setIndicatorId(int indicatorId) {
			set("indicatorId" , indicatorId);
		}

		public String getName() {
			return get("name");
		}

		public void setName(String name) {
			set("name" , name);
		}
	}
}
