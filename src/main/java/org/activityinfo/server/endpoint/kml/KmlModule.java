/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.endpoint.kml;

import com.google.inject.servlet.ServletModule;

/**
 * @author Alex Bertram
 */
public class KmlModule extends ServletModule {

    @Override
    protected void configureServlets() {
        serve("/earth").with(KmlLinkServlet.class);
        serve("/earth/activities").with(KmlActivityServlet.class);
        serve("/earth/sites").with(KmlDataServlet.class);
    }


}
