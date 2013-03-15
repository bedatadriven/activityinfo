package org.activityinfo.server.digest;

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

import org.activityinfo.server.report.output.LocalStorageProvider;
import org.activityinfo.server.report.output.StorageProvider;
import org.activityinfo.server.report.renderer.image.ImageMapRenderer;
import org.activityinfo.server.report.renderer.itext.TestGeometry;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

public class TestDigestModule extends AbstractModule {

    @Override
    protected void configure() {
    }

    @Provides
    public ImageMapRenderer provideImageMapRenderer() {
        return new ImageMapRenderer(TestGeometry.get(), "war/mapicons");
    }

    @Provides
    public StorageProvider provideImageStorageProvider() {
        return new LocalStorageProvider("target");
    }
}
