/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.command;

import org.sigmah.shared.command.result.SiteResult;
import org.sigmah.shared.dao.Filter;
import org.sigmah.shared.report.model.DimensionType;

/**
 * Retrieves a list of sites based on the provided filter and limits.
 * 
 *
 * @author Alex Bertram
 *
 */
public class GetSites extends PagingGetCommand<SiteResult> implements OfflineSupport {

    private Filter filter = new Filter();

    private Integer seekToSiteId;

    public GetSites() {
		
	}

    public Filter filter() {
        return filter;
    }

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        assert filter != null : "Filter cannot be null! Use new Filter() to create an empty filter";
        this.filter = filter;
    }

    public GetSites clone() {
        GetSites c = new GetSites();
        c.filter = new Filter(filter);
        c.setLimit(getLimit());
        c.setOffset(getOffset());
        c.setSortInfo(getSortInfo());

        return c;
    }


    public static GetSites byId(int siteId) {
		GetSites cmd = new GetSites();
		cmd.filter().addRestriction(DimensionType.Site, siteId);
		
		return cmd;
	}	

	public static GetSites byActivity(int activityId) {
		GetSites cmd = new GetSites();
		cmd.filter().onActivity(activityId);
		
		return cmd;
	}

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("GetSites: {\nfilter=").append(filter.toString());

        if(seekToSiteId != null) {
            sb.append(", seektoid=").append(seekToSiteId);
        }
        if(filter !=null) {
            sb.append(", filter=").append(filter.toString());
        }
        sb.append("}");

        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GetSites getSites = (GetSites) o;

        if (!filter.equals(getSites.filter)) return false;
        if (seekToSiteId != null ? !seekToSiteId.equals(getSites.seekToSiteId) : getSites.seekToSiteId != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = filter.hashCode();
        result = 31 * result + (seekToSiteId != null ? seekToSiteId.hashCode() : 0);
        return result;
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
}
