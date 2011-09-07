package org.sigmah.shared.dao.pivot;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.sigmah.shared.dao.Filter;
import org.sigmah.shared.report.model.Dimension;

import com.bedatadriven.rebar.sql.client.SqlDatabase;
import com.bedatadriven.rebar.sql.client.SqlTransaction;
import com.bedatadriven.rebar.sql.client.query.SqlDialect;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;


public class PivotDAOAsync  {

    private final SqlDialect dialect;

    @Inject
    public PivotDAOAsync(SqlDatabase db) {
        this.dialect = db.getDialect();
    }

    public void aggregate(SqlTransaction tx, Set<Dimension> dimensions, Filter filter, int userId, AsyncCallback<List<Bucket>> callback) {
        final List<Bucket> buckets = new ArrayList<Bucket>();
        
        // first step
        new PivotQuery(tx, dialect, filter, dimensions, userId)
        	.addTo(buckets)
        	.queryForSumAndAverages();
        
        // second step
        new PivotQuery(tx, dialect, filter, dimensions, userId)
        	.addTo(buckets)
        	.callbackTo( callback )
        	.queryForSiteCounts();
    }
}

