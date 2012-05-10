package org.activityinfo.embed.client;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.page.entry.SiteGridPanel;
import org.activityinfo.client.page.entry.grouping.NullGroupingModel;
import org.activityinfo.shared.command.Filter;
import org.activityinfo.shared.report.model.DimensionType;

import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.util.Theme;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.Viewport;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.common.collect.Lists;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window.Location;
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

		if (Location.getParameter("activityId") == null) {
			Log.info("Could not obtain Activity Id!");

			Html html = new Html(I18N.CONSTANTS.noActivityIdProvided());
			addToRootPanel(html);
			return;
		}

		GXT.setDefaultTheme(Theme.BLUE, true);

		final EmbedInjector injector = GWT.create(EmbedInjector.class);

		SiteGridPanel panel = new SiteGridPanel(injector.getDispatcher());
		panel.load(NullGroupingModel.INSTANCE, createFilter());

		addToRootPanel(panel);
	}

	private Filter createFilter() {
		Filter filter = new Filter();
		Map<String, List<String>> parameterMap = Location.getParameterMap();

		String databaseId = Location.getParameter("databaseId");
		
		if(databaseId!=null){
			filter.onDatabase(Integer.valueOf(databaseId));	
		}
		
		String siteId = Location.getParameter("siteId"); 
		if(siteId !=null){
			filter.onSite(Integer.valueOf(siteId));	
		}
		
		String maxDate = Location.getParameter("maxDate");
		if(maxDate !=null){
			filter.setMaxDate(new Date(Long.valueOf(maxDate)));
		}
		
		String minDate = Location.getParameter("minDate");
		if(minDate !=null){
			filter.setMinDate(new Date(Long.valueOf(minDate)));	
		}
		
		addToFilter(filter,DimensionType.Activity,"activityId",parameterMap);
		addToFilter(filter,DimensionType.Partner,"partnerId",parameterMap);
		addToFilter(filter,DimensionType.AdminLevel,"adminLevelId",parameterMap);
		addToFilter(filter,DimensionType.Indicator,"indicatorId",parameterMap);
		addToFilter(filter,DimensionType.Project,"projectId",parameterMap);
		addToFilter(filter,DimensionType.Location,"locationId",parameterMap);
		
		return filter;
	}
	
	private void addToFilter(Filter filter, DimensionType dimension, String paramName, Map<String, List<String>> parameterMap){
		List<String> ids = parameterMap.get(paramName);
		if (ids != null) {
			List<Integer> values = Lists.newArrayList();
			for (String id : ids) {
				values.add(Integer.valueOf(id));
			}
			filter.addRestriction(dimension, values);
		}
		
	}

	private void addToRootPanel(Widget panel) {
		Viewport viewport = new Viewport();
		viewport.setLayout(new FitLayout());
		viewport.add(panel);

		RootPanel.get().add(viewport);
	}
}
