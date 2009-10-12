package org.activityinfo.client.page.common.toolbar;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.button.SplitButton;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import org.activityinfo.client.Application;
import org.activityinfo.shared.command.RenderElement;

public class ExportMenuButton extends SplitButton {

    private RenderElement.Format defaultFormat;
    private ExportCallback callback;

    public ExportMenuButton(final RenderElement.Format defaultFormat, final ExportCallback callback) {

        this.defaultFormat = defaultFormat;
        this.setIcon(formatIcon(defaultFormat));
        this.setText(Application.CONSTANTS.export());

        this.addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                callback.export(defaultFormat);
            }
        });

        SelectionListener<MenuEvent> menuListener = new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(MenuEvent ce) {

                callback.export((RenderElement.Format) ce.getItem().getData("format"));

            }
        };

        Menu menu = new Menu();

        MenuItem word = new MenuItem("Word", formatIcon(RenderElement.Format.Word), menuListener);
        word.setData("format", RenderElement.Format.Word);
        menu.add(word);

        MenuItem excel = new MenuItem("Excel", formatIcon(RenderElement.Format.Excel), menuListener);
        excel.setData("format", RenderElement.Format.Excel);
        menu.add(excel);

        MenuItem ppt = new MenuItem("PowerPoint", formatIcon(RenderElement.Format.PowerPoint), menuListener);
        ppt.setData("format", RenderElement.Format.PowerPoint);
        menu.add(ppt);

        MenuItem pdf = new MenuItem("PDF", formatIcon(RenderElement.Format.PDF), menuListener);
        pdf.setData("format", RenderElement.Format.PDF);
        menu.add(pdf);

        MenuItem image = new MenuItem("Image", formatIcon(RenderElement.Format.PNG), menuListener);
        image.setData("format", RenderElement.Format.PNG);
        menu.add(image);

        setMenu(menu);

    }

    private AbstractImagePrototype formatIcon(RenderElement.Format format) {
        if(format == RenderElement.Format.Excel) {
            return Application.ICONS.excel();
        } else if(format == RenderElement.Format.PNG) {
            return Application.ICONS.image();
        } else if(format == RenderElement.Format.PowerPoint) {
            return Application.ICONS.ppt();
        } else if(format == RenderElement.Format.Word) {
            return Application.ICONS.msword();
        } else if(format == RenderElement.Format.PDF) {
            return Application.ICONS.pdf();
        } else {
            return Application.ICONS.report();
        }
    }

}
