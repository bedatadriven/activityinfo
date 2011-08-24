package org.sigmah.client.page.search;

import java.util.List;

import org.sigmah.client.i18n.I18N;
import org.sigmah.client.page.map.MapView;
import org.sigmah.client.page.map.MapView.SiteSelectedEvent;
import org.sigmah.client.page.map.MapView.SiteSelectedHandler;
import org.sigmah.client.page.map.MapViewImpl;
import org.sigmah.client.page.search.SearchPresenter.RecentSiteModel;
import org.sigmah.shared.command.result.SitePointList;

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
	private MapView mapWidget;
	private ListView<RecentSiteModel> listviewSites;
	private ListStore<RecentSiteModel> storeSites;
	private List<RecentSiteModel> sites;
	
	public RecentSitesView() {
		super();
		
		initializeComponent();
		
		createSitesPanel();
		createMapWidget();
	}
	
	public void setSites(List<RecentSiteModel> sites) {
		this.sites=sites;
		
		storeSites.removeAll();
		storeSites.add(sites);
	}
	
	public void setSitePoins(SitePointList sitePoints) {
		mapWidget.setSites(sitePoints);
	}

	private void createSitesPanel() {
		listviewSites = new ListView<RecentSiteModel>(storeSites);
		
		listviewSites.setTemplate(SearchResources.INSTANCE.sitesTemplate().getText());
		listviewSites.setItemSelector(".site");
		listviewSites.addListener(Events.Select, new Listener<ListViewEvent<RecentSiteModel>>() {
			@Override
			public void handleEvent(ListViewEvent<RecentSiteModel> be) {
				mapWidget.selectSite(be.getModel().getSiteId());
			}
		});
		
	    VBoxLayoutData vbld = new VBoxLayoutData();
	    vbld.setFlex(3);
		add(listviewSites, vbld);
	}

	private void createMapWidget() {
		mapWidget = new MapViewImpl();
		mapWidget.addSiteSelectedHandler(new SiteSelectedHandler() {
			@Override
			public void onSiteSelected(SiteSelectedEvent siteSelectedEvent) {
				RecentSiteModel site = getSiteById(siteSelectedEvent.getSiteId());
				listviewSites.getSelectionModel().select(site, false);
				mapWidget.selectSite(site.getSiteId());
			}
		});
		
	    VBoxLayoutData vbld = new VBoxLayoutData();
	    vbld.setFlex(1);
	}
	
	// TODO: move to collection class
	private RecentSiteModel getSiteById(int siteId) {
		for (RecentSiteModel site : sites) {
			if (site.getSiteId() == siteId)
			{
				return site;
			}
		}
		return null;
	}

	private void initializeComponent() {
		setHeading(I18N.MESSAGES.recentlyAddedSites("10"));
		VBoxLayout vboxLayout = new VBoxLayout();
		vboxLayout.setVBoxLayoutAlign(VBoxLayoutAlign.STRETCH);
		vboxLayout.setPadding(new Padding(5));
		
		setLayout(vboxLayout);

		storeSites = new ListStore<RecentSiteModel>();
	}
	
}
