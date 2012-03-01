/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.endpoint.jsonrpc;

import com.google.inject.servlet.ServletModule;

public class JsonRpcModule extends ServletModule {

    @Override
    protected void configureServlets() {
        serve("/api/*").with(JsonCommandServlet.class);
    }
}
