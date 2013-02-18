package org.activityinfo.server.endpoint.refine;

import java.util.List;

import com.google.common.collect.Lists;


public class QueryResponse {

	private List<Match> result = Lists.newArrayList();

	
	
	public QueryResponse(List<Match> result) {
		super();
		this.result = result;
	}


	public List<Match> getResult() {
		return result;
	}

	
}
