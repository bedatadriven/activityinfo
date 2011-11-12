package org.activityinfo.embed.client;

import org.sigmah.client.inject.AppInjector;
import org.sigmah.client.page.Page;
import org.sigmah.shared.dto.AnonymousUser;

import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.util.Theme;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window.Location;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;

public class EmbedEntryPoint implements EntryPoint {

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		
		Log.info("Application: onModuleLoad starting");
		Log.info("Application Permutation: " + GWT.getPermutationStrongName());

		if (!GWT.isScript()) {
			Log.setCurrentLogLevel(Log.LOG_LEVEL_TRACE);
		}
		if (Log.isErrorEnabled()) {
			GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {
				@Override
				public void onUncaughtException(Throwable e) {
					Log.error("Uncaught exception", e);
				}
			});
		}

		GXT.setDefaultTheme(Theme.BLUE, true);

		AnonymousUser.createCookiesForAnonymousUser();
		
        final AppInjector injector = GWT.create(AppInjector.class);

        int databaseId = Integer.valueOf(Location.getParameter("databaseId")); 
        loadSites(injector, databaseId);
		
	}
	
	private void loadSites(AppInjector injector, int databaseId){
		
		SitesList sites = new SitesList(injector,databaseId);
		RootPanel.get().add(sites);
	}
}
