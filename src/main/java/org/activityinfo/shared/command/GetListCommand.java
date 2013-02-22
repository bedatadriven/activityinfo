package org.activityinfo.shared.command;

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

import org.activityinfo.shared.command.result.CommandResult;

import com.extjs.gxt.ui.client.data.SortInfo;

/**
 * Base class for Commands that return lists of objects, support sorting on the
 * server, and play nicely with GXT's ListLoader. They can be used in
 * conjunction with {@link ListCmdLoader}
 * 
 * @author Alex Bertram
 * @param <T>
 *            The result type of the command. Generally should be
 *            {@link org.activityinfo.shared.command.result.ListResult} or
 *            {@link org.activityinfo.shared.command.result.PagingResult}
 */
public abstract class GetListCommand<T extends CommandResult> implements
    Command<T> {

    private SortInfo sortInfo = new SortInfo();

    public SortInfo getSortInfo() {
        return sortInfo;
    }

    public void setSortInfo(SortInfo sortInfo) {
        this.sortInfo = sortInfo;
    }
}
