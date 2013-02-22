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

import com.extjs.gxt.ui.client.Style;

public abstract class AbstractPagingGridPageState extends AbstractGridPageState {
    private int pageNum = -1;

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    @Override
    protected void appendGridStateToken(StringBuilder sb) {
        if (getSortInfo() != null
            && getSortInfo().getSortDir() != Style.SortDir.NONE &&
            getSortInfo().getSortField().length() != 0) {

            sb.append("/sort");
            if (getSortInfo().getSortDir() == Style.SortDir.DESC) {
                sb.append("-desc");
            }
            sb.append(":").append(getSortInfo().getSortField());
            if (pageNum > 0) {
                sb.append("/p").append(pageNum);
            }
        }
    }

    @Override
    public boolean parseGridStateTokens(String t) {
        if (super.parseGridStateTokens(t)) {
            return true;
        }

        if (t.startsWith("p")) {
            pageNum = Integer.parseInt(t.substring(1));
        } else {
            return false;
        }
        return true;
    }
}
