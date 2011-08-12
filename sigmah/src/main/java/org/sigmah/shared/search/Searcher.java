package org.sigmah.shared.search;

import org.sigmah.shared.dao.Filter;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface Searcher {

	void search(String testQuery, AsyncCallback<Filter> callback);
	
}
