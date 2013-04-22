package org.activityinfo.server.login;

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

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.activityinfo.server.authentication.ServerSideAuthProvider;
import org.activityinfo.server.login.model.HostPageModel;
import org.activityinfo.server.login.model.LoginPageModel;
import org.activityinfo.server.util.config.DeploymentConfiguration;
import org.activityinfo.server.util.logging.LogException;

import com.bedatadriven.rebar.appcache.server.UserAgentProvider;
import com.google.inject.Inject;
import com.sun.jersey.api.view.Viewable;

@Path(HostController.ENDPOINT)
public class HostController {
    public static final String ENDPOINT = "/";

    private final DeploymentConfiguration deployConfig;
    private final ServerSideAuthProvider authProvider;

    @Inject
    public HostController(DeploymentConfiguration deployConfig,
        ServerSideAuthProvider authProvider) {
        super();
        this.deployConfig = deployConfig;
        this.authProvider = authProvider;
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @LogException(emailAlert = true)
    public Response getHostPage(@Context UriInfo uri,
        @Context HttpServletRequest req,
        @QueryParam("redirect") boolean redirect) throws Exception {
        if (!authProvider.isAuthenticated()) {
            return Response
                .ok(new LoginPageModel().asViewable())
                .type(MediaType.TEXT_HTML)
                .cacheControl(CacheControl.valueOf("no-cache"))
                .build();
        }

        if (redirect) {
            return Response.seeOther(
                uri.getAbsolutePathBuilder().replacePath(ENDPOINT).build())
                .build();
        }

        String appUri = uri.getAbsolutePathBuilder().replaceQuery("").build()
            .toString();

        HostPageModel model = new HostPageModel(appUri);
        model.setAppCacheEnabled(checkAppCacheEnabled(req));
        model.setMapsApiKey(deployConfig.getProperty("mapsApiKey"));
        return Response.ok(model.asViewable())
            .type(MediaType.TEXT_HTML)
            .cacheControl(CacheControl.valueOf("no-cache"))
            .build();
    }
    
    /**
     * 
     * @return a simple error page indicating that the GWT app does not support
     *  the user's browser. This is necessary because user-agent based selection
     *  is done server-side when the javascript is requested, so all we can do 
     *  is redirect the user to this page.
     */
    @GET
    @Path("/unsupportedBrowser")
    public Viewable getUnsupportedBrowserMessage() {
        return new Viewable("/page/UnsupportedBrowser.ftl", new HashMap());
    }

    private boolean checkAppCacheEnabled(HttpServletRequest req) {
        // for browsers that only support database synchronisation via gears at
        // this point,
        // we would rather use gears managed resources stores than HTML5
        // appcache
        // so that we only have to display one permission
        // (this really only applies to FF <= 3.6 right now)
        UserAgentProvider userAgentProvider = new UserAgentProvider();
        return !userAgentProvider.canSupportGears(req);
    }

}
