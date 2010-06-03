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

package org.activityinfo.server.endpoint.gwtrpc;

import com.google.inject.servlet.ServletModule;
import org.activityinfo.server.endpoint.login.LoginServlet;

public class GwtRpcModule extends ServletModule {

    @Override
    protected void configureServlets() {
        // The CacheFilter assures that static files marked with
        // .nocache (e.g. strongly named js permutations) get sent with the
        // appropriate cache header so browsers don't ask for it again.
        filter("/Application/*").through(CacheFilter.class);
        filter("/Login/*").through(CacheFilter.class);

        serve("/Application/cmd").with(CommandServlet.class);
        serve("/Application/download").with(DownloadServlet.class);

        // this is here for now but should be probably live elsewhere, if
        // we really need it at all
        serve("/icon").with(MapIconServlet.class);

        serve("/Login/service").with(LoginServiceServlet.class);

    }
}
