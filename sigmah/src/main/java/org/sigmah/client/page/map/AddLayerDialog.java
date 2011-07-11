package org.sigmah.client.page.map;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.page.common.filter.IndicatorTreePanel;
import org.sigmah.client.page.common.widget.ColorField;
import org.sigmah.shared.report.model.layers.BubbleMapLayer;
import org.sigmah.shared.report.model.layers.IconMapLayer;
import org.sigmah.shared.report.model.layers.MapLayer;
import org.sigmah.shared.report.model.layers.PiechartMapLayer;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.Radio;
import com.extjs.gxt.ui.client.widget.form.RadioGroup;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Image;
import com.google.inject.Inject;

/*
 * Displays a modal window enabling the user to add a layer by selecting one or more indicators
 */
public class AddLayerDialog extends Window {
    protected Dispatcher service;

    // Indicators list
    private FieldSet fieldsetTreePanel = new FieldSet();
	private IndicatorTreePanel treepanelIndicators;
    
    // Indicator options
    private FieldSet fieldsetIndicatorOptions = new FieldSet();
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
		
		setSize(500, 500);
		setPlain(true);
		setHeading("Add layer");
		setLayout(new FlowLayout());
		setModal(true);
		setBlinkModal(true);
		
		createIndicatorOptions();
		createLayerOptions();
		createIndicatorTreePanel();
		createCloseButton();
	}
	
	private void createIndicatorOptions() {
		fieldsetLayerType.setHeading("Type of layer");
		radioProportionalCircle.setBoxLabel(I18N.CONSTANTS.proportionalCircle());
		radioIcon.setBoxLabel(I18N.CONSTANTS.icon());
		radioPiechart.setBoxLabel(I18N.CONSTANTS.pieChart());
		
		radiogroupLayerType.add(radioPiechart);
		radiogroupLayerType.add(radioProportionalCircle);
		radiogroupLayerType.add(radioIcon);
		
		fieldsetLayerType.add(radioProportionalCircle);
		fieldsetLayerType.add(radioIcon);
		fieldsetLayerType.add(radioPiechart);
		
		// Let the user know whether or not he can select multiple indicators for the layer
		// he wants to add to the map
		radiogroupLayerType.addListener(Events.Change, new Listener<FieldEvent>() {
			@Override
			public void handleEvent(FieldEvent be) {
				if ((Boolean)be.getValue())
				{
					MapLayer layer = fromRadio((Radio) be.getSource());
					setCanMultipleSelect(layer.supportsMultipleIndicators());
				}
			}
		});
		
		add(fieldsetLayerType);
	}

	private void createLayerOptions() {
		fieldsetIndicatorOptions.setHeading("Indicator options");
	
		addButton(new Button("Add layer", new SelectionListener<ButtonEvent>() {  
	        @Override  
	        public void componentSelected(ButtonEvent ce) {  
	            addLayer();  
	        }  
        }));
		
		add(fieldsetIndicatorOptions);
	}

	protected void addLayer() {
		Radio selected = radiogroupLayerType.getValue();
		MapLayer newLayer = fromRadio(selected);
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
		//indicatorTreePanel.setIcon(icons.indicator());
		treepanelIndicators.setHeaderVisible(false);
		fieldsetTreePanel.add(treepanelIndicators);
		fieldsetTreePanel.setTitle("Indicators");
		add(fieldsetTreePanel);
    }

	private void createCloseButton() {
	    addButton(new Button("Close", new SelectionListener<ButtonEvent>() {  
	        @Override  
	        public void componentSelected(ButtonEvent ce) {  
	            hide();  
	        }  
        }));
	}
	
	private void setCanMultipleSelect(boolean multipleSelect) {
		if (multipleSelect) {
			labelCanSelectMultiple.setText("You can include multiple indicators on this layertype");
			changeSelectionIcon(MapResources.INSTANCE.multiSelect());
		} else {
			labelCanSelectMultiple.setText("One indicator can be selected for this layertype");
			changeSelectionIcon(MapResources.INSTANCE.singleSelect());
		}
	}
	
	private void changeSelectionIcon(ImageResource icon) {
		// Change the icon in some panel
	}
}
