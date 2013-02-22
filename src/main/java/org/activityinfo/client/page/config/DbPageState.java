package org.activityinfo.client.page.config;

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

import java.util.Arrays;
import java.util.List;

import org.activityinfo.client.page.PageId;
import org.activityinfo.client.page.PageState;
import org.activityinfo.client.page.PageStateParser;
import org.activityinfo.client.page.app.Section;
import org.activityinfo.client.page.common.grid.AbstractPagingGridPageState;

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

    @Override
    public PageId getPageId() {
        return pageId;
    }

    @Override
    public String serializeAsHistoryToken() {
        StringBuilder sb = new StringBuilder();
        sb.append(databaseId);
        appendGridStateToken(sb);
        return sb.toString();
    }

    @Override
    public List<PageId> getEnclosingFrames() {
        return Arrays.asList(ConfigFrameSet.PAGE_ID, pageId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DbPageState dbPlace = (DbPageState) o;

        if (databaseId != dbPlace.databaseId) {
            return false;
        }
        if (pageId != dbPlace.pageId) {
            return false;
        }

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

        @Override
        public PageState parse(String token) {
            return new DbPageState(pageId, Integer.parseInt(token));
        }
    }

    @Override
    public Section getSection() {
        return Section.DESIGN;
    }

}
