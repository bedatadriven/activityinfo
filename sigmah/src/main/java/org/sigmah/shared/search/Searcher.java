package org.sigmah.shared.search;

import java.util.List;

import org.sigmah.shared.report.model.DimensionType;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface Searcher<M> {
	public void search(String testQuery, final AsyncCallback<List<Integer>> callback);

	public DimensionType getDimensionType();
}
