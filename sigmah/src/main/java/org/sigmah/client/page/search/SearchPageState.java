package org.sigmah.client.page.search;

import java.util.Arrays;
import java.util.List;

import org.sigmah.client.page.PageId;
import org.sigmah.client.page.PageState;
import org.sigmah.client.page.PageStateParser;

public class SearchPageState implements PageState {

	private String searchQuery = "";
	
	public SearchPageState() {
	}

	public SearchPageState(String query) {
		searchQuery = query;
	}

	@Override
	public String serializeAsHistoryToken() {
		return null;
	}

	@Override
	public PageId getPageId() {
		return SearchPresenter.Search;
	}

	@Override
	public List<PageId> getEnclosingFrames() {
		return Arrays.asList(SearchPresenter.Search);
	}
	
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SearchPageState searchPlace = (SearchPageState) o;

        if (searchQuery != searchPlace.searchQuery) {
            return false;
        }
        
        return true;
    }
	
    @Override
    public int hashCode() {
        return 0;
    }
	
	public String getSearchQuery() {
		return searchQuery;
	}

	public static class Parser implements PageStateParser {
        @Override
        public PageState parse(String token) { 
            return new SearchPageState(token);
        }
    }
	
}
