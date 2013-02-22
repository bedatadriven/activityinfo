package org.activityinfo.client.page.config.link;

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
import org.activityinfo.client.page.app.Section;
import org.activityinfo.client.page.config.ConfigFrameSet;
import org.activityinfo.client.page.config.DbListPageState;

public class IndicatorLinkPlace implements PageState {

	@Override
	public String serializeAsHistoryToken() {
		return null;
	}

	@Override
	public List<PageId> getEnclosingFrames() {
		return Arrays.asList(ConfigFrameSet.PAGE_ID, IndicatorLinkPage.PAGE_ID);
	}

	@Override
	public PageId getPageId() {
		return IndicatorLinkPage.PAGE_ID;
	}

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof DbListPageState;
    }

    @Override
    public int hashCode() {
        return 0;
    }

	@Override
	public Section getSection() {
		return Section.DESIGN;
	}

}
