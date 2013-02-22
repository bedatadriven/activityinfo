package org.activityinfo.client.page.search;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

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
