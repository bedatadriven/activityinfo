/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.endpoint.export;

import com.google.inject.servlet.ServletModule;

public class ExportModule extends ServletModule {

    @Override
    protected void configureServlets() {
    	serve("/ActivityInfo/export/users*").with(ExportUsersServlet.class);
        serve("/ActivityInfo/export*").with(ExportSitesServlet.class);
        serve("/report").with(ReportServlet.class);
    }
}
