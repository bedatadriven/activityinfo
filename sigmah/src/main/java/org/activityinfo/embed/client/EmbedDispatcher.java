package org.activityinfo.embed.client;

import org.sigmah.client.EventBus;
import org.sigmah.client.dispatch.remote.RemoteDispatcher;
import org.sigmah.shared.auth.AuthenticatedUser;
import org.sigmah.shared.command.RemoteCommandServiceAsync;
import org.sigmah.shared.dto.AnonymousUser;
import com.google.inject.Inject;

/**
 * Dispatcher which sends commands to the server.
 */
public class EmbedDispatcher extends RemoteDispatcher {

    @Inject
    public EmbedDispatcher(RemoteCommandServiceAsync service, EventBus eventBus, AuthenticatedUser authentication) {
    	super(service, eventBus, authentication);
    	authentication.setAuthToken(AnonymousUser.AUTHTOKEN);
    }
}
