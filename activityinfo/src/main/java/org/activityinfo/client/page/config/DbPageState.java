package org.activityinfo.client.page.config;

import org.activityinfo.client.page.Frames;
import org.activityinfo.client.page.PageId;
import org.activityinfo.client.page.PageState;
import org.activityinfo.client.page.PageStateParser;
import org.activityinfo.client.page.common.grid.AbstractPagingGridPageState;

import java.util.Arrays;
import java.util.List;
/*
 * @author Alex Bertram
 */

public class DbPageState extends AbstractPagingGridPageState {

    private PageId pageId;
    private int databaseId;

    protected DbPageState() {
    }

    public DbPageState(PageId pageId, int databaseId) {
        this.pageId = pageId;
        this.databaseId = databaseId;
    }

    public int getDatabaseId() {
        return databaseId;
    }

    public void setDatabaseId(int databaseId) {
        this.databaseId = databaseId;
    }

    public PageId getPageId() {
        return pageId;
    }

    public String serializeAsHistoryToken() {
        StringBuilder sb = new StringBuilder();
        sb.append(databaseId);
        appendGridStateToken(sb);
        return sb.toString();
    }

    public List<PageId> getEnclosingFrames() {
        return Arrays.asList(Frames.ConfigFrameSet, pageId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DbPageState dbPlace = (DbPageState) o;

        if (databaseId != dbPlace.databaseId) return false;
        if (pageId != dbPlace.pageId) return false;
        
        return true;
    }

    @Override
    public int hashCode() {
        return databaseId;
    }

    public static class Parser implements PageStateParser {

        private PageId pageId;

        public Parser(PageId pageId) {
            this.pageId = pageId;
        }

        public PageState parse(String token) {
            return new DbPageState(pageId, Integer.parseInt(token));
        }
    }


}
