package org.activityinfo.client.report.editor.map;

import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.filter.IndicatorTreePanel;
import org.activityinfo.client.i18n.I18N;
import org.activityinfo.shared.dto.IndicatorDTO;
import org.activityinfo.shared.dto.IndicatorGroup;
import org.activityinfo.shared.report.model.layers.BubbleMapLayer;
import org.activityinfo.shared.report.model.layers.IconMapLayer;
import org.activityinfo.shared.report.model.layers.MapLayer;
import org.activityinfo.shared.report.model.layers.PiechartMapLayer;

import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.event.TreePanelEvent;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.form.Radio;
import com.extjs.gxt.ui.client.widget.form.RadioGroup;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayout.VBoxLayoutAlign;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayoutData;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Image;
import com.google.inject.Inject;

/**
 * Displays a modal window enabling the user to add a layer by selecting one or more indicators
 */
public final class AddLayerDialog extends Window implements HasValue<MapLayer> {
    private Dispatcher service;
    private boolean multiSelect = true;
    private MapLayer newLayer = null;

    // List of all indicators
	private IndicatorTreePanel treepanelIndicators;
	
	// List of selected indicators
	private ListStore<IndicatorDTO> indicatorsStore = new ListStore<IndicatorDTO>();
    
    // Indicator options
	private Button buttonAddLayer = new Button();
	private LabelField labelCanSelectMultiple = new LabelField();
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
		
		addButton(new Button(I18N.CONSTANTS.addLayer(), new SelectionListener<ButtonEvent>() {  
	        @Override  
	        public void componentSelected(ButtonEvent ce) {  
	        	addLayer();
	        }  
        }));
		
		radioProportionalCircle.setValue(true);
	}

	private void initializeComponent() {
		VBoxLayout layout = new VBoxLayout();
		layout.setVBoxLayoutAlign(VBoxLayoutAlign.STRETCH);
		setLayout(layout);
		
		setSize((int)Math.min(com.google.gwt.user.client.Window.getClientWidth() * 0.8, 800d),
			    (int)Math.min(com.google.gwt.user.client.Window.getClientWidth() * 0.8, 600d));
		setPlain(true);
		setHeading("Add layer");
		setModal(true);
	}
	
	private void createLayerOptions() {
		HorizontalPanel radioPanel = new HorizontalPanel();
		HorizontalPanel muliselectPanel = new HorizontalPanel();
		
		fieldsetLayerType.setHeading(I18N.CONSTANTS.typeOfLayer());
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
	
	/*
	 * Makes up a name for given layer, using following pattern:
	 * -One indicator -> use indicator name
	 * -One IndicatorGroup -> use indicatorgroups' name
	 * -Multiple indicators, multiple indicator groups -> "N indicators"
	 * 
	 * TODO: implement indicatorgroup detection
	 */
	private void createLayerName(MapLayer mapLayer) {
		if (mapLayer.getIndicatorIds().size() == 1) {
			mapLayer.setName(indicatorsStore.getModels().get(0).getName());
			return;
		}
		
		if (mapLayer.getIndicatorIds().size() > 1) {
			mapLayer.setName(mapLayer.getIndicatorIds().size() + " " + I18N.CONSTANTS.indicators());
			return;
		}
	}

	protected void addLayer() {
		if (indicatorsStore.getModels().size() > 0) {
			Radio selected = radiogroupLayerType.getValue();
			
			// Create the new layer based on the selected type
			newLayer = fromRadio(selected);
			for (IndicatorDTO indicator : indicatorsStore.getModels()) {
				newLayer.addIndicatorId(indicator.getId());
			}
			createLayerName(newLayer);
			
			// Set UI back to defaults
			clearSelection();
			hide();
			ValueChangeEvent.fire(this, newLayer);
		}
	}

	/*
	 * Clears list of selected indicators
	 */
	private void clearSelection() {
		indicatorsStore.removeAll();
		treepanelIndicators.clearSelection();
		buttonAddLayer.setEnabled(false);
	}
	
	/*
	 * Factory method for a MapLayer based on given Radio widget
	 */
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
		treepanelIndicators.setLeafCheckableOnly();
		treepanelIndicators.addCheckChangedListener(new Listener<TreePanelEvent>(){
			@Override
			public void handleEvent(TreePanelEvent be) {
				if (isSingleSelect() && be.isChecked()) {
                    for (IndicatorDTO indicator : treepanelIndicators.getSelection()) {
                        if (!indicator.equals(be.getItem())) {
                        	treepanelIndicators.setChecked(indicator, false);
                        }
                    }
				}
				
				if (be.getItem() instanceof IndicatorGroup && multiSelect) {
					IndicatorGroup indicatorGroup = (IndicatorGroup)be.getItem();
					if (be.isChecked()) {
						addIndicatorGroupToStore((IndicatorGroup) be.getItem());
					} else {
						removeIndicatorGroupFromStore(indicatorGroup);
					}
					buttonAddLayer.setEnabled(hasSelectedItems());
				}
				if (be.getItem() instanceof IndicatorDTO) {
					IndicatorDTO indicator = (IndicatorDTO)be.getItem();
					if (be.isChecked()) {
						addIndicatorToStoreIfNotPresent(indicator);
					} else {
						removeIndicatorFromStore(indicator);
					}
					buttonAddLayer.setEnabled(hasSelectedItems());
				}
			}

			private void removeIndicatorGroupFromStore(IndicatorGroup indicatorGroup) {
				for (IndicatorDTO indicator : indicatorGroup.getIndicators()) {
					removeIndicatorFromStore(indicator);
				}
			}

		});
		
		VBoxLayoutData vbld = new VBoxLayoutData();
		vbld.setFlex(10);
		add(treepanelIndicators, vbld);
    }
		
	private void removeIndicatorFromStore(IndicatorDTO item) {
		if (indicatorsStore.contains(item)) {
			indicatorsStore.remove(item);
		}
	}

	private boolean hasSelectedItems() {
		return indicatorsStore.getModels().size() > 0;
	}
	
	private void addIndicatorGroupToStore(IndicatorGroup group) {
		for (IndicatorDTO indicator : group.getIndicators()) {
			addIndicatorToStoreIfNotPresent(indicator);
		}
	}
	
	private void addIndicatorToStoreIfNotPresent(IndicatorDTO indicator) {
		if (!indicatorsStore.contains(indicator)) {
			indicatorsStore.add(indicator);
		}
	}
	
	private boolean isSingleSelect() {
		return !multiSelect;
	}
	
	private void setCanMultipleSelect() {
		if (multiSelect) {
			labelCanSelectMultiple.setText(I18N.CONSTANTS.canIncludeMultipleIndicators());
			imageCanSelectMultiple.setResource(MapResources.INSTANCE.multiSelect());
		} else {
			labelCanSelectMultiple.setText(I18N.CONSTANTS.canIncludeSingleIndicator());
			imageCanSelectMultiple.setResource(MapResources.INSTANCE.singleSelect());
			clearSelectionIfMoreThenOneItem();
		}
		
		treepanelIndicators.setMultipleSelection(multiSelect);
	}

	private void clearSelectionIfMoreThenOneItem() {
		if (indicatorsStore.getModels().size() > 1) {
			clearSelection();
		}
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
