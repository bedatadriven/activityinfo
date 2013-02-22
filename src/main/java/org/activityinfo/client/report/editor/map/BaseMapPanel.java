package org.activityinfo.client.report.editor.map;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.report.editor.map.mapOptions.BaseMapDialog;
import org.activityinfo.client.report.editor.map.mapOptions.BaseMapDialog.Callback;
import org.activityinfo.shared.command.GetBaseMaps;
import org.activityinfo.shared.command.result.BaseMapResult;
import org.activityinfo.shared.map.GoogleBaseMap;
import org.activityinfo.shared.map.TileBaseMap;
import org.activityinfo.shared.report.model.MapReportElement;

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
		if(MapReportElement.AUTO_BASEMAP.equals(value) || value == null) {
			label.setText("Default");
		} else if(GoogleBaseMap.HYBRID.getId().equals(value)) {
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
		dispatcher.execute(new GetBaseMaps(), new AsyncCallback<BaseMapResult>() {

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
