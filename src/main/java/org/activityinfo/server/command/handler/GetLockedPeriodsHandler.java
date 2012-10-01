package org.activityinfo.server.command.handler;

import java.util.List;

import org.activityinfo.shared.command.GetLockedPeriods;
import org.activityinfo.shared.command.GetLockedPeriods.LockedPeriodsResult;
import org.activityinfo.shared.command.handler.CommandHandlerAsync;
import org.activityinfo.shared.command.handler.ExecutionContext;
import org.activityinfo.shared.dto.ActivityDTO;
import org.activityinfo.shared.dto.LockedPeriodDTO;
import org.activityinfo.shared.dto.ProjectDTO;
import org.activityinfo.shared.dto.UserDatabaseDTO;

import org.activityinfo.client.Log;
import com.bedatadriven.rebar.sql.client.SqlResultCallback;
import com.bedatadriven.rebar.sql.client.SqlResultSet;
import com.bedatadriven.rebar.sql.client.SqlResultSetRow;
import com.bedatadriven.rebar.sql.client.SqlTransaction;
import com.bedatadriven.rebar.sql.client.query.SqlQuery;
import com.bedatadriven.rebar.sql.client.util.RowHandler;
import com.google.common.collect.Lists;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class GetLockedPeriodsHandler implements CommandHandlerAsync<GetLockedPeriods, LockedPeriodsResult>{

	@Override
	public void execute(GetLockedPeriods command, ExecutionContext context,
			final AsyncCallback<LockedPeriodsResult> callback) {
		
		final List<Integer> projectIds = Lists.newArrayList();
		final List<Integer> activityIds = Lists.newArrayList();
		final List<LockedPeriodDTO> lockedPeriods = Lists.newArrayList();

		SqlQuery.select("ProjectId")
				.from("project")
				.where("DatabaseId")
				.equalTo(command.getDatabaseId())
				.execute(context.getTransaction(), new RowHandler() {
					@Override
					public void handleRow(SqlResultSetRow row) {
						projectIds.add(row.getInt("ProjectId"));
					}
		});
		
		SqlQuery.select("ActivityId")
				.from("activity")
				.where("DatabaseId")
				.equalTo(command.getDatabaseId())
				.execute(context.getTransaction(), new RowHandler() {
					@Override
					public void handleRow(SqlResultSetRow row) {
						activityIds.add(row.getInt("ActivityId"));
					}
		});
		
		
		// TODO(ruud): load only what is visible to user 
		SqlQuery.select("fromDate", "toDate", "enabled", "name",
				"lockedPeriodId", "userDatabaseId", "activityId",
				"projectId")
				.from("lockedperiod")
					.where("ActivityId")
					.in(activityIds)
				.or()
					.where("ProjectId")
					.in(projectIds)
				.or()
					.where("UserDatabaseId")
					.equalTo(command.getDatabaseId())

		.execute(context.getTransaction(), new SqlResultCallback() {
			@Override
			public void onSuccess(SqlTransaction tx, SqlResultSet results) {
				UserDatabaseDTO db = new UserDatabaseDTO();
				ActivityDTO activity = new ActivityDTO();
				ProjectDTO project = new ProjectDTO();
				
				for (SqlResultSetRow row : results.getRows()) {
					LockedPeriodDTO lockedPeriod = new LockedPeriodDTO();

					lockedPeriod.setFromDate(row.getDate("fromDate"));
					lockedPeriod.setToDate(row.getDate("toDate"));
					lockedPeriod.setEnabled(row.getBoolean("enabled"));
					lockedPeriod.setName(row.getString("name"));
					lockedPeriod.setId(row.getInt("lockedPeriodId"));

					boolean parentFound = false;

					if (!row.isNull("activityId")) {
						Integer activityId = row.getInt("activityId");
						lockedPeriod.setParentId(activityId);
						lockedPeriod.setParentType(activity.getEntityName());
						parentFound = true;
					}
					if (!row.isNull("userDatabaseId")) {
						Integer databaseId = row.getInt("userDatabaseId");
						lockedPeriod.setParentId(databaseId);
						lockedPeriod.setParentType(db.getEntityName());
						parentFound = true;
					}
					if (!row.isNull("projectID")) {
						Integer projectId = row.getInt("projectId");
						lockedPeriod.setParentId(projectId);
						lockedPeriod.setParentType(project.getEntityName());
						parentFound = true;
					}

					if (!parentFound) {
						Log.debug("Orphan lockedPeriod: No parent (UserDatabase/Activity/Project) found for LockedPeriod with Id="
								+ lockedPeriod.getId());
					}
					lockedPeriods.add(lockedPeriod);
				}
				
				callback.onSuccess(new LockedPeriodsResult(lockedPeriods));
			}
		});
		
	}

}
