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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;

import javax.persistence.EntityManager;
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
import org.activityinfo.server.database.hibernate.entity.Domain;
import org.activityinfo.server.login.model.HostPageModel;
import org.activityinfo.server.login.model.RootPageModel;
import org.activityinfo.server.util.config.DeploymentConfiguration;
import org.activityinfo.server.util.jaxrs.JaxRsIO;
import org.activityinfo.server.util.logging.LogException;
import org.activityinfo.server.util.logging.LogSlow;

import com.bedatadriven.rebar.appcache.server.UserAgentProvider;
import com.google.appengine.api.files.FileService;
import com.google.appengine.api.files.FileServiceFactory;
import com.google.appengine.api.files.LockException;
import com.google.inject.Inject;
import com.sun.jersey.api.view.Viewable;

@Path(HostController.ENDPOINT)
public class HostController {
    public static final String ENDPOINT = "/";

    private final DeploymentConfiguration deployConfig;
    private final ServerSideAuthProvider authProvider;
    private final EntityManager entityManager;

    private FileService fileService = FileServiceFactory.getFileService();

    @Inject
    public HostController(DeploymentConfiguration deployConfig,
        ServerSideAuthProvider authProvider,
        EntityManager entityManager) {
        super();
        this.deployConfig = deployConfig;
        this.authProvider = authProvider;
        this.entityManager = entityManager;
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @LogException(emailAlert = true)
    public Response getHostPage(@Context UriInfo uri,
        @Context HttpServletRequest req,
        @QueryParam("redirect") boolean redirect) throws Exception {

        String host = req.getServerName();
        Domain domain = entityManager.find(Domain.class, host);

        if (!authProvider.isAuthenticated()) {
            // If the request came from a branded domain, serve the custom welcome page.
            if (domain != null) {
                return brandedDomainPage(domain);
            } else {
                // Otherwise, go to the default ActivityInfo root page
                return Response
                    .ok(new RootPageModel().asViewable())
                    .type(MediaType.TEXT_HTML)
                    .cacheControl(CacheControl.valueOf("no-cache"))
                    .build();
            }
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
        if (domain != null) {
            model.setHost(host);
            model.setTitle(domain.getTitle());
            model.setResourceBasePath(domain.getResourceBasePath());
        }

        return Response.ok(model.asViewable())
            .type(MediaType.TEXT_HTML)
            .cacheControl(CacheControl.valueOf("no-cache"))
            .build();
    }
    
    @SuppressWarnings("deprecation")
    @LogSlow(threshold = 100)
    private Response brandedDomainPage(Domain domain) throws FileNotFoundException, LockException, IOException {
        URL page = domain.getGCSPageURL();
        final InputStream is = page.openStream();
        return JaxRsIO.stream(is);
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
