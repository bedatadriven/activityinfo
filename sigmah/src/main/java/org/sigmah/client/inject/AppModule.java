/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.inject;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;
import org.sigmah.client.EventBus;
import org.sigmah.client.LoggingEventBus;
import org.sigmah.client.dispatch.DispatchEventSource;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.dispatch.RemoteServiceProvider;
import org.sigmah.client.dispatch.SwitchingDispatcher;
import org.sigmah.client.dispatch.remote.Authentication;
import org.sigmah.client.dispatch.remote.Direct;
import org.sigmah.client.dispatch.remote.DirectDispatcher;
import org.sigmah.client.dispatch.remote.RemoteDispatcher;
import org.sigmah.client.page.Frame;
import org.sigmah.client.page.PageStateSerializer;
import org.sigmah.client.page.app.AppFrameSet;
import org.sigmah.client.page.charts.ChartPage;
import org.sigmah.client.page.charts.ChartPagePresenter;
import org.sigmah.client.page.common.GalleryPage;
import org.sigmah.client.page.common.GalleryView;
import org.sigmah.client.util.state.GXTStateManager;
import org.sigmah.client.util.state.IStateManager;
import org.sigmah.shared.command.RemoteCommandServiceAsync;

public class AppModule extends AbstractGinModule {

    @Override
    protected void configure() {
        bind(Authentication.class).toProvider(AuthProvider.class).in(Singleton.class);
        bind(RemoteCommandServiceAsync.class).toProvider(RemoteServiceProvider.class).in(Singleton.class);
        bind(Dispatcher.class).to(SwitchingDispatcher.class).in(Singleton.class);
        bind(Dispatcher.class).annotatedWith(Direct.class).to(DirectDispatcher.class).in(Singleton.class);
        bind(DispatchEventSource.class).to(RemoteDispatcher.class);
        bind(PageStateSerializer.class).in(Singleton.class);
        bind(EventBus.class).to(LoggingEventBus.class).in(Singleton.class);

        bind(IStateManager.class).to(GXTStateManager.class);
        bind(Frame.class).annotatedWith(Root.class).to(AppFrameSet.class);
        bind(GalleryView.class).to(GalleryPage.class);
        bind(ChartPagePresenter.View.class).to(ChartPage.class);
    }
}
