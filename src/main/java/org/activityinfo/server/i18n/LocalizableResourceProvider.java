package org.activityinfo.server.i18n;

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

import com.google.gwt.i18n.client.LocalizableResource;
import com.google.inject.Provider;
import com.teklabs.gwt.i18n.client.LocaleFactory;
import com.teklabs.gwt.i18n.server.LocaleProxy;

public class LocalizableResourceProvider<T extends LocalizableResource>
    implements Provider<T> {

    private Class<T> clazz;

    public LocalizableResourceProvider(Class<T> clazz) {
        super();
        this.clazz = clazz;
        LocaleProxy.initialize();
    }

    @Override
    public T get() {
        return LocaleFactory.get(clazz);
    }
}
