package org.sigmah.shared.search;

import org.sigmah.shared.dao.Filter;

import com.bedatadriven.rebar.sql.client.SqlDatabase;
import com.google.inject.Inject;

public class AbstractSearcher<M> {
	protected final Filter resultFilter = new Filter();
	protected String query;
	
	protected SqlDatabase db;
	
	@Inject
	public AbstractSearcher(SqlDatabase db) {
		this.db = db;
	}
	
	protected String likeify(String testQuery) {
		return "%" + testQuery + "%";
	}
}
