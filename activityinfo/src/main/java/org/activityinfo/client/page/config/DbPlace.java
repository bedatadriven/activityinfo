package org.activityinfo.client.page.config;

import org.activityinfo.client.Place;
import org.activityinfo.client.PlaceParser;
import org.activityinfo.client.ViewPath;
import org.activityinfo.client.common.grid.AbstractPagingGridPlace;
import org.activityinfo.client.page.PageId;
import org.activityinfo.client.page.Pages;

import java.util.List;
/*
 * @author Alex Bertram
 */

public class DbPlace extends AbstractPagingGridPlace {

    private PageId pageId;
    private int databaseId;

    protected DbPlace() {
    }

    public DbPlace(PageId pageId, int databaseId) {
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

    public String pageStateToken() {
        StringBuilder sb = new StringBuilder();
        sb.append(databaseId);
        appendGridStateToken(sb);
        return sb.toString();
    }

    public List<ViewPath.Node> getViewPath() {
        return ViewPath.make(Pages.ConfigFrameSet, pageId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DbPlace dbPlace = (DbPlace) o;

        if (databaseId != dbPlace.databaseId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return databaseId;
    }

    public static class Parser implements PlaceParser {

        private PageId pageId;

        public Parser(PageId pageId) {
            this.pageId = pageId;
        }

        public Place parse(String token) {
            return new DbPlace(pageId, Integer.parseInt(token));
        }
    }


}
