package org.activityinfo.server.util.config;

import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.google.common.collect.Maps;
import com.sun.jersey.api.view.Viewable;

import freemarker.template.Configuration;

/**
 * Simple servlet to allow AppEngine administrators to define the configuration
 * properties for this instance. This makes it possible to set config params,
 * like api keys, etc, seperately from the (public) source code.
 * 
 * <p>
 * This servlet stores the text of a properties file to the Datastore
 * 
 */
@Path("/admin/config")
public class AppengineConfigResource {

    public static final String END_POINT = "/admin/config";

    private final Configuration templateCfg;

    @Inject
    public AppengineConfigResource(Configuration templateCfg) {
        super();
        this.templateCfg = templateCfg;
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public Viewable getPage() {
        Map<String, String> model = Maps.newHashMap();
        model.put("currentConfig", AppEngineConfig.getPropertyFile());

        return new Viewable("/page/Config.ftl", model);
    }

    @POST
    public Response update(@Context UriInfo uri,
        @FormParam("config") String config) {
        AppEngineConfig.setPropertyFile(config);

        return Response.seeOther(uri.getRequestUri()).build();
    }

}
