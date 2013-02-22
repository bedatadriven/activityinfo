package org.activityinfo.client;

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

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.event.Observable;

/**
 * The EventBus is a publish/subscribe API that allows objects to communicate
 * with each other without having to refer to each other.
 */
public interface EventBus extends Observable {

    public static class NamedEventType extends EventType {
        private final String name;

        public NamedEventType(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public boolean fireEvent(EventType type);

    public boolean fireEvent(BaseEvent event);
}
