package org.activityinfo.shared.command.handler.search;

import java.io.Serializable;
import java.util.List;

import org.activityinfo.shared.report.model.DimensionType;

import com.bedatadriven.rebar.sql.client.SqlTransaction;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface Searcher extends Serializable {
	public void search(List<String> testQuery, SqlTransaction tx, final AsyncCallback<List<Integer>> callback);

	public DimensionType getDimensionType();
}
