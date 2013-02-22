

package org.activityinfo.client.event;

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

import org.activityinfo.client.page.PageState;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.EventType;

/**
 * Encapsulates information related to page navigation events.
 * 
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class NavigationEvent extends BaseEvent {

    private final PageState place;

    public NavigationEvent(EventType type, PageState place) {
        super(type);
        this.place = place;

        assert this.place != null;
    }

    public PageState getPlace() {
        return place;
    }

    @Override
    public String toString() {
        return place.getPageId() + "/" + place.serializeAsHistoryToken();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        NavigationEvent that = (NavigationEvent) o;
        if (place != null ? !place.equals(that.place) : that.place != null) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return place != null ? place.hashCode() : 0;
    }
}
