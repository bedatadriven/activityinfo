package org.activityinfo.client.report.editor.map.layerOptions;

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

import java.util.HashMap;
import java.util.Map;

import org.activityinfo.shared.report.model.MapIcon;
import org.activityinfo.shared.report.model.MapIcon.Icon;
import org.activityinfo.shared.report.model.layers.IconMapLayer;

import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.Radio;
import com.extjs.gxt.ui.client.widget.form.RadioGroup;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Image;

/*
 * Displays a list of options the user can choose to configure an IconMapLayer
 */
public class IconLayerOptions extends LayoutContainer implements LayerOptionsWidget<IconMapLayer> {
	private IconMapLayer iconMapLayer;
	private RadioGroup radiogroupIcons =  new RadioGroup();
	private HorizontalPanel contentpanelIcons =  new HorizontalPanel();
	// Are Bidimaps from commons collections supported GWT clientside?
	private Map<Radio, Icon> radioIcons = new HashMap<Radio, Icon>();
	private Map<Icon, Radio> iconsRadio = new HashMap<Icon, Radio>();
	
	public IconLayerOptions() {
		super();
		
		initializeComponent();

		populateWithIcons();

		radiogroupIcons.addListener(Events.Change, new Listener<FieldEvent>() {
			@Override
			public void handleEvent(FieldEvent be) {
				iconMapLayer.setIcon(radioIcons.get(radiogroupIcons.getValue()).name());
				ValueChangeEvent.fire(IconLayerOptions.this, iconMapLayer);
			}
		});
	}

	private void initializeComponent() {
		contentpanelIcons.setAutoWidth(true);
		add(contentpanelIcons);
	}

	private void populateWithIcons() {
		boolean isFirst = true;
		for (Icon mapIcon : Icon.values())
		{
			ContentPanel iconPanel = new ContentPanel();
			iconPanel.setHeaderVisible(false);
			iconPanel.setLayout(new RowLayout(Orientation.VERTICAL));
			iconPanel.setAutoWidth(true);
			
			Radio radiobuttonIcon = new Radio();
			
			iconPanel.add(radiobuttonIcon);
			iconPanel.add(new Image(MapIcon.fromEnum(mapIcon)));
			
			radioIcons.put(radiobuttonIcon, mapIcon);
			iconsRadio.put(mapIcon, radiobuttonIcon);
			radiogroupIcons.add(radiobuttonIcon);
			contentpanelIcons.add(iconPanel);
			
			if (isFirst) {
				radiobuttonIcon.setValue(true);
				isFirst=false;
			}
		}
	}
	
	@Override
	public IconMapLayer getValue() {
		return iconMapLayer;
	}
	@Override
	public void setValue(IconMapLayer value) {
		this.iconMapLayer=value;
		updateUI();
	}
	private void updateUI() {
		iconsRadio.get(Icon.valueOf(iconMapLayer.getIcon())).setValue(true);
	}

	@Override
	public void setValue(IconMapLayer value, boolean fireEvents) {
		setValue(value);
	}
	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<IconMapLayer> handler) {
		return this.addHandler(handler, ValueChangeEvent.getType());
	}
	
}
