package org.activityinfo.shared.command.result;

import java.util.List;

import org.activityinfo.shared.dto.SiteDTO;

public class SitesWithoutLocationsResult extends SiteResult{
	private int totalLocationsCount = 0;

	public SitesWithoutLocationsResult() {
	}

	public SitesWithoutLocationsResult(List<SiteDTO> data) {
		super(data);
	}

	public int getTotalLocationsCount() {
		return totalLocationsCount;
	}

	public void setTotalLocationsCount(int totalLocationsCount) {
		this.totalLocationsCount = totalLocationsCount;
	}
	
}
