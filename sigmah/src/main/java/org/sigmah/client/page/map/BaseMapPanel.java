package org.sigmah.client.page.map;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.page.map.mapOptions.BaseMapDialog;
import org.sigmah.client.page.map.mapOptions.BaseMapDialog.Callback;
import org.sigmah.shared.command.GetBaseMaps;
import org.sigmah.shared.command.result.BaseMapResult;
import org.sigmah.shared.dto.element.handler.ValueEvent;
import org.sigmah.shared.map.GoogleBaseMap;
import org.sigmah.shared.map.TileBaseMap;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout.HBoxLayoutAlign;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayoutData;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Image;

/**
 * Displays the current basemap id with a button to change it.
 * 
 */
public class BaseMapPanel extends ContentPanel implements HasValue<String> {
	
	private final Dispatcher dispatcher;
	private String value;
	private Label label;
	
	
	public BaseMapPanel(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
		
		setCollapsible(false);
		setFrame(true);
		setHeading(I18N.CONSTANTS.basemap());
		setBodyBorder(false);
		setIcon(AbstractImagePrototype.create(MapResources.INSTANCE.layers()));
		
		HBoxLayout layout = new HBoxLayout();
		layout.setHBoxLayoutAlign(HBoxLayoutAlign.MIDDLE);

		setLayout(layout);

		Image icon = new Image(MapResources.INSTANCE.globe());
		label = new Label();
		Button button = new Button("Change", new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				chooseBaseMap();
			}
		});
		
		HBoxLayoutData iconLayout = new HBoxLayoutData(0, 5, 0, 2);
		add(icon, iconLayout);
		
		HBoxLayoutData labelLayout = new HBoxLayoutData(0, 0, 0, 5);
		labelLayout.setFlex(1);
		add(label, labelLayout);
		
		add(button);
		
		label.setText(I18N.CONSTANTS.loading());
	}

	private void chooseBaseMap() {
		BaseMapDialog dialog = new BaseMapDialog(dispatcher);
		dialog.show(value, new Callback() {
			
			@Override
			public void onSelect(String baseMapId, String label) {
				BaseMapPanel.this.value = baseMapId;
				BaseMapPanel.this.label.setText(label);
				
				ValueChangeEvent.fire(BaseMapPanel.this, value);
			}
		});
	}

	@Override
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<String> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

	/**
	 * Gets the BaseMap id
	 */
	@Override
	public String getValue() {
		return value;
	}

	/**
	 * Sets the BaseMap id
	 */
	@Override
	public void setValue(String value) {
		setValue(value, false);
	}

	@Override
	public void setValue(String value, boolean fireEvents) {
		this.value = value;
		if(GoogleBaseMap.HYBRID.getId().equals(value)) {
			label.setText(I18N.CONSTANTS.googleHybrid());
		} else if(GoogleBaseMap.ROADMAP.getId().equals(value)) {
			label.setText(I18N.CONSTANTS.googleRoadmap());
		} else if(GoogleBaseMap.SATELLITE.getId().equals(value)) {
			label.setText(I18N.CONSTANTS.googleSatelliteMap());
		} else if(GoogleBaseMap.TERRAIN.getId().equals(value)) {
			label.setText(I18N.CONSTANTS.googleTerrainMap());
		} else {
			loadTileMapLabel();
		}
	}

	private void loadTileMapLabel() {

		label.setText(I18N.CONSTANTS.loading());
		dispatcher.execute(new GetBaseMaps(), null, new AsyncCallback<BaseMapResult>() {

			@Override
			public void onFailure(Throwable caught) {
				label.setText(value);
			}

			@Override
			public void onSuccess(BaseMapResult result) {
				for(TileBaseMap baseMap : result.getBaseMaps()) {
					if(baseMap.getId().equals(value)) {
						label.setText(baseMap.getName());
						return;
					}
				}
				label.setText(value);
			}
		});
	}
}
