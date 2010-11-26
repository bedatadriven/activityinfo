/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.ui;

import com.extjs.gxt.ui.client.widget.form.Field;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Widget;

/**
 * GXT style form field that contains a functionnal GWT widget.
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
public class WidgetField<W extends Widget, D> extends Field<D> {
    private W widget;

    public WidgetField(W widget) {
        setWidth(150); // Default width for GXT fields. Taken from TextField<D>.
        this.widget = widget;
    }

    public W getWidget() {
        return widget;
    }

    @Override
    public void onBrowserEvent(Event event) {
        int mouseX = event.getClientX();
        int mouseY = event.getClientY();

        int top = widget.getAbsoluteTop();
        int left = widget.getAbsoluteLeft();

        int buttonWidth = getButtonWidth(widget.getElement().getId());
        int buttonHeight = getButtonHeight(widget.getElement().getId());

        if( (mouseX > left && mouseX < left+buttonWidth) &&
            (mouseY > top && mouseY < top+buttonHeight)) {
            widget.onBrowserEvent(event);
        }
    }

    private native int getButtonWidth(String id) /*-{
        var element = $wnd.document.getElementById(id);
        var style = $wnd.getComputedStyle(element, null);
        return parseInt(style.width);
    }-*/;

    private native int getButtonHeight(String id) /*-{
        var element = $wnd.document.getElementById(id);
        var style = $wnd.getComputedStyle(element, null);
        return parseInt(style.height);
    }-*/;

    @Override
    protected void onRender(Element parent, int index) {
        if(el() == null) {
            setElement(widget.getElement(), parent, index);
        }

        if(widget.getElement().getId() == null)
            widget.getElement().setId("element-"+(int) (Math.random()*9999)+'-'+(int) (Math.random()*9999));

        super.onRender(parent, index);
    }
}
