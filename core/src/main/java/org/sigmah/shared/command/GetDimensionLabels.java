package org.sigmah.shared.command;

import java.util.Map;
import java.util.Set;

import org.sigmah.shared.command.GetDimensionLabels.DimensionLabels;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.report.model.DimensionType;

public class GetDimensionLabels implements Command<DimensionLabels> {

	private DimensionType type;
	private Set<Integer> ids;
	
	public GetDimensionLabels(DimensionType type, Set<Integer> ids) {
		this.type = type;
		this.ids = ids;
	}

	public DimensionType getType() {
		return type;
	}

	public void setType(DimensionType type) {
		this.type = type;
	}

	public Set<Integer> getIds() {
		return ids;
	}

	public void setIds(Set<Integer> ids) {
		this.ids = ids;
	}

	public static class DimensionLabels implements CommandResult {
		private Map<Integer, String> labels;
		
		public DimensionLabels(Map<Integer, String> labels) {
			this.labels = labels;
		}
		
		public DimensionLabels() {
			
		}

		public Map<Integer, String> getLabels() {
			return labels;
		}

		public void setLabels(Map<Integer, String> labels) {
			this.labels = labels;
		}
		
		
		
	}
}
