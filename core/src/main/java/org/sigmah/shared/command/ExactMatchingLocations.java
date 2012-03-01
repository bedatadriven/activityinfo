package org.sigmah.shared.command;

import java.util.List;

import org.sigmah.shared.command.ExactMatchingLocations.ExactMatchingLocationsResult;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.dto.LocationDTO;

import com.google.common.collect.Lists;


/** Gets a list of locations matching which have identical data, except for the identifier */
public class ExactMatchingLocations implements Command<ExactMatchingLocationsResult>{
	
	// Nothing really
	
	public static class ExactMatchingLocationsResult implements CommandResult {
		private List<List<LocationDTO>> matchingLocations = Lists.newArrayList();
		private int millisecondsToCompleteQuery;

		public List<List<LocationDTO>> getMatchingLocations() {
			return matchingLocations;
		}
		public void setMatchingLocations(List<List<LocationDTO>> matchingLocations) {
			this.matchingLocations = matchingLocations;
		}
		public int getMillisecondsToCompleteQuery() {
			return millisecondsToCompleteQuery;
		}
		public void setMillisecondsToCompleteQuery(int millisecondsToCompleteQuery) {
			this.millisecondsToCompleteQuery = millisecondsToCompleteQuery;
		}
		
	}
}
