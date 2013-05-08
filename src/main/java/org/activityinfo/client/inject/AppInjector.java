/**
 * Support classes for the Dependency Injection Framework, grace a Gin
 */
package org.activityinfo.client.inject;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import org.activityinfo.client.EventBus;
import org.activityinfo.client.HistoryManager;
import org.activityinfo.client.MixPanel;
import org.activityinfo.client.UsageTracker;
import org.activityinfo.client.dispatch.remote.cache.AdminEntityCache;
import org.activityinfo.client.dispatch.remote.cache.SchemaCache;
import org.activityinfo.client.local.LocalController;
import org.activityinfo.client.local.LocalModule;
import org.activityinfo.client.local.ui.PromptOfflineDialog;
import org.activityinfo.client.page.app.AppLoader;
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
 * TODO: having this number of explicit entries is probably not ideal, try to
 * make better use of injection and injecting Provider<>s
 */
@GinModules({
    AppModule.class,
    ReportModule.class,
    EntryModule.class,
    MapModule.class,
    ConfigModule.class,
    LocalModule.class,
    SearchModule.class
})
public interface AppInjector extends Ginjector {
    EventBus getEventBus();

    HistoryManager getHistoryManager();

    DataEntryLoader createDataEntryLoader();

    ReportLoader createReportLoader();

    ConfigLoader createConfigLoader();

    LocalController createOfflineController();

    PromptOfflineDialog createPromptOfflineDialog();

    UsageTracker getUsageTracker();

    SearchLoader createSearchLoader();

    DashboardLoader createDashboardLoader();

    SchemaCache createSchemaCache();

    AdminEntityCache createAdminCache();

    MixPanel createMixPanelTracker();

    AppLoader createAppLoader();
}
