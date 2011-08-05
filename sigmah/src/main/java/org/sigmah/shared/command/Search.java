package org.sigmah.shared.command;

import org.sigmah.shared.command.result.SearchResult;

public class Search implements Command<SearchResult> {
	int pageId;
	int userId;
	String searchQuery;
	
	public Search() {
	}
	
	public Search(int pageId, int userId, String searchQuery) {
		super();
		this.pageId = pageId;
		this.userId = userId;
		this.searchQuery = searchQuery;
	}
	
	public int getPageId() {
		return pageId;
	}
	public void setPageId(int pageId) {
		this.pageId = pageId;
	}
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	public String getSearchQuery() {
		return searchQuery;
	}
	public void setSearchQuery(String searchQuery) {
		this.searchQuery = searchQuery;
	}
	
}