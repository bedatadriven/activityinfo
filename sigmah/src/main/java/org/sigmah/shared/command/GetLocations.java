package org.sigmah.shared.command;

import java.util.Collection;
import java.util.List;

import org.sigmah.shared.command.GetLocations.LocationsResult;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.dto.LocationDTO2;

public class GetLocations implements Command<LocationsResult>{
	private Collection<Integer> adminEntityIds;
	private String name;
	private int threshold = 30;
	
	public int getThreshold() {
		return threshold;
	}

	public Collection<Integer> getAdminEntityIds() {
		return adminEntityIds;
	}

	public GetLocations setAdminEntityIds(Collection<Integer> adminEntityIds) {
		this.adminEntityIds = adminEntityIds;
		return this;
	}

	public String getName() {
		return name;
	}

	public GetLocations setName(String name) {
		this.name = name;
		return this;
	}

	public static class LocationsResult implements CommandResult {
		List<LocationDTO2> locations;
		boolean hasExceededTreshold;
		
		public LocationsResult() {
			super();
		}
		public LocationsResult(List<LocationDTO2> locations,
				boolean hasExceededTreshold) {
			super();
			this.locations = locations;
			this.hasExceededTreshold = hasExceededTreshold;
		}
		public List<LocationDTO2> getLocations() {
			return locations;
		}
		public void setLocations(List<LocationDTO2> locations) {
			this.locations = locations;
		}
		public boolean isHasExceededTreshold() {
			return hasExceededTreshold;
		}
		public void setHasExceededTreshold(boolean hasExceededTreshold) {
			this.hasExceededTreshold = hasExceededTreshold;
		}
		
	}
}
