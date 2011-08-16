package org.sigmah.shared.search;

import org.sigmah.shared.dao.Filter;

public class AbstractSearcher<M> {
	protected final Filter resultFilter = new Filter();
	
	protected String likeify(String testQuery) {
		return "%" + testQuery + "%";
	}
}
