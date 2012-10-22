package org.activityinfo.shared.command.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.activityinfo.client.Log;
import org.activityinfo.shared.command.Filter;
import org.activityinfo.shared.command.PivotSites;
import org.activityinfo.shared.command.PivotSites.PivotResult;
import org.activityinfo.shared.command.PivotSites.ValueType;
import org.activityinfo.shared.command.handler.pivot.PivotQuery;
import org.activityinfo.shared.command.result.Bucket;
import org.activityinfo.shared.report.model.Dimension;
import org.activityinfo.shared.report.model.DimensionType;

import com.bedatadriven.rebar.sql.client.SqlTransaction;
import com.bedatadriven.rebar.sql.client.query.SqlDialect;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

public class PivotSitesHandler implements CommandHandlerAsync<PivotSites, PivotSites.PivotResult> {

    private final SqlDialect dialect;

    @Inject
    public PivotSitesHandler(SqlDialect dialect) {
        this.dialect = dialect;
    }

    public void aggregate(SqlTransaction tx, Set<Dimension> dimensions, Filter filter, int userId, AsyncCallback<List<Bucket>> callback) {

    }

	@Override
	public void execute(PivotSites command, ExecutionContext context, AsyncCallback<PivotResult> callback) {

        final List<Bucket> buckets = new ArrayList<Bucket>();
		if (command.getValueType() == ValueType.INDICATOR) {
			if (command.getFilter() == null || 
					command.getFilter().getRestrictions(DimensionType.Indicator).isEmpty()) {
				Log.error("No indicator filter provided to pivot query");
			}
			
			new PivotQuery(context.getTransaction(), dialect, command, context.getUser().getId())
					.addTo(buckets)
					.queryForTargetValues();
	        
	        new PivotQuery(context.getTransaction(), dialect, command, context.getUser().getId())
					.addTo(buckets)
					.queryForSumAndAverages();
	        
	        new PivotQuery(context.getTransaction(), dialect, command, context.getUser().getId())
					.addTo(buckets)
					.callbackTo(callback)
					.queryForSiteCountIndicators();

        } else {
	        new PivotQuery(context.getTransaction(), dialect, command, context.getUser().getId())
					.addTo(buckets)
					.callbackTo(callback)
					.queryForTotalSiteCounts();
        }
	}
}
