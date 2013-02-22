package org.activityinfo.client.page.common.grid;

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

import org.activityinfo.client.page.PageState;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.SortInfo;

public abstract class AbstractGridPageState implements PageState {
    private SortInfo sortInfo = null;

    public SortInfo getSortInfo() {
        return sortInfo;
    }

    public void setSortInfo(SortInfo sortInfo) {
        this.sortInfo = sortInfo;
    }

    protected void appendGridStateToken(StringBuilder sb) {

    }

    public boolean parseGridStateTokens(String t) {
        if (t.startsWith("sort-desc:")) {
            setSortInfo(new SortInfo(t.substring("sort-desc:".length()),
                Style.SortDir.DESC));
        } else if (t.startsWith("sort:")) {
            setSortInfo(new SortInfo(t.substring("sort:".length()),
                Style.SortDir.DESC));
        } else {
            return false;
        }
        return true;
    }
}
