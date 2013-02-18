/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.client.page.common.toolbar;

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
