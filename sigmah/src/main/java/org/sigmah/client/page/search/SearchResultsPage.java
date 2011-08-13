package org.sigmah.client.page.search;

import java.util.List;

import org.sigmah.client.dispatch.AsyncMonitor;
import org.sigmah.client.dispatch.monitor.NullAsyncMonitor;
import org.sigmah.shared.command.result.SearchResult;
import org.sigmah.shared.dto.SearchHitDTO;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.ListView;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimpleEventBus;

public class SearchResultsPage extends ContentPanel implements SearchView {
	private SearchResult searchResult;
	private ListStore<SearchHitDTO> storeHits = new ListStore<SearchHitDTO>();
	private ListView<SearchHitDTO> listviewSearchResults;
	private ListStore<SearchHitDTO> storeLatestAdditions = new ListStore<SearchHitDTO>();
	private ListView<SearchHitDTO> listViewLatestAdditions;
	private TextField<String> textfieldSearch;
	private AsyncMonitor loadingMonitor = new NullAsyncMonitor();
	private SimpleEventBus eventBus = new SimpleEventBus();
	
	public SearchResultsPage() {
		
		initializeComponent();
	}

	private void initializeComponent() {
		setHeading("Search results");
		setLayout(new BorderLayout());
		
		createResultsList();
		createLatestAdditionsList();
		createSearchBox();
	}

	private void createSearchBox() {
		textfieldSearch = new TextField<String>();
		textfieldSearch.setStyleAttribute("font-size", "2em");
		textfieldSearch.setStyleAttribute("background-color", "grey");
		
		BorderLayoutData bld = new BorderLayoutData(LayoutRegion.NORTH);
		bld.setSize(100);
		
		add(textfieldSearch, bld);
	}

	private void createLatestAdditionsList() {
		listViewLatestAdditions = new ListView<SearchHitDTO>(storeLatestAdditions);
		
		listViewLatestAdditions.setTemplate(SearchResources.INSTANCE.latestAdditionsTemplate().getText());
		listViewLatestAdditions.setItemSelector(".addition");
		
		BorderLayoutData bld = new BorderLayoutData(LayoutRegion.EAST);
		bld.setSize(300);
		
		add(listViewLatestAdditions, bld);
	}

	private void createResultsList() {
		listviewSearchResults = new ListView<SearchHitDTO>(storeHits);

		listviewSearchResults.setTemplate(SearchResources.INSTANCE.resultsTemplate().getText());
		listviewSearchResults.setItemSelector(".hit");

		BorderLayoutData bld = new BorderLayoutData(LayoutRegion.CENTER);
		
		add(listviewSearchResults, bld);
	}

	@Override
	public void setParent(SearchResult parent) {
		searchResult = parent;
	}

	@Override
	public void setItems(List<SearchHitDTO> items) {
		storeHits.removeAll();
		storeHits.add(items);
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
