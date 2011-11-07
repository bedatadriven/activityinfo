package org.sigmah.client.page.entry.location;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.shared.dto.CountryDTO;
import org.sigmah.shared.dto.LocationTypeDTO;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;

public class LocationDialog extends Window {

	private final LocationSearchPresenter presenter;
	private Dispatcher dispatcher;
	
	public LocationDialog(Dispatcher dispatcher, CountryDTO country, LocationTypeDTO locationType) {
		this.dispatcher = dispatcher;
		this.presenter = new LocationSearchPresenter(dispatcher, country, locationType);

		setHeading("Choose Location");
		setWidth((int)(com.google.gwt.user.client.Window.getClientWidth() * 0.95));
		setHeight((int)(com.google.gwt.user.client.Window.getClientHeight() * 0.95));
		setLayout(new BorderLayout());
		
		addSearchPanel();
		addMap();
	}

	private void addSearchPanel() {
		
		LayoutContainer container = new LayoutContainer();
		container.setLayout(new FlowLayout());
		container.setScrollMode(Scroll.AUTOY);
		container.addStyleName("locSerPanel");
	
		container.add(newHeader("Choosing a location"));
		container.add(newExplanation("Here you can choose the location where your activity is taking place. First use the " +
				"filters below to find locations where activites have already been added. This will make it possible to " +
				"link your activity's results to other activities both inside and outside of your organization. If you can't " +
				"find the location listed here, click the add new location button or double-click the map to the right."));
		
		container.add(newHeader("Search for existing locations"));
		container.add(new SearchForm(dispatcher, presenter));
		
		container.add(newHeader("Search Results"));
		container.add(new SearchResultListView(presenter));
		container.add(new SearchStatusView(presenter));
				
		BorderLayoutData layout = new BorderLayoutData(LayoutRegion.WEST);
		layout.setSize(350);
		
		add(container, layout);
	}

	private Html newHeader(String string) {
		Html html = new Html(string);
		html.addStyleName("locSerHdr");
		return html;
	}

	private Html newExplanation(String string) {
		Html html = new Html(string);
		html.addStyleName("locSecHlp");
		return html;
	}
	
	private void addMap() {
		SearchMapView mapView = new SearchMapView(presenter);
		
		add(mapView, new BorderLayoutData(LayoutRegion.CENTER));
	}
}
