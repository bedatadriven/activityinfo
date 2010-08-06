/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.dispatch.remote.cache.AdminEntityCache;
import org.sigmah.client.dispatch.remote.cache.SchemaCache;
import org.sigmah.client.i18n.UIConstants;
import org.sigmah.client.page.NavigationHandler;
import org.sigmah.client.page.charts.Charter;
import org.sigmah.client.page.charts.SigmahChartLoader;
import org.sigmah.client.page.config.AccountEditor;
import org.sigmah.client.page.config.ConfigModule;
import org.sigmah.client.page.config.ConfigNavigator;
import org.sigmah.client.page.config.DbConfigPresenter;
import org.sigmah.client.page.config.DbListPage;
import org.sigmah.client.page.config.DbListPresenter;
import org.sigmah.client.page.config.DbPartnerEditor;
import org.sigmah.client.page.config.DbUserEditor;
import org.sigmah.client.page.config.SigmahConfigLoader;
import org.sigmah.client.page.config.design.Designer;
import org.sigmah.client.page.dashboard.DashboardPageLoader;
import org.sigmah.client.page.dashboard.DashboardPresenter;
import org.sigmah.client.page.entry.DataEntryNavigator;
import org.sigmah.client.page.entry.EntryModule;
import org.sigmah.client.page.entry.SigmahDataEntryLoader;
import org.sigmah.client.page.map.SigmahMapLoader;
import org.sigmah.client.page.map.SingleMapForm;
import org.sigmah.client.page.project.ProjectModule;
import org.sigmah.client.page.project.ProjectPageLoader;
import org.sigmah.client.page.project.ProjectPresenter;
import org.sigmah.client.page.report.ReportHomePresenter;
import org.sigmah.client.page.report.ReportModule;
import org.sigmah.client.page.report.ReportPreviewPresenter;
import org.sigmah.client.page.report.SigmahReportLoader;
import org.sigmah.client.page.table.PivotModule;
import org.sigmah.client.page.table.PivotPresenter;
import org.sigmah.client.page.table.SigmahPivotPageLoader;
import org.sigmah.client.util.state.IStateManager;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

@GinModules({SigmahModule.class,
             ProjectModule.class,
             PivotModule.class,
             ReportModule.class,
             EntryModule.class,
             ConfigModule.class})
public interface SigmahInjector extends Ginjector {

    EventBus getEventBus();
    Dispatcher getService();
    NavigationHandler getNavigationHandler();
    HistoryManager getHistoryManager();
    
    // Pages from Sigmah
    DashboardPageLoader registerDashboardPageLoader();
//    ProjectListPageLoader registerProjectListPageLoader();
    ProjectPageLoader registerProjectPageLoader();
    
    // Required by the 'Dashboard' page
    DashboardPresenter getDashboardPresenter();
    
    // Required by the 'Project' page
    ProjectPresenter getProjectListPresenter();

    // Required by the 'Project' page
    ProjectPresenter getProjectPresenter();
    
    // Pages from ActivityInfo
    SigmahDataEntryLoader registerDataEntryLoader();
    SigmahMapLoader registerMapLoader();
    SigmahChartLoader registerChartLoader();
    SigmahConfigLoader registerConfigLoader();
    SigmahPivotPageLoader registerPivotLoader();
    SigmahReportLoader registerReportLoader();
    
    // Required by the 'Data Entry' page
    DataEntryNavigator getDataEntryNavigator();
    IStateManager getStateManager();
    UIConstants getMessages();
    
    // Required by the 'Maps' page
    SingleMapForm getSingleMapForm();
    
    // Required by the 'Charts' page
    Charter getCharter();
    
    // Required by the 'Report' page
    ReportPreviewPresenter getReportPreviewPresenter();
    ReportHomePresenter getReportHomePresenter();
    
    // Required by the 'Config' page
    ConfigNavigator getConfigNavigator();
    AccountEditor getAccountEditor();
    DbListPresenter getDbListPresenter();
    DbUserEditor getDbUserEditor();
    DbPartnerEditor getDbPartnerEditor();
    Designer getDesigner();
    DbConfigPresenter getDbConfigPresenter();
    DbListPage getDbListPage();
    
    // Required by the 'Pivot' page
    PivotPresenter getPivotPresenter();
    
    // Cache
    SchemaCache createSchemaCache();
    AdminEntityCache createAdminCache();
}
