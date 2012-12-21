/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.client;



import org.activityinfo.client.authentication.ClientSideAuthProvider;
import org.activityinfo.client.inject.AppInjector;
import org.activityinfo.client.util.state.SafeStateProvider;

import com.extjs.gxt.ui.client.GXT;
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
	@Override
  public void onModuleLoad() {

        Log.info("Application: onModuleLoad starting");
        Log.info("Application Permutation: " + GWT.getPermutationStrongName());

        try {
        	new ClientSideAuthProvider().get();
        } catch(Exception e) {
        	Log.error("Exception getting client side authentication", e);
        	SessionUtil.forceLogin();
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
		
		// avoid cookie overflow
		StateManager.get().setProvider(new SafeStateProvider());

        Log.trace("Application: GXT theme set");

        final AppInjector injector = GWT.create(AppInjector.class);

        injector.createDashboardLoader();
        injector.createDataEntryLoader();
        injector.createReportLoader();
        injector.createConfigLoader();
        injector.createSearchLoader();
        

        injector.getUsageTracker();
        injector.createMixPanelTracker();
        
        injector.getHistoryManager();
        
    	injector.createOfflineController();	

        createCaches(injector);

        AppCacheMonitor.start();
        
        Log.info("Application: everyone plugged, firing Init event");

        injector.getEventBus().fireEvent(AppEvents.INIT);
	}

	protected void createCaches(AppInjector injector) {
        injector.createSchemaCache();
        injector.createAdminCache();
    }
}
