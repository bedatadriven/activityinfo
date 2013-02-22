package org.activityinfo.client.mock;

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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.activityinfo.client.util.state.CrossSessionStateProvider;

public class StateManagerStub implements CrossSessionStateProvider {

    Map<String, Object> state = new HashMap<String, Object>();

    @Override
    public Object get(String name) {
        return state.get(name);
    }

    @Override
    public Date getDate(String name) {
        return (Date) get(name);
    }

    @Override
    public Integer getInteger(String name) {
        return (Integer) get(name);
    }

    @Override
    public Map<String, Object> getMap(String name) {
        return (Map<String, Object>) get(name);
    }

    @Override
    public String getString(String name) {
        return (String) get(name);
    }

    @Override
    public void set(String name, Object value) {
        state.put(name, value);
    }
}
