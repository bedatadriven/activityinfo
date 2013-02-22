

package org.activityinfo.client.widget;

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

import com.extjs.gxt.ui.client.event.ColorPaletteEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.DomEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.form.TriggerField;
import com.extjs.gxt.ui.client.widget.menu.ColorMenu;
import com.google.gwt.user.client.Element;

/**
 * GXT Field that allows users to select a color from a standard palette
 *
 * @author Alex Bertram
 */
public class ColorField extends TriggerField<String> {
    private static final String WHITE_HEX_STRING = "FFFFFF";
    private static final int WHITE = 0xFFFFFF;
	private ColorMenu menu;

    public ColorField() {
        setEditable(false);
        setFireChangeEventOnSetValue(true);
    }

    @Override
	protected boolean validateBlur(DomEvent ce, Element target) {
        return menu == null || (menu != null && !menu.isVisible() && !menu.getElement().isOrHasChild(target));
	}

	@Override
    protected void onTriggerClick(ComponentEvent ce) {
        super.onTriggerClick(ce);
        
        menu = new ColorMenu() {

			@Override
			protected void onClick(ComponentEvent ce) {
				// TODO Auto-generated method stub
				
			}};
        
        menu.getColorPalette().addListener(Events.BeforeSelect, new Listener<ColorPaletteEvent>() {
            @Override
			public void handleEvent(ColorPaletteEvent ce) {
                setValue(ce.getColor());
                menu.hide();
            }
        });
        menu.show(getElement(), "l");
    }

    /**
     * Sets the field's current value
     *
     * @param value the color value, as a hex string, WITHOUT preceding '#', e.g. FFFFFF
     */
    @Override
    public void setValue(String value) {
        super.setValue(value);
        if(isRendered()) {
            input.setStyleAttribute("backgroundColor", "#" + (value == null ? WHITE_HEX_STRING : value));
        }
    }
    
    public void setValue(int value) {
    	setValue(Integer.toHexString(value));
    }
    

    @Override
    public void render(Element target, int index) {
        super.render(target, index);
        input.setStyleAttribute("backgroundColor", "#" + (value == null ? WHITE_HEX_STRING : value));
        input.setStyleAttribute("backgroundImage", "none");
    }

    /**
     * @return the current color value as a 32-bit integer.
     */
    public int getIntValue() {
        return value == null ? WHITE : Integer.parseInt(value, 16);
    }

	@Override
	protected void onBlur(ComponentEvent ce) {
		super.onBlur(ce);
        fireEvent(Events.Select, new FieldEvent(ColorField.this));
	}

	public void setValue(String bubbleColor, boolean b) {
		if (b) {
			setFireChangeEventOnSetValue(false);
			setValue(bubbleColor);
			setFireChangeEventOnSetValue(true);
		} else {
			setValue(bubbleColor);
		}
	}

}
