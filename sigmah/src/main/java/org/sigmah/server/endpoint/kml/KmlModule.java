/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.endpoint.kml;

import com.google.inject.servlet.ServletModule;

/**
 * @author Alex Bertram
 */
public class KmlModule extends ServletModule {

    @Override
    protected void configureServlets() {
        serve("/kml").with(KmlLinkServlet.class);
        serve("/kml/data").with(KmlDataServlet.class);
    }


}
