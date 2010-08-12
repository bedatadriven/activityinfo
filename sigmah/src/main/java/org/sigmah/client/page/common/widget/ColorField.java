/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.common.widget;

import com.extjs.gxt.ui.client.event.ColorPaletteEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
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
    private static final int WHITE = 0xFFFFF;

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
}
