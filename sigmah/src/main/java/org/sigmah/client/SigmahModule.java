/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;
import org.sigmah.client.dispatch.DispatchEventSource;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.dispatch.remote.Authentication;
import org.sigmah.client.dispatch.remote.RemoteDispatcher;
import org.sigmah.client.inject.RemoteServiceProvider;
import org.sigmah.client.inject.Root;
import org.sigmah.client.inject.SigmahAuthProvider;
import org.sigmah.client.page.Frame;
import org.sigmah.client.page.PageStateSerializer;
import org.sigmah.client.util.state.GXTStateManager;
import org.sigmah.client.util.state.IStateManager;
import org.sigmah.shared.command.RemoteCommandServiceAsync;

/**
 * Gin configuration module for Sigmah.
 * @author rca
 */
public class SigmahModule extends AbstractGinModule {

    /**
     * Configures the Gin injector.
     */
    @Override
    protected void configure() {

        bind(Authentication.class).toProvider(SigmahAuthProvider.class).in(Singleton.class);
        bind(RemoteCommandServiceAsync.class).toProvider(RemoteServiceProvider.class).in(Singleton.class);
        bind(Dispatcher.class).to(RemoteDispatcher.class);
        bind(DispatchEventSource.class).to(RemoteDispatcher.class);
        bind(PageStateSerializer.class).in(Singleton.class);
        bind(EventBus.class).to(LoggingEventBus.class).in(Singleton.class);
        bind(IStateManager.class).to(GXTStateManager.class);

        bind(Frame.class).annotatedWith(Root.class).to(SigmahAppFrame.class);
    }
}
