package org.activityinfo.server.endpoint.odk;

import javax.inject.Provider;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.activityinfo.shared.auth.AuthenticatedUser;

import com.google.inject.Inject;
import com.sun.jersey.api.core.InjectParam;
import com.sun.jersey.multipart.FormDataParam;

@Path("/submission")
public class FormSubmissionResource extends ODKResource {
    private final Provider<FormParser> formParser;

    @Inject
    public FormSubmissionResource(Provider<FormParser> formParser) {
        this.formParser = formParser;
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.TEXT_XML)
    public Response submit(@InjectParam AuthenticatedUser user, @FormDataParam("xml_submission_file") String xml)
        throws Exception {

        authorize();

        LOGGER.finer("ODK form submitted by user " + user.getEmail());

        SiteFormData data = formParser.get().parse(xml);
        if (data == null) {
            return error("Problem parsing submission XML");
        }

        if (data.getActivity() == 0 || data.getPartner() == 0 || data.getGps().isEmpty() ||
            data.getDate1() == null || data.getDate2() == null || data.getDate2().before(data.getDate1())) {
            return error("Problem validating submission XML");
        }

        try {
            LOGGER.info(data.toString());
            // create site

        } catch (Exception e) {
            e.printStackTrace();
            throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR);
        }
        return Response.status(Status.CREATED).build();
    }
}
