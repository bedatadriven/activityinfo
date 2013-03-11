package org.activityinfo.server.digest;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Logger;

import javax.mail.MessagingException;
import javax.persistence.EntityManager;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.activityinfo.server.authentication.ServerSideAuthProvider;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.server.mail.MailSender;
import org.activityinfo.server.mail.Message;
import org.activityinfo.server.util.date.DateFormatter;

import com.google.inject.Inject;
import com.google.inject.Provider;

@Path(UserDigestResource.ENDPOINT)
public class UserDigestResource {
    public static final String ENDPOINT = "/tasks/userdigest";
    public static final String PARAM_USER = "u";
    public static final String PARAM_NOW = "n";
    public static final String PARAM_GEO_DAYS = "g";
    public static final String PARAM_GEO_DAYS_DEF = "1";
    public static final String PARAM_ACTIVITY_DAYS = "a";
    public static final String PARAM_ACTIVITY_DAYS_DEF = "7";
    public static final String PARAM_SENDEMAIL = "send";
    public static final String PARAM_SENDEMAIL_DEF = "true";


    private static final Logger LOGGER = Logger
        .getLogger(UserDigestResource.class.getName());


    private final Provider<EntityManager> entityManager;
    private final Provider<MailSender> mailSender;
    private final ServerSideAuthProvider authProvider;

    private final GeoDigestRenderer geoDigestRenderer;
    private final ActivityDigestRenderer activityDigestRenderer;

    @Inject
    public UserDigestResource(Provider<EntityManager> entityManager,
        Provider<MailSender> mailSender,
        ServerSideAuthProvider authProvider,
        GeoDigestRenderer geoDigestRenderer,
        ActivityDigestRenderer activityDigestRenderer) {
        this.entityManager = entityManager;
        this.mailSender = mailSender;
        this.authProvider = authProvider;
        this.geoDigestRenderer = geoDigestRenderer;
        this.activityDigestRenderer = activityDigestRenderer;
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public String createUserDigest(
        @QueryParam(PARAM_USER) int userId,
        @QueryParam(PARAM_NOW) Long now,
        @QueryParam(PARAM_GEO_DAYS) @DefaultValue(PARAM_GEO_DAYS_DEF) int geoDays,
        @QueryParam(PARAM_ACTIVITY_DAYS) @DefaultValue(PARAM_ACTIVITY_DAYS_DEF) int activityDays,
        @QueryParam(PARAM_SENDEMAIL) @DefaultValue(PARAM_SENDEMAIL_DEF) boolean sendEmail)
            throws IOException, MessagingException {

        if (userId <= 0) {
            return "no user specified";
        }

        Date date = now == null ? new Date() : new Date(now);

        User user = entityManager.get().find(User.class, userId);
        authProvider.set(user);

        LOGGER.info("creating digest for " + user.getEmail()
            + " on " + DateFormatter.formatDateTime(date)
            + " for geo period: " + geoDays + " days"
            + " and activity period: " + activityDays + " days."
            + " (sending email: " + sendEmail + ")");

        UserDigestBuilder digest =
            new UserDigestBuilder(geoDigestRenderer, activityDigestRenderer);
        digest.setUser(user);
        digest.setDate(date);
        digest.setGeoDays(geoDays);
        digest.setActivityDays(activityDays);

        Message message = digest.build();
        if (message != null && sendEmail) {
            mailSender.get().send(message);
        }

        return message == null ? "no updates found" : message.getHtmlBody();
    }
}
