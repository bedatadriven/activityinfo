package org.sigmah.shared.search;

import java.io.Serializable;
import java.util.List;

import org.sigmah.shared.report.model.DimensionType;

import com.bedatadriven.rebar.sql.client.SqlTransaction;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface Searcher<M> extends Serializable {
	public void search(String testQuery, SqlTransaction tx, final AsyncCallback<List<Integer>> callback);

	public DimensionType getDimensionType();
}
