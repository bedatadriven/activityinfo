package org.sigmah.shared.command;

import java.util.Collection;

import org.sigmah.shared.command.result.LocationResult;

public class SearchLocations implements Command<LocationResult> {
	private Collection<Integer> adminEntityIds;
	private String name;
	private int threshold = 300;
	private int locationTypeId = 0;
	
	public int getThreshold() {
		return threshold;
	}

	public Collection<Integer> getAdminEntityIds() {
		return adminEntityIds;
	}

	public SearchLocations setAdminEntityIds(Collection<Integer> adminEntityIds) {
		this.adminEntityIds = adminEntityIds;
		return this;
	}

	public String getName() {
		return name;
	}

	public SearchLocations setName(String name) {
		this.name = name;
		return this;
	}
	
	public int getLocationTypeId() {
		return locationTypeId;
	}

	public SearchLocations setLocationTypeId(int locationTypeId) {
		this.locationTypeId = locationTypeId;
		return this;
	}
}
