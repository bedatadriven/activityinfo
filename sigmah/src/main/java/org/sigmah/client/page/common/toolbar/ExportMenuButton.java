/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.common.toolbar;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.button.SplitButton;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.shared.command.RenderElement;

/**
 * SplitButton that provides users with a choice to export in a number of formats.
 */
public class ExportMenuButton extends SplitButton {

    private Menu menu;
	private SelectionListener<MenuEvent> menuListener;
	private ExportCallback callback;

	/**
     * @param defaultFormat the default format that should appear on the SplitButton
     * @param callback the object to call back upon selection
     */
    public ExportMenuButton(final RenderElement.Format defaultFormat) {
        this.setIcon(formatIcon(defaultFormat));
        this.setText(I18N.CONSTANTS.export());

        this.addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
            	assert callback != null : "You must call withCallback() when building!!";
                callback.export(defaultFormat);
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
    }
    
    public ExportMenuButton callbackTo(ExportCallback callback) {
    	this.callback = callback;
    	return this;
    }
    
    public ExportMenuButton withWord() {
        MenuItem word = new MenuItem(I18N.CONSTANTS.word(), formatIcon(RenderElement.Format.Word), menuListener);
        word.setData("format", RenderElement.Format.Word);
        menu.add(word);
        
        return this;
    }
    
    public ExportMenuButton withExcel() {
        MenuItem excel = new MenuItem(I18N.CONSTANTS.excel(), formatIcon(RenderElement.Format.Excel), menuListener);
        excel.setData("format", RenderElement.Format.Excel);
        menu.add(excel);
        
        return this;
    }
    
    public ExportMenuButton withPowerPoint() {
        MenuItem ppt = new MenuItem(I18N.CONSTANTS.powerPoint(), formatIcon(RenderElement.Format.PowerPoint), menuListener);
        ppt.setData("format", RenderElement.Format.PowerPoint);
        menu.add(ppt);
        
        return this;
    }
    
    public ExportMenuButton withPdf() {
        MenuItem pdf = new MenuItem(I18N.CONSTANTS.pdf(), formatIcon(RenderElement.Format.PDF), menuListener);
        pdf.setData("format", RenderElement.Format.PDF);
        menu.add(pdf);
        
        return this;
    }
    
    public ExportMenuButton withPng() {
        MenuItem image = new MenuItem(I18N.CONSTANTS.image(), formatIcon(RenderElement.Format.PNG), menuListener);
        image.setData("format", RenderElement.Format.PNG);
        menu.add(image);
        
        return this;
    }
    
    
    private AbstractImagePrototype formatIcon(RenderElement.Format format) {
        if (format == RenderElement.Format.Excel) {
            return IconImageBundle.ICONS.excel();
        } else if (format == RenderElement.Format.PNG) {
            return IconImageBundle.ICONS.image();
        } else if (format == RenderElement.Format.PowerPoint) {
            return IconImageBundle.ICONS.ppt();
        } else if (format == RenderElement.Format.Word) {
            return IconImageBundle.ICONS.msword();
        } else if (format == RenderElement.Format.PDF) {
            return IconImageBundle.ICONS.pdf();
        } else {
            return IconImageBundle.ICONS.report();
        }
    }
}
