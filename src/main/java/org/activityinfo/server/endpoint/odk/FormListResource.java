package org.activityinfo.server.endpoint.odk;

import java.util.Enumeration;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.activityinfo.server.command.DispatcherSync;
import org.activityinfo.shared.auth.AuthenticatedUser;
import org.activityinfo.shared.command.GetSchema;
import org.activityinfo.shared.dto.SchemaDTO;

import com.google.inject.Inject;
import com.sun.jersey.api.core.InjectParam;
import com.sun.jersey.api.view.Viewable;

@Path("/formList")
public class FormListResource {
    private static final Logger LOGGER = Logger.getLogger(FormListResource.class.getName());

    private final DispatcherSync dispatcher;

    @Inject
    public FormListResource(DispatcherSync dispatcher) {
        this.dispatcher = dispatcher;
    }

    @GET
    @Produces(MediaType.TEXT_XML)
    public Response formList(@InjectParam AuthenticatedUser user, @Context HttpServletRequest request) throws Exception {
        Enumeration e = request.getHeaderNames();
        if (AuthenticatedUser.isAnonymous(user)) {
            return Response.status(401).header("WWW-Authenticate", "Basic realm=\"Activityinfo\"").build();
        }

        SchemaDTO schemaDTO = dispatcher.execute(new GetSchema());
        return Response.ok(new Viewable("/odk/formList.ftl", schemaDTO)).build();
    }
}
