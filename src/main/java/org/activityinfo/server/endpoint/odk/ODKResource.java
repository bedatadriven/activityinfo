package org.activityinfo.server.endpoint.odk;

import java.util.logging.Logger;

import javax.inject.Provider;
import javax.persistence.EntityManager;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.activityinfo.server.authentication.ServerSideAuthProvider;
import org.activityinfo.server.command.DispatcherSync;
import org.activityinfo.server.database.hibernate.dao.UserPermissionDAO;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.server.util.config.DeploymentConfiguration;
import org.activityinfo.shared.auth.AuthenticatedUser;

import com.google.inject.Inject;

public abstract class ODKResource {
    protected final Logger LOGGER = Logger.getLogger(this.getClass().getName());

    @Inject
    protected final DispatcherSync dispatcher = null;
    @Inject
    protected final Provider<EntityManager> entityManager = null;
    @Inject
    protected final Provider<UserPermissionDAO> userPermissionDAO = null;
    @Inject
    protected final ServerSideAuthProvider auth = null;
    @Inject
    protected final DeploymentConfiguration config = null;

    protected boolean enforceAuthorization() {
        if (getUser().isAnonymous()) {
            // do we have a dummy userid configured?
            String odkDebugAuthorizationUserId = config.getProperty("odk.debug.authorization.userid");
            if (odkDebugAuthorizationUserId != null) {
                int userId = Integer.parseInt(odkDebugAuthorizationUserId);
                if (userId > 0) {
                    // if so, we're assuming that user is authorized.
                    auth.set(entityManager.get().find(User.class, userId));
                    return false;
                }
            }
            // otherwise ask for (basic) authentication
            return true;
        } else {
            // authorized user, continue
            return false;
        }
    }

    protected AuthenticatedUser getUser() {
        return auth.get();
    }

    protected Response askAuthentication() {
        return Response.status(401).header("WWW-Authenticate", "Basic realm=\"Activityinfo\"").build();
    }

    protected Response badRequest(String msg) {
        return Response.status(Status.BAD_REQUEST).entity(msg).type(MediaType.TEXT_PLAIN).build();
    }

    protected Response notFound(String msg) {
        return Response.status(Status.NOT_FOUND).entity(msg).type(MediaType.TEXT_PLAIN).build();
    }

}
