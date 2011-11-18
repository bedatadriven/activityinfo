package org.activityinfo.embed.client;

import org.sigmah.client.EventBus;
import org.sigmah.client.LoggingEventBus;
import org.sigmah.client.authentication.ClientSideAuthProvider;
import org.sigmah.client.dispatch.DispatchEventSource;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.dispatch.RemoteServiceProvider;
import org.sigmah.client.dispatch.remote.RemoteDispatcher;
import org.sigmah.client.util.state.GxtStateProvider;
import org.sigmah.client.util.state.StateProvider;
import org.sigmah.shared.auth.AuthenticatedUser;
import org.sigmah.shared.command.RemoteCommandServiceAsync;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;

public class EmbedModule extends AbstractGinModule {

	@Override
	protected void configure() {
		bind(AuthenticatedUser.class).toProvider(ClientSideAuthProvider.class);
		bind(RemoteCommandServiceAsync.class).toProvider(RemoteServiceProvider.class).in(Singleton.class);
		bind(Dispatcher.class).to(RemoteDispatcher.class).in(Singleton.class);
		bind(DispatchEventSource.class).to(RemoteDispatcher.class);
		bind(EventBus.class).to(LoggingEventBus.class).in(Singleton.class);
		bind(StateProvider.class).to(GxtStateProvider.class);
	}
}
