package org.activityinfo.server.endpoint.odk;

import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.activityinfo.shared.command.GetSchema;

import com.google.common.collect.Maps;
import com.sun.jersey.api.view.Viewable;

@Path("/formList")
public class FormListResource extends ODKResource {
    @GET
    @Produces(MediaType.TEXT_XML)
    public Response formList(@Context UriInfo info) throws Exception {
        if (enforceAuthorization()) {
            return Response.status(401).header("WWW-Authenticate", "Basic realm=\"Activityinfo\"").build();
        }
        LOGGER.finer("ODK formlist requested by " + getUser().getEmail() + " (" + getUser().getId() + ")");

        Map<String, Object> map = Maps.newHashMap();
        map.put("schema", dispatcher.execute(new GetSchema()));
        map.put("host", info.getBaseUri().toString());

        return Response.ok(new Viewable("/odk/formList.ftl", map)).build();
    }
}
