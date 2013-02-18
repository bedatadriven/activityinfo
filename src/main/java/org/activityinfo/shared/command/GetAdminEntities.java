/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.shared.command;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.activityinfo.shared.command.result.AdminEntityResult;

/**
 * Retrieves a list of admin entities from the server.
 * 
 * @author alexander
 *
 */
public class GetAdminEntities extends GetListCommand<AdminEntityResult> {

	private Collection<Integer> countryIds;
	private Integer levelId;
	private Integer parentId;
	private Filter filter;

	public GetAdminEntities() {
		super();
	}

	public GetAdminEntities(int levelId) {
		super();
		this.levelId = levelId;
	}

	public GetAdminEntities(int levelId, Integer parentId) {
		super();
		this.levelId = levelId;
		this.parentId = parentId;
	}

	public GetAdminEntities(int countryId, Filter filter) {
		super();
		this.countryIds = Arrays.asList(countryId);
		this.filter = filter;
	}

	public GetAdminEntities(Collection<Integer> countryIds, Filter filter) {
		super();
		this.countryIds = countryIds;
		this.filter = filter;
	}

	public Integer getLevelId() {
		return levelId;
	}
	public void setLevelId(Integer levelId) {
		this.levelId = levelId;
	}
	public Integer getParentId() {
		return parentId;
	}
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public Collection<Integer> getCountryIds() {
		return countryIds;
	}

	public void setCountryId(Integer countryId) {
		this.countryIds = Arrays.asList(countryId);
	}

	public void setCountryIds(Collection<Integer> countryIds) {
		this.countryIds = countryIds;
	}

	public Filter getFilter() {
		return filter;
	}

	public void setFilter(Filter filter) {
		this.filter = filter;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((filter == null) ? 0 : filter.hashCode());
		result = prime * result + ((levelId == null) ? 0 : levelId.hashCode());
		result = prime * result
		+ ((parentId == null) ? 0 : parentId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (obj == null) {
			return false;
		} else if (getClass() != obj.getClass()) {
			return false;
		}
		GetAdminEntities other = (GetAdminEntities) obj;
		if (filter == null) {
			if (other.filter != null) {
				return false;
			}
		} else if (!filter.equals(other.filter)) {
			return false;
		}
		if (levelId == null) {
			if (other.levelId != null) {
				return false;
			}
		} else if (!levelId.equals(other.levelId)) {
			return false;
		}
		if (parentId == null) {
			if (other.parentId != null) {
				return false;
			}
		} else if (!parentId.equals(other.parentId)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "GetAdminEntities [levelId=" + levelId + ", parentId="
		+ parentId + ", filter=" + filter + ", country=" + countryIds+"]";
	}



}