package org.activityinfo.shared.command;

import org.activityinfo.shared.command.result.PagingResult;

/**
 * Super class for commands that return paged result sets compatible with
 * GXT's loader framework.
 *
 * See also: {@link com.extjs.gxt.ui.client.data.PagingLoadConfig},
 * {@link com.extjs.gxt.ui.client.data.PagingLoadResult},
 * {@link org.activityinfo.client.command.loader.PagingCmdLoader}
 *
 * @param <T> The result class of this command.
 */
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
