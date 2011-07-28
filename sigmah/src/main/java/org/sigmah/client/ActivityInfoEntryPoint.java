/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client;

import com.allen_sauer.gwt.log.client.Log;
import com.bedatadriven.rebar.appcache.client.AppCacheFactory;
import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.util.Theme;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.gears.client.Factory;

import org.sigmah.client.inject.AppInjector;
import org.sigmah.client.offline.AuthTokenUtil;


/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ActivityInfoEntryPoint implements EntryPoint {

    /**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {

        Log.info("Application: onModuleLoad starting");
        Log.info("Application Permutation: " + GWT.getPermutationStrongName());

        if(!GWT.isScript()) {
            Log.setCurrentLogLevel(Log.LOG_LEVEL_TRACE);
        }
        if(Log.isErrorEnabled()) {
            GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {
                @Override
                public void onUncaughtException(Throwable e) {
                    Log.error("Uncaught exception", e);
                }
            });
        }

		GXT.setDefaultTheme(Theme.BLUE, true);

        Log.trace("Application: GXT theme set");

        AppInjector injector = GWT.create(AppInjector.class);

        // this isn't strictly necessary,
        // the cookie's expiration date should be properly set upon offline installation,
        // but double check here to make sure we can check for the manifest later
        AuthTokenUtil.maybeEnsurePersistentCookie(injector.getAuthentication());

        injector.createWelcomeLoader();
        injector.createDataEntryLoader();
        injector.createChartLoader();
        injector.createReportLoader();
        injector.createPivotLoader();
        injector.createMapLoader();
        injector.createConfigLoader();

        injector.getUsageTracker();
        injector.getHistoryManager();
        injector.getDownloadManager();
        
        if(isOfflineModeSupported()) {
        	injector.createOfflinePresenter();
        } else {
        	injector.createUnsupportedOfflinePresenter();
        }

        createCaches(injector);

        Log.info("Application: everyone plugged, firing Init event");

        injector.getEventBus().fireEvent(AppEvents.Init);
    }

    private boolean isOfflineModeSupported() {
    	// Gears is currently required for offline mode
		return Factory.getInstance() != null;
	}

	protected void createCaches(AppInjector injector) {
        injector.createSchemaCache();
        injector.createAdminCache();
    }
}
