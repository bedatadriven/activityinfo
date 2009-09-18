package org.activityinfo.client.inject;

import com.google.gwt.inject.client.Ginjector;
import com.google.gwt.inject.client.GinModules;

import org.activityinfo.client.EventBus;
import org.activityinfo.client.HistoryManager;
import org.activityinfo.client.PlaceSerializer;
import org.activityinfo.client.command.CommandService;
import org.activityinfo.client.command.cache.AdminEntityCache;
import org.activityinfo.client.command.cache.SchemaCache;
import org.activityinfo.client.page.PageManager;
import org.activityinfo.client.page.PagePresenter;
import org.activityinfo.client.page.app.AppFrameSet;
import org.activityinfo.client.page.charts.ChartLoader;
import org.activityinfo.client.page.charts.Charter;
import org.activityinfo.client.page.config.*;
import org.activityinfo.client.page.config.design.Designer;
import org.activityinfo.client.page.entry.DataEntryLoader;
import org.activityinfo.client.page.entry.DataEntryNavigator;
import org.activityinfo.client.page.map.MapHomePage;
import org.activityinfo.client.page.map.MapLoader;
import org.activityinfo.client.page.map.SingleMapForm;
import org.activityinfo.client.page.report.ReportHomePresenter;
import org.activityinfo.client.page.report.ReportLoader;
import org.activityinfo.client.page.table.PivotPageLoader;
import org.activityinfo.client.page.table.PivotPresenter;
import org.activityinfo.client.page.welcome.Welcome;
import org.activityinfo.client.page.welcome.WelcomeLoader;
import org.activityinfo.client.util.IStateManager;
import org.activityinfo.shared.i18n.UIConstants;

@GinModules(AppModule.class)
public interface AppInjector extends Ginjector {

    AppFrameSet getAppFrameSet();

    PageManager getPageManager();

    HistoryManager createHistoryManager();

    EventBus getEventBus();

    CommandService getService();
    IStateManager getStateManager();
    PlaceSerializer getPlaceSerializer();

    Welcome getWelcomePage();

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
    PivotPageLoader createPivotLoader();
    MapLoader createMapLoader();
    ChartLoader createChartLoader();
    ConfigLoader createConfigLoader();
    ReportLoader createReportLoader();

    SchemaCache createSchemaCache();
    AdminEntityCache createAdminEntityCache();


    PivotPresenter getPivotPresenter();

    MapHomePage getMapHome();

    SingleMapForm getSingleMapForm();

    UIConstants getMessages();

    Charter getCharter();
}
