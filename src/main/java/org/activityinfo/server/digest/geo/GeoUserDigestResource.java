package org.activityinfo.server.digest.geo;

import javax.persistence.EntityManager;
import javax.ws.rs.Path;

import org.activityinfo.server.authentication.ServerSideAuthProvider;
import org.activityinfo.server.digest.UserDigestResource;
import org.activityinfo.server.mail.MailSender;

import com.google.inject.Inject;
import com.google.inject.Provider;

@Path(GeoUserDigestResource.ENDPOINT)
public class GeoUserDigestResource extends UserDigestResource {
    public static final String ENDPOINT = "/tasks/geouserdigest";
    public static final int PARAM_DAYS_DEF = 1; // today

    @Inject
    public GeoUserDigestResource(Provider<EntityManager> entityManager,
        Provider<MailSender> mailSender,
        ServerSideAuthProvider authProvider,
        GeoDigestModelBuilder geoDigestModelBuilder,
        GeoDigestRenderer geoDigestRenderer) {
        super(entityManager, mailSender, authProvider, geoDigestModelBuilder, geoDigestRenderer);
    }

    @Override
    public int getDefaultDays() {
        return PARAM_DAYS_DEF;
    }
}
