package org.activityinfo.client.page.search;

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
