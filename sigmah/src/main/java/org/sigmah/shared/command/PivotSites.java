package org.sigmah.shared.command;

import java.util.List;
import java.util.Set;

import org.sigmah.shared.command.PivotSites.PivotResult;
import org.sigmah.shared.command.result.Bucket;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.dao.Filter;
import org.sigmah.shared.report.model.Dimension;

public class PivotSites implements Command<PivotResult> {

	private Set<Dimension> dimensions;
	private Filter filter;
	
	public PivotSites() {
	}
	
	public PivotSites(Set<Dimension> dimensions, Filter filter) {
		super();
		this.dimensions = dimensions;
		this.filter = filter;
	}

	public Set<Dimension> getDimensions() {
		return dimensions;
	}

	public void setDimensions(Set<Dimension> dimensions) {
		this.dimensions = dimensions;
	}

	public Filter getFilter() {
		return filter;
	}

	public void setFilter(Filter filter) {
		this.filter = filter;
	}

	public static class PivotResult implements CommandResult {
		private List<Bucket> buckets;

		public PivotResult() {
		}
		
		public PivotResult(List<Bucket> buckets) {
			this.buckets = buckets;
		}
		
		public List<Bucket> getBuckets() {
			return buckets;
		}
		
		public void setBuckets(List<Bucket> buckets) {
			this.buckets = buckets;
		}
	}
	
}
