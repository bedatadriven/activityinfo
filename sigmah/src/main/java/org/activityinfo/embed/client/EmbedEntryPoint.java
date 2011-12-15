package org.activityinfo.embed.client;

import org.sigmah.client.page.entry.SiteGridPanel;
import org.sigmah.client.page.entry.grouping.NullGroupingModel;
import org.sigmah.shared.command.Filter;
import org.sigmah.shared.dto.AnonymousUser;
import org.sigmah.shared.report.model.DimensionType;

import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.util.Theme;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.Viewport;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.LayoutData;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window.Location;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

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

		if(Location.getParameter("activityId") == null){
			Log.info("Could not obtain Activity Id!");
			
			Html html = new Html("No activity id provided.");
			addToRootPanel(html);
	    	return;
	    }
		
		GXT.setDefaultTheme(Theme.BLUE, true);

	    final EmbedInjector injector = GWT.create(EmbedInjector.class);
	    	
        int activityId = Integer.valueOf(Location.getParameter("activityId")); 
        
		Filter filter = new Filter();
		filter.addRestriction(DimensionType.Activity, activityId);
		
		SiteGridPanel panel = new SiteGridPanel(injector.getDispatcher());
		panel.load(NullGroupingModel.INSTANCE, filter);

		addToRootPanel(panel);              
	}
	
	private void addToRootPanel(Widget panel){
		Viewport viewport = new Viewport();
        viewport.setLayout(new FitLayout());
        viewport.add(panel);
        
        RootPanel.get().add(viewport);
	}
}
