package org.sigmah.client.page.map;

import java.util.ArrayList;
import java.util.List;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.page.common.filter.IndicatorTreePanel;
import org.sigmah.client.page.common.widget.ColorField;
import org.sigmah.shared.dto.IndicatorDTO;
import org.sigmah.shared.dto.IndicatorGroup;
import org.sigmah.shared.report.model.layers.BubbleMapLayer;
import org.sigmah.shared.report.model.layers.IconMapLayer;
import org.sigmah.shared.report.model.layers.MapLayer;
import org.sigmah.shared.report.model.layers.PiechartMapLayer;

import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.event.TreePanelEvent;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.Radio;
import com.extjs.gxt.ui.client.widget.form.RadioGroup;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayoutData;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayout.VBoxLayoutAlign;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Image;
import com.google.inject.Inject;

/*
 * Displays a modal window enabling the user to add a layer by selecting one or more indicators
 */
public class AddLayerDialog extends Window implements HasValue<MapLayer> {
    protected Dispatcher service;
    private boolean multiSelect = true;
    private MapLayer newLayer = null;

    // List of all indicators
	private IndicatorTreePanel treepanelIndicators;
	
	// List of selected indicators
	private Grid<IndicatorDTO> gridSelectedIndicators;
	private ListStore<IndicatorDTO> indicatorsStore = new ListStore<IndicatorDTO>();
	private Button buttonClearSelection = new Button();
    
    // Indicator options
	private Button buttonAddLayer = new Button();
	private Label labelCanSelectMultiple = new Label();
	private Image imageCanSelectMultiple = new Image();
    
	// Choice for type of layer
	private FieldSet fieldsetLayerType = new FieldSet();
	private RadioGroup radiogroupLayerType = new RadioGroup();
	private Radio radioProportionalCircle = new Radio();
	private Radio radioIcon = new Radio();
	private Radio radioPiechart = new Radio();
	
	@Inject
	public AddLayerDialog(Dispatcher service) {
		super();
		
		this.service=service;
		
		initializeComponent();
		
		createLayerOptions();
		createIndicatorTreePanel();
		createSelectedIndicatorsList();
		
		addButton(new Button("Add layer", new SelectionListener<ButtonEvent>() {  
	        @Override  
	        public void componentSelected(ButtonEvent ce) {  
	            addLayer();  
	        }  
        }));
		
		radioProportionalCircle.setValue(true);
	}

	private void createSelectedIndicatorsList() {
		List<ColumnConfig> columnConfigs = new ArrayList<ColumnConfig>();
		
		ColumnConfig column = new ColumnConfig();
	    column.setId("name");
	    column.setDataIndex("name");
	    column.setHeader("Selected indicators");
	    column.setWidth(700);  
	    columnConfigs.add(column);

		ColumnModel columnmodelIndicators = new ColumnModel(columnConfigs);

		gridSelectedIndicators = new Grid<IndicatorDTO>(indicatorsStore, columnmodelIndicators);
		gridSelectedIndicators.setBorders(false);
		
		VBoxLayoutData vbld = new VBoxLayoutData();
		vbld.setFlex(2);
		
		add(gridSelectedIndicators, vbld);
	}

	private void initializeComponent() {
		VBoxLayout layout = new VBoxLayout();
		layout.setVBoxLayoutAlign(VBoxLayoutAlign.STRETCH);
		setLayout(layout);

		setSize(800, 600);
		setPlain(true);
		setHeading("Add layer");
		setModal(true);
		setBlinkModal(true);
	}
	
	private void createLayerOptions() {
		HorizontalPanel radioPanel = new HorizontalPanel();
		HorizontalPanel muliselectPanel = new HorizontalPanel();
		
		fieldsetLayerType.setHeading("Type of layer");
		fieldsetLayerType.setLayout(new RowLayout(Orientation.VERTICAL));
		radioProportionalCircle.setBoxLabel(I18N.CONSTANTS.proportionalCircle());
		radioIcon.setBoxLabel(I18N.CONSTANTS.icon());
		radioPiechart.setBoxLabel(I18N.CONSTANTS.pieChart());
		
		radiogroupLayerType.add(radioPiechart);
		radiogroupLayerType.add(radioProportionalCircle);
		radiogroupLayerType.add(radioIcon);
		
		radioPanel.add(radioProportionalCircle);
		radioPanel.add(radioIcon);
		radioPanel.add(radioPiechart);
		
		imageCanSelectMultiple.setWidth("32px");
		muliselectPanel.add(imageCanSelectMultiple);
		muliselectPanel.add(labelCanSelectMultiple);
		
		fieldsetLayerType.add(radioPanel);
		fieldsetLayerType.add(muliselectPanel);
		
		// Let the user know whether or not he can select multiple indicators for the layer
		// he wants to add to the map
		radiogroupLayerType.addListener(Events.Change, new Listener<FieldEvent>() {
			@Override
			public void handleEvent(FieldEvent be) {
				if ((Boolean)be.getValue())
				{
					// Grab the newly selected radio from the group, cache its MapLayer
					MapLayer layer = fromRadio(((RadioGroup) be.getSource()).getValue());
					multiSelect = layer.supportsMultipleIndicators();
					setCanMultipleSelect();
				}
			}
		});
		
		add(fieldsetLayerType);
	}

	protected void addLayer() {
		if (indicatorsStore.getModels().size() > 0) {
			Radio selected = radiogroupLayerType.getValue();
			newLayer = fromRadio(selected);
			List<Integer> indicators = new ArrayList<Integer>();
			for (IndicatorDTO indicator : indicatorsStore.getModels()) {
				newLayer.getIndicatorIds().add(indicator.getId());
			}
			clearSelection();
			hide();
			ValueChangeEvent.fire(this, newLayer);
		}
	}

	private void clearSelection() {
		indicatorsStore.removeAll();
	}
	
	private MapLayer fromRadio(Radio radio) {
		if (radio.equals(radioIcon)) {
			return new IconMapLayer();
		}
		if (radio.equals(radioPiechart)) {
			return new PiechartMapLayer();
		}
		if (radio.equals(radioProportionalCircle)) {
			return new BubbleMapLayer();
		}
		
		return null;
	}

	private void createIndicatorTreePanel() {
		treepanelIndicators = new IndicatorTreePanel(service, false);

		// TODO: GXT V3+: add icon
		//indicatorTreePanel.setIcon(icons.indicator());
		
		treepanelIndicators.setHeaderVisible(false);
		treepanelIndicators.addCheckChangedListener(new Listener<TreePanelEvent>(){
			@Override
			public void handleEvent(TreePanelEvent be) {
				if (!multiSelect) {
					clearSelection();
				}
				
				if (be.isChecked()) {
					if (be.getItem() instanceof IndicatorGroup && multiSelect) {
						IndicatorGroup group = (IndicatorGroup) be.getItem();
						for (IndicatorDTO indicator : group.getIndicators()) {
							indicatorsStore.add(indicator);
						}
					}
					if (be.getItem() instanceof IndicatorDTO) {
						indicatorsStore.add((IndicatorDTO) be.getItem());
					}
				} else {
					if (be.getItem() instanceof IndicatorGroup) {
						IndicatorGroup group = (IndicatorGroup) be.getItem();
						for (IndicatorDTO indicator : group.getIndicators()) {
							indicatorsStore.remove(indicator);
						}
					}
					if (be.getItem() instanceof IndicatorDTO) {
						indicatorsStore.remove((IndicatorDTO) be.getItem());
					}
				}
					
				indicatorsStore.commitChanges();
			}
		});
		
		VBoxLayoutData vbld = new VBoxLayoutData();
		vbld.setFlex(10);
		add(treepanelIndicators, vbld);
    }
	
	private void setCanMultipleSelect() {
		if (multiSelect) {
			labelCanSelectMultiple.setText("You can include multiple indicators on this layertype");
			imageCanSelectMultiple.setResource(MapResources.INSTANCE.multiSelect());
		} else {
			labelCanSelectMultiple.setText("One indicator can be selected for this layertype");
			imageCanSelectMultiple.setResource(MapResources.INSTANCE.singleSelect());
		}
		
		treepanelIndicators.setMultipleSelection(multiSelect);
	}

	@Override
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<MapLayer> handler) {
		return addHandler(handler, ValueChangeEvent.getType());

	}

	@Override
	public MapLayer getValue() {
		return newLayer;
	}

	@Override
	public void setValue(MapLayer value) {
		// Do nothing
	}

	@Override
	public void setValue(MapLayer value, boolean fireEvents) {
		// do nothing
	}

	@Override
	public void hide() {
		super.hide();
		clearSelection();
	}
}
