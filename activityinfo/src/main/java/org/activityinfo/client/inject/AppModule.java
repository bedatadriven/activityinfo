package org.activityinfo.client.inject;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;
import org.activityinfo.client.EventBus;
import org.activityinfo.client.LoggingEventBus;
import org.activityinfo.client.PlaceSerializer;
import org.activityinfo.client.dispatch.DispatchEventSource;
import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.dispatch.remote.Authentication;
import org.activityinfo.client.dispatch.remote.RemoteDispatcher;
import org.activityinfo.client.page.FrameSetPresenter;
import org.activityinfo.client.page.app.AppFrameSet;
import org.activityinfo.client.page.charts.ChartPage;
import org.activityinfo.client.page.charts.Charter;
import org.activityinfo.client.page.common.GalleryPage;
import org.activityinfo.client.page.common.GalleryView;
import org.activityinfo.client.util.state.GXTStateManager;
import org.activityinfo.client.util.state.IStateManager;
import org.activityinfo.shared.command.RemoteCommandServiceAsync;

public class AppModule extends AbstractGinModule {

    @Override
    protected void configure() {

        bind(Authentication.class).toProvider(AuthProvider.class).in(Singleton.class);
        bind(RemoteCommandServiceAsync.class).toProvider(RemoteServiceProvider.class).in(Singleton.class);
        bind(Dispatcher.class).to(RemoteDispatcher.class);
        bind(DispatchEventSource.class).to(RemoteDispatcher.class);
        bind(PlaceSerializer.class).in(Singleton.class);
        bind(EventBus.class).to(LoggingEventBus.class).in(Singleton.class);

        bind(IStateManager.class).to(GXTStateManager.class);

//      eager singletson seem to cause program
//        bind(PageManager.class).asEagerSingleton();
//        bind(HistoryManager.class).asEagerSingleton();

        bind(FrameSetPresenter.class).annotatedWith(Root.class).to(AppFrameSet.class);

        bind(GalleryView.class).to(GalleryPage.class);

        bind(Charter.View.class).to(ChartPage.class);

    }
}
