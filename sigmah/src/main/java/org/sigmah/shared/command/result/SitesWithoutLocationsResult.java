package org.sigmah.shared.command.result;

import java.util.List;

import org.sigmah.shared.dto.SiteDTO;

public class SitesWithoutLocationsResult extends SiteResult{
	private int totalLocationsCount = 0;

	public SitesWithoutLocationsResult() {
	}

	public SitesWithoutLocationsResult(List<SiteDTO> data) {
		this.data=data;
	}

	public int getTotalLocationsCount() {
		return totalLocationsCount;
	}

	public void setTotalLocationsCount(int totalLocationsCount) {
		this.totalLocationsCount = totalLocationsCount;
	}
	
}
