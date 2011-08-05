package org.sigmah.client.page.search;

import java.util.List;

import org.sigmah.client.dispatch.AsyncMonitor;
import org.sigmah.client.page.search.SearchPresenter.SearchView;
import org.sigmah.shared.command.result.SearchResult;
import org.sigmah.shared.dto.SearchHitDTO;

import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ListView;

public class SearchResultsPage implements SearchView {
	SearchResult searchResult;
	ListStore<SearchHitDTO> hitsStore = new ListStore<SearchHitDTO>();
	ListView<SearchHitDTO> listviewSearchResults;
	
	public SearchResultsPage() {
		
		initializeComponent();
	}

	private void initializeComponent() {
		listviewSearchResults = new ListView<SearchHitDTO>(hitsStore);
		
	}

	@Override
	public void setParent(SearchResult parent) {
		searchResult = parent;
	}

	@Override
	public void setItems(List<SearchHitDTO> items) {
		hitsStore.removeAll();
		hitsStore.add(items);
	}

	@Override
	public void initialize() {
		// TODO Auto-generated method stub

	}

	@Override
	public AsyncMonitor getAsyncMonitor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setValue(SearchHitDTO value) {
	}

	@Override
	public SearchHitDTO getValue() {
		// TODO Auto-generated method stub
		return null;
	}

}
