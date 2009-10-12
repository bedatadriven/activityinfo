package org.activityinfo.client.page.common.grid;

import com.extjs.gxt.ui.client.Style;
/*
 * @author Alex Bertram
 */

public abstract class AbstractPagingGridPlace extends AbstractGridPlace {
    protected int pageNum = -1;

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    protected void appendGridStateToken(StringBuilder sb) {
        if(sortInfo != null && sortInfo.getSortDir() != Style.SortDir.NONE &&
                sortInfo.getSortField().length() !=0) {

            sb.append("/sort");
            if(sortInfo.getSortDir() == Style.SortDir.DESC) {
                sb.append("-desc");
            }
            sb.append(":").append(sortInfo.getSortField());
            if(pageNum > 0) {
                sb.append("/p").append(pageNum);
            }
        }
    }

    public boolean parseGridStateTokens(String t) {
        if(super.parseGridStateTokens(t))
            return true;

        if(t.startsWith("p")) {
            pageNum = Integer.parseInt(t.substring(1));
        } else {
            return false;
        }
        return true;
    }
}
