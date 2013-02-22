package org.activityinfo.embed.client;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import org.activityinfo.client.page.entry.SiteGridPanel;
import org.activityinfo.client.page.entry.grouping.NullGroupingModel;
import org.activityinfo.shared.command.Filter;
import org.activityinfo.shared.command.FilterUrlSerializer;

import org.activityinfo.client.Log;
import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.util.Theme;
import com.extjs.gxt.ui.client.widget.Viewport;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.common.base.Strings;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
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

		if (Log.isErrorEnabled()) {
			GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {
				@Override
				public void onUncaughtException(Throwable e) {
					Log.error("Uncaught exception", e);
				}
			});
		}

		GXT.setDefaultTheme(Theme.BLUE, true);

		final EmbedInjector injector = GWT.create(EmbedInjector.class);

		if(!Strings.isNullOrEmpty(Location.getParameter("sites"))) {
			Filter filter = FilterUrlSerializer.fromQueryParameter(Location.getParameter("sites"));
			SiteGridPanel panel = new SiteGridPanel(injector.getDispatcher());
			panel.load(NullGroupingModel.INSTANCE, filter);

			addToRootPanel(panel);	
		} else {
			Window.alert("The url is malformed");
		}
		
	}


	private void addToRootPanel(Widget panel) {
		Viewport viewport = new Viewport();
		viewport.setLayout(new FitLayout());
		viewport.add(panel);

		RootPanel.get().add(viewport);
	}
}
