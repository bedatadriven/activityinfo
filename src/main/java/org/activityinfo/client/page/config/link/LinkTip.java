package org.activityinfo.client.page.config.link;

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

import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.tips.Tip;
import com.google.gwt.user.client.Event;

public class LinkTip extends Tip {

    public static final int WIDTH = 75;
    public static final int HEIGHT = 45;
    private Html html;

    public LinkTip() {
        setWidth(WIDTH);
        setHeight(HEIGHT);
        html = new Html("Link");
        html.addStyleName(IndicatorLinkResources.INSTANCE.style().linkToggle());
        html.sinkEvents(Event.ONCLICK);
        html.addListener(Events.BrowserEvent, new Listener<ComponentEvent>() {

            @Override
            public void handleEvent(ComponentEvent be) {
                if (be.getEventTypeInt() == Event.ONCLICK) {
                    fireEvent(Events.Select, be);
                }
            }

        });
        add(html);
    }

    public void setLinked(boolean link) {
        html.setHtml(link ? "Unlink" : "Link");
    }

    public void addSelectListener(SelectionListener<ComponentEvent> listener) {
        addListener(Events.Select, listener);
    }

}
