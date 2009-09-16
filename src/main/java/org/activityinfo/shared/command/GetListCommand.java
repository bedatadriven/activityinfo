package org.activityinfo.shared.command;

import org.activityinfo.client.command.loader.ListCmdLoader;
import org.activityinfo.shared.command.result.CommandResult;

import com.extjs.gxt.ui.client.data.SortInfo;

/**
 * Base class for Commands that return lists of objects, support sorting on the server,
 * and play nicely with GXT's ListLoader. They can be used in conjunction with {@link ListCmdLoader}
 * 
 * @author Alex Bertram
 *
 * @param <T> The result type of the command. Generally should be {@link org.activityinfo.shared.command.result.ListResult} or
 * {@link org.activityinfo.shared.command.result.PagingResult}
 */
public abstract class GetListCommand<T extends CommandResult> implements Command<T> {

	private SortInfo sortInfo = new SortInfo();

	public SortInfo getSortInfo() {
		return sortInfo;
	}

	public void setSortInfo(SortInfo sortInfo) {
		this.sortInfo = sortInfo;
	}
}
