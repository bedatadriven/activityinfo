/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.report;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import org.sigmah.server.report.generator.MapIconPath;
import org.sigmah.server.report.renderer.html.HtmlChartRenderer;
import org.sigmah.server.report.renderer.html.HtmlChartRendererJC;

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
