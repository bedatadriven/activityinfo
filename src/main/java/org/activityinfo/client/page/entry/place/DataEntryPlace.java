

package org.activityinfo.client.page.entry.place;

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
import org.activityinfo.client.page.app.Section;
import org.activityinfo.client.page.common.grid.AbstractPagingGridPageState;
import org.activityinfo.client.page.entry.DataEntryPage;
import org.activityinfo.client.page.entry.grouping.GroupingModel;
import org.activityinfo.client.page.entry.grouping.NullGroupingModel;
import org.activityinfo.shared.command.Filter;
import org.activityinfo.shared.dto.ActivityDTO;
import org.activityinfo.shared.dto.UserDatabaseDTO;
import org.activityinfo.shared.report.model.DimensionType;


public class DataEntryPlace extends AbstractPagingGridPageState {

	private Filter filter;
	private GroupingModel grouping = NullGroupingModel.INSTANCE;
	
    public DataEntryPlace() {
    	filter = new Filter();
    }

    public DataEntryPlace(Filter filter) {
        this.filter = filter;
    }
   
    public DataEntryPlace(GroupingModel grouping, Filter filter) {
		super();
		this.filter = filter;
		this.grouping = grouping;
	}

	public DataEntryPlace(ActivityDTO activity) {
    	filter = new Filter();
    	filter.addRestriction(DimensionType.Activity, activity.getId());
    }

    public DataEntryPlace(UserDatabaseDTO database) {
    	filter = new Filter();
    	filter.addRestriction(DimensionType.Database, database.getId());        
    }
    
    @Deprecated
    public DataEntryPlace(int activityId) {
    	filter = new Filter();
    	filter.addRestriction(DimensionType.Activity, activityId);
    }

    public Filter getFilter() {
    	return filter;
    }
    
    public GroupingModel getGrouping() {
    	return grouping;
    }
    
	public DataEntryPlace setGrouping(GroupingModel grouping) {
		this.grouping = grouping;
		return this;
	}
    
    @Override
	public PageId getPageId() {
		return DataEntryPage.PAGE_ID;
	}

    @Override
    public String serializeAsHistoryToken() {
    	return DataEntryPlaceParser.serialize(this);
    }

	@Override
	public List<PageId> getEnclosingFrames() {
		return Arrays.asList(DataEntryPage.PAGE_ID);
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((filter == null) ? 0 : filter.hashCode());
		result = prime * result
				+ ((grouping == null) ? 0 : grouping.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		DataEntryPlace other = (DataEntryPlace) obj;
		if (filter == null) {
			if (other.filter != null) {
				return false;
			}
		} else if (!filter.equals(other.filter)) {
			return false;
		}
		if (grouping == null) {
			if (other.grouping != null) {
				return false;
			}
		} else if (!grouping.equals(other.grouping)) {
			return false;
		}
		return true;
	}

	public DataEntryPlace copy() {
		return new DataEntryPlace(grouping, new Filter(filter));
	}

	public DataEntryPlace setFilter(Filter filter) {
		this.filter = filter;
		return this;
	}

	@Override
	public Section getSection() {
		return Section.DATA_ENTRY;
	}

}
