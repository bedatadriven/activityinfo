package org.sigmah.shared.command;

import org.sigmah.shared.command.result.OrgUnitListResult;
import org.sigmah.shared.dao.Filter;

public class GetOrgUnits implements Command<OrgUnitListResult> {

	private Filter filter;
	
	public GetOrgUnits() {
		
		
	}

	public Filter getFilter() {
		return filter;
	}

	public void setFilter(Filter filter) {
		this.filter = filter;
	}
	
	
	
}
