package org.sigmah.client.page.search;

import java.util.List;

import org.sigmah.client.page.map.MapView;
import org.sigmah.client.page.map.MapView.SiteSelectedEvent;
import org.sigmah.client.page.map.MapView.SiteSelectedHandler;
import org.sigmah.client.page.map.MapViewImpl;
import org.sigmah.shared.command.result.SitePointList;
import org.sigmah.shared.dto.SiteDTO;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.ListViewEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.ListView;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;

public class RecentSitesView extends ContentPanel {
	private MapView mapWidget;
	private ListView<SiteDTO> listviewSites;
	private ListStore<SiteDTO> storeSites;
	private List<SiteDTO> sites;
	
	public RecentSitesView() {
		super();
		
		initializeComponent();
		
		createMapWidget();
		createSitesPanel();
	}
	
	public void setSites(List<SiteDTO> sites) {
		this.sites=sites;
		storeSites.removeAll();
		storeSites.add(sites);

		mapWidget.setSites(SitePointList.fromSitesList(sites));
	}

	private void createSitesPanel() {
		listviewSites = new ListView<SiteDTO>(storeSites);
		
		listviewSites.setTemplate(SearchResources.INSTANCE.sitesTemplate().getText());
		listviewSites.setItemSelector(".site");
		listviewSites.addListener(Events.Select, new Listener<ListViewEvent<SiteDTO>>() {
			@Override
			public void handleEvent(ListViewEvent<SiteDTO> be) {
				mapWidget.selectSite(be.getModel().getId());
			}
		});
		
		BorderLayoutData bld = new BorderLayoutData(LayoutRegion.CENTER); 
		
		add(listviewSites, bld);
	}

	private void createMapWidget() {
		mapWidget = new MapViewImpl();
		mapWidget.addSiteSelectedHandler(new SiteSelectedHandler() {
			@Override
			public void onSiteSelected(SiteSelectedEvent siteSelectedEvent) {
				SiteDTO site = getSiteById(siteSelectedEvent.getSiteId());
				listviewSites.getSelectionModel().select(site, false);
				mapWidget.selectSite(site.getId());
			}
		});
		
		BorderLayoutData bld= new BorderLayoutData(LayoutRegion.SOUTH); 
		bld.setMinSize(300);
		bld.setSize(300);
		
		add(mapWidget.asWidget(), bld);
	}
	
	// TODO: move to collection class
	private SiteDTO getSiteById(int siteId) {
		for (SiteDTO site : sites) {
			if (site.getId() == siteId)
			{
				return site;
			}
		}
		return null;
	}

	private void initializeComponent() {
		setHeading("Recently added sites");
		
		storeSites = new ListStore<SiteDTO>();
	}
	
}
