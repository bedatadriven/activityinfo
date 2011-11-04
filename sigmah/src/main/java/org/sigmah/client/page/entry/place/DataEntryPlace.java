/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.entry.place;


import java.util.Arrays;
import java.util.List;

import org.sigmah.client.page.PageId;
import org.sigmah.client.page.common.grid.AbstractPagingGridPageState;
import org.sigmah.client.page.entry.DataEntryPage;
import org.sigmah.client.page.entry.grouping.AdminGroupingModel;
import org.sigmah.client.page.entry.grouping.GroupingModel;
import org.sigmah.client.page.entry.grouping.NullGroupingModel;
import org.sigmah.shared.command.Filter;
import org.sigmah.shared.dto.ActivityDTO;
import org.sigmah.shared.dto.UserDatabaseDTO;
import org.sigmah.shared.report.model.DimensionType;


public class DataEntryPlace extends AbstractPagingGridPageState {

	private Filter filter;
	private GroupingModel grouping = NullGroupingModel.INSTANCE;
	
    public DataEntryPlace() {
    	filter = new Filter();
    }

    public DataEntryPlace(Filter filter) {
        this.filter = filter;
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
    
	public void setGrouping(AdminGroupingModel grouping) {
		this.grouping = grouping;
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
		if (!filter.equals(other.filter)) {
			return false;
		}
		return true;
	}

}
