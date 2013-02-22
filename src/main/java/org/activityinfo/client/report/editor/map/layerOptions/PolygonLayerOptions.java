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

import org.activityinfo.client.i18n.I18N;
import org.activityinfo.shared.report.model.layers.PolygonMapLayer;

import com.extjs.gxt.ui.client.event.ColorPaletteEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.ColorPalette;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.google.common.base.Objects;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;

public class PolygonLayerOptions extends LayoutContainer implements LayerOptionsWidget<PolygonMapLayer> {
		private PolygonMapLayer layer;
		private ColorPalette colorPicker = new ColorPalette();
		
		public PolygonLayerOptions() {
			super();
			setStyleAttribute("padding", "5px");
			createColorPicker();
		}

		private void createColorPicker() {
			colorPicker.setValue("000000");
			
			// Set the selected color to the maplayer
			colorPicker.addListener(Events.Select, new Listener<ColorPaletteEvent>() {
				@Override
				public void handleEvent(ColorPaletteEvent be) {
					if(!Objects.equal(layer.getMaxColor(), colorPicker.getValue())) {
						layer.setMaxColor(colorPicker.getValue());
						ValueChangeEvent.fire(PolygonLayerOptions.this, layer);
					}
			}});

			LabelField labelColor = new LabelField(I18N.CONSTANTS.color());
			add(labelColor);
			add(colorPicker);
		}
		

		@Override
		public PolygonMapLayer getValue() {
			return layer;
		}

		private void updateUI() {
			colorPicker.setValue(layer.getMaxColor());
		}
		
		// TODO: fireevent
		@Override
		public void setValue(PolygonMapLayer value, boolean fireEvents) {
			setValue(value);
		}
		 
		@Override
		public void setValue(PolygonMapLayer value) {
			this.layer=value;
			updateUI();
		}
		
		@Override
		public HandlerRegistration addValueChangeHandler(
				ValueChangeHandler<PolygonMapLayer> handler) {
			return this.addHandler(handler, ValueChangeEvent.getType());
		}
	}
