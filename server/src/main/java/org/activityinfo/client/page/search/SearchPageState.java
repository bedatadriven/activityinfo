package org.activityinfo.client.page.search;

import java.util.Arrays;
import java.util.List;

import org.activityinfo.client.page.PageId;
import org.activityinfo.client.page.PageState;
import org.activityinfo.client.page.PageStateParser;
import org.activityinfo.client.page.app.Section;

public class SearchPageState implements PageState {

	private String searchQuery = "";
	
	public SearchPageState() {
	}

	public SearchPageState(String query) {
		searchQuery = query;
	}

	@Override
	public String serializeAsHistoryToken() {
		if (searchQuery != null & !searchQuery.isEmpty()) {
			return "q=" + searchQuery;
		} 
		return null;
	}

	@Override
	public PageId getPageId() {
		return SearchPresenter.SEARCH_PAGE_ID;
	}

	@Override
	public List<PageId> getEnclosingFrames() {
		return Arrays.asList(SearchPresenter.SEARCH_PAGE_ID);
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
        	if(token == null) {
        		return new SearchPageState();
        	}
        	String query = "";
        	if (token.startsWith("q")) {
        		try {
        			query = token.split("=")[1];
        		} catch(Exception ex) { }
        	}
            return new SearchPageState(query);
        }
    }

	@Override
	public Section getSection() {
		return null;
	}
	
}
