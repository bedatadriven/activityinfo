/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.client.page.common.grid;

import com.extjs.gxt.ui.client.Style;

public abstract class AbstractPagingGridPageState extends AbstractGridPageState {
    private int pageNum = -1;

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    protected void appendGridStateToken(StringBuilder sb) {
        if(getSortInfo() != null && getSortInfo().getSortDir() != Style.SortDir.NONE &&
                getSortInfo().getSortField().length() !=0) {

            sb.append("/sort");
            if(getSortInfo().getSortDir() == Style.SortDir.DESC) {
                sb.append("-desc");
            }
            sb.append(":").append(getSortInfo().getSortField());
            if(pageNum > 0) {
                sb.append("/p").append(pageNum);
            }
        }
    }

    public boolean parseGridStateTokens(String t) {
        if(super.parseGridStateTokens(t)) {
            return true;
        }

        if(t.startsWith("p")) {
            pageNum = Integer.parseInt(t.substring(1));
        } else {
            return false;
        }
        return true;
    }
}
