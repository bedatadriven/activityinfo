package org.activityinfo.client.util.state;

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
import java.util.Map;

import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.state.CookieProvider;
import com.extjs.gxt.ui.client.state.Provider;
import com.google.gwt.storage.client.Storage;
import com.google.inject.Singleton;

@Singleton
public class CrossSessionStateProviderImpl implements CrossSessionStateProvider {

    private Provider provider;

    public CrossSessionStateProviderImpl() {
        if (Storage.isLocalStorageSupported()) {
            provider = new SafeStateProvider();
        } else {
            provider = new CookieProvider("/", null, null, GXT.isSecure);
        }
    }

    @Override
    public Object get(String name) {
        return provider.get(name);
    }

    @Override
    public Date getDate(String name) {
        return provider.getDate(name);
    }

    @Override
    public Integer getInteger(String name) {
        return provider.getInteger(name);
    }

    @Override
    public Map<String, Object> getMap(String name) {
        return provider.getMap(name);
    }

    @Override
    public String getString(String name) {
        return provider.getString(name);
    }

    @Override
    public void set(String name, Object value) {
        provider.set(name, value);
    }
}
