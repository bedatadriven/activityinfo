package org.activityinfo.server.endpoint.odk;

import java.util.List;

import javax.inject.Provider;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.activityinfo.client.local.command.handler.KeyGenerator;
import org.activityinfo.server.database.hibernate.dao.Geocoder;
import org.activityinfo.server.database.hibernate.entity.AdminEntity;
import org.activityinfo.server.database.hibernate.entity.Location;
import org.activityinfo.server.endpoint.odk.SiteFormData.FormAttributeGroup;
import org.activityinfo.server.endpoint.odk.SiteFormData.FormIndicator;
import org.activityinfo.shared.auth.AuthenticatedUser;
import org.activityinfo.shared.command.CreateSite;
import org.activityinfo.shared.command.GetSchema;
import org.activityinfo.shared.dto.ActivityDTO;
import org.activityinfo.shared.dto.AttributeGroupDTO;
import org.activityinfo.shared.dto.SchemaDTO;
import org.activityinfo.shared.dto.SiteDTO;
import org.activityinfo.shared.util.CollectionUtil;

import com.google.inject.Inject;
import com.sun.jersey.api.core.InjectParam;
import com.sun.jersey.multipart.FormDataParam;

@Path("/submission")
public class FormSubmissionResource extends ODKResource {
    private final Provider<FormParser> formParser;
    private final Geocoder geocoder;

    @Inject
    public FormSubmissionResource(Provider<FormParser> formParser, Geocoder geocoder) {
        this.formParser = formParser;
        this.geocoder = geocoder;
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.TEXT_XML)
    public Response submit(@InjectParam AuthenticatedUser user, @FormDataParam("xml_submission_file") String xml)
        throws Exception {

        if (AuthenticatedUser.isAnonymous(user)) {
            if (bypassAuthorization()) {
                setBypassUser();
            } else {
                return Response.status(401).header("WWW-Authenticate", "Basic realm=\"Activityinfo\"").build();
            }
        }

        LOGGER.finer("ODK form submitted by user " + user.getEmail());

        // parse
        SiteFormData data = formParser.get().parse(xml);
        if (data == null) {
            return badRequest("Problem parsing submission XML");
        }

        // basic validation
        if (data.getActivity() == 0 || data.getPartner() == 0 ||
            data.getLatitude() == 999 || data.getLongitude() == 999 ||
            data.getDate1() == null || data.getDate2() == null || data.getDate2().before(data.getDate1())) {
            return badRequest("Problem validating submission XML");
        }

        // check if activity exists
        SchemaDTO schemaDTO = dispatcher.execute(new GetSchema());
        ActivityDTO activity = schemaDTO.getActivityById(data.getActivity());
        if (activity == null) {
            return notFound("Unknown activity");
        }

        // create site
        try {
            createSite(data, schemaDTO, activity);
        } catch (Exception e) {
            e.printStackTrace();
            throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR);
        }
        return Response.status(Status.CREATED).build();
    }

    private void createSite(SiteFormData data, SchemaDTO schemaDTO, ActivityDTO activity) {
        final SiteDTO site = new SiteDTO();
        site.setId(new KeyGenerator().generateInt());
        site.setActivityId(data.getActivity());

        if (activity.getReportingFrequency() == ActivityDTO.REPORT_ONCE) {
            site.setReportingPeriodId(new KeyGenerator().generateInt());
        }

        // set activitymodel
        if (activity.getReportingFrequency() == ActivityDTO.REPORT_ONCE) {
            site.setDate1(data.getDate1());
            site.setDate2(data.getDate2());
        }
        site.setPartner(schemaDTO.getPartnerById(data.getPartner()));

        // set location
        int locationId = findLocationId(data);
        site.setLocationId(locationId);

        // set comments
        site.setComments(data.getComments());

        // set attributes
        for (FormAttributeGroup formAttributeGroup : data.getAttributegroups()) {
            AttributeGroupDTO attributeGroup = activity.getAttributeGroupById(formAttributeGroup.getId());
            for (Integer attributeId : attributeGroup.getAttributeIds()) {
                site.setAttributeValue(attributeId, formAttributeGroup.isSelected(attributeId));
            }
        }

        // set indicators
        if (activity.getReportingFrequency() == ActivityDTO.REPORT_ONCE) {
            for (FormIndicator formIndicator : data.getIndicators()) {
                site.setIndicatorValue(formIndicator.getId(), formIndicator.getDoubleValue());
            }
        }

        dispatcher.execute(new CreateSite(site));
    }


    // TODO this is one big hack right now. We need to come up with a better way to do this..
    private int findLocationId(SiteFormData data) {
        LOGGER.finest("finding location for coordinates " + data.getLatitude() + ", " + data.getLongitude());
        List<AdminEntity> adminentities =
            geocoder.geocode(data.getLatitude(), data.getLongitude());

        if (adminentities.isEmpty()) {
            LOGGER.severe("shouldn't happen: no adminentities found for coordinates " +
                data.getLatitude() + ", " + data.getLongitude() + " - using location 2301 (Boma) as a default");
            return 2301;
        }

        // take the first if somehow all adminentities in the list have children
        AdminEntity mostSpecific = adminentities.get(0);
        for (AdminEntity adminentity : adminentities) {
            if (CollectionUtil.isEmpty(adminentity.getChildren())) {
                mostSpecific = adminentity;
                break;
            }
        }

        Location arbitraryLocation = mostSpecific.getLocations().iterator().next();
        return arbitraryLocation.getId();
    }
}
