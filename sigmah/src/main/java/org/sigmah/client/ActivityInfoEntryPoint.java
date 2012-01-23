/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client;



import org.sigmah.client.inject.AppInjector;
import org.sigmah.client.util.state.SafeStateProvider;
import org.sigmah.shared.exception.InvalidAuthTokenException;

import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.state.StateManager;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.Window;


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

		if(!GWT.isScript()) {
			Log.setCurrentLogLevel(Log.LOG_LEVEL_TRACE);
		}
		if(Log.isErrorEnabled()) {
			GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {
				@Override
				public void onUncaughtException(Throwable e) {
					Log.error("Uncaught exception", e);
					if(e instanceof InvalidAuthTokenException) {
						SessionUtil.forceLogin();
					} else {
						Window.alert("Oops, this is quite embarrasing, but there has been an uncaught error: \n\n" + e.getMessage() + "\n\n" +
								"Please let us know at support@bedatadriven.com so we can correct it ASAP!");
					}
				}
			});
		}
		
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			
			@Override
			public void execute() {
				// running as a deferred task will ensure that 
				// this ensures that exceptions will be caught by our uncaught exception handler above. 
				
				doLoad();
			}
		});
	}

	private void doLoad() {
		Log.trace("Application: doLoad() starting...");
		
		// avoid cookie overflow
		StateManager.get().setProvider(new SafeStateProvider());

		

		final AppInjector injector = GWT.create(AppInjector.class);

		injector.createWelcomeLoader();
		injector.createDataEntryLoader();
		injector.createChartLoader();
		injector.createReportLoader();
		injector.createPivotLoader();
		injector.createMapLoader();
		injector.createConfigLoader();
		injector.createSearchLoader();
		injector.createReportDesignLoader();


		injector.getUsageTracker();
		injector.getHistoryManager();
		injector.getDownloadManager();

		injector.createOfflineController();

		createCaches(injector);

		Log.info("Application: everyone plugged, firing Init event");

		injector.getEventBus().fireEvent(AppEvents.INIT);
	}

	protected void createCaches(AppInjector injector) {
		injector.createSchemaCache();
		injector.createAdminCache();
	}
}
