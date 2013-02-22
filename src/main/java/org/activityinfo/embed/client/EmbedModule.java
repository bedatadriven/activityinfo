package org.activityinfo.embed.client;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import org.activityinfo.client.EventBus;
import org.activityinfo.client.LoggingEventBus;
import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.dispatch.RemoteServiceProvider;
import org.activityinfo.client.dispatch.remote.IncompatibleRemoteHandler;
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
        bind(RemoteCommandServiceAsync.class).toProvider(
            RemoteServiceProvider.class).in(Singleton.class);
        bind(Dispatcher.class).to(RemoteDispatcher.class).in(Singleton.class);
        bind(EventBus.class).to(LoggingEventBus.class).in(Singleton.class);
        bind(StateProvider.class).to(GxtStateProvider.class);
        bind(IncompatibleRemoteHandler.class).to(
            SimpleIncompatibleRemoteHandler.class);
    }

    @Provides
    public AuthenticatedUser provideAuth() {
        return AuthenticatedUser.getAnonymous(LocaleInfo.getCurrentLocale());
    }
}
