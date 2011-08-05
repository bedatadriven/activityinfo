package org.sigmah.client.page.search;

import org.sigmah.client.mvp.ListView;
import org.sigmah.shared.command.result.SearchResult;
import org.sigmah.shared.dto.SearchHitDTO;

public class SearchPresenter {
	public interface SearchView extends ListView<SearchHitDTO, SearchResult> {
		
	}
}
