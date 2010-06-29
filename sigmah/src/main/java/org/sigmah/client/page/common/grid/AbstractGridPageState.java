package org.sigmah.client.page.common.grid;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.SortInfo;
import org.sigmah.client.page.PageState;
/*
 * @author Alex Bertram
 */

public abstract class AbstractGridPageState implements PageState {
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
