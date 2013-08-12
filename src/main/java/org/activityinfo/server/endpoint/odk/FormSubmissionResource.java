package org.activityinfo.server.endpoint.odk;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.activityinfo.server.command.DispatcherSync;
import org.activityinfo.shared.auth.AuthenticatedUser;

import com.google.inject.Inject;
import com.sun.jersey.api.core.InjectParam;

@Path("/formSubmission")
public class FormSubmissionResource {
    private static final Logger LOGGER = Logger.getLogger(FormSubmissionResource.class.getName());

    private final DispatcherSync dispatcher;

    @Inject
    public FormSubmissionResource(DispatcherSync dispatcher) {
        this.dispatcher = dispatcher;
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    // MultivaluedMap<String, String> formParams)
    public Response submit(@InjectParam AuthenticatedUser user, @Context HttpServletRequest request)
        throws Exception {
        LOGGER.finer("ODK form submitted by user " + user.getEmail());
        return Response.ok().build();
    }
    

//    private String getRequestAsString(HttpServletRequest request)
//        throws java.io.IOException {
//
//        BufferedReader requestData = new BufferedReader(
//            new InputStreamReader(request.getInputStream()));
//        StringBuffer stringBuffer = new StringBuffer();
//        String line;
//        try {
//            while ((line = requestData.readLine()) != null) {
//                stringBuffer.append(line);
//            }
//        } catch (Exception e) {
//        }
//        return stringBuffer.toString();
//
//    }
}
