package org.activityinfo.embed.client;

import java.util.List;
import java.util.Map;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.page.entry.SiteGridPanel;
import org.sigmah.client.page.entry.grouping.NullGroupingModel;
import org.sigmah.shared.command.Filter;
import org.sigmah.shared.report.model.DimensionType;
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
		
		int activityId = Integer.valueOf(Location.getParameter("activityId"));
		filter.addRestriction(DimensionType.Activity, activityId);
		
		Map<String, List<String>> parameterMap = Location.getParameterMap();
		List<String> ids = parameterMap.get("partnerId");
		if (ids != null) {
			List<Integer> partnerIds = Lists.newArrayList();
			for (String id : ids) {
				partnerIds.add(Integer.valueOf(id));
			}
			filter.addRestriction(DimensionType.Partner, partnerIds);
		}

		return filter;
	}

	private void addToRootPanel(Widget panel) {
		Viewport viewport = new Viewport();
		viewport.setLayout(new FitLayout());
		viewport.add(panel);

		RootPanel.get().add(viewport);
	}
}
