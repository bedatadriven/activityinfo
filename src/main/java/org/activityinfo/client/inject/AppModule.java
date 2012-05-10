/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.client.inject;

import org.activityinfo.client.EventBus;
import org.activityinfo.client.LoggingEventBus;
import org.activityinfo.client.authentication.ClientSideAuthProvider;
import org.activityinfo.client.dispatch.DispatchEventSource;
import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.dispatch.RemoteServiceProvider;
import org.activityinfo.client.dispatch.remote.Direct;
import org.activityinfo.client.dispatch.remote.DirectDispatcher;
import org.activityinfo.client.dispatch.remote.Remote;
import org.activityinfo.client.dispatch.remote.RemoteDispatcher;
import org.activityinfo.client.offline.OfflineController;
import org.activityinfo.client.page.Frame;
import org.activityinfo.client.page.PageStateSerializer;
import org.activityinfo.client.page.app.AppFrameSet;
import org.activityinfo.client.page.common.GalleryPage;
import org.activityinfo.client.page.common.GalleryView;
import org.activityinfo.client.util.state.GxtStateProvider;
import org.activityinfo.client.util.state.StateProvider;
import org.activityinfo.shared.auth.AuthenticatedUser;
import org.activityinfo.shared.command.RemoteCommandServiceAsync;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;

public class AppModule extends AbstractGinModule {

    @Override
    protected void configure() {
        bind(AuthenticatedUser.class).toProvider(ClientSideAuthProvider.class);
        bind(RemoteCommandServiceAsync.class).toProvider(RemoteServiceProvider.class).in(Singleton.class);
        bind(Dispatcher.class).to(OfflineController.class).in(Singleton.class);
        bind(Dispatcher.class).annotatedWith(Direct.class).to(DirectDispatcher.class).in(Singleton.class);
        bind(Dispatcher.class).annotatedWith(Remote.class).to(RemoteDispatcher.class).in(Singleton.class);
        bind(DispatchEventSource.class).to(RemoteDispatcher.class);
        bind(PageStateSerializer.class).in(Singleton.class);
        bind(EventBus.class).to(LoggingEventBus.class).in(Singleton.class);

        bind(StateProvider.class).to(GxtStateProvider.class);
        bind(Frame.class).annotatedWith(Root.class).to(AppFrameSet.class);
        bind(GalleryView.class).to(GalleryPage.class);
    }
}
