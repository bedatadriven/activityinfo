package org.activityinfo.shared.command.result;

import java.util.ArrayList;
import java.util.List;

import org.activityinfo.shared.command.handler.search.Searcher;
import org.activityinfo.shared.dto.DTO;
import org.activityinfo.shared.dto.SearchHitDTO;
import org.activityinfo.shared.dto.SiteDTO;
import org.activityinfo.shared.report.content.PivotContent;

public class SearchResult extends ListResult<SearchHitDTO> implements CommandResult, DTO {
	private PivotContent pivotTabelData; // hierarchy of activities
	private List<SiteDTO> recentAdditions;
	private List<Searcher> failedSearchers = new ArrayList<Searcher>();

	public SearchResult() {
		super();
	}

	public SearchResult(PivotContent pivotTabelData, List<SiteDTO> recentAdditions) {
		super();
		
		this.pivotTabelData = pivotTabelData;
		this.recentAdditions = recentAdditions;
	}
	
	public PivotContent getPivotTabelData() {
		return pivotTabelData;
	}

	public void setPivotTabelData(PivotContent pivotTabelData) {
		this.pivotTabelData = pivotTabelData;
	}

	public List<Searcher> getFailedSearchers() {
		return failedSearchers;
	}

	public void setFailedSearchers(List<Searcher> failedSearchers) {
		this.failedSearchers = failedSearchers;
	}

	public List<SiteDTO> getRecentAdditions() {
		return recentAdditions;
	}

	public void setRecentAdditions(List<SiteDTO> recentAdditions) {
		this.recentAdditions = recentAdditions;
	}
}
