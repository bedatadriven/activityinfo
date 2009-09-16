package org.activityinfo.shared.command;

import org.activityinfo.shared.command.result.PagingResult;

public class PagingGetCommand<T extends PagingResult> extends GetListCommand<T> {

	private int offset = 0;
	private int limit = -1;
	
	
	public int getOffset() {
		return offset;
	}
	public void setOffset(int offset) {
		this.offset = offset;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}

}
