/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

/**
 * Support classes for the Dependency Injection Framework, grace a Gin
 */
package org.sigmah.client.inject;

import org.sigmah.client.EventBus;
import org.sigmah.client.HistoryManager;
import org.sigmah.client.UsageTracker;
import org.sigmah.client.dispatch.remote.cache.AdminEntityCache;
import org.sigmah.client.dispatch.remote.cache.SchemaCache;
import org.sigmah.client.offline.OfflineController;
import org.sigmah.client.offline.OfflineModule;
import org.sigmah.client.page.DownloadManager;
import org.sigmah.client.page.config.ConfigLoader;
import org.sigmah.client.page.config.ConfigModule;
import org.sigmah.client.page.dashboard.DashboardLoader;
import org.sigmah.client.page.entry.DataEntryLoader;
import org.sigmah.client.page.entry.EntryModule;
import org.sigmah.client.page.report.ReportLoader;
import org.sigmah.client.page.report.ReportModule;
import org.sigmah.client.page.search.SearchLoader;
import org.sigmah.client.page.search.SearchModule;
import org.sigmah.client.report.editor.map.MapModule;

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
    DownloadManager getDownloadManager();
    UsageTracker getUsageTracker();
	SearchLoader createSearchLoader();
	DashboardLoader createDashboardLoader();
	
    SchemaCache createSchemaCache();
    AdminEntityCache createAdminCache();
	
}
