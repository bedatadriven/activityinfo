package org.activityinfo.client.inject;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;

import org.activityinfo.client.EventBus;
import org.activityinfo.client.LoggingEventBus;
import org.activityinfo.client.PlaceSerializer;
import org.activityinfo.client.command.Authentication;
import org.activityinfo.client.command.CommandEventSource;
import org.activityinfo.client.command.CommandService;
import org.activityinfo.client.command.CommandServiceImpl;
import org.activityinfo.client.page.FrameSetPresenter;
import org.activityinfo.client.page.PageManager;
import org.activityinfo.client.page.charts.Charter;
import org.activityinfo.client.page.charts.ChartPage;
import org.activityinfo.client.page.table.PivotPage;
import org.activityinfo.client.page.table.PivotPresenter;
import org.activityinfo.client.page.app.AppFrameSet;
import org.activityinfo.client.page.app.AppFrameSetPresenter;
import org.activityinfo.client.page.base.GalleryPage;
import org.activityinfo.client.page.base.GalleryView;
import org.activityinfo.client.page.config.*;
import org.activityinfo.client.page.config.design.DesignTree;
import org.activityinfo.client.page.config.design.Designer;
import org.activityinfo.client.page.report.ReportGrid;
import org.activityinfo.client.page.report.ReportHomePresenter;
import org.activityinfo.client.page.report.ReportPreview;
import org.activityinfo.client.page.report.ReportPreviewPresenter;
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

        bind(PageManager.class).in(Singleton.class);

        bind(IStateManager.class).to(GxtStateManagerImpl.class);
        bind(ITimer.class).to(GWTTimerImpl.class);

        bind(FrameSetPresenter.class).annotatedWith(Root.class).to(AppFrameSetPresenter.class);

        bind(GalleryView.class).to(GalleryPage.class);

        // I would prefer to see these bound with @ImplementedBy
        // but this is not currently supported in GIN
        bind(AppFrameSetPresenter.View.class).to(AppFrameSet.class);

        bind(AccountEditor.View.class).to(AccountPanel.class);
        bind(DbListPresenter.View.class).to(DbListGrid.class);
        bind(DbUserEditor.View.class).to(DbUserGrid.class);
        bind(DbPartnerEditor.View.class).to(DbPartnerGrid.class);
        bind(Designer.View.class).to(DesignTree.class);

        bind(ReportHomePresenter.View.class).to(ReportGrid.class);

        bind(ReportPreviewPresenter.View.class).to(ReportPreview.class);

        bind(PivotPresenter.View.class).to(PivotPage.class);

        bind(Charter.View.class).to(ChartPage.class);
    }
}
