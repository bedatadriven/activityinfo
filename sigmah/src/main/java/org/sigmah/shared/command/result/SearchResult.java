package org.sigmah.shared.command.result;

import java.util.ArrayList;
import java.util.List;

import org.sigmah.shared.dto.DTO;
import org.sigmah.shared.dto.SearchHitDTO;

public class SearchResult extends ListResult<SearchHitDTO> implements CommandResult, DTO {
	List<SearchHitDTO> hits = new ArrayList<SearchHitDTO>();
	List<SearchHitDTO> latestAdditions = new ArrayList<SearchHitDTO>();
	
	public SearchResult() {
		super();
	}

	public SearchResult(List<SearchHitDTO> hits,
			List<SearchHitDTO> latestAdditions) {
		this.hits = hits;
		this.latestAdditions = latestAdditions;
	}
	
	public List<SearchHitDTO> getHits() {
		return hits;
	}
	public void setHits(List<SearchHitDTO> hits) {
		this.hits = hits;
	}
	public List<SearchHitDTO> getLatestAdditions() {
		return latestAdditions;
	}
	public void setLatestAdditions(List<SearchHitDTO> latestAdditions) {
		this.latestAdditions = latestAdditions;
	}
}
