/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.common.grid;

import org.sigmah.client.page.PageState;

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
        if(t.startsWith("sort-desc:")) {
            setSortInfo(new SortInfo(t.substring("sort-desc:".length()), Style.SortDir.DESC));
        } else if(t.startsWith("sort:")) {
            setSortInfo(new SortInfo(t.substring("sort:".length()), Style.SortDir.DESC));
        } else {
            return false;
        }
        return true;
    }
}
