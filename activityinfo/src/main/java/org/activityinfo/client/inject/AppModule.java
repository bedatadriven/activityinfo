package org.activityinfo.client.inject;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;
import org.activityinfo.client.EventBus;
import org.activityinfo.client.HistoryManager;
import org.activityinfo.client.LoggingEventBus;
import org.activityinfo.client.PlaceSerializer;
import org.activityinfo.client.command.Authentication;
import org.activityinfo.client.command.CommandEventSource;
import org.activityinfo.client.command.CommandService;
import org.activityinfo.client.command.CommandServiceImpl;
import org.activityinfo.client.page.FrameSetPresenter;
import org.activityinfo.client.page.PageManager;
import org.activityinfo.client.page.app.AppFrameSet;
import org.activityinfo.client.page.charts.ChartPage;
import org.activityinfo.client.page.charts.Charter;
import org.activityinfo.client.page.common.GalleryPage;
import org.activityinfo.client.page.common.GalleryView;
import org.activityinfo.client.util.GWTTimerImpl;
import org.activityinfo.client.util.GxtStateManagerImpl;
import org.activityinfo.client.util.IStateManager;
import org.activityinfo.client.util.ITimer;
import org.activityinfo.shared.command.RemoteCommandServiceAsync;

public class AppModule extends AbstractGinModule {

    @Override
    protected void configure() {

        bind(Authentication.class).toProvider(AuthProvider.class).in(Singleton.class);
        bind(RemoteCommandServiceAsync.class).toProvider(RemoteServiceProvider.class).in(Singleton.class);
        bind(CommandService.class).to(CommandServiceImpl.class);
        bind(CommandEventSource.class).to(CommandServiceImpl.class);
        bind(PlaceSerializer.class).in(Singleton.class);
        bind(EventBus.class).to(LoggingEventBus.class).in(Singleton.class);

        bind(IStateManager.class).to(GxtStateManagerImpl.class);
        bind(ITimer.class).to(GWTTimerImpl.class);

        bind(PageManager.class).asEagerSingleton();
        bind(HistoryManager.class).asEagerSingleton();

        bind(FrameSetPresenter.class).annotatedWith(Root.class).to(AppFrameSet.class);

        bind(GalleryView.class).to(GalleryPage.class);

        bind(Charter.View.class).to(ChartPage.class);

    }
}
