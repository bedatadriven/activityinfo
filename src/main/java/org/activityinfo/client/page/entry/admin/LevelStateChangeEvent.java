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

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.EventType;

/**
 * Signals that an AdminLevel's enabled state has changed based on the current
 * selection in {@link AdminFieldSetPresenter}
 */
public class LevelStateChangeEvent extends BaseEvent {

    public static final EventType TYPE = new EventType();

    private int levelId;
    private boolean enabled;

    public LevelStateChangeEvent(int levelId, boolean enabled) {
        super(TYPE);
        this.levelId = levelId;
        this.enabled = enabled;
    }

    public int getLevelId() {
        return levelId;
    }

    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (enabled ? 1231 : 1237);
        result = prime * result + levelId;
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
        LevelStateChangeEvent other = (LevelStateChangeEvent) obj;
        if (enabled != other.enabled) {
            return false;
        }
        if (levelId != other.levelId) {
            return false;
        }
        return true;
    }

}
