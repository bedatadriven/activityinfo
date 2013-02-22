package org.activityinfo.client.page.entry.admin;

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

import org.activityinfo.shared.util.mapping.Extents;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.EventType;

/**
 * Signals that the geographic bounds of the selection in the
 * AdminFieldSetPresenter have changed.
 */
public class BoundsChangedEvent extends BaseEvent {

    public static final EventType TYPE = new EventType();

    private Extents bounds;
    private String name;

    public BoundsChangedEvent(Extents bounds, String name) {
        super(TYPE);
        this.bounds = bounds;
        this.name = name;
    }

    public Extents getBounds() {
        return bounds;
    }

    public void setBounds(Extents bounds) {
        this.bounds = bounds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((bounds == null) ? 0 : bounds.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        BoundsChangedEvent other = (BoundsChangedEvent) obj;
        if (bounds == null) {
            if (other.bounds != null) {
                return false;
            }
        } else if (!bounds.equals(other.bounds)) {
            return false;
        }
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "BoundsChangedEvent [bounds=" + bounds + ", name=" + name + "]";
    }
}
