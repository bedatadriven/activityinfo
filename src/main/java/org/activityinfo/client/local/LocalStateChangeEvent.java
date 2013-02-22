package org.activityinfo.client.local;

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

import org.activityinfo.client.EventBus.NamedEventType;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.EventType;

public class LocalStateChangeEvent extends BaseEvent {

    public static final EventType TYPE = new NamedEventType(
        "OfflineStateChange");

    public enum State {
        CHECKING, UNINSTALLED, INSTALLED, INSTALLING
    };

    private State state;

    public LocalStateChangeEvent(State state) {
        super(TYPE);
        this.state = state;
    }

    public State getState() {
        return state;
    }

    @Override
    public String toString() {
        return "OfflineStateChangeEvent: " + state.name();
    }
}
