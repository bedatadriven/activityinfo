/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.endpoint.export;

import com.google.inject.servlet.ServletModule;

public class ExportModule extends ServletModule {

    @Override
    protected void configureServlets() {
        serve("/ActivityInfo/export*").with(ExportServlet.class);
        serve("/report").with(ReportServlet.class);
    }
}
