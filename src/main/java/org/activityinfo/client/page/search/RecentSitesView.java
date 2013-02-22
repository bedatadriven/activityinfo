package org.activityinfo.client.page.search;

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

import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.page.search.SearchPresenter.RecentSiteModel;
import org.activityinfo.shared.command.result.SitePointList;

import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.ListViewEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Padding;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.ListView;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayout.VBoxLayoutAlign;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayoutData;

public class RecentSitesView extends ContentPanel {
	private ListStore<RecentSiteModel> storeSites;
	
	public RecentSitesView() {
		super();
		
		initializeComponent();
		
		createSitesPanel();
		createMapWidget();
	}
	
	public void setSites(List<RecentSiteModel> sites) {
		
		storeSites.removeAll();
		storeSites.add(sites);
	}
	
	private void initializeComponent() {
		setHeading(I18N.MESSAGES.recentlyEditedSites("10"));
		VBoxLayout vboxLayout = new VBoxLayout();
		vboxLayout.setVBoxLayoutAlign(VBoxLayoutAlign.STRETCH);
		vboxLayout.setPadding(new Padding(5));
		
		setLayout(vboxLayout);

		storeSites = new ListStore<RecentSiteModel>();
	}
	
	public void setSitePoins(SitePointList sitePoints) {
//		map.setSites(sitePoints);
	}

	private void createSitesPanel() {
		ListView<RecentSiteModel> listviewSites = new ListView<RecentSiteModel>(storeSites);
		
		listviewSites.setTemplate(SearchResources.INSTANCE.sitesTemplate().getText());
		listviewSites.setItemSelector(".site");
		listviewSites.addListener(Events.Select, new Listener<ListViewEvent<RecentSiteModel>>() {
			@Override
			public void handleEvent(ListViewEvent<RecentSiteModel> be) {
//				mapWidget.selectSite(be.getModel().getSiteId());
			}
		});
		
	    VBoxLayoutData vbld = new VBoxLayoutData();
	    vbld.setFlex(3);
		add(listviewSites, vbld);
	}

	private void createMapWidget() {
	}
}
