package org.sigmah.shared.command;

import java.util.Collection;
import java.util.List;

import org.sigmah.shared.command.GetLocations.LocationsResult;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.dto.LocationDTO2;

public class GetLocations implements Command<LocationsResult>{
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
	
	public int getLocationTypeId() {
		return locationTypeId;
	}

	public GetLocations setLocationTypeId(int locationTypeId) {
		this.locationTypeId = locationTypeId;
		return this;
	}

	public static class LocationsResult implements CommandResult {
		private List<LocationDTO2> locations;
		private boolean hasExceededTreshold;
		private int amountResults;
		
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
		public LocationsResult setLocations(List<LocationDTO2> locations) {
			this.locations = locations;
			return this;
		}
		public boolean isHasExceededTreshold() {
			return hasExceededTreshold;
		}
		public LocationsResult setHasExceededTreshold(boolean hasExceededTreshold) {
			this.hasExceededTreshold = hasExceededTreshold;
			return this;
		}
		public int getAmountResults() {
			return amountResults;
		}

		public LocationsResult setAmountResults(int amountResults) {
			this.amountResults = amountResults;
			return this;
		}
	}
}
