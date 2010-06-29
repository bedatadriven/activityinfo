/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.endpoint.wfs;

import com.google.inject.servlet.ServletModule;

public class WfsModule extends ServletModule {

    @Override
    protected void configureServlets() {
        serve("/wfs").with(WfsServlet.class);
        serve("/wfs*").with(WfsServlet.class);
    }
}
