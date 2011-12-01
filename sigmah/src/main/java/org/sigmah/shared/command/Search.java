package org.sigmah.shared.command;

import org.sigmah.shared.command.result.SearchResult;

public class Search implements Command<SearchResult> {
	private int pageId;
	private String searchQuery;
	
	public Search() {
	}
	
	public Search(int pageId, String searchQuery) {
		super();
		
		this.pageId = pageId;
		this.searchQuery = searchQuery;
	}
	
	public Search(String query) {
		this.searchQuery = query;
	}

	public int getPageId() {
		return pageId;
	}
	public void setPageId(int pageId) {
		this.pageId = pageId;
	}
	
	public String getSearchQuery() {
		return searchQuery;
	}
	public void setSearchQuery(String searchQuery) {
		this.searchQuery = searchQuery;
	}
	
}