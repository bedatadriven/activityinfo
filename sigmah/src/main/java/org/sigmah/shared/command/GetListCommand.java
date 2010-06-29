package org.sigmah.shared.command;

import com.extjs.gxt.ui.client.data.SortInfo;
import org.sigmah.client.dispatch.loader.ListCmdLoader;
import org.sigmah.shared.command.result.CommandResult;

/**
 * Base class for Commands that return lists of objects, support sorting on the server,
 * and play nicely with GXT's ListLoader. They can be used in conjunction with {@link ListCmdLoader}
 *
 * @author Alex Bertram
 * @param <T> The result type of the command. Generally should be
 * {@link org.sigmah.shared.command.result.ListResult} or
 * {@link org.sigmah.shared.command.result.PagingResult}
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
