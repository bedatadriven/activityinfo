package org.sigmah.shared.command;

import org.sigmah.shared.command.result.VoidResult;

public class RemoveFavorite implements Command<VoidResult> {
	private int userId;
	private String PageId;
	
	public RemoveFavorite(int userId, String pageId) {
		super();
		this.userId = userId;
		PageId = pageId;
	}
	public RemoveFavorite() {
		super();
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getPageId() {
		return PageId;
	}
	public void setPageId(String pageId) {
		PageId = pageId;
	}
}