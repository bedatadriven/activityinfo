package org.sigmah.shared.command;

import org.sigmah.shared.command.result.PartnerResult;

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
	
}
