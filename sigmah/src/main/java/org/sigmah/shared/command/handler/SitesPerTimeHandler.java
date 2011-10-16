package org.sigmah.shared.command.handler;

import java.util.List;

import org.sigmah.shared.command.SitesPerTime;
import org.sigmah.shared.command.SitesPerTime.SitesPerTimeResult;
import org.sigmah.shared.command.SitesPerTime.SitesPerTimeResult.MonthResult;
import org.sigmah.shared.command.SitesPerTime.SitesPerTimeResult.YearResult;

import com.bedatadriven.rebar.sql.client.SqlResultCallback;
import com.bedatadriven.rebar.sql.client.SqlResultSet;
import com.bedatadriven.rebar.sql.client.SqlResultSetRow;
import com.bedatadriven.rebar.sql.client.SqlTransaction;
import com.google.common.collect.Lists;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class SitesPerTimeHandler implements CommandHandlerAsync<SitesPerTime, SitesPerTimeResult>{
	/*
	 * select year(date2), month(date2), count(*) from site  group by year(date2), month(date2)  
	 * @see org.sigmah.shared.command.handler.CommandHandlerAsync#execute(org.sigmah.shared.command.Command, org.sigmah.shared.command.handler.ExecutionContext, com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	@Override
	public void execute(SitesPerTime command, ExecutionContext context, final AsyncCallback<SitesPerTimeResult> callback) {
		context.getTransaction().executeSql("Select year(date2) as y, month(date2) as m, count(*) as c from site where site.activityid=" + command.getActivityId() + " and dateDeleted is null group by y, m order by y desc, m desc", new SqlResultCallback() {
			@Override
			public void onSuccess(SqlTransaction tx, SqlResultSet results) {
				List<YearResult> years = Lists.newArrayList();
				for (SqlResultSetRow row : results.getRows()) {
					if (!row.isNull("y") && !row.isNull("m")) {
						int year = row.getInt("y");
						int month= row.getInt("m");
						int amountSites = row.getInt("c");
						
						YearResult yearResult = byYear(year, years);
						if (yearResult == null) {
							yearResult = new YearResult(year);
							years.add(yearResult);
						}
						yearResult.addMonth(new MonthResult(month, amountSites)); 
					}
				}
				callback.onSuccess(new SitesPerTimeResult(years));
			}
			
			private YearResult byYear(int year, List<YearResult> years) {
				for (YearResult yearResult: years) {
					if (year == yearResult.getYear()) {
						return yearResult;
					}
				}
				return null;
			}
		});
	}
}
