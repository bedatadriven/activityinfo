package org.sigmah.shared.command;

import java.util.List;

import org.sigmah.shared.command.ExactMatchingLocations.ExactMatchingLocationsResult;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.dto.LocationDTO2;

import com.google.gwt.dev.util.collect.Lists;

/** Gets a list of locations matching which have identical data, except for the identifier */
public class ExactMatchingLocations implements Command<ExactMatchingLocationsResult>{
	
	// Nothing really
	
	public static class ExactMatchingLocationsResult implements CommandResult {
		private List<List<LocationDTO2>> matchingLocations = Lists.create();
		private int millisecondsToCompleteQuery;

		public List<List<LocationDTO2>> getMatchingLocations() {
			return matchingLocations;
		}
		public void setMatchingLocations(List<List<LocationDTO2>> matchingLocations) {
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
