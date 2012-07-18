package org.activityinfo.embed.client;

import org.activityinfo.client.EventBus;
import org.activityinfo.client.LoggingEventBus;
import org.activityinfo.client.dispatch.DispatchEventSource;
import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.dispatch.RemoteServiceProvider;
import org.activityinfo.client.dispatch.remote.IncompatibleRemoteHandler;
import org.activityinfo.client.dispatch.remote.MergingDispatcher;
import org.activityinfo.client.dispatch.remote.RemoteDispatcher;
import org.activityinfo.client.util.state.GxtStateProvider;
import org.activityinfo.client.util.state.StateProvider;
import org.activityinfo.shared.auth.AuthenticatedUser;
import org.activityinfo.shared.command.RemoteCommandServiceAsync;

import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

public class EmbedModule extends AbstractGinModule {

	@Override
	protected void configure() {
		bind(RemoteCommandServiceAsync.class).toProvider(RemoteServiceProvider.class).in(Singleton.class);
		bind(Dispatcher.class).to(RemoteDispatcher.class).in(Singleton.class);
		bind(EventBus.class).to(LoggingEventBus.class).in(Singleton.class);
		bind(StateProvider.class).to(GxtStateProvider.class);
		bind(IncompatibleRemoteHandler.class).to(SimpleIncompatibleRemoteHandler.class);
	}
	
	@Provides
	public AuthenticatedUser provideAuth() {
        return AuthenticatedUser.getAnonymous(LocaleInfo.getCurrentLocale());
	}
}
