package org.activityinfo.client.page.entry.location;

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

import org.activityinfo.shared.dto.CountryDTO;
import org.activityinfo.shared.dto.LocationDTO;
import org.activityinfo.shared.report.content.AiLatLng;
import org.activityinfo.shared.util.mapping.Extents;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.BaseObservable;
import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.event.Listener;

public class NewLocationPresenter extends BaseObservable {

    public static final EventType POSITION_CHANGED = new EventType();
    public static final EventType ACTIVE_STATE_CHANGED = new EventType();
    public static final EventType BOUNDS_CHANGED = new EventType();
    public static final EventType ACCEPTED = new EventType();

    private AiLatLng latLng;
    private boolean provisional;
    private boolean active;
    private Extents bounds;

    public NewLocationPresenter(CountryDTO country) {
        provisional = true;
        bounds = country.getBounds();
        latLng = bounds.center();
    }

    public AiLatLng getLatLng() {
        return latLng;
    }

    public boolean isProvisional() {
        return provisional;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
        fireEvent(ACTIVE_STATE_CHANGED, new BaseEvent(ACTIVE_STATE_CHANGED));
    }

    public void setLatLng(AiLatLng latLng) {
        this.latLng = latLng;
        this.provisional = false;
        fireEvent(POSITION_CHANGED, new BaseEvent(POSITION_CHANGED));
    }

    public void setBounds(Extents bounds) {
        this.bounds = bounds;
        if (!bounds.contains(latLng)) {
            latLng = bounds.center();
            provisional = true;
            fireEvent(POSITION_CHANGED, new BaseEvent(POSITION_CHANGED));
        }
        fireEvent(BOUNDS_CHANGED, new BaseEvent(BOUNDS_CHANGED));
    }

    public Extents getBounds() {
        return bounds;
    }

    public void addAcceptedListener(Listener<LocationEvent> listener) {
        addListener(ACCEPTED, listener);
    }

    public void accept(LocationDTO location) {
        fireEvent(ACCEPTED, new LocationEvent(ACCEPTED, this, location));
    }

}
