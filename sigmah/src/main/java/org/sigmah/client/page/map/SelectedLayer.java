package org.sigmah.client.page.map;

import org.sigmah.client.EventBus;
import org.sigmah.shared.domain.Indicator;
import org.sigmah.shared.report.model.MapLayer;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Image;

/*
 * Displays layer name, icon, enable/disable button, remove button for a layer
 * selected by the user in
 * 
 * @author Ruud Poutsma
 */
public class SelectedLayer extends LayoutContainer {
	private SelectedLayerList parent; 

	private CheckBox checkboxEnable = new CheckBox();
	private Label labelName = new Label();
	private Image imageIcon;
	private Button buttonRemove = new Button();
	private BorderLayout layout;
	private HorizontalPanel restPanel = new HorizontalPanel(); 
	
	// The indicator represented in this widget
	private MapLayer indicatorLayer; 
	
	public static EventType RemoveLayer = new EventType();
	
	public SelectedLayer() {
		super();
		
		restPanel.add(imageIcon);
		restPanel.add(checkboxEnable);
		restPanel.add(labelName);
		
	}

	@Override
	protected void onRender(Element parent, int index) {
		super.onRender(parent, index);
		
		layout = new BorderLayout();
		add(buttonRemove, new BorderLayoutData(LayoutRegion.EAST, 16));
		add(restPanel, new BorderLayoutData(LayoutRegion.WEST));
		
		setLayout(layout);
	}

	/*
	 * Returns the indicator this control is managing
	 */
	private MapLayer getMapLayer() {
		return indicatorLayer;
	}

	/*
	 * Changes indicator this control manages to given one
	 */
	private void setMapLayer(MapLayer layer) {
		this.indicatorLayer = layer;
	}

}