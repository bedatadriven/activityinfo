package org.activityinfo.server.endpoint.odk;

import java.util.logging.Logger;

import javax.inject.Provider;
import javax.persistence.EntityManager;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.activityinfo.server.authentication.ServerSideAuthProvider;
import org.activityinfo.server.command.DispatcherSync;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.server.util.config.DeploymentConfiguration;

import com.google.inject.Inject;

public abstract class ODKResource {
    protected final Logger LOGGER = Logger.getLogger(this.getClass().getName());

    @Inject
    protected final DispatcherSync dispatcher = null;
    @Inject
    protected final Provider<EntityManager> entityManager = null;
    @Inject
    protected final ServerSideAuthProvider auth = null;

    protected int authorizationUserId = -1;

    @Inject(optional = true)
    public void setProperties(DeploymentConfiguration properties) {
        String odkAuthorizationUserId =
            properties.getProperty("odk.authorization.userid");
        if (odkAuthorizationUserId != null) {
            authorizationUserId = Integer.parseInt(odkAuthorizationUserId);
        }
    }

    protected boolean bypassAuthorization() {
        return authorizationUserId > 0;
    }

    protected void setBypassUser() {
        auth.set(entityManager.get().find(User.class, authorizationUserId));
    }

    protected Response error(String msg) {
        return Response.status(Status.BAD_REQUEST).entity(msg).type(MediaType.TEXT_PLAIN).build();
    }
}
