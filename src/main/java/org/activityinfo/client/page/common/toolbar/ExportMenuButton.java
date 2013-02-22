

package org.activityinfo.client.page.common.toolbar;

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

import java.util.Collections;
import java.util.List;

import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.icon.IconImageBundle;
import org.activityinfo.shared.command.RenderElement;
import org.activityinfo.shared.command.RenderElement.Format;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.button.SplitButton;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * SplitButton that provides users with a choice to export in a number of formats.
 */
public class ExportMenuButton extends SplitButton { 

    private Menu menu;
	private SelectionListener<MenuEvent> menuListener;
	private ExportCallback callback;
	private List<Format> formats;

	/**
     * @param defaultFormat the default format that should appear on the SplitButton
     * @param callback the object to call back upon selection
     */
    public ExportMenuButton() {
        this.setText(I18N.CONSTANTS.export());

        this.addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
            	if(callback != null) {
            		callback.export(formats.get(0));
            	}
            }
        });
        menuListener = new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(MenuEvent ce) {
                callback.export((RenderElement.Format) ce.getItem().getData("format"));
            }
        };

        menu = new Menu();
        setMenu(menu);
        
        setFormats(Collections.singletonList(Format.PDF));
    }
    
    public void setFormats(List<RenderElement.Format> formats) {
    	this.formats = formats;
    	setIcon(formatIcon(formats.get(0)));
    	menu.removeAll();
    	for(RenderElement.Format format : formats) {
            MenuItem word = new MenuItem(formatLabel(format), formatIcon(format), menuListener);
            word.setData("format", format);
            menu.add(word);
    	}
    }
    
    public ExportMenuButton setCallback(ExportCallback callback) {
    	this.callback = callback;
    	return this;
    }
    
    
    private AbstractImagePrototype formatIcon(RenderElement.Format format) {
    	switch(format) {
    	case Excel:
    		return IconImageBundle.ICONS.excel();
    	case PowerPoint:
    		return IconImageBundle.ICONS.ppt();
    	case PDF:
    		return IconImageBundle.ICONS.pdf();
    	case Word:
    		return IconImageBundle.ICONS.msword();
    	case PNG:
    		return IconImageBundle.ICONS.image();
    	}
    	throw new IllegalArgumentException("" + format);
    }
    
    private String formatLabel(RenderElement.Format format) {
    	switch(format) {
    	case Excel:
    		return I18N.CONSTANTS.excel();
    	case PowerPoint:
    		return I18N.CONSTANTS.powerPoint();
    	case PDF:
    		return I18N.CONSTANTS.pdf();
    	case Word:
    		return I18N.CONSTANTS.word();
    	case PNG:
    		return I18N.CONSTANTS.image();
    	}
    	throw new IllegalArgumentException("" + format);
    }
}
