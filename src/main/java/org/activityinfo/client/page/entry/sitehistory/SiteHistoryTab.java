package org.activityinfo.client.page.entry.sitehistory;

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

import java.util.List;

import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.i18n.I18N;
import org.activityinfo.shared.command.GetLocations;
import org.activityinfo.shared.command.GetLocations.GetLocationsResult;
import org.activityinfo.shared.command.GetSchema;
import org.activityinfo.shared.command.GetSiteHistory;
import org.activityinfo.shared.command.GetSiteHistory.GetSiteHistoryResult;
import org.activityinfo.shared.dto.LocationDTO;
import org.activityinfo.shared.dto.SchemaDTO;
import org.activityinfo.shared.dto.SiteDTO;
import org.activityinfo.shared.dto.SiteHistoryDTO;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class SiteHistoryTab extends TabItem {

	private final Html content;
	private final Dispatcher dispatcher;
	
	public SiteHistoryTab(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
		
		this.setScrollMode(Scroll.AUTO);
		
		setText(I18N.CONSTANTS.history());
		
		content = new Html();
		content.setStyleName("details");
		add(content);
	}
	
	// retrieve all needed data: sitehistoryresult, schema, and locations
	public void setSite(final SiteDTO site) {
		renderLoading();
		
		dispatcher.execute(new GetSiteHistory(site.getId()), new AsyncCallback<GetSiteHistoryResult>() {
			@Override
			public void onFailure(Throwable caught) {
				renderNotAvailable(site);
			}
			@Override
			public void onSuccess(final GetSiteHistoryResult historyResult) {
				if (historyResult.hasHistories()) {
					dispatcher.execute(new GetLocations(historyResult.collectLocationIds()), new AsyncCallback<GetLocationsResult>() {
						@Override
						public void onFailure(Throwable caught) {
							renderNotAvailable(site);
						}
						@Override
						public void onSuccess(final GetLocationsResult locationsResult) {
							dispatcher.execute(new GetSchema(), new AsyncCallback<SchemaDTO>() {
								@Override
								public void onFailure(Throwable caught) {
									renderNotAvailable(site);
								}
								@Override
								public void onSuccess(SchemaDTO schema) {
									render(schema, locationsResult.getLocations(), site, historyResult.getSiteHistories());
								}
							});
						}
					});
				} else {
					renderNotAvailable(site);
				}
			}
		});
	}
	
	private void render(final SchemaDTO schema, final List<LocationDTO> locations, final SiteDTO site, final List<SiteHistoryDTO> histories) {
		content.setHtml(new SiteHistoryRenderer().render(schema, locations, site, histories));
	}

	private void renderNotAvailable(final SiteDTO site) {
		content.setHtml(new SiteHistoryRenderer().renderNotAvailable(site));
	}

	private void renderLoading() {
		content.setHtml(new SiteHistoryRenderer().renderLoading());
	}
}
