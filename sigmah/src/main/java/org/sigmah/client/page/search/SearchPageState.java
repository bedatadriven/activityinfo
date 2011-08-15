package org.sigmah.client.page.search;

import java.util.Arrays;
import java.util.List;

import org.sigmah.client.page.PageId;
import org.sigmah.client.page.PageState;
import org.sigmah.client.page.PageStateParser;
import org.sigmah.client.page.config.AccountPageState;

public class SearchPageState implements PageState {

	private String searchQuery = "";
	
	public SearchPageState(String value) {
		searchQuery = value;
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
    public boolean equals(Object obj) {
        return obj != null && obj instanceof AccountPageState;
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
