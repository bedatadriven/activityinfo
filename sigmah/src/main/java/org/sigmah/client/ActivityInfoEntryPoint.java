/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client;


import java.util.HashMap;

import org.sigmah.client.inject.AppInjector;
import org.sigmah.client.offline.AuthTokenUtil;
import org.sigmah.client.offline.ui.PromptOfflineDialog;

import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.state.Provider;
import com.extjs.gxt.ui.client.state.StateManager;
import com.extjs.gxt.ui.client.util.Theme;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;


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
		StateManager.get().setProvider(new Provider() {
			HashMap<String, String> inMemorySessionStateMap = new HashMap<String, String>(); 
			
			@Override
			protected void setValue(String name, String value) {
				inMemorySessionStateMap.put(name, value);
			}
			
			@Override
			protected String getValue(String name) {
				return inMemorySessionStateMap.get(name);
			}
			
			@Override
			protected void clearKey(String name) {
				inMemorySessionStateMap.remove(name);
			}
		});

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
        injector.createDashboardLoader();
        injector.createSearchLoader();

        injector.getUsageTracker();
        injector.getHistoryManager();
        injector.getDownloadManager();
        
    	injector.createOfflinePresenter();

        createCaches(injector);

        Log.info("Application: everyone plugged, firing Init event");

        injector.getEventBus().fireEvent(AppEvents.Init);
    }
	
	protected void createCaches(AppInjector injector) {
        injector.createSchemaCache();
        injector.createAdminCache();
    }
}
