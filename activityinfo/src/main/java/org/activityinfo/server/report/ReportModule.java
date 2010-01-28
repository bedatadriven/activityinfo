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

package org.activityinfo.server.report;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import org.activityinfo.server.report.generator.MapIconPath;
import org.activityinfo.server.report.renderer.html.HtmlChartRenderer;
import org.activityinfo.server.report.renderer.html.HtmlChartRendererJC;

public class ReportModule extends AbstractModule {

    @Override
    protected void configure() {

        bind(String.class)
                .annotatedWith(MapIconPath.class)
                .toProvider(MapIconPathProvider.class)
                .in(Singleton.class);

        /* Renderers */
        bind(HtmlChartRenderer.class).to(HtmlChartRendererJC.class);
    }
}
