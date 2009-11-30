/**
 * Support classes for the Dependency Injection Framework, grace a Gin
 */
package org.activityinfo.client.inject;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import org.activityinfo.client.EventBus;
import org.activityinfo.client.HistoryManager;
import org.activityinfo.client.UsageTracker;
import org.activityinfo.client.command.CommandService;
import org.activityinfo.client.command.cache.AdminEntityCache;
import org.activityinfo.client.command.cache.CacheModule;
import org.activityinfo.client.command.cache.SchemaCache;
import org.activityinfo.client.offline.DatabaseStateProvider;
import org.activityinfo.client.offline.OfflineManager;
import org.activityinfo.client.offline.OfflineModule;
import org.activityinfo.client.offline.OfflineSchemaCache;
import org.activityinfo.client.page.DownloadManager;
import org.activityinfo.client.page.charts.ChartLoader;
import org.activityinfo.client.page.charts.Charter;
import org.activityinfo.client.page.config.*;
import org.activityinfo.client.page.config.design.Designer;
import org.activityinfo.client.page.entry.DataEntryLoader;
import org.activityinfo.client.page.entry.DataEntryNavigator;
import org.activityinfo.client.page.entry.EntryModule;
import org.activityinfo.client.page.map.MapHomePage;
import org.activityinfo.client.page.map.MapLoader;
import org.activityinfo.client.page.map.SingleMapForm;
import org.activityinfo.client.page.report.ReportHomePresenter;
import org.activityinfo.client.page.report.ReportLoader;
import org.activityinfo.client.page.report.ReportModule;
import org.activityinfo.client.page.report.ReportPreviewPresenter;
import org.activityinfo.client.page.table.PivotModule;
import org.activityinfo.client.page.table.PivotPageLoader;
import org.activityinfo.client.page.table.PivotPresenter;
import org.activityinfo.client.page.welcome.Welcome;
import org.activityinfo.client.page.welcome.WelcomeLoader;
import org.activityinfo.client.util.IStateManager;
import org.activityinfo.shared.i18n.UIConstants;

@GinModules({
        AppModule.class,
        CacheModule.class,
        ReportModule.class,
        EntryModule.class,
        PivotModule.class,
        ConfigModule.class,
        OfflineModule.class
        })
public interface AppInjector extends Ginjector {

    
    EventBus getEventBus();
    CommandService getService();
    
    Welcome getWelcomePage();
    
    IStateManager getStateManager();
    HistoryManager getHistoryManager();

    DataEntryNavigator getDataEntryNavigator();

    ReportHomePresenter getReportHomePresenter();

    ConfigNavigator getConfigNavigator();
    AccountEditor getAccountEditor();
    DbListPresenter getDbListPresenter();
    DbUserEditor getDbUserEditor();
    DbPartnerEditor getDbPartnerEditor();
    Designer getDesigner();

    WelcomeLoader createWelcomeLoader();
    DataEntryLoader createDataEntryLoader();
    MapLoader createMapLoader();
    ChartLoader createChartLoader();
    ReportLoader createReportLoader();
    ConfigLoader createConfigLoader();
    PivotPageLoader createPivotLoader();
    
    PivotPresenter getPivotPresenter();

    MapHomePage getMapHome();
    
    SingleMapForm getSingleMapForm();

    UIConstants getMessages();

    Charter getCharter();


    ReportPreviewPresenter getReportPreviewPresenter();


    OfflineManager createOfflineManager();
    OfflineSchemaCache createOfflineSchemaCache();

    SchemaCache createSchemaCache();

    AdminEntityCache createAdminCache();


    DatabaseStateProvider getDatabaseStateProvider();

    DownloadManager getDownloadManager();

    DbConfigPresenter getDbConfigPresenter();

    UsageTracker getUsageTracker();
    
}
