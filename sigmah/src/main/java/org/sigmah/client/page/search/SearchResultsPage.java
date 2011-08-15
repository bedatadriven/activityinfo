package org.sigmah.client.page.search;

import java.util.List;

import org.sigmah.client.dispatch.AsyncMonitor;
import org.sigmah.client.dispatch.monitor.NullAsyncMonitor;
import org.sigmah.client.page.map.AIMapWidget;
import org.sigmah.shared.command.result.SearchResult;
import org.sigmah.shared.dto.SearchHitDTO;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.ListView;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.TextBox;

public class SearchResultsPage extends ContentPanel implements SearchView {
	private SearchResult searchResult;
//	private ListStore<SearchHitDTO> storeHits = new ListStore<SearchHitDTO>();
//	private ListView<SearchHitDTO> listviewSearchResults;
	private ListStore<SearchHitDTO> storeLatestAdditions = new ListStore<SearchHitDTO>();
	private ListView<SearchHitDTO> listViewLatestAdditions;
	
	private TextBox textboxSearch;
	private AsyncMonitor loadingMonitor = new NullAsyncMonitor();
	private SimpleEventBus eventBus = new SimpleEventBus();
	private AIMapWidget mapWidget;
	
	public SearchResultsPage() {
		
		initializeComponent();
	}

	private void initializeComponent() {
		setHeading("Search results");
		setLayout(new BorderLayout());
		
//		createResultsList();
		createLatestAdditionsList();
		createLatestAdditionsMap();
		createSearchBox();
	}

	private void createLatestAdditionsMap() {
		// Meh, no MVP yet
		//mapWidget = new AIMapWidget(dispatcher)
	}

	private void createSearchBox() {
		textboxSearch = new TextBox();
		textboxSearch.setSize("2em", "2em");
		textboxSearch.setStylePrimaryName("font-size: 1em");
//		textboxSearch.setStyleAttribute("font-size", "2em");
//		textboxSearch.setStyleAttribute("background-color", "grey");
		
		BorderLayoutData bld = new BorderLayoutData(LayoutRegion.NORTH);
		bld.setSize(50);
		bld.setSplit(true);

		add(textboxSearch, bld);
	}

	private void createLatestAdditionsList() {
		listViewLatestAdditions = new ListView<SearchHitDTO>(storeLatestAdditions);
		
		listViewLatestAdditions.setTemplate(SearchResources.INSTANCE.latestAdditionsTemplate().getText());
		listViewLatestAdditions.setItemSelector(".addition");
		
		BorderLayoutData bld = new BorderLayoutData(LayoutRegion.EAST);
		bld.setSize(300);
		bld.setSplit(true);
		
		add(listViewLatestAdditions, bld);
	}

//	private void createResultsList() {
//		listviewSearchResults = new ListView<SearchHitDTO>(storeHits);
//
//		listviewSearchResults.setTemplate(SearchResources.INSTANCE.resultsTemplate().getText());
//		listviewSearchResults.setItemSelector(".hit");
//
//		BorderLayoutData bld = new BorderLayoutData(LayoutRegion.CENTER);
//		bld.setSplit(true);
//		
//		add(listviewSearchResults, bld);
//	}

	@Override
	public void setParent(SearchResult parent) {
		searchResult = parent;
	}

	@Override
	public void setItems(List<SearchHitDTO> items) {
//		storeHits.removeAll();
//		storeHits.add(items);
	}

	@Override
	public void initialize() {
		// TODO Auto-generated method stub

	}

	@Override
	public AsyncMonitor getLoadingMonitor() {
		return loadingMonitor;
	}

	@Override
	public void setValue(SearchHitDTO value) {
	}

	@Override
	public SearchHitDTO getValue() {
		return null;
	}

	@Override
	public void setLatestAdditions(List<SearchHitDTO> latestAdditions) {
		storeLatestAdditions.removeAll();
		storeLatestAdditions.add(latestAdditions);
	}

	@Override
	public HandlerRegistration addSearchHandler(SearchHandler handler) {
		return eventBus.addHandler(SearchEvent.TYPE, handler);
	}

}
