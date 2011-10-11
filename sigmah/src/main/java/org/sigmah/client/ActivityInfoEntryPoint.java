/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client;

import java.util.HashMap;

import org.sigmah.client.dispatch.remote.Authentication;
import org.sigmah.client.inject.AppInjector;

import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.state.Provider;
import com.extjs.gxt.ui.client.state.StateManager;
import com.extjs.gxt.ui.client.util.Theme;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.gears.client.Factory;


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

        final AppInjector injector = GWT.create(AppInjector.class);


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
   
        Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			
			@Override
			public void execute() {
		        updateOlarkInfo(injector.getAuthentication());
			}
		});
	}

    private void updateOlarkInfo(Authentication authentication) {
    	try {
	    	OlarkApi.updateEmailAddress(authentication.getEmail());
	    	OlarkApi.updateFullName(authentication.getUserName());
    	} catch(Throwable caught) {
    		Log.debug("failed to update olark info", caught);
    	}
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
