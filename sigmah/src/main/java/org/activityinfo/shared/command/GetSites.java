package org.activityinfo.shared.command;

import org.activityinfo.shared.command.result.SiteResult;
import org.activityinfo.shared.report.model.Filter;

/**
 * Retrieves a list of sites based on the provided criteria.
 * 
 *
 * @author Alex Bertram
 *
 */
public class GetSites extends PagingGetCommand<SiteResult> {

	private boolean assessmentsOnly;
	private Integer activityId;
	private Integer databaseId;
	private Integer siteId;
    private String filter;
    private Filter pivotFilter;

    private Integer seekToSiteId;
    private int adminEntityId;

    public GetSites() {
		
	}

	
	/**
	 * 
	 * Option to query only for the sites of assessment activities.
	 * See {@link org.activityinfo.server.domain.Activity#isAssessment()}
	 * 
	 * @return True if the command is to return only sites of assessment activities.
	 */
	public boolean isAssessmentsOnly() {
		return assessmentsOnly;
	}
	
	/**
	 * 
	 * Option to query only for the sites of assessment activities.
	 * See {@link org.activityinfo.server.domain.Activity#isAssessment()}
	 * 
	 * @param assessmentsOnly True if the command is to return only sites of assessment activities.
	 */
	public void setAssessmentsOnly(boolean assessmentsOnly) {
		this.assessmentsOnly = assessmentsOnly;
	}

	/**
	 * Option to query only for sites of a given activity
	 * 
	 * @param activityId The ID of the activity to query for, or NULL for all sites
	 */
	public void setActivityId(Integer activityId) {
		this.activityId = activityId;
	}

	/**
	 * Option to query only for sites of a given activity
	 *
	 * @returns The ID of the activity to query for, or NULL for all sites
	 */
	public Integer getActivityId() {
		return activityId;
	}


	/**
	 * @param databaseId the databaseId to set
	 */
	public void setDatabaseId(Integer databaseId) {
		this.databaseId = databaseId;
	}


	/**
	 * @return the databaseId
	 */
	public Integer getDatabaseId() {
		return databaseId;
	}


	/**
	 * @param siteId the siteId to set
	 */
	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public Filter getPivotFilter() {
        return pivotFilter;
    }

    public void setPivotFilter(Filter pivotFilter) {
        this.pivotFilter = pivotFilter;
    }

    public GetSites clone() {
        GetSites c = new GetSites();
        c.assessmentsOnly = assessmentsOnly;
        c.activityId = activityId;
        c.databaseId = databaseId;
        c.siteId = siteId;
        c.setLimit(getLimit());
        c.setOffset(getOffset());
        c.setSortInfo(getSortInfo());

        return c;
    }

	/**
	 * @return the siteId
	 */
	public Integer getSiteId() {
		return siteId;
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        GetSites other = (GetSites) o;

        if (assessmentsOnly != other.assessmentsOnly) {
            return false;
        }
        if (activityId != null ? !activityId.equals(other.activityId) : other.activityId != null) {
            return false;
        }
        if (databaseId != null ? !databaseId.equals(other.databaseId) : other.databaseId != null) {
            return false;
        }
        if (siteId != null ? !siteId.equals(other.siteId) : other.siteId != null) {
            return false;
        }
        if (seekToSiteId != null ? !seekToSiteId.equals(other.seekToSiteId) : other.seekToSiteId != null) {
            return false;
        }
        if (pivotFilter != null ?  !pivotFilter.equals(other.pivotFilter) : other.pivotFilter != null) {
            return false;
        }
        
        return getLimit()==other.getLimit() && getOffset()==other.getOffset();
    }

    @Override
    public int hashCode() {
        int result = (assessmentsOnly ? 1 : 0);
        result = 31 * result + (activityId != null ? activityId.hashCode() : 0);
        result = 31 * result + (databaseId != null ? databaseId.hashCode() : 0);
        result = 31 * result + (siteId != null ? siteId.hashCode() : 0);
        return result;
    }

    public static GetSites byId(int siteId) {
		GetSites cmd = new GetSites();
		cmd.setSiteId(siteId);
		
		return cmd;
	}	

	public static GetSites byActivity(int activityId) {
		GetSites cmd = new GetSites();
		cmd.setActivityId(activityId);
		
		return cmd;
	}

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("GetSites: {");
        if(siteId != null) {
            sb.append("SiteId=").append(siteId);
        } else if(activityId != null) {
            sb.append("ActivityId=").append(activityId);
        } else if(databaseId != null) {
            sb.append("DatabaseId=").append(databaseId);
        } else {
            sb.append("ALL");
        }
        if(assessmentsOnly) {
            sb.append(", assessments only");
        }
        if(seekToSiteId != null) {
            sb.append(", seektoid=").append(seekToSiteId);
        }
        if(pivotFilter!=null) {
            sb.append(", filter=").append(pivotFilter.toString());
        }
        sb.append("}");

        return sb.toString();
    }

    @Override
    public void setOffset(int offset) {
        if(offset != getOffset())  {
            super.setOffset(offset);
            seekToSiteId = null;
        }
    }

    public Integer getSeekToSiteId() {
        return seekToSiteId;
    }

    public void setSeekToSiteId(Integer seekToSiteId) {
        this.seekToSiteId = seekToSiteId;
    }

    public void setAdminEntityId(int adminEntityId) {
        this.adminEntityId = adminEntityId;
    }

    public int getAdminEntityId() {
        return adminEntityId;
    }
}
