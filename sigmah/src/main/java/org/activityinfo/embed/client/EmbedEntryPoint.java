package org.activityinfo.embed.client;

import org.sigmah.shared.dto.AnonymousUser;

import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.util.Theme;
import com.extjs.gxt.ui.client.widget.Viewport;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window.Location;
import com.google.gwt.user.client.ui.RootPanel;

public class EmbedEntryPoint implements EntryPoint {

	/**
	 * This is the entry point method.
	 */
	@Override
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
		
        final EmbedInjector injector = GWT.create(EmbedInjector.class);

        int activityId = Integer.valueOf(Location.getParameter("activityId")); 
        
		SitesList sites = injector.getSitesList();
		sites.load(activityId);

        Viewport viewport = new Viewport();
        viewport.add(sites);
        
        RootPanel.get().add(viewport);        
	}
}
