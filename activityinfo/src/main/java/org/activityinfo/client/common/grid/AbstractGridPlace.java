package org.activityinfo.client.common.grid;

import org.activityinfo.client.Place;

import com.extjs.gxt.ui.client.data.SortInfo;
import com.extjs.gxt.ui.client.Style;
/*
 * @author Alex Bertram
 */

public abstract class AbstractGridPlace implements Place {
    protected SortInfo sortInfo = null;

    public SortInfo getSortInfo() {
        return sortInfo;
    }

    public void setSortInfo(SortInfo sortInfo) {
        this.sortInfo = sortInfo;
    }

    protected void appendGridStateToken(StringBuilder sb) {
        
    }

    public boolean parseGridStateTokens(String t) {
        if(t.startsWith("sort-desc:")) {
            sortInfo = new SortInfo(t.substring("sort-desc:".length()), Style.SortDir.DESC);
        } else if(t.startsWith("sort:")) {
            sortInfo = new SortInfo(t.substring("sort:".length()), Style.SortDir.DESC);
        } else {
            return false;
        }
        return true;
    }
}
