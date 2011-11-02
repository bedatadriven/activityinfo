/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.inject;

import org.sigmah.client.EventBus;
import org.sigmah.client.LoggingEventBus;
import org.sigmah.client.auth.ClientSideAuthProvider;
import org.sigmah.client.dispatch.DispatchEventSource;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.dispatch.RemoteServiceProvider;
import org.sigmah.client.dispatch.remote.Direct;
import org.sigmah.client.dispatch.remote.DirectDispatcher;
import org.sigmah.client.dispatch.remote.Remote;
import org.sigmah.client.dispatch.remote.RemoteDispatcher;
import org.sigmah.client.offline.OfflineController;
import org.sigmah.client.page.Frame;
import org.sigmah.client.page.PageStateSerializer;
import org.sigmah.client.page.app.AppFrameSet;
import org.sigmah.client.page.charts.ChartPage;
import org.sigmah.client.page.charts.ChartPagePresenter;
import org.sigmah.client.page.common.GalleryPage;
import org.sigmah.client.page.common.GalleryView;
import org.sigmah.client.util.state.GxtStateProvider;
import org.sigmah.client.util.state.StateProvider;
import org.sigmah.shared.auth.AuthenticatedUser;
import org.sigmah.shared.command.RemoteCommandServiceAsync;

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
        bind(ChartPagePresenter.View.class).to(ChartPage.class);
    }
}
