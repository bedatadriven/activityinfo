package org.sigmah.client.inject;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;
import org.sigmah.client.EventBus;
import org.sigmah.client.LoggingEventBus;
import org.sigmah.client.dispatch.DispatchEventSource;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.dispatch.remote.Authentication;
import org.sigmah.client.dispatch.remote.RemoteDispatcher;
import org.sigmah.client.page.Frame;
import org.sigmah.client.page.PageStateSerializer;
import org.sigmah.client.page.app.AppFrameSet;
import org.sigmah.client.page.charts.ChartPage;
import org.sigmah.client.page.charts.Charter;
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
        bind(Dispatcher.class).to(RemoteDispatcher.class);
        bind(DispatchEventSource.class).to(RemoteDispatcher.class);
        bind(PageStateSerializer.class).in(Singleton.class);
        bind(EventBus.class).to(LoggingEventBus.class).in(Singleton.class);

        bind(IStateManager.class).to(GXTStateManager.class);

//      eager singletson seem to cause program
//        bind(PageManager.class).asEagerSingleton();
//        bind(HistoryManager.class).asEagerSingleton();

        bind(Frame.class).annotatedWith(Root.class).to(AppFrameSet.class);

        bind(GalleryView.class).to(GalleryPage.class);

        bind(Charter.View.class).to(ChartPage.class);

    }
}
