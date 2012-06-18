/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

/**
 * Support classes for the Dependency Injection Framework, grace a Gin
 */
package org.activityinfo.client.inject;

import org.activityinfo.client.EventBus;
import org.activityinfo.client.HistoryManager;
import org.activityinfo.client.UsageTracker;
import org.activityinfo.client.dispatch.remote.cache.AdminEntityCache;
import org.activityinfo.client.dispatch.remote.cache.SchemaCache;
import org.activityinfo.client.offline.OfflineController;
import org.activityinfo.client.offline.OfflineModule;
import org.activityinfo.client.page.config.ConfigLoader;
import org.activityinfo.client.page.config.ConfigModule;
import org.activityinfo.client.page.dashboard.DashboardLoader;
import org.activityinfo.client.page.entry.DataEntryLoader;
import org.activityinfo.client.page.entry.EntryModule;
import org.activityinfo.client.page.report.ReportLoader;
import org.activityinfo.client.page.report.ReportModule;
import org.activityinfo.client.page.search.SearchLoader;
import org.activityinfo.client.page.search.SearchModule;
import org.activityinfo.client.report.editor.map.MapModule;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

/**
 * GIN injector.
 *
 * TODO: having this number of explicit entries is probably not ideal, try to make better use
 * of injection and injecting Provider<>s
 */
@GinModules({
        AppModule.class,
        ReportModule.class,
        EntryModule.class,
        MapModule.class,
        ConfigModule.class,
        OfflineModule.class,
        SearchModule.class
})
public interface AppInjector extends Ginjector {
    EventBus getEventBus();
    HistoryManager getHistoryManager();
    DataEntryLoader createDataEntryLoader();
    ReportLoader createReportLoader();
    ConfigLoader createConfigLoader();
    OfflineController createOfflineController();
    UsageTracker getUsageTracker();
	SearchLoader createSearchLoader();
	DashboardLoader createDashboardLoader();
	
    SchemaCache createSchemaCache();
    AdminEntityCache createAdminCache();
	
}
