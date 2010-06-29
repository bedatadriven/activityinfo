package org.sigmah.client.page.common.widget;

import com.extjs.gxt.ui.client.event.ColorPaletteEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.form.TriggerField;
import com.extjs.gxt.ui.client.widget.menu.ColorMenu;
import com.google.gwt.user.client.Element;
/*
 * @author Alex Bertram
 */

public class ColorField extends TriggerField<String> {


    public ColorField() {
        setEditable(false);
    }

    @Override
    protected void onTriggerClick(ComponentEvent ce) {
        super.onTriggerClick(ce);

        ColorMenu menu = new ColorMenu();
        menu.getColorPalette().addListener(Events.Select, new Listener<ColorPaletteEvent>() {
            public void handleEvent(ColorPaletteEvent ce) {
                setValue(ce.getColor());
            }
        });
        menu.show(getElement(), "b");
    }

    @Override
    public void setValue(String value) {
        super.setValue(value);

        if(isRendered()) {
            input.setStyleAttribute("backgroundColor", "#" + (value == null ? "FFFFFF" : value));
        }
    }

    @Override
    public void render(Element target, int index) {
        super.render(target, index);

        input.setStyleAttribute("backgroundColor", "#" + (value == null ? "FFFFFF" : value));
        input.setStyleAttribute("backgroundImage", "none");
    }

    public void setIntValue(int rgb) {
        StringBuilder sb = new StringBuilder(6);
        String hex = Integer.toHexString(rgb);
        for(int i=hex.length(); i!=6; ++i) {
            sb.append('0');
        }
        sb.append(hex);
        setValue(sb.toString());
    }
    
    public int getIntValue() {
        return value == null ? null : Integer.parseInt(value, 16);
    }
}
