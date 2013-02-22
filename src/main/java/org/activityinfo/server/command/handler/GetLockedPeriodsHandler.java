package org.activityinfo.server.command.handler;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import java.util.List;

import org.activityinfo.client.Log;
import org.activityinfo.shared.command.GetLockedPeriods;
import org.activityinfo.shared.command.GetLockedPeriods.LockedPeriodsResult;
import org.activityinfo.shared.command.handler.CommandHandlerAsync;
import org.activityinfo.shared.command.handler.ExecutionContext;
import org.activityinfo.shared.dto.ActivityDTO;
import org.activityinfo.shared.dto.LockedPeriodDTO;
import org.activityinfo.shared.dto.ProjectDTO;
import org.activityinfo.shared.dto.UserDatabaseDTO;

import com.bedatadriven.rebar.sql.client.SqlResultCallback;
import com.bedatadriven.rebar.sql.client.SqlResultSet;
import com.bedatadriven.rebar.sql.client.SqlResultSetRow;
import com.bedatadriven.rebar.sql.client.SqlTransaction;
import com.bedatadriven.rebar.sql.client.query.SqlQuery;
import com.bedatadriven.rebar.sql.client.util.RowHandler;
import com.google.common.collect.Lists;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class GetLockedPeriodsHandler implements
    CommandHandlerAsync<GetLockedPeriods, LockedPeriodsResult> {

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
