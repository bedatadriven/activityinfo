package org.sigmah.shared.command.result;

import java.util.ArrayList;
import java.util.List;

import org.sigmah.shared.dto.DTO;
import org.sigmah.shared.dto.FilterResult;
import org.sigmah.shared.dto.SearchHitDTO;
import org.sigmah.shared.dto.SiteDTO;
import org.sigmah.shared.report.content.PivotContent;

public class SearchResult extends ListResult<SearchHitDTO> implements CommandResult, DTO {
	PivotContent pivotTabelData; // hierarchy of activities
	List<FilterResult> filterResults;
	List<SiteDTO> recentAdditions = new ArrayList<SiteDTO>();

	public SearchResult() {
		super();
	}


	public SearchResult(PivotContent pivotTabelData,
			List<FilterResult> filterResults, List<SiteDTO> recentAdditions) {
		super();
		this.pivotTabelData = pivotTabelData;
		this.filterResults = filterResults;
		this.recentAdditions = recentAdditions;
	}


	public List<SiteDTO> getLatestAdditions() {
		return recentAdditions;
	}
	
	public void setLatestAdditions(List<SiteDTO> latestAdditions) {
		this.recentAdditions = latestAdditions;
	}
	
	public PivotContent getPivotTabelData() {
		return pivotTabelData;
	}

	public void setPivotTabelData(PivotContent pivotTabelData) {
		this.pivotTabelData = pivotTabelData;
	}
}
