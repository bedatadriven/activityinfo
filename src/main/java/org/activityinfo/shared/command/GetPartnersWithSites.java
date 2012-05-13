package org.activityinfo.shared.command;

import org.activityinfo.shared.command.result.PartnerResult;

import com.google.common.base.Objects;

/**
 * Retrieves a list of partners 
 * @author alex
 *
 */
public class GetPartnersWithSites implements Command<PartnerResult> {

	private Filter filter;

	public GetPartnersWithSites() {
		
	}
	
	public GetPartnersWithSites(Filter filter) {
		this.filter = filter;
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
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GetPartnersWithSites other = (GetPartnersWithSites) obj;
		return Objects.equal(other.filter, filter);
	}
}
