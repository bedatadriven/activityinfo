package org.activityinfo.server.endpoint.odk;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.activityinfo.shared.command.GetSchema;
import org.activityinfo.shared.dto.ActivityDTO;
import org.activityinfo.shared.dto.SchemaDTO;

import com.sun.jersey.api.view.Viewable;

@Path("/form")
public class FormResource extends ODKResource {
    @GET
    @Produces(MediaType.TEXT_XML)
    public Response form(@QueryParam("id") int id) throws Exception {
        if (enforceAuthorization()) {
            return Response.status(401).header("WWW-Authenticate", "Basic realm=\"Activityinfo\"").build();
        }
        LOGGER.finer("ODK form " + id + " requested by " + getUser().getEmail() + " (" + getUser().getId() + ")");

        SchemaDTO schemaDTO = dispatcher.execute(new GetSchema());
        ActivityDTO activity = schemaDTO.getActivityById(id);

        if (activity == null) {
            throw new WebApplicationException(Status.NOT_FOUND);
        }
        if (!activity.getDatabase().isEditAllowed()) {
            throw new WebApplicationException(Status.FORBIDDEN);
        }

        return Response.ok(new Viewable("/odk/form.ftl", activity)).build();
    }
}
