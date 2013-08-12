package org.activityinfo.server.endpoint.odk;

import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.activityinfo.server.command.DispatcherSync;
import org.activityinfo.shared.auth.AuthenticatedUser;
import org.activityinfo.shared.command.GetSchema;
import org.activityinfo.shared.dto.ActivityDTO;
import org.activityinfo.shared.dto.SchemaDTO;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.sun.jersey.api.core.InjectParam;
import com.sun.jersey.api.view.Viewable;

@Path("/form")
public class FormResource {
    private static final Logger LOGGER = Logger.getLogger(FormResource.class.getName());

    private final DispatcherSync dispatcher;
    private final Provider<EntityManager> entityManager;

    @Inject
    public FormResource(DispatcherSync dispatcher, Provider<EntityManager> entityManager) {
        this.dispatcher = dispatcher;
        this.entityManager = entityManager;
    }

    @GET
    @Produces(MediaType.TEXT_XML)
    public Response form(@InjectParam AuthenticatedUser user, @QueryParam("id") int id) throws Exception {
        if (user.isAnonymous()) {
            throw new WebApplicationException(Status.FORBIDDEN);
        }

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
