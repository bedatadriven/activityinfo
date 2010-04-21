/*
 * This file is part of ActivityInfo.
 *
 * ActivityInfo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ActivityInfo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ActivityInfo.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2010 Alex Bertram and contributors.
 */

package org.activityinfo.server.util;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

import java.util.ArrayList;
import java.util.List;

public class BeanMappingModule extends AbstractModule {

    @Override
    protected void configure() {
    }

    @Provides
    @Singleton
    Mapper provideMapper() {
        List<String> mappingFiles = new ArrayList<String>();
        mappingFiles.add("dozer-admin-mapping.xml");
        mappingFiles.add("dozer-schema-mapping.xml");

        return new DozerBeanMapper(mappingFiles);
    }

    public static Mapper getMapper() {
        BeanMappingModule mod = new BeanMappingModule();
        return mod.provideMapper();
    }
}
